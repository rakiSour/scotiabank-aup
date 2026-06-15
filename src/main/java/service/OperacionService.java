package service;

import model.*;
import repository.H2Database;

import java.math.BigDecimal;
import java.util.List;

public class OperacionService {
    private final H2Database db = H2Database.getInstance();
    private final AuditoriaService auditoriaService = new AuditoriaService();

    public OperacionBancaria registrar(Usuario usuario, Cliente cliente, SolicitudCrediticia solicitud, String tipoOperacion, BigDecimal monto, String descripcion) {
        if (cliente == null) throw new IllegalArgumentException("Debe seleccionar un cliente.");
        if (tipoOperacion == null || tipoOperacion.trim().isEmpty()) throw new IllegalArgumentException("Debe indicar el tipo de operación.");
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("El monto debe ser mayor a cero.");

        OperacionBancaria operacion = db.insertOperacion(cliente, solicitud, usuario, tipoOperacion, monto, descripcion);
        db.insertMovimiento(operacion, tipoOperacion, monto, monto);
        db.insertComprobante(operacion, "B001", String.format("%08d", operacion.getId()), "COMPROBANTE");
        auditoriaService.registrar(usuario, "Operaciones Bancarias", "Registrar operación", "Operación " + tipoOperacion + " por S/ " + monto);
        return operacion;
    }

    public List<OperacionBancaria> listar() {
        return db.listOperaciones();
    }

    public List<OperacionBancaria> listarPorCliente(Cliente cliente) {
        return db.listOperacionesByCliente(cliente);
    }

    public List<ComprobanteElectronico> listarComprobantes() {
        return db.listComprobantes();
    }
}
