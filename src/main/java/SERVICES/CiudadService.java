package SERVICES;

import DAO.CiudadDao;
import MODEL.Ciudad;
import PROVIDERS.ResponseProvider;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import org.json.JSONException;

public class CiudadService {
    
    public static Response getCiudades() {
        List<Ciudad> ciudades = new ArrayList<>(); // Crea una lista para almacenar las ciudades recuperadas
        
        try {
            // Ejecuta la consulta para obtener todas las ciudades mediante la capa DAO
            ResultSet respuesta = CiudadDao.getCiudades();
            while (respuesta.next()) { // Cambia esto a un while
                // Crea un objeto Ciudad con los datos de cada fila
                Ciudad ciudad = new Ciudad(
                    respuesta.getInt("id"),
                    respuesta.getString("nombre")
                );
                // Añade el objeto Usuario a la lista de ciudades
                ciudades.add(ciudad);
            }
            // Cierra el ResultSet para liberar recursos
            respuesta.close();
            // Devuelve la lista de ciudades con estado 200 OK si hay ciudades
            if (!ciudades.isEmpty()) {
                return ResponseProvider.success(ciudades, "Ciudades obtenidas con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay ciudades registradas.", 404);
            }
            
        } catch (SQLException e) {
            // Si ocurre un error en la consulta, devuelve un estado 500
            return ResponseProvider.error("Error interno al obtener las ciudades", 500);
        }
    }
    
    public static Response getCiudadById(int id) {
        Ciudad ciudad = null; // Variable para almacenar la ciudad encontrada
        
        try {
            // Realiza la consulta para recuperar el usuario por su ID a través de la capa DAO
            ResultSet respuesta = CiudadDao.getCiudadById(id);
            
            // Si la consulta devuelve datos, crea el objeto Usuario
            while (respuesta.next()) { 
                ciudad = new Ciudad(
                    respuesta.getInt("id"), // Obtiene el ID de la ciudad
                    respuesta.getString("nombre") // Obtiene el nombre de la ciudad
                );
            }
            // Cierra el ResultSet para liberar recursos
            respuesta.close();
            
            // Devuelve el usuario con estado 200 OK si existe
            if (ciudad == null) {
                return ResponseProvider.error("La ciudad no existe.", 404);
            } else {
                return ResponseProvider.success(ciudad, "Ciudad obtenida con éxito.", 200);
            }
            
        } catch (SQLException e) {
            // Si ocurre un error en la consulta, devuelve un estado 500
            return ResponseProvider.error("Error interno al obtener la ciudad", 500);
        }
    }
    
    public static Response createCiudad(Ciudad ciudadData) {
        try {
            
            int idGenerado = 0; // Inicializa variable para almacenar el ID generado
            
            // Inserta la nueva ciudad en la base de datos y obtiene el último ID generado
            ResultSet ultimoRegistro = CiudadDao.createCiudad(ciudadData);
            
            // Si la inserción fue exitosa, asigna el ID generado al objeto ciudad
            while (ultimoRegistro.next()) {
                idGenerado = ultimoRegistro.getInt(1);
                ciudadData.setId(idGenerado); // Asigna el ID generado al objeto ciudad
            }
            
            // Cierra el conjunto de resultados para optimizar recursos
            ultimoRegistro.close();
            
            // Devuelve la ciudad con estado 200 OK si se crea con exito
            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear la ciudad.", 400);
            } else {
                return ResponseProvider.success(ciudadData, "Ciudad creada con éxito.", 200);
            }
            
        } catch (SQLException e) {
            // Si ocurre un error en la consulta, devuelve un estado 500
            return ResponseProvider.error("Error interno al crear la ciudad.", 500);
        }
    }
    
    public static Response updateCiudad(int id, Ciudad ciudadData) {
        try {
           
           Response ciudadExistente = getCiudadById(id);
            
           if (ciudadExistente.getStatus() == 404) return ResponseProvider.error("Esta ciudad no existe.", 404);
           
            // Intenta actualizar la ciudad en la base de datos y devuelve el número de filas afectadas
            int rowsAffected = CiudadDao.updateCiudad(id, ciudadData);
            
            if (rowsAffected != 0){
                
                ciudadData.setId(id);
                // Si la actualización se realizó, se confirma el éxito con código 200 OK
                return ResponseProvider.success(ciudadData, "Ciudad actualizada con éxito.", 200);
            }
            else 
                return ResponseProvider.error("Error al actualizar la ciudad.", 400);
            
        } catch (Exception e) {
            // Captura cualquier problema interno y devuelve un error 500 con mensaje
            return ResponseProvider.error("Error interno al actualizar la ciudad.", 500);
        }
    }
    
   public static Response deleteCiudad(int id) {
        try {
            
            // Ejecuta la eliminación de la ciudad en la base de datos y recibe la cantidad de filas afectadas
            int rowsAffected = CiudadDao.deleteCiudad(id);
            
            if (rowsAffected != 0) 
                // Si ciudad es eliminada correctamente, devuelve código 204 No Content con mensaje
                return ResponseProvider.success(null, "Ciudad eliminada con éxito.", 200);
            else 
                // Si no encontró ciudad para eliminar, devuelve 404 Not Found con mensaje
                return ResponseProvider.error("Esta ciudad no existe.", 404);
            
        } catch (Exception e) {
            // Para cualquier error interno, retorna un error 500 con mensaje
            return ResponseProvider.error("Error interno al eliminar la ciudad.", 500);
        }
    }
}
