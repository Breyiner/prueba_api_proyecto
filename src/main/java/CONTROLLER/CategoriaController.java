package CONTROLLER; // Define que esta clase pertenece al paquete CONTROLLER, encargado de la lógica de control del sistema.

import MODELO.CategoriaDao; // Importa la clase que se comunica con la base de datos para operaciones sobre categorías.
import MODELO.Categoria; // Importa el modelo de datos de la categoría.

import javax.ws.rs.*; // Importa las anotaciones necesarias para crear servicios REST (como @GET, @POST, etc.).
import javax.ws.rs.core.MediaType; // Especifica el tipo de contenido (JSON, XML, etc.).
import javax.ws.rs.core.Response; // Permite construir respuestas HTTP completas.
import java.sql.ResultSet; // Se usa para procesar los resultados de consultas a la base de datos.
import java.sql.SQLException; // Maneja posibles errores al interactuar con la base de datos.
import java.util.ArrayList; // Lista dinámica para almacenar objetos.
import java.util.List; // Interfaz general para listas.

@Path("/categorias") // Define la ruta base para acceder a este controlador mediante una API REST.
public class CategoriaController { // Clase que gestiona las operaciones CRUD de categorías.

    @GET // Indica que este método responde a peticiones GET.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public Response getCategorias() { // Método para obtener todas las categorías registradas.
        List<Categoria> lista = new ArrayList<>(); // Lista para almacenar las categorías recuperadas.

        try {
            ResultSet rs = CategoriaDao.getCategorias(); // Ejecuta la consulta que obtiene todas las categorías.
            while (rs.next()) { // Recorre cada fila del resultado.
                Categoria categoria = new Categoria( // Crea una nueva instancia de categoría con los datos recuperados.
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("icono"),
                    rs.getInt("tipo_movimiento_id")
                );
                lista.add(categoria); // Agrega la categoría a la lista.
            }
            rs.close(); // Cierra el ResultSet para liberar recursos.

            if (!lista.isEmpty()) { // Si se encontraron categorías...
                return ResponseProvider.success(lista, "Categorías obtenidas con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay categorías registradas.", 404); // Si la lista está vacía.
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener las categorías", 500); // Si ocurre un error en la consulta.
        }
    }

    @GET
    @Path("tipoMovimiento/{tipo_movimiento_id}") // Ruta para obtener categorías por tipo de movimiento.
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategoriasByTipoMovimiento(@PathParam("tipo_movimiento_id") int tipo_movimiento_id) {
        List<Categoria> lista = new ArrayList<>();

        try {
            ResultSet rs = CategoriaDao.getCategoriasByMovimientoId(tipo_movimiento_id); // Consulta filtrada.
            while (rs.next()) {
                Categoria categoria = new Categoria(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("icono"),
                    rs.getInt("tipo_movimiento_id")
                );
                lista.add(categoria);
            }
            rs.close();

            if (!lista.isEmpty()) {
                return ResponseProvider.success(lista, "Categorías obtenidas con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay categorías registradas.", 404);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener las categorías", 500);
        }
    }

    @GET
    @Path("/{id}") // Ruta para obtener una categoría específica por ID.
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategoria(@PathParam("id") int id) {
        Categoria categoria = null;

        try {
            ResultSet rs = CategoriaDao.getCategoriaById(id); // Consulta individual.

            while (rs.next()) {
                categoria = new Categoria(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("icono"),
                    rs.getInt("tipo_movimiento_id")
                );
            }
            rs.close();

            if (categoria == null) {
                return ResponseProvider.error("La categoría no existe.", 404); // Si no se encontró.
            } else {
                return ResponseProvider.success(categoria, "Categoría obtenida con éxito.", 200); // Si se encontró.
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener la categoría", 500);
        }
    }

    @POST
    @Validar(entidad = "Categorias") // Aplica validación automática a los datos recibidos.
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el contenido recibido será JSON.
    public Response createCategoria(Categoria categoriaData) {
        try {
            int idGenerado = 0; // Almacena el ID generado por la base de datos.
            ResultSet rs = CategoriaDao.createCategoria(categoriaData); // Ejecuta la inserción en la base de datos.

            while (rs.next()) {
                idGenerado = rs.getInt(1); // Recupera el ID generado.
                categoriaData.setId(idGenerado); // Asigna el ID al objeto.
            }
            rs.close();

            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear la categoría.", 400); // Si la inserción falló.
            } else {
                return ResponseProvider.success(categoriaData, "Categoría creada con éxito.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al crear la categoría.", 500);
        }
    }

    @PUT
    @Path("/{id}") // Ruta para actualizar una categoría específica.
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCategoria(@PathParam("id") int id, Categoria categoriaData) {
        try {
            Response existente = getCategoria(id); // Verifica si la categoría existe.

            if (existente.getStatus() == 404)
                return ResponseProvider.error("Esta categoría no existe.", 404);

            int filasAfectadas = CategoriaDao.updateCategoria(id, categoriaData); // Intenta actualizar.

            if (filasAfectadas != 0) {
                categoriaData.setId(id); // Actualiza el ID en el objeto.
                return ResponseProvider.success(categoriaData, "Categoría actualizada con éxito.", 200);
            } else {
                return ResponseProvider.error("Error al actualizar la categoría.", 400); // Si no se afectó ningún registro.
            }

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar la categoría.", 500);
        }
    }

    @DELETE
    @Path("/{id}") // Ruta para eliminar una categoría por ID.
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCategoria(@PathParam("id") int id) {
        try {
            int filasAfectadas = CategoriaDao.deleteCategoria(id); // Intenta eliminar la categoría.

            if (filasAfectadas != 0) {
                return ResponseProvider.success(null, "Categoría eliminada con éxito.", 200);
            } else {
                return ResponseProvider.error("Esta categoría no existe.", 404); // Si no se encontró para eliminar.
            }

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al eliminar la categoría.", 500);
        }
    }
}
