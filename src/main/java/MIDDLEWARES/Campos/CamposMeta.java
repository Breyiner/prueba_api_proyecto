package MIDDLEWARES.Campos;

import MIDDLEWARES.Campo;
import java.util.ArrayList;
import java.util.List;

public class CamposMeta {
    
    public static List<Campo> getCampos() {
        
        List<Campo> campos = new ArrayList<>() ;
        
        campos.add(new Campo("usuario_id", true, 1, 11, "number", null));
        campos.add(new Campo("nombre", true, 3, 50, "string", null));
        campos.add(new Campo("monto", true, 3, 15, "number", null));
        campos.add(new Campo("descripcion", true, 5, 50, "string", null));
        campos.add(new Campo("fecha_limite", false, 8, 8, "date", null));
        
        return campos;
    }
}
