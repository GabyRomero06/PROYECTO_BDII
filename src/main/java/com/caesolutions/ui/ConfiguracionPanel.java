package com.caesolutions.ui;

import com.caesolutions.dao.EmpresaDAO;
import com.caesolutions.model.Empresa;
import com.caesolutions.util.LoadingPanel;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;

public class ConfiguracionPanel extends JPanel {
    private EmpresaDAO empresaDAO;
    private Empresa empresaActual;

    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextArea txtDireccion;

    private CardLayout cardLayout;
    private JPanel contentPanel;

    public ConfiguracionPanel() {
        empresaDAO = new EmpresaDAO();
        
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        
        add(new LoadingPanel("Cargando configuración..."), "LOADING");
        
        contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setOpaque(false);
        add(contentPanel, "CONTENT");

        initUI();
        
        cardLayout.show(this, "LOADING");
        loadData();
    }

    private void initUI() {
        JPanel toolbar = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("Configuración de la Empresa");
        lblTitle.setFont(new Font("Inter", Font.BOLD, 24));
        toolbar.add(lblTitle, BorderLayout.WEST);
        contentPanel.add(toolbar, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos Generales"));
        formPanel.setBackground(Color.decode("#1E293B")); // Slate 800
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        txtNombre = new JTextField(25);
        txtTelefono = new JTextField(25);
        txtCorreo = new JTextField(25);
        txtDireccion = new JTextArea(4, 25);
        txtDireccion.setLineWrap(true);
        txtDireccion.setWrapStyleWord(true);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nombre de Empresa:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtTelefono, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Correo:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtCorreo, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(txtDireccion), gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        JButton btnSave = new JButton("Guardar Cambios");
        btnSave.setFont(new Font("Inter", Font.BOLD, 13));
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.putClientProperty(FlatClientProperties.STYLE, 
            "background: #2563EB; " + // Blue
            "foreground: #FFFFFF; " +
            "arc: 8; " +
            "borderWidth: 0; " +
            "focusWidth: 0; " +
            "hoverBackground: #3B82F6; " +
            "margin: 6,14,6,14;"
        );
        btnSave.addActionListener(e -> saveChanges());
        formPanel.add(btnSave, gbc);

        // Wrapper to keep form panel aligned top-left
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapper.setOpaque(false);
        wrapper.add(formPanel);
        
        contentPanel.add(wrapper, BorderLayout.CENTER);
    }

    private void loadData() {
        SwingWorker<Empresa, Void> worker = new SwingWorker<>() {
            @Override
            protected Empresa doInBackground() {
                try { Thread.sleep(300); } catch (Exception ignored) {}
                return empresaDAO.getEmpresa();
            }
            @Override
            protected void done() {
                try {
                    empresaActual = get();
                    if (empresaActual != null) {
                        txtNombre.setText(empresaActual.getNombreEmpresa());
                        txtTelefono.setText(empresaActual.getTelefono());
                        txtCorreo.setText(empresaActual.getCorreo());
                        txtDireccion.setText(empresaActual.getDireccion());
                    }
                    cardLayout.show(ConfiguracionPanel.this, "CONTENT");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ConfiguracionPanel.this, "Error cargando configuración.", "Error", JOptionPane.ERROR_MESSAGE);
                    cardLayout.show(ConfiguracionPanel.this, "CONTENT");
                }
            }
        };
        worker.execute();
    }

    private void saveChanges() {
        if (empresaActual == null) return;
        
        empresaActual.setNombreEmpresa(txtNombre.getText());
        empresaActual.setTelefono(txtTelefono.getText());
        empresaActual.setCorreo(txtCorreo.getText());
        empresaActual.setDireccion(txtDireccion.getText());
        
        if (empresaDAO.updateEmpresa(empresaActual)) {
            JOptionPane.showMessageDialog(this, "Configuración actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(this, "LOADING");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Error actualizando la configuración.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
