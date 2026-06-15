package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OperacionBancaria {
    private int id;
    private Cliente cliente;
    private SolicitudCrediticia solicitud;
    private Usuario usuario;
    private String tipoOperacion;
    private BigDecimal monto;
    private String descripcion;
    private String estado;
    private LocalDateTime fechaOperacion;

    public OperacionBancaria(int id, Cliente cliente, SolicitudCrediticia solicitud, Usuario usuario,
                             String tipoOperacion, BigDecimal monto, String descripcion) {
        this.id = id;
        this.cliente = cliente;
        this.solicitud = solicitud;
        this.usuario = usuario;
        this.tipoOperacion = tipoOperacion;
        this.monto = monto;
        this.descripcion = descripcion;
        this.estado = "REGISTRADA";
        this.fechaOperacion = LocalDateTime.now();
    }

    public int getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public SolicitudCrediticia getSolicitud() { return solicitud; }
    public Usuario getUsuario() { return usuario; }
    public String getTipoOperacion() { return tipoOperacion; }
    public BigDecimal getMonto() { return monto; }
    public String getDescripcion() { return descripcion; }
    public String getEstado() { return estado; }
    public LocalDateTime getFechaOperacion() { return fechaOperacion; }
}
