package ru.vlitomsk.oraclient.ctl;

import ru.vlitomsk.oraclient.model.ActiveTableModel;

import java.sql.SQLException;

/**
 * Created by vas on 03.06.2016.
 */
public class ActiveTableCtl {
    private ActiveTableModel model;

    public ActiveTableCtl(ActiveTableModel model) {
        this.model = model;
    }

    public void setActiveTable(String tblName) throws SQLException {
        model.setActive(tblName);
    }

    public void setActiveQueried(String query) throws SQLException {
        model.setActiveQueried(query);
    }

    public void toggleRemove(int[] indices) {
        model.toggleRemove(indices);
    }

    public void writeChanges() throws SQLException {
        model.writeChanges();
    }

    public void valueChanged(int r, int c, String valueAt) {
        model.valueChanged(r, c, valueAt);
    }

    public void addRow() {
        model.addRow();
    }

    private String joinstr(String[] arr, String sep) {
        String res = "";
        for (int i = 0; i < arr.length - 1; ++i)
            res += arr[i] + sep;
        if (arr.length != 0)
            res += arr[arr.length - 1];
        return res;
    }

    public void addpk(String constname, String[] columns) throws SQLException {
        model.setActiveQueried("alter table " + model.getActiveTblName() + " add constraint " + constname +
         " primary key (" + joinstr(columns,",") + ")");
        model.refresh();
    }

    public void addfk(String constname, String[] columns, String tbl, String[] refcolumns) throws SQLException {
        model.setActiveQueried("alter table " + model.getActiveTblName() + " add constraint " + constname +
         " foreign key (" + joinstr(columns, ",") + ") references " + tbl + "(" + joinstr(refcolumns, ",") + ")");
        model.refresh();
    }

    public void dropconst(String constName) throws SQLException {
        model.setActiveQueried("alter table " + model.getActiveTblName() + " drop constraint " + constName);
        model.refresh();
    }
}
