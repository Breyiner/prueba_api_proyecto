package SERVICES;

import DAO.EstadoDao;
import MODEL.Estado;
import PROVIDERS.ResponseProvider;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

public class EstadoService {

    public static Response getEstados() {
        List<Estado> estados = new ArrayList<>();
        try {
            ResultSet respuesta = EstadoDao.getEstados();
            while (respuesta.next()) {
                Estado estado = new Estado(
                    respuesta.getInt("id"),
                    respuesta.getString("nombre")
                );
                estados.add(estado);
            }
            respuesta.close();
            if (!estados.isEmpty()) {
                return ResponseProvider.success(estados, "Estados obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay estados registrados.", 404);
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los estados", 500);
        }
    }

    public static Response getEstadoById(int id) {
        Estado estado = null;
        try {
            ResultSet respuesta = EstadoDao.getEstadoById(id);
            while (respuesta.next()) {
                estado = new Estado(
                    respuesta.getInt("id"),
                    respuesta.getString("nombre")
                );
            }
            respuesta.close();
            if (estado == null) {
                return ResponseProvider.error("El estado no existe.", 404);
            } else {
                return ResponseProvider.success(estado, "Estado obtenido con éxito.", 200);
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener el estado", 500);
        }
    }

    public static Response createEstado(Estado estadoData) {
        try {
            int idGenerado = 0;
            ResultSet ultimoRegistro = EstadoDao.createEstado(estadoData);
            while (ultimoRegistro.next()) {
                idGenerado = ultimoRegistro.getInt(1);
                estadoData.setId(idGenerado);
            }
            ultimoRegistro.close();
            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear el estado.", 400);
            } else {
                return ResponseProvider.success(estadoData, "Estado creado con éxito.", 200);
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al crear el estado.", 500);
        }
    }

    public static Response updateEstado(int id, Estado estadoData) {
        try {
            Response estadoExistente = getEstadoById(id);
            if (estadoExistente.getStatus() == 404) return ResponseProvider.error("Este estado no existe.", 404);

            int rowsAffected = EstadoDao.updateEstado(id, estadoData);
            if (rowsAffected != 0) {
                estadoData.setId(id);
                return ResponseProvider.success(estadoData, "Estado actualizado con éxito.", 200);
            } else {
                return ResponseProvider.error("Error al actualizar el estado.", 400);
            }
        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar el estado.", 500);
        }
    }

    public static Response deleteEstado(int id) {
        try {
            int rowsAffected = EstadoDao.deleteEstado(id);
            if (rowsAffected != 0) {
                return ResponseProvider.success(null, "Estado eliminado con éxito.", 200);
            } else {
                return ResponseProvider.error("Este estado no existe.", 404);
            }
        } catch (Exception e) {
            return ResponseProvider.error("Error interno al eliminar el estado.", 500);
        }
    }
}