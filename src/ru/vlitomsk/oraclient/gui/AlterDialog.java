package ru.vlitomsk.oraclient.gui;

import ru.vlitomsk.oraclient.gui.components.JOkCancel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by vas on 08.06.2016.
 */
public class AlterDialog extends JDialog {
    private Component acomp;
    private boolean okay;
    public AlterDialog(Frame owner, Component acomp, String title) {
        super(owner,true);
        setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        setTitle(title);

        JOkCancel buttons = new JOkCancel();
        buttons.addOkListener((e) -> {
            okay = true;
            dispose();
        });
        buttons.addCancelListener((e) -> dispose());

        add(this.acomp = acomp);
        add(buttons);

        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    public boolean isOkay() {
        return okay;
    }

    @Override
    public String toString() {
        return !okay ? null : acomp.toString();
    }
}
