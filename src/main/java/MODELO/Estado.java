package MODELO;  // Paquete modelo

public class Estado {

    private int id;         // ID único del estado
    private String nombre;  // Nombre del estado

    // Constructor vacío para facilitar la creación sin parámetros
    public Estado() {
    }

    // Constructor con parámetros para inicializar ID y nombre
    public Estado(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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
}
