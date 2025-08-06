package MODELO;

public class UsuarioLoginDTO {
    
    private String correo; // Correo electrónico del usuario
    private String contrasena; // Contraseña del usuario
    
    public UsuarioLoginDTO() {}

    public UsuarioLoginDTO(String correo, String contrasena) {
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
