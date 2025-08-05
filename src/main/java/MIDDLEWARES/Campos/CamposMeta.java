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
        campos.add(new Campo("descripcion", false, 5, 50, "string", null));
        campos.add(new Campo("fecha_limite", false, 10, 10, "date", "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$"));
        
        return campos;
    }
}
