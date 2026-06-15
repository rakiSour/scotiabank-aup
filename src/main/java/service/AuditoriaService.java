package service;

import model.Auditoria;
import model.Usuario;
import repository.H2Database;

import java.util.List;

public class AuditoriaService {
    private final H2Database db = H2Database.getInstance();

    public void registrar(Usuario usuario, String modulo, String accion, String descripcion) {
        db.insertAuditoria(usuario, modulo, accion, descripcion);
    }

    public List<Auditoria> listar() {
        return db.listAuditorias();
    }
}
