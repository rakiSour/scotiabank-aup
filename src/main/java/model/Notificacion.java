package model;

import java.time.LocalDateTime;

public class Notificacion {
    private int id;
    private Cliente cliente;
    private SolicitudCrediticia solicitud;
    private String canal;
    private String asunto;
    private String mensaje;
    private String estadoEnvio;
    private LocalDateTime fechaEnvio;

    public Notificacion(int id, Cliente cliente, SolicitudCrediticia solicitud, String canal, String asunto, String mensaje) {
        this.id = id;
        this.cliente = cliente;
        this.solicitud = solicitud;
        this.canal = canal;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.estadoEnvio = "ENVIADA";
        this.fechaEnvio = LocalDateTime.now();
    }

    public int getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public SolicitudCrediticia getSolicitud() { return solicitud; }
    public String getCanal() { return canal; }
    public String getAsunto() { return asunto; }
    public String getMensaje() { return mensaje; }
    public String getEstadoEnvio() { return estadoEnvio; }
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
}
