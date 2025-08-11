package CONTROLLER;

import java.util.List;
import javax.ws.rs.core.Response;
import CONTROLLER.ResponseMessage;

public class ResponseProvider {

    // Método para construir una respuesta exitosa con datos y mensaje
    public static Response success(Object data, String message, int status) {
        return Response.status(status) // Define el código HTTP
                .entity(new ResponseMessage(true, status, message, data)) // Crea el cuerpo de la respuesta con éxito=true
                .build(); // Construye la respuesta final
    }
    
    // Método para construir una respuesta de error sin detalles adicionales
    public static Response error(String message, int status) {
        return Response.status(status) // Código HTTP de error
                .entity(new ResponseMessage(false, status, message, null)) // Cuerpo con éxito=false y sin datos
                .build();
    }
    
    // Método para construir una respuesta de error que incluye una lista de errores específicos
    public static Response error(String message, int status, List<String> errores) {
        return Response.status(status)
                .entity(new ResponseMessage(false, status, message, errores)) // Los errores van en el campo data
                .build();
    }
    
    // Método para construir una respuesta de error con un objeto adicional (no usado en el body, siempre null)
    public static Response error(String message, int status, Object data) {
        return Response.status(status)
                .entity(new ResponseMessage(false, status, message, null)) // Siempre pasa null en data, posible error lógico
                .build();
    }
}
