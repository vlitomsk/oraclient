package ru.vlitomsk.oraclient.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by vas on 03.06.2016.
 */
public class AppModel extends Observable {
    private DBConn connection;
    private ActiveTableModel activeTableModel = new ActiveTableModel(null);
    public void newConnection(String serv, String login, String pass, String listener) throws ConnException,SQLException {
        if (connection != null)
            throw new ConnException("Connections has already established");
        connection = new OracleDBConn(serv, login, pass, listener);
        activeTableModel.newConnection(connection);
        setChanged();
        notifyObservers(new ConnectedUpdate());
        tablesChanged();
    }

    public ActiveTableModel getActiveTableModel() {
        return activeTableModel;
    }

    private static final List<String> emptyList = new ArrayList<>();
    private void tablesChanged() throws SQLException {
        setChanged();
        TableNamesUpdate arg = new TableNamesUpdate(connection != null ? connection.getTableNames() : emptyList) ;
        notifyObservers(arg);
        activeTableModel.refresh();
    }

    public void disconnect() throws ConnException,SQLException {
        if (connection == null)
            return;
        DBConn oldcon = connection;
        connection = null;
        oldcon.close();
        activeTableModel.disconnect();

        setChanged();
        notifyObservers(new DisconnUpdate());
        tablesChanged();
    }
}
