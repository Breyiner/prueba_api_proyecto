package SERVICES;

import DAO.MetaDao;
import MODEL.CantidadRegistrosDTO;
import MODEL.Meta;
import MODEL.MetaDetalleDTO;
import MODEL.MetaResumenDTO;
import PROVIDERS.ResponseProvider;
import java.math.BigDecimal;
import java.util.Date;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

public class MetaService {

    public static Response getMetas() {
        List<Meta> metas = new ArrayList<>();

        try {
            ResultSet rs = MetaDao.getMetas();
            while (rs.next()) {
                Meta meta = new Meta(
                    rs.getInt("id"),
                    rs.getInt("usuario_id"),
                    rs.getString("nombre"),
                    rs.getBigDecimal("monto"),
                    rs.getString("descripcion"),
                    rs.getDate("fecha_limite")
                );
                meta.setFecha_creacion(rs.getDate("fecha_creacion"));
                
                meta.setCompletada(rs.getBoolean("completada"));

                metas.add(meta);
            }
            rs.close();

            if (!metas.isEmpty()) {
                return ResponseProvider.success(metas, "Metas obtenidas con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay metas registradas.", 404);
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

            metas = obtenerResumenMetas(usuario_id);
            
            for (MetaResumenDTO meta : metas) {

                BigDecimal monto = meta.getMonto();
                BigDecimal total = meta.getTotal();

                int comparacion = monto.compareTo(total);

                if(comparacion == 0 || comparacion < 0) updateCompletada(meta.getId(), usuario_id, true);
                else updateCompletada(meta.getId(), usuario_id, false);
                
            }
            
            metas = obtenerResumenMetas(usuario_id);


            if (!metas.isEmpty()) {
                return ResponseProvider.success(metas, "Metas obtenidas con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay metas registradas.", 404);
            }

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al obtener las metas.", 500);
        }
    }
    
    
    private static List<MetaResumenDTO> obtenerResumenMetas(int usuario_id) {
        List<MetaResumenDTO> metas = new ArrayList<>();
        
        try {
            
            ResultSet rs = MetaDao.getMetasConTotal(usuario_id);
            while (rs.next()) {
                MetaResumenDTO meta = new MetaResumenDTO(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getBigDecimal("monto"),
                    rs.getDate("fecha_limite"),
                    rs.getDate("fecha_creacion"),
                    rs.getBigDecimal("total"),
                    rs.getBoolean("completada")
                );
                
                LocalDate fecha = LocalDate.now();

                Date fecha_actual = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
                Date fecha_limite = meta.getFecha_limite();
                
                BigDecimal monto = meta.getMonto();
                BigDecimal total = meta.getTotal();

                int comparacion = monto.compareTo(total);

                if(comparacion == 0 || comparacion < 0) meta.setMensaje("¡Lo lograste!, Felicidades");
                else meta.setMensaje("¡Tu puedes lograrlo!, Suerte");
                
                if(fecha_limite != null && fecha_limite.before( fecha_actual) && !meta.isCompletada()) meta.setMensaje("¡No te rindas!, Aún puedes actualizar la fecha.");
                metas.add(meta);
            }
            rs.close();
            
            return metas;
        } catch (SQLException e) {
            throw new Error("Error al obtener las metas");
        }
      
    }
    
    public static Response getMetasCantMovimientos(int id, int usuario_id) {
        MetaDetalleDTO meta = null;

        try {
            ResultSet rs = MetaDao.getMetasCantMovimientos(id, usuario_id);
            while (rs.next()) {
                meta = new MetaDetalleDTO(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getBigDecimal("monto"),
                    rs.getBigDecimal("total"),
                    rs.getDate("fecha_creacion"),
                    rs.getDate("fecha_limite"),
                    rs.getInt("cantidad_aportes")
                );

                String estado = "";
                
                if(rs.getBoolean("completada")) estado = "Completada";
                else estado = "Incompleta";
                
                meta.setEstado(estado);
            }
            rs.close();

            if (meta != null) {
                return ResponseProvider.success(meta, "Meta obtenida con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay meta registrada.", 404);
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
                meta = new Meta(
                    rs.getInt("id"),
                    rs.getInt("usuario_id"),
                    rs.getString("nombre"),
                    rs.getBigDecimal("monto"),
                    rs.getString("descripcion"),
                    rs.getDate("fecha_limite")
                );
                meta.setFecha_creacion(rs.getDate("fecha_creacion"));
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
            Response existente = getMetaById(id);
            if (existente.getStatus() == 404)
                return ResponseProvider.error("La meta no existe.", 404);

            int filasAfectadas = MetaDao.updateMeta(id, usuario_id, metaData);

            if (filasAfectadas != 0) {
                metaData.setId(id);
                return ResponseProvider.success(metaData, "Meta actualizada con éxito.", 200);
            } else {
                return ResponseProvider.error("Error al actualizar la meta.", 400);
            }

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar la meta.", 500);
        }
    }

    public static Response updateCompletada(int id, int usuario_id, boolean completada) {
        try {
            Response existente = getMetaById(id);
            if (existente.getStatus() == 404)
                return ResponseProvider.error("La meta no existe.", 404);

            int filasAfectadas = MetaDao.updateCompletada(id, usuario_id, completada);

            if (filasAfectadas != 0) {
                return ResponseProvider.success(null, "Campo 'completada' actualizado correctamente.", 200);
            } else {
                return ResponseProvider.error("Error al actualizar el campo 'completada'.", 400);
            }

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar el campo 'completada'.", 500);
        }
    }

    public static Response deleteMeta(int id, int usuario_id) {
        try {
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