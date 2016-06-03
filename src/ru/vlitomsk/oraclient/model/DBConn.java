package ru.vlitomsk.oraclient.model;

import java.sql.SQLException;

/**
 * Created by vas on 03.06.2016.
 */
public interface DBConn {
    public void close() throws SQLException;
}
