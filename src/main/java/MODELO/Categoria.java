package MODELO;

public class Categoria {
    private int id;                      // Identificador único de la categoría
    private String nombre;               // Nombre descriptivo de la categoría
    private String icono;                // Icono asociado para UI o representación visual
    private int tipo_movimiento_id;     // Relación con el tipo de movimiento (FK)

    // Constructor vacío necesario para frameworks y serialización
    public Categoria() {}

    // Constructor con todos los atributos para crear objetos completos fácilmente
    public Categoria(int id, String nombre, String icono, int tipo_movimiento_id) {
        this.id = id;
        this.nombre = nombre;
        this.icono = icono;
        this.tipo_movimiento_id = tipo_movimiento_id;
    }

    // Getters y setters para acceso y modificación controlada

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
