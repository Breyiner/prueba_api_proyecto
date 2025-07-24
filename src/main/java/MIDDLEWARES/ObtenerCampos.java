package MIDDLEWARES;

import MIDDLEWARES.Campos.CamposCiudad;
import MIDDLEWARES.Campos.CamposEstado;
import MIDDLEWARES.Campos.CamposGenero;
import MIDDLEWARES.Campos.CamposUsuarioLogin;
import MIDDLEWARES.Campos.CamposUsuario;
import java.util.List;

public class ObtenerCampos {

    public static List<Campo> obtener(String entidad) {
        
        return switch (entidad.toLowerCase()){
            case "usuario" -> CamposUsuario.getCampos();
            case "usuariologin" -> CamposUsuarioLogin.getCampos();
            case "ciudades" -> CamposCiudad.getCampos();
            case "generos" -> CamposGenero.getCampos();
            case "estados" -> CamposEstado.getCampos();
            default -> null;
        };
    }
    
}
