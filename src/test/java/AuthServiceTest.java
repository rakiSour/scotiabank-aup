import model.Usuario;
import org.junit.Test;
import service.AuthService;

import java.util.Optional;

import static org.junit.Assert.*;

public class AuthServiceTest {

    private final AuthService authService = new AuthService();

    @Test
    public void loginConCredencialesValidasDebeRetornarUsuario() {
        Optional<Usuario> usuario = authService.login("admin@scotia.com", "123456");

        assertTrue(usuario.isPresent());
        assertEquals("admin@scotia.com", usuario.get().getCorreo());
    }

    @Test
    public void loginConCredencialesInvalidasDebeRetornarVacio() {
        Optional<Usuario> usuario = authService.login("admin@scotia.com", "claveIncorrecta");

        assertFalse(usuario.isPresent());
    }
}