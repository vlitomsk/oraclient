package ru.vlitomsk.oraclient.model;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by vas on 03.06.2016.
 */
public class ActiveTableModel extends Observable {
    private DBConn connection;
    private String activeTblName;

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
        ResultSet rs = connection.sqlQuery("SELECT " + tblName + ".* FROM " + tblName);
        setChanged();
        notifyObservers(new ActiveTableUpdate(rs, tblName, true));
    }

    public void setActiveQueried(String sqlQuery) throws SQLException {
        if (connection == null)
            throw new SQLException("You need to connect!");
        ResultSet rs = connection.sqlQuery(sqlQuery);
        setChanged();
        notifyObservers(new TableNamesUpdate(connection.getTableNames()));
        setChanged();
        notifyObservers(new ActiveTableUpdate(rs, "SQL query result", false));
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
}
