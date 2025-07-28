package MODEL;

import java.math.BigDecimal;

public class ResumenMetasDTO {
    private int id;
    private int tipo_movimiento_id;
    private String icono;
    private String color;
    private String color_bg;
    private String nombre;
    private int cantidad;
    private BigDecimal total;

    public ResumenMetasDTO() {
    }

    public ResumenMetasDTO(int id, int tipo_movimiento_id, String icono, String color, String color_bg, String nombre, int cantidad, BigDecimal total) {
        this.id = id;
        this.tipo_movimiento_id = tipo_movimiento_id;
        this.icono = icono;
        this.nombre = nombre;
        this.color = color;
        this.color_bg = color_bg;
        this.cantidad = cantidad;
        this.total = total;
    }

    public int getTipo_movimiento_id() {
        return tipo_movimiento_id;
    }

    public void setTipo_movimiento_id(int tipo_movimiento_id) {
        this.tipo_movimiento_id = tipo_movimiento_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor_bg() {
        return color_bg;
    }

    public void setColor_bg(String color_bg) {
        this.color_bg = color_bg;
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
