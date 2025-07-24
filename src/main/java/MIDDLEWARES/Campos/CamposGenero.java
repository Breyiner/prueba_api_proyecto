package MIDDLEWARES.Campos;

import MIDDLEWARES.Campo;
import java.util.ArrayList;
import java.util.List;

public class CamposGenero {
    
    public static List<Campo> getCampos() {
        
        List<Campo> campos = new ArrayList<>();
        
        campos.add(new Campo("nombre", true, 3, 50, "string"));
        
        return campos;
    }
}
