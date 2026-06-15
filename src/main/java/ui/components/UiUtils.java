package ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class UiUtils {
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private UiUtils() {}

    public static JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(AppColors.ROJO);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 14, 8, 14));
        return button;
    }

    public static JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(AppColors.AZUL_OSCURO);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 14, 8, 14));
        return button;
    }

    public static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setForeground(AppColors.AZUL_OSCURO);
        return label;
    }

    public static JPanel card() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.GRIS_BORDE),
                new EmptyBorder(14, 14, 14, 14)
        ));
        return panel;
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(26);
        table.setFillsViewportHeight(true);
        table.setGridColor(AppColors.GRIS_BORDE);
        JTableHeader header = table.getTableHeader();
        header.setBackground(AppColors.AZUL_OSCURO);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 12));
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBorder(new EmptyBorder(2, 6, 2, 6));
        table.setDefaultRenderer(Object.class, renderer);
    }

    public static String date(LocalDateTime dateTime) {
        return dateTime == null ? "" : dateTime.format(DF);
    }

    public static BigDecimal money(String value) {
        try {
            return new BigDecimal(value.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("Ingrese un monto numérico válido.");
        }
    }

    public static int integer(String value, String fieldName) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("Ingrese un valor numérico válido para " + fieldName + ".");
        }
    }
}
