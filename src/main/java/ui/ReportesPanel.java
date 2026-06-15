package ui;

import model.EstadoSolicitud;
import model.Usuario;
import service.ReporteService;
import ui.components.AppColors;
import ui.components.UiUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class ReportesPanel extends JPanel {
    private final ReporteService reporteService = new ReporteService();
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Indicador", "Valor"}, 0);
    private final JTable table = new JTable(model);
    private final DefaultTableModel estadosModel = new DefaultTableModel(new Object[]{"Estado", "Cantidad"}, 0);
    private final JTable estadosTable = new JTable(estadosModel);

    public ReportesPanel(Usuario usuario) {
        setLayout(new BorderLayout(12, 12));
        setBackground(AppColors.GRIS_CLARO);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        init();
        cargarDatos();
    }

    private void init() {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(UiUtils.title("Gestión de Reportes Gerenciales"), BorderLayout.WEST);
        JButton btnActualizar = UiUtils.primaryButton("Actualizar indicadores");
        btnActualizar.addActionListener(e -> cargarDatos());
        top.add(btnActualizar, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        UiUtils.styleTable(table);
        UiUtils.styleTable(estadosTable);
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(table), new JScrollPane(estadosTable));
        split.setResizeWeight(0.60);
        add(split, BorderLayout.CENTER);
    }

    private void cargarDatos() {
        model.setRowCount(0);
        for (Map.Entry<String, String> entry : reporteService.indicadores().entrySet()) {
            model.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
        estadosModel.setRowCount(0);
        for (Map.Entry<EstadoSolicitud, Long> entry : reporteService.solicitudesPorEstado().entrySet()) {
            estadosModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }
}
