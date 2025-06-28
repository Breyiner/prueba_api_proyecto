package MIDDLEWARES;

import PROVIDERS.ResponseProvider;
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
            
            // Se obtiene la anotacion
            Validar anotacion = metodo.getAnnotation(Validar.class);
            
            // Leer el JSON del request y convertirlo a String
            String data = new String(requestContext.getEntityStream().readAllBytes());
            
            // El json del request convetido en String se convierte en un JSONObject para poder recorrerlo
            JSONObject jsonData = new JSONObject(data);
            
            // obtenemos la entidad proveniente de la anotacion
            String entidad = anotacion.entidad();
            
            List<Campo> campos = ObtenerCampos.obtener(entidad);
            
            
            // Validar el objeto
            List<String> errores = ValidarCampos.validar(jsonData, campos);
            if (!errores.isEmpty()) {
                requestContext.abortWith(
                        ResponseProvider.error("Error al momento de validar los datos", 400, errores)
                );
            }
            
            requestContext.setEntityStream(new java.io.ByteArrayInputStream(data.getBytes()));
        }
        
    }
    
}
