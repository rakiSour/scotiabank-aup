package service;

import model.*;
import repository.H2Database;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SolicitudService {
    private final H2Database db = H2Database.getInstance();
    private final AuditoriaService auditoriaService = new AuditoriaService();

    public SolicitudCrediticia registrar(Usuario usuario, Cliente cliente, ProductoFinanciero producto, BigDecimal monto, int plazoMeses, String finalidad) {
        if (cliente == null) throw new IllegalArgumentException("Debe seleccionar un cliente.");
        if (producto == null) throw new IllegalArgumentException("Debe seleccionar un producto financiero.");
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("El monto debe ser mayor a cero.");
        if (plazoMeses <= 0) throw new IllegalArgumentException("El plazo debe ser mayor a cero.");
        if (finalidad == null || finalidad.trim().isEmpty()) throw new IllegalArgumentException("La finalidad es obligatoria.");
        String codigo = generarCodigo(producto.getTipo());
        SolicitudCrediticia solicitud = db.insertSolicitud(codigo, cliente, producto, usuario, monto, plazoMeses, finalidad.trim());
        auditoriaService.registrar(usuario, "Solicitud Crediticia", "Registrar solicitud", "Solicitud " + codigo + " registrada en estado PENDIENTE");
        return solicitud;
    }

    public List<SolicitudCrediticia> listar() {
        return db.listSolicitudes();
    }

    public List<SolicitudCrediticia> listarPorEstado(EstadoSolicitud estado) {
        return db.listSolicitudesByEstado(estado);
    }

    public List<SolicitudCrediticia> listarPorCliente(Cliente cliente) {
        return db.listSolicitudesByCliente(cliente);
    }

    private String generarCodigo(TipoProducto tipo) {
        String prefijo = tipo == TipoProducto.PRESTAMO ? "PRE" : "TAR";
        String fecha = java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int correlativo = db.total("solicitudes") + 1;
        return prefijo + "-" + fecha + "-" + String.format("%04d", correlativo);
    }
}
