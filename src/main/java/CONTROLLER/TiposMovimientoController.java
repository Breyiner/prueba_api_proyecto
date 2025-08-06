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
public class TiposMovimientoController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTiposMovimiento() {
        try {
            ResultSet rs = TiposMovimientoDao.getTipos();
            List<TiposMovimiento> lista = new ArrayList<>();
            while (rs.next()) {
                TiposMovimiento tm = new TiposMovimiento(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("icono"),
                    rs.getString("color")
                );
                lista.add(tm);
            }
            rs.close();

            if (!lista.isEmpty()) {
                return ResponseProvider.success(lista, "Tipos de movimiento obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay tipos de movimiento registrados.", 404);
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los tipos de movimiento", 500);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTipoMovimiento(@PathParam("id") int id) {
        try {
            ResultSet rs = TiposMovimientoDao.getTipoById(id);
            TiposMovimiento tm = null;
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
                return ResponseProvider.error("El tipo de movimiento no existe.", 404);
            } else {
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
    public Response createTipoMovimiento(TiposMovimiento tipoMovimientoData) {
        try {
            ResultSet rs = TiposMovimientoDao.createTipo(tipoMovimientoData);
            int idGenerado = 0;
            while (rs.next()) {
                idGenerado = rs.getInt(1);
                tipoMovimientoData.setId(idGenerado);
            }
            rs.close();

            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear el tipo de movimiento.", 400);
            } else {
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
    public Response updateTipoMovimiento(@PathParam("id") int id, TiposMovimiento tipoMovimientoData) {
        try {
            // Validar existencia
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
                return ResponseProvider.error("Este tipo de movimiento no existe.", 404);
            }

            int filasAfectadas = TiposMovimientoDao.updateTipo(id, tipoMovimientoData);
            if (filasAfectadas != 0) {
                tipoMovimientoData.setId(id);
                return ResponseProvider.success(tipoMovimientoData, "Tipo de movimiento actualizado con éxito.", 200);
            } else {
                return ResponseProvider.error("Error al actualizar el tipo de movimiento.", 400);
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al actualizar el tipo de movimiento.", 500);
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTipoMovimiento(@PathParam("id") int id) {
        try {
            int filasAfectadas = TiposMovimientoDao.deleteTipo(id);
            if (filasAfectadas != 0) {
                return ResponseProvider.success(null, "Tipo de movimiento eliminado con éxito.", 200);
            } else {
                return ResponseProvider.error("Este tipo de movimiento no existe.", 404);
            }
        } catch (Exception e) {
            return ResponseProvider.error("Error interno al eliminar el tipo de movimiento.", 500);
        }
    }
}
