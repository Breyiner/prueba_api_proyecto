package CONTROLLER;

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
            case "aportesmeta" -> CamposAportesMeta.getCampos();
            case "usuarioperfil" -> CamposUsuarioPerfil.getCampos();
            default -> null;
        };
    }
    
}
