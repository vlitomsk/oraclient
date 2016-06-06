package ru.vlitomsk.oraclient.gui;

import ru.vlitomsk.oraclient.ctl.ActiveTableCtl;
import ru.vlitomsk.oraclient.model.ActiveTableUpdate;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by vas on 03.06.2016.
 */
public class ActiveTableView extends JScrollPane implements Observer {
    private ActiveTableCtl ctl;
    private JTable tbl;
    public ActiveTableView(ActiveTableCtl ctl) {
        super();
        this.ctl = ctl;
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    private ArrayList<Object[]> lst = new ArrayList<>();
    private JTable rebuildTable(ActiveTableUpdate upd) throws SQLException {
        ResultSet rs = upd.getRs();
        String[] colNames;
        Object[][] data;
        if (rs != null){
            ResultSetMetaData rsmd = rs.getMetaData();
            colNames = new String[rsmd.getColumnCount()];
            for (int i = 0; i < colNames.length; ++i) {
                colNames[i] = rsmd.getColumnName(i + 1);
            }
            lst.clear();
            while (rs.next()) {
                Object[] row = new Object[colNames.length];
                for (int i = 0; i < colNames.length; ++i) {
                    row[i] = rs.getString(i + 1);
                }
                lst.add(row);
            }
            data = new Object[lst.size()][];
            lst.toArray(data);
        } else {
            colNames = new String[]{};
            data = new Object[][]{};
        }
        JTable tbl = new JTable(data, colNames);
        tbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        resizeColumnWidth(tbl);
        tbl.setEnabled(upd.isEditable());

        return tbl;
    }

    private void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 50; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        tbl.setEnabled(false);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof ActiveTableUpdate) {
            try {
                tbl = rebuildTable((ActiveTableUpdate) arg);
                setViewportView(tbl);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
