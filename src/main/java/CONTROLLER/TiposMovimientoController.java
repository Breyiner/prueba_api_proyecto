package CONTROLLER;

import MODELO.TiposMovimientoDao;
import MODELO.TiposMovimiento;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Path("/tiposMovimiento")
// Ruta base para este controlador REST: todos los métodos responden bajo "/tiposMovimiento"
public class TiposMovimientoController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    // Endpoint para obtener la lista completa de tipos de movimiento
    public Response getTiposMovimiento() {
        try {
            // Ejecuta la consulta que devuelve todos los tipos de movimiento
            ResultSet rs = TiposMovimientoDao.getTipos();
            List<TiposMovimiento> lista = new ArrayList<>();

            // Itera sobre el conjunto de resultados y construye objetos TiposMovimiento
            while (rs.next()) {
                TiposMovimiento tm = new TiposMovimiento(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("icono"),
                    rs.getString("color")
                );
                lista.add(tm);
            }
            rs.close(); // Liberar recursos cerrando ResultSet

            // Si se encontraron tipos de movimiento, responde con éxito y lista
            if (!lista.isEmpty()) {
                return ResponseProvider.success(lista, "Tipos de movimiento obtenidos con éxito.", 200);
            } else {
                // Si no hay registros, responde con error 404 indicando ausencia de datos
                return ResponseProvider.error("No hay tipos de movimiento registrados.", 404);
            }
        } catch (SQLException e) {
            // Captura y responde ante errores internos (base de datos, conexión, etc.)
            return ResponseProvider.error("Error interno al obtener los tipos de movimiento", 500);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    // Obtiene un tipo de movimiento específico según su ID
    public Response getTipoMovimiento(@PathParam("id") int id) {
        try {
            ResultSet rs = TiposMovimientoDao.getTipoById(id);
            TiposMovimiento tm = null;

            // Procesa el resultado (debería ser una sola fila si existe)
            while (rs.next()) {
                tm = new TiposMovimiento(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("icono"),
                    rs.getString("color")
                );
            }
            rs.close();

            if (tm == null) {
                // No encontró el tipo de movimiento con el ID dado
                return ResponseProvider.error("El tipo de movimiento no existe.", 404);
            } else {
                // Retorna el tipo de movimiento encontrado
                return ResponseProvider.success(tm, "Tipo de movimiento obtenido con éxito.", 200);
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener el tipo de movimiento", 500);
        }
    }

    @POST
    @Validar(entidad = "TiposMovimiento")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    // Crea un nuevo tipo de movimiento con los datos enviados en el cuerpo JSON
    public Response createTipoMovimiento(TiposMovimiento tipoMovimientoData) {
        try {
            ResultSet rs = TiposMovimientoDao.createTipo(tipoMovimientoData);
            int idGenerado = 0;

            // Obtiene el ID generado al insertar el nuevo tipo en la base de datos
            while (rs.next()) {
                idGenerado = rs.getInt(1);
                tipoMovimientoData.setId(idGenerado);
            }
            rs.close();

            // Validación: si no se generó ID, hubo un error en creación
            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear el tipo de movimiento.", 400);
            } else {
                // Respuesta exitosa con el objeto creado y su ID asignado
                return ResponseProvider.success(tipoMovimientoData, "Tipo de movimiento creado con éxito.", 200);
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al crear el tipo de movimiento.", 500);
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    // Actualiza un tipo de movimiento existente identificado por ID
    public Response updateTipoMovimiento(@PathParam("id") int id, TiposMovimiento tipoMovimientoData) {
        try {
            // Primero valida que el tipo de movimiento exista en la base de datos
            ResultSet rsCheck = TiposMovimientoDao.getTipoById(id);
            TiposMovimiento existente = null;

            while (rsCheck.next()) {
                existente = new TiposMovimiento(
                    rsCheck.getInt("id"),
                    rsCheck.getString("nombre"),
                    rsCheck.getString("icono"),
                    rsCheck.getString("color")
                );
            }
            rsCheck.close();

            if (existente == null) {
                // Si no existe, responde con error 404
                return ResponseProvider.error("Este tipo de movimiento no existe.", 404);
            }

            // Ejecuta la actualización con los nuevos datos
            int filasAfectadas = TiposMovimientoDao.updateTipo(id, tipoMovimientoData);

            if (filasAfectadas != 0) {
                // Actualización exitosa, devuelve el objeto actualizado
                tipoMovimientoData.setId(id);
                return ResponseProvider.success(tipoMovimientoData, "Tipo de movimiento actualizado con éxito.", 200);
            } else {
                // Si no se afectaron filas, indica error en actualización
                return ResponseProvider.error("Error al actualizar el tipo de movimiento.", 400);
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al actualizar el tipo de movimiento.", 500);
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    // Elimina un tipo de movimiento por ID
    public Response deleteTipoMovimiento(@PathParam("id") int id) {
        try {
            int filasAfectadas = TiposMovimientoDao.deleteTipo(id);

            if (filasAfectadas != 0) {
                // Eliminación exitosa
                return ResponseProvider.success(null, "Tipo de movimiento eliminado con éxito.", 200);
            } else {
                // No existe el tipo de movimiento para eliminar
                return ResponseProvider.error("Este tipo de movimiento no existe.", 404);
            }
        } catch (Exception e) {
            // Cualquier error interno genera respuesta 500
            return ResponseProvider.error("Error interno al eliminar el tipo de movimiento.", 500);
        }
    }
}
