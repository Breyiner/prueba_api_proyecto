package MODELO; // Define que esta clase pertenece al paquete MODELO, que contiene entidades y DTOs del sistema.

public class TiposMovimiento { // Clase que representa la entidad TiposMovimiento, usada para clasificar movimientos financieros.

    private int id; // Identificador único del tipo de movimiento en la base de datos.
    private String nombre; // Nombre descriptivo del tipo de movimiento (por ejemplo, "Ingreso", "Gasto", "Ahorro").
    private String icono; // Icono asociado al tipo de movimiento, probablemente para la interfaz de usuario.
    private String color; // Color asociado al tipo de movimiento, usado para visualización (por ejemplo, en hexadecimal).
    private String color_bg; // Color de fondo asociado al tipo de movimiento, usado para visualización.

    /**
     * Constructor por defecto para la serialización/deserialización (por ejemplo, JSON).
     * Permite crear instancias vacías de la clase.
     */
    public TiposMovimiento() {}

    /**
     * Constructor parametrizado para inicializar la entidad con todos sus campos.
     * @param id Identificador único del tipo de movimiento.
     * @param nombre Nombre descriptivo del tipo de movimiento.
     * @param icono Icono asociado al tipo de movimiento.
     * @param color Color asociado al tipo de movimiento.
     * @param color_bg Color de fondo asociado al tipo de movimiento.
     */
    public TiposMovimiento(int id, String nombre, String icono, String color, String color_bg) {
        this.id = id;
        this.nombre = nombre;
        this.icono = icono;
        this.color = color;
        this.color_bg = color_bg;
    }

    /**
     * Obtiene el identificador único del tipo de movimiento.
     * @return El ID del tipo de movimiento.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre descriptivo del tipo de movimiento.
     * @return El nombre del tipo de movimiento.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el icono asociado al tipo de movimiento.
     * @return El icono del tipo de movimiento.
     */
    public String getIcono() {
        return icono;
    }

    /**
     * Obtiene el color asociado al tipo de movimiento.
     * @return El color del tipo de movimiento.
     */
    public String getColor() {
        return color;
    }

    /**
     * Establece el identificador único del tipo de movimiento.
     * @param id El ID a asignar.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Establece el nombre descriptivo del tipo de movimiento.
     * @param nombre El nombre a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece el icono asociado al tipo de movimiento.
     * @param icono El icono a asignar.
     */
    public void setIcono(String icono) {
        this.icono = icono;
    }

    /**
     * Establece el color asociado al tipo de movimiento.
     * @param color El color a asignar.
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Obtiene el color de fondo asociado al tipo de movimiento.
     * @return El color de fondo del tipo de movimiento.
     */
    public String getColor_bg() {
        return color_bg;
    }

    /**
     * Establece el color de fondo asociado al tipo de movimiento.
     * @param color_bg El color de fondo a asignar.
     */
    public void setColor_bg(String color_bg) {
        this.color_bg = color_bg;
    }
}