package MODEL;

import java.math.BigDecimal;

public class ResumenCategoriasDTO {
    private String icono;
    private String nombre;
    private String color;
    private int cantidad;
    private BigDecimal total;

    public ResumenCategoriasDTO() {
    }

    public ResumenCategoriasDTO(String icono, String nombre, String color, int cantidad, BigDecimal total) {
        this.icono = icono;
        this.nombre = nombre;
        this.color = color;
        this.cantidad = cantidad;
        this.total = total;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
