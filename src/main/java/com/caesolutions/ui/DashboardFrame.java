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

        // Sidebar (Dark Navy to Dark Blue Gradient + Glow effect)
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                // Fondo limpio con degradado oscuro suave
                Color color1 = Color.decode("#030c17"); 
                Color color2 = Color.decode("#09182a"); 
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Franja lateral de brillo azul (glow effect) de arriba a abajo
                Color glowEnd = new Color(0, 160, 255, 100);
                Color glowStart = new Color(0, 160, 255, 0);
                GradientPaint glowGp = new GradientPaint(getWidth() - 15, 0, glowStart, getWidth(), 0, glowEnd);
                g2.setPaint(glowGp);
                g2.fillRect(getWidth() - 15, 0, 15, getHeight());
                
                // Borde sólido sutil
                g2.setColor(new Color(0, 180, 255, 180));
                g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());

                g2.dispose();
                super.paintComponent(g);
            }
        };
        sidebar.setOpaque(false);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        // sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.decode("#1E293B"))); // Removed for custom glow

        // Logo Area
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setOpaque(false);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 50, 20)); // Centered and spaced at the top
        
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

        // Navigation Buttons (Columna continua, sin los grandes espacios de antes)
        int space = 12; // Separación incrementada
        JToggleButton btnInicio = createNavButton("Dashboard", "🏠", "inicio", navGroup);
        sidebar.add(btnInicio);
        sidebar.add(Box.createRigidArea(new Dimension(0, space)));
        sidebar.add(createNavButton("Clientes", "👥", "clientes", navGroup));
        sidebar.add(Box.createRigidArea(new Dimension(0, space)));
        sidebar.add(createNavButton("Licencias", "🔑", "licencias", navGroup));
        sidebar.add(Box.createRigidArea(new Dimension(0, space)));
        sidebar.add(createNavButton("Pagos & Facturación", "🧾", "pagos", navGroup));
        sidebar.add(Box.createRigidArea(new Dimension(0, space)));
        sidebar.add(createNavButton("Soporte Técnico", "🎧", "tickets", navGroup));
        sidebar.add(Box.createRigidArea(new Dimension(0, space)));
        sidebar.add(createNavButton("Venta de Equipos", "🖥️", "equipos", navGroup));
        sidebar.add(Box.createRigidArea(new Dimension(0, space)));
        
        JToggleButton btnConfiguracion = createNavButton("Configuración", "⚙️", "configuracion", navGroup);
        sidebar.add(btnConfiguracion);
        sidebar.add(Box.createRigidArea(new Dimension(0, space)));
        
        sidebar.add(createNavButton("Usuarios", "👤", "usuarios", navGroup));
        
        JToggleButton btnLogout = createNavButton("Salir", "🚪", "logout", null);
        btnLogout.putClientProperty(FlatClientProperties.STYLE, 
            "background: rgba(0,0,0,0); " +
            "foreground: #F8FAFC; " +
            "arc: 16; " +
            "borderWidth: 0; " +
            "focusWidth: 0; " +
            "margin: 12,25,12,20; " +
            "hoverBackground: rgba(239,68,68,0.1); hoverForeground: #EF4444;"
        );
        btnLogout.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_BORDERLESS);
        btnLogout.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnLogout);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

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
        
        // Select Configuración by default
        btnConfiguracion.setSelected(true);
        showCard("configuracion");
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
                case "equipos": 
                    newPanel = new JPanel(new BorderLayout()); 
                    newPanel.setOpaque(false);
                    JLabel lbl = new JLabel("Módulo de Venta de Equipos en Construcción", SwingConstants.CENTER);
                    lbl.setForeground(Color.WHITE);
                    lbl.setFont(new Font("Inter", Font.BOLD, 18));
                    newPanel.add(lbl, BorderLayout.CENTER);
                    break;
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
        btn.setMaximumSize(new Dimension(230, 48)); // Altura ampliada
        btn.setFocusPainted(false);
        btn.setFont(new Font("Inter", Font.BOLD, 16)); // Letra más grandecita
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        
        btn.putClientProperty(FlatClientProperties.STYLE, 
            "background: rgba(0,0,0,0); " + 
            "foreground: #E2E8F0; " +
            "arc: 16; " + 
            "borderWidth: 0; " +
            "focusWidth: 0; " +
            "margin: 12,25,12,20; " + // Mayor separación interna
            "hoverBackground: rgba(255,255,255,0.05); " +
            "hoverForeground: #F8FAFC; " +
            "selectedBackground: #0284C7; " + 
            "selectedForeground: #BAE6FD;" 
        );
        
        // This makes the toggle button act like a toolbar/tab button (transparent by default)
        btn.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_BORDERLESS);

        if (!cardName.equals("logout")) {
            btn.addActionListener(e -> showCard(cardName));
            if (group != null) group.add(btn);
        }
        return btn;
    }
}
