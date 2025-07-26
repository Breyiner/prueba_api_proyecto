package MODEL;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.sql.Date;

public class MetaDetalleDTO {
  
    public int id;
    public String nombre;
    public String descripcion;
    public BigDecimal monto;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date fecha_creacion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date fecha_limite;
    public int cantidad_aportes;

    public MetaDetalleDTO() {}

    public MetaDetalleDTO(int id, String nombre, String descripcion, BigDecimal monto, Date fecha_creacion, Date fecha_limite, int cantidad_aportes) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha_creacion = fecha_creacion;
        this.fecha_limite = fecha_limite;
        this.cantidad_aportes = cantidad_aportes;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_reacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Date getFecha_limite() {
        return fecha_limite;
    }

    public void setFecha_limite(Date fecha_limite) {
        this.fecha_limite = fecha_limite;
    }

    public int getCantidad_aportes() {
        return cantidad_aportes;
    }

    public void setCantidad_aportes(int cantidad_aportes) {
        this.cantidad_aportes = cantidad_aportes;
    }
  
    
}
