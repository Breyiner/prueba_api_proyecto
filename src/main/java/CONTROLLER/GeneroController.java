package CONTROLLER; // Define que esta clase pertenece al paquete CONTROLLER, que contiene los controladores RESTful.

import MODELO.GeneroDao; // Importa la clase DAO para interactuar con la tabla 'generos' en la base de datos.
import MODELO.Genero; // Importa la entidad Genero, que representa un género en el sistema.
import javax.ws.rs.*; // Importa las anotaciones de JAX-RS para definir endpoints REST.
import javax.ws.rs.core.MediaType; // Importa MediaType para especificar el formato de las solicitudes y respuestas.
import javax.ws.rs.core.Response; // Importa Response para construir respuestas HTTP.
import java.sql.ResultSet; // Importa ResultSet para manejar resultados de consultas SQL.
import java.sql.SQLException; // Importa SQLException para manejar excepciones SQL.
import java.util.ArrayList; // Importa ArrayList para almacenar listas de géneros.
import java.util.List; // Importa List para trabajar con colecciones.

@Path("/generos") // Define la ruta base para los endpoints de este controlador (/generos).
public class GeneroController { // Clase que maneja las operaciones REST para la entidad Genero.

    /**
     * Endpoint para obtener todos los géneros registrados.
     * @return Respuesta HTTP con una lista de géneros en formato JSON o un error si falla.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON.
    public static Response getGeneros() {
        List<Genero> generos = new ArrayList<>(); // Lista para almacenar los géneros obtenidos.
        try {
            ResultSet respuesta = GeneroDao.getGeneros(); // Llama al DAO para obtener todos los géneros.
            while (respuesta.next()) {
                // Crea un objeto Genero por cada registro en el ResultSet.
                Genero genero = new Genero(
                    respuesta.getInt("id"),
                    respuesta.getString("nombre")
                );
                generos.add(genero); // Agrega el género a la lista.
            }
            respuesta.close(); // Cierra el ResultSet para liberar recursos.
            if (!generos.isEmpty()) {
                // Devuelve una respuesta exitosa con la lista de géneros.
                return ResponseProvider.success(generos, "Géneros obtenidos con éxito.", 200);
            } else {
                // Devuelve un error 404 si no hay géneros registrados.
                return ResponseProvider.error("No hay géneros registrados.", 404);
            }
        } catch (SQLException e) {
            // Devuelve un error 500 si ocurre una excepción SQL.
            return ResponseProvider.error("Error interno al obtener los géneros", 500);
        }
    }

    /**
     * Endpoint para obtener un género específico por su ID.
     * @param id Identificador único del género, pasado como parámetro en la URL.
     * @return Respuesta HTTP con el género en formato JSON o un error si falla.
     */
    @GET
    @Path("/{id}") // Define la ruta con un parámetro dinámico (/generos/{id}).
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getGenero(@PathParam("id") int id) {
        Genero genero = null; // Variable para almacenar el género encontrado.
        try {
            ResultSet respuesta = GeneroDao.getGeneroById(id); // Llama al DAO para obtener el género por ID.
            while (respuesta.next()) {
                // Crea un objeto Genero con los datos del ResultSet.
                genero = new Genero(
                    respuesta.getInt("id"),
                    respuesta.getString("nombre")
                );
            }
            respuesta.close(); // Cierra el ResultSet para liberar recursos.
            if (genero == null) {
                // Devuelve un error 404 si el género no existe.
                return ResponseProvider.error("El género no existe.", 404);
            } else {
                // Devuelve una respuesta exitosa con el género.
                return ResponseProvider.success(genero, "Género obtenido con éxito.", 200);
            }
        } catch (SQLException e) {
            // Devuelve un error 500 si ocurre una excepción SQL.
            return ResponseProvider.error("Error interno al obtener el género", 500);
        }
    }

    /**
     * Endpoint para crear un nuevo género.
     * @param generoData Objeto Genero con los datos del nuevo género, recibido en el cuerpo de la solicitud.
     * @return Respuesta HTTP con el género creado en formato JSON o un error si falla.
     */
    @POST
    @Validar(entidad = "Generos") // Aplica validación personalizada para la entidad Generos.
    @Consumes(MediaType.APPLICATION_JSON) // Indica que la solicitud debe ser en formato JSON.
    @Produces(MediaType.APPLICATION_JSON)
    public static Response createGenero(Genero generoData) {
        try {
            int idGenerado = 0; // Variable para almacenar el ID generado del nuevo género.
            ResultSet ultimoRegistro = GeneroDao.createGenero(generoData); // Llama al DAO para crear el género.
            while (ultimoRegistro.next()) {
                idGenerado = ultimoRegistro.getInt(1); // Obtiene el ID generado.
                generoData.setId(idGenerado); // Actualiza el objeto con el ID generado.
            }
            ultimoRegistro.close(); // Cierra el ResultSet para liberar recursos.
            if (idGenerado == 0) {
                // Devuelve un error 400 si no se generó un ID (fallo en la creación).
                return ResponseProvider.error("Error al crear el género.", 400);
            } else {
                // Devuelve una respuesta exitosa con el género creado.
                return ResponseProvider.success(generoData, "Género creado con éxito.", 200);
            }
        } catch (SQLException e) {
            // Devuelve un error 500 si ocurre una excepción SQL.
            return ResponseProvider.error("Error interno al crear el género.", 500);
        }
    }

    /**
     * Endpoint para actualizar un género existente.
     * @param id Identificador único del género, pasado como parámetro en la URL.
     * @param generoData Objeto Genero con los nuevos datos, recibido en el cuerpo de la solicitud.
     * @return Respuesta HTTP con el género actualizado en formato JSON o un error si falla.
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public static Response updateGenero(@PathParam("id") int id, Genero generoData) {
        try {
            // Verifica si el género existe llamando al endpoint getGenero.
            Response generoExistente = getGenero(id);
            if (generoExistente.getStatus() == 404) {
                return ResponseProvider.error("Este género no existe.", 404);
            }

            int rowsAffected = GeneroDao.updateGenero(id, generoData); // Llama al DAO para actualizar el género.
            if (rowsAffected != 0) {
                generoData.setId(id); // Actualiza el ID en el objeto generoData.
                // Devuelve una respuesta exitosa con el género actualizado.
                return ResponseProvider.success(generoData, "Género actualizado con éxito.", 200);
            } else {
                // Devuelve un error 400 si no se actualizó el género.
                return ResponseProvider.error("Error al actualizar el género.", 400);
            }
        } catch (Exception e) {
            // Devuelve un error 500 si ocurre cualquier excepción.
            return ResponseProvider.error("Error interno al actualizar el género.", 500);
        }
    }

    /**
     * Endpoint para eliminar un género.
     * @param id Identificador único del género, pasado como parámetro en la URL.
     * @return Respuesta HTTP con un mensaje de éxito o un error si falla.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response deleteGenero(@PathParam("id") int id) {
        try {
            // Verifica si el género tiene usuarios asociados llamando a UserController.
            Response haveUsers = UserController.getUsuariosByGeneroId(id);
            if (haveUsers.getStatus() == 200) {
                // Devuelve un error 409 si el género tiene usuarios relacionados.
                return ResponseProvider.error("Este género tiene usuarios relacionados, no se puede eliminar.", 409);
            }

            int rowsAffected = GeneroDao.deleteGenero(id); // Llama al DAO para eliminar el género.
            if (rowsAffected != 0) {
                // Devuelve una respuesta exitosa si se eliminó el género.
                return ResponseProvider.success(null, "Género eliminado con éxito.", 200);
            } else {
                // Devuelve un error 404 si el género no existe.
                return ResponseProvider.error("Este género no existe.", 404);
            }
        } catch (Exception e) {
            // Devuelve un error 500 si ocurre cualquier excepción.
            return ResponseProvider.error("Error interno al eliminar el género.", 500);
        }
    }
}