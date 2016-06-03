package ru.vlitomsk.oraclient.gui.components;

import ru.vlitomsk.oraclient.gui.Strings;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Created by vas on 03.06.2016.
 */
public class JOkCancel extends JPanel {
    private JButton ok, cancel;
    public JOkCancel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(ok = new JButton(Strings.DLG_OK));
        add(cancel = new JButton(Strings.DLG_CANCEL));
    }

    public void addOkListener(ActionListener al) {
        ok.addActionListener(al);
    }

    public void addCancelListener(ActionListener al) {
        cancel.addActionListener(al);
    }
}
