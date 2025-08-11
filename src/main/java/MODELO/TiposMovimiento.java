package MODELO;

public class TiposMovimiento {
    private int id;
    private String nombre;
    private String icono;
    private String color;
    private String color_bg;

    public TiposMovimiento() {}

    public TiposMovimiento(int id, String nombre, String icono, String color, String color_bg) {
        this.id = id;
        this.nombre = nombre;
        this.icono = icono;
        this.color = color;
        this.color_bg = color_bg;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIcono() {
        return icono;
    }

    public String getColor() {
        return color;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIcono(String icono) {
        this.icono = icono;
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
    
    
}