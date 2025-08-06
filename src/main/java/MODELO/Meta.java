package MODELO;  // Define el paquete donde está la clase Meta

import com.fasterxml.jackson.annotation.JsonFormat;  // Anotación para formatear fechas en JSON
import java.math.BigDecimal;  // Para manejar números decimales con precisión (montos)
import java.sql.Date;  // Importado pero no usado directamente (puede eliminarse si no es necesario)

public class Meta {
    
    private int id;  // Identificador único de la meta
    private int usuario_id;  // Identificador del usuario propietario de la meta
    private String nombre;  // Nombre o título de la meta
    private BigDecimal monto;  // Monto objetivo a alcanzar
    private String descripcion;  // Descripción adicional o detalles de la meta
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")  // Formato fecha para JSON
    private String fecha_limite;  // Fecha límite para cumplir la meta (en formato yyyy-MM-dd)
    
    private boolean completada;  // Indica si la meta fue completada (true/false)
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")  // Formato fecha para JSON
    private String fecha_creacion;  // Fecha de creación de la meta (en formato yyyy-MM-dd)

    public Meta() {}  // Constructor vacío necesario para frameworks y serialización JSON

    // Constructor con los campos esenciales para crear una meta (sin completada ni fecha_creacion)
    public Meta(int id, int usuario_id, String nombre, BigDecimal monto, String descripcion, String fecha_limite) {
        this.id = id;
        this.usuario_id = usuario_id;
        this.nombre = nombre;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha_limite = fecha_limite;
    }

    // Getters y setters para encapsular los campos y permitir acceso controlado
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha_limite() {
        return fecha_limite;
    }

    public void setFecha_limite(String fecha_limite) {
        this.fecha_limite = fecha_limite;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }
}
