package ru.vlitomsk.oraclient.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by vas on 03.06.2016.
 */
public class ActiveTableModel extends Observable {
    private DBConn connection;
    private String activeTblName;
    private ResultSet lastRs;
    private Set<Integer> rmRowSet = new TreeSet<>();
    private int selectCount = 0;
    private int insertCount = 0;
    private int columns = 0;

    public String getActiveTblName() {
        return activeTblName;
    }

    class Change {
        int col;
        String val;

        public Change(int col, String val) {
            this.col = col;
            this.val = val;
        }
    }
    private List<HashMap<Integer,String>> rowChanges = new ArrayList<>();

    private void resetSet() throws  SQLException{
        rmRowSet.clear();
        rowChanges.clear();
        selectCount = 0;
        insertCount = 0;
//        lastRs.beforeFirst();
   //     lastRs.moveToCurrentRow();
        if (lastRs == null)
            return;
        columns = lastRs.getMetaData().getColumnCount();
        while (lastRs.next()) {
            ++selectCount;
        }
        lastRs.beforeFirst();
    }

    public ActiveTableModel(DBConn connection) {
        this.connection = connection;
    }

    public void newConnection(DBConn connection) {
        this.connection = connection;
        setChanged();
        notifyObservers(new ActiveTableUpdate(null, null, false, null, null));
    }

    private List<PKey> pkeys = new ArrayList<>();
    private List<FKey> fkeys = new ArrayList<>();

    private void reloadKeys() throws  SQLException {
        ResultSet pkset = connection.getDBMetaData().getPrimaryKeys(null, null, activeTblName.toUpperCase());
        pkeys.clear();
        fkeys.clear();
        while (pkset.next()) {
            pkeys.add(new PKey(pkset.getString("COLUMN_NAME"), pkset.getString("PK_NAME")));
        }
        pkset.close();
        ResultSet fkset = connection.getDBMetaData().getImportedKeys(null, null, activeTblName.toUpperCase());
        while (fkset.next()) {
            FKey fk;
            fk = new FKey(fkset.getString("PKTABLE_NAME"), fkset.getString("PKCOLUMN_NAME"), fkset.getString("FK_NAME"));
            String query = "SELECT " + fk.columnName + " FROM " + fk.table;
            ResultSet availfk = connection.sqlSelectQuery(query);
            while (availfk.next()) {
                fk.addAvail(availfk.getString(1));
            }
            fkeys.add(fk);
            availfk.close();
        }
        fkset.close();
        setChanged();
        notifyObservers(new KeysUpdate(pkeys, fkeys));
    }

    public void setActive(String tblName) throws SQLException {
        if (connection == null)
            return;
        activeTblName = tblName;
        if (lastRs != null)
            lastRs.close();
        lastRs = connection.sqlSelectQuery("SELECT " + tblName + ".* FROM " + tblName);
        reloadKeys();
        resetSet();
        setChanged();
        notifyObservers(new ActiveTableUpdate(lastRs, tblName, true, pkeys, fkeys));
    }

    public void setActiveQueried(String sqlQuery) throws SQLException {
        if (connection == null)
            throw new SQLException("You need to connect!");

        boolean hasRs = connection.sqlQuery(sqlQuery);
        pkeys.clear();
        fkeys.clear();
        resetSet();

        if (hasRs) {
            if (lastRs != null)
                lastRs.close();
            lastRs = connection.getResultSet();
            setChanged();
            notifyObservers(new ActiveTableUpdate(lastRs, "SQL query result", false, pkeys, fkeys));
        }
        setChanged();
        notifyObservers(new TableNamesUpdate(connection.getTableNames()));
    }

    public void disconnect() {
        this.connection = null;
        setChanged();
        notifyObservers(new ActiveTableUpdate(null, null, false, null, null));
    }

    public void refresh() throws SQLException {
        if (activeTblName != null)
            setActive(activeTblName);
    }

    public void toggleRemove(int[] indices) {
        ToggleRemoveUpdate upd = new ToggleRemoveUpdate();
        upd.indices = indices;
        upd.toremove = new boolean[indices.length];
        int j = 0;
        for (int i : indices) {
            upd.toremove[j] = !rmRowSet.contains(i);
            if (!upd.toremove[j])
                rmRowSet.remove(i);
            else
                rmRowSet.add(i);
            ++j;
        }
        setChanged();
        notifyObservers(upd);
    }

    public void writeChanges() throws SQLException {
        lastRs.beforeFirst();
        //lastRs.moveToCurrentRow();
        Integer[] rmIdxArr = new Integer[rmRowSet.size()];
        rmRowSet.toArray(rmIdxArr);
        rmRowSet.clear();
        Arrays.sort(rmIdxArr);
        int idx = 0;
        int rmidx = 0;
        boolean insertmode = idx == selectCount;
        if (idx == selectCount) {
            lastRs.moveToInsertRow();
            insertmode = true;
        }
        while ((!insertmode && lastRs.next()) || (insertmode && idx < rowChanges.size())) {
            if (rmidx < rmIdxArr.length && idx == rmIdxArr[rmidx]) {
                ++rmidx;
                if (!insertmode)
                    lastRs.deleteRow();
            } else if (idx < rowChanges.size() && !rowChanges.get(idx).entrySet().isEmpty()) {
                for (Map.Entry<Integer,String> ent : rowChanges.get(idx).entrySet()) {
                    lastRs.updateString(ent.getKey(), ent.getValue());
                }

                if (!insertmode) {
                    lastRs.updateRow();
                } else {
                    lastRs.insertRow();
                }
            }
            ++idx;
            if (idx == selectCount) {
                lastRs.moveToInsertRow();
                insertmode = true;
            }
        }

        refresh();
    }

    public void valueChanged(int r, int c, String valueAt) {
        ++r; ++c;
        for (int left = r - rowChanges.size(); left > 0; --left) {
            rowChanges.add(new HashMap<>());
        }
        rowChanges.get(r-1).put(c, valueAt);
        setChanged();
        notifyObservers(new CellChangeUpdate(r-1, c-1, valueAt));
    }

    public void addRow() {
        setChanged();
        notifyObservers(new AddRowUpdate());
        for (int c = 0; c < columns; ++c)
            valueChanged(selectCount+insertCount, c, null);
        ++insertCount;
    }
}
