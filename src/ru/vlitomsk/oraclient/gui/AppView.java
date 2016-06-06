package ru.vlitomsk.oraclient.gui;

import ru.vlitomsk.oraclient.ctl.AppCtl;
import ru.vlitomsk.oraclient.model.ActiveTableUpdate;
import ru.vlitomsk.oraclient.model.ConnectedUpdate;
import ru.vlitomsk.oraclient.model.DisconnUpdate;
import ru.vlitomsk.oraclient.model.TableNamesUpdate;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.util.*;
import java.util.function.*;

import static ru.vlitomsk.oraclient.gui.Strings.*;
/**
 * Created by vas on 03.06.2016.
 */
public class AppView extends JFrame implements Observer{
    private static final String
            SACT_EXIT = "Exit",
            SACT_ABOUT = "About",
            SACT_CONNECT = "Connect",
            SACT_DISCONNECT = "Disconnect",
            SACT_SQLQUERY = "SQL query",
            SACT_WRITE = "Write changes",
            SACT_RMROW = "Remove row";

    private static final String
        IC_EXIT = "exit.png",
        IC_ABOUT = "about.png",
        IC_CONNECT = "connect.png",
        IC_DISCONNECT = "disconnect.png",
        IC_SQLQUERY = "query.png",
        IC_WRITECHANGES = "write.png",
        IC_RMROW = "rmrow.png";

    private static final String
            MENU_CONN = "Connection",
            MENU_SQL = "SQL",
            MENU_HELP = "Help";

    private static final String aboutString =
            "Оракуль БД климент \n" +
                    "от Василия Литвинова <kvas.omsk at gmail.com>";

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

    private void tcatch(ExRunnable r) {
        try {
            r.run();
        } catch (Exception ex) {
            showError(ex.toString());
            ex.printStackTrace();
        }
    }

    private ActionListener killListener = (e) -> dispose();
    private ActionListener connectListener =  (e) -> SwingUtilities.invokeLater(()-> tcatch(() -> {
        ConnectDialog dlg = new ConnectDialog(AppView.this);
        if (dlg.isOkay()) {
            controller.newConnection(dlg.getServer(), dlg.getLogin(), dlg.getPass(), dlg.getListener());
        }
    }));

    private ActionListener disconnectListener = (e) -> SwingUtilities.invokeLater(() -> tcatch(() -> {
        controller.disconnect();
    }));

    private ActionListener aboutLitener = (e) ->
            JOptionPane.showMessageDialog(AppView.this, aboutString);
    private String lastSql = "";
    private ActionListener sqlQueryListener = (e) -> SwingUtilities.invokeLater(() -> tcatch(() -> {
        SQLQueryDialog dlg = new SQLQueryDialog(AppView.this, lastSql);
        if (dlg.isOkay()) {
            controller.sqlQuery(lastSql = dlg.getQuery());
        }
    }));
    private ActionListener writeChangesListener = (e) -> {};
    private ActionListener rmRowListener = (e) -> {};

    private final Map<String, ItemDesc> mItems = new HashMap<String,ItemDesc>() {
        {
            put(SACT_EXIT, new ItemDesc(SACT_EXIT, IC_EXIT, killListener));
            put(SACT_CONNECT, new ItemDesc(SACT_CONNECT, IC_CONNECT, connectListener));
            put(SACT_DISCONNECT, new ItemDesc(SACT_DISCONNECT, IC_DISCONNECT, disconnectListener));
            put(SACT_ABOUT, new ItemDesc(SACT_ABOUT, IC_ABOUT, aboutLitener));
            put(SACT_SQLQUERY, new ItemDesc(SACT_SQLQUERY, IC_SQLQUERY, sqlQueryListener));
            put(SACT_WRITE, new ItemDesc(SACT_WRITE, IC_WRITECHANGES, writeChangesListener));
            put(SACT_RMROW, new ItemDesc(SACT_RMROW, IC_RMROW, rmRowListener));
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
        addMenuItem(connMenu, SACT_DISCONNECT);
        connMenu.addSeparator();
        addMenuItem(connMenu, SACT_EXIT);
        return connMenu;
    }

    private JMenu createSQLMenu() {
        JMenu sqlMenu = new JMenu(MENU_SQL);
        addMenuItem(sqlMenu, SACT_SQLQUERY);
        addMenuItem(sqlMenu, SACT_RMROW);
        addMenuItem(sqlMenu, SACT_WRITE);
        return sqlMenu;
    }

    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu(MENU_HELP);
        addMenuItem(helpMenu, SACT_ABOUT);
        return helpMenu;
    }

    private void addMenu() {
        JMenuBar menu = new JMenuBar();
        menu.add(createConnMenu());
        menu.add(createSQLMenu());
        menu.add(createHelpMenu());
        setJMenuBar(menu);
    }

    private void addToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        addToolbarBtn(toolbar, SACT_CONNECT);
        addToolbarBtn(toolbar, SACT_DISCONNECT);
        addToolbarBtn(toolbar, SACT_SQLQUERY);
        toolbar.addSeparator();
        addToolbarBtn(toolbar, SACT_WRITE);
        toolbar.addSeparator();
        addToolbarBtn(toolbar, SACT_RMROW);
        toolbar.addSeparator();
        addToolbarBtn(toolbar, SACT_EXIT);
        add(toolbar, BorderLayout.NORTH);
    }

    private JPanel statusPanel;
    private JLabel statusLabel;
    private JLabel activeTableLabel;

    private void addStatusbar() {
        // create the status bar panel and shove it down the bottom of the frame
        statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(getWidth(), 16));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusLabel = new JLabel("Disconnected");
        statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        activeTableLabel = new JLabel("");
        activeTableLabel.setForeground(Color.RED);
        statusPanel.add(new JLabel("Active table: "));
        statusPanel.add(activeTableLabel);
        statusPanel.add(new JLabel(" ; Status: "));
        statusPanel.add(statusLabel);
        statusLabel.setForeground(Color.RED);
    }

    private JSplitPane splitPane;
    private TablesListView tablesListView;
    private ActiveTableView activeTableView;

    public TablesListView getTablesListView() {
        return tablesListView;
    }

    public ActiveTableView getActiveTableView() {
        return activeTableView;
    }

    private void addSplitView() {
        tablesListView = new TablesListView(controller.getActiveTableCtl());
        activeTableView = new ActiveTableView(controller.getActiveTableCtl());
        SwingUtil.setAllSize(tablesListView, new Dimension(100, 500));
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablesListView, activeTableView);
        splitPane.setOneTouchExpandable(false);
        splitPane.setDividerLocation(200);

        add(splitPane);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof ConnectedUpdate) {
            statusLabel.setText("Connected");
            statusLabel.setForeground(Color.green);
        }

        if (arg instanceof DisconnUpdate) {
            DisconnUpdate upd = (DisconnUpdate) arg;
            statusLabel.setText("Disconnected");
            statusLabel.setForeground(Color.red);
        }

        if (arg instanceof ActiveTableUpdate) {
            ActiveTableUpdate upd = (ActiveTableUpdate) arg;
            activeTableLabel.setText(upd.getTitle());
        }
    }
}
