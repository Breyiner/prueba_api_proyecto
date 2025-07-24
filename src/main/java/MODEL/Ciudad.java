package MODEL;

public class Ciudad {
    
    private int id; // Identificador tipo entero único de la ciudad
    private String nombre; // Nombre de la ciudad

    // Constructor por defecto
    public Ciudad() {
    }

    public Ciudad(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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
}
