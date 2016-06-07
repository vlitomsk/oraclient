package ru.vlitomsk.oraclient.gui;

import ru.vlitomsk.oraclient.ctl.ActiveTableCtl;
import ru.vlitomsk.oraclient.model.ActiveTableUpdate;
import ru.vlitomsk.oraclient.model.AddRowUpdate;
import ru.vlitomsk.oraclient.model.CellChangeUpdate;
import ru.vlitomsk.oraclient.model.ToggleRemoveUpdate;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.sql.Types;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

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

    class ActiveJTableModel extends DefaultTableModel {
        private boolean[][] cellsEditable;
        private int w, h;
        private String[] colNames;
        private Color[][] cellColors;
        private List<String[]> cellvals  = new ArrayList<>();
        public ActiveJTableModel(ActiveTableUpdate upd) throws SQLException {
            ResultSet rs = upd.getRs();
            ResultSetMetaData rsmd = rs.getMetaData();
            w = rsmd.getColumnCount();
            colNames = new String[w];
            for (int i = 0; i < w; ++i) {
                colNames[i] = rsmd.getColumnName(i + 1);
            }
            h=0;
            rs.beforeFirst();
            while (rs.next()) {
                String[] row = new String[w];
                for (int i = 0; i < w; ++i) {
                    row[i] = rs.getString(i + 1);
                }
                ++h;
                cellvals.add(row);
            }
            cellColors = new Color[h][w];
            cellsEditable = new boolean[h][w];
            for (int i = 0; i < h; ++i)
                for (int j = 0; j < w; ++j) {
                    cellsEditable[i][j] = true;
                    cellColors[i][j] = Color.WHITE;
                }
        }

        @Override
        public int getRowCount() {
            return h;
        }

        @Override
        public int getColumnCount() {
            return w;
        }

        @Override
        public String getColumnName(int column) {
            return colNames[column];
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object val = cellvals.get(row)[column];
            return val;
            //return val==null?"<NULL>":val;
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            Object old = cellvals.get(row)[column];
            if ((aValue == null && old == null) ||
                (aValue != null && old != null && aValue.equals(old)))
            {
                return;
            }
            cellvals.get(row)[column] = (String)aValue;
            fireTableCellUpdated(row,column);
        }

        public void setRowColor(int row, Color color) {
            for (int i = 0; i < w; ++i) {
                cellColors[row][i] = color;
            }
            fireTableRowsUpdated(row,row);
        }

        public void setCellColor(int row, int col, Color color) {
            cellColors[row][col] = color;
            fireTableRowsUpdated(row,row);
        }

        public Color getCellColor(int row, int col) {
            return cellColors[row][col];
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return cellsEditable[row][column];
        }

        public void setRowEditable(int row, boolean rowEnabled) {
            for (int i = 0; i < w; ++i)
                cellsEditable[row][i] = rowEnabled;
            fireTableRowsUpdated(row,row);
        }

        @Override
        public void addRow(Object[] rowData) {
            Color[][] cellColors2 = new Color[h+1][w];
            boolean[][] cellsEditable2 = new boolean[h+1][w];
            for (int i = 0; i < h; ++i) {
                for (int j = 0; j < w; ++j) {
                    cellColors2[i][j] = cellColors[i][j];
                    cellsEditable2[i][j] = cellsEditable[i][j];
                }
            }
            cellColors = cellColors2;
            cellsEditable = cellsEditable2;
            String[] casted = new String[rowData.length];
            for (int i = 0; i < w; ++i)
                casted[i] = (String)rowData[i];
            cellvals.add(casted);
            ++h;

            setRowColor(h-1, Color.green);
            setRowEditable(h-1, true);
            fireTableRowsUpdated(h-1,h-1);
        }
    }

    private static final Color RM_COLOR = Color.red;
    private static final Color CHG_COLOR = new Color(255,255,0);
    private static final Color ADD_COLOR = Color.green;
    private static final Color SEL_RM_COLOR = Color.pink;
    private static final Color SEL_CHG_COLOR = new Color(255, 255, 200);
    private static final Color SEL_ADD_COLOR = new Color(200,255,200);
    private static final Color[][] palette = {{Color.white,new Color(200,200,255)},{RM_COLOR, SEL_RM_COLOR}, {CHG_COLOR, SEL_CHG_COLOR}, {ADD_COLOR , SEL_ADD_COLOR}};

    private Color lighten(Color col) {
        for (Color[] pair : palette) {
            if (pair[0] == col)
                return pair[1];
        }
        throw new RuntimeException("unknown color");
    }

    class ActiveTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            ActiveJTableModel mdl = (ActiveJTableModel) tbl.getModel();
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            Color col = mdl.getCellColor(row, column);

            if (isSelected) {
                if (col != Color.white)
                    c.setBackground(lighten(col));
                else
                    c.setBackground(tbl.getSelectionBackground());
            } else
                c.setBackground(col);
            return c;
        }
    }

    private ArrayList<Object[]> lst = new ArrayList<>();
    private ActiveTableCellRenderer renderer = new ActiveTableCellRenderer();
    private JTable rebuildTable(ActiveTableUpdate upd) throws SQLException {
        ResultSet rs = upd.getRs();
        if (rs == null) {
            return tbl=null;
        } else {
            tbl = new JTable(new ActiveJTableModel(upd));
            tbl.setDefaultRenderer(Object.class, renderer);
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 0; i < tbl.getColumnCount(); ++i) {
                int type = rsmd.getColumnType(i+1);
                if (type == Types.BOOLEAN) {
                    JComboBox comboBox = new JComboBox();
                    comboBox.addItem("NULL");
                    comboBox.addItem("TRUE");
                    comboBox.addItem("FALSE");
                    tbl.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor(comboBox));
                }
            }
        }
        tbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        resizeColumnWidth(tbl);
        tbl.setEnabled(upd.isEditable());
        tbl.setRowSelectionAllowed(true);
        tbl.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tbl.getModel().addTableModelListener(chgListener);
        tbl.setColumnSelectionAllowed(true);
        tbl.setRowSelectionAllowed(true);

        return tbl;
    }

    private TableModelListener chgListener = e -> {
        int r = e.getFirstRow();
        int c = e.getColumn();
        if (c >= 0)
            ctl.valueChanged(r, c, (String)tbl.getValueAt(r, c));
    };

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

        if (arg instanceof ToggleRemoveUpdate) {
            ToggleRemoveUpdate upd = (ToggleRemoveUpdate) arg;
            if (tbl == null)
                return;
            ActiveJTableModel tmdl = (ActiveJTableModel)tbl.getModel();
            for (int i = 0; i < upd.indices.length; ++i) {
                tmdl.setRowColor(upd.indices[i], upd.toremove[i] ? RM_COLOR : ADD_COLOR);
                tmdl.setRowEditable(upd.indices[i], !upd.toremove[i]);
            }
        }

        if (arg instanceof CellChangeUpdate) {
            CellChangeUpdate upd = (CellChangeUpdate) arg;
            if (tbl == null)
                return;
            ActiveJTableModel tmdl = (ActiveJTableModel)tbl.getModel();
            tmdl.setCellColor(upd.row, upd.col, CHG_COLOR);
            tbl.setValueAt(upd.value, upd.row, upd.col);
        }

        if (arg instanceof AddRowUpdate) {
            AddRowUpdate upd = (AddRowUpdate) arg;
            if (tbl == null)
                return;
            Object[] newrow = new Object[tbl.getColumnCount()];
            Arrays.fill(newrow, null);
            ActiveJTableModel tmdl = (ActiveJTableModel)tbl.getModel();
            tmdl.addRow(newrow);
            tmdl.fireTableDataChanged();
        }
    }

    public ActionListener getRmRowListener() {
        return e->{
            if (tbl==null)
                return;
            ctl.toggleRemove(tbl.getSelectedRows());
        };
    }

    public ActionListener getWriteChangesListener() {
        return e-> {
            if (tbl == null)
                return;
            tcatch(()-> ctl.writeChanges());
        };
    }

    public ActionListener getAddRowListener() {
        return e ->{
            if (tbl ==null)
                return;
            ctl.addRow();
        };
    }

    public ActionListener getSetNullListener() {
        return e-> {
            if (tbl == null)
                return;
            int[] rows = tbl.getSelectedRows();
            int[] cols = tbl.getSelectedColumns();
            for (int row : rows) {
                for (int col : cols) {
                    ctl.valueChanged(row, col, null);
                }
            }
        };
    }
}
