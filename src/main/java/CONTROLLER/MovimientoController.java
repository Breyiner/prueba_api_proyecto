package CONTROLLER;

import MODELO.MovimientoDao;
import MODELO.Movimiento;
import MODELO.CantidadRegistrosDTO;
import MODELO.MovimientoCalendarioDTO;
import MODELO.MovimientoDetalleDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/movimientos")
public class MovimientoController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getMovimientos() {
        List<Movimiento> movimientos = new ArrayList<>();

        try {
            ResultSet rs = MovimientoDao.getMovimientos();
            while (rs.next()) {
                // Validar descripcion null
                String descripcion = rs.getString("descripcion");
                if (descripcion == null) {
                    descripcion = "";
                }
                
                Movimiento movimiento = new Movimiento(
                    rs.getInt("id"),
                    rs.getInt("usuario_id"),
                    rs.getString("nombre"),
                    rs.getBigDecimal("monto"),
                    descripcion,
                    rs.getInt("categoria_id")
                );

                movimiento.setFecha_creacion(rs.getString("fecha_creacion"));
                movimientos.add(movimiento);
            }
            rs.close();

            if (!movimientos.isEmpty()) {
                return ResponseProvider.success(movimientos, "movimientos obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.success(movimientos, "No hay movimientos registrados.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los movimientos", 500);
        }
    }
    
    @GET // Indica que este método responde a solicitudes GET
    @Path("/cantidad")
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public static Response getUsuario() {
        
        CantidadRegistrosDTO cantidad = null;
        
        try {

            ResultSet respuesta = MovimientoDao.getCantidadMovimientos();
            while (respuesta.next()) { // Cambia esto a un while

                cantidad = new CantidadRegistrosDTO(
                    respuesta.getInt("cantidad")
                );
            }
            // Cierra el ResultSet para liberar recursos
            respuesta.close();
            // Devuelve el usuario con estado 200 OK si existe
            if (cantidad == null) {
                return ResponseProvider.error("No se pudo obtener la cantidad de movimientos.", 404);
            } else {
                return ResponseProvider.success(cantidad, "Cantidad de movimientos obtenida con éxito.", 200);
            }
            
        } catch (SQLException e) {
            // Si ocurre un error en la consulta, devuelve un estado 500
            return ResponseProvider.error("Error interno al obtener la cantidad de movimientos", 500);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getMovimientoById(@PathParam("id") int id) {
        Movimiento movimiento = null;

        try {
            ResultSet rs = MovimientoDao.getMovimientoById(id);

            while (rs.next()) {
                // Validar descripcion null
                String descripcion = rs.getString("descripcion");
                if (descripcion == null) {
                    descripcion = "";
                }
                
                movimiento = new Movimiento(
                    rs.getInt("id"),
                    rs.getInt("usuario_id"),
                    rs.getString("nombre"),
                    rs.getBigDecimal("monto"),
                    descripcion,
                    rs.getInt("categoria_id")
                );

                movimiento.setFecha_creacion(rs.getString("fecha_creacion"));
            }
            rs.close();

            if (movimiento == null) {
                return ResponseProvider.error("El movimiento no existe.", 404);
            } else {
                return ResponseProvider.success(movimiento, "Movimiento obtenido con éxito.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener el movimiento", 500);
        }
    }

    @GET
    @Path("{id}/usuario/{usuario_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getMovimientosByUsuario(
            @PathParam("id") int id,
            @PathParam("usuario_id") int usuario_id) {
        List<Movimiento> movimientos = new ArrayList<>();

        try {
            ResultSet rs = MovimientoDao.getMovimientosByUserId(id, usuario_id);

            while (rs.next()) {
                // Validar descripcion null
                String descripcion = rs.getString("descripcion");
                if (descripcion == null) {
                    descripcion = "";
                }
                
                Movimiento movimiento = new Movimiento(
                    rs.getInt("id"),
                    rs.getInt("usuario_id"),
                    rs.getString("nombre"),
                    rs.getBigDecimal("monto"),
                    descripcion,
                    rs.getInt("categoria_id")
                );
                
                movimiento.setTipo_movimiento_id(rs.getInt("tipo_movimiento_id"));
                movimiento.setFecha_creacion(rs.getString("fecha_creacion").substring(0, 10));
                
                movimientos.add(movimiento);
            }
            rs.close();

            if (!movimientos.isEmpty()) {
                return ResponseProvider.success(movimientos, "Movimiento del usuario obtenido con éxito.", 200);
            } else {
                return ResponseProvider.success(movimientos, "No hay movimiento registrado para el usuario.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener el movimiento", 500);
        }
    }

    @GET
    @Path("/categoria/{cat_id}/usuario/{usuario_id}/tipoMovimiento/{tipo_id}/mes/{mes}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getMovimientosPorCategoria(
        @PathParam("cat_id") int categoria_id,
        @PathParam("usuario_id") int usuario_id,
        @PathParam("tipo_id") int tipo_movimiento_id,
        @PathParam("mes") int mes
    ) {
        List<MovimientoDetalleDTO> movimientos = new ArrayList<>();
        
        try {
            
            ResultSet rs = MovimientoDao.getMovimientosByCategoria(categoria_id, usuario_id, tipo_movimiento_id, mes);
            
            while(rs.next()) {
                MovimientoDetalleDTO movimiento = new MovimientoDetalleDTO(
                    rs.getInt("id"),
                    rs.getString("icono"),
                    rs.getString("categoria"),
                    rs.getString("color"),
                    rs.getString("color_bg"),
                    rs.getString("nombre"),
                    rs.getString("fecha_creacion").substring(0, 10),
                    rs.getBigDecimal("monto")
                );
                
                movimientos.add(movimiento);
            }
            
            rs.close();
            
            if (!movimientos.isEmpty()) {
                return ResponseProvider.success(movimientos, "Movimientos obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.success(movimientos, "No hay movimientos registrados.", 200);
            }
            
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los movimientos", 500);
        }
    }
    
    @GET
    @Path("usuario/{usuario_id}/tipoMovimiento/{tipo_movimiento_id}/mes/{mes}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getMovimientosResumidos(
        @PathParam("usuario_id") int usuario_id,
        @PathParam("tipo_movimiento_id") int tipo_movimiento_id,
        @PathParam("mes") int mes)
    {
        List<MovimientoCalendarioDTO> movimientos = new ArrayList<>();
        
        try {
            ResultSet rs = MovimientoDao.getMovimientoResumidos(usuario_id, tipo_movimiento_id, mes);
            
            while(rs.next()) {
                MovimientoCalendarioDTO movimiento = new MovimientoCalendarioDTO(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("color"),
                    rs.getString("fecha_creacion").substring(0, 10)
                );
                movimientos.add(movimiento);
            }
            
            rs.close();
            
            if (!movimientos.isEmpty()) {
                return ResponseProvider.success(movimientos, "Movimientos obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.success(movimientos, "No hay movimientos registrados.", 200);
            }
            
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los movimientos", 500);
        }
    }
    
    @GET
    @Path("usuario/{usuario_id}/tipoMovimiento/{tipo_movimiento_id}/fecha/{fecha}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getMovimientosByDate(
        @PathParam("usuario_id") int usuario_id,
        @PathParam("tipo_movimiento_id") int tipo_movimiento_id,
        @PathParam("fecha") String fecha)
    {
        List<MovimientoDetalleDTO> movimientos = new ArrayList<>();
        
        try {
            ResultSet rs = MovimientoDao.getMovimientosByDate(usuario_id, tipo_movimiento_id, fecha);
            
            while(rs.next()) {
                MovimientoDetalleDTO movimiento = new MovimientoDetalleDTO(
                    rs.getInt("id"),
                    rs.getString("icono"),
                    rs.getString("categoria"),
                    rs.getString("color"),
                    rs.getString("color_bg"),
                    rs.getString("nombre"),
                    rs.getString("fecha_creacion").substring(0, 10),
                    rs.getBigDecimal("monto")
                );
                
                movimientos.add(movimiento);
            }
            
            rs.close();
            
            if (!movimientos.isEmpty()) {
                return ResponseProvider.success(movimientos, "Movimientos obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.success(movimientos, "No hay movimientos registrados.", 200);
            }
            
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los movimientos", 500);
        }
    }

    @POST
    @Validar(entidad = "Movimiento")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public static Response createMovimiento(Movimiento movimientoData) {
        ResultSet rs = null;
        try {
            
            int idGenerado = 0;
            if(movimientoData.getFecha_creacion() == null) {
                rs = MovimientoDao.createMovimiento(movimientoData);
            }

            else  {
                rs = MovimientoDao.createMovimientoDate(movimientoData);
            }

            while (rs.next()) {
                idGenerado = rs.getInt(1);
                movimientoData.setId(idGenerado);
            }
            rs.close();

            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear el movimiento.", 400);
            } else {
                return ResponseProvider.success(null, "Movimiento creado con éxito.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al crear el movimiento.", 500);
        }
    }

    @PUT
    @Validar(entidad = "Movimiento")
    @Path("/{id}/usuario/{usuario_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public static Response updateMovimiento(
        @PathParam("id") int id,
        @PathParam("usuario_id") int usuario_id,
        Movimiento movimientoData
    ) {
        try {
            Response existente = getMovimientoById(id);

            if (existente.getStatus() == 404)
                return ResponseProvider.error("Este movimiento no existe.", 404);

            int filasAfectadas = MovimientoDao.updateMovimiento(id, usuario_id, movimientoData);

            if (filasAfectadas != 0) {
                movimientoData.setId(id);
                return ResponseProvider.success(null, "Movimiento actualizado con éxito.", 200);
            } else {
                return ResponseProvider.error("Error al actualizar el movimiento.", 400);
            }

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar el movimiento.", 500);
        }
    }

    @DELETE // Indica que este método responde a solicitudes DELETE
    @Path("soft/{id}") 
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public static Response softDeleteMovimiento(@PathParam("id") int id) {
     
        try {
            
            int rowsAffected = MovimientoDao.softDeleteMovimiento(id);
            
            if (rowsAffected != 0) 
                return ResponseProvider.success(null, "Movimiento eliminado de forma segura.", 200);
            else 
                return ResponseProvider.error("Este movimiento no existe.", 404);
            
        } catch (Exception e) {
            // Para cualquier error interno, retorna un error 500 con mensaje
            return ResponseProvider.error("Error interno al eliminar el movimiento.", 500);
        }
    }
    
    @DELETE
    @Path("/{id}/usuario/{usuario_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response deleteMovimiento(
        @PathParam("id") int id,
        @PathParam("usuario_id") int usuario_id
    ) {
        try {
            int filasAfectadas = MovimientoDao.deleteMovimiento(id, usuario_id);

            if (filasAfectadas != 0) {
                return ResponseProvider.success(null, "Movimiento eliminado con éxito.", 200);
            } else {
                return ResponseProvider.error("Este movimiento no existe.", 404);
            }

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al eliminar el movimiento.", 500);
        }
    }
}