package ru.vlitomsk.oraclient.model;

/**
 * Created by vas on 08.06.2016.
 */
public class PKey {
    public String columnName, constraint;

    public PKey(String columnName, String constraint) {
        this.columnName = columnName;
        this.constraint = constraint;
    }
}
