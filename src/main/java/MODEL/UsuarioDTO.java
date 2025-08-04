package MODEL;

public class UsuarioDTO {
    
    private int id; // Identificador tipo entero único del usuario
    private String nombre; // Nombre del usuario
    private String apellido; // Apellido del usuario
    private int rol_id;

    public UsuarioDTO() {
    }
    
    public UsuarioDTO(int id, String nombre, String apellido, int rol_id) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol_id = rol_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRol_id() {
        return rol_id;
    }

    public void setRol_id(int rol_id) {
        this.rol_id = rol_id;
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
