package ui;

import model.Rol;
import model.Usuario;
import service.AuditoriaService;
import service.SeguridadService;
import ui.components.AppColors;
import ui.components.UiUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SeguridadPanel extends JPanel {
    private final Usuario admin;
    private final SeguridadService seguridadService = new SeguridadService();
    private final AuditoriaService auditoriaService = new AuditoriaService();
    private final DefaultTableModel usuariosModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Correo", "Rol", "Activo"}, 0);
    private final JTable usuariosTable = new JTable(usuariosModel);
    private final DefaultTableModel auditoriaModel = new DefaultTableModel(new Object[]{"ID", "Usuario", "Módulo", "Acción", "Descripción", "Fecha"}, 0);
    private final JTable auditoriaTable = new JTable(auditoriaModel);

    private final JTextField txtNombres = new JTextField();
    private final JTextField txtApellidos = new JTextField();
    private final JTextField txtCorreo = new JTextField();
    private final JTextField txtPassword = new JTextField("123456");
    private final JComboBox<Rol> cboRol = new JComboBox<>(Rol.values());
    private Usuario seleccionado;

    public SeguridadPanel(Usuario admin) {
        this.admin = admin;
        setLayout(new BorderLayout(12, 12));
        setBackground(AppColors.GRIS_CLARO);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        init();
        cargarUsuarios();
        cargarAuditoria();
    }

    private void init() {
        add(UiUtils.title("Gestión de Seguridad y Auditoría"), BorderLayout.NORTH);
        UiUtils.styleTable(usuariosTable);
        UiUtils.styleTable(auditoriaTable);
        usuariosTable.getSelectionModel().addListSelectionListener(e -> seleccionarUsuario());

        JPanel form = UiUtils.card();
        form.setLayout(new GridLayout(4, 4, 8, 8));
        form.add(new JLabel("Nombres")); form.add(txtNombres);
        form.add(new JLabel("Apellidos")); form.add(txtApellidos);
        form.add(new JLabel("Correo")); form.add(txtCorreo);
        form.add(new JLabel("Contraseña")); form.add(txtPassword);
        form.add(new JLabel("Rol")); form.add(cboRol);

        JButton btnCrear = UiUtils.primaryButton("Crear usuario");
        JButton btnCambiarRol = UiUtils.secondaryButton("Cambiar rol");
        JButton btnEstado = new JButton("Activar / desactivar");
        JButton btnActualizar = new JButton("Actualizar tablas");
        btnCrear.addActionListener(e -> crearUsuario());
        btnCambiarRol.addActionListener(e -> cambiarRol());
        btnEstado.addActionListener(e -> cambiarEstado());
        btnActualizar.addActionListener(e -> { cargarUsuarios(); cargarAuditoria(); });
        form.add(btnCrear); form.add(btnCambiarRol); form.add(btnEstado); form.add(btnActualizar);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(usuariosTable), new JScrollPane(auditoriaTable));
        split.setResizeWeight(0.45);
        add(split, BorderLayout.CENTER);
        add(form, BorderLayout.SOUTH);
    }

    private void crearUsuario() {
        try {
            seguridadService.crearUsuario(admin, txtNombres.getText(), txtApellidos.getText(), txtCorreo.getText(), txtPassword.getText(), (Rol) cboRol.getSelectedItem());
            JOptionPane.showMessageDialog(this, "Usuario creado correctamente.");
            txtNombres.setText(""); txtApellidos.setText(""); txtCorreo.setText(""); txtPassword.setText("123456");
            cargarUsuarios(); cargarAuditoria();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cambiarRol() {
        try {
            seguridadService.cambiarRol(admin, seleccionado, (Rol) cboRol.getSelectedItem());
            cargarUsuarios(); cargarAuditoria();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cambiarEstado() {
        try {
            seguridadService.cambiarEstado(admin, seleccionado);
            cargarUsuarios(); cargarAuditoria();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void seleccionarUsuario() {
        int row = usuariosTable.getSelectedRow();
        if (row < 0) return;
        int id = Integer.parseInt(usuariosTable.getValueAt(row, 0).toString());
        seleccionado = seguridadService.listarUsuarios().stream().filter(u -> u.getId() == id).findFirst().orElse(null);
        if (seleccionado != null) {
            txtNombres.setText(seleccionado.getNombres());
            txtApellidos.setText(seleccionado.getApellidos());
            txtCorreo.setText(seleccionado.getCorreo());
            cboRol.setSelectedItem(seleccionado.getRol());
        }
    }

    private void cargarUsuarios() {
        usuariosModel.setRowCount(0);
        seguridadService.listarUsuarios().forEach(u -> usuariosModel.addRow(new Object[]{u.getId(), u.getNombreCompleto(), u.getCorreo(), u.getRol(), u.isActivo()}));
    }

    private void cargarAuditoria() {
        auditoriaModel.setRowCount(0);
        auditoriaService.listar().forEach(a -> auditoriaModel.addRow(new Object[]{
                a.getId(), a.getUsuario() == null ? "Sistema" : a.getUsuario().getCorreo(), a.getModulo(), a.getAccion(), a.getDescripcion(), UiUtils.date(a.getFechaHora())
        }));
    }
}
