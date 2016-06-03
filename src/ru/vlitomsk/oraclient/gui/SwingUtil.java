package ru.vlitomsk.oraclient.gui;

/**
 * Created by vas on 03.06.2016.
 */
import java.awt.*;
import javax.swing.*;
public class SwingUtil {
    public static void setAllSize(Component comp, Dimension dim) {
        comp.setSize(dim);
        comp.setPreferredSize(dim);
        comp.setMinimumSize(dim);
    }
}
