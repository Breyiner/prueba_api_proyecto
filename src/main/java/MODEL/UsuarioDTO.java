package MODEL;

public class UsuarioDTO {
    
    private int id; // Identificador tipo entero único del usuario
    private String nombre; // Nombre del usuario
    private String apellido; // Apellido del usuario

    public UsuarioDTO() {
    }
    
    public UsuarioDTO(int id, String nombre, String apellido) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
