package model;

import java.time.LocalDateTime;

public class ComprobanteElectronico {
    private int id;
    private OperacionBancaria operacion;
    private String serie;
    private String numero;
    private String tipoComprobante;
    private LocalDateTime fechaEmision;

    public ComprobanteElectronico(int id, OperacionBancaria operacion, String serie, String numero, String tipoComprobante) {
        this.id = id;
        this.operacion = operacion;
        this.serie = serie;
        this.numero = numero;
        this.tipoComprobante = tipoComprobante;
        this.fechaEmision = LocalDateTime.now();
    }

    public int getId() { return id; }
    public OperacionBancaria getOperacion() { return operacion; }
    public String getSerie() { return serie; }
    public String getNumero() { return numero; }
    public String getTipoComprobante() { return tipoComprobante; }
    public LocalDateTime getFechaEmision() { return fechaEmision; }
}
