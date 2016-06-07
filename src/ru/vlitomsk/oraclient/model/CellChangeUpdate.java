package ru.vlitomsk.oraclient.model;

/**
 * Created by vas on 06.06.2016.
 */
public class CellChangeUpdate {
    public int row, col;
    public String value;

    public CellChangeUpdate(int row, int col, String value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }
}
