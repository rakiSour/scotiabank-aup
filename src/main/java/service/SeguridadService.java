package service;

import model.Rol;
import model.Usuario;
import repository.H2Database;

import java.util.List;

public class SeguridadService {
    private final H2Database db = H2Database.getInstance();
    private final AuditoriaService auditoriaService = new AuditoriaService();

    public List<Usuario> listarUsuarios() {
        return db.listUsuarios();
    }

    public Usuario crearUsuario(Usuario admin, String nombres, String apellidos, String correo, String password, Rol rol) {
        if (nombres.trim().isEmpty() || apellidos.trim().isEmpty() || correo.trim().isEmpty() || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Complete todos los campos del usuario.");
        }
        boolean existe = db.listUsuarios().stream().anyMatch(u -> u.getCorreo().equalsIgnoreCase(correo.trim()));
        if (existe) throw new IllegalArgumentException("Ya existe un usuario con ese correo.");
        Usuario usuario = db.insertUsuario(nombres.trim(), apellidos.trim(), correo.trim(), password, rol, true);
        auditoriaService.registrar(admin, "Seguridad", "Crear usuario", "Se creó el usuario " + usuario.getCorreo());
        return usuario;
    }

    public void cambiarRol(Usuario admin, Usuario usuario, Rol rol) {
        if (usuario == null) throw new IllegalArgumentException("Seleccione un usuario.");
        usuario.setRol(rol);
        db.updateUsuarioRol(usuario.getId(), rol);
        auditoriaService.registrar(admin, "Seguridad", "Cambiar rol", "Se asignó el rol " + rol + " a " + usuario.getCorreo());
    }

    public void cambiarEstado(Usuario admin, Usuario usuario) {
        if (usuario == null) throw new IllegalArgumentException("Seleccione un usuario.");
        usuario.setActivo(!usuario.isActivo());
        db.updateUsuarioActivo(usuario.getId(), usuario.isActivo());
        auditoriaService.registrar(admin, "Seguridad", "Cambiar estado", "Estado de " + usuario.getCorreo() + ": " + usuario.isActivo());
    }
}
