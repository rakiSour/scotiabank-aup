package service;

import model.EstadoSolicitud;
import model.OperacionBancaria;
import model.SolicitudCrediticia;
import repository.H2Database;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ReporteService {
    private final H2Database db = H2Database.getInstance();

    public Map<String, String> indicadores() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("Clientes registrados", String.valueOf(db.total("clientes")));
        map.put("Solicitudes totales", String.valueOf(db.total("solicitudes")));
        map.put("Solicitudes pendientes", String.valueOf(contarEstado(EstadoSolicitud.PENDIENTE)));
        map.put("Solicitudes aprobadas", String.valueOf(contarEstado(EstadoSolicitud.APROBADA)));
        map.put("Solicitudes rechazadas", String.valueOf(contarEstado(EstadoSolicitud.RECHAZADA)));
        map.put("Operaciones registradas", String.valueOf(db.total("operaciones")));
        map.put("Comprobantes emitidos", String.valueOf(db.total("comprobantes")));
        map.put("Auditorías registradas", String.valueOf(db.total("auditorias")));
        BigDecimal monto = db.listOperaciones().stream().map(OperacionBancaria::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add);
        map.put("Monto total operaciones", "S/ " + monto);
        return map;
    }

    public Map<EstadoSolicitud, Long> solicitudesPorEstado() {
        Map<EstadoSolicitud, Long> base = new EnumMap<>(EstadoSolicitud.class);
        for (EstadoSolicitud estado : EstadoSolicitud.values()) base.put(estado, 0L);
        base.putAll(db.listSolicitudes().stream().collect(Collectors.groupingBy(SolicitudCrediticia::getEstado, () -> new EnumMap<>(EstadoSolicitud.class), Collectors.counting())));
        return base;
    }

    private long contarEstado(EstadoSolicitud estado) {
        return db.listSolicitudesByEstado(estado).size();
    }
}
