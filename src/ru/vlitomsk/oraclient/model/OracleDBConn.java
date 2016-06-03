package ru.vlitomsk.oraclient.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by vas on 03.06.2016.
 */
public class OracleDBConn implements DBConn {
    private Connection connection;
    public OracleDBConn(String serv, String login, String pass, String listener) throws ConnException,SQLException {
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
}
