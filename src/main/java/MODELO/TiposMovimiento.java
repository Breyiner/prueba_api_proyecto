package MODELO;

public class TiposMovimiento {
    private int id;
    private String nombre;
    private String icono;
    private String color;

    public TiposMovimiento() {}

    public TiposMovimiento(int id, String nombre, String icono, String color) {
        this.id = id;
        this.nombre = nombre;
        this.icono = icono;
        this.color = color;
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
}