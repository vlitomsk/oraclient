package ru.vlitomsk.oraclient.gui;

import ru.vlitomsk.oraclient.gui.components.JScrolledList;

/**
 * Created by vas on 03.06.2016.
 */
public class TablesListView extends JScrolledList<String> {
    public TablesListView(String[] initList) {
        super(initList);
    }

    public TablesListView() {
        super(new String[]{"test string 1", "test string 2"});
    }
}
