package CONTROLLER;

import MODEL.Rol;
import MIDDLEWARES.Validar;
import PROVIDERS.ResponseProvider;
import SERVICES.RolService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/roles")
public class RolController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoles() {
        try {
            return RolService.getRoles();
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener los roles", 500);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRol(@PathParam("id") int id) {
        try {
            return RolService.getRolById(id);
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener el rol", 500);
        }
    }

    @POST
    @Validar(entidad = "Roles")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRol(Rol rolData) {
        try {
            return RolService.createRol(rolData);
        } catch (Exception e) {
            return ResponseProvider.error("Error al crear el rol", 500);
        }
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRol(@PathParam("id") int id, Rol rolData) {
        try {
            return RolService.updateRol(id, rolData);
        } catch (Exception e) {
            return ResponseProvider.error("Error al actualizar el rol", 500);
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRol(@PathParam("id") int id) {
        try {
            return RolService.deleteRol(id);
        } catch (Exception e) {
            return ResponseProvider.error("Error al eliminar el rol", 500);
        }
    }
}