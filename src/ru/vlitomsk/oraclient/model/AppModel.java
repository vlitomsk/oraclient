package ru.vlitomsk.oraclient.model;

import java.sql.SQLException;
import java.util.Observable;

/**
 * Created by vas on 03.06.2016.
 */
public class AppModel extends Observable {
    private DBConn connection;
    public void newConnection(String serv, String login, String pass, String listener) throws ConnException,SQLException {
        if (connection != null)
            throw new ConnException("Connections has already established");
        connection = new OracleDBConn(serv, login, pass, listener);

        setChanged();
        notifyObservers(new NewConnUpdate());
    }

    public void disconnect() throws ConnException,SQLException {
        if (connection == null)
            return;
        DBConn oldcon = connection;
        connection = null;
        oldcon.close();

        setChanged();
        notifyObservers(new DisconnUpdate());
    }
}
