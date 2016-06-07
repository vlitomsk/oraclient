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
        init(lbl, "", 20);
    }
    public JLabEdit(String lbl, int cols) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        init(lbl, "", cols);
    }

    public JLabEdit(String lbl, String defaultValue) {
        super();
        init(lbl, defaultValue, 20);
    }

    private JLabel lab ;
    private void init(String lbl, String defaultValue, int cols) {
        add(this.lab = new JLabel(lbl));
        add(fld = new JTextField(defaultValue));
        fld.setColumns(cols);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        fld.setEnabled(enabled);
        lab.setEnabled(enabled);
    }

    public String getText() {
        return fld.getText();
    }
}
