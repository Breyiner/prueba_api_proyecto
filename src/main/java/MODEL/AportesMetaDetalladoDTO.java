package MODEL;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.sql.Date;

public class AportesMetaDetalladoDTO {
    private int id;
    private int meta_id;
    private String icono;
    private String color;
    private String color_bg;
    private String nombre;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private String fecha_creacion;
    private BigDecimal monto;

    // Constructor vacío
    public AportesMetaDetalladoDTO() {}

    // Constructor completo
    public AportesMetaDetalladoDTO(int id, int meta_id, String icono, 
                                  String color, String color_bg, String nombre, String fecha_creacion, 
                                  BigDecimal monto) {
        this.id = id;
        this.meta_id = meta_id;
        this.icono = icono;
        this.color = color;
        this.color_bg = color_bg;
        this.nombre = nombre;
        this.fecha_creacion = fecha_creacion;
        this.monto = monto;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMeta_id() {
        return meta_id;
    }

    public void setMeta_id(int meta_id) {
        this.meta_id = meta_id;
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

    public String getColor_bg() {
        return color_bg;
    }

    public void setColor_bg(String color_bg) {
        this.color_bg = color_bg;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}