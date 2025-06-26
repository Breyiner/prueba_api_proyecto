package MIDDLEWARES;

import java.io.IOException;
import javax.ws.rs.container.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.List;
import org.json.JSONObject;

@Provider
public class ValidacionFilter implements ContainerRequestFilter {
    
    @Context
    private ResourceInfo resourceInfo; // Para obtener información del método llamado
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Obtener el método del controlador que se está ejecutando
        Method metodo = resourceInfo.getResourceMethod();
        // Verificar si tiene la anotación @Validar
        if (metodo.isAnnotationPresent(Validar.class)) {
            
//             Leer el JSON del request y convertirlo a la clase especificada
            String data = new String(requestContext.getEntityStream().readAllBytes());
            JSONObject jsonData = new JSONObject(data);
//             Validar el objeto
            List<String> errores = ValidarCampos.validar(jsonData);
            if (!errores.isEmpty()) {
                requestContext.abortWith(
                    Response.ok("puto")
                        .build()
                );
            }
            
            requestContext.setEntityStream(new java.io.ByteArrayInputStream(data.getBytes()));
        }
        
    }
    
}
