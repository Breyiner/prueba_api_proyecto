package MODEL;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.sql.Date;

public class AportesMeta {
    private int id;
    private int meta_id;
    private BigDecimal monto;
    private String descripcion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fecha_creacion;

    // Constructores
    public AportesMeta() {}

    public AportesMeta(int id, int meta_id, BigDecimal monto, String descripcion) {
        this.id = id;
        this.meta_id = meta_id;
        this.monto = monto;
        this.descripcion = descripcion;
    }

    public AportesMeta(int id, int meta_id, BigDecimal monto, String descripcion, Date fecha_creacion) {
        this.id = id;
        this.meta_id = meta_id;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha_creacion = fecha_creacion;
    }

    // Getters y Setters
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

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }
}