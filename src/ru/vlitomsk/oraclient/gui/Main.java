package ru.vlitomsk.oraclient.gui;

import ru.vlitomsk.oraclient.ctl.AppCtl;
import ru.vlitomsk.oraclient.model.AppModel;
import ru.vlitomsk.oraclient.model.ConnException;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws ConnException {
        AppModel model = new AppModel();
        AppCtl appController = new AppCtl(model);
        AppView appView = new AppView(appController);
        model.addObserver(appView);
        SwingUtilities.invokeLater(() -> appView.setVisible(true));
    }
}
