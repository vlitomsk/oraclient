package ru.vlitomsk.oraclient.gui.components;

import javax.swing.*;

/**
 * Created by vas on 08.06.2016.
 */
public class JAlterDropColumn extends JLabEdit {
    public JAlterDropColumn() {
        super("Column:");
    }

    @Override
    public String toString() {
        return "DROP COLUMN " + getText();
    }
}
