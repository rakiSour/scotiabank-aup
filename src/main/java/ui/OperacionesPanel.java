package ui;

import model.Cliente;
import model.EstadoSolicitud;
import model.SolicitudCrediticia;
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

public class OperacionesPanel extends JPanel {
    private final Usuario usuario;
    private final ClienteService clienteService = new ClienteService();
    private final SolicitudService solicitudService = new SolicitudService();
    private final OperacionService operacionService = new OperacionService();
    private final JComboBox<Cliente> cboCliente = new JComboBox<>();
    private final JComboBox<SolicitudCrediticia> cboSolicitud = new JComboBox<>();
    private final JComboBox<String> cboTipo = new JComboBox<>(new String[]{"DESEMBOLSO", "PAGO", "TRANSFERENCIA", "CONSULTA"});
    private final JTextField txtMonto = new JTextField();
    private final JTextArea txtDescripcion = new JTextArea(3, 20);
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Cliente", "Solicitud", "Tipo", "Monto", "Estado", "Fecha"}, 0);
    private final JTable table = new JTable(model);

    public OperacionesPanel(Usuario usuario) {
        this.usuario = usuario;
        setLayout(new BorderLayout(12, 12));
        setBackground(AppColors.GRIS_CLARO);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        init();
        cargarCombos();
        cargarTabla();
    }

    private void init() {
        add(UiUtils.title("Gestión de Operaciones Bancarias"), BorderLayout.NORTH);
        UiUtils.styleTable(table);
        cboCliente.addActionListener(e -> cargarSolicitudesAprobadas());

        JPanel form = UiUtils.card();
        form.setLayout(new GridLayout(5, 2, 8, 8));
        form.add(new JLabel("Cliente")); form.add(cboCliente);
        form.add(new JLabel("Solicitud aprobada")); form.add(cboSolicitud);
        form.add(new JLabel("Tipo de operación")); form.add(cboTipo);
        form.add(new JLabel("Monto")); form.add(txtMonto);
        form.add(new JLabel("Descripción")); form.add(new JScrollPane(txtDescripcion));

        JButton btnRegistrar = UiUtils.primaryButton("Registrar operación y comprobante");
        btnRegistrar.addActionListener(e -> registrar());

        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel south = new JPanel(new BorderLayout(8, 8));
        south.setOpaque(false);
        south.add(form, BorderLayout.CENTER);
        south.add(btnRegistrar, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);
    }

    private void cargarCombos() {
        cboCliente.removeAllItems();
        clienteService.listar().forEach(cboCliente::addItem);
        cargarSolicitudesAprobadas();
    }

    private void cargarSolicitudesAprobadas() {
        cboSolicitud.removeAllItems();
        Cliente cliente = (Cliente) cboCliente.getSelectedItem();
        if (cliente == null) return;
        solicitudService.listarPorCliente(cliente).stream()
                .filter(s -> s.getEstado() == EstadoSolicitud.APROBADA)
                .forEach(cboSolicitud::addItem);
    }

    private void registrar() {
        try {
            operacionService.registrar(usuario, (Cliente) cboCliente.getSelectedItem(), (SolicitudCrediticia) cboSolicitud.getSelectedItem(),
                    cboTipo.getSelectedItem().toString(), UiUtils.money(txtMonto.getText()), txtDescripcion.getText());
            JOptionPane.showMessageDialog(this, "Operación registrada y comprobante generado.");
            txtMonto.setText(""); txtDescripcion.setText("");
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarTabla() {
        model.setRowCount(0);
        operacionService.listar().forEach(o -> model.addRow(new Object[]{
                o.getId(), o.getCliente().getNombreCompleto(), o.getSolicitud() == null ? "-" : o.getSolicitud().getCodigo(),
                o.getTipoOperacion(), o.getMonto(), o.getEstado(), UiUtils.date(o.getFechaOperacion())
        }));
    }
}
