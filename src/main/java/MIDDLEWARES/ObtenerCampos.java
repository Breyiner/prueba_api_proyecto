/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MIDDLEWARES;

import java.util.List;

public class ObtenerCampos {

    public static List<Campo> obtener(String entidad) {
        
        return switch (entidad){
            case "usuario" -> CamposUsuario.getCampos();
            default -> null;
        };
    }
    
}
