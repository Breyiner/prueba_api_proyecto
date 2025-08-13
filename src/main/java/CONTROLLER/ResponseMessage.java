package CONTROLLER; // Define que esta clase pertenece al paquete CONTROLLER, que contiene los controladores REST y clases relacionadas.

public class ResponseMessage { // Clase que encapsula el formato estándar de las respuestas JSON de la API.
    private boolean success; // Indica si la operación fue exitosa (true) o fallida (false).
    private int code;        // Código HTTP (por ejemplo, 200, 404, 500) o código interno para identificar el estado de la respuesta.
    private String message;  // Mensaje descriptivo que explica el resultado de la operación (éxito o error).
    private Object data;     // Objeto que contiene los datos devueltos (por ejemplo, entidades, listas, DTOs) o detalles de error.

    /**
     * Constructor para inicializar una respuesta con todos sus campos.
     * @param success Indica si la operación fue exitosa.
     * @param code Código HTTP o interno asociado a la respuesta.
     * @param message Mensaje descriptivo sobre el resultado.
     * @param data Datos adicionales (puede ser null en caso de error o respuestas sin datos).
     */
    public ResponseMessage(boolean success, int code, String message, Object data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // Getters y setters para permitir la serialización/deserialización y el acceso a los campos.

    /**
     * Obtiene el estado de éxito de la operación.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Establece el estado de éxito de la operación.
     * @param success true para éxito, false para fallo.
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Obtiene el código HTTP o interno de la respuesta.
     * @return Código asociado (por ejemplo, 200, 404, 500).
     */
    public int getCode() {
        return code;
    }

    /**
     * Establece el código HTTP o interno de la respuesta.
     * @param code Código a asignar.
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Obtiene el mensaje descriptivo de la respuesta.
     * @return Mensaje que describe el resultado de la operación.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Establece el mensaje descriptivo de la respuesta.
     * @param message Mensaje a asignar.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Obtiene los datos asociados a la respuesta.
     * @return Objeto que contiene los datos (puede ser null).
     */
    public Object getData() {
        return data;
    }

    /**
     * Establece los datos asociados a la respuesta.
     * @param data Objeto a asignar (entidades, listas, DTOs, etc.).
     */
    public void setData(Object data) {
        this.data = data;
    }
}