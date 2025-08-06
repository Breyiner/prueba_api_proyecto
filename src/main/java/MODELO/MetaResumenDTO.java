package MODELO;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;

public class MetaResumenDTO {
    
    // Campos privados para encapsulación de datos de resumen de una meta
    private int id;                       // ID único de la meta
    private String nombre;                // Nombre descriptivo de la meta
    private BigDecimal monto;             // Monto objetivo definido para la meta

    // Fecha límite para alcanzar la meta, con formato JSON estandarizado yyyy-MM-dd y zona UTC
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private String fecha_limite;

    // Fecha de creación de la meta, formateada igual que fecha_limite
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private String fecha_creacion;

    private BigDecimal total;             // Total acumulado o aportado hasta el momento
    private boolean completada;           // Estado de la meta: si está completada o no
    private String mensaje;               // Mensaje opcional (ej: notificación o estado extra)

    // Constructor vacío para frameworks y serialización
    public MetaResumenDTO() {}

    // Constructor completo para crear instancia con todos los datos excepto mensaje
    public MetaResumenDTO(int id, String nombre, BigDecimal monto, String fecha_limite, String fecha_creacion, BigDecimal total, boolean completada) {
        this.id = id;
        this.nombre = nombre;
        this.monto = monto;
        this.fecha_limite = fecha_limite;
        this.fecha_creacion = fecha_creacion;
        this.total = total;
        this.completada = completada;
    }

    // Getters y setters: acceso y modificación segura de cada propiedad

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
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

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getFecha_limite() {
        return fecha_limite;
    }

    public void setFecha_limite(String fecha_limite) {
        this.fecha_limite = fecha_limite;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
