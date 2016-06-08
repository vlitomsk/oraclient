package ru.vlitomsk.oraclient.gui.components;

import javax.swing.*;
import java.awt.*;

/**
 * Created by vas on 08.06.2016.
 */
public class DropTableDialog extends JDialog {
    private JLabEdit tname = new JLabEdit("Table name:", 20);
    private boolean okay = false;
    public DropTableDialog(Frame owner) {
        super(owner,true);
        setTitle("Drop table");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JOkCancel buttons = new JOkCancel();
        add(tname);
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

    @Override
    public String toString() {
        return "DROP TABLE " + tname.getText();
    }
}
