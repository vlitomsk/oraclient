package ru.vlitomsk.oraclient.gui.components;

import javax.swing.*;

/**
 * Created by vas on 08.06.2016.
 */
class CharBounds extends FJPanel {
    private JTextField fld1 = new JTextField(4);

    public CharBounds() {
        add(new JLabel("("));
        add(fld1);
        add(new JLabel(")"));
    }

    @Override
    public String toString() {
        return "(" + fld1.getText() + ")";
    }
}
