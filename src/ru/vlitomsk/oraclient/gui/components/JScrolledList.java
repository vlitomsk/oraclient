package ru.vlitomsk.oraclient.gui.components;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.util.List;
import java.util.Vector;

/**
 * Created by vas on 03.06.2016.
 */
public class JScrolledList<Tp> extends JScrollPane {
    protected JList<Tp> list;

    public JScrolledList(Tp[] initList) {
        super();
        init(initList);
    }

    private void init(Tp[] initList) {
        list = new JList<>(initList);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        setViewportView(list);
    }

    public void setList(List<Tp> vals) {
        list.setListData(new Vector<Tp>(vals));
        list.setSelectedIndex(-1);
    }

    public Tp getSelectedValue() {
        return list.getSelectedValue();
    }

    public void addListSelectionListener(ListSelectionListener listener) {
        list.addListSelectionListener(listener);
    }
}
