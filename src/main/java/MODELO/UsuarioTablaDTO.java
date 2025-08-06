package MODELO; // Paquete que contiene las clases de modelo de la aplicación

public class UsuarioTablaDTO {

    private int id; // Identificador tipo entero único del usuario
    private String rol; 
    private String nombre; // Nombre del usuario
    private String apellido; // Apellido del usuario
    private String correo; // Correo electrónico del usuario
    private String genero; 
    private String ciudad; 
    private String estado; 

    // Constructor por defecto
    public UsuarioTablaDTO() {}

    public UsuarioTablaDTO(int id, String rol, String nombre, String apellido, String correo, String genero, String ciudad, String estado) {
        this.id = id;
        this.rol = rol;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.genero = genero;
        this.ciudad = ciudad;
        this.estado = estado;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
}