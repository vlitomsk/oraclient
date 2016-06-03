package ru.vlitomsk.oraclient.gui.components;

import javax.swing.*;

/**
 * Created by vas on 03.06.2016.
 */
public class JLabEdit extends JPanel {
    private JTextField fld;
    public JLabEdit(String lbl) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        init(lbl, "");
    }

    public JLabEdit(String lbl, String defaultValue) {
        super();
        init(lbl, defaultValue);
    }

    private void init(String lbl, String defaultValue) {
        add(new JLabel(lbl));
        add(fld = new JTextField(defaultValue));
        fld.setColumns(20);
    }

    public String getText() {
        return fld.getText();
    }
}
