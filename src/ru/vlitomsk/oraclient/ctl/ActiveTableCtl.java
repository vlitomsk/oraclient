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

}
