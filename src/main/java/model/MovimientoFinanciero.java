package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimientoFinanciero {
    private int id;
    private OperacionBancaria operacion;
    private String tipoMovimiento;
    private BigDecimal monto;
    private BigDecimal saldoResultante;
    private LocalDateTime fechaMovimiento;

    public MovimientoFinanciero(int id, OperacionBancaria operacion, String tipoMovimiento, BigDecimal monto, BigDecimal saldoResultante) {
        this.id = id;
        this.operacion = operacion;
        this.tipoMovimiento = tipoMovimiento;
        this.monto = monto;
        this.saldoResultante = saldoResultante;
        this.fechaMovimiento = LocalDateTime.now();
    }

    public int getId() { return id; }
    public OperacionBancaria getOperacion() { return operacion; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public BigDecimal getMonto() { return monto; }
    public BigDecimal getSaldoResultante() { return saldoResultante; }
    public LocalDateTime getFechaMovimiento() { return fechaMovimiento; }
}
