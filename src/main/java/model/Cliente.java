package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Cliente {
    private int id;
    private String dni;
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private String direccion;
    private String ocupacion;
    private BigDecimal ingresosMensuales;
    private String estado;
    private LocalDateTime fechaRegistro;

    public Cliente(int id, String dni, String nombres, String apellidos, String correo, String telefono,
                   String direccion, String ocupacion, BigDecimal ingresosMensuales, String estado) {
        this.id = id;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.ocupacion = ocupacion;
        this.ingresosMensuales = ingresosMensuales;
        this.estado = estado;
        this.fechaRegistro = LocalDateTime.now();
    }

    public int getId() { return id; }
    public String getDni() { return dni; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getCorreo() { return correo; }
    public String getTelefono() { return telefono; }
    public String getDireccion() { return direccion; }
    public String getOcupacion() { return ocupacion; }
    public BigDecimal getIngresosMensuales() { return ingresosMensuales; }
    public String getEstado() { return estado; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }

    public void setDni(String dni) { this.dni = dni; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }
    public void setIngresosMensuales(BigDecimal ingresosMensuales) { this.ingresosMensuales = ingresosMensuales; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    @Override
    public String toString() {
        return dni + " - " + getNombreCompleto();
    }
}
