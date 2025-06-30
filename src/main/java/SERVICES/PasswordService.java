package SERVICES;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordService {

    public static String hashPassword(String contrasenaText) {
        
        return BCrypt.hashpw(contrasenaText, BCrypt.gensalt());
    }

    public static boolean checkPassword(String contrasenaText, String hashedContrasena) {
        return BCrypt.checkpw(contrasenaText, hashedContrasena);
    }
}