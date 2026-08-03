package com.caesolutions;

import com.caesolutions.ui.LoginFrame;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

public class Main {
    public static void main(String[] args) {
        // Habilitar decoraciones de ventana personalizadas (integradas)
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        // Inicializar el Look and Feel moderno (FlatMacDarkLaf)
        FlatMacDarkLaf.setup();
        
        // Configuraciones globales para aspecto Web/Moderno Antigravity
        UIManager.put("Component.arc", 999);
        UIManager.put("Button.arc", 999); // Botones estilo pastilla perfecta
        UIManager.put("ProgressBar.arc", 999);
        UIManager.put("TextComponent.arc", 12);
        UIManager.put("Component.focusWidth", 2);
        UIManager.put("Component.innerFocusWidth", 0);
        UIManager.put("ScrollBar.showButtons", false);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
        
        // Colores y tipografía
        UIManager.put("defaultFont", new Font("Inter", Font.PLAIN, 14));
        UIManager.put("Panel.background", Color.decode("#0F172A")); // Slate 900
        UIManager.put("Viewport.background", Color.decode("#0F172A"));
        UIManager.put("control", Color.decode("#0F172A"));
        UIManager.put("Label.foreground", Color.decode("#F8FAFC"));

        // Lanzar la aplicación en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
