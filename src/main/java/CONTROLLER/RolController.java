package CONTROLLER;

import MODELO.RolDao;
import MODELO.Rol;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Path("/roles")
public class RolController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoles() {
        try {
            ResultSet respuesta = RolDao.getRoles();
            List<Rol> roles = new ArrayList<>();
            while (respuesta.next()) {
                roles.add(new Rol(
                    respuesta.getInt("id"),
                    respuesta.getString("nombre")
                ));
            }
            respuesta.close();
            if (!roles.isEmpty()) {
                return ResponseProvider.success(roles, "Roles obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay roles registrados.", 404);
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los roles", 500);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRol(@PathParam("id") int id) {
        try {
            ResultSet respuesta = RolDao.getRolById(id);
            Rol rol = null;
            while (respuesta.next()) {
                rol = new Rol(
                    respuesta.getInt("id"),
                    respuesta.getString("nombre")
                );
            }
            respuesta.close();
            if (rol == null) {
                return ResponseProvider.error("El rol no existe.", 404);
            } else {
                return ResponseProvider.success(rol, "Rol obtenido con éxito.", 200);
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener el rol", 500);
        }
    }

    @POST
    @Validar(entidad = "Roles")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRol(Rol rolData) {
        try {
            ResultSet ultimoRegistro = RolDao.createRol(rolData);
            int idGenerado = 0;
            while (ultimoRegistro.next()) {
                idGenerado = ultimoRegistro.getInt(1);
                rolData.setId(idGenerado);
            }
            ultimoRegistro.close();
            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear el rol.", 400);
            } else {
                return ResponseProvider.success(rolData, "Rol creado con éxito.", 200);
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al crear el rol.", 500);
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRol(@PathParam("id") int id, Rol rolData) {
        try {
            Response rolExistente = getRol(id);
            if (rolExistente.getStatus() == 404) return ResponseProvider.error("Este rol no existe.", 404);

            int rowsAffected = RolDao.updateRol(id, rolData);
            if (rowsAffected != 0) {
                rolData.setId(id);
                return ResponseProvider.success(rolData, "Rol actualizado con éxito.", 200);
            } else {
                return ResponseProvider.error("Error al actualizar el rol.", 400);
            }
        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar el rol.", 500);
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRol(@PathParam("id") int id) {
        try {
            int rowsAffected = RolDao.deleteRol(id);
            if (rowsAffected != 0) {
                return ResponseProvider.success(null, "Rol eliminado con éxito.", 200);
            } else {
                return ResponseProvider.error("Este rol no existe.", 404);
            }
        } catch (Exception e) {
            return ResponseProvider.error("Error interno al eliminar el rol.", 500);
        }
    }
}
