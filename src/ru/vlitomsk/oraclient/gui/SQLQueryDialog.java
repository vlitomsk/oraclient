package ru.vlitomsk.oraclient.gui;

import ru.vlitomsk.oraclient.gui.components.JOkCancel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by vas on 03.06.2016.
 */
public class SQLQueryDialog extends JDialog {
    private boolean okay = false;
    private JTextArea sqlArea;
    public SQLQueryDialog(Frame owner, String lastSql) {
        super(owner, true);
        setTitle(Strings.QUERY_DLG_TITLE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JPanel inPanel = new JPanel();
        inPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        inPanel.add(new JLabel(Strings.QUERY_DLG_INPUT), BorderLayout.WEST);
        JScrollPane jsp = new JScrollPane(sqlArea = new JTextArea(20, 80));
        sqlArea.setLineWrap(true);
        sqlArea.setText(lastSql);
        sqlArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        inPanel.add(jsp, BorderLayout.EAST);
        add(inPanel);
        JOkCancel buttons = new JOkCancel();
        add(buttons);
        buttons.addOkListener((e) -> {
            okay = true;
            dispose();
        });
        buttons.addCancelListener((e) -> dispose());

        pack();
        setResizable(false);
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    public boolean isOkay() {
        return okay;
    }

    public String sr(String s) {
        return okay ? s : null;
    }

    public String getQuery() {
        return sr(sqlArea.getText());
    }
}
