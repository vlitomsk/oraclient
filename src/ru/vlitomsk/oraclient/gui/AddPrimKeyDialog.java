package ru.vlitomsk.oraclient.gui;

import ru.vlitomsk.oraclient.ctl.ActiveTableCtl;
import ru.vlitomsk.oraclient.gui.components.JLabEdit;
import ru.vlitomsk.oraclient.gui.components.JOkCancel;

import javax.swing.*;
import java.awt.*;


/**
 * Created by vas on 08.06.2016.
 */
public class AddPrimKeyDialog extends JDialog {
    private JLabEdit constrEdit = new JLabEdit("Constraint: ");
    private JLabEdit cols = new JLabEdit("Space-sep. cols: ");
    private JLabEdit reftbl = new JLabEdit("Referenced table: ");
    private JLabEdit refs = new JLabEdit("Space-sep. refs: ");
    private boolean okay = false;
    public AddPrimKeyDialog(Frame owner) {
        super(owner, true);
        setTitle("Add PK dialog");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(constrEdit);
        add(cols);
        //add(reftbl);
        //add(refs);
        JOkCancel buttons = new JOkCancel();
        add(buttons);
        buttons.addOkListener((e) -> {
            okay = true;
            dispose();
        });
        buttons.addCancelListener((e) -> dispose());

        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    public boolean isOkay() {
        return okay;
    }

    public String getConstraint() {
        return constrEdit.getText();
    }

    public String[] getCols() {
        return cols.getText().split("\\s+");
    }

    public String getReftable() {
        return reftbl.getText();
    }

    public String[] getRefs() {
        return refs.getText().split("\\s+");
    }
}
