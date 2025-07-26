package MIDDLEWARES;

import MIDDLEWARES.Campos.CamposCategorias;
import MIDDLEWARES.Campos.CamposCiudad;
import MIDDLEWARES.Campos.CamposEstado;
import MIDDLEWARES.Campos.CamposGenero;
import MIDDLEWARES.Campos.CamposMeta;
import MIDDLEWARES.Campos.CamposMovimiento;
import MIDDLEWARES.Campos.CamposRol;
import MIDDLEWARES.Campos.CamposTiposMovimiento;
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
            case "roles" -> CamposRol.getCampos();
            case "tiposmovimiento" -> CamposTiposMovimiento.getCampos();
            case "categorias" -> CamposCategorias.getCampos();
            case "movimiento" -> CamposMovimiento.getCampos();
            case "meta" -> CamposMeta.getCampos();
            default -> null;
        };
    }
    
}
