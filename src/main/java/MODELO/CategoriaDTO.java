package MODELO;

public class CategoriaDTO {
    private int id;                      // Identificador único de la categoría
    private String nombre;               // Nombre descriptivo de la categoría
    private String icono;                // Icono asociado para UI o representación visual
    private String tipo_movimiento;     // Relación con el tipo de movimiento 

    // Constructor vacío necesario para frameworks y serialización
    public CategoriaDTO() {}

    // Constructor con todos los atributos para crear objetos completos fácilmente
    public CategoriaDTO(int id, String nombre, String icono, String tipo_movimiento) {
        this.id = id;
        this.nombre = nombre;
        this.icono = icono;
        this.tipo_movimiento = tipo_movimiento;
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

    public String getTipo_movimiento() {
        return tipo_movimiento;
    }

    public void setTipo_movimiento_id(String tipo_movimiento) {
        this.tipo_movimiento = tipo_movimiento;
    }
}
