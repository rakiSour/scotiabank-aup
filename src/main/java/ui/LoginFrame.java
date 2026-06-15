package ui;

import model.Usuario;
import service.AuthService;
import ui.components.AppColors;
import ui.components.UiUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final AuthService authService = new AuthService();
    private final JTextField txtCorreo = new JTextField("admin@scotia.com");
    private final JPasswordField txtPassword = new JPasswordField("123456");

    public LoginFrame() {
        setTitle("Scotiabank - Plataforma Integral de Gestión Bancaria Ágil");
        setSize(520, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppColors.GRIS_CLARO);
        root.setBorder(new EmptyBorder(25, 45, 25, 45));

        JLabel title = new JLabel("SCOTIABANK", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(AppColors.ROJO);

        JLabel subtitle = new JLabel("Plataforma Integral de Gestión Bancaria Ágil", SwingConstants.CENTER);
        subtitle.setForeground(AppColors.AZUL_OSCURO);

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        header.add(title);
        header.add(subtitle);
        root.add(header, BorderLayout.NORTH);

        JPanel form = UiUtils.card();
        form.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Correo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(txtCorreo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        form.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(txtPassword, gbc);

        JButton btnLogin = UiUtils.primaryButton("Ingresar");
        btnLogin.addActionListener(e -> login());
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        form.add(btnLogin, gbc);

        JTextArea ayuda = new JTextArea("Usuarios demo:\nadmin@scotia.com / 123456\nasesor@scotia.com / 123456\nanalista@scotia.com / 123456\ngerente@scotia.com / 123456\ncliente@scotia.com / 123456");
        ayuda.setEditable(false);
        ayuda.setFont(new Font("Arial", Font.PLAIN, 12));
        ayuda.setOpaque(false);
        ayuda.setForeground(Color.DARK_GRAY);
        gbc.gridy = 3;
        form.add(ayuda, gbc);

        root.add(form, BorderLayout.CENTER);
        add(root);
    }

    private void login() {
        String correo = txtCorreo.getText();
        String pass = new String(txtPassword.getPassword());
        java.util.Optional<Usuario> usuarioEncontrado = authService.login(correo, pass);
        if (usuarioEncontrado.isPresent()) {
            openDashboard(usuarioEncontrado.get());
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas o usuario inactivo.", "Acceso denegado", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openDashboard(Usuario usuario) {
        new DashboardFrame(usuario).setVisible(true);
        dispose();
    }
}
