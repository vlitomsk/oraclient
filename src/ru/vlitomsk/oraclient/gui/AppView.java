package ru.vlitomsk.oraclient.gui;

import ru.vlitomsk.oraclient.ctl.AppCtl;
import ru.vlitomsk.oraclient.gui.components.JScrolledList;
import ru.vlitomsk.oraclient.model.DisconnUpdate;
import ru.vlitomsk.oraclient.model.NewConnUpdate;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.util.*;

import static ru.vlitomsk.oraclient.gui.Strings.*;
/**
 * Created by vas on 03.06.2016.
 */
public class AppView extends JFrame implements Observer{
    private static final String
            SACT_EXIT = "Exit",
            SACT_ABOUT = "About",
            SACT_CONNECT = "Connect",
            SACT_DISCONNECT = "Disconnect";

    private static final String
            MENU_CONN = "Connection",
            MENU_HELP = "Help";

    private static final String aboutString =
            "This is Oracle DB client \n" +
                    "by Vasiliy Litvinov <kvas.omsk at gmail.com>";

    private AppCtl controller;

    public AppView(AppCtl controller) {
        super();
        this.controller = controller;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Dimension minSize = new Dimension(800, 600);
        setSize(minSize);
        setMinimumSize(minSize);
        setPreferredSize(minSize);
        setTitle(APP_TITLE);

        addMenu();
        addToolbar();
        addStatusbar();
        addSplitView();

        setLocationRelativeTo(null);
    }

    private class ItemDesc {
        String title;
        ImageIcon icon;
        ActionListener listener;

        public ItemDesc(String title, String iconPath, ActionListener listener) {
            if (iconPath != null)
                this.icon = new ImageIcon(iconPath);
            this.listener = listener;
            this.title = title;
        }
    }

    private void showError(String err) {
        JOptionPane.showMessageDialog(this, err, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private ActionListener killListener = (e) -> dispose();
    private ActionListener connectListener =  (e) -> SwingUtilities.invokeLater(()->{
        LoginDialog dlg = new LoginDialog(AppView.this);
        if (dlg.isOkay()) {
            try {
                controller.newConnection(dlg.getServer(), dlg.getLogin(), dlg.getPass(), dlg.getListener());
            } catch (Exception ex) {
                showError(ex.toString());
                ex.printStackTrace();
            }
        }
    });
    private ActionListener disconnectListener = (e) -> SwingUtilities.invokeLater(() -> {
        try {
            controller.disconnect();
        } catch (Exception ex) {
            showError(ex.toString());
            ex.printStackTrace();
        }
    });
    private ActionListener aboutLitener = (e) ->
            JOptionPane.showMessageDialog(AppView.this, aboutString);

    private final Map<String, ItemDesc> mItems = new HashMap<String,ItemDesc>() {
        {
            put(SACT_EXIT, new ItemDesc(SACT_EXIT, null, killListener));
            put(SACT_CONNECT, new ItemDesc(SACT_CONNECT, null, connectListener));
            put(SACT_DISCONNECT, new ItemDesc(SACT_DISCONNECT, null, disconnectListener));
            put(SACT_ABOUT, new ItemDesc(SACT_ABOUT, null, aboutLitener));
        }
    };

    private void addMenuItem(JMenu menu, String itemName) {
        ItemDesc desc = mItems.get(itemName);
        JMenuItem mitem = new JMenuItem(desc.title);
        mitem.addActionListener(desc.listener);
        if (desc.icon != null)
            mitem.setIcon(desc.icon);
        menu.add(mitem);
    }

    private void addToolbarBtn(JToolBar tb, String itemName) {
        ItemDesc desc = mItems.get(itemName);
        JButton btn = new JButton();
        if (desc.icon != null)
            btn.setIcon(desc.icon);
        else
            btn.setText(desc.title);
        btn.setFocusable(false);
        btn.addActionListener(desc.listener);
        btn.setToolTipText(desc.title);
        tb.add(btn);
    }

    private JMenu createConnMenu() {
        JMenu connMenu = new JMenu(MENU_CONN);
        addMenuItem(connMenu, SACT_CONNECT);
        connMenu.addSeparator();
        addMenuItem(connMenu, SACT_EXIT);
        return connMenu;
    }

    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu(MENU_HELP);
        addMenuItem(helpMenu, SACT_ABOUT);
        return helpMenu;
    }

    private void addMenu() {
        JMenuBar menu = new JMenuBar();
        menu.add(createConnMenu());
        menu.add(createHelpMenu());
        setJMenuBar(menu);
    }

    private void addToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        addToolbarBtn(toolbar, SACT_CONNECT);
        addToolbarBtn(toolbar, SACT_DISCONNECT);
        toolbar.addSeparator();
        addToolbarBtn(toolbar, SACT_EXIT);
        add(toolbar, BorderLayout.NORTH);
    }

    private JPanel statusPanel;
    private JLabel statusLabel;

    private void addStatusbar() {
        // create the status bar panel and shove it down the bottom of the frame
        statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(getWidth(), 16));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusLabel = new JLabel("Disconnected");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
    }

    private JSplitPane splitPane;
    private void addSplitView() {
        TablesListView tablesListView = new TablesListView();
        JPanel right = new JPanel();
        SwingUtil.setAllSize(tablesListView, new Dimension(100, 500));
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablesListView, right);
        splitPane.setOneTouchExpandable(false);
        splitPane.setDividerLocation(200);

        add(splitPane);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof NewConnUpdate) {
            NewConnUpdate upd = (NewConnUpdate) arg;
            statusLabel.setText("Connected");
        }
        if (arg instanceof DisconnUpdate) {
            DisconnUpdate upd = (DisconnUpdate) arg;
            statusLabel.setText("Disconnected");
        }
    }
}
