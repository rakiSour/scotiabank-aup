package service;

import model.Usuario;
import repository.H2Database;

import java.util.Optional;

public class AuthService {
    private final H2Database db = H2Database.getInstance();
    private final AuditoriaService auditoriaService = new AuditoriaService();

    public Optional<Usuario> login(String correo, String password) {
        Optional<Usuario> usuario = db.findUsuarioByCredentials(correo, password);
        usuario.ifPresent(u -> auditoriaService.registrar(u, "Seguridad", "Inicio de sesión", "Acceso correcto a la plataforma"));
        return usuario;
    }
}
