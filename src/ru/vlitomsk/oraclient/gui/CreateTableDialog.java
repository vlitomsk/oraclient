package ru.vlitomsk.oraclient.gui;

import ru.vlitomsk.oraclient.gui.components.JCreateDef;
import ru.vlitomsk.oraclient.gui.components.JLabEdit;
import ru.vlitomsk.oraclient.gui.components.JOkCancel;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;

/**
 * Created by vas on 08.06.2016.
 */
public class CreateTableDialog extends JDialog {
    private boolean okay = false;
    private JLabEdit tname = new JLabEdit("Table name:", 20);
    private JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
    private JPanel colEditorsPanel;
    public CreateTableDialog(Frame owner) {
        super(owner,true);
        setTitle("Create table");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel spinPanel = new JPanel(new FlowLayout());
        spinPanel.add(new JLabel("Columns:"));
        spinPanel.add(spinner);
        spinner.addChangeListener(e-> {
            updColEditors();
            pack();
        });

        colEditorsPanel = new JPanel();
        colEditorsPanel.setLayout(new BoxLayout(colEditorsPanel, BoxLayout.Y_AXIS));
        updColEditors();

        add(tname);
        add(spinPanel);
        add(colEditorsPanel);
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

    private List<JCreateDef>  cdefEditors = new ArrayList<>();

    private int edCount = 0;
    private void updColEditors() {
        int delta = (int)(spinner.getValue()) - edCount;
        if (delta < 0) {
            for (int i = edCount-1; i > edCount + delta - 1; --i) {
                colEditorsPanel.remove(cdefEditors.get(i));
                cdefEditors.remove(i);
            }
        } else if (delta > 0) {
            for (int i = edCount-1; i < edCount+delta-1; ++i) {
                cdefEditors.add(new JCreateDef());
                colEditorsPanel.add(cdefEditors.get(i+1));
            }
        }
        edCount = (int)spinner.getValue();
        colEditorsPanel.revalidate();
    }

    private String joinstr(List<JCreateDef> lst, String sep) {
        String res = "";
        for (int i = 0; i < lst.size() - 1; ++i)
            res += lst.get(i).toString() + sep;
        if (!lst.isEmpty())
            res += lst.get(lst.size() - 1).toString();
        return res;
    }

    @Override
    public String toString() {
        return "CREATE TABLE " + tname.getText() + " ( " + joinstr(cdefEditors, ", ") + " ) ";
    }

    public boolean isOkay() {
        return okay;
    }
}
