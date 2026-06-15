package service;

import model.Cliente;
import model.Usuario;
import repository.H2Database;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ClienteService {
    private final H2Database db = H2Database.getInstance();
    private final AuditoriaService auditoriaService = new AuditoriaService();

    public Cliente registrar(Usuario usuario, String dni, String nombres, String apellidos, String correo, String telefono,
                             String direccion, String ocupacion, BigDecimal ingresos) {
        validarCliente(dni, nombres, apellidos, ingresos);
        if (buscarPorDni(dni).isPresent()) {
            throw new IllegalArgumentException("Ya existe un cliente registrado con el DNI indicado.");
        }
        Cliente cliente = db.insertCliente(dni.trim(), nombres.trim(), apellidos.trim(), texto(correo),
                texto(telefono), texto(direccion), texto(ocupacion), ingresos, "ACTIVO");
        auditoriaService.registrar(usuario, "Gestión de Clientes", "Registrar cliente", "Se registró al cliente " + cliente.getNombreCompleto());
        return cliente;
    }

    public void actualizar(Usuario usuario, Cliente cliente, String nombres, String apellidos, String correo, String telefono,
                           String direccion, String ocupacion, BigDecimal ingresos, String estado) {
        if (cliente == null) throw new IllegalArgumentException("Seleccione un cliente para actualizar.");
        if (nombres.trim().isEmpty() || apellidos.trim().isEmpty()) throw new IllegalArgumentException("Nombres y apellidos son obligatorios.");
        cliente.setNombres(nombres.trim());
        cliente.setApellidos(apellidos.trim());
        cliente.setCorreo(texto(correo));
        cliente.setTelefono(texto(telefono));
        cliente.setDireccion(texto(direccion));
        cliente.setOcupacion(texto(ocupacion));
        cliente.setIngresosMensuales(ingresos);
        cliente.setEstado(estado);
        db.updateCliente(cliente);
        auditoriaService.registrar(usuario, "Gestión de Clientes", "Actualizar cliente", "Se actualizó al cliente " + cliente.getNombreCompleto());
    }

    public Optional<Cliente> buscarPorDni(String dni) {
        return db.findClienteByDni(dni);
    }

    public List<Cliente> buscar(String texto) {
        return db.searchClientes(texto);
    }

    public List<Cliente> listar() {
        return db.listClientes();
    }

    private String texto(String value) {
        return value == null ? "" : value.trim();
    }

    private void validarCliente(String dni, String nombres, String apellidos, BigDecimal ingresos) {
        if (dni == null || dni.trim().isEmpty()) throw new IllegalArgumentException("El DNI es obligatorio.");
        if (nombres == null || nombres.trim().isEmpty()) throw new IllegalArgumentException("Los nombres son obligatorios.");
        if (apellidos == null || apellidos.trim().isEmpty()) throw new IllegalArgumentException("Los apellidos son obligatorios.");
        if (ingresos == null || ingresos.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Los ingresos deben ser mayores o iguales a cero.");
    }
}
