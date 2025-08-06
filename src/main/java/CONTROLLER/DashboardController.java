package CONTROLLER;

import MODELO.DashboardDao;
import MODELO.ResumenCardDTO;
import MODELO.ResumenCategoriasDTO;
import MODELO.ResumenMetasDTO;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Path("/dashboard")
public class DashboardController {

    @GET
    @Path("/movimientos/usuario/{usuario_id}/mes/{mes}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResumenMovimientos(
            @PathParam("usuario_id") int usuario_id,
            @PathParam("mes") int mes) {
        List<ResumenCardDTO> resumen = new ArrayList<>();

        try {
            ResultSet rs = DashboardDao.getResumenMovimientos(usuario_id, mes);
            while (rs.next()) {
                ResumenCardDTO card = new ResumenCardDTO(
                    rs.getString("icono"),
                    rs.getString("color"),
                    rs.getString("nombre"),
                    rs.getBigDecimal("total"),
                    "movimiento"
                );
                resumen.add(card);
            }
            rs.close();

            if (!resumen.isEmpty()) {
                return ResponseProvider.success(resumen, "Resumen de movimientos obtenido con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay movimientos registrados para este mes.", 404);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener el resumen de movimientos.", 500);
        }
    }

    @GET
    @Path("/metas/usuario/{usuario_id}/mes/{mes}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResumenMetas(
            @PathParam("usuario_id") int usuario_id,
            @PathParam("mes") int mes) {
        List<ResumenCardDTO> resumen = new ArrayList<>();

        try {
            ResultSet rs = DashboardDao.getResumenMetas(usuario_id, mes);
            while (rs.next()) {
                ResumenCardDTO card = new ResumenCardDTO(
                    rs.getString("icono"),
                    rs.getString("color"),
                    rs.getString("nombre"),
                    rs.getBigDecimal("total"),
                    "meta"
                );
                resumen.add(card);
            }
            rs.close();

            if (!resumen.isEmpty()) {
                return ResponseProvider.success(resumen, "Resumen de metas obtenido con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay aportes a metas registrados para este mes.", 404);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener el resumen de metas.", 500);
        }
    }

    @GET
    @Path("/completo/usuario/{usuario_id}/mes/{mes}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResumenCompleto(
            @PathParam("usuario_id") int usuarioId,
            @PathParam("mes") int mes) {
        List<ResumenCardDTO> resumenCompleto = new ArrayList<>();

        try {
            // Obtener resumen de movimientos
            ResultSet rsMovimientos = DashboardDao.getResumenMovimientos(usuarioId, mes);
            while (rsMovimientos.next()) {
                ResumenCardDTO card = new ResumenCardDTO(
                    rsMovimientos.getString("icono"),
                    rsMovimientos.getString("color"),
                    rsMovimientos.getString("nombre"),
                    rsMovimientos.getBigDecimal("total"),
                    "movimiento"
                );
                resumenCompleto.add(card);
            }
            rsMovimientos.close();

            // Obtener resumen de metas
            ResultSet rsMetas = DashboardDao.getResumenMetas(usuarioId, mes);
            while (rsMetas.next()) {
                ResumenCardDTO card = new ResumenCardDTO(
                    rsMetas.getString("icono"),
                    rsMetas.getString("color"),
                    rsMetas.getString("nombre"),
                    rsMetas.getBigDecimal("total"),
                    "meta"
                );
                resumenCompleto.add(card);
            }
            rsMetas.close();
            
            BigDecimal balance = resumenCompleto.get(0).getTotal();
            
            for (int i = 1; i < resumenCompleto.size(); i++) {
                
                balance = balance.subtract(resumenCompleto.get(i).getTotal());
            }
            
            ResumenCardDTO card = new ResumenCardDTO(
                    "ri-wallet-3-line",
                    "#3367d6",
                    "Balance",
                    balance,
                    "balance"
            );
            resumenCompleto.add(0,card);

            if (!resumenCompleto.isEmpty()) {
                return ResponseProvider.success(resumenCompleto, "Resumen completo obtenido con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay datos registrados para este mes.", 404);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener el resumen completo.", 500);
        }
    }
    
    @GET
    @Path("/categorias/usuario/{usuario_id}/mes/{mes}/tipo/{tipo_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResumenCategoriasDetalle(
            @PathParam("usuario_id") int usuario_id,
            @PathParam("mes") int mes,
            @PathParam("tipo_id") int tipo_id) {
        try {
            ResultSet rs = DashboardDao.getResumenCategoriasDetalle(usuario_id, mes, tipo_id);
            List<ResumenCategoriasDTO> lista = new ArrayList<>();

            while (rs.next()) {
                ResumenCategoriasDTO dto = new ResumenCategoriasDTO(
                    rs.getInt("id"),
                    rs.getInt("tipo_movimiento_id"),
                    rs.getString("icono"),
                    rs.getString("nombre"),
                    rs.getString("color"),
                    rs.getString("color_bg"),
                    rs.getInt("cantidad"),
                    rs.getBigDecimal("total")
                );
                lista.add(dto);
            }

            return ResponseProvider.success(lista, "Resumen completo obtenido con éxito.", 200);
        } catch (SQLException e) {
            return ResponseProvider.error("Error al obtener resumen detallado de las categorias", 500);
        }
    }
    
    
        
    @GET
    @Path("/metas/detalle/usuario/{usuario_id}/mes/{mes}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResumenMetasDetalle(
            @PathParam("usuario_id") int usuarioId,
            @PathParam("mes") int mes) {
        try {
            ResultSet rs = DashboardDao.getResumenMetasDetalle(usuarioId, mes);
            List<ResumenMetasDTO> lista = new ArrayList<>();

            while (rs.next()) {
                ResumenMetasDTO dto = new ResumenMetasDTO(
                    rs.getInt("id"),
                    rs.getInt("tipo_movimiento_id"),
                    rs.getString("icono"),
                    rs.getString("color"),
                    rs.getString("color_bg"),
                    rs.getString("nombre"),
                    rs.getInt("cantidad"),
                    rs.getBigDecimal("total")
                );
                lista.add(dto);
            }

            return ResponseProvider.success(lista, "Resumen completo obtenido con éxito.", 200);
        } catch (SQLException e) {
            return ResponseProvider.error("Error al obtener resumen detallado de metas", 500);
        }
    }

}