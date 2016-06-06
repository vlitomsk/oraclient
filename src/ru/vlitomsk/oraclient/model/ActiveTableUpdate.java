package ru.vlitomsk.oraclient.model;

import java.sql.ResultSet;

/**
 * Created by vas on 03.06.2016.
 */
public class ActiveTableUpdate {
    private ResultSet rs;
    private String title;
    private boolean editable;

    public ActiveTableUpdate(ResultSet rs, String title, boolean editable) {
        this.rs = rs;
        this.title = title;
        this.editable = editable;
    }

    public ResultSet getRs() {
        return rs;
    }

    public String getTitle() {
        return title;
    }

    public boolean isEditable() {
        return editable;
    }
}

