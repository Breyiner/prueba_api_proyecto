package MIDDLEWARES.Campos;

import MIDDLEWARES.Campo;
import java.util.ArrayList;
import java.util.List;

public class CamposAportesMeta {
    
    public static List<Campo> getCampos() {
        
        List<Campo> campos = new ArrayList<>() ;
        
        campos.add(new Campo("meta_id", false, 1, 11, "number", null));
        campos.add(new Campo("monto", true, 3, 15, "number", null));
        campos.add(new Campo("descripcion", false, 5, 50, "string", null));
        
        return campos;
    }
    
}
