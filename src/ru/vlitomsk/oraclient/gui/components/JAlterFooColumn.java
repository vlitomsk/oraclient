package ru.vlitomsk.oraclient.gui.components;

import javax.swing.*;

/**
 * Created by vas on 08.06.2016.
 */
public class JAlterFooColumn extends JPanel {
    private final JCreateDef cdef;
    private JLabEdit jafter = new JLabEdit("AFTER ");
    private JCheckBox chkFirst;
    private String foo;
    public JAlterFooColumn(String foo) {
        this.foo=foo;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel afterwhat = new JPanel();
        afterwhat.setLayout(new BoxLayout(afterwhat, BoxLayout.X_AXIS));
        chkFirst = new JCheckBox("FIRST");
        chkFirst.setSelected(false);
        afterwhat.add(chkFirst);
        afterwhat.add(jafter);
        chkFirst.addActionListener(e -> {
            jafter.setEnabled(!chkFirst.isSelected());
        });
        add(cdef = new JCreateDef());
    }

    @Override
    public String toString() {
        return " " + foo + " " + cdef.toString();
    }
}
