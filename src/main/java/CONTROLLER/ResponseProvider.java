package CONTROLLER; // Define que esta clase pertenece al paquete CONTROLLER, que contiene los controladores REST y clases utilitarias.

import java.util.List; // Importa la interfaz List para manejar listas de errores.
import javax.ws.rs.core.Response; // Importa la clase Response de JAX-RS para construir respuestas HTTP.
import CONTROLLER.ResponseMessage; // Importa la clase que encapsula el formato estándar de las respuestas JSON.

public class ResponseProvider { // Clase utilitaria para construir respuestas HTTP estandarizadas.

    /**
     * Método para construir una respuesta HTTP exitosa con datos y un mensaje descriptivo.
     * @param data Objeto que contiene los datos a devolver (entidades, listas, DTOs, etc.).
     * @param message Mensaje descriptivo del resultado (por ejemplo, "Movimientos obtenidos con éxito.").
     * @param status Código HTTP de la respuesta (por ejemplo, 200 para éxito).
     * @return Objeto Response con el código HTTP y un cuerpo JSON basado en ResponseMessage.
     */
    public static Response success(Object data, String message, int status) {
        return Response.status(status) // Establece el código HTTP de la respuesta.
                .entity(new ResponseMessage(true, status, message, data)) // Crea el cuerpo con éxito=true, el código, mensaje y datos.
                .build(); // Construye y retorna la respuesta HTTP final.
    }

    /**
     * Método para construir una respuesta HTTP de error sin datos adicionales.
     * @param message Mensaje descriptivo del error (por ejemplo, "Error interno al obtener los movimientos").
     * @param status Código HTTP de error (por ejemplo, 404, 500).
     * @return Objeto Response con el código HTTP y un cuerpo JSON basado en ResponseMessage.
     */
    public static Response error(String message, int status) {
        return Response.status(status) // Establece el código HTTP de error.
                .entity(new ResponseMessage(false, status, message, null)) // Crea el cuerpo con éxito=false, el código, mensaje y datos null.
                .build(); // Construye y retorna la respuesta HTTP final.
    }

    /**
     * Método para construir una respuesta HTTP de error con una lista de errores específicos.
     * @param message Mensaje descriptivo del error (por ejemplo, "Datos inválidos").
     * @param status Código HTTP de error (por ejemplo, 400).
     * @param errores Lista de mensajes de error detallados.
     * @return Objeto Response con el código HTTP y un cuerpo JSON que incluye la lista de errores en el campo data.
     */
    public static Response error(String message, int status, List<String> errores) {
        return Response.status(status) // Establece el código HTTP de error.
                .entity(new ResponseMessage(false, status, message, errores)) // Crea el cuerpo con éxito=false, el código, mensaje y la lista de errores en data.
                .build(); // Construye y retorna la respuesta HTTP final.
    }

    /**
     * Método para construir una respuesta HTTP de error con un objeto adicional (no usado en el cuerpo).
     * @param message Mensaje descriptivo del error.
     * @param status Código HTTP de error.
     * @param data Objeto adicional (no se usa, siempre se pasa null en el cuerpo).
     * @return Objeto Response con el código HTTP y un cuerpo JSON basado en ResponseMessage (data siempre null).
     */
    public static Response error(String message, int status, Object data) {
        return Response.status(status) // Establece el código HTTP de error.
                .entity(new ResponseMessage(false, status, message, null)) // Crea el cuerpo con éxito=false, el código, mensaje y datos null (ignora el parámetro data).
                .build(); // Construye y retorna la respuesta HTTP final.
    }
}