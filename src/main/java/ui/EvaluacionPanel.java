package ui;

import model.EstadoSolicitud;
import model.SolicitudCrediticia;
import model.Usuario;
import service.EvaluacionService;
import ui.components.AppColors;
import ui.components.UiUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class EvaluacionPanel extends JPanel {
    private final Usuario usuario;
    private final EvaluacionService evaluacionService = new EvaluacionService();
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Código", "Cliente", "Producto", "Monto", "Estado"}, 0);
    private final JTable table = new JTable(model);
    private final JTextArea txtDetalle = new JTextArea();
    private final JTextField txtPuntaje = new JTextField("700");
    private final JTextField txtIngreso = new JTextField("3500");
    private final JTextArea txtComentario = new JTextArea(4, 20);
    private SolicitudCrediticia seleccionada;

    public EvaluacionPanel(Usuario usuario) {
        this.usuario = usuario;
        setLayout(new BorderLayout(12, 12));
        setBackground(AppColors.GRIS_CLARO);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        init();
        cargarTabla();
    }

    private void init() {
        add(UiUtils.title("Gestión de Evaluación Crediticia"), BorderLayout.NORTH);
        UiUtils.styleTable(table);
        table.getSelectionModel().addListSelectionListener(e -> seleccionar());

        txtDetalle.setEditable(false);
        txtDetalle.setLineWrap(true);
        txtDetalle.setWrapStyleWord(true);

        JPanel decision = UiUtils.card();
        decision.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6); gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0; decision.add(new JLabel("Puntaje crediticio"), gbc);
        gbc.gridx = 1; decision.add(txtPuntaje, gbc);
        gbc.gridx = 0; gbc.gridy = 1; decision.add(new JLabel("Ingreso validado"), gbc);
        gbc.gridx = 1; decision.add(txtIngreso, gbc);
        gbc.gridx = 0; gbc.gridy = 2; decision.add(new JLabel("Comentario"), gbc);
        gbc.gridx = 1; decision.add(new JScrollPane(txtComentario), gbc);

        JButton btnAprobar = UiUtils.primaryButton("Aprobar");
        JButton btnRechazar = UiUtils.secondaryButton("Rechazar");
        JButton btnObservar = new JButton("Observar");
        btnAprobar.addActionListener(e -> evaluar(EstadoSolicitud.APROBADA));
        btnRechazar.addActionListener(e -> evaluar(EstadoSolicitud.RECHAZADA));
        btnObservar.addActionListener(e -> evaluar(EstadoSolicitud.OBSERVADA));
        gbc.gridx = 0; gbc.gridy = 3; decision.add(btnAprobar, gbc);
        gbc.gridx = 1; decision.add(btnRechazar, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; decision.add(btnObservar, gbc);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(table), new JScrollPane(txtDetalle));
        split.setResizeWeight(0.65);
        add(split, BorderLayout.CENTER);
        add(decision, BorderLayout.SOUTH);
    }

    private void seleccionar() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
        seleccionada = evaluacionService.listarPendientesYObservadas().stream().filter(s -> s.getId() == id).findFirst().orElse(null);
        if (seleccionada != null) {
            txtIngreso.setText(seleccionada.getCliente().getIngresosMensuales().toString());
            txtDetalle.setText("Solicitud: " + seleccionada.getCodigo()
                    + "\nCliente: " + seleccionada.getCliente().getNombreCompleto()
                    + "\nProducto: " + seleccionada.getProducto().getNombre()
                    + "\nMonto: S/ " + seleccionada.getMontoSolicitado()
                    + "\nPlazo: " + seleccionada.getPlazoMeses() + " meses"
                    + "\nFinalidad: " + seleccionada.getFinalidad()
                    + "\nIngresos declarados: S/ " + seleccionada.getCliente().getIngresosMensuales()
                    + "\nEstado actual: " + seleccionada.getEstado());
        }
    }

    private void evaluar(EstadoSolicitud estado) {
        try {
            evaluacionService.evaluar(usuario, seleccionada, UiUtils.integer(txtPuntaje.getText(), "puntaje"), new BigDecimal(txtIngreso.getText().trim()), estado, txtComentario.getText());
            JOptionPane.showMessageDialog(this, "Evaluación registrada: " + estado);
            seleccionada = null; txtDetalle.setText(""); txtComentario.setText("");
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarTabla() {
        model.setRowCount(0);
        List<SolicitudCrediticia> solicitudes = evaluacionService.listarPendientesYObservadas();
        for (SolicitudCrediticia s : solicitudes) {
            model.addRow(new Object[]{s.getId(), s.getCodigo(), s.getCliente().getNombreCompleto(), s.getProducto().getNombre(), s.getMontoSolicitado(), s.getEstado()});
        }
    }
}
