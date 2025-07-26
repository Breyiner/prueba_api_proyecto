package SERVICES;

import DAO.AportesMetaDao;
import MODEL.AportesMeta;
import PROVIDERS.ResponseProvider;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

public class AportesMetaService {

    public static Response getAportes() {
        List<AportesMeta> aportes = new ArrayList<>();

        try {
            ResultSet rs = AportesMetaDao.getAportes();
            while (rs.next()) {
                AportesMeta aporte = new AportesMeta(
                    rs.getInt("id"),
                    rs.getInt("meta_id"),
                    rs.getBigDecimal("monto"),
                    rs.getString("descripcion"),
                    rs.getDate("fecha_creacion")
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

    public static Response getAportesByMetaId(int meta_id) {
        List<AportesMeta> aportes = new ArrayList<>();

        try {
            ResultSet rs = AportesMetaDao.getAportesByMetaId(meta_id);
            while (rs.next()) {
                AportesMeta aporte = new AportesMeta(
                    rs.getInt("id"),
                    rs.getInt("meta_id"),
                    rs.getBigDecimal("monto"),
                    rs.getString("descripcion"),
                    rs.getDate("fecha_creacion")
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

    public static Response getAporteById(int id) {
        AportesMeta aporte = null;

        try {
            ResultSet rs = AportesMetaDao.getAporteById(id);
            while (rs.next()) {
                aporte = new AportesMeta(
                    rs.getInt("id"),
                    rs.getInt("meta_id"),
                    rs.getBigDecimal("monto"),
                    rs.getString("descripcion"),
                    rs.getDate("fecha_creacion")
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

    public static Response createAporte(AportesMeta aporteData) {
        try {
            int idGenerado = 0;
            ResultSet rs = AportesMetaDao.createAporte(aporteData);

            while (rs.next()) {
                idGenerado = rs.getInt(1);
                aporteData.setId(idGenerado);
            }
            rs.close();

            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear el aporte.", 400);
            } else {
                return ResponseProvider.success(aporteData, "Aporte creado con éxito.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al crear el aporte.", 500);
        }
    }

    public static Response updateAporte(int id, int meta_id, AportesMeta aporteData) {
        try {
            Response existente = getAporteById(id);
            if (existente.getStatus() == 404)
                return ResponseProvider.error("El aporte no existe.", 404);

            int filasAfectadas = AportesMetaDao.updateAporte(id, meta_id, aporteData);

            if (filasAfectadas != 0) {
                aporteData.setId(id);
                aporteData.setMeta_id(meta_id);
                return ResponseProvider.success(aporteData, "Aporte actualizado con éxito.", 200);
            } else {
                return ResponseProvider.error("El aporte no pertenece a la meta especificada o no existe.", 400);
            }

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar el aporte.", 500);
        }
    }

    public static Response deleteAporte(int id, int meta_id) {
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

    public static Response deleteAllAportesByMetaId(int meta_id) {
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