package ru.vlitomsk.oraclient.model;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by vas on 03.06.2016.
 */
public interface DBConn {
    public void close() throws SQLException;
    public List<String> getTableNames() throws SQLException;
    public DatabaseMetaData getDBMetaData() throws SQLException;
    ResultSet sqlSelectQuery(String query) throws SQLException;
    boolean sqlQuery(String query) throws SQLException;
    ResultSet getResultSet() throws SQLException;
}
