package CONTROLLER; // Define que esta clase pertenece al paquete CONTROLLER, encargado de la lógica de control del sistema.

import MODELO.CiudadDao; // Importa la clase DAO para operaciones con la base de datos relacionadas con ciudades.
import MODELO.Ciudad;    // Importa la entidad que representa una ciudad en el sistema.
import MODELO.Usuario;   // Importa la entidad Usuario para verificar relaciones en eliminación.
import javax.ws.rs.*; // Importa anotaciones JAX-RS para definir endpoints REST (GET, POST, PUT, DELETE, etc.).
import javax.ws.rs.core.MediaType; // Define los tipos de contenido para entradas y salidas (por ejemplo, JSON).
import javax.ws.rs.core.Response; // Permite construir respuestas HTTP con códigos de estado y datos.
import java.sql.ResultSet; // Clase para manejar resultados de consultas SQL.
import java.sql.SQLException; // Maneja excepciones relacionadas con operaciones SQL.
import java.util.ArrayList; // Implementación de lista dinámica para almacenar objetos.
import java.util.List; // Interfaz para colecciones de tipo lista.

@Path("/ciudades") // Define la ruta base para todos los endpoints REST relacionados con ciudades.
public class CiudadController { // Clase que gestiona las operaciones CRUD para la entidad Ciudad.

    /**
     * Método para obtener todas las ciudades registradas en la base de datos.
     * Responde a solicitudes GET en /ciudades.
     * @return Response HTTP con la lista de ciudades o un mensaje de error si no hay datos.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response getCiudades() {
        List<Ciudad> ciudades = new ArrayList<>(); // Lista para almacenar las ciudades obtenidas.

        try {
            // Llama al método DAO para obtener todas las ciudades desde la base de datos.
            ResultSet respuesta = CiudadDao.getCiudades();

            // Itera sobre el ResultSet para construir objetos Ciudad.
            while (respuesta.next()) {
                Ciudad ciudad = new Ciudad(
                    respuesta.getInt("id"),         // ID único de la ciudad.
                    respuesta.getString("nombre")   // Nombre de la ciudad.
                );
                ciudades.add(ciudad); // Agrega la ciudad a la lista.
            }

            // Cierra el ResultSet para liberar recursos.
            respuesta.close();

            // Si la lista tiene ciudades, retorna éxito con código 200.
            if (!ciudades.isEmpty()) {
                return ResponseProvider.success(ciudades, "Ciudades obtenidas con éxito.", 200);
            } else {
                // Si no hay ciudades, retorna error 404 con mensaje informativo.
                return ResponseProvider.error("No hay ciudades registradas.", 404);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener las ciudades", 500);
        }
    }

    /**
     * Método para obtener una ciudad específica por su ID.
     * Responde a solicitudes GET en /ciudades/{id}.
     * @param id Identificador único de la ciudad a buscar.
     * @return Response HTTP con los datos de la ciudad o un mensaje de error.
     */
    @GET
    @Path("/{id}") // Ruta con parámetro para buscar una ciudad específica.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response getCiudad(@PathParam("id") int id) {
        Ciudad ciudad = null; // Objeto para almacenar la ciudad encontrada.

        try {
            // Llama al método DAO para obtener la ciudad por su ID.
            ResultSet respuesta = CiudadDao.getCiudadById(id);

            // Procesa el ResultSet para construir el objeto Ciudad.
            while (respuesta.next()) {
                ciudad = new Ciudad(
                    respuesta.getInt("id"),         // ID único de la ciudad.
                    respuesta.getString("nombre")   // Nombre de la ciudad.
                );
            }

            // Cierra el ResultSet para liberar recursos.
            respuesta.close();

            // Si no se encontró la ciudad, retorna error 404.
            if (ciudad == null) {
                return ResponseProvider.error("La ciudad no existe.", 404);
            } else {
                // Si se encontró, retorna éxito con código 200 y los datos de la ciudad.
                return ResponseProvider.success(ciudad, "Ciudad obtenida con éxito.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener la ciudad", 500);
        }
    }

    /**
     * Método para crear una nueva ciudad.
     * Responde a solicitudes POST en /ciudades.
     * @param ciudadData Objeto Ciudad con los datos de la nueva ciudad.
     * @return Response HTTP con la ciudad creada o un mensaje de error.
     */
    @POST
    @Validar(entidad = "Ciudades") // Aplica validaciones definidas para la entidad Ciudad.
    @Consumes(MediaType.APPLICATION_JSON) // Indica que recibe datos en formato JSON.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response createCiudad(Ciudad ciudadData) {
        try {
            int idGenerado = 0; // Variable para almacenar el ID generado por la base de datos.

            // Llama al método DAO para insertar la ciudad en la base de datos.
            ResultSet ultimoRegistro = CiudadDao.createCiudad(ciudadData);

            // Procesa el ResultSet para obtener el ID generado tras la inserción.
            while (ultimoRegistro.next()) {
                idGenerado = ultimoRegistro.getInt(1); // Obtiene el ID del primer campo.
                ciudadData.setId(idGenerado); // Asigna el ID al objeto ciudad.
            }

            // Cierra el ResultSet para liberar recursos.
            ultimoRegistro.close();

            // Si no se generó un ID (inserción fallida), retorna error 400.
            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear la ciudad.", 400);
            } else {
                // Si la inserción fue exitosa, retorna la ciudad creada con código 200.
                return ResponseProvider.success(ciudadData, "Ciudad creada con éxito.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al crear la ciudad.", 500);
        }
    }

    /**
     * Método para actualizar una ciudad existente.
     * Responde a solicitudes PUT en /ciudades/{id}.
     * @param id Identificador único de la ciudad a actualizar.
     * @param ciudadData Objeto Ciudad con los nuevos datos.
     * @return Response HTTP indicando éxito o error.
     */
    @PUT
    @Path("/{id}") // Ruta con parámetro para actualizar una ciudad específica.
    @Consumes(MediaType.APPLICATION_JSON) // Indica que recibe datos en formato JSON.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response updateCiudad(@PathParam("id") int id, Ciudad ciudadData) {
        try {
            // Verifica si la ciudad existe llamando al método getCiudad.
            Response ciudadExistente = getCiudad(id);

            // Si la ciudad no existe, retorna error 404.
            if (ciudadExistente.getStatus() == 404)
                return ResponseProvider.error("Esta ciudad no existe.", 404);

            // Llama al método DAO para actualizar los datos de la ciudad.
            int rowsAffected = CiudadDao.updateCiudad(id, ciudadData);

            // Si la actualización fue exitosa, retorna código 200 con la ciudad actualizada.
            if (rowsAffected != 0) {
                ciudadData.setId(id); // Asegura que el ID se mantenga en el objeto.
                return ResponseProvider.success(ciudadData, "Ciudad actualizada con éxito.", 200);
            } else {
                // Si no se afectó ningún registro, retorna error 400.
                return ResponseProvider.error("Error al actualizar la ciudad.", 400);
            }

        } catch (Exception e) {
            // Captura errores inesperados y retorna error 500.
            return ResponseProvider.error("Error interno al actualizar la ciudad.", 500);
        }
    }

    /**
     * Método para eliminar una ciudad de la base de datos.
     * Responde a solicitudes DELETE en /ciudades/{id}.
     * @param id Identificador único de la ciudad a eliminar.
     * @return Response HTTP indicando éxito o error.
     */
    @DELETE
    @Path("/{id}") // Ruta con parámetro para eliminar una ciudad específica.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response deleteCiudad(@PathParam("id") int id) {
        try {
            // Verifica si la ciudad tiene usuarios asociados llamando al controlador de usuarios.
            Response respuesta = UserController.getUsuariosByCiudadId(id);

            // Si la ciudad tiene usuarios asociados, retorna error 409 (Conflicto).
            if (respuesta.getStatus() == 200)
                return ResponseProvider.error("Esta ciudad tiene usuarios relacionados, no se puede eliminar.", 409);

            // Llama al método DAO para eliminar la ciudad de la base de datos.
            int rowsAffected = CiudadDao.deleteCiudad(id);

            // Si la eliminación fue exitosa, retorna código 200.
            if (rowsAffected != 0) {
                return ResponseProvider.success(null, "Ciudad eliminada con éxito.", 200);
            } else {
                // Si la ciudad no existe, retorna error 404.
                return ResponseProvider.error("Esta ciudad no existe.", 404);
            }

        } catch (Exception e) {
            // Captura errores inesperados y retorna error 500.
            return ResponseProvider.error("Error interno al eliminar la ciudad.", 500);
        }
    }
}