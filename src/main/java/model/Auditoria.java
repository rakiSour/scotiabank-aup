package model;

import java.time.LocalDateTime;

public class Auditoria {
    private int id;
    private Usuario usuario;
    private String modulo;
    private String accion;
    private String descripcion;
    private LocalDateTime fechaHora;

    public Auditoria(int id, Usuario usuario, String modulo, String accion, String descripcion) {
        this.id = id;
        this.usuario = usuario;
        this.modulo = modulo;
        this.accion = accion;
        this.descripcion = descripcion;
        this.fechaHora = LocalDateTime.now();
    }

    public int getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public String getModulo() { return modulo; }
    public String getAccion() { return accion; }
    public String getDescripcion() { return descripcion; }
    public LocalDateTime getFechaHora() { return fechaHora; }
}
