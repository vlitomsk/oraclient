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
}
