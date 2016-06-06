package ru.vlitomsk.oraclient.gui;

import ru.vlitomsk.oraclient.ctl.ActiveTableCtl;
import ru.vlitomsk.oraclient.gui.components.JScrolledList;
import ru.vlitomsk.oraclient.model.ActiveTableUpdate;
import ru.vlitomsk.oraclient.model.TableNamesUpdate;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by vas on 03.06.2016.
 */
public class TablesListView extends JScrolledList<String> implements Observer {
    private ActiveTableCtl ctl;
    public TablesListView(ActiveTableCtl ctl, String[] initList) {
        super(initList);
        this.ctl = ctl;
        addListeners();
    }
    public TablesListView(ActiveTableCtl ctl) {
        super(new String[]{});
        this.ctl = ctl;
        addListeners();
    }


    private void addListeners() {
    }

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

    {
        addListSelectionListener(e -> tcatch(()-> {
            if (e.getValueIsAdjusting())
                ctl.setActiveTable(list.getSelectedValue());
        }));
    }

    private List<String> currentNames = new ArrayList<>();

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof TableNamesUpdate) {
            TableNamesUpdate upd = (TableNamesUpdate) arg;
            currentNames = upd.getTableNames();
            setList(currentNames);
        }

        if (arg instanceof ActiveTableUpdate) {
            String tname = ((ActiveTableUpdate)arg).getTitle();
            int i;
            for (i = currentNames.size() - 1; i >= 0; --i) {
                if (currentNames.get(i).equals(tname))
                    break;
            }
            if (i < 0)
                list.clearSelection();
            else
                list.setSelectedIndex(i);
        }
    }
}
