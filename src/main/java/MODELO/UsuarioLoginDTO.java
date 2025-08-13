package MODELO; // Define que esta clase pertenece al paquete MODELO, que contiene entidades y DTOs del sistema.

public class UsuarioLoginDTO { // Clase DTO para encapsular los datos de autenticación de un usuario (correo y contraseña).

    private String correo; // Campo que almacena el correo electrónico del usuario.
    private String contrasena; // Campo que almacena la contraseña del usuario.

    /**
     * Constructor por defecto para la serialización/deserialización (por ejemplo, JSON).
     * Permite crear instancias vacías de la clase.
     */
    public UsuarioLoginDTO() {}

    /**
     * Constructor parametrizado para inicializar el DTO con correo y contraseña.
     * @param correo Correo electrónico del usuario.
     * @param contrasena Contraseña del usuario.
     */
    public UsuarioLoginDTO(String correo, String contrasena) {
        this.correo = correo;
        this.contrasena = contrasena;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     * @return El correo electrónico almacenado en el DTO.
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Establece el correo electrónico del usuario.
     * @param correo El correo electrónico a asignar.
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Obtiene la contraseña del usuario.
     * @return La contraseña almacenada en el DTO.
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Establece la contraseña del usuario.
     * @param contrasena La contraseña a asignar.
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}