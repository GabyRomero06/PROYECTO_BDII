package com.caesolutions.ui;

import com.caesolutions.model.Usuario;
import com.caesolutions.util.ImageUtils;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class DashboardFrame extends JFrame {
    private Usuario loggedInUser;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private Map<String, JPanel> loadedPanels = new HashMap<>();

    public DashboardFrame(Usuario user) {
        this.loggedInUser = user;
        setTitle("CAE SOLUTIONS - CRM Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla completa
        setLocationRelativeTo(null);
        
        Image appIcon = ImageUtils.getTransparentLogo(-1, -1);
        if (appIcon != null) {
            setIconImage(appIcon);
        }

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Sidebar (Dark Navy)
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBackground(Color.decode("#0B1120"));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.decode("#1E293B"))); 

        // Logo Area
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBackground(Color.decode("#0B1120"));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        
        Image logoImg = ImageUtils.getTransparentLogo(120, 120);
        JLabel lblLogo;
        if (logoImg != null) {
            lblLogo = new JLabel(new ImageIcon(logoImg));
        } else {
            lblLogo = new JLabel("🛡️");
            lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        }
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.add(lblLogo);
        sidebar.add(logoPanel);

        ButtonGroup navGroup = new ButtonGroup();

        // Navigation Buttons
        JToggleButton btnInicio = createNavButton("Dashboard", "🏠", "inicio", navGroup);
        sidebar.add(btnInicio);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(createNavButton("Clientes", "👥", "clientes", navGroup));
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(createNavButton("Licencias", "🔑", "licencias", navGroup));
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(createNavButton("Pagos & Facturación", "💲", "pagos", navGroup));
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(createNavButton("Soporte Técnico", "🎧", "tickets", navGroup));
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(createNavButton("Configuración", "⚙️", "configuracion", navGroup));
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(createNavButton("Usuarios", "👤", "usuarios", navGroup));
        
        JToggleButton btnLogout = createNavButton("Salir", "🚪", "logout", null);
        btnLogout.putClientProperty(FlatClientProperties.STYLE, 
            "background: #0B1120; " +
            "foreground: #F87171; " +
            "arc: 12; " +
            "borderWidth: 0; " +
            "focusWidth: 0; " +
            "margin: 0,20,0,0; " +
            "hoverBackground: #FEF2F2; hoverForeground: #EF4444;"
        );
        btnLogout.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnLogout);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        add(sidebar, BorderLayout.WEST);

        // Content Wrapper
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(Color.decode("#0F172A"));
        
        // Header Panel
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setBackground(Color.decode("#0F172A"));
        topHeader.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));

        // Left side of header
        JPanel titleArea = new JPanel();
        titleArea.setLayout(new BoxLayout(titleArea, BoxLayout.Y_AXIS));
        titleArea.setOpaque(false);
        JLabel lblMainTitle = new JLabel("Dashboard");
        lblMainTitle.setFont(new Font("Inter", Font.BOLD, 28));
        lblMainTitle.setForeground(Color.decode("#F8FAFC"));
        JLabel lblSubTitle = new JLabel("Panel de CAE Solutions");
        lblSubTitle.setFont(new Font("Inter", Font.PLAIN, 14));
        lblSubTitle.setForeground(Color.decode("#94A3B8"));
        titleArea.add(lblMainTitle);
        titleArea.add(lblSubTitle);

        // Right side of header
        JPanel profileArea = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        profileArea.setOpaque(false);
        
        String nombreUsuario = loggedInUser.getNombreUsuario().toUpperCase();
        String iniciales = nombreUsuario.length() >= 2 ? nombreUsuario.substring(0, 2) : "US";
        
        JLabel lblAvatar = new JLabel(iniciales, SwingConstants.CENTER);
        lblAvatar.setPreferredSize(new Dimension(42, 42));
        lblAvatar.setFont(new Font("Inter", Font.BOLD, 16));
        lblAvatar.setForeground(Color.decode("#F8FAFC"));
        lblAvatar.putClientProperty(FlatClientProperties.STYLE, "background: #2563EB; arc: 999;"); // Blue circle
        
        JLabel lblUserName = new JLabel(nombreUsuario);
        lblUserName.setFont(new Font("Inter", Font.BOLD, 15));
        lblUserName.setForeground(Color.decode("#F8FAFC"));

        profileArea.add(lblAvatar);
        profileArea.add(lblUserName);

        topHeader.add(titleArea, BorderLayout.WEST);
        topHeader.add(profileArea, BorderLayout.EAST);

        contentWrapper.add(topHeader, BorderLayout.NORTH);

        // Main Content Area
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setOpaque(false);
        
        contentWrapper.add(mainContentPanel, BorderLayout.CENTER);
        add(contentWrapper, BorderLayout.CENTER);
        
        // Select Home by default
        btnInicio.setSelected(true);
        showCard("inicio");
    }

    private void showCard(String cardName) {
        if (!loadedPanels.containsKey(cardName)) {
            JPanel newPanel = null;
            switch (cardName) {
                case "inicio": newPanel = new HomePanel(); break;
                case "clientes": newPanel = new ClientesPanel(); break;
                case "licencias": newPanel = new LicenciasPanel(); break;
                case "pagos": newPanel = new PagosPanel(); break;
                case "tickets": newPanel = new TicketsPanel(); break;
                case "usuarios": newPanel = new UsuariosPanel(); break;
                case "configuracion": newPanel = new ConfiguracionPanel(); break;
            }
            if (newPanel != null) {
                mainContentPanel.add(newPanel, cardName);
                loadedPanels.put(cardName, newPanel);
            }
        }
        cardLayout.show(mainContentPanel, cardName);
    }

    private JToggleButton createNavButton(String text, String icon, String cardName, ButtonGroup group) {
        JToggleButton btn = new JToggleButton(icon + "   " + text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(210, 45));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Inter", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        
        btn.putClientProperty(FlatClientProperties.STYLE, 
            "background: #0B1120; " +
            "foreground: #94A3B8; " +
            "arc: 12; " +
            "borderWidth: 0; " +
            "focusWidth: 0; " +
            "margin: 0,20,0,0; " +
            "hoverBackground: #1E293B; " +
            "hoverForeground: #F8FAFC; " +
            "selectedBackground: #1E3A8A; " + // Deep blue
            "selectedForeground: #F8FAFC;"
        );

        if (!cardName.equals("logout")) {
            btn.addActionListener(e -> showCard(cardName));
            if (group != null) group.add(btn);
        }
        return btn;
    }
}
