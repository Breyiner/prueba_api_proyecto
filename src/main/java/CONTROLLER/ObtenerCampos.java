package CONTROLLER;

import java.util.List;

public class ObtenerCampos {

    // Método estático que devuelve la lista de campos según la entidad recibida
    public static List<Campo> obtener(String entidad) {
        
        // Utiliza switch expression para elegir la lista de campos según la entidad (en minúsculas)
        return switch (entidad.toLowerCase()){
            case "usuario" -> CamposUsuario.getCampos();               // Campos para la entidad usuario
            case "usuariologin" -> CamposUsuarioLogin.getCampos();     // Campos para login de usuario
            case "ciudades" -> CamposCiudad.getCampos();                // Campos para ciudades
            case "generos" -> CamposGenero.getCampos();                 // Campos para géneros
            case "estados" -> CamposEstado.getCampos();                 // Campos para estados
            case "roles" -> CamposRol.getCampos();                       // Campos para roles
            case "tiposmovimiento" -> CamposTiposMovimiento.getCampos();// Campos para tipos de movimiento
            case "categorias" -> CamposCategorias.getCampos();          // Campos para categorías
            case "movimiento" -> CamposMovimiento.getCampos();          // Campos para movimiento
            case "meta" -> CamposMeta.getCampos();                       // Campos para meta
            case "aportesmeta" -> CamposAportesMeta.getCampos();        // Campos para aportes a meta
            case "usuarioperfil" -> CamposUsuarioPerfil.getCampos();    // Campos para perfil de usuario
            default -> null;                                             // Si no coincide, retorna null
        };
    }
    
}
