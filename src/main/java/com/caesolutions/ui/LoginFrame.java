package com.caesolutions.ui;

import com.caesolutions.dao.UsuarioDAO;
import com.caesolutions.model.Usuario;
import com.caesolutions.util.ImageUtils;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private UsuarioDAO usuarioDAO;

    public LoginFrame() {
        usuarioDAO = new UsuarioDAO();
        setTitle("CAE Solutions - Iniciar Sesión");
        setSize(850, 550); // Más ancho para diseño de pantalla dividida (split view)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla
        setResizable(false);
        
        Image appIcon = ImageUtils.getTransparentLogo(-1, -1);
        if (appIcon != null) {
            setIconImage(appIcon);
        }

        initUI();
    }

    private void initUI() {
        // Contenedor Principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // --- Panel Izquierdo (Branding) ---
        JPanel brandingPanel = new JPanel();
        brandingPanel.setLayout(new BoxLayout(brandingPanel, BoxLayout.Y_AXIS));
        brandingPanel.setPreferredSize(new Dimension(400, 550));
        brandingPanel.setBackground(Color.decode("#0B1120")); // Very dark navy
        brandingPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        Image logoImg = ImageUtils.getTransparentLogo(150, 150);
        JLabel lblLogo;
        if (logoImg != null) {
            lblLogo = new JLabel(new ImageIcon(logoImg));
        } else {
            lblLogo = new JLabel("🛡️");
            lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        }
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblAppTitle = new JLabel("CAE SOLUTIONS");
        lblAppTitle.setFont(new Font("Inter", Font.BOLD, 32));
        lblAppTitle.setForeground(Color.decode("#F8FAFC"));
        lblAppTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblDesc = new JLabel("<html><center>Plataforma integral para<br>gestión de clientes y licencias.</center></html>");
        lblDesc.setFont(new Font("Inter", Font.PLAIN, 15));
        lblDesc.setForeground(Color.decode("#94A3B8"));
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        brandingPanel.add(Box.createVerticalGlue());
        brandingPanel.add(lblLogo);
        brandingPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        brandingPanel.add(lblAppTitle);
        brandingPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        brandingPanel.add(lblDesc);
        brandingPanel.add(Box.createVerticalGlue());
        
        // --- Panel Derecho (Formulario) ---
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.decode("#0F172A")); // Slate 900
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 60, 0, 60));

        JLabel lblWelcome = new JLabel("¡Bienvenido!");
        lblWelcome.setFont(new Font("Inter", Font.BOLD, 36));
        lblWelcome.setForeground(Color.decode("#F8FAFC"));
        lblWelcome.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblSub = new JLabel("Ingresa tus credenciales para continuar.");
        lblSub.setFont(new Font("Inter", Font.PLAIN, 15));
        lblSub.setForeground(Color.decode("#94A3B8"));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Usuario
        JLabel lblUser = new JLabel("Nombre de Usuario");
        lblUser.setFont(new Font("Inter", Font.BOLD, 13));
        lblUser.setForeground(Color.decode("#E2E8F0"));
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtUsername = new JTextField();
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "usuario");
        txtUsername.putClientProperty(FlatClientProperties.STYLE, 
            "background: #1E293B; " +
            "foreground: #F8FAFC; " +
            "arc: 12; " +
            "borderWidth: 1; " +
            "borderColor: #334155; " +
            "margin: 5,15,5,15;"
        );

        // Contraseña
        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(new Font("Inter", Font.BOLD, 13));
        lblPass.setForeground(Color.decode("#E2E8F0"));
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "••••••••");
        txtPassword.putClientProperty(FlatClientProperties.STYLE, 
            "background: #1E293B; " +
            "foreground: #F8FAFC; " +
            "arc: 12; " +
            "borderWidth: 1; " +
            "borderColor: #334155; " +
            "showRevealButton: true; " +
            "margin: 5,15,5,15;"
        );

        // Boton
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnLogin.setFont(new Font("Inter", Font.BOLD, 15));
        btnLogin.putClientProperty(FlatClientProperties.STYLE, 
            "background: #2563EB; " + // Blue 600
            "foreground: #FFFFFF; " +
            "arc: 12; " +
            "borderWidth: 0; " +
            "focusWidth: 0; " +
            "hoverBackground: #3B82F6; " + // Blue 500
            "pressedBackground: #1D4ED8;" // Blue 700
        );
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.addActionListener(this::performLogin);

        // Ensamblar Panel Derecho
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(lblWelcome);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        rightPanel.add(lblSub);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 45)));
        
        rightPanel.add(lblUser);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        rightPanel.add(txtUsername);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        rightPanel.add(lblPass);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        rightPanel.add(txtPassword);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 45)));
        
        rightPanel.add(btnLogin);
        rightPanel.add(Box.createVerticalGlue());

        mainPanel.add(brandingPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }

    private void performLogin(ActionEvent e) {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese usuario y contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario loggedInUser = usuarioDAO.authenticate(user, pass);

        if (loggedInUser != null) {
            DashboardFrame dashboard = new DashboardFrame(loggedInUser);
            dashboard.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }
}
