package CONTROLLER; // Define que esta clase pertenece al paquete CONTROLLER, encargado de la lógica de control del sistema.

import MODELO.EstadoDao; // Importa la clase DAO para operaciones con la base de datos relacionadas con estados.
import MODELO.Estado; // Importa la entidad que representa un estado en el sistema.
import javax.ws.rs.*; // Importa anotaciones JAX-RS para definir endpoints REST (GET, POST, PUT, DELETE, etc.).
import javax.ws.rs.core.MediaType; // Define los tipos de contenido para entradas y salidas (por ejemplo, JSON).
import javax.ws.rs.core.Response; // Permite construir respuestas HTTP con códigos de estado y datos.
import java.sql.ResultSet; // Clase para manejar resultados de consultas SQL.
import java.sql.SQLException; // Maneja excepciones relacionadas con operaciones SQL.
import java.util.ArrayList; // Implementación de lista dinámica para almacenar objetos.
import java.util.List; // Interfaz para colecciones de tipo lista.

@Path("/estados") // Define la ruta base para todos los endpoints REST relacionados con estados.
public class EstadoController { // Clase que gestiona las operaciones CRUD para la entidad Estado.

    /**
     * Método para obtener todos los estados registrados en la base de datos.
     * Responde a solicitudes GET en /estados.
     * @return Response HTTP con la lista de estados o un mensaje de error si no hay datos.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response getEstados() {
        List<Estado> estados = new ArrayList<>(); // Lista para almacenar los estados obtenidos.

        try {
            // Llama al método DAO para obtener todos los estados desde la base de datos.
            ResultSet respuesta = EstadoDao.getEstados();

            // Itera sobre el ResultSet para construir objetos Estado.
            while (respuesta.next()) {
                Estado estado = new Estado(
                    respuesta.getInt("id"),         // ID único del estado.
                    respuesta.getString("nombre")   // Nombre del estado.
                );
                estados.add(estado); // Agrega el estado a la lista.
            }

            // Cierra el ResultSet para liberar recursos.
            respuesta.close();

            // Si la lista tiene estados, retorna éxito con código 200.
            if (!estados.isEmpty()) {
                return ResponseProvider.success(estados, "Estados obtenidos con éxito.", 200);
            } else {
                // Si no hay estados, retorna error 404 con mensaje informativo.
                return ResponseProvider.error("No hay estados registrados.", 404);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener los estados", 500);
        }
    }

    /**
     * Método para obtener un estado específico por su ID.
     * Responde a solicitudes GET en /estados/{id}.
     * @param id Identificador único del estado a buscar.
     * @return Response HTTP con los datos del estado o un mensaje de error.
     */
    @GET
    @Path("/{id}") // Ruta con parámetro para buscar un estado específico.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response getEstado(@PathParam("id") int id) {
        Estado estado = null; // Objeto para almacenar el estado encontrado.

        try {
            // Llama al método DAO para obtener el estado por su ID.
            ResultSet respuesta = EstadoDao.getEstadoById(id);

            // Procesa el ResultSet para construir el objeto Estado.
            while (respuesta.next()) {
                estado = new Estado(
                    respuesta.getInt("id"),         // ID único del estado.
                    respuesta.getString("nombre")   // Nombre del estado.
                );
            }

            // Cierra el ResultSet para liberar recursos.
            respuesta.close();

            // Si no se encontró el estado, retorna error 404.
            if (estado == null) {
                return ResponseProvider.error("El estado no existe.", 404);
            } else {
                // Si se encontró, retorna éxito con código 200 y los datos del estado.
                return ResponseProvider.success(estado, "Estado obtenido con éxito.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener el estado", 500);
        }
    }

    /**
     * Método para crear un nuevo estado.
     * Responde a solicitudes POST en /estados.
     * @param estadoData Objeto Estado con los datos del nuevo estado.
     * @return Response HTTP con el estado creado o un mensaje de error.
     */
    @POST
    @Validar(entidad = "Estados") // Aplica validaciones definidas para la entidad Estado.
    @Consumes(MediaType.APPLICATION_JSON) // Indica que recibe datos en formato JSON.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response createEstado(Estado estadoData) {
        try {
            int idGenerado = 0; // Variable para almacenar el ID generado por la base de datos.

            // Llama al método DAO para insertar el estado en la base de datos.
            ResultSet ultimoRegistro = EstadoDao.createEstado(estadoData);

            // Procesa el ResultSet para obtener el ID generado tras la inserción.
            while (ultimoRegistro.next()) {
                idGenerado = ultimoRegistro.getInt(1); // Obtiene el ID del primer campo.
                estadoData.setId(idGenerado); // Asigna el ID al objeto estado.
            }

            // Cierra el ResultSet para liberar recursos.
            ultimoRegistro.close();

            // Si no se generó un ID (inserción fallida), retorna error 400.
            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear el estado.", 400);
            } else {
                // Si la inserción fue exitosa, retorna el estado creado con código 200.
                return ResponseProvider.success(estadoData, "Estado creado con éxito.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al crear el estado.", 500);
        }
    }

    /**
     * Método para actualizar un estado existente.
     * Responde a solicitudes PUT en /estados/{id}.
     * @param id Identificador único del estado a actualizar.
     * @param estadoData Objeto Estado con los nuevos datos.
     * @return Response HTTP indicando éxito o error.
     */
    @PUT
    @Path("/{id}") // Ruta con parámetro para actualizar un estado específico.
    @Consumes(MediaType.APPLICATION_JSON) // Indica que recibe datos en formato JSON.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response updateEstado(@PathParam("id") int id, Estado estadoData) {
        try {
            // Verifica si el estado existe llamando al método getEstado.
            Response estadoExistente = getEstado(id);

            // Si el estado no existe, retorna error 404.
            if (estadoExistente.getStatus() == 404) {
                return ResponseProvider.error("Este estado no existe.", 404);
            }

            // Llama al método DAO para actualizar los datos del estado.
            int rowsAffected = EstadoDao.updateEstado(id, estadoData);

            // Si la actualización fue exitosa, retorna código 200 con el estado actualizado.
            if (rowsAffected != 0) {
                estadoData.setId(id); // Asegura que el ID se mantenga en el objeto.
                return ResponseProvider.success(estadoData, "Estado actualizado con éxito.", 200);
            } else {
                // Si no se afectó ningún registro, retorna error 400.
                return ResponseProvider.error("Error al actualizar el estado.", 400);
            }

        } catch (Exception e) {
            // Captura errores inesperados y retorna error 500.
            return ResponseProvider.error("Error interno al actualizar el estado.", 500);
        }
    }

    /**
     * Método para eliminar un estado de la base de datos.
     * Responde a solicitudes DELETE en /estados/{id}.
     * @param id Identificador único del estado a eliminar.
     * @return Response HTTP indicando éxito o error.
     */
    @DELETE
    @Path("/{id}") // Ruta con parámetro para eliminar un estado específico.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response deleteEstado(@PathParam("id") int id) {
        try {
            // Llama al método DAO para eliminar el estado de la base de datos.
            int rowsAffected = EstadoDao.deleteEstado(id);

            // Si la eliminación fue exitosa, retorna código 200.
            if (rowsAffected != 0) {
                return ResponseProvider.success(null, "Estado eliminado con éxito.", 200);
            } else {
                // Si el estado no existe, retorna error 404.
                return ResponseProvider.error("Este estado no existe.", 404);
            }

        } catch (Exception e) {
            // Captura errores inesperados y retorna error 500.
            return ResponseProvider.error("Error interno al eliminar el estado.", 500);
        }
    }
}