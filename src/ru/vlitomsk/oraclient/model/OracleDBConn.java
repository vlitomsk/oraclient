package ru.vlitomsk.oraclient.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vas on 03.06.2016.
 */
public class OracleDBConn implements DBConn {
    private Connection connection;
    private String serv, login, pass, listener;
    public OracleDBConn(String serv, String login, String pass, String listener) throws ConnException,SQLException {
        this.serv = serv;
        this.login = login.toUpperCase();
        this.pass = pass;
        this.listener = listener;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ConnException("Where is your Oracle JDBC Driver?");
        }

        connection = DriverManager.getConnection("jdbc:oracle:thin:@" + serv + ":1521:" + listener, login, pass);
        System.out.println("Connected!");
    }

    @Override
    public void close() throws SQLException {
        connection.close();
        System.out.println("Disconnected!");
    }

    @Override
    public List<String> getTableNames() throws SQLException {
        DatabaseMetaData dmd = connection.getMetaData();
        ResultSet resultSet = dmd.getTables(null, this.login, "%", new String[]{"TABLE"});
        List<String> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(resultSet.getString(3));
        }
        return result;
    }

    @Override
    public DatabaseMetaData getDBMetaData() throws SQLException {
        return connection.getMetaData();
    }

    @Override
    public ResultSet sqlSelectQuery(String query) throws SQLException {
        if (connection == null)
            return null;
        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        return stmt.executeQuery(query);
    }

    private Statement qryStatement ;
    @Override
    public boolean sqlQuery(String query) throws SQLException {
        if (connection == null)
            return false;
        qryStatement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        return qryStatement.execute(query);
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return qryStatement.getResultSet();
    }
}
