package com.caesolutions.ui;

import com.caesolutions.dao.ClienteDAO;
import com.caesolutions.dao.LicenciaDAO;
import com.caesolutions.dao.PlanDAO;
import com.caesolutions.dao.SistemaDAO;
import com.caesolutions.model.Cliente;
import com.caesolutions.model.Licencia;
import com.caesolutions.model.Plan;
import com.caesolutions.model.Sistema;
import com.caesolutions.util.LoadingPanel;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class LicenciasPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private LicenciaDAO licenciaDAO;
    private ClienteDAO clienteDAO;
    private SistemaDAO sistemaDAO;
    private PlanDAO planDAO;

    private CardLayout cardLayout;
    private JPanel contentPanel;

    public LicenciasPanel() {
        licenciaDAO = new LicenciaDAO();
        clienteDAO = new ClienteDAO();
        sistemaDAO = new SistemaDAO();
        planDAO = new PlanDAO();
        
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        
        add(new LoadingPanel("Cargando licencias..."), "LOADING");
        
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
        JLabel lblTitle = new JLabel("Gestión de Licencias");
        lblTitle.setFont(new Font("Inter", Font.BOLD, 24));
        toolbar.add(lblTitle, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton btnAdd = createStyledButton("Nueva Licencia", "#2563EB", "#3B82F6"); // Blue
        btnAdd.addActionListener(e -> showLicenciaDialog());
        
        JButton btnDelete = createStyledButton("Eliminar", "#DC2626", "#EF4444"); // Red
        btnDelete.addActionListener(e -> deleteSelectedLicencia());
        
        JButton btnRefresh = createStyledButton("Refrescar", "#475569", "#64748B"); // Slate
        btnRefresh.addActionListener(e -> {
            cardLayout.show(this, "LOADING");
            loadData();
        });

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnAdd);
        toolbar.add(buttonPanel, BorderLayout.EAST);

        contentPanel.add(toolbar, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new Object[]{
            "ID", "CLIENTE", "PLAN ASIGNADO", "<html>FECHA<br>INICIO</html>", "<html>PRÓXIMO<br>PAGO</html>", "<html>DEUDA<br>(MESES)</html>", "<html>TOTAL DEUDA<br>+ RECARGOS</html>", "ESTADO", "ACCIONES"
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
        SwingWorker<List<Licencia>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Licencia> doInBackground() {
                try { Thread.sleep(300); } catch (Exception ignored) {}
                return licenciaDAO.getAllLicencias();
            }

            @Override
            protected void done() {
                try {
                    List<Licencia> licencias = get();
                    for (Licencia l : licencias) {
                        String clienteHtml = "<html><b>" + l.getNombreCliente() + "</b><br><span style='font-size:10px; font-weight:normal; color:#94A3B8'>" + l.getIdCliente() + "</span></html>";
                        String fechaInicio = new java.text.SimpleDateFormat("yyyy-MM-dd").format(l.getFechaAdquisicion());
                        String proxPago = new java.text.SimpleDateFormat("yyyy-MM-dd").format(l.getFechaProximoPago());
                        String deudaStr = l.getDeudaMeses() + " meses";
                        String totalDeuda = "L " + String.format("%.2f", l.getPrecioAcordado() + l.getRecargoAplicado());

                        tableModel.addRow(new Object[]{
                            l.getIdLicencia(),
                            clienteHtml,
                            l.getNombrePlan(),
                            fechaInicio,
                            proxPago,
                            deudaStr,
                            totalDeuda,
                            l.getEstado(),
                            "" // Badge will be drawn by renderer
                        });
                    }
                    cardLayout.show(LicenciasPanel.this, "CONTENT");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LicenciasPanel.this, "Error cargando licencias.", "Error", JOptionPane.ERROR_MESSAGE);
                    cardLayout.show(LicenciasPanel.this, "CONTENT");
                }
            }
        };
        worker.execute();
    }

    private void showLicenciaDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nueva Licencia", true);
        dialog.setSize(450, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<Cliente> cbClientes = new JComboBox<>();
        for(Cliente c : clienteDAO.getAllClientes()) cbClientes.addItem(c);
        
        JComboBox<Sistema> cbSistemas = new JComboBox<>();
        for(Sistema s : sistemaDAO.getAllSistemas()) cbSistemas.addItem(s);
        
        JComboBox<Plan> cbPlanes = new JComboBox<>();
        for(Plan p : planDAO.getAllPlanes()) cbPlanes.addItem(p);

        JTextField txtPrecio = new JTextField();

        panel.add(new JLabel("Cliente:")); panel.add(cbClientes);
        panel.add(new JLabel("Sistema:")); panel.add(cbSistemas);
        panel.add(new JLabel("Plan:")); panel.add(cbPlanes);
        panel.add(new JLabel("Precio Acordado:")); panel.add(txtPrecio);
        
        // Simplified default values
        panel.add(new JLabel("Adquisición:")); panel.add(new JLabel("Hoy"));
        panel.add(new JLabel("Próximo Pago:")); panel.add(new JLabel("En 30 días"));

        JButton btnSave = createStyledButton("Guardar", "#2563EB", "#3B82F6");
        btnSave.addActionListener(e -> {
            try {
                Cliente selectedC = (Cliente) cbClientes.getSelectedItem();
                Sistema selectedS = (Sistema) cbSistemas.getSelectedItem();
                Plan selectedP = (Plan) cbPlanes.getSelectedItem();
                
                Licencia l = new Licencia();
                l.setIdCliente(selectedC.getIdCliente());
                l.setIdSistema(selectedS.getIdSistema());
                l.setIdPlan(selectedP.getIdPlan());
                l.setPrecioAcordado(Double.parseDouble(txtPrecio.getText()));
                l.setEstado("Activa");
                
                Date today = new Date();
                Date nextMonth = new Date(today.getTime() + (30L * 24 * 60 * 60 * 1000));
                l.setFechaAdquisicion(today);
                l.setFechaProximoPago(nextMonth);
                l.setDeudaMeses(0);
                l.setRecargoAplicado(0.0);

                if (licenciaDAO.insertLicencia(l)) {
                    dialog.dispose();
                    cardLayout.show(LicenciasPanel.this, "LOADING");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Error al guardar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Verifique los datos (el precio debe ser número).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(new JLabel(""));
        panel.add(btnSave);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void deleteSelectedLicencia() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una licencia para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar esta licencia?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (licenciaDAO.deleteLicencia(id)) {
                cardLayout.show(this, "LOADING");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Error eliminando la licencia.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
