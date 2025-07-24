package MIDDLEWARES.Campos;

import MIDDLEWARES.Campo;
import java.util.ArrayList;
import java.util.List;

public class CamposCategorias {
    
    public static List<Campo> getCampos() {
        
        List<Campo> campos = new ArrayList<>() ;
        
        campos.add(new Campo("nombre", true, 3, 50, "string", null));
        campos.add(new Campo("icono", true, 3, 50, "string", null));
        campos.add(new Campo("tipo_movimiento_id", true, 1, 11, "number", null));
        
        return campos;
    }
    
}