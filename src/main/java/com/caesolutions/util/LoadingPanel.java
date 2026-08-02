package com.caesolutions.util;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;

public class LoadingPanel extends JPanel {

    public LoadingPanel(String message) {
        setLayout(new GridBagLayout());
        setBackground(Color.decode("#0F172A")); // Slate 900 background para mezclar con el layout principal

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Barra de progreso indeterminada (estilo spinner/barra moderna)
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(200, 6));
        progressBar.putClientProperty(FlatClientProperties.STYLE, "arc: 999;");
        
        // Label animado
        JLabel lblMessage = new JLabel(message);
        lblMessage.setFont(new Font("Inter", Font.BOLD, 14));
        lblMessage.setForeground(Color.decode("#38BDF8"));
        lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(progressBar);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(lblMessage);

        add(centerPanel);
    }
}
