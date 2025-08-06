package MODELO;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.sql.Date;

public class MovimientoDetalleDTO {
    
    // Identificador único del movimiento
    private int id;

    // Icono representativo para la categoría o tipo del movimiento
    private String icono;

    // Nombre de la categoría a la que pertenece el movimiento
    private String categoria;

    // Color principal asociado (para UI o visualización)
    private String color;

    // Color de fondo asociado (para UI o visualización)
    private String color_bg;

    // Nombre o descripción del movimiento
    private String nombre;

    // Fecha de creación del movimiento, formateada como String yyyy-MM-dd para JSON
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private String fecha_creacion;

    // Valor monetario o cantidad asociada al movimiento
    private BigDecimal monto;
    
    // Constructor vacío necesario para frameworks y serialización
    public MovimientoDetalleDTO() {}

    // Constructor completo para inicializar el objeto con todos los atributos
    public MovimientoDetalleDTO(int id, String icono, String categoria, String color, String color_bg, String nombre, String fecha_creacion, BigDecimal monto) {
        this.id = id;
        this.icono = icono;
        this.categoria = categoria;
        this.color = color;
        this.color_bg = color_bg;
        this.nombre = nombre;
        this.fecha_creacion = fecha_creacion;
        this.monto = monto;
    }

    // Getters y setters para cada atributo, permiten acceder y modificar los valores
    
    public String getColor_bg() {
        return color_bg;
    }

    public void setColor_bg(String color_bg) {
        this.color_bg = color_bg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}
