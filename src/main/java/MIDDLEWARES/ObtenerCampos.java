package MIDDLEWARES;

import MIDDLEWARES.Campos.CamposCiudades;
import MIDDLEWARES.Campos.CamposUsuarioLogin;
import MIDDLEWARES.Campos.CamposUsuario;
import java.util.List;

public class ObtenerCampos {

    public static List<Campo> obtener(String entidad) {
        
        return switch (entidad.toLowerCase()){
            case "usuario" -> CamposUsuario.getCampos();
            case "usuariologin" -> CamposUsuarioLogin.getCampos();
            case "ciudades" -> CamposCiudades.getCampos();
            default -> null;
        };
    }
    
}
