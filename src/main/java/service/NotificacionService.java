package service;

import model.Cliente;
import model.Notificacion;
import model.SolicitudCrediticia;
import repository.H2Database;

import java.util.List;

public class NotificacionService {
    private final H2Database db = H2Database.getInstance();

    public Notificacion notificarResultado(Cliente cliente, SolicitudCrediticia solicitud, String mensaje) {
        return db.insertNotificacion(cliente, solicitud, "Correo", "Resultado de solicitud crediticia", mensaje);
    }

    public List<Notificacion> listar() {
        return db.listNotificaciones();
    }
}
