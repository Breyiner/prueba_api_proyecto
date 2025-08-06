package MODELO;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;

public class AportesMeta {
    private int id;                    // Identificador único del aporte
    private int meta_id;               // ID de la meta a la que pertenece el aporte
    private BigDecimal monto;          // Valor del aporte
    private String descripcion;        // Descripción opcional del aporte
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private String fecha_creacion;    // Fecha de creación con formato y zona horaria

    // Constructor vacío requerido por frameworks o serialización
    public AportesMeta() {}

    // Constructor sin fecha de creación
    public AportesMeta(int id, int meta_id, BigDecimal monto, String descripcion) {
        this.id = id;
        this.meta_id = meta_id;
        this.monto = monto;
        this.descripcion = descripcion;
    }

    // Constructor completo con fecha de creación
    public AportesMeta(int id, int meta_id, BigDecimal monto, String descripcion, String fecha_creacion) {
        this.id = id;
        this.meta_id = meta_id;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha_creacion = fecha_creacion;
    }

    // Getters y setters para manipular atributos
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMeta_id() {
        return meta_id;
    }

    public void setMeta_id(int meta_id) {
        this.meta_id = meta_id;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }
}
