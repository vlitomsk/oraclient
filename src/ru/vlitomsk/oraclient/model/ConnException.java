package ru.vlitomsk.oraclient.model;

/**
 * Created by vas on 03.06.2016.
 */
public class ConnException extends Exception {
    private String s;
    public ConnException(String s) {
        this.s = s;
    }
    @Override
    public String toString() {
        return s;
    }
}
