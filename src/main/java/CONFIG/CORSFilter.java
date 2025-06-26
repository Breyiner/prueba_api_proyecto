package CONFIG; // Paquete que contiene los filtros de la aplicación

import javax.ws.rs.container.ContainerResponseFilter; // Importa la interfaz para filtrar respuestas de contenedores
import javax.ws.rs.container.ContainerRequestContext; // Importa la clase para manejar el contexto de la solicitud
import javax.ws.rs.ext.Provider; // Importa la anotación para registrar el filtro como un proveedor
import javax.ws.rs.core.Response; // Importa la clase Response para manejar respuestas HTTP
import java.io.IOException; // Importa la clase IOException para manejar excepciones de entrada/salida
import javax.ws.rs.container.ContainerResponseContext; // Importa la clase para manejar el contexto de la respuesta

@Provider // Anotación que indica que esta clase es un proveedor de JAX-RS
public class CORSFilter implements ContainerResponseFilter { // Implementa la interfaz ContainerResponseFilter para filtrar respuestas
    
    /**
     * Método que se ejecuta para filtrar las respuestas del contenedor.
     * Agrega los encabezados necesarios para permitir CORS (Cross-Origin Resource Sharing).
     * 
     * @param requestContext Contexto de la solicitud que contiene información sobre la solicitud HTTP
     * @param responseContext Contexto de la respuesta que permite modificar la respuesta HTTP
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        // Agrega el encabezado para permitir el acceso desde cualquier origen
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
        // Especifica los métodos HTTP permitidos para las solicitudes CORS
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        // Especifica los encabezados permitidos en las solicitudes CORS
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        
        // Manejar solicitudes preflight (OPTIONS)
        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            // Si la solicitud es de tipo OPTIONS, establece el estado de la respuesta a 200 OK
            responseContext.setStatus(Response.Status.OK.getStatusCode());
        }
    }
}