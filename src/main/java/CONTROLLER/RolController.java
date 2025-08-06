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

@Path("/roles")  // Ruta base para los recursos relacionados con roles
public class RolController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)  // Devuelve JSON
    public static Response getRoles() {
        try {
            ResultSet respuesta = RolDao.getRoles();  // Obtiene todos los roles desde la BD
            List<Rol> roles = new ArrayList<>();
            while (respuesta.next()) {  // Itera sobre el resultado
                roles.add(new Rol(
                    respuesta.getInt("id"),
                    respuesta.getString("nombre")
                ));
            }
            respuesta.close();  // Cierra el ResultSet para liberar recursos
            if (!roles.isEmpty()) {
                return ResponseProvider.success(roles, "Roles obtenidos con éxito.", 200);  // Respuesta exitosa con roles
            } else {
                return ResponseProvider.error("No hay roles registrados.", 404);  // No hay datos, responde con 404
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los roles", 500);  // Error inesperado en BD
        }
    }

    @GET
    @Path("/{id}")  // Obtiene un rol específico por id
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getRol(@PathParam("id") int id) {
        try {
            ResultSet respuesta = RolDao.getRolById(id);  // Consulta rol por id
            Rol rol = null;
            while (respuesta.next()) {
                rol = new Rol(
                    respuesta.getInt("id"),
                    respuesta.getString("nombre")
                );
            }
            respuesta.close();
            if (rol == null) {
                return ResponseProvider.error("El rol no existe.", 404);  // Rol no encontrado
            } else {
                return ResponseProvider.success(rol, "Rol obtenido con éxito.", 200);  // Devuelve el rol
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener el rol", 500);
        }
    }

    @POST
    @Validar(entidad = "Roles")  // Validación personalizada antes de crear
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public static Response createRol(Rol rolData) {
        try {
            ResultSet ultimoRegistro = RolDao.createRol(rolData);  // Inserta rol en BD
            int idGenerado = 0;
            while (ultimoRegistro.next()) {  // Obtiene el id generado
                idGenerado = ultimoRegistro.getInt(1);
                rolData.setId(idGenerado);
            }
            ultimoRegistro.close();
            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear el rol.", 400);  // No se creó
            } else {
                return ResponseProvider.success(rolData, "Rol creado con éxito.", 200);  // Creación exitosa
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al crear el rol.", 500);
        }
    }

    @PUT
    @Path("/{id}")  // Actualiza un rol existente por id
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public static Response updateRol(@PathParam("id") int id, Rol rolData) {
        try {
            Response rolExistente = getRol(id);  // Verifica que el rol existe
            if (rolExistente.getStatus() == 404) return ResponseProvider.error("Este rol no existe.", 404);

            int rowsAffected = RolDao.updateRol(id, rolData);  // Actualiza en BD
            if (rowsAffected != 0) {
                rolData.setId(id);
                return ResponseProvider.success(rolData, "Rol actualizado con éxito.", 200);  // Actualización exitosa
            } else {
                return ResponseProvider.error("Error al actualizar el rol.", 400);  // No se pudo actualizar
            }
        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar el rol.", 500);
        }
    }

    @DELETE
    @Path("/{id}")  // Elimina un rol por id
    @Produces(MediaType.APPLICATION_JSON)
    public static Response deleteRol(@PathParam("id") int id) {
        try {
            int rowsAffected = RolDao.deleteRol(id);  // Elimina de la BD
            if (rowsAffected != 0) {
                return ResponseProvider.success(null, "Rol eliminado con éxito.", 200);  // Eliminación exitosa
            } else {
                return ResponseProvider.error("Este rol no existe.", 404);  // No existe para eliminar
            }
        } catch (Exception e) {
            return ResponseProvider.error("Error interno al eliminar el rol.", 500);
        }
    }
}
