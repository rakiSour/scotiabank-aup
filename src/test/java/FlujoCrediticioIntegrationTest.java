import model.*;
import org.junit.Test;
import service.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class FlujoCrediticioIntegrationTest {

    private final AuthService authService = new AuthService();
    private final ClienteService clienteService = new ClienteService();
    private final ProductoService productoService = new ProductoService();
    private final SolicitudService solicitudService = new SolicitudService();
    private final EvaluacionService evaluacionService = new EvaluacionService();
    private final OperacionService operacionService = new OperacionService();
    private final ReporteService reporteService = new ReporteService();

    @Test
    public void flujoCompletoDeSolicitudEvaluacionOperacionYReporteDebeFuncionar() {
        Usuario asesor = authService.login("asesor@scotia.com", "123456").get();
        Usuario analista = authService.login("analista@scotia.com", "123456").get();

        Cliente cliente = clienteService.registrar(
                asesor,
                generarDni(),
                "Cliente",
                "Integracion",
                "integracion" + System.nanoTime() + "@mail.com",
                "988777666",
                "Av. Integracion 123",
                "Administrador",
                new BigDecimal("5000.00")
        );

        ProductoFinanciero producto = productoService.listarActivos().get(0);

        SolicitudCrediticia solicitud = solicitudService.registrar(
                asesor,
                cliente,
                producto,
                new BigDecimal("12000.00"),
                24,
                "Capital de trabajo"
        );

        assertNotNull(solicitud);
        assertEquals(EstadoSolicitud.PENDIENTE, solicitud.getEstado());

        EvaluacionCrediticia evaluacion = evaluacionService.evaluar(
                analista,
                solicitud,
                720,
                new BigDecimal("5000.00"),
                EstadoSolicitud.APROBADA,
                "Cliente cumple con los criterios crediticios."
        );

        assertNotNull(evaluacion);
        assertEquals(EstadoSolicitud.APROBADA, solicitud.getEstado());

        OperacionBancaria operacion = operacionService.registrar(
                asesor,
                cliente,
                solicitud,
                "Desembolso",
                new BigDecimal("12000.00"),
                "Desembolso de préstamo aprobado"
        );

        assertNotNull(operacion);
        assertTrue(operacionService.listarComprobantes().size() > 0);

        Map<String, String> indicadores = reporteService.indicadores();

        assertTrue(indicadores.containsKey("Clientes registrados"));
        assertTrue(indicadores.containsKey("Solicitudes totales"));
        assertTrue(indicadores.containsKey("Operaciones registradas"));
    }

    private String generarDni() {
        return String.valueOf(81000000 + (int) (System.currentTimeMillis() % 1000000));
    }
}