package com.caesolutions.ui;

import com.caesolutions.dao.ClienteDAO;
import com.caesolutions.dao.TicketDAO;
import com.caesolutions.model.Cliente;
import com.caesolutions.model.Ticket;
import com.caesolutions.util.LoadingPanel;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TicketsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private TicketDAO ticketDAO;
    private ClienteDAO clienteDAO;
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public TicketsPanel() {
        ticketDAO = new TicketDAO();
        clienteDAO = new ClienteDAO();
        
        clienteDAO = new ClienteDAO();
        
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        
        add(new LoadingPanel("Cargando tickets..."), "LOADING");
        
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
        JLabel lblTitle = new JLabel("Soporte Técnico (Tickets)");
        lblTitle.setFont(new Font("Inter", Font.BOLD, 24));
        toolbar.add(lblTitle, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton btnAdd = createStyledButton("Nuevo Ticket", "#2563EB", "#3B82F6"); // Blue
        btnAdd.addActionListener(e -> showCreateDialog());
        
        JButton btnResolve = createStyledButton("Resolver / Cerrar", "#10B981", "#34D399"); // Emerald
        btnResolve.addActionListener(e -> showResolveDialog());
        
        JButton btnRefresh = createStyledButton("Refrescar", "#475569", "#64748B"); // Slate
        btnRefresh.addActionListener(e -> {
            cardLayout.show(this, "LOADING");
            loadData();
        });

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnResolve);
        buttonPanel.add(btnAdd);
        toolbar.add(buttonPanel, BorderLayout.EAST);

        contentPanel.add(toolbar, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{
            "ID", "Cliente", "Asunto", "Estado", "F. Creación", "F. Resolución"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(40);
        table.getTableHeader().setReorderingAllowed(false);
        
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
        SwingWorker<List<Ticket>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Ticket> doInBackground() {
                try { Thread.sleep(300); } catch (Exception ignored) {}
                return ticketDAO.getAllTickets();
            }
            @Override
            protected void done() {
                try {
                    List<Ticket> tickets = get();
                    for (Ticket t : tickets) {
                        tableModel.addRow(new Object[]{
                            t.getIdTicket(),
                            t.getNombreCliente(),
                            t.getAsunto(),
                            t.getEstado(),
                            t.getFechaCreacion(),
                            t.getFechaResolucion() == null ? "N/A" : t.getFechaResolucion()
                        });
                    }
                    cardLayout.show(TicketsPanel.this, "CONTENT");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(TicketsPanel.this, "Error cargando tickets.", "Error", JOptionPane.ERROR_MESSAGE);
                    cardLayout.show(TicketsPanel.this, "CONTENT");
                }
            }
        };
        worker.execute();
    }

    private void showCreateDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nuevo Ticket", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<Cliente> cbClientes = new JComboBox<>();
        for(Cliente c : clienteDAO.getAllClientes()) cbClientes.addItem(c);

        JTextField txtAsunto = new JTextField();
        JTextArea txtDesc = new JTextArea(3, 20);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDesc);

        panel.add(new JLabel("Cliente:")); panel.add(cbClientes);
        panel.add(new JLabel("Asunto:")); panel.add(txtAsunto);
        panel.add(new JLabel("Descripción:")); panel.add(scrollDesc);

        JButton btnSave = createStyledButton("Crear", "#2563EB", "#3B82F6");
        btnSave.addActionListener(e -> {
            Cliente c = (Cliente) cbClientes.getSelectedItem();
            if (c == null || txtAsunto.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Asunto es requerido.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Ticket t = new Ticket();
            t.setIdCliente(c.getIdCliente());
            t.setAsunto(txtAsunto.getText());
            t.setDescripcion(txtDesc.getText());
            
            if (ticketDAO.insertTicket(t)) {
                dialog.dispose();
                cardLayout.show(TicketsPanel.this, "LOADING");
                loadData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al crear ticket.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(new JLabel(""));
        panel.add(btnSave);

        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void showResolveDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un ticket de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String estado = (String) tableModel.getValueAt(selectedRow, 3);
        
        if ("Resuelto".equals(estado)) {
            JOptionPane.showMessageDialog(this, "El ticket ya está resuelto.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Resolver Ticket", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Notas de Resolución:"), BorderLayout.NORTH);
        
        JTextArea txtNotas = new JTextArea();
        txtNotas.setLineWrap(true);
        txtNotas.setWrapStyleWord(true);
        panel.add(new JScrollPane(txtNotas), BorderLayout.CENTER);
        
        JButton btnSave = createStyledButton("Marcar como Resuelto", "#10B981", "#34D399");
        btnSave.addActionListener(e -> {
            if (ticketDAO.updateTicketStatus(id, "Resuelto", txtNotas.getText())) {
                dialog.dispose();
                cardLayout.show(TicketsPanel.this, "LOADING");
                loadData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al actualizar ticket.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(btnSave, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
}
