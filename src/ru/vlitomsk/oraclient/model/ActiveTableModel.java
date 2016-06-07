package ru.vlitomsk.oraclient.model;

import java.sql.DatabaseMetaData;
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
        lastRs.beforeFirst();
        lastRs.moveToCurrentRow();
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
        notifyObservers(new ActiveTableUpdate(null, null, false));
    }

    public void setActive(String tblName) throws SQLException {
        if (connection == null)
            return;
        activeTblName = tblName;
        lastRs = connection.sqlQuery("SELECT " + tblName + ".* FROM " + tblName);
        resetSet();
        setChanged();
        notifyObservers(new ActiveTableUpdate(lastRs, tblName, true));
    }

    public void setActiveQueried(String sqlQuery) throws SQLException {
        if (connection == null)
            throw new SQLException("You need to connect!");
        lastRs = connection.sqlQuery(sqlQuery);
        resetSet();
        setChanged();
        notifyObservers(new TableNamesUpdate(connection.getTableNames()));
        setChanged();
        notifyObservers(new ActiveTableUpdate(lastRs, "SQL query result", false));
    }

    public void disconnect() {
        this.connection = null;
        setChanged();
        notifyObservers(new ActiveTableUpdate(null, null, false));
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
        while ((lastRs.next()) || (insertmode && idx < rowChanges.size())) {
            if (idx == selectCount) {
                lastRs.moveToInsertRow();
                insertmode = true;
            }
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
