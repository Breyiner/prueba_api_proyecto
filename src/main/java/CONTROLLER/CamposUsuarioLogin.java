package CONTROLLER;

import CONTROLLER.Campo;
import java.util.ArrayList;
import java.util.List;

public class CamposUsuarioLogin {
    

    public static List<Campo> getCampos() {
        
        List<Campo> campos = new ArrayList<>() ;
        
        campos.add(new Campo("correo", true, 3, 50, "string", null));
        campos.add(new Campo("contrasena", true, 8, 20, "string", null));
        
        return campos;
    }

}