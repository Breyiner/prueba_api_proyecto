package CONTROLLER;

import CONTROLLER.Campo;
import java.util.ArrayList;
import java.util.List;

public class CamposCiudad {
    
    public static List<Campo> getCampos() {
        
        List<Campo> campos = new ArrayList<>() ;
        
        campos.add(new Campo("nombre", true, 3, 50, "string", null));
        
        return campos;
    }
}
