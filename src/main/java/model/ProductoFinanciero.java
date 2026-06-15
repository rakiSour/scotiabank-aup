package model;

import java.math.BigDecimal;

public class ProductoFinanciero {
    private int id;
    private String nombre;
    private TipoProducto tipo;
    private String descripcion;
    private BigDecimal tasaBase;
    private boolean activo;

    public ProductoFinanciero(int id, String nombre, TipoProducto tipo, String descripcion, BigDecimal tasaBase, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.tasaBase = tasaBase;
        this.activo = activo;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public TipoProducto getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
    public BigDecimal getTasaBase() { return tasaBase; }
    public boolean isActivo() { return activo; }

    @Override
    public String toString() {
        return nombre + " (" + tipo + ")";
    }
}
