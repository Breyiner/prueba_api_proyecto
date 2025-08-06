package MODELO;

public class UsuarioDTO {
    
    private int id;           // Identificador único del usuario (entero)
    private String nombre;    // Nombre del usuario
    private String apellido;  // Apellido del usuario
    private int rol_id;       // Identificador del rol asignado al usuario

    // Constructor vacío necesario para frameworks y serialización
    public UsuarioDTO() {
    }
    
    // Constructor completo para inicializar todas las propiedades
    public UsuarioDTO(int id, String nombre, String apellido, int rol_id) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol_id = rol_id;
    }

    // Obtiene el id del usuario
    public int getId() {
        return id;
    }

    // Establece el id del usuario
    public void setId(int id) {
        this.id = id;
    }

    // Obtiene el id del rol del usuario
    public int getRol_id() {
        return rol_id;
    }

    // Establece el id del rol del usuario
    public void setRol_id(int rol_id) {
        this.rol_id = rol_id;
    }

    // Obtiene el nombre del usuario
    public String getNombre() {
        return nombre;
    }

    // Establece el nombre del usuario
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Obtiene el apellido del usuario
    public String getApellido() {
        return apellido;
    }

    // Establece el apellido del usuario
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
