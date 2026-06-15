import model.Cliente;
import model.Usuario;
import org.junit.Test;
import service.AuthService;
import service.ClienteService;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ClienteServiceTest {

    private final AuthService authService = new AuthService();
    private final ClienteService clienteService = new ClienteService();

    @Test
    public void registrarClienteConDatosValidosDebeGuardarCliente() {
        Usuario asesor = authService.login("asesor@scotia.com", "123456").get();
        String dni = generarDni();

        Cliente cliente = clienteService.registrar(
                asesor,
                dni,
                "Juan",
                "Prueba",
                "juan.prueba" + System.nanoTime() + "@mail.com",
                "999888777",
                "Av. Lima 123",
                "Empleado",
                new BigDecimal("3500.00")
        );

        assertNotNull(cliente);
        assertEquals(dni, cliente.getDni());
        assertEquals("Juan Prueba", cliente.getNombreCompleto());
    }

    @Test(expected = IllegalArgumentException.class)
    public void registrarClienteConDniDuplicadoDebeLanzarError() {
        Usuario asesor = authService.login("asesor@scotia.com", "123456").get();
        String dni = generarDni();

        clienteService.registrar(
                asesor,
                dni,
                "Ana",
                "Duplicada",
                "ana" + System.nanoTime() + "@mail.com",
                "999111222",
                "Av. Principal",
                "Contadora",
                new BigDecimal("4000.00")
        );

        clienteService.registrar(
                asesor,
                dni,
                "Ana",
                "Duplicada",
                "ana2" + System.nanoTime() + "@mail.com",
                "999111333",
                "Av. Principal",
                "Contadora",
                new BigDecimal("4000.00")
        );
    }

    private String generarDni() {
        return String.valueOf(80000000 + (int) (System.currentTimeMillis() % 10000000));
    }
}