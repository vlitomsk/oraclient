package ru.vlitomsk.oraclient.gui;

import ru.vlitomsk.oraclient.gui.components.JLabEdit;
import ru.vlitomsk.oraclient.gui.components.JOkCancel;

import javax.swing.*;
import java.awt.*;

import static ru.vlitomsk.oraclient.gui.Strings.*;
/**
 * Created by vas on 03.06.2016.
 */
public class LoginDialog extends JDialog {
    private JLabEdit edLogin, edPass, edListener, edServer;
    private boolean okay = false;
    public LoginDialog(Frame owner) {
        super(owner, true);
        setTitle(LOGIN_DLGTITLE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(edServer = new JLabEdit(CONN_SERV, "127.0.0.1"));
        add(edLogin = new JLabEdit(CONN_LOGIN, "vas"));
        add(edPass = new JLabEdit(CONN_PASS, "toor"));
        add(edListener = new JLabEdit(CONN_LISTENER, "XE"));
        JOkCancel buttons = new JOkCancel();
        add(buttons);
        buttons.addOkListener((e) -> {
            okay = true;
            dispose();
        });
        buttons.addCancelListener((e) -> dispose());

        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    public boolean isOkay() {
        return okay;
    }

    public String sr(String s) {
        return okay ? s : null;
    }

    public String getLogin() {
        return sr(edLogin.getText());
    }

    public String getPass() {
        return sr(edPass.getText());
    }

    public String getListener() {
        return sr(edListener.getText());
    }

    public String getServer() {
        return sr(edServer.getText());
    }
}
