package MODELO;

// DTO simple para encapsular la cantidad total de registros (ej. conteo de filas)
public class CantidadRegistrosDTO {
 
    private int cantidad; // Campo para almacenar la cantidad de registros

    // Constructor vacío requerido para frameworks y serialización
    public CantidadRegistrosDTO() {}

    // Constructor que inicializa la cantidad directamente
    public CantidadRegistrosDTO(int cantidad) {
        this.cantidad = cantidad;
    }

    // Getter para obtener la cantidad
    public int getCantidad() {
        return cantidad;
    }

    // Setter para modificar la cantidad
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
   
}