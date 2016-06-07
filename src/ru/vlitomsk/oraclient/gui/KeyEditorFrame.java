package ru.vlitomsk.oraclient.gui;

import ru.vlitomsk.oraclient.ctl.ActiveTableCtl;
import ru.vlitomsk.oraclient.gui.components.JLabEdit;
import ru.vlitomsk.oraclient.gui.components.JOkCancel;
import ru.vlitomsk.oraclient.gui.components.JScrolledList;
import ru.vlitomsk.oraclient.model.FKey;
import ru.vlitomsk.oraclient.model.KeysUpdate;
import ru.vlitomsk.oraclient.model.PKey;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by vas on 08.06.2016.
 */
public class KeyEditorFrame extends JFrame implements Observer {
    private JScrolledList<String> lst;
    private ActiveTableCtl ctl;

    private void showError(String err) {
        JOptionPane.showMessageDialog(this, err, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void tcatch(ExRunnable r) {
        try {
            r.run();
        } catch (Exception ex) {
            showError(ex.toString());
            ex.printStackTrace();
        }
    }
    public KeyEditorFrame(ActiveTableCtl ctl, List<String> constr) {
        this.ctl = ctl;
        setTitle("Keys editor");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(lst = new JScrolledList<String>(new String[]{}));
        lst.setList(constr);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JButton addpk, addfk, rmk;
        panel.add(addpk = new JButton("Add PK.."));
        panel.add(addfk = new JButton("Add FK.."));
        panel.add(rmk = new JButton("Remove key"));
        add(panel);
        addpk.addActionListener(e -> {
            AddPrimKeyDialog dlg = new AddPrimKeyDialog(KeyEditorFrame.this);
            if (dlg.isOkay()) {
                tcatch(()->ctl.addpk(dlg.getConstraint(), dlg.getCols()));
            }
        });
        addfk.addActionListener(e -> {
            AddForeignKeyDialog dlg = new AddForeignKeyDialog(KeyEditorFrame.this);
            if (dlg.isOkay()) {
                tcatch(() -> ctl.addfk(dlg.getConstraint(), dlg.getCols(), dlg.getReftable(), dlg.getRefs()));
            }
            //tcatch(()->ctl.addfk(lst.getSelectedValue()));
        });
        rmk.addActionListener(e -> {
            tcatch(()->ctl.dropconst(lst.getSelectedValue()));
        });
        pack();
        setLocationRelativeTo(null);
        //setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof KeysUpdate) {
            KeysUpdate upd = (KeysUpdate) arg;
            List<String> keys = new ArrayList<>();
            for (FKey fk : upd.fKeys)
                keys.add(fk.constraint);
            for (PKey pk : upd.pkeys)
                keys.add(pk.constraint);
            lst.setList(keys);
        }
    }
}
