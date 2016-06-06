package ru.vlitomsk.oraclient.ctl;

import ru.vlitomsk.oraclient.model.AppModel;
import ru.vlitomsk.oraclient.model.ConnException;

import java.sql.SQLException;

/**
 * Created by vas on 03.06.2016.
 */
public class AppCtl {
    private AppModel model;
    private ActiveTableCtl activeTableCtl;
    public AppCtl(AppModel model) {
        this.model = model;
        activeTableCtl = new ActiveTableCtl(model.getActiveTableModel());
    }
    public void newConnection(String serv, String login, String pass, String listener) throws ConnException,SQLException {
        model.newConnection(serv, login, pass, listener);
    }

    public void disconnect() throws ConnException,SQLException {
        model.disconnect();
    }

    public void sqlQuery(String query) throws  SQLException {
        activeTableCtl.setActiveQueried(query);
    }

    public ActiveTableCtl getActiveTableCtl() {
        return activeTableCtl;
    }
}
