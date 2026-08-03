package com.caesolutions.ui;

import com.caesolutions.dao.ClienteDAO;
import com.caesolutions.dao.LicenciaDAO;
import com.caesolutions.dao.PagoDAO;
import com.caesolutions.dao.TicketDAO;
import com.caesolutions.model.Licencia;
import com.caesolutions.model.Pago;
import com.caesolutions.model.Ticket;
import com.caesolutions.util.LoadingPanel;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class HomePanel extends JPanel {

    private ClienteDAO clienteDAO;
    private LicenciaDAO licenciaDAO;
    private PagoDAO pagoDAO;
    private TicketDAO ticketDAO;

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JPanel cardsPanel;
    private JTable tablaLicencias;
    private DefaultTableModel modeloLicencias;
    private JTable tablaTickets;
    private DefaultTableModel modeloTickets;

    public HomePanel() {
        clienteDAO = new ClienteDAO();
        licenciaDAO = new LicenciaDAO();
        pagoDAO = new PagoDAO();
        ticketDAO = new TicketDAO();

        cardLayout = new CardLayout();
        setLayout(cardLayout);
        
        // Pantalla de carga
        add(new LoadingPanel("Cargando métricas del sistema..."), "LOADING");

        // Panel de contenido
        contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setOpaque(false);
        add(contentPanel, "CONTENT");

        initUI();
        
        // Mostrar animación de carga e iniciar consulta
        cardLayout.show(this, "LOADING");
        loadDataAsync();
    }

    private void initUI() {
        // En DashboardFrame ya hay un header global, así que removemos el headerPanel de HomePanel
        // Cambiamos la cuadrícula de nuevo a 1x4 (1 fila, 4 columnas)
        cardsPanel = new JPanel(new GridLayout(1, 4, 20, 20));
        cardsPanel.setOpaque(false);
        contentPanel.add(cardsPanel, BorderLayout.NORTH);
        
        // Tablas inferiores (Split Panel)
        JPanel tablesPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        tablesPanel.setOpaque(false);
        tablesPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Panel de Licencias Activas
        com.caesolutions.util.AntigravityContainer pnlLicencias = new com.caesolutions.util.AntigravityContainer();
        pnlLicencias.setLayout(new BorderLayout(10, 10));
        
        JPanel pnlLicPadding = new JPanel(new BorderLayout(10, 10));
        pnlLicPadding.setOpaque(false);
        pnlLicPadding.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblLicTitle = new JLabel("Licencias Activas");
        lblLicTitle.setFont(new Font("Inter", Font.BOLD, 18));
        lblLicTitle.setForeground(Color.decode("#F8FAFC"));
        pnlLicPadding.add(lblLicTitle, BorderLayout.NORTH);

        modeloLicencias = new DefaultTableModel(new Object[]{"CLIENTE", "PLAN", "ESTADO"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaLicencias = createStyledTable(modeloLicencias);
        JScrollPane scrollLicencias = new JScrollPane(tablaLicencias);
        scrollLicencias.setBorder(BorderFactory.createEmptyBorder());
        scrollLicencias.setOpaque(false);
        scrollLicencias.getViewport().setOpaque(false);
        pnlLicPadding.add(scrollLicencias, BorderLayout.CENTER);
        pnlLicencias.add(pnlLicPadding, BorderLayout.CENTER);

        // Panel de Tickets Recientes
        com.caesolutions.util.AntigravityContainer pnlTickets = new com.caesolutions.util.AntigravityContainer();
        pnlTickets.setLayout(new BorderLayout(10, 10));
        
        JPanel pnlTickPadding = new JPanel(new BorderLayout(10, 10));
        pnlTickPadding.setOpaque(false);
        pnlTickPadding.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTickTitle = new JLabel("Tickets Recientes");
        lblTickTitle.setFont(new Font("Inter", Font.BOLD, 18));
        lblTickTitle.setForeground(Color.decode("#F8FAFC"));
        pnlTickPadding.add(lblTickTitle, BorderLayout.NORTH);

        modeloTickets = new DefaultTableModel(new Object[]{"ASUNTO", "ESTADO"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaTickets = createStyledTable(modeloTickets);
        JScrollPane scrollTickets = new JScrollPane(tablaTickets);
        scrollTickets.setBorder(BorderFactory.createEmptyBorder());
        scrollTickets.setOpaque(false);
        scrollTickets.getViewport().setOpaque(false);
        pnlTickPadding.add(scrollTickets, BorderLayout.CENTER);
        pnlTickets.add(pnlTickPadding, BorderLayout.CENTER);

        tablesPanel.add(pnlLicencias);
        tablesPanel.add(pnlTickets);

        contentPanel.add(tablesPanel, BorderLayout.CENTER);
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(50);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 15));
        
        table.setOpaque(false);
        table.setBackground(new Color(0, 0, 0, 0));
        
        table.setDefaultRenderer(Object.class, new com.caesolutions.util.AntigravityCellRenderer());
        table.getTableHeader().setDefaultRenderer(new com.caesolutions.util.AntigravityHeaderRenderer());
        
        table.putClientProperty(FlatClientProperties.STYLE, 
            "selectionBackground: null; selectionForeground: null; background: null; foreground: #F8FAFC;" +
            "showHorizontalLines: true; showVerticalLines: false; gridColor: #1E3A8A;"
        );
        
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(new Color(0,0,0,0));
        table.getTableHeader().putClientProperty(FlatClientProperties.STYLE, 
            "background: null; foreground: null; separatorColor: #0B1727;"
        );
        return table;
    }

    private void loadDataAsync() {
        SwingWorker<Object[], Void> worker = new SwingWorker<>() {
            @Override
            protected Object[] doInBackground() {
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}

                double[] results = new double[4];
                results[0] = clienteDAO.getAllClientes().size();
                
                List<Licencia> allLicencias = licenciaDAO.getAllLicencias();
                int licenciasActivas = 0;
                for (Licencia l : allLicencias) {
                    if ("Activa".equalsIgnoreCase(l.getEstado())) licenciasActivas++;
                }
                results[1] = licenciasActivas;
                
                List<Ticket> allTickets = ticketDAO.getAllTickets();
                int ticketsAbiertos = 0;
                for (Ticket t : allTickets) {
                    if ("Abierto".equalsIgnoreCase(t.getEstado())) ticketsAbiertos++;
                }
                results[3] = ticketsAbiertos;
                
                double ingresosMes = 0;
                Calendar cal = Calendar.getInstance();
                int currentMonth = cal.get(Calendar.MONTH);
                int currentYear = cal.get(Calendar.YEAR);
                for (Pago p : pagoDAO.getAllPagos()) {
                    if (p.getFechaPago() != null) {
                        cal.setTime(p.getFechaPago());
                        if (cal.get(Calendar.MONTH) == currentMonth && cal.get(Calendar.YEAR) == currentYear) {
                            ingresosMes += p.getMonto();
                        }
                    }
                }
                results[2] = ingresosMes;
                
                return new Object[]{results, allLicencias, allTickets};
            }

            @Override
            protected void done() {
                try {
                    Object[] data = get();
                    double[] results = (double[]) data[0];
                    List<Licencia> lics = (List<Licencia>) data[1];
                    List<Ticket> tcks = (List<Ticket>) data[2];

                    cardsPanel.removeAll();
                    cardsPanel.add(createCard("Total Clientes", String.valueOf((int)results[0]), "👥", "#1E3A8A", "#60A5FA")); 
                    cardsPanel.add(createCard("Licencias Activas", String.valueOf((int)results[1]), "🔑", "#064E3B", "#34D399"));
                    cardsPanel.add(createCard("Ingresos del Mes", String.format("L %.2f", results[2]), "💵", "#78350F", "#FBBF24"));
                    cardsPanel.add(createCard("Tickets Abiertos", String.valueOf((int)results[3]), "🎧", "#7F1D1D", "#F87171"));
                    
                    cardsPanel.revalidate();
                    cardsPanel.repaint();
                    
                    // Llenar tablas
                    modeloLicencias.setRowCount(0);
                    int count = 0;
                    for (Licencia l : lics) {
                        if (count++ >= 5) break;
                        String nombreCli = l.getNombreCliente() != null ? l.getNombreCliente() : "ID: " + l.getIdCliente();
                        String nombrePlan = l.getNombrePlan() != null ? l.getNombrePlan() : "Plan " + l.getIdPlan();
                        modeloLicencias.addRow(new Object[]{nombreCli, nombrePlan, l.getEstado()});
                    }
                    if (modeloLicencias.getRowCount() == 0) {
                        modeloLicencias.addRow(new Object[]{"No hay licencias", "", ""});
                    }

                    modeloTickets.setRowCount(0);
                    count = 0;
                    for (Ticket t : tcks) {
                        if (count++ >= 5) break;
                        modeloTickets.addRow(new Object[]{t.getAsunto(), t.getEstado()});
                    }
                    if (modeloTickets.getRowCount() == 0) {
                        modeloTickets.addRow(new Object[]{"No hay tickets", ""});
                    }
                    
                    cardLayout.show(HomePanel.this, "CONTENT");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private JPanel createCard(String title, String value, String icon, String iconBgColor, String iconFgColor) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.putClientProperty(FlatClientProperties.STYLE, 
            "background: #1E293B; " + // Slate 800
            "arc: 20; " + 
            "border: 20,20,20,20;"
        );

        // Textos (Izquierda)
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Inter", Font.PLAIN, 14));
        lblTitle.setForeground(Color.decode("#94A3B8"));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Inter", Font.BOLD, 32));
        lblValue.setForeground(Color.decode("#F8FAFC"));

        textPanel.add(lblTitle);
        textPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        textPanel.add(lblValue);
        
        // Icono (Derecha)
        JPanel iconWrapper = new JPanel(new GridBagLayout());
        iconWrapper.setPreferredSize(new Dimension(50, 50));
        iconWrapper.setMinimumSize(new Dimension(50, 50));
        iconWrapper.putClientProperty(FlatClientProperties.STYLE, "background: " + iconBgColor + "; arc: 16;");
        
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        lblIcon.setForeground(Color.decode(iconFgColor));
        iconWrapper.add(lblIcon);
        
        // Contenedor principal de la tarjeta
        JPanel mainCardContent = new JPanel(new BorderLayout());
        mainCardContent.setOpaque(false);
        mainCardContent.add(textPanel, BorderLayout.WEST);
        mainCardContent.add(iconWrapper, BorderLayout.EAST);

        card.add(mainCardContent, BorderLayout.CENTER);
        return card;
    }
}
