package CONTROLLER;

// Importa la clase EstadoDao para operaciones con la base de datos
import MODELO.EstadoDao;
// Importa el modelo Estado
import MODELO.Estado;

// Importaciones para JAX-RS y manejo de respuestas HTTP
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

// Importaciones para manejo de resultados SQL y colecciones
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Define la ruta base del recurso REST
@Path("/estados")
public class EstadoController {

    // Método GET para obtener todos los estados en formato JSON
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getEstados() {
        // Lista para almacenar los objetos Estado obtenidos
        List<Estado> estados = new ArrayList<>();
        try {
            // Ejecuta consulta para obtener todos los estados desde la base de datos
            ResultSet respuesta = EstadoDao.getEstados();
            // Itera sobre cada fila del resultado
            while (respuesta.next()) {
                // Crea un objeto Estado con los datos del resultado
                Estado estado = new Estado(
                    respuesta.getInt("id"),           // ID del estado
                    respuesta.getString("nombre")     // Nombre del estado
                );
                // Agrega el estado a la lista
                estados.add(estado);
            }
            // Cierra el ResultSet para liberar recursos
            respuesta.close();
            // Si la lista no está vacía, responde con éxito y datos
            if (!estados.isEmpty()) {
                return ResponseProvider.success(estados, "Estados obtenidos con éxito.", 200);
            } else {
                // Si no hay estados, responde con error 404
                return ResponseProvider.error("No hay estados registrados.", 404);
            }
        } catch (SQLException e) {
            // En caso de error SQL, responde con error 500 interno
            return ResponseProvider.error("Error interno al obtener los estados", 500);
        }
    }

    // Método GET para obtener un estado específico por ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getEstado(@PathParam("id") int id) {
        // Variable para almacenar el estado encontrado
        Estado estado = null;
        try {
            // Ejecuta consulta para obtener el estado con el ID dado
            ResultSet respuesta = EstadoDao.getEstadoById(id);
            // Si hay resultado, crea el objeto Estado
            while (respuesta.next()) {
                estado = new Estado(
                    respuesta.getInt("id"),
                    respuesta.getString("nombre")
                );
            }
            // Cierra el ResultSet
            respuesta.close();
            // Si no se encontró estado, responde con 404
            if (estado == null) {
                return ResponseProvider.error("El estado no existe.", 404);
            } else {
                // Si existe, responde con éxito y el objeto estado
                return ResponseProvider.success(estado, "Estado obtenido con éxito.", 200);
            }
        } catch (SQLException e) {
            // En caso de error SQL, responde con error 500
            return ResponseProvider.error("Error interno al obtener el estado", 500);
        }
    }

    // Método POST para crear un nuevo estado
    @POST
    @Validar(entidad = "Estados")  // Valida datos antes de procesar
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public static Response createEstado(Estado estadoData) {
        try {
            int idGenerado = 0;  // Variable para capturar el ID generado
            // Ejecuta inserción y devuelve el último registro insertado
            ResultSet ultimoRegistro = EstadoDao.createEstado(estadoData);
            // Obtiene el ID generado del insert
            while (ultimoRegistro.next()) {
                idGenerado = ultimoRegistro.getInt(1);
                estadoData.setId(idGenerado);
            }
            // Cierra el ResultSet
            ultimoRegistro.close();
            // Si no se generó ID, hay un error al crear
            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear el estado.", 400);
            } else {
                // Responde con éxito y el estado creado con su ID
                return ResponseProvider.success(estadoData, "Estado creado con éxito.", 200);
            }
        } catch (SQLException e) {
            // Error interno al crear estado
            return ResponseProvider.error("Error interno al crear el estado.", 500);
        }
    }

    // Método PUT para actualizar un estado existente por ID
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public static Response updateEstado(@PathParam("id") int id, Estado estadoData) {
        try {
            // Verifica si el estado existe mediante llamada interna
            Response estadoExistente = getEstado(id);
            // Si no existe, responde con 404
            if (estadoExistente.getStatus() == 404) return ResponseProvider.error("Este estado no existe.", 404);

            // Actualiza el estado en la base de datos
            int rowsAffected = EstadoDao.updateEstado(id, estadoData);
            // Si la actualización afectó filas, devuelve éxito
            if (rowsAffected != 0) {
                estadoData.setId(id);
                return ResponseProvider.success(estadoData, "Estado actualizado con éxito.", 200);
            } else {
                // No se pudo actualizar el estado (por ejemplo, datos iguales)
                return ResponseProvider.error("Error al actualizar el estado.", 400);
            }
        } catch (Exception e) {
            // Error interno al actualizar estado
            return ResponseProvider.error("Error interno al actualizar el estado.", 500);
        }
    }

    // Método DELETE para eliminar un estado por ID
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response deleteEstado(@PathParam("id") int id) {
        try {
            // Intenta eliminar el estado en la base de datos
            int rowsAffected = EstadoDao.deleteEstado(id);
            // Si se eliminaron filas, responde éxito
            if (rowsAffected != 0) {
                return ResponseProvider.success(null, "Estado eliminado con éxito.", 200);
            } else {
                // Si no existía el estado, responde con 404
                return ResponseProvider.error("Este estado no existe.", 404);
            }
        } catch (Exception e) {
            // Error interno al eliminar el estado
            return ResponseProvider.error("Error interno al eliminar el estado.", 500);
        }
    }
}
