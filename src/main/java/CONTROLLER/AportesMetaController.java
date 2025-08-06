package CONTROLLER;

import MIDDLEWARES.Validar;
import MODEL.AportesMeta;
import PROVIDERS.ResponseProvider;
import SERVICES.AportesMetaService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/aportes")
public class AportesMetaController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAportes() {
        try {
            return AportesMetaService.getAportes();
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener los aportes", 500);
        }
    }
    
    @GET // Indica que este método responde a solicitudes GET
    @Path("/cantidad")
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public Response getUsuario() {
        
        try {
            return AportesMetaService.getCantidadAportes();
        } catch (Exception e) {
           return ResponseProvider.error("Error al obtener la cantida de aportes", 500);
        }
    }

    @GET
    @Path("/meta/{meta_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAportesByMetaId(@PathParam("meta_id") int meta_id) {
        try {
            return AportesMetaService.getAportesByMetaId(meta_id);
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener los aportes de la meta", 500);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAporteById(@PathParam("id") int id) {
        try {
            return AportesMetaService.getAporteById(id);
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener el aporte", 500);
        }
    }

    @GET
    @Path("/detallados/meta/{meta_id}/usuario/{usuario_id}/mes/{mes}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAportesDetalladosByParametros(
        @PathParam("meta_id") int meta_id,
        @PathParam("usuario_id") int usuario_id,
        @PathParam("mes") int mes
    ) {
        try {
            return AportesMetaService.getAportesDetalladosByParametros(meta_id, usuario_id, mes);
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener los aportes detallados", 500);
        }
    }
    
    @GET
    @Path("/detallados/meta/{meta_id}/usuario/{usuario_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAportesDetalladosByMeta(
        @PathParam("meta_id") int meta_id,
        @PathParam("usuario_id") int usuario_id
    ) {
        try {
            return AportesMetaService.getAportesDetalladosByMeta(meta_id, usuario_id);
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener los aportes detallados", 500);
        }
    }
    
    @GET
    @Path("usuario/{usuario_id}/mes/{mes}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAportesResumidos(
        @PathParam("usuario_id") int usuario_id,
        @PathParam("mes") int mes)
    {
        try {
            return AportesMetaService.getAportesResumidos(usuario_id, mes);
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener los aportes", 500);
        }
    }
    
    @GET
    @Path("resumidos/usuario/{usuario_id}/fecha/{fecha}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAportesResumidosByMeta(
        @PathParam("usuario_id") int usuario_id,
        @PathParam("fecha") String fecha)
    {
        try {
            return AportesMetaService.getAportesByDate(usuario_id, fecha);
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener los aportes", 500);
        }
    }

    @POST
    @Validar(entidad = "AportesMeta")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAporte(AportesMeta aporteData) {
        try {
            return AportesMetaService.createAporte(aporteData);
        } catch (Exception e) {
            return ResponseProvider.error("Error al crear el aporte", 500);
        }
    }

    @PUT
    @Validar(entidad = "AportesMeta")
    @Path("/{id}/meta/{meta_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAporte(
        @PathParam("id") int id,
        @PathParam("meta_id") int meta_id,
        AportesMeta aporteData
    ) {
        try {
            return AportesMetaService.updateAporte(id, meta_id, aporteData);
        } catch (Exception e) {
            return ResponseProvider.error("Error al actualizar el aporte", 500);
        }
    }
    
    @DELETE // Indica que este método responde a solicitudes DELETE
    @Path("soft/{id}")
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public Response softDeleteAporte(@PathParam("id") int id) {
     
        try {
            return AportesMetaService.softDeleteAporte(id);
        } catch (Exception e) {
           return ResponseProvider.error("Error al eliminar el aporte", 500);
        }
    }

    @DELETE
    @Path("/{id}/meta/{meta_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAporte(
        @PathParam("id") int id,
        @PathParam("meta_id") int meta_id
    ) {
        try {
            return AportesMetaService.deleteAporte(id, meta_id);
        } catch (Exception e) {
            return ResponseProvider.error("Error al eliminar el aporte", 500);
        }
    }

    @DELETE
    @Path("/meta/{meta_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAllAportesByMetaId(@PathParam("meta_id") int meta_id) {
        try {
            return AportesMetaService.deleteAllAportesByMetaId(meta_id);
        } catch (Exception e) {
            return ResponseProvider.error("Error al eliminar todos los aportes de la meta", 500);
        }
    }
}