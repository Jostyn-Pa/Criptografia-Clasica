package com.cripto.sis;

import com.cripto.sis.ui.VentanaGUI;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaGUI app = new VentanaGUI();
            app.setVisible(true);
        });
    }
}
