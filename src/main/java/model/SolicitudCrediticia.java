package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SolicitudCrediticia {
    private int id;
    private String codigo;
    private Cliente cliente;
    private ProductoFinanciero producto;
    private Usuario usuarioRegistro;
    private BigDecimal montoSolicitado;
    private int plazoMeses;
    private String finalidad;
    private EstadoSolicitud estado;
    private LocalDateTime fechaRegistro;

    public SolicitudCrediticia(int id, String codigo, Cliente cliente, ProductoFinanciero producto, Usuario usuarioRegistro,
                               BigDecimal montoSolicitado, int plazoMeses, String finalidad) {
        this.id = id;
        this.codigo = codigo;
        this.cliente = cliente;
        this.producto = producto;
        this.usuarioRegistro = usuarioRegistro;
        this.montoSolicitado = montoSolicitado;
        this.plazoMeses = plazoMeses;
        this.finalidad = finalidad;
        this.estado = EstadoSolicitud.PENDIENTE;
        this.fechaRegistro = LocalDateTime.now();
    }

    public int getId() { return id; }
    public String getCodigo() { return codigo; }
    public Cliente getCliente() { return cliente; }
    public ProductoFinanciero getProducto() { return producto; }
    public Usuario getUsuarioRegistro() { return usuarioRegistro; }
    public BigDecimal getMontoSolicitado() { return montoSolicitado; }
    public int getPlazoMeses() { return plazoMeses; }
    public String getFinalidad() { return finalidad; }
    public EstadoSolicitud getEstado() { return estado; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }

    public void setEstado(EstadoSolicitud estado) { this.estado = estado; }
    public void setMontoSolicitado(BigDecimal montoSolicitado) { this.montoSolicitado = montoSolicitado; }
    public void setPlazoMeses(int plazoMeses) { this.plazoMeses = plazoMeses; }
    public void setFinalidad(String finalidad) { this.finalidad = finalidad; }

    @Override
    public String toString() {
        return codigo + " - " + cliente.getNombreCompleto() + " - " + producto.getNombre();
    }
}
