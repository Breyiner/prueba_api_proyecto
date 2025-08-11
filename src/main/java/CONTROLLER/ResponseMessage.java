package CONTROLLER;

// Clase para encapsular el formato estándar de la respuesta JSON 
public class ResponseMessage {
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
