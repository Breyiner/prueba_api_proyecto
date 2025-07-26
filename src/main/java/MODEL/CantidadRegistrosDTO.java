package MODEL;

public class CantidadRegistrosDTO {
 
    private int cantidad;

    public CantidadRegistrosDTO() {}
    
    public CantidadRegistrosDTO(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
   
}
