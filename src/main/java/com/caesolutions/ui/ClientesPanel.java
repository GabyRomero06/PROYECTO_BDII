package com.caesolutions.ui;

import com.caesolutions.dao.ClienteDAO;
import com.caesolutions.model.Cliente;
import com.caesolutions.util.LoadingPanel;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClientesPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private ClienteDAO clienteDAO;
    
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public ClientesPanel() {
        clienteDAO = new ClienteDAO();
        
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        
        add(new LoadingPanel("Cargando clientes..."), "LOADING");
        
        contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setOpaque(false);
        add(contentPanel, "CONTENT");

        initUI();
        
        cardLayout.show(this, "LOADING");
        loadData();
    }

    private void initUI() {
        // Toolbar with title and buttons
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);
        
        JLabel lblTitle = new JLabel("Gestión de Clientes");
        lblTitle.setFont(new Font("Inter", Font.BOLD, 24));
        toolbar.add(lblTitle, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton btnAdd = createStyledButton("Nuevo Cliente", "#2563EB", "#3B82F6"); // Blue
        btnAdd.addActionListener(e -> showClienteDialog(null));
        
        JButton btnEdit = createStyledButton("Editar", "#475569", "#64748B"); // Slate
        btnEdit.addActionListener(e -> editSelectedCliente());
        
        JButton btnDelete = createStyledButton("Eliminar", "#DC2626", "#EF4444"); // Red
        btnDelete.addActionListener(e -> deleteSelectedCliente());
        
        JButton btnRefresh = createStyledButton("Refrescar", "#475569", "#64748B");
        btnRefresh.addActionListener(e -> {
            cardLayout.show(this, "LOADING");
            loadData();
        });

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnAdd);
        toolbar.add(buttonPanel, BorderLayout.EAST);

        contentPanel.add(toolbar, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre Comercial", "Contacto", "Teléfono", "Email"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(40); // Filas más anchas estilo web
        table.getTableHeader().setReorderingAllowed(false);
        
        // FlatLaf Table Styling
        table.putClientProperty(FlatClientProperties.STYLE, 
            "showHorizontalLines: true; " +
            "showVerticalLines: false; " +
            "intercellSpacing: 0,0; " +
            "selectionBackground: #334155; " + // Slate 700
            "selectionForeground: #F8FAFC;"
        );
        table.getTableHeader().putClientProperty(FlatClientProperties.STYLE, 
            "separatorColor: #1E293B; " +
            "background: #1E293B; " +
            "foreground: #94A3B8; " +
            "font: bold;"
        );
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.putClientProperty(FlatClientProperties.STYLE, 
            "arc: 12; " +
            "border: 1,1,1,1,#334155" // Slate 700 border
        );
        contentPanel.add(scrollPane, BorderLayout.CENTER);
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
        SwingWorker<List<Cliente>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Cliente> doInBackground() {
                try { Thread.sleep(300); } catch (Exception ignored) {} // Animación visible
                return clienteDAO.getAllClientes();
            }

            @Override
            protected void done() {
                try {
                    List<Cliente> clientes = get();
                    for (Cliente c : clientes) {
                        tableModel.addRow(new Object[]{
                            c.getIdCliente(),
                            c.getNombreComercial(),
                            c.getContactoPrincipal(),
                            c.getTelefono(),
                            c.getEmail()
                        });
                    }
                    cardLayout.show(ClientesPanel.this, "CONTENT");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ClientesPanel.this, "Error cargando clientes.", "Error", JOptionPane.ERROR_MESSAGE);
                    cardLayout.show(ClientesPanel.this, "CONTENT");
                }
            }
        };
        worker.execute();
    }

    private void showClienteDialog(Cliente clienteToEdit) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), clienteToEdit != null ? "Editar Cliente" : "Nuevo Cliente", true);
        dialog.setSize(450, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.decode("#0F172A")); // Slate 900
        
        JTextField txtNombre = createFormInput(clienteToEdit != null ? clienteToEdit.getNombreComercial() : "");
        JTextField txtContacto = createFormInput(clienteToEdit != null ? clienteToEdit.getContactoPrincipal() : "");
        JTextField txtTel = createFormInput(clienteToEdit != null ? clienteToEdit.getTelefono() : "");
        JTextField txtEmail = createFormInput(clienteToEdit != null ? clienteToEdit.getEmail() : "");
        JTextField txtDir = createFormInput(clienteToEdit != null ? clienteToEdit.getDireccion() : "");
        JTextField txtRtn = createFormInput(clienteToEdit != null ? clienteToEdit.getRtnDni() : "");

        panel.add(createFormLabel("Nombre Comercial:")); panel.add(txtNombre);
        panel.add(createFormLabel("Contacto Principal:")); panel.add(txtContacto);
        panel.add(createFormLabel("Teléfono:")); panel.add(txtTel);
        panel.add(createFormLabel("Email:")); panel.add(txtEmail);
        panel.add(createFormLabel("Dirección:")); panel.add(txtDir);
        panel.add(createFormLabel("RTN/DNI:")); panel.add(txtRtn);

        JButton btnSave = createStyledButton("Guardar Cliente", "#2563EB", "#3B82F6");
        btnSave.addActionListener(e -> {
            Cliente c = clienteToEdit != null ? clienteToEdit : new Cliente();
            c.setNombreComercial(txtNombre.getText());
            c.setContactoPrincipal(txtContacto.getText());
            c.setTelefono(txtTel.getText());
            c.setEmail(txtEmail.getText());
            c.setDireccion(txtDir.getText());
            c.setRtnDni(txtRtn.getText());

            boolean success = clienteToEdit != null ? clienteDAO.updateCliente(c) : clienteDAO.insertCliente(c);
            if (success) {
                dialog.dispose();
                cardLayout.show(this, "LOADING");
                loadData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al guardar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(new JLabel("")); // Spacer
        panel.add(btnSave);

        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private JLabel createFormLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Inter", Font.BOLD, 12));
        lbl.setForeground(Color.decode("#94A3B8"));
        return lbl;
    }
    
    private JTextField createFormInput(String initialValue) {
        JTextField txt = new JTextField(initialValue);
        txt.putClientProperty(FlatClientProperties.STYLE, 
            "background: #1E293B; " +
            "foreground: #F8FAFC; " +
            "arc: 8; " +
            "margin: 5,10,5,10;"
        );
        return txt;
    }

    private void editSelectedCliente() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Cliente toEdit = clienteDAO.getAllClientes().stream().filter(c -> c.getIdCliente() == id).findFirst().orElse(null);
        if (toEdit != null) {
            showClienteDialog(toEdit);
        }
    }

    private void deleteSelectedCliente() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar este cliente?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (clienteDAO.deleteCliente(id)) {
                cardLayout.show(this, "LOADING");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "No se puede eliminar el cliente (puede tener licencias asociadas).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
