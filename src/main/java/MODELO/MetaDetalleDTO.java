package MODELO;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;

public class MetaDetalleDTO {
  
    // Campos públicos que representan detalles de una meta con datos financieros y fechas
    public int id;                        // ID único de la meta
    public String nombre;                 // Nombre de la meta
    public String descripcion;            // Descripción de la meta
    public BigDecimal monto;              // Monto objetivo o esperado para la meta
    public BigDecimal total;              // Total acumulado o aportado hasta ahora
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    public String fecha_creacion;         // Fecha de creación de la meta, formato JSON yyyy-MM-dd
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    public String fecha_limite;           // Fecha límite para alcanzar la meta, formato JSON yyyy-MM-dd
    public int cantidad_aportes;          // Número de aportes registrados para la meta
    public String estado;                 // Estado de la meta (ej. activa, completada, etc.)

    // Constructor vacío para frameworks y serialización
    public MetaDetalleDTO() {}

    // Constructor para crear objeto con todos los detalles excepto estado
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

    // Getters y setters para acceder y modificar cada campo

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

    // Nota: en el setter hay un typo en el nombre: setFecha_reacion debería ser setFecha_creacion
    public void setFecha_creacion(String fecha_creacion) {
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
