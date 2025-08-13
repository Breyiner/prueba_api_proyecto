package CONTROLLER; // Define que esta clase pertenece al paquete CONTROLLER, encargado de la lógica de control del sistema.

import MODELO.CategoriaDao; // Importa la clase DAO para operaciones con la base de datos relacionadas con categorías.
import MODELO.Categoria; // Importa la entidad que representa una categoría en el sistema.
import MODELO.CategoriaDTO; // Importa el DTO para transferir datos de categorías con información adicional (como el nombre del tipo de movimiento).
import javax.ws.rs.*; // Importa anotaciones JAX-RS para definir endpoints REST (GET, POST, PUT, DELETE, etc.).
import javax.ws.rs.core.MediaType; // Define los tipos de contenido para entradas y salidas (por ejemplo, JSON).
import javax.ws.rs.core.Response; // Permite construir respuestas HTTP con códigos de estado y datos.
import java.sql.ResultSet; // Clase para manejar resultados de consultas SQL.
import java.sql.SQLException; // Maneja excepciones relacionadas con operaciones SQL.
import java.util.ArrayList; // Implementación de lista dinámica para almacenar objetos.
import java.util.List; // Interfaz para colecciones de tipo lista.

@Path("/categorias") // Define la ruta base para todos los endpoints REST relacionados con categorías.
public class CategoriaController { // Clase que gestiona las operaciones CRUD para la entidad Categoria.

    /**
     * Método para obtener todas las categorías registradas en la base de datos.
     * Responde a solicitudes GET en /categorias.
     * @return Response HTTP con la lista de categorías o un mensaje de error si no hay datos.
     */
    @GET // Indica que este método responde a solicitudes HTTP GET.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response getCategorias() {
        List<Categoria> lista = new ArrayList<>(); // Lista para almacenar las categorías obtenidas.

        try {
            // Llama al método DAO para obtener todas las categorías desde la base de datos.
            ResultSet rs = CategoriaDao.getCategorias();

            // Itera sobre el ResultSet para construir objetos Categoria.
            while (rs.next()) {
                Categoria categoria = new Categoria(
                    rs.getInt("id"),                    // ID único de la categoría.
                    rs.getString("nombre"),             // Nombre de la categoría.
                    rs.getString("icono"),              // Icono asociado a la categoría.
                    rs.getInt("tipo_movimiento_id")     // ID del tipo de movimiento asociado.
                );
                lista.add(categoria); // Agrega la categoría a la lista.
            }

            // Cierra el ResultSet para liberar recursos.
            rs.close();

            // Si la lista tiene categorías, retorna éxito con código 200.
            if (!lista.isEmpty()) {
                return ResponseProvider.success(lista, "Categorías obtenidas con éxito.", 200);
            } else {
                // Si no hay categorías, retorna error 404 con mensaje informativo.
                return ResponseProvider.error("No hay categorías registradas.", 404);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener las categorías", 500);
        }
    }

    /**
     * Método para obtener todas las categorías con el nombre del tipo de movimiento.
     * Responde a solicitudes GET en /categorias/nombres.
     * Útil para mostrar información enriquecida en la interfaz de usuario.
     * @return Response HTTP con la lista de categorías en formato DTO o un mensaje de error.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    @Path("nombres") // Define la ruta específica para este endpoint.
    public static Response getCategoriasNombres() {
        List<CategoriaDTO> lista = new ArrayList<>(); // Lista para almacenar categorías en formato DTO.

        try {
            // Llama al método DAO para obtener categorías con nombres de tipo de movimiento.
            ResultSet rs = CategoriaDao.getCategoriasNombres();

            // Itera sobre el ResultSet para construir objetos CategoriaDTO.
            while (rs.next()) {
                CategoriaDTO categoria = new CategoriaDTO(
                    rs.getInt("id"),                    // ID único de la categoría.
                    rs.getString("nombre"),             // Nombre de la categoría.
                    rs.getString("icono"),              // Icono asociado a la categoría.
                    rs.getString("tipo_movimiento")     // Nombre descriptivo del tipo de movimiento.
                );
                lista.add(categoria); // Agrega la categoría DTO a la lista.
            }

            // Cierra el ResultSet para liberar recursos.
            rs.close();

            // Si la lista tiene categorías, retorna éxito con código 200.
            if (!lista.isEmpty()) {
                return ResponseProvider.success(lista, "Categorías obtenidas con éxito.", 200);
            } else {
                // Si no hay categorías, retorna error 404 con mensaje informativo.
                return ResponseProvider.error("No hay categorías registradas.", 404);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener las categorías", 500);
        }
    }

    /**
     * Método para obtener categorías filtradas por tipo de movimiento.
     * Responde a solicitudes GET en /categorias/tipoMovimiento/{tipo_movimiento_id}.
     * @param tipo_movimiento_id Identificador del tipo de movimiento para filtrar.
     * @return Response HTTP con la lista de categorías filtradas o un mensaje de error.
     */
    @GET
    @Path("/tipoMovimiento/{tipo_movimiento_id}") // Ruta con parámetro para filtrar por tipo de movimiento.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response getCategoriasByTipoMovimiento(@PathParam("tipo_movimiento_id") int tipo_movimiento_id) {
        List<Categoria> lista = new ArrayList<>(); // Lista para almacenar categorías filtradas.

        try {
            // Llama al método DAO para obtener categorías asociadas al tipo de movimiento.
            ResultSet rs = CategoriaDao.getCategoriasByMovimientoId(tipo_movimiento_id);

            // Itera sobre el ResultSet para construir objetos Categoria.
            while (rs.next()) {
                Categoria categoria = new Categoria(
                    rs.getInt("id"),                    // ID único de la categoría.
                    rs.getString("nombre"),             // Nombre de la categoría.
                    rs.getString("icono"),              // Icono asociado a la categoría.
                    rs.getInt("tipo_movimiento_id")     // ID del tipo de movimiento asociado.
                );
                lista.add(categoria); // Agrega la categoría a la lista.
            }

            // Cierra el ResultSet para liberar recursos.
            rs.close();

            // Si la lista tiene categorías, retorna éxito con código 200.
            if (!lista.isEmpty()) {
                return ResponseProvider.success(lista, "Categorías obtenidas con éxito.", 200);
            } else {
                // Si no hay categorías para el tipo de movimiento, retorna error 404.
                return ResponseProvider.error("No hay categorías registradas.", 404);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener las categorías", 500);
        }
    }

    /**
     * Método para obtener una categoría específica por su ID.
     * Responde a solicitudes GET en /categorias/{id}.
     * @param id Identificador único de la categoría a buscar.
     * @return Response HTTP con los datos de la categoría o un mensaje de error.
     */
    @GET
    @Path("/{id}") // Ruta con parámetro para buscar una categoría específica.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response getCategoria(@PathParam("id") int id) {
        Categoria categoria = null; // Objeto para almacenar la categoría encontrada.

        try {
            // Llama al método DAO para obtener la categoría por su ID.
            ResultSet rs = CategoriaDao.getCategoriaById(id);

            // Procesa el ResultSet para construir el objeto Categoria.
            while (rs.next()) {
                categoria = new Categoria(
                    rs.getInt("id"),                    // ID único de la categoría.
                    rs.getString("nombre"),             // Nombre de la categoría.
                    rs.getString("icono"),              // Icono asociado a la categoría.
                    rs.getInt("tipo_movimiento_id")     // ID del tipo de movimiento asociado.
                );
            }

            // Cierra el ResultSet para liberar recursos.
            rs.close();

            // Si no se encontró la categoría, retorna error 404.
            if (categoria == null) {
                return ResponseProvider.error("La categoría no existe.", 404);
            } else {
                // Si se encontró, retorna éxito con código 200 y los datos de la categoría.
                return ResponseProvider.success(categoria, "Categoría obtenida con éxito.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener la categoría", 500);
        }
    }

    /**
     * Método para crear una nueva categoría.
     * Responde a solicitudes POST en /categorias.
     * @param categoriaData Objeto Categoria con los datos de la nueva categoría.
     * @return Response HTTP con la categoría creada o un mensaje de error.
     */
    @POST
    @Validar(entidad = "Categorias") // Aplica validaciones definidas para la entidad Categoria.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    @Consumes(MediaType.APPLICATION_JSON) // Indica que recibe datos en formato JSON.
    public static Response createCategoria(Categoria categoriaData) {
        try {
            int idGenerado = 0; // Variable para almacenar el ID generado por la base de datos.

            // Llama al método DAO para insertar la categoría en la base de datos.
            ResultSet rs = CategoriaDao.createCategoria(categoriaData);

            // Procesa el ResultSet para obtener el ID generado tras la inserción.
            while (rs.next()) {
                idGenerado = rs.getInt(1); // Obtiene el ID del primer campo.
                categoriaData.setId(idGenerado); // Asigna el ID al objeto categoría.
            }

            // Cierra el ResultSet para liberar recursos.
            rs.close();

            // Si no se generó un ID (inserción fallida), retorna error 400.
            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear la categoría.", 400);
            } else {
                // Si la inserción fue exitosa, retorna la categoría creada con código 200.
                return ResponseProvider.success(categoriaData, "Categoría creada con éxito.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al crear la categoría.", 500);
        }
    }

    /**
     * Método para actualizar una categoría existente.
     * Responde a solicitudes PUT en /categorias/{id}.
     * @param id Identificador único de la categoría a actualizar.
     * @param categoriaData Objeto Categoria con los nuevos datos.
     * @return Response HTTP indicando éxito o error.
     */
    @PUT
    @Path("/{id}") // Ruta con parámetro para actualizar una categoría específica.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    @Consumes(MediaType.APPLICATION_JSON) // Indica que recibe datos en formato JSON.
    public static Response updateCategoria(@PathParam("id") int id, Categoria categoriaData) {
        try {
            // Verifica si la categoría existe llamando al método getCategoria.
            Response existente = getCategoria(id);

            // Si la categoría no existe, retorna error 404.
            if (existente.getStatus() == 404)
                return ResponseProvider.error("Esta categoría no existe.", 404);

            // Llama al método DAO para actualizar los datos de la categoría.
            int filasAfectadas = CategoriaDao.updateCategoria(id, categoriaData);

            // Si la actualización fue exitosa, retorna código 200 con la categoría actualizada.
            if (filasAfectadas != 0) {
                categoriaData.setId(id); // Asegura que el ID se mantenga en el objeto.
                return ResponseProvider.success(categoriaData, "Categoría actualizada con éxito.", 200);
            } else {
                // Si no se afectó ningún registro, retorna error 400.
                return ResponseProvider.error("Error al actualizar la categoría.", 400);
            }

        } catch (Exception e) {
            // Captura errores inesperados y retorna error 500.
            return ResponseProvider.error("Error interno al actualizar la categoría.", 500);
        }
    }

    /**
     * Método para eliminar una categoría de la base de datos.
     * Responde a solicitudes DELETE en /categorias/{id}.
     * @param id Identificador único de la categoría a eliminar.
     * @return Response HTTP indicando éxito o error.
     */
    @DELETE
    @Path("/{id}") // Ruta con parámetro para eliminar una categoría específica.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response deleteCategoria(@PathParam("id") int id) {
        try {
            // Verifica si la categoría tiene movimientos asociados.
            Response haveMovs = MovimientoController.getMovimientosByCategoriaId(id);
            
            // Si la categoría tiene movimientos asociados, retorna error 409 (Conflicto).
            if (haveMovs.getStatus() == 200)
                return ResponseProvider.error("Esta categoría tiene movimientos relacionados, no se puede eliminar.", 409);

            // Llama al método DAO para eliminar la categoría de la base de datos.
            int filasAfectadas = CategoriaDao.deleteCategoria(id);

            // Si la eliminación fue exitosa, retorna código 200.
            if (filasAfectadas != 0) {
                return ResponseProvider.success(null, "Categoría eliminada con éxito.", 200);
            } else {
                // Si la categoría no existe, retorna error 404.
                return ResponseProvider.error("Esta categoría no existe.", 404);
            }

        } catch (Exception e) {
            // Captura errores inesperados y retorna error 500.
            return ResponseProvider.error("Error interno al eliminar la categoría.", 500);
        }
    }
}