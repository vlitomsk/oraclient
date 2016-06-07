package ru.vlitomsk.oraclient.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vas on 08.06.2016.
 */
public class FKey {
    public String table, columnName;
    public List<String> availValues = new ArrayList<>();

    public String getTable() {
        return table;
    }

    public String getColumnName() {
        return columnName;
    }

    public FKey(String table, String columnName) {

        this.table = table;
        this.columnName = columnName;
    }

    public void addAvail(String string) {
        availValues.add(string);
    }
}
