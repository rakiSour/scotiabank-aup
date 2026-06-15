package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EvaluacionCrediticia {
    private int id;
    private SolicitudCrediticia solicitud;
    private Usuario analista;
    private int puntajeCrediticio;
    private NivelRiesgo nivelRiesgo;
    private BigDecimal ingresoValidado;
    private EstadoSolicitud resultado;
    private String comentario;
    private LocalDateTime fechaEvaluacion;

    public EvaluacionCrediticia(int id, SolicitudCrediticia solicitud, Usuario analista, int puntajeCrediticio,
                                NivelRiesgo nivelRiesgo, BigDecimal ingresoValidado, EstadoSolicitud resultado, String comentario) {
        this.id = id;
        this.solicitud = solicitud;
        this.analista = analista;
        this.puntajeCrediticio = puntajeCrediticio;
        this.nivelRiesgo = nivelRiesgo;
        this.ingresoValidado = ingresoValidado;
        this.resultado = resultado;
        this.comentario = comentario;
        this.fechaEvaluacion = LocalDateTime.now();
    }

    public int getId() { return id; }
    public SolicitudCrediticia getSolicitud() { return solicitud; }
    public Usuario getAnalista() { return analista; }
    public int getPuntajeCrediticio() { return puntajeCrediticio; }
    public NivelRiesgo getNivelRiesgo() { return nivelRiesgo; }
    public BigDecimal getIngresoValidado() { return ingresoValidado; }
    public EstadoSolicitud getResultado() { return resultado; }
    public String getComentario() { return comentario; }
    public LocalDateTime getFechaEvaluacion() { return fechaEvaluacion; }
}
