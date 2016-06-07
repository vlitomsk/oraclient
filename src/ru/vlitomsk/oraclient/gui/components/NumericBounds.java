package ru.vlitomsk.oraclient.gui.components;

import javax.swing.*;

/**
 * Created by vas on 08.06.2016.
 */
class NumericBounds extends FJPanel {
    private JTextField fld1 = new JTextField(4);
    private JTextField fld2 = new JTextField(4);

    public NumericBounds() {
        add(new JLabel("("));
        add(fld1);
        add(new JLabel(", "));
        add(fld2);
        add(new JLabel(")"));
    }

    private boolean empty(JTextField fld) {
        return fld.getText().trim().length() == 0;
    }

    @Override
    public String toString() {
        boolean b1 = empty(fld1), b2 = empty(fld2);
        if (!b1 && b2)
            return "(" + fld1.getText() + ")";
        if (b1 && !b2)
            return "(" + fld2.getText() + ")";
        if (!b1 && !b2)
            return "(" + fld1.getText() + "," + fld2.getText() + ")";
        return "";
    }
}
