package com.caesolutions.ui;

import com.caesolutions.dao.LicenciaDAO;
import com.caesolutions.dao.PagoDAO;
import com.caesolutions.model.Licencia;
import com.caesolutions.model.Pago;
import com.caesolutions.util.LoadingPanel;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class PagosPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private PagoDAO pagoDAO;
    private LicenciaDAO licenciaDAO;
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public PagosPanel() {
        pagoDAO = new PagoDAO();
        licenciaDAO = new LicenciaDAO();
        
        licenciaDAO = new LicenciaDAO();
        
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        
        add(new LoadingPanel("Cargando pagos..."), "LOADING");
        
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
        JLabel lblTitle = new JLabel("Historial de Pagos");
        lblTitle.setFont(new Font("Inter", Font.BOLD, 24));
        toolbar.add(lblTitle, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton btnAdd = createStyledButton("Registrar Pago", "#10B981", "#34D399"); // Emerald
        btnAdd.addActionListener(e -> showPagoDialog());
        
        JButton btnRefresh = createStyledButton("Refrescar", "#475569", "#64748B"); // Slate
        btnRefresh.addActionListener(e -> {
            cardLayout.show(this, "LOADING");
            loadData();
        });

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnAdd);
        toolbar.add(buttonPanel, BorderLayout.EAST);

        contentPanel.add(toolbar, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{
            "ID Pago", "Licencia", "Fecha", "Monto", "Comprobante", "Notas"
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
        SwingWorker<List<Pago>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Pago> doInBackground() {
                try { Thread.sleep(300); } catch (Exception ignored) {}
                return pagoDAO.getAllPagos();
            }
            @Override
            protected void done() {
                try {
                    List<Pago> pagos = get();
                    for (Pago p : pagos) {
                        tableModel.addRow(new Object[]{
                            p.getIdPago(),
                            p.getInfoLicencia(),
                            p.getFechaPago(),
                            "$" + String.format("%.2f", p.getMonto()),
                            p.getComprobante(),
                            p.getNotas()
                        });
                    }
                    cardLayout.show(PagosPanel.this, "CONTENT");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PagosPanel.this, "Error cargando pagos.", "Error", JOptionPane.ERROR_MESSAGE);
                    cardLayout.show(PagosPanel.this, "CONTENT");
                }
            }
        };
        worker.execute();
    }

    private void showPagoDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Registrar Pago", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Combo for selecting Licencia
        JComboBox<String> cbLicencias = new JComboBox<>();
        List<Licencia> allLicencias = licenciaDAO.getAllLicencias();
        for(Licencia l : allLicencias) {
            cbLicencias.addItem(l.getIdLicencia() + " - " + l.getNombreCliente() + " (" + l.getNombreSistema() + ")");
        }

        JTextField txtMonto = new JTextField();
        JTextField txtComprobante = new JTextField();
        JTextField txtNotas = new JTextField();

        panel.add(new JLabel("Licencia:")); panel.add(cbLicencias);
        panel.add(new JLabel("Monto:")); panel.add(txtMonto);
        panel.add(new JLabel("N° Comprobante:")); panel.add(txtComprobante);
        panel.add(new JLabel("Notas:")); panel.add(txtNotas);

        JButton btnSave = createStyledButton("Guardar Pago", "#10B981", "#34D399");
        btnSave.addActionListener(e -> {
            try {
                int selectedIndex = cbLicencias.getSelectedIndex();
                if(selectedIndex == -1) return;
                
                Licencia selectedL = allLicencias.get(selectedIndex);
                
                Pago p = new Pago();
                p.setIdLicencia(selectedL.getIdLicencia());
                p.setFechaPago(new Date());
                p.setMonto(Double.parseDouble(txtMonto.getText()));
                p.setComprobante(txtComprobante.getText());
                p.setNotas(txtNotas.getText());
                
                if (pagoDAO.insertPago(p)) {
                    dialog.dispose();
                    cardLayout.show(PagosPanel.this, "LOADING");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Error al registrar el pago.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Verifique los datos (el monto debe ser numérico).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(new JLabel(""));
        panel.add(btnSave);

        dialog.add(panel);
        dialog.setVisible(true);
    }
}
