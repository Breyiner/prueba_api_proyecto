package MIDDLEWARES.Campos;

import MIDDLEWARES.Campo;
import java.util.ArrayList;
import java.util.List;

public class CamposTiposMovimiento {
    
    public static List<Campo> getCampos() {
        
        List<Campo> campos = new ArrayList<>() ;
        
        campos.add(new Campo("nombre", true, 3, 50, "string", null));
        campos.add(new Campo("icono", true, 3, 50, "string", null));
        campos.add(new Campo("color", true, 2, 7, "string", "^#[0-9A-Fa-f]{1,6}$"));
        
        return campos;
    }
}