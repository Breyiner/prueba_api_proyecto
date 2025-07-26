package MODEL;

import java.math.BigDecimal;

public class ResumenMetasDTO {
    private String icono;
    private String color;
    private String nombre;
    private int cantidad;
    private BigDecimal total;

    public ResumenMetasDTO() {
    }

    public ResumenMetasDTO(String icono, String color, String nombre, int cantidad, BigDecimal total) {
        this.icono = icono;
        this.color = color;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.total = total;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
