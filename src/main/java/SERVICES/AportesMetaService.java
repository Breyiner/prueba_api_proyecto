package SERVICES;

import DAO.AportesMetaDao;
import MODEL.AporteMetaCalendarioDTO;
import MODEL.AportesCalendarioDTO;
import MODEL.AportesMeta;
import MODEL.AportesMetaDetalladoDTO;
import MODEL.CantidadRegistrosDTO;
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
    
    public static Response getCantidadAportes() {
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

    public static Response getAportesByMetaId(int meta_id) {
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

    public static Response getAporteById(int id) {
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

    public static Response getAportesDetalladosByParametros(int meta_id, int usuario_id, int mes) {
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
    
    public static Response getAportesDetalladosByMeta(int meta_id, int usuario_id) {
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
    
    public static Response getAportesResumidos(int usuario_id, int mes) {
        
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
    
    public static Response getAportesByDate(int usuario_id, String fecha) {
        
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
                
                MetaService.gestionarMetaCompletada(aporteData.getMeta_id());
                
                return ResponseProvider.success(aporteData, "Aporte creado con éxito.", 200);
            }

        } catch (SQLException e) {
            System.out.println(e);
            return ResponseProvider.error("Error interno al crear el aporte.", 500);
        }
    }

    public static Response updateAporte(int id, int meta_id, AportesMeta aporteData) {
        try {
            Response existente = getAporteById(id);
            if (existente.getStatus() == 404)
                return ResponseProvider.error("El aporte no existe.", 404);

            if(aporteData.getDescripcion() == null) aporteData.setDescripcion("");
            
            int filasAfectadas = AportesMetaDao.updateAporte(id, meta_id, aporteData);

            if (filasAfectadas != 0) {
                aporteData.setId(id);
                aporteData.setMeta_id(meta_id);
                MetaService.gestionarMetaCompletada(meta_id);
                return ResponseProvider.success(aporteData, "Aporte actualizado con éxito.", 200);
            } else {
                
                return ResponseProvider.error("El aporte no pertenece a la meta especificada o no existe.", 400);
            }

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar el aporte.", 500);
        }
    }
    
    public static Response softDeleteAporte(int id) {
        
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