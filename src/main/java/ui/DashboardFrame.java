package ui;

import model.Rol;
import model.Usuario;
import ui.components.AppColors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class DashboardFrame extends JFrame {
    private final Usuario usuario;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel content = new JPanel(cardLayout);
    private final Map<String, JPanel> panels = new LinkedHashMap<>();

    public DashboardFrame(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Sistema de Gestión Bancaria - " + usuario.getNombreCompleto());
        setSize(1200, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppColors.GRIS_CLARO);

        JPanel sidebar = new JPanel();
        sidebar.setBackground(AppColors.AZUL_OSCURO);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(16, 12, 16, 12));

        JLabel logo = new JLabel("Scotiabank");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Arial", Font.BOLD, 24));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logo);

        JLabel userLabel = new JLabel("Usuario: " + usuario.getRol());
        userLabel.setForeground(Color.WHITE);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(userLabel);
        sidebar.add(Box.createVerticalStrut(20));

        addModule("Inicio", new InicioPanel(usuario), sidebar);
        if (canAccessClientes()) addModule("Clientes", new ClientesPanel(usuario), sidebar);
        if (canAccessSolicitudes()) addModule("Solicitudes", new SolicitudesPanel(usuario), sidebar);
        if (canAccessEvaluacion()) addModule("Evaluación", new EvaluacionPanel(usuario), sidebar);
        if (canAccessOperaciones()) addModule("Operaciones", new OperacionesPanel(usuario), sidebar);
        if (canAccessReportes()) addModule("Reportes", new ReportesPanel(usuario), sidebar);
        if (canAccessSeguridad()) addModule("Seguridad", new SeguridadPanel(usuario), sidebar);

        sidebar.add(Box.createVerticalGlue());
        JButton btnSalir = new JButton("Cerrar sesión");
        btnSalir.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSalir.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        sidebar.add(btnSalir);

        content.setBackground(AppColors.GRIS_CLARO);
        root.add(sidebar, BorderLayout.WEST);
        root.add(content, BorderLayout.CENTER);
        add(root);
        cardLayout.show(content, "Inicio");
    }

    private void addModule(String name, JPanel panel, JPanel sidebar) {
        panels.put(name, panel);
        content.add(panel, name);
        JButton button = new JButton(name);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        button.setFocusPainted(false);
        button.addActionListener(e -> cardLayout.show(content, name));
        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(8));
    }

    private boolean isAdmin() { return usuario.getRol() == Rol.ADMINISTRADOR; }
    private boolean canAccessClientes() { return isAdmin() || usuario.getRol() == Rol.ASESOR_BANCARIO; }
    private boolean canAccessSolicitudes() { return isAdmin() || usuario.getRol() == Rol.CLIENTE || usuario.getRol() == Rol.ASESOR_BANCARIO; }
    private boolean canAccessEvaluacion() { return isAdmin() || usuario.getRol() == Rol.ANALISTA_CREDITO; }
    private boolean canAccessOperaciones() { return isAdmin() || usuario.getRol() == Rol.ASESOR_BANCARIO; }
    private boolean canAccessReportes() { return isAdmin() || usuario.getRol() == Rol.GERENTE; }
    private boolean canAccessSeguridad() { return isAdmin(); }
}
