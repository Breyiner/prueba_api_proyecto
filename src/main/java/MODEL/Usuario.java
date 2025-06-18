package MODEL; // Paquete que contiene las clases de modelo de la aplicación

/**
 * Clase que representa un usuario en el sistema.
 * Contiene atributos que describen las propiedades de un usuario
 * y métodos para acceder y modificar esos atributos.
 */
public class Usuario {

    private int id; // Identificador tipo entero único del usuario
    private String nombre; // Nombre del usuario
    private String apellido; // Apellido del usuario
    private String correo; // Correo electrónico del usuario
    private String contrasena; // Contraseña del usuario
    private int genero_id; // ID que representa el género del usuario
    private int ciudad_id; // ID que representa la ciudad del usuario
    private int estado_id; // ID que representa el estado del usuario

    // Constructor por defecto
    public Usuario() {
    }

    /**
     * Constructor que inicializa un objeto Usuario con los valores proporcionados.
     * 
     * @param id Identificador único del usuario
     * @param nombre Nombre del usuario
     * @param apellido Apellido del usuario
     * @param correo Correo electrónico del usuario
     * @param contrasena Contraseña del usuario
     * @param genero_id ID del género del usuario
     * @param ciudad_id ID de la ciudad del usuario
     * @param estado_id ID del estado del usuario
     */
    public Usuario(int id, String nombre, String apellido, String correo, String contrasena, int genero_id, int ciudad_id, int estado_id) {
        this.id = id; // Asigna el ID del usuario
        this.nombre = nombre; // Asigna el nombre del usuario
        this.apellido = apellido; // Asigna el apellido del usuario
        this.correo = correo; // Asigna el correo electrónico del usuario
        this.contrasena = contrasena; // Asigna la contraseña del usuario
        this.genero_id = genero_id; // Asigna el ID del género del usuario
        this.ciudad_id = ciudad_id; // Asigna el ID de la ciudad del usuario
        this.estado_id = estado_id; // Asigna el ID del estado del usuario
    }

    // Métodos getter y setter para acceder y modificar los atributos

    public int getId() {
        return id; // Devuelve el ID del usuario
    }

    public void setId(int id) {
        this.id = id; // Establece el ID del usuario
    }

    public String getNombre() {
        return nombre; // Devuelve el nombre del usuario
    }

    public void setNombre(String nombre) {
        this.nombre = nombre; // Establece el nombre del usuario
    }

    public String getApellido() {
        return apellido; // Devuelve el apellido del usuario
    }

    public void setApellido(String apellido) {
        this.apellido = apellido; // Establece el apellido del usuario
    }

    public String getCorreo() {
        return correo; // Devuelve el correo electrónico del usuario
    }

    public void setCorreo(String correo) {
        this.correo = correo; // Establece el correo electrónico del usuario
    }

    public String getContrasena() {
        return contrasena; // Devuelve la contraseña del usuario
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena; // Establece la contraseña del usuario
    }

    public int getCiudad_id() {
        return ciudad_id; // Devuelve el ID de la ciudad del usuario
    }

    public void setCiudad_id(int ciudad_id) {
        this.ciudad_id = ciudad_id; // Establece el ID de la ciudad del usuario
    }

    public int getEstado_id() {
        return estado_id; // Devuelve el ID del estado del usuario
    }

    public void setEstado_id(int estado_id) {
        this.estado_id = estado_id; // Establece el ID del estado del usuario
    }

    public int getGenero_id() {
        return genero_id; // Devuelve el ID del género del usuario
    }

    public void setGenero_id(int genero_id) {
        this.genero_id = genero_id; // Establece el ID del género del usuario
    }
}