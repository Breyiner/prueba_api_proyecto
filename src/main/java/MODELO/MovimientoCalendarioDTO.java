package MODELO;

public class MovimientoCalendarioDTO {
    
    // ID único que identifica el movimiento
    private int id;

    // Nombre descriptivo del movimiento
    private String nombre;

    // Color asociado al movimiento (puede ser para UI o categoría)
    private String color;

    // Fecha en la que se creó el movimiento, guardada como String (idealmente yyyy-MM-dd)
    private String fecha_creacion;
  
    // Constructor vacío necesario para frameworks y serialización
    public MovimientoCalendarioDTO() {}

    // Constructor completo para inicializar la instancia con todos los campos
    public MovimientoCalendarioDTO(int id, String nombre, String color, String fecha_creacion) {
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

    // Getter para obtener el ID
    public int getId() {
        return id;
    }

    // Setter para modificar el ID
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
