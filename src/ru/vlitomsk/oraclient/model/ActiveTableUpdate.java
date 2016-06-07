package ru.vlitomsk.oraclient.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vas on 03.06.2016.
 */
public class ActiveTableUpdate {
    private ResultSet rs;
    private String title;
    private boolean editable;
    private List<PKey> pkeys ;
    private List<FKey> fkeys ;

    public ActiveTableUpdate(ResultSet rs, String title, boolean editable, List<PKey> pkeys, List<FKey> fkeys) {
        this.rs = rs;
        this.title = title;
        this.editable = editable;
        this.pkeys = pkeys;
        this.fkeys = fkeys;
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

    public List<PKey> getPkeys() {
        return pkeys;
    }

    public List<FKey> getFkeys() {
        return fkeys;
    }
}

