package SERVICES;

import DAO.MovimientoDao;
import MODEL.CantidadRegistrosDTO;
import MODEL.Movimiento;
import MODEL.MovimientoDetalleDTO;
import PROVIDERS.ResponseProvider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

public class MovimientoService {

    public static Response getMovimiento() {
        List<Movimiento> movimientos = new ArrayList<>();

        try {
            ResultSet rs = MovimientoDao.getMovimientos();
            while (rs.next()) {
                Movimiento movimiento = new Movimiento(
                    rs.getInt("id"),
                    rs.getInt("usuario_id"),
                    rs.getString("nombre"),
                    rs.getBigDecimal("monto"),
                    rs.getString("descripcion"),
                    rs.getInt("categoria_id")
                );

                movimiento.setFecha_creacion(rs.getDate("fecha_creacion"));
                movimientos.add(movimiento);
            }
            rs.close();

            if (!movimientos.isEmpty()) {
                return ResponseProvider.success(movimientos, "movimientos obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay movimientos registrados.", 404);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los movimientos", 500);
        }
    }
    
    public static Response getCantidadMovimientos() {
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

    public static Response getMovimientoById(int id) {
        Movimiento movimiento = null;

        try {
            ResultSet rs = MovimientoDao.getMovimientoById(id);

            while (rs.next()) {
                movimiento = new Movimiento(
                    rs.getInt("id"),
                    rs.getInt("usuario_id"),
                    rs.getString("nombre"),
                    rs.getBigDecimal("monto"),
                    rs.getString("descripcion"),
                    rs.getInt("categoria_id")
                );

                movimiento.setFecha_creacion(rs.getDate("fecha_creacion"));
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
    
    public static Response getMovimientosByUser(int id, int usuario_id) {
        List<Movimiento> movimientos = new ArrayList<>();

        try {
            ResultSet rs = MovimientoDao.getMovimientosByUserId(id, usuario_id);

            while (rs.next()) {
                Movimiento movimiento = new Movimiento(
                    rs.getInt("id"),
                    rs.getInt("usuario_id"),
                    rs.getString("nombre"),
                    rs.getBigDecimal("monto"),
                    rs.getString("descripcion"),
                    rs.getInt("categoria_id")
                );
                
                movimiento.setTipo_movimiento_id(rs.getInt("tipo_movimiento_id"));
                movimiento.setFecha_creacion(rs.getDate("fecha_creacion"));
                
                movimientos.add(movimiento);
            }
            rs.close();

            if (!movimientos.isEmpty()) {
                return ResponseProvider.success(movimientos, "Movimiento del usuario obtenido con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay movimiento registrado para el usuario.", 404);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener el movimiento", 500);
        }
    }
    
    public static Response getMovimientosByCategoria(int categoria_id, int usuario_id, int tipo_movimiento_id, int mes){
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
                    rs.getDate("fecha_creacion"),
                    rs.getBigDecimal("monto")
                );
                
                movimientos.add(movimiento);
            }
            
            rs.close();
            
            if (!movimientos.isEmpty()) {
                return ResponseProvider.success(movimientos, "Movimientos obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay movimientos registrados.", 404);
            }
            
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los movimientos", 500);
        }
        
    }

    public static Response createMovimiento(Movimiento movimientoData) {
        try {
            int idGenerado = 0;
            ResultSet rs = MovimientoDao.createMovimiento(movimientoData);

            while (rs.next()) {
                idGenerado = rs.getInt(1);
                movimientoData.setId(idGenerado);
            }
            rs.close();

            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear el movimiento.", 400);
            } else {
                return ResponseProvider.success(movimientoData, "Movimiento creado con éxito.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al crear el movimiento.", 500);
        }
    }

    public static Response updateMovimiento(int id, int usuario_id, Movimiento movimientoData) {
        try {
            Response existente = getMovimientoById(id);

            if (existente.getStatus() == 404)
                return ResponseProvider.error("Este movimiento no existe.", 404);

            int filasAfectadas = MovimientoDao.updateMovimiento(id, usuario_id, movimientoData);

            if (filasAfectadas != 0) {
                movimientoData.setId(id);
                return ResponseProvider.success(movimientoData, "Movimiento actualizado con éxito.", 200);
            } else {
                return ResponseProvider.error("Error al actualizar el movimiento.", 400);
            }

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar el movimiento.", 500);
        }
    }

    public static Response deleteMovimiento(int id, int usuario_id) {
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