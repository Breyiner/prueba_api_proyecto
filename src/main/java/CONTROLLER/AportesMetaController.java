package CONTROLLER; // Definimos que esta clase pertenece al paquete CONTROLLER

// Importamos todas las clases necesarias para que funcione nuestro controlador
import MODELO.AportesMetaDao; // Clase que maneja las consultas a la base de datos
import MODELO.AporteMetaCalendarioDTO; // Clase para datos de aportes en formato calendario
import MODELO.AportesCalendarioDTO; // Clase para datos de aportes con información de calendario
import MODELO.AportesMeta; // Clase principal que representa un aporte
import MODELO.AportesMetaDetalladoDTO; // Clase para aportes con información detallada (colores, iconos)
import MODELO.CantidadRegistrosDTO; // Clase para devolver números/cantidades
import java.sql.ResultSet; // Para manejar los resultados de las consultas SQL
import java.sql.SQLException; // Para capturar errores de base de datos
import java.util.ArrayList; // Para crear listas dinámicas
import java.util.List; // Interfaz para manejar listas
import javax.ws.rs.*; // Importa todas las anotaciones de REST (GET, POST, etc.)
import javax.ws.rs.core.MediaType; // Para especificar tipos de contenido (JSON)
import javax.ws.rs.core.Response; // Para crear respuestas HTTP

/**
 * Controlador REST para manejar todas las operaciones relacionadas con aportes a metas
 * Este controlador permite crear, leer, actualizar y eliminar aportes financieros
 * que los usuarios hacen para cumplir sus metas de ahorro
 */
@Path("/aportes") // Define que todas las rutas de esta clase empezarán con /aportes
public class AportesMetaController { // Declaramos la clase pública del controlador

    /**
     * Obtiene todos los aportes registrados en el sistema
     * @return Lista completa de todos los aportes con sus datos básicos
     */
    @GET // Indica que este método responde a peticiones HTTP GET
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON
    public static Response getAportes() { // Método público estático que devuelve una respuesta HTTP
        List<AportesMeta> aportes = new ArrayList<>(); // Creamos una lista vacía para guardar los aportes

        try { // Iniciamos un bloque try para capturar errores
            ResultSet rs = AportesMetaDao.getAportes(); // Llamamos al DAO para obtener datos de la base de datos
            while (rs.next()) { // Recorremos cada fila del resultado de la consulta
                String descripcion = rs.getString("descripcion"); // Obtenemos el valor de la columna "descripcion"
                if (descripcion == null) { // Verificamos si la descripción es null
                    descripcion = ""; // Si es null, la cambiamos por una cadena vacía
                } // Cerramos el if
                
                AportesMeta aporte = new AportesMeta( // Creamos un nuevo objeto AportesMeta
                    rs.getInt("id"), // Obtenemos el ID como número entero
                    rs.getInt("meta_id"), // Obtenemos el ID de la meta como número entero
                    rs.getBigDecimal("monto"), // Obtenemos el monto como BigDecimal (para dinero)
                    descripcion, // Usamos la descripción que ya validamos
                    rs.getString("fecha_creacion").substring(0, 10) // Obtenemos la fecha y tomamos solo los primeros 10 caracteres
                ); // Cerramos la creación del objeto
                aportes.add(aporte); // Agregamos el aporte a nuestra lista
            } // Cerramos el while
            rs.close(); // Cerramos el ResultSet para liberar memoria

            if (!aportes.isEmpty()) { // Verificamos si la lista tiene elementos
                return ResponseProvider.success(aportes, "Aportes obtenidos con éxito.", 200); // Devolvemos respuesta exitosa
            } else { // Si la lista está vacía
                return ResponseProvider.error("No hay aportes registrados.", 404); // Devolvemos error "no encontrado"
            } // Cerramos el else

        } catch (SQLException e) { // Capturamos errores de base de datos
            return ResponseProvider.error("Error interno al obtener los aportes.", 500); // Devolvemos error del servidor
        } // Cerramos el catch
    } // Cerramos el método
    
    /**
     * Obtiene el número total de aportes registrados en el sistema
     * @return Cantidad total de aportes como número entero
     */
    @GET // Este método responde a peticiones HTTP GET
    @Path("/cantidad") // Define la ruta específica como /aportes/cantidad
    @Produces(MediaType.APPLICATION_JSON) // La respuesta será en formato JSON
    public static Response getUsuario() { // Método público estático (nota: el nombre debería ser getCantidadAportes)
        
        CantidadRegistrosDTO cantidad = null; // Inicializamos la variable como null
        
        try { // Iniciamos bloque try para manejar errores
            ResultSet respuesta = AportesMetaDao.getCantidadAportes(); // Ejecutamos consulta para contar aportes
            while (respuesta.next()) { // Iteramos sobre el resultado (debería ser solo una fila)
                cantidad = new CantidadRegistrosDTO( // Creamos objeto con la cantidad
                    respuesta.getInt("cantidad") // Obtenemos el número de aportes de la columna "cantidad"
                ); // Cerramos la creación del objeto
            } // Cerramos el while
            respuesta.close(); // Liberamos los recursos del ResultSet
            
            if (cantidad == null) { // Verificamos si no se obtuvo ningún resultado
                return ResponseProvider.error("No se pudo obtener la cantidad de aportes.", 404); // Error si no hay datos
            } else { // Si sí obtuvimos la cantidad
                return ResponseProvider.success(cantidad, "Cantidad de aportes obtenida con éxito.", 200); // Respuesta exitosa
            } // Cerramos el else
            
        } catch (SQLException e) { // Capturamos errores de SQL
            return ResponseProvider.error("Error interno al obtener la cantidad de aportes", 500); // Error del servidor
        } // Cerramos el catch
    } // Cerramos el método

    /**
     * Obtiene todos los aportes que pertenecen a una meta específica
     * @param meta_id ID de la meta de la cual queremos obtener los aportes
     * @return Lista de aportes que pertenecen únicamente a esa meta
     */
    @GET // Método que responde a peticiones GET
    @Path("/meta/{meta_id}") // Ruta dinámica donde {meta_id} será reemplazado por el valor real
    @Produces(MediaType.APPLICATION_JSON) // Respuesta en formato JSON
    public static Response getAportesByMetaId(@PathParam("meta_id") int meta_id) { // Método que recibe el ID de meta desde la URL
        List<AportesMeta> aportes = new ArrayList<>(); // Lista para almacenar los aportes encontrados

        try { // Bloque para manejar errores
            ResultSet rs = AportesMetaDao.getAportesByMetaId(meta_id); // Consultamos aportes de esa meta específica
            while (rs.next()) { // Recorremos cada resultado
                String descripcion = rs.getString("descripcion"); // Obtenemos la descripción del aporte
                if (descripcion == null) { // Verificamos si es null
                    descripcion = ""; // La convertimos en cadena vacía si es null
                } // Fin de la validación
                
                AportesMeta aporte = new AportesMeta( // Creamos nuevo objeto aporte
                    rs.getInt("id"), // ID del aporte
                    rs.getInt("meta_id"), // ID de la meta
                    rs.getBigDecimal("monto"), // Cantidad monetaria
                    descripcion, // Descripción ya validada
                    rs.getString("fecha_creacion").substring(0, 10) // Fecha en formato YYYY-MM-DD
                ); // Fin de la creación del objeto
                aportes.add(aporte); // Agregamos el aporte a la lista
            } // Fin del while
            rs.close(); // Liberamos recursos

            if (!aportes.isEmpty()) { // Si encontramos aportes
                return ResponseProvider.success(aportes, "Aportes de la meta obtenidos con éxito.", 200); // Respuesta exitosa
            } else { // Si no hay aportes
                return ResponseProvider.error("No hay aportes registrados para esta meta.", 404); // Error no encontrado
            } // Fin del else

        } catch (SQLException e) { // Captura de errores SQL
            return ResponseProvider.error("Error interno al obtener los aportes de la meta.", 500); // Error del servidor
        } // Fin del catch
    } // Fin del método

    /**
     * Busca y devuelve un aporte específico usando su ID único
     * @param id Identificador único del aporte que queremos encontrar
     * @return El aporte encontrado o mensaje de error si no existe
     */
    @GET // Método GET
    @Path("/{id}") // Ruta con parámetro dinámico para el ID
    @Produces(MediaType.APPLICATION_JSON) // Respuesta JSON
    public static Response getAporteById(@PathParam("id") int id) { // Método que recibe ID desde la URL
        AportesMeta aporte = null; // Variable para almacenar el aporte encontrado

        try { // Manejo de errores
            ResultSet rs = AportesMetaDao.getAporteById(id); // Buscamos el aporte por ID
            while (rs.next()) { // Procesamos el resultado (máximo una fila)
                String descripcion = rs.getString("descripcion"); // Obtenemos descripción
                if (descripcion == null) { // Validamos si es null
                    descripcion = ""; // Asignamos cadena vacía si es null
                } // Fin validación
                
                aporte = new AportesMeta( // Creamos el objeto aporte
                    rs.getInt("id"), // ID del aporte
                    rs.getInt("meta_id"), // ID de la meta padre
                    rs.getBigDecimal("monto"), // Monto del aporte
                    descripcion, // Descripción validada
                    rs.getString("fecha_creacion").substring(0, 10) // Fecha formateada
                ); // Fin creación objeto
            } // Fin while
            rs.close(); // Liberamos recursos

            if (aporte == null) { // Si no se encontró el aporte
                return ResponseProvider.error("El aporte no existe.", 404); // Error no encontrado
            } else { // Si se encontró el aporte
                return ResponseProvider.success(aporte, "Aporte obtenido con éxito.", 200); // Respuesta exitosa
            } // Fin else

        } catch (SQLException e) { // Captura errores SQL
            return ResponseProvider.error("Error interno al obtener el aporte.", 500); // Error servidor
        } // Fin catch
    } // Fin método

    /**
     * Obtiene aportes con información detallada filtrados por meta, usuario y mes
     * Incluye datos adicionales como iconos y colores para mostrar en la interfaz
     * @param meta_id ID de la meta
     * @param usuario_id ID del usuario
     * @param mes Número del mes (1-12)
     * @return Lista de aportes con información visual adicional
     */
    @GET // Método GET
    @Path("/detallados/meta/{meta_id}/usuario/{usuario_id}/mes/{mes}") // Ruta con tres parámetros
    @Produces(MediaType.APPLICATION_JSON) // Respuesta JSON
    public static Response getAportesDetalladosByParametros( // Método con múltiples parámetros
        @PathParam("meta_id") int meta_id, // ID de la meta desde URL
        @PathParam("usuario_id") int usuario_id, // ID del usuario desde URL
        @PathParam("mes") int mes // Número del mes desde URL
    ) { // Inicio del método
       List<AportesMetaDetalladoDTO> aportes = new ArrayList<>(); // Lista para aportes detallados

        try { // Manejo de errores
            ResultSet rs = AportesMetaDao.getAportesDetalladosByParametros(meta_id, usuario_id, mes); // Consulta con filtros
            while (rs.next()) { // Recorremos resultados
                AportesMetaDetalladoDTO aporte = new AportesMetaDetalladoDTO( // Creamos objeto detallado
                    rs.getInt("id"), // ID del aporte
                    rs.getInt("meta_id"), // ID de la meta
                    rs.getString("icono"), // Icono para la interfaz
                    rs.getString("color"), // Color principal
                    rs.getString("color_bg"), // Color de fondo
                    rs.getString("nombre"), // Nombre de la meta
                    rs.getString("fecha_creacion").substring(0, 10), // Fecha formateada
                    rs.getBigDecimal("monto") // Monto del aporte
                ); // Fin creación objeto
                aportes.add(aporte); // Agregamos a la lista
            } // Fin while
            rs.close(); // Liberamos recursos

            if (!aportes.isEmpty()) { // Si hay aportes
                return ResponseProvider.success(aportes, "Aportes detallados obtenidos con éxito.", 200); // Respuesta exitosa
            } else { // Si no hay aportes
                return ResponseProvider.error("No hay aportes registrados con los parámetros especificados.", 404); // Error no encontrado
            } // Fin else

        } catch (SQLException e) { // Captura errores SQL
            return ResponseProvider.error("Error interno al obtener los aportes detallados.", 500); // Error servidor
        } // Fin catch
    } // Fin método
    
    /**
     * Obtiene aportes detallados de una meta específica para un usuario
     * Similar al método anterior pero sin filtro de mes
     * @param meta_id ID de la meta
     * @param usuario_id ID del usuario
     * @return Lista de aportes detallados sin filtro de fecha
     */
    @GET // Método GET
    @Path("/detallados/meta/{meta_id}/usuario/{usuario_id}") // Ruta con dos parámetros
    @Produces(MediaType.APPLICATION_JSON) // Respuesta JSON
    public static Response getAportesDetalladosByMeta( // Método con dos parámetros
        @PathParam("meta_id") int meta_id, // ID de meta desde URL
        @PathParam("usuario_id") int usuario_id // ID de usuario desde URL
    ) { // Inicio método
        List<AportesMetaDetalladoDTO> aportes = new ArrayList<>(); // Lista para aportes

        try { // Manejo errores
            ResultSet rs = AportesMetaDao.getAportesDetalladosByMeta(meta_id, usuario_id); // Consulta sin filtro de mes
            while (rs.next()) { // Recorremos resultados
                AportesMetaDetalladoDTO aporte = new AportesMetaDetalladoDTO( // Creamos objeto detallado
                    rs.getInt("id"), // ID aporte
                    rs.getInt("meta_id"), // ID meta
                    rs.getString("icono"), // Icono
                    rs.getString("color"), // Color
                    rs.getString("color_bg"), // Color fondo
                    rs.getString("nombre"), // Nombre meta
                    rs.getString("fecha_creacion").substring(0, 10), // Fecha
                    rs.getBigDecimal("monto") // Monto
                ); // Fin objeto
                aportes.add(aporte); // Agregamos a lista
            } // Fin while
            rs.close(); // Liberamos recursos

            if (!aportes.isEmpty()) { // Si hay datos
                return ResponseProvider.success(aportes, "Aportes detallados obtenidos con éxito.", 200); // Éxito
            } else { // Sin datos
                return ResponseProvider.error("No hay aportes registrados con los parámetros especificados.", 404); // Error
            } // Fin else

        } catch (SQLException e) { // Error SQL
            return ResponseProvider.error("Error interno al obtener los aportes detallados.", 500); // Error servidor
        } // Fin catch
    } // Fin método
    
    /**
     * Obtiene un resumen de aportes para mostrar en un calendario
     * Devuelve información básica de aportes de un usuario en un mes específico
     * @param usuario_id ID del usuario
     * @param mes Número del mes a consultar
     * @return Lista resumida de aportes para vista de calendario
     */
    @GET // Método GET
    @Path("usuario/{usuario_id}/mes/{mes}") // Ruta con usuario y mes
    @Produces(MediaType.APPLICATION_JSON) // Respuesta JSON
    public static Response getAportesResumidos( // Método para calendario
        @PathParam("usuario_id") int usuario_id, // Usuario desde URL
        @PathParam("mes") int mes) // Mes desde URL
    { // Inicio método
        List<AporteMetaCalendarioDTO> movimientos = new ArrayList<>(); // Lista para calendario
        
        try { // Manejo errores
            ResultSet rs = AportesMetaDao.getAportesResumidos(usuario_id, mes); // Consulta resumida
            
            while(rs.next()) { // Recorremos resultados
                AporteMetaCalendarioDTO movimiento = new AporteMetaCalendarioDTO( // Objeto calendario
                    rs.getInt("id"), // ID aporte
                    rs.getString("nombre"), // Nombre meta
                    rs.getString("color"), // Color para calendario
                    rs.getString("fecha_creacion").substring(0, 10) // Fecha
                ); // Fin objeto
                movimientos.add(movimiento); // Agregamos a lista
            } // Fin while
            
            rs.close(); // Liberamos recursos
            
            if (!movimientos.isEmpty()) { // Si hay datos
                return ResponseProvider.success(movimientos, "Aportes obtenidos con éxito.", 200); // Éxito
            } else { // Sin datos
                return ResponseProvider.success(movimientos, "No hay aportes registrados.", 200); // Éxito vacío
            } // Fin else
            
        } catch (SQLException e) { // Error SQL
            return ResponseProvider.error("Error interno al obtener los aportes", 500); // Error servidor
        } // Fin catch
    } // Fin método
    
    /**
     * Obtiene aportes detallados para una fecha específica
     * Usado para mostrar todos los movimientos de un día en particular
     * @param usuario_id ID del usuario
     * @param fecha Fecha específica en formato YYYY-MM-DD
     * @return Lista de aportes realizados en esa fecha
     */
    @GET // Método GET
    @Path("resumidos/usuario/{usuario_id}/fecha/{fecha}") // Ruta con usuario y fecha
    @Produces(MediaType.APPLICATION_JSON) // Respuesta JSON
    public static Response getAportesResumidosByDate( // Método por fecha
        @PathParam("usuario_id") int usuario_id, // Usuario desde URL
        @PathParam("fecha") String fecha) // Fecha desde URL como String
    { // Inicio método
        List<AportesCalendarioDTO> aportes = new ArrayList<>(); // Lista aportes calendario
        
        try { // Manejo errores
            ResultSet rs = AportesMetaDao.getAportesByDate(usuario_id, fecha); // Consulta por fecha
            
            while(rs.next()) { // Recorremos resultados
                AportesCalendarioDTO movimiento = new AportesCalendarioDTO( // Objeto completo calendario
                    rs.getInt("id"), // ID aporte
                    rs.getString("icono"), // Icono meta
                    "Aporte", // Tipo movimiento (fijo)
                    rs.getString("color"), // Color principal
                    rs.getString("color_bg"), // Color fondo
                    rs.getString("nombre"), // Nombre meta
                    rs.getString("fecha_creacion").substring(0, 10), // Fecha
                    rs.getBigDecimal("monto") // Monto
                ); // Fin objeto
                
                aportes.add(movimiento); // Agregamos a lista
            } // Fin while
            
            rs.close(); // Liberamos recursos
            
            if (!aportes.isEmpty()) { // Si hay datos
                return ResponseProvider.success(aportes, "Aportes obtenidos con éxito.", 200); // Éxito
            } else { // Sin datos
                return ResponseProvider.success(aportes, "No hay aportes registrados.", 200); // Éxito vacío
            } // Fin else
            
        } catch (SQLException e) { // Error SQL
            return ResponseProvider.error("Error interno al obtener los aportes", 500); // Error servidor
        } // Fin catch
    } // Fin método

    /**
     * Crea un nuevo aporte en el sistema
     * Después de crear el aporte, verifica si la meta se completó
     * @param aporteData Objeto con los datos del nuevo aporte
     * @return El aporte creado con su ID generado o mensaje de error
     */
    @POST // Método POST para crear
    @Validar(entidad = "AportesMeta") // Validación personalizada
    @Consumes(MediaType.APPLICATION_JSON) // Acepta JSON
    @Produces(MediaType.APPLICATION_JSON) // Devuelve JSON
    public static Response createAporte(AportesMeta aporteData) { // Método crear con datos JSON
        try { // Manejo errores
            int idGenerado = 0; // Variable para ID generado
            
            if(aporteData.getDescripcion() == null) aporteData.setDescripcion(""); // Validamos descripción null
            
            ResultSet rs = AportesMetaDao.createAporte(aporteData); // Creamos aporte en BD

            while (rs.next()) { // Obtenemos ID generado
                idGenerado = rs.getInt(1); // Primer campo es el ID
                aporteData.setId(idGenerado); // Asignamos ID al objeto
            } // Fin while
            rs.close(); // Liberamos recursos

            if (idGenerado == 0) { // Si no se generó ID
                return ResponseProvider.error("Error al crear el aporte.", 400); // Error creación
            } else { // Si se creó correctamente
                
                MetaController.gestionarMetaCompletada(aporteData.getMeta_id()); // Verificamos si meta se completó
                
                return ResponseProvider.success(aporteData, "Aporte creado con éxito.", 200); // Éxito
            } // Fin else

        } catch (SQLException e) { // Error SQL
            System.out.println(e); // Mostramos error en consola
            return ResponseProvider.error("Error interno al crear el aporte.", 500); // Error servidor
        } // Fin catch
    } // Fin método

    /**
     * Actualiza un aporte existente
     * Verifica que el aporte exista y pertenezca a la meta indicada antes de actualizar
     * @param id ID del aporte a actualizar
     * @param meta_id ID de la meta (para verificar que coincida)
     * @param aporteData Nuevos datos para el aporte
     * @return El aporte actualizado o mensaje de error
     */
    @PUT // Método PUT para actualizar
    @Validar(entidad = "AportesMeta") // Validación datos
    @Path("/{id}/meta/{meta_id}") // Ruta con dos parámetros
    @Consumes(MediaType.APPLICATION_JSON) // Acepta JSON
    @Produces(MediaType.APPLICATION_JSON) // Devuelve JSON
    public static Response updateAporte( // Método actualizar
        @PathParam("id") int id, // ID aporte desde URL
        @PathParam("meta_id") int meta_id, // ID meta desde URL
        AportesMeta aporteData // Datos nuevos desde JSON
    ) { // Inicio método
        try { // Manejo errores
            Response existente = getAporteById(id); // Verificamos que exista
            if (existente.getStatus() == 404) // Si no existe
                return ResponseProvider.error("El aporte no existe.", 404); // Error no encontrado

            if(aporteData.getDescripcion() == null) aporteData.setDescripcion(""); // Validamos descripción
            
            int filasAfectadas = AportesMetaDao.updateAporte(id, meta_id, aporteData); // Actualizamos en BD

            if (filasAfectadas != 0) { // Si se actualizó
                aporteData.setId(id); // Asignamos ID
                aporteData.setMeta_id(meta_id); // Asignamos meta ID
                MetaController.gestionarMetaCompletada(meta_id); // Verificamos meta completada
                return ResponseProvider.success(aporteData, "Aporte actualizado con éxito.", 200); // Éxito
            } else { // Si no se actualizó
                
                return ResponseProvider.error("El aporte no pertenece a la meta especificada o no existe.", 400); // Error
            } // Fin else

        } catch (Exception e) { // Error general
            return ResponseProvider.error("Error interno al actualizar el aporte.", 500); // Error servidor
        } // Fin catch
    } // Fin método
    
    /**
     * Realiza un "borrado suave" de un aporte
     * No elimina físicamente el registro, solo lo marca como eliminado
     * @param id ID del aporte a eliminar de forma suave
     * @return Mensaje de confirmación o error
     */
    @DELETE // Método DELETE
    @Path("soft/{id}") // Ruta para borrado suave
    @Produces(MediaType.APPLICATION_JSON) // Respuesta JSON
    public static Response softDeleteAporte(@PathParam("id") int id) { // Método borrado suave
     
        try { // Manejo errores
            
            int rowsAffected = AportesMetaDao.softDeleteAportes(id); // Marcamos como eliminado
            
            if (rowsAffected != 0) // Si se afectó algún registro
                return ResponseProvider.success(null, "Aporte eliminado de forma segura.", 200); // Éxito
            else // Si no se afectó ningún registro
                return ResponseProvider.error("Este aporte no existe.", 404); // Error no encontrado
            
        } catch (Exception e) { // Error general
            return ResponseProvider.error("Error interno al eliminar el aporte.", 500); // Error servidor
        } // Fin catch
    } // Fin método

    /**
     * Elimina permanentemente un aporte específico
     * Verifica que pertenezca a la meta indicada antes de eliminar
     * @param id ID del aporte a eliminar
     * @param meta_id ID de la meta para verificar pertenencia
     * @return Mensaje de confirmación o error
     */
    @DELETE // Método DELETE
    @Path("/{id}/meta/{meta_id}") // Ruta con dos parámetros
    @Produces(MediaType.APPLICATION_JSON) // Respuesta JSON
    public static Response deleteAporte( // Método eliminar permanente
        @PathParam("id") int id, // ID aporte desde URL
        @PathParam("meta_id") int meta_id // ID meta desde URL
    ) { // Inicio método
        try { // Manejo errores
            Response existente = getAporteById(id); // Verificamos que existe
            if (existente.getStatus() == 404) // Si no existe
                return ResponseProvider.error("El aporte no existe.", 404); // Error no encontrado

            int filasAfectadas = AportesMetaDao.deleteAporte(id, meta_id); // Eliminamos de BD

            if (filasAfectadas != 0) { // Si se eliminó
                return ResponseProvider.success(null, "Aporte eliminado con éxito.", 200); // Éxito
            } else { // Si no se eliminó
                return ResponseProvider.error("El aporte no pertenece a la meta especificada.", 400); // Error
            } // Fin else

        } catch (Exception e) { // Error general
            return ResponseProvider.error("Error interno al eliminar el aporte.", 500); // Error servidor
        } // Fin catch
    } // Fin método

    /**
     * Elimina todos los aportes que pertenecen a una meta específica
     * Útil cuando se elimina una meta completa
     * @param meta_id ID de la meta cuyos aportes se eliminarán
     * @return Mensaje indicando cuántos aportes se eliminaron
     */
    @DELETE // Método DELETE
    @Path("/meta/{meta_id}") // Ruta con meta ID
    @Produces(MediaType.APPLICATION_JSON) // Respuesta JSON
    public static Response deleteAllAportesByMetaId(@PathParam("meta_id") int meta_id) { // Método eliminar todos
        try { // Manejo errores
            int filasAfectadas = AportesMetaDao.deleteAllAportesByMetaId(meta_id); // Eliminamos todos los aportes

            if (filasAfectadas != 0) { // Si se eliminaron registros
                return ResponseProvider.success(null, "Todos los aportes de la meta eliminados con éxito.", 200); // Éxito
            } else { // Si no se eliminó nada
                return ResponseProvider.error("No hay aportes para eliminar en esta meta.", 404); // Error no encontrado
            } // Fin else

        } catch (Exception e) { // Error general
            return ResponseProvider.error("Error interno al eliminar los aportes de la meta.", 500); // Error servidor
        } // Fin catch
    } // Fin método
} // Fin clase