package service;

import model.ProductoFinanciero;
import repository.H2Database;

import java.util.List;

public class ProductoService {
    private final H2Database db = H2Database.getInstance();

    public List<ProductoFinanciero> listarActivos() {
        return db.listProductosActivos();
    }
}
