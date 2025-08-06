package MODELO;

public class AporteMetaCalendarioDTO {
    
    private int id;
    private String nombre;
    private String color;
    private String fecha_creacion;
  
    public AporteMetaCalendarioDTO() {}

    public AporteMetaCalendarioDTO(int id, String nombre, String color, String fecha_creacion) {
        this.id = id;
        this.nombre = nombre;
        this.color = color;
        this.fecha_creacion = fecha_creacion;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }
}
