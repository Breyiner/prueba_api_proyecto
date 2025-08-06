package MODELO;

public class PasswordDTO {
    
    // Contraseña antigua para validar el cambio
    private String old_password;

    // Nueva contraseña que se quiere establecer
    private String new_password;

    // Confirmación de la nueva contraseña para evitar errores
    private String confirm_password;

    // Constructor vacío necesario para frameworks o serialización
    public PasswordDTO() {
    }

    // Constructor completo para crear el objeto con los tres valores
    public PasswordDTO(String old_password, String new_password, String confirm_password) {
        this.old_password = old_password;
        this.new_password = new_password;
        this.confirm_password = confirm_password;
    }

    // Getter para la contraseña antigua
    public String getOld_password() {
        return old_password;
    }

    // Setter para la contraseña antigua
    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    // Getter para la nueva contraseña
    public String getNew_password() {
        return new_password;
    }

    // Setter para la nueva contraseña
    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    // Getter para la confirmación de la nueva contraseña
    public String getConfirm_password() {
        return confirm_password;
    }

    // Setter para la confirmación de la nueva contraseña
    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }
}
