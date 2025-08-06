package CONTROLLER;

import java.util.List;
import javax.ws.rs.core.Response;

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

// Clase para encapsular el formato estándar de la respuesta JSON
class ResponseMessage {
    private boolean success; // Indica si la operación fue exitosa o no
    private int code;        // Código HTTP o código interno
    private String message;  // Mensaje descriptivo
    private Object data;     // Datos adicionales o errores (puede ser cualquier objeto)
    
    public ResponseMessage(boolean success, int code, String message, Object data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // Getters y setters para la serialización/deserialización y acceso a los campos

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
