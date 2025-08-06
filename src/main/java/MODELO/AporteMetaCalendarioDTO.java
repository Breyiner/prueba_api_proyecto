package MODELO;

// Clase DTO (Data Transfer Object) para representar aportes a metas en formato calendario
public class AporteMetaCalendarioDTO {
    
    private int id;                // ID único del aporte
    private String nombre;         // Nombre asociado al aporte/meta
    private String color;          // Color representativo para la visualización en calendario
    private String fecha_creacion; // Fecha de creación del aporte en formato String (puede ser ISO o personalizado)
  
    // Constructor vacío (por defecto)
    public AporteMetaCalendarioDTO() {}

    // Constructor con todos los campos para crear instancias fácilmente
    public AporteMetaCalendarioDTO(int id, String nombre, String color, String fecha_creacion) {
        this.id = id;
        this.nombre = nombre;
        this.color = color;
        this.fecha_creacion = fecha_creacion;
    }

    // Getter para obtener el color
    public String getColor() {
        return color;
    }

    // Setter para modificar el color
    public void setColor(String color) {
        this.color = color;
    }

    // Getter para obtener el id
    public int getId() {
        return id;
    }

    // Setter para modificar el id
    public void setId(int id) {
        this.id = id;
    }

    // Getter para obtener el nombre
    public String getNombre() {
        return nombre;
    }

    // Setter para modificar el nombre
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getter para obtener la fecha de creación
    public String getFecha_creacion() {
        return fecha_creacion;
    }

    // Setter para modificar la fecha de creación
    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }
}
