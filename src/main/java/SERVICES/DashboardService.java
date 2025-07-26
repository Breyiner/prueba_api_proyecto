package SERVICES;

import DAO.DashboardDao;
import MODEL.ResumenCardDTO;
import MODEL.ResumenCategoriasDTO;
import MODEL.ResumenMetasDTO;
import PROVIDERS.ResponseProvider;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

public class DashboardService {

    public static Response getResumenMovimientos(int usuario_id, int mes) {
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

    public static Response getResumenMetas(int usuario_id, int mes) {
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

    public static Response getResumenCompleto(int usuario_id, int mes) {
        List<ResumenCardDTO> resumenCompleto = new ArrayList<>();

        try {
            // Obtener resumen de movimientos
            ResultSet rsMovimientos = DashboardDao.getResumenMovimientos(usuario_id, mes);
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
            ResultSet rsMetas = DashboardDao.getResumenMetas(usuario_id, mes);
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

            if (!resumenCompleto.isEmpty()) {
                return ResponseProvider.success(resumenCompleto, "Resumen completo obtenido con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay datos registrados para este mes.", 404);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener el resumen completo.", 500);
        }
    }
    
    public static Response getResumenMetasDetalle(int usuario_id, int mes) {
        try {
            ResultSet rs = DashboardDao.getResumenMetasDetalle(usuario_id, mes);
            List<ResumenMetasDTO> lista = new ArrayList<>();

            while (rs.next()) {
                ResumenMetasDTO dto = new ResumenMetasDTO(
                    rs.getString("icono"),
                    rs.getString("color"),
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

    public static Response getResumenCategoriasDetalle(int usuario_id, int mes, int tipo_movimiento) {
        try {
            ResultSet rs = DashboardDao.getResumenCategoriasDetalle(usuario_id, mes, tipo_movimiento);
            List<ResumenCategoriasDTO> lista = new ArrayList<>();

            while (rs.next()) {
                ResumenCategoriasDTO dto = new ResumenCategoriasDTO(
                    rs.getString("icono"),
                    rs.getString("color"),
                    rs.getString("nombre"),
                    rs.getInt("cantidad"),
                    rs.getBigDecimal("total")
                );
                lista.add(dto);
            }

            return ResponseProvider.success(lista, "Resumen completo obtenido con éxito.", 200);
        } catch (SQLException e) {
            return ResponseProvider.error("Error al obtener resumen detallado de los movimients", 500);
        }

    }
}