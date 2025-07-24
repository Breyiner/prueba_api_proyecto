package SERVICES;

import DAO.RolDao;
import MODEL.Rol;
import PROVIDERS.ResponseProvider;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RolService {

    public static Response getRoles() {
        List<Rol> roles = new ArrayList<>();
        try {
            ResultSet respuesta = RolDao.getRoles();
            while (respuesta.next()) {
                roles.add(new Rol(
                        respuesta.getInt("id"),
                        respuesta.getString("nombre")
                ));
            }
            respuesta.close();
            if (!roles.isEmpty()) {
                return ResponseProvider.success(roles, "Roles obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay roles registrados.", 404);
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los roles", 500);
        }
    }

    public static Response getRolById(int id) {
        Rol rol = null;
        try {
            ResultSet respuesta = RolDao.getRolById(id);
            while (respuesta.next()) {
                rol = new Rol(
                        respuesta.getInt("id"),
                        respuesta.getString("nombre")
                );
            }
            respuesta.close();
            if (rol == null) {
                return ResponseProvider.error("El rol no existe.", 404);
            } else {
                return ResponseProvider.success(rol, "Rol obtenido con éxito.", 200);
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener el rol", 500);
        }
    }

    public static Response createRol(Rol rolData) {
        try {
            int idGenerado = 0;
            ResultSet ultimoRegistro = RolDao.createRol(rolData);
            while (ultimoRegistro.next()) {
                idGenerado = ultimoRegistro.getInt(1);
                rolData.setId(idGenerado);
            }
            ultimoRegistro.close();

            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear el rol.", 400);
            } else {
                return ResponseProvider.success(rolData, "Rol creado con éxito.", 200);
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al crear el rol.", 500);
        }
    }

    public static Response updateRol(int id, Rol rolData) {
        try {
            Response rolExistente = getRolById(id);
            if (rolExistente.getStatus() == 404) return ResponseProvider.error("Este rol no existe.", 404);

            int rowsAffected = RolDao.updateRol(id, rolData);

            if (rowsAffected != 0) {
                rolData.setId(id);
                return ResponseProvider.success(rolData, "Rol actualizado con éxito.", 200);
            } else {
                return ResponseProvider.error("Error al actualizar el rol.", 400);
            }
        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar el rol.", 500);
        }
    }

    public static Response deleteRol(int id) {
        try {
            int rowsAffected = RolDao.deleteRol(id);
            if (rowsAffected != 0) {
                return ResponseProvider.success(null, "Rol eliminado con éxito.", 200);
            } else {
                return ResponseProvider.error("Este rol no existe.", 404);
            }
        } catch (Exception e) {
            return ResponseProvider.error("Error interno al eliminar el rol.", 500);
        }
    }
}