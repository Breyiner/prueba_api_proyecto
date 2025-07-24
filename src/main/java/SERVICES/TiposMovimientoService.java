package SERVICES;

import DAO.TiposMovimientoDao;
import MODEL.TiposMovimiento;
import PROVIDERS.ResponseProvider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

public class TiposMovimientoService {

    public static Response getTipos() {
        List<TiposMovimiento> lista = new ArrayList<>();

        try {
            ResultSet rs = TiposMovimientoDao.getTipos();
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

    public static Response getTipoById(int id) {
        TiposMovimiento tm = null;

        try {
            ResultSet rs = TiposMovimientoDao.getTipoById(id);

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

    public static Response createTipo(TiposMovimiento tmData) {
        try {
            int idGenerado = 0;
            ResultSet rs = TiposMovimientoDao.createTipo(tmData);

            while (rs.next()) {
                idGenerado = rs.getInt(1);
                tmData.setId(idGenerado);
            }
            rs.close();

            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear el tipo de movimiento.", 400);
            } else {
                return ResponseProvider.success(tmData, "Tipo de movimiento creado con éxito.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al crear el tipo de movimiento.", 500);
        }
    }

    public static Response updateTipo(int id, TiposMovimiento tmData) {
        try {
            Response existente = getTipoById(id);

            if (existente.getStatus() == 404)
                return ResponseProvider.error("Este tipo de movimiento no existe.", 404);

            int filasAfectadas = TiposMovimientoDao.updateTipo(id, tmData);

            if (filasAfectadas != 0) {
                tmData.setId(id);
                return ResponseProvider.success(tmData, "Tipo de movimiento actualizado con éxito.", 200);
            } else {
                return ResponseProvider.error("Error al actualizar el tipo de movimiento.", 400);
            }

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar el tipo de movimiento.", 500);
        }
    }

    public static Response deleteTipo(int id) {
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