package service;

import model.*;
import repository.H2Database;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class EvaluacionService {
    private final H2Database db = H2Database.getInstance();
    private final AuditoriaService auditoriaService = new AuditoriaService();
    private final NotificacionService notificacionService = new NotificacionService();

    public EvaluacionCrediticia evaluar(Usuario analista, SolicitudCrediticia solicitud, int puntaje, BigDecimal ingresoValidado,
                                        EstadoSolicitud resultado, String comentario) {
        if (solicitud == null) throw new IllegalArgumentException("Debe seleccionar una solicitud.");
        if (puntaje < 0 || puntaje > 999) throw new IllegalArgumentException("El puntaje debe estar entre 0 y 999.");
        if (ingresoValidado == null || ingresoValidado.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("El ingreso validado no puede ser negativo.");
        if (resultado == EstadoSolicitud.PENDIENTE) throw new IllegalArgumentException("Debe elegir APROBADA, RECHAZADA u OBSERVADA.");

        NivelRiesgo riesgo = calcularRiesgo(puntaje, ingresoValidado, solicitud.getMontoSolicitado());
        solicitud.setEstado(resultado);
        db.updateSolicitudEstado(solicitud.getId(), resultado);
        EvaluacionCrediticia evaluacion = db.insertEvaluacion(solicitud, analista, puntaje, riesgo, ingresoValidado, resultado, comentario);

        String mensaje = "Su solicitud " + solicitud.getCodigo() + " fue " + resultado + ". Comentario: " + comentario;
        notificacionService.notificarResultado(solicitud.getCliente(), solicitud, mensaje);
        auditoriaService.registrar(analista, "Evaluación Crediticia", "Evaluar solicitud", mensaje);
        return evaluacion;
    }

    public List<SolicitudCrediticia> listarPendientesYObservadas() {
        return db.listSolicitudes().stream()
                .filter(s -> s.getEstado() == EstadoSolicitud.PENDIENTE || s.getEstado() == EstadoSolicitud.OBSERVADA)
                .collect(Collectors.toList());
    }

    public List<EvaluacionCrediticia> listarEvaluaciones() {
        return db.listEvaluaciones();
    }

    private NivelRiesgo calcularRiesgo(int puntaje, BigDecimal ingreso, BigDecimal monto) {
        BigDecimal relacion = ingreso.compareTo(BigDecimal.ZERO) == 0 ? new BigDecimal("999") : monto.divide(ingreso, 2, java.math.RoundingMode.HALF_UP);
        if (puntaje >= 700 && relacion.compareTo(new BigDecimal("8")) <= 0) return NivelRiesgo.BAJO;
        if (puntaje >= 550 && relacion.compareTo(new BigDecimal("15")) <= 0) return NivelRiesgo.MEDIO;
        return NivelRiesgo.ALTO;
    }
}
