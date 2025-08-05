package SERVICES;

import DAO.MetaDao;
import MODEL.CantidadRegistrosDTO;
import MODEL.Meta;
import MODEL.MetaDetalleDTO;
import MODEL.MetaResumenDTO;
import PROVIDERS.ResponseProvider;
import java.math.BigDecimal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

public class MetaService {

    public static Response getMetas() {
        List<Meta> metas = new ArrayList<>();

        try {
            ResultSet rs = MetaDao.getMetas();
            while (rs.next()) {
                // Validar descripcion null
                String descripcion = rs.getString("descripcion");
                if (descripcion == null) {
                    descripcion = "";
                }
                
                Meta meta = new Meta(
                    rs.getInt("id"),
                    rs.getInt("usuario_id"),
                    rs.getString("nombre"),
                    rs.getBigDecimal("monto"),
                    descripcion,
                    rs.getString("fecha_limite")
                );
                meta.setFecha_creacion(rs.getString("fecha_creacion").substring(0, 10));
                
                meta.setCompletada(rs.getBoolean("completada"));

                metas.add(meta);
            }
            rs.close();

            if (!metas.isEmpty()) {
                return ResponseProvider.success(metas, "Metas obtenidas con éxito.", 200);
            } else {
                return ResponseProvider.success(metas, "No hay metas registradas.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener las metas.", 500);
        }
    }
    
    public static Response getCantidadMetas() {
        CantidadRegistrosDTO cantidad = null;
        
        try {

            ResultSet respuesta = MetaDao.getCantidadMetas();
            while (respuesta.next()) { // Cambia esto a un while

                cantidad = new CantidadRegistrosDTO(
                    respuesta.getInt("cantidad")
                );
            }
            // Cierra el ResultSet para liberar recursos
            respuesta.close();
            // Devuelve el usuario con estado 200 OK si existe
            if (cantidad == null) {
                return ResponseProvider.error("No se pudo obtener la cantidad de metas.", 404);
            } else {
                return ResponseProvider.success(cantidad, "Cantidad de metas obtenida con éxito.", 200);
            }
            
        } catch (SQLException e) {
            // Si ocurre un error en la consulta, devuelve un estado 500
            return ResponseProvider.error("Error interno al obtener la cantidad de metas", 500);
        }
    }
    
    public static Response getMetasConTotal(int usuario_id) {
        List<MetaResumenDTO> metas = new ArrayList<>();

        try {

            ResultSet rs = MetaDao.getMetasConTotal(usuario_id);
            while (rs.next()) {
                MetaResumenDTO meta = new MetaResumenDTO(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getBigDecimal("monto"),
                    rs.getString("fecha_limite"),
                    rs.getString("fecha_creacion").substring(0, 10),
                    rs.getBigDecimal("total"),
                    rs.getBoolean("completada")
                );
                
                LocalDate fecha_actual = LocalDate.now();

                LocalDate fecha_limite = null;
                if (meta.getFecha_limite() != null) {
                    fecha_limite = LocalDate.parse(meta.getFecha_limite());
                }

                
                BigDecimal monto = meta.getMonto();
                BigDecimal total = meta.getTotal();

                int comparacion = monto.compareTo(total);

                if(comparacion == 0 || comparacion < 0) meta.setMensaje("¡Lo lograste!, Felicidades");
                else if(fecha_limite != null && fecha_limite.isBefore( fecha_actual) && !meta.isCompletada()) 
                    meta.setMensaje("¡No te rindas!, Aún puedes actualizar la fecha.");
                else meta.setMensaje("¡Tu puedes lograrlo!, Suerte");
                
                
                metas.add(meta);
            }
            rs.close();


            if (!metas.isEmpty()) {
                return ResponseProvider.success(metas, "Metas obtenidas con éxito.", 200);
            } else {
                return ResponseProvider.success(metas, "No hay metas registradas.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener las metas.", 500);
        }
    }
    
    public static void gestionarMetaCompletada(int meta_id) {
            
        try {
            
            ResultSet rs = MetaDao.getMetaTotales(meta_id);
            
            while(rs.next()) {
                System.out.println(rs.getBigDecimal("monto"));
                BigDecimal monto = rs.getBigDecimal("monto");
                BigDecimal total = rs.getBigDecimal("total");

                int comparacion = monto.compareTo(total);

                if(comparacion == 0 || comparacion < 0) updateCompletada(meta_id, true);
                else updateCompletada(meta_id, false);
            }
            
        } catch (SQLException e) {
            throw new Error("Error al verificar el estado de la meta");
        }
    }
    
    public static Response getMetasCantMovimientos(int id, int usuario_id) {
        MetaDetalleDTO meta = null;

        try {
            ResultSet rs = MetaDao.getMetasCantMovimientos(id, usuario_id);
            while (rs.next()) {
                // Validar descripcion null
                String descripcion = rs.getString("descripcion");
                if (descripcion == null) {
                    descripcion = "";
                }
                
                meta = new MetaDetalleDTO(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    descripcion,
                    rs.getBigDecimal("monto"),
                    rs.getBigDecimal("total"),
                    rs.getString("fecha_creacion").substring(0, 10),
                    rs.getString("fecha_limite"),
                    rs.getInt("cantidad_aportes")
                );

                String estado = null;
                
                if(rs.getBoolean("completada")) estado = "Completada";
                else estado = "Incompleta";
                
                meta.setEstado(estado);
            }
            rs.close();

            if (meta != null) {
                return ResponseProvider.success(meta, "Meta obtenida con éxito.", 200);
            } else {
                return ResponseProvider.error("La meta no existe.", 404);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener la meta.", 500);
        }
    }

    public static Response getMetaById(int id) {
        Meta meta = null;

        try {
            ResultSet rs = MetaDao.getMetaById(id);
            while (rs.next()) {
                // Validar descripcion null
                String descripcion = rs.getString("descripcion");
                if (descripcion == null) {
                    descripcion = "";
                }
                
                meta = new Meta(
                    rs.getInt("id"),
                    rs.getInt("usuario_id"),
                    rs.getString("nombre"),
                    rs.getBigDecimal("monto"),
                    descripcion,
                    rs.getString("fecha_limite")
                );
                meta.setFecha_creacion(rs.getString("fecha_creacion").substring(0, 10));
                meta.setCompletada(rs.getBoolean("completada"));
            }
            rs.close();

            if (meta == null) {
                return ResponseProvider.error("La meta no existe.", 404);
            } else {
                return ResponseProvider.success(meta, "Meta obtenida con éxito.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener la meta.", 500);
        }
    }

    public static Response createMeta(Meta metaData) {
        try {
            
            if(metaData.getDescripcion() == null) metaData.setDescripcion("");
            int idGenerado = 0;
            ResultSet rs = MetaDao.createMeta(metaData);

            while (rs.next()) {
                idGenerado = rs.getInt(1);
                metaData.setId(idGenerado);
            }
            rs.close();

            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear la meta.", 400);
            } else {
                return ResponseProvider.success(metaData, "Meta creada con éxito.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al crear la meta.", 500);
        }
    }

    public static Response updateMeta(int id, int usuario_id, Meta metaData) {
        try {
            if(metaData.getDescripcion() == null) metaData.setDescripcion("");
            
            Response existente = getMetaById(id);
            if (existente.getStatus() == 404)
                return ResponseProvider.error("La meta no existe.", 404);

            int filasAfectadas = MetaDao.updateMeta(id, usuario_id, metaData);

            if (filasAfectadas != 0) {
                metaData.setId(id);
                return ResponseProvider.success(metaData, "Meta actualizada con éxito.", 200);
            } else {
                gestionarMetaCompletada(id);
                return ResponseProvider.error("Error al actualizar la meta.", 400);
            }

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar la meta.", 500);
        }
    }

    public static Response updateCompletada(int id, boolean completada) {
        try {
            Response existente = getMetaById(id);
            if (existente.getStatus() == 404)
                return ResponseProvider.error("La meta no existe.", 404);

            int filasAfectadas = MetaDao.updateCompletada(id, completada);

            if (filasAfectadas != 0) {
                return ResponseProvider.success(null, "Campo 'completada' actualizado correctamente.", 200);
            } else {
                return ResponseProvider.error("Error al actualizar el campo 'completada'.", 400);
            }

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar el campo 'completada'.", 500);
        }
    }
    
    public static Response softDeleteMeta(int id) {
        
        try {
            
            int rowsAffected = MetaDao.softDeleteMeta(id);
            
            if (rowsAffected != 0) 
                return ResponseProvider.success(null, "Meta eliminada de forma segura.", 200);
            else 
                return ResponseProvider.error("Esta meta no existe.", 404);
            
        } catch (Exception e) {
            // Para cualquier error interno, retorna un error 500 con mensaje
            return ResponseProvider.error("Error interno al eliminar la meta.", 500);
        }
        
    }

    public static Response deleteMeta(int id, int usuario_id) {
        try {
            
            Response hasAportes = AportesMetaService.getAportesByMetaId(id);
            
            if (hasAportes.getStatus() == 200) return ResponseProvider.error("La meta tiene aportes registrados.", 409);
            
            int filasAfectadas = MetaDao.deleteMeta(id, usuario_id);

            if (filasAfectadas != 0) {
                return ResponseProvider.success(null, "Meta eliminada con éxito.", 200);
            } else {
                return ResponseProvider.error("La meta no existe o no pertenece al usuario.", 404);
            }

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al eliminar la meta.", 500);
        }
    }
}