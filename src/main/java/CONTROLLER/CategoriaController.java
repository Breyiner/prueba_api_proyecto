package CONTROLLER;

import MODELO.CategoriaDao;
import MODELO.Categoria;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Path("/categorias")
public class CategoriaController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategorias() {
        List<Categoria> lista = new ArrayList<>();

        try {
            ResultSet rs = CategoriaDao.getCategorias();
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
    @Path("tipoMovimiento/{tipo_movimiento_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategoriasByTipoMovimiento(@PathParam("tipo_movimiento_id") int tipo_movimiento_id) {
        List<Categoria> lista = new ArrayList<>();

        try {
            ResultSet rs = CategoriaDao.getCategoriasByMovimientoId(tipo_movimiento_id);
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
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategoria(@PathParam("id") int id) {
        Categoria categoria = null;

        try {
            ResultSet rs = CategoriaDao.getCategoriaById(id);

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
                return ResponseProvider.error("La categoría no existe.", 404);
            } else {
                return ResponseProvider.success(categoria, "Categoría obtenida con éxito.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener la categoría", 500);
        }
    }

    @POST
    @Validar(entidad = "Categorias")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCategoria(Categoria categoriaData) {
        try {
            int idGenerado = 0;
            ResultSet rs = CategoriaDao.createCategoria(categoriaData);

            while (rs.next()) {
                idGenerado = rs.getInt(1);
                categoriaData.setId(idGenerado);
            }
            rs.close();

            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear la categoría.", 400);
            } else {
                return ResponseProvider.success(categoriaData, "Categoría creada con éxito.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al crear la categoría.", 500);
        }
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCategoria(@PathParam("id") int id, Categoria categoriaData) {
        try {
            Response existente = getCategoria(id);

            if (existente.getStatus() == 404)
                return ResponseProvider.error("Esta categoría no existe.", 404);

            int filasAfectadas = CategoriaDao.updateCategoria(id, categoriaData);

            if (filasAfectadas != 0) {
                categoriaData.setId(id);
                return ResponseProvider.success(categoriaData, "Categoría actualizada con éxito.", 200);
            } else {
                return ResponseProvider.error("Error al actualizar la categoría.", 400);
            }

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar la categoría.", 500);
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCategoria(@PathParam("id") int id) {
        try {
            int filasAfectadas = CategoriaDao.deleteCategoria(id);

            if (filasAfectadas != 0) {
                return ResponseProvider.success(null, "Categoría eliminada con éxito.", 200);
            } else {
                return ResponseProvider.error("Esta categoría no existe.", 404);
            }

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al eliminar la categoría.", 500);
        }
    }
}