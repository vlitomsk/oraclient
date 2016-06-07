package ru.vlitomsk.oraclient.gui.components;

import javax.swing.*;
import java.awt.*;

/**
 * Created by vas on 08.06.2016.
 */
public class JCreateDef extends JPanel {
    private JLabEdit fld = new JLabEdit("Column:", 15);
    private JSQLType jsqlType = new JSQLType();
    private JCheckBox nullable = new JCheckBox("NULL?");
    public JCreateDef()  {
        setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        add(fld);
        add(jsqlType);
        add(nullable);
    }

    @Override
    public String toString() {
        return fld.getText() + " " + jsqlType.toString() + (nullable.isSelected() ? "" : " NOT NULL");
    }
}
