package ru.vlitomsk.oraclient.gui;

import ru.vlitomsk.oraclient.gui.components.JAlterComp;
import ru.vlitomsk.oraclient.gui.components.JOkCancel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by vas on 08.06.2016.
 */
public class AlterDialog extends JDialog {
    private JAlterComp acomp;
    private boolean okay;
    public AlterDialog(Frame owner, Class alterClass, String title) {
        super(owner,true);
        setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        this.acomp = acomp;
        setTitle(title);

        JOkCancel buttons = new JOkCancel();
        buttons.addOkListener((e) -> {
            okay = true;
            dispose();
        });
        buttons.addCancelListener((e) -> dispose());

        try {
            add(acomp=(JAlterComp)alterClass.newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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
