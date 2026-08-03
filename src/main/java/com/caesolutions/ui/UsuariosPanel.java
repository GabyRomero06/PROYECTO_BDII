package com.caesolutions.ui;

import com.caesolutions.dao.UsuarioDAO;
import com.caesolutions.model.Usuario;
import com.caesolutions.util.LoadingPanel;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuariosPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private UsuarioDAO usuarioDAO;
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public UsuariosPanel() {
        usuarioDAO = new UsuarioDAO();
        
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        
        add(new LoadingPanel("Cargando usuarios..."), "LOADING");
        
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
        JLabel lblTitle = new JLabel("Gestión de Usuarios");
        lblTitle.setFont(new Font("Inter", Font.BOLD, 24));
        toolbar.add(lblTitle, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton btnAdd = createStyledButton("Nuevo Usuario", "#2563EB", "#3B82F6"); // Blue
        btnAdd.addActionListener(e -> showCreateDialog());
        
        JButton btnToggle = createStyledButton("Activar / Desactivar", "#F59E0B", "#FCD34D"); // Amber
        btnToggle.addActionListener(e -> toggleSelectedUser());
        
        JButton btnRefresh = createStyledButton("Refrescar", "#475569", "#64748B"); // Slate
        btnRefresh.addActionListener(e -> {
            cardLayout.show(this, "LOADING");
            loadData();
        });

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnToggle);
        buttonPanel.add(btnAdd);
        toolbar.add(buttonPanel, BorderLayout.EAST);

        contentPanel.add(toolbar, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{
            "ID", "Usuario", "Estado"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(50);
        table.getTableHeader().setReorderingAllowed(false);
        table.setDefaultRenderer(Object.class, new com.caesolutions.util.AntigravityCellRenderer());
        table.getTableHeader().setDefaultRenderer(new com.caesolutions.util.AntigravityHeaderRenderer());
        
        table.setOpaque(false);
        table.setBackground(new Color(0, 0, 0, 0));
        
        table.putClientProperty(FlatClientProperties.STYLE, 
            "showHorizontalLines: true; " +
            "showVerticalLines: false; " +
            "intercellSpacing: 0,0; " +
            "background: null; " +
            "foreground: #F8FAFC; " +
            "selectionBackground: null; " + 
            "selectionForeground: null; " +
            "gridColor: #1E3A8A;"
        );
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(new Color(0,0,0,0));
        table.getTableHeader().putClientProperty(FlatClientProperties.STYLE, 
            "separatorColor: #0B1727; " +
            "background: null; " +
            "foreground: null;"
        );
        
        com.caesolutions.util.AntigravityContainer tableCard = new com.caesolutions.util.AntigravityContainer();
        tableCard.setLayout(new BorderLayout());
        
        JPanel paddingPanel = new JPanel(new BorderLayout());
        paddingPanel.setOpaque(false);
        paddingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        
        paddingPanel.add(scrollPane, BorderLayout.CENTER);
        tableCard.add(paddingPanel, BorderLayout.CENTER);
        
        contentPanel.add(tableCard, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, String bg, String hoverBg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Inter", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE, 
            "background: " + bg + "; " +
            "foreground: #FFFFFF; " +
            "arc: 8; " +
            "borderWidth: 0; " +
            "focusWidth: 0; " +
            "hoverBackground: " + hoverBg + "; " +
            "margin: 6,14,6,14;"
        );
        return btn;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        SwingWorker<List<Usuario>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Usuario> doInBackground() {
                try { Thread.sleep(300); } catch (Exception ignored) {}
                return usuarioDAO.getAllUsuarios();
            }
            @Override
            protected void done() {
                try {
                    List<Usuario> usuarios = get();
                    for (Usuario u : usuarios) {
                        String estadoStr = u.getEstadoUsuario() == 1 ? "Activo" : "Inactivo";
                        tableModel.addRow(new Object[]{
                            u.getIdUsuario(),
                            u.getNombreUsuario(),
                            estadoStr
                        });
                    }
                    cardLayout.show(UsuariosPanel.this, "CONTENT");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(UsuariosPanel.this, "Error cargando usuarios.", "Error", JOptionPane.ERROR_MESSAGE);
                    cardLayout.show(UsuariosPanel.this, "CONTENT");
                }
            }
        };
        worker.execute();
    }

    private void showCreateDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nuevo Usuario", true);
        dialog.setSize(300, 250);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();

        panel.add(new JLabel("Nombre de Usuario:")); panel.add(txtUser);
        panel.add(new JLabel("Contraseña:")); panel.add(txtPass);

        JButton btnSave = createStyledButton("Crear", "#2563EB", "#3B82F6");
        btnSave.addActionListener(e -> {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword());
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Llene todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (usuarioDAO.insertUsuario(user, pass)) {
                dialog.dispose();
                cardLayout.show(UsuariosPanel.this, "LOADING");
                loadData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al crear usuario (quizás ya existe).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(new JLabel(""));
        panel.add(btnSave);

        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void toggleSelectedUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String estado = (String) tableModel.getValueAt(selectedRow, 2);
        int nuevoEstado = "Activo".equals(estado) ? 0 : 1;
        
        // Prevent disabling user ID 1 (Admin)
        if (id == 1 && nuevoEstado == 0) {
            JOptionPane.showMessageDialog(this, "No puedes desactivar al administrador principal.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (usuarioDAO.toggleEstado(id, nuevoEstado)) {
            cardLayout.show(this, "LOADING");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Error actualizando estado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
