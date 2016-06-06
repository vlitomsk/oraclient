package ru.vlitomsk.oraclient.model;

import java.util.List;

/**
 * Created by vas on 03.06.2016.
 */
public class TableNamesUpdate {
    private List<String> tableNames;

    public TableNamesUpdate(List<String> tableNames) {
        this.tableNames = tableNames;
    }

    public List<String> getTableNames() {
        return tableNames;
    }
}
