package ui;

import model.Cliente;
import model.Usuario;
import service.ClienteService;
import service.OperacionService;
import service.SolicitudService;
import ui.components.AppColors;
import ui.components.UiUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClientesPanel extends JPanel {
    private final Usuario usuario;
    private final ClienteService clienteService = new ClienteService();
    private final SolicitudService solicitudService = new SolicitudService();
    private final OperacionService operacionService = new OperacionService();
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "DNI", "Cliente", "Correo", "Teléfono", "Ingresos", "Estado"}, 0);
    private final JTable table = new JTable(model);

    private final JTextField txtBuscar = new JTextField();
    private final JTextField txtDni = new JTextField();
    private final JTextField txtNombres = new JTextField();
    private final JTextField txtApellidos = new JTextField();
    private final JTextField txtCorreo = new JTextField();
    private final JTextField txtTelefono = new JTextField();
    private final JTextField txtDireccion = new JTextField();
    private final JTextField txtOcupacion = new JTextField();
    private final JTextField txtIngresos = new JTextField();
    private final JComboBox<String> cboEstado = new JComboBox<>(new String[]{"ACTIVO", "INACTIVO"});
    private Cliente seleccionado;

    public ClientesPanel(Usuario usuario) {
        this.usuario = usuario;
        setLayout(new BorderLayout(12, 12));
        setBackground(AppColors.GRIS_CLARO);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        init();
        cargarTabla(clienteService.listar());
    }

    private void init() {
        add(UiUtils.title("Gestión de Clientes"), BorderLayout.NORTH);
        UiUtils.styleTable(table);
        table.getSelectionModel().addListSelectionListener(e -> seleccionarDesdeTabla());

        JPanel form = UiUtils.card();
        form.setLayout(new GridLayout(6, 4, 8, 8));
        form.add(new JLabel("DNI")); form.add(txtDni);
        form.add(new JLabel("Nombres")); form.add(txtNombres);
        form.add(new JLabel("Apellidos")); form.add(txtApellidos);
        form.add(new JLabel("Correo")); form.add(txtCorreo);
        form.add(new JLabel("Teléfono")); form.add(txtTelefono);
        form.add(new JLabel("Dirección")); form.add(txtDireccion);
        form.add(new JLabel("Ocupación")); form.add(txtOcupacion);
        form.add(new JLabel("Ingresos")); form.add(txtIngresos);
        form.add(new JLabel("Estado")); form.add(cboEstado);

        JButton btnRegistrar = UiUtils.primaryButton("Registrar cliente");
        JButton btnActualizar = UiUtils.secondaryButton("Actualizar cliente");
        JButton btnHistorial = UiUtils.secondaryButton("Consultar historial");
        JButton btnLimpiar = new JButton("Limpiar");
        btnRegistrar.addActionListener(e -> registrar());
        btnActualizar.addActionListener(e -> actualizar());
        btnHistorial.addActionListener(e -> verHistorial());
        btnLimpiar.addActionListener(e -> limpiar());
        form.add(btnRegistrar); form.add(btnActualizar); form.add(btnHistorial); form.add(btnLimpiar);

        JPanel search = new JPanel(new BorderLayout(8, 8));
        search.setOpaque(false);
        JButton btnBuscar = UiUtils.secondaryButton("Buscar");
        btnBuscar.addActionListener(e -> cargarTabla(clienteService.buscar(txtBuscar.getText())));
        search.add(new JLabel("Buscar por DNI, nombre o apellido:"), BorderLayout.WEST);
        search.add(txtBuscar, BorderLayout.CENTER);
        search.add(btnBuscar, BorderLayout.EAST);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.add(search, BorderLayout.NORTH);
        center.add(new JScrollPane(table), BorderLayout.CENTER);

        add(form, BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);
    }

    private void registrar() {
        try {
            clienteService.registrar(usuario, txtDni.getText(), txtNombres.getText(), txtApellidos.getText(), txtCorreo.getText(),
                    txtTelefono.getText(), txtDireccion.getText(), txtOcupacion.getText(), UiUtils.money(txtIngresos.getText()));
            JOptionPane.showMessageDialog(this, "Cliente registrado correctamente.");
            limpiar();
            cargarTabla(clienteService.listar());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizar() {
        try {
            clienteService.actualizar(usuario, seleccionado, txtNombres.getText(), txtApellidos.getText(), txtCorreo.getText(), txtTelefono.getText(),
                    txtDireccion.getText(), txtOcupacion.getText(), UiUtils.money(txtIngresos.getText()), cboEstado.getSelectedItem().toString());
            JOptionPane.showMessageDialog(this, "Cliente actualizado correctamente.");
            cargarTabla(clienteService.listar());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void seleccionarDesdeTabla() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
        seleccionado = clienteService.listar().stream().filter(c -> c.getId() == id).findFirst().orElse(null);
        if (seleccionado != null) {
            txtDni.setText(seleccionado.getDni());
            txtNombres.setText(seleccionado.getNombres());
            txtApellidos.setText(seleccionado.getApellidos());
            txtCorreo.setText(seleccionado.getCorreo());
            txtTelefono.setText(seleccionado.getTelefono());
            txtDireccion.setText(seleccionado.getDireccion());
            txtOcupacion.setText(seleccionado.getOcupacion());
            txtIngresos.setText(seleccionado.getIngresosMensuales().toString());
            cboEstado.setSelectedItem(seleccionado.getEstado());
            txtDni.setEditable(false);
        }
    }

    private void verHistorial() {
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Cliente: ").append(seleccionado.getNombreCompleto()).append("\n\nSolicitudes:\n");
        solicitudService.listarPorCliente(seleccionado).forEach(s -> sb.append("- ").append(s.getCodigo()).append(" | ").append(s.getProducto().getNombre()).append(" | ").append(s.getEstado()).append("\n"));
        sb.append("\nOperaciones:\n");
        operacionService.listarPorCliente(seleccionado).forEach(o -> sb.append("- ").append(o.getTipoOperacion()).append(" | S/ ").append(o.getMonto()).append(" | ").append(UiUtils.date(o.getFechaOperacion())).append("\n"));
        JTextArea area = new JTextArea(sb.toString(), 18, 55);
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Historial del cliente", JOptionPane.INFORMATION_MESSAGE);
    }

    private void limpiar() {
        seleccionado = null;
        txtDni.setEditable(true);
        for (JTextField text : java.util.Arrays.asList(txtDni, txtNombres, txtApellidos, txtCorreo, txtTelefono, txtDireccion, txtOcupacion, txtIngresos)) {
            text.setText("");
        }
        cboEstado.setSelectedItem("ACTIVO");
    }

    private void cargarTabla(List<Cliente> clientes) {
        model.setRowCount(0);
        for (Cliente c : clientes) {
            model.addRow(new Object[]{c.getId(), c.getDni(), c.getNombreCompleto(), c.getCorreo(), c.getTelefono(), c.getIngresosMensuales(), c.getEstado()});
        }
    }
}
