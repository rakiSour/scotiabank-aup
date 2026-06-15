package model;

public class Usuario {
    private int id;
    private String nombres;
    private String apellidos;
    private String correo;
    private String password;
    private Rol rol;
    private boolean activo;

    public Usuario(int id, String nombres, String apellidos, String correo, String password, Rol rol, boolean activo) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
        this.activo = activo;
    }

    public int getId() { return id; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getCorreo() { return correo; }
    public String getPassword() { return password; }
    public Rol getRol() { return rol; }
    public boolean isActivo() { return activo; }

    public void setNombres(String nombres) { this.nombres = nombres; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setPassword(String password) { this.password = password; }
    public void setRol(Rol rol) { this.rol = rol; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}
