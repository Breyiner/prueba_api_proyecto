package MODELO;

public class Categoria {
    private int id;
    private String nombre;
    private String icono;
    private int tipo_movimiento_id;

    public Categoria() {}

    public Categoria(int id, String nombre, String icono, int tipo_movimiento_id) {
        this.id = id;
        this.nombre = nombre;
        this.icono = icono;
        this.tipo_movimiento_id = tipo_movimiento_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public int getTipo_movimiento_id() {
        return tipo_movimiento_id;
    }

    public void setTipo_movimiento_id(int tipo_movimiento_id) {
        this.tipo_movimiento_id = tipo_movimiento_id;
    }
}
