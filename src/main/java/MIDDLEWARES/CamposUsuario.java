package MIDDLEWARES;

import java.util.ArrayList;
import java.util.List;

public class CamposUsuario {
    

    public static List<Campo> getCampos() {
        
        List<Campo> campos = new ArrayList<>() ;
        
        campos.add(new Campo("nombre", true, 3, 50, "string"));
        campos.add(new Campo("apellido", true, 3, 50, "string"));
        campos.add(new Campo("correo", true, 3, 50, "string"));
        campos.add(new Campo("contrasena", true, 8, 20, "string"));
        campos.add(new Campo("genero_id", true, 1, 11, "number"));
        campos.add(new Campo("ciudad_id", true, 1, 11, "number"));
        campos.add(new Campo("estado_id", true, 1, 11, "number"));
        
        return campos;
    }

}
