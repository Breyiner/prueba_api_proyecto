/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MIDDLEWARES;

import MIDDLEWARES.Campos.CamposUsuarioLogin;
import MIDDLEWARES.Campos.CamposUsuario;
import java.util.List;

public class ObtenerCampos {

    public static List<Campo> obtener(String entidad) {
        
        return switch (entidad.toLowerCase()){
            case "usuario" -> CamposUsuario.getCampos();
            case "usuariologin" -> CamposUsuarioLogin.getCampos();
            default -> null;
        };
    }
    
}
