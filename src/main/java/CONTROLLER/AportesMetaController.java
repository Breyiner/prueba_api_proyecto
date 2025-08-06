package CONTROLLER;

import MODELO.AportesMetaDao;
import MODELO.AporteMetaCalendarioDTO;
import MODELO.AportesCalendarioDTO;
import MODELO.AportesMeta;
import MODELO.AportesMetaDetalladoDTO;
import MODELO.CantidadRegistrosDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/aportes")
public class AportesMetaController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getAportes() {
        List<AportesMeta> aportes = new ArrayList<>();

        try {
            ResultSet rs = AportesMetaDao.getAportes();
            while (rs.next()) {
                // Validar descripcion null
                String descripcion = rs.getString("descripcion");
                if (descripcion == null) {
                    descripcion = "";
                }
                
                AportesMeta aporte = new AportesMeta(
                    rs.getInt("id"),
                    rs.getInt("meta_id"),
                    rs.getBigDecimal("monto"),
                    descripcion,
                    rs.getString("fecha_creacion").substring(0, 10)
                );
                aportes.add(aporte);
            }
            rs.close();

            if (!aportes.isEmpty()) {
                return ResponseProvider.success(aportes, "Aportes obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay aportes registrados.", 404);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los aportes.", 500);
        }
    }
    
    @GET // Indica que este método responde a solicitudes GET
    @Path("/cantidad")
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public static Response getUsuario() {
        
        CantidadRegistrosDTO cantidad = null;
        
        try {

            ResultSet respuesta = AportesMetaDao.getCantidadAportes();
            while (respuesta.next()) { // Cambia esto a un while

                cantidad = new CantidadRegistrosDTO(
                    respuesta.getInt("cantidad")
                );
            }
            // Cierra el ResultSet para liberar recursos
            respuesta.close();
            // Devuelve el usuario con estado 200 OK si existe
            if (cantidad == null) {
                return ResponseProvider.error("No se pudo obtener la cantidad de aportes.", 404);
            } else {
                return ResponseProvider.success(cantidad, "Cantidad de aportes obtenida con éxito.", 200);
            }
            
        } catch (SQLException e) {
            // Si ocurre un error en la consulta, devuelve un estado 500
            return ResponseProvider.error("Error interno al obtener la cantidad de aportes", 500);
        }
    }

    @GET
    @Path("/meta/{meta_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getAportesByMetaId(@PathParam("meta_id") int meta_id) {
        List<AportesMeta> aportes = new ArrayList<>();

        try {
            ResultSet rs = AportesMetaDao.getAportesByMetaId(meta_id);
            while (rs.next()) {
                // Validar descripcion null
                String descripcion = rs.getString("descripcion");
                if (descripcion == null) {
                    descripcion = "";
                }
                
                AportesMeta aporte = new AportesMeta(
                    rs.getInt("id"),
                    rs.getInt("meta_id"),
                    rs.getBigDecimal("monto"),
                    descripcion,
                    rs.getString("fecha_creacion").substring(0, 10)
                );
                aportes.add(aporte);
            }
            rs.close();

            if (!aportes.isEmpty()) {
                return ResponseProvider.success(aportes, "Aportes de la meta obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay aportes registrados para esta meta.", 404);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los aportes de la meta.", 500);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getAporteById(@PathParam("id") int id) {
        AportesMeta aporte = null;

        try {
            ResultSet rs = AportesMetaDao.getAporteById(id);
            while (rs.next()) {
                // Validar descripcion null
                String descripcion = rs.getString("descripcion");
                if (descripcion == null) {
                    descripcion = "";
                }
                
                aporte = new AportesMeta(
                    rs.getInt("id"),
                    rs.getInt("meta_id"),
                    rs.getBigDecimal("monto"),
                    descripcion,
                    rs.getString("fecha_creacion").substring(0, 10)
                );
            }
            rs.close();

            if (aporte == null) {
                return ResponseProvider.error("El aporte no existe.", 404);
            } else {
                return ResponseProvider.success(aporte, "Aporte obtenido con éxito.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener el aporte.", 500);
        }
    }

    @GET
    @Path("/detallados/meta/{meta_id}/usuario/{usuario_id}/mes/{mes}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getAportesDetalladosByParametros(
        @PathParam("meta_id") int meta_id,
        @PathParam("usuario_id") int usuario_id,
        @PathParam("mes") int mes
    ) {
       List<AportesMetaDetalladoDTO> aportes = new ArrayList<>();

        try {
            ResultSet rs = AportesMetaDao.getAportesDetalladosByParametros(meta_id, usuario_id, mes);
            while (rs.next()) {
                AportesMetaDetalladoDTO aporte = new AportesMetaDetalladoDTO(
                    rs.getInt("id"),
                    rs.getInt("meta_id"),
                    rs.getString("icono"),
                    rs.getString("color"),
                    rs.getString("color_bg"),
                    rs.getString("nombre"),
                    rs.getString("fecha_creacion").substring(0, 10),
                    rs.getBigDecimal("monto")
                );
                aportes.add(aporte);
            }
            rs.close();

            if (!aportes.isEmpty()) {
                return ResponseProvider.success(aportes, "Aportes detallados obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay aportes registrados con los parámetros especificados.", 404);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los aportes detallados.", 500);
        }
    }
    
    @GET
    @Path("/detallados/meta/{meta_id}/usuario/{usuario_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getAportesDetalladosByMeta(
        @PathParam("meta_id") int meta_id,
        @PathParam("usuario_id") int usuario_id
    ) {
        List<AportesMetaDetalladoDTO> aportes = new ArrayList<>();

        try {
            ResultSet rs = AportesMetaDao.getAportesDetalladosByMeta(meta_id, usuario_id);
            while (rs.next()) {
                AportesMetaDetalladoDTO aporte = new AportesMetaDetalladoDTO(
                    rs.getInt("id"),
                    rs.getInt("meta_id"),
                    rs.getString("icono"),
                    rs.getString("color"),
                    rs.getString("color_bg"),
                    rs.getString("nombre"),
                    rs.getString("fecha_creacion").substring(0, 10),
                    rs.getBigDecimal("monto")
                );
                aportes.add(aporte);
            }
            rs.close();

            if (!aportes.isEmpty()) {
                return ResponseProvider.success(aportes, "Aportes detallados obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay aportes registrados con los parámetros especificados.", 404);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los aportes detallados.", 500);
        }
    }
    
    @GET
    @Path("usuario/{usuario_id}/mes/{mes}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getAportesResumidos(
        @PathParam("usuario_id") int usuario_id,
        @PathParam("mes") int mes)
    {
        List<AporteMetaCalendarioDTO> movimientos = new ArrayList<>();
        
        try {
            ResultSet rs = AportesMetaDao.getAportesResumidos(usuario_id, mes);
            
            while(rs.next()) {
                AporteMetaCalendarioDTO movimiento = new AporteMetaCalendarioDTO(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("color"),
                    rs.getString("fecha_creacion").substring(0, 10)
                );
                movimientos.add(movimiento);
            }
            
            rs.close();
            
            if (!movimientos.isEmpty()) {
                return ResponseProvider.success(movimientos, "Aportes obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.success(movimientos, "No hay aportes registrados.", 200);
            }
            
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los aportes", 500);
        }
    }
    
    @GET
    @Path("resumidos/usuario/{usuario_id}/fecha/{fecha}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getAportesResumidosByMeta(
        @PathParam("usuario_id") int usuario_id,
        @PathParam("fecha") String fecha)
    {
        List<AportesCalendarioDTO> aportes = new ArrayList<>();
        
        try {
            ResultSet rs = AportesMetaDao.getAportesByDate(usuario_id, fecha);
            
            while(rs.next()) {
                AportesCalendarioDTO movimiento = new AportesCalendarioDTO(
                    rs.getInt("id"),
                    rs.getString("icono"),
                    "Aporte",
                    rs.getString("color"),
                    rs.getString("color_bg"),
                    rs.getString("nombre"),
                    rs.getString("fecha_creacion").substring(0, 10),
                    rs.getBigDecimal("monto")
                );
                
                aportes.add(movimiento);
            }
            
            rs.close();
            
            if (!aportes.isEmpty()) {
                return ResponseProvider.success(aportes, "Aportes obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.success(aportes, "No hay aportes registrados.", 200);
            }
            
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los aportes", 500);
        }
    }

    @POST
    @Validar(entidad = "AportesMeta")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public static Response createAporte(AportesMeta aporteData) {
        try {
            int idGenerado = 0;
            
            if(aporteData.getDescripcion() == null) aporteData.setDescripcion("");
            
            ResultSet rs = AportesMetaDao.createAporte(aporteData);

            while (rs.next()) {
                idGenerado = rs.getInt(1);
                aporteData.setId(idGenerado);
            }
            rs.close();

            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear el aporte.", 400);
            } else {
                
                MetaController.gestionarMetaCompletada(aporteData.getMeta_id());
                
                return ResponseProvider.success(aporteData, "Aporte creado con éxito.", 200);
            }

        } catch (SQLException e) {
            System.out.println(e);
            return ResponseProvider.error("Error interno al crear el aporte.", 500);
        }
    }

    @PUT
    @Validar(entidad = "AportesMeta")
    @Path("/{id}/meta/{meta_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public static Response updateAporte(
        @PathParam("id") int id,
        @PathParam("meta_id") int meta_id,
        AportesMeta aporteData
    ) {
        try {
            Response existente = getAporteById(id);
            if (existente.getStatus() == 404)
                return ResponseProvider.error("El aporte no existe.", 404);

            if(aporteData.getDescripcion() == null) aporteData.setDescripcion("");
            
            int filasAfectadas = AportesMetaDao.updateAporte(id, meta_id, aporteData);

            if (filasAfectadas != 0) {
                aporteData.setId(id);
                aporteData.setMeta_id(meta_id);
                MetaController.gestionarMetaCompletada(meta_id);
                return ResponseProvider.success(aporteData, "Aporte actualizado con éxito.", 200);
            } else {
                
                return ResponseProvider.error("El aporte no pertenece a la meta especificada o no existe.", 400);
            }

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar el aporte.", 500);
        }
    }
    
    @DELETE // Indica que este método responde a solicitudes DELETE
    @Path("soft/{id}")
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public static Response softDeleteAporte(@PathParam("id") int id) {
     
        try {
            
            int rowsAffected = AportesMetaDao.softDeleteAportes(id);
            
            if (rowsAffected != 0) 
                return ResponseProvider.success(null, "Aporte eliminado de forma segura.", 200);
            else 
                return ResponseProvider.error("Este aporte no existe.", 404);
            
        } catch (Exception e) {
            // Para cualquier error interno, retorna un error 500 con mensaje
            return ResponseProvider.error("Error interno al eliminar el aporte.", 500);
        }
    }

    @DELETE
    @Path("/{id}/meta/{meta_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response deleteAporte(
        @PathParam("id") int id,
        @PathParam("meta_id") int meta_id
    ) {
        try {
            Response existente = getAporteById(id);
            if (existente.getStatus() == 404)
                return ResponseProvider.error("El aporte no existe.", 404);

            int filasAfectadas = AportesMetaDao.deleteAporte(id, meta_id);

            if (filasAfectadas != 0) {
                return ResponseProvider.success(null, "Aporte eliminado con éxito.", 200);
            } else {
                return ResponseProvider.error("El aporte no pertenece a la meta especificada.", 400);
            }

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al eliminar el aporte.", 500);
        }
    }

    @DELETE
    @Path("/meta/{meta_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response deleteAllAportesByMetaId(@PathParam("meta_id") int meta_id) {
        try {
            int filasAfectadas = AportesMetaDao.deleteAllAportesByMetaId(meta_id);

            if (filasAfectadas != 0) {
                return ResponseProvider.success(null, "Todos los aportes de la meta eliminados con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay aportes para eliminar en esta meta.", 404);
            }

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al eliminar los aportes de la meta.", 500);
        }
    }
}