package MODEL;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.sql.Date;

public class MetaDetalleDTO {
  
    public int id;
    public String nombre;
    public String descripcion;
    public BigDecimal monto;
    public BigDecimal total;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    public String fecha_creacion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    public String fecha_limite;
    public int cantidad_aportes;
    public String estado;

    public MetaDetalleDTO() {}

    public MetaDetalleDTO(int id, String nombre, String descripcion, BigDecimal monto, BigDecimal total, String fecha_creacion, String fecha_limite, int cantidad_aportes) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.monto = monto;
        this.total = total;
        this.fecha_creacion = fecha_creacion;
        this.fecha_limite = fecha_limite;
        this.cantidad_aportes = cantidad_aportes;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_reacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getFecha_limite() {
        return fecha_limite;
    }

    public void setFecha_limite(String fecha_limite) {
        this.fecha_limite = fecha_limite;
    }

    public int getCantidad_aportes() {
        return cantidad_aportes;
    }

    public void setCantidad_aportes(int cantidad_aportes) {
        this.cantidad_aportes = cantidad_aportes;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
  
    
}
