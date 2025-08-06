package MODELO;

import java.math.BigDecimal;

public class ResumenCategoriasDTO {

    // Identificador único de la categoría
    private int id;

    // Id del tipo de movimiento (ej: ingreso, egreso)
    private int tipo_movimiento_id;

    // Icono representativo de la categoría
    private String icono;

    // Nombre descriptivo de la categoría
    private String nombre;

    // Color principal asociado a la categoría
    private String color;

    // Color de fondo para la categoría (UI)
    private String color_bg;

    // Cantidad de movimientos registrados en esta categoría
    private int cantidad;

    // Total acumulado del monto en esta categoría
    private BigDecimal total;

    // Constructor vacío para frameworks o serialización
    public ResumenCategoriasDTO() {
    }

    // Constructor con todos los campos para crear instancias completas
    public ResumenCategoriasDTO(int id, int tipo_movimiento_id, String icono, String nombre, String color, String color_bg, int cantidad, BigDecimal total) {
        this.id = id;
        this.tipo_movimiento_id = tipo_movimiento_id;
        this.icono = icono;
        this.nombre = nombre;
        this.color = color;
        this.color_bg = color_bg;
        this.cantidad = cantidad;
        this.total = total;
    }

    // Getters y setters para cada propiedad
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
