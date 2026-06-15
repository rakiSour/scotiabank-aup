package ui;

import model.Usuario;
import service.ReporteService;
import ui.components.AppColors;
import ui.components.UiUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class InicioPanel extends JPanel {
    private final ReporteService reporteService = new ReporteService();

    public InicioPanel(Usuario usuario) {
        setLayout(new BorderLayout());
        setBackground(AppColors.GRIS_CLARO);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel top = new JPanel(new GridLayout(2, 1));
        top.setOpaque(false);
        top.add(UiUtils.title("Dashboard Principal"));
        top.add(new JLabel("Bienvenido, " + usuario.getNombreCompleto() + " | Rol: " + usuario.getRol()));
        add(top, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(3, 3, 12, 12));
        grid.setOpaque(false);
        for (Map.Entry<String, String> entry : reporteService.indicadores().entrySet()) {
            JPanel card = UiUtils.card();
            card.setLayout(new BorderLayout());
            JLabel label = new JLabel(entry.getKey());
            JLabel value = new JLabel(entry.getValue());
            value.setFont(new Font("Arial", Font.BOLD, 24));
            value.setForeground(AppColors.ROJO);
            card.add(label, BorderLayout.NORTH);
            card.add(value, BorderLayout.CENTER);
            grid.add(card);
        }
        add(grid, BorderLayout.CENTER);
    }
}
