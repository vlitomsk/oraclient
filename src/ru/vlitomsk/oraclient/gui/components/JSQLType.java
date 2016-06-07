package ru.vlitomsk.oraclient.gui.components;

import javax.swing.*;

/**
 * Created by vas on 08.06.2016.
 */
public class JSQLType extends JPanel {
    private static final String
      tpNumeric, tpChar , tpVarchar, tpDate, tpLong;
    private static final String[] tpStrNames = {
            tpNumeric="NUMERIC",
            tpChar="CHAR",
            tpVarchar="VARCHAR",
            tpDate="DATE",
            tpLong="LONG"
    };
    private static Class[] tpBoundClasses = {
            NumericBounds.class,
            CharBounds.class,
            VarcharBounds.class,
            DateBounds.class,
            LongBounds.class
    };

    private FJPanel boundsPanel;
    private JComboBox<String> comboBox;

    public JSQLType() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        comboBox = new JComboBox<>();
        for (String s : tpStrNames)
            comboBox.addItem(s);
        comboBox.setSelectedIndex(0);
        add(comboBox);
        updBoundsPanel();
        comboBox.addActionListener(e -> {
            updBoundsPanel();
        });
    }

    private void updBoundsPanel() {
        if (boundsPanel != null)
            remove(boundsPanel);
        try {
            boundsPanel = (FJPanel) (tpBoundClasses[comboBox.getSelectedIndex()].newInstance());
            add(boundsPanel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        revalidate();
    }

    @Override
    public String toString() {
        int idx = comboBox.getSelectedIndex();
        return tpStrNames[idx] + " " + boundsPanel.toString();
    }
}
