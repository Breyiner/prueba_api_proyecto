package MODELO;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;

// Clase DTO para representar aportes en formato calendario con detalles de categoría, icono, colores y monto
public class AportesCalendarioDTO {
    
    private int id;               // ID único del aporte
    private String icono;         // Icono representativo de la categoría o tipo
    private String categoria;     // Nombre de la categoría a la que pertenece el aporte
    private String color;         // Color principal para mostrar en UI (ej. texto o borde)
    private String color_bg;      // Color de fondo para mejorar visibilidad o destacar el aporte
    private String nombre;        // Nombre o descripción del aporte o meta asociada
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC") // Formato JSON de la fecha para serialización
    private String fecha_creacion; // Fecha de creación del aporte en formato String (ISO)
    private BigDecimal monto;     // Valor monetario del aporte, usando BigDecimal para precisión financiera

    // Constructor vacío para frameworks y serialización
    public AportesCalendarioDTO() {
    }
    
    // Constructor completo para inicializar todos los campos de forma directa
    public AportesCalendarioDTO(int id, String icono, String categoria, String color, String color_bg, String nombre, String fecha_creacion, BigDecimal monto) {
        this.id = id;
        this.icono = icono;
        this.categoria = categoria;
        this.color = color;
        this.color_bg = color_bg;
        this.nombre = nombre;
        this.fecha_creacion = fecha_creacion;
        this.monto = monto;
    }

    // Getter para obtener el ID
    public int getId() {
        return id;
    }

    // Setter para establecer el ID
    public void setId(int id) {
        this.id = id;
    }

    // Getter para obtener el icono
    public String getIcono() {
        return icono;
    }

    // Setter para establecer el icono
    public void setIcono(String icono) {
        this.icono = icono;
    }

    // Getter para obtener la categoría
    public String getCategoria() {
        return categoria;
    }

    // Setter para establecer la categoría
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    // Getter para obtener el color principal
    public String getColor() {
        return color;
    }

    // Setter para establecer el color principal
    public void setColor(String color) {
        this.color = color;
    }

    // Getter para obtener el color de fondo
    public String getColor_bg() {
        return color_bg;
    }

    // Setter para establecer el color de fondo
    public void setColor_bg(String color_bg) {
        this.color_bg = color_bg;
    }

    // Getter para obtener el nombre
    public String getNombre() {
        return nombre;
    }

    // Setter para establecer el nombre
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getter para obtener la fecha de creación
    public String getFecha_creacion() {
        return fecha_creacion;
    }

    // Setter para establecer la fecha de creación
    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    // Getter para obtener el monto del aporte
    public BigDecimal getMonto() {
        return monto;
    }

    // Setter para establecer el monto del aporte
    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}
