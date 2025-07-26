package MODEL;

import java.math.BigDecimal;

public class ResumenCardDTO {
    private String icono;
    private String color;
    private String nombre;
    private BigDecimal total;
    private String tipo; // "movimiento" o "meta" para identificar el origen

    // Constructor vacío
    public ResumenCardDTO() {}

    // Constructor completo
    public ResumenCardDTO(String icono, String color, String nombre, BigDecimal total) {
        this.icono = icono;
        this.color = color;
        this.nombre = nombre;
        this.total = total;
    }

    // Constructor con tipo
    public ResumenCardDTO(String icono, String color, String nombre, BigDecimal total, String tipo) {
        this.icono = icono;
        this.color = color;
        this.nombre = nombre;
        this.total = total;
        this.tipo = tipo;
    }

    // Getters y Setters
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}