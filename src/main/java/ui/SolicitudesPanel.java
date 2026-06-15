package ui;

import model.Cliente;
import model.ProductoFinanciero;
import model.SolicitudCrediticia;
import model.Usuario;
import service.ClienteService;
import service.ProductoService;
import service.SolicitudService;
import ui.components.AppColors;
import ui.components.UiUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SolicitudesPanel extends JPanel {
    private final Usuario usuario;
    private final ClienteService clienteService = new ClienteService();
    private final ProductoService productoService = new ProductoService();
    private final SolicitudService solicitudService = new SolicitudService();
    private final JComboBox<Cliente> cboCliente = new JComboBox<>();
    private final JComboBox<ProductoFinanciero> cboProducto = new JComboBox<>();
    private final JTextField txtMonto = new JTextField();
    private final JTextField txtPlazo = new JTextField();
    private final JTextArea txtFinalidad = new JTextArea(3, 20);
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Código", "Cliente", "Producto", "Monto", "Plazo", "Estado", "Fecha"}, 0);
    private final JTable table = new JTable(model);

    public SolicitudesPanel(Usuario usuario) {
        this.usuario = usuario;
        setLayout(new BorderLayout(12, 12));
        setBackground(AppColors.GRIS_CLARO);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        init();
        cargarCombos();
        cargarTabla();
    }

    private void init() {
        add(UiUtils.title("Gestión de Solicitud Crediticia"), BorderLayout.NORTH);
        UiUtils.styleTable(table);

        JPanel form = UiUtils.card();
        form.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Cliente"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; form.add(cboCliente, gbc);
        gbc.gridx = 2; gbc.weightx = 0; form.add(new JLabel("Producto"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; form.add(cboProducto, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; form.add(new JLabel("Monto solicitado"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; form.add(txtMonto, gbc);
        gbc.gridx = 2; gbc.weightx = 0; form.add(new JLabel("Plazo meses"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; form.add(txtPlazo, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; form.add(new JLabel("Finalidad"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; form.add(new JScrollPane(txtFinalidad), gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        JButton btnRegistrar = UiUtils.primaryButton("Enviar solicitud");
        btnRegistrar.addActionListener(e -> registrar());
        form.add(btnRegistrar, gbc);

        add(form, BorderLayout.SOUTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void cargarCombos() {
        cboCliente.removeAllItems();
        clienteService.listar().forEach(cboCliente::addItem);
        cboProducto.removeAllItems();
        productoService.listarActivos().forEach(cboProducto::addItem);
    }

    private void registrar() {
        try {
            SolicitudCrediticia solicitud = solicitudService.registrar(usuario, (Cliente) cboCliente.getSelectedItem(),
                    (ProductoFinanciero) cboProducto.getSelectedItem(), UiUtils.money(txtMonto.getText()), UiUtils.integer(txtPlazo.getText(), "plazo"), txtFinalidad.getText());
            JOptionPane.showMessageDialog(this, "Solicitud registrada correctamente. Código: " + solicitud.getCodigo());
            txtMonto.setText(""); txtPlazo.setText(""); txtFinalidad.setText("");
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarTabla() {
        model.setRowCount(0);
        for (SolicitudCrediticia s : solicitudService.listar()) {
            model.addRow(new Object[]{s.getCodigo(), s.getCliente().getNombreCompleto(), s.getProducto().getNombre(), s.getMontoSolicitado(), s.getPlazoMeses(), s.getEstado(), UiUtils.date(s.getFechaRegistro())});
        }
    }
}
