package MIDDLEWARES.Campos;

import MIDDLEWARES.Campo;
import java.util.ArrayList;
import java.util.List;

public class CamposUsuario {
    

    public static List<Campo> getCampos() {
        
        List<Campo> campos = new ArrayList<>() ;
        
        campos.add(new Campo("nombre", true, 3, 50, "string", null));
        campos.add(new Campo("apellido", true, 3, 50, "string", null));
        campos.add(new Campo("correo", true, 3, 50, "string", null));
        campos.add(new Campo("contrasena", true, 8, 20, "string", null));
        campos.add(new Campo("genero_id", true, 1, 11, "number", null));
        campos.add(new Campo("ciudad_id", true, 1, 11, "number", null));
        campos.add(new Campo("estado_id", true, 1, 11, "number", null));
        
        return campos;
    }

}

