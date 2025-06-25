package MIDDLEWARES;

import javax.ws.rs.ext.Provider;
import javax.ws.rs.container.*;
import javax.ws.rs.core.*;
import java.lang.reflect.Method;
import java.util.List;

@Provider
public class ValidacionFilter {
    
    @Context
    private ResourceInfo resourceInfo; // Para obtener información del método llamado
    
    
}
