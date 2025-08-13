package CONTROLLER; // Define que esta clase pertenece al paquete CONTROLLER, encargado de la lógica de control del sistema.

import MODELO.MetaDao; // Importa la clase DAO para operaciones con la base de datos relacionadas con metas.
import MODELO.CantidadRegistrosDTO; // Importa el DTO para devolver la cantidad total de registros.
import MODELO.Meta; // Importa la entidad que representa una meta en el sistema.
import MODELO.MetaDetalleDTO; // Importa el DTO para detalles extendidos de una meta.
import MODELO.MetaResumenDTO; // Importa el DTO para resúmenes de metas con totales agregados.
import java.math.BigDecimal; // Clase para manejar cálculos precisos con números decimales.
import java.sql.ResultSet; // Clase para manejar resultados de consultas SQL.
import java.sql.SQLException; // Maneja excepciones relacionadas con operaciones SQL.
import java.time.LocalDate; // Clase para manejar fechas modernas (Java 8+).
import java.util.ArrayList; // Implementación de lista dinámica para almacenar objetos.
import java.util.List; // Interfaz para colecciones de tipo lista.
import javax.ws.rs.*; // Importa anotaciones JAX-RS para definir endpoints REST (GET, POST, PUT, DELETE, etc.).
import javax.ws.rs.core.MediaType; // Define los tipos de contenido para entradas y salidas (por ejemplo, JSON).
import javax.ws.rs.core.Response; // Permite construir respuestas HTTP con códigos de estado y datos.

@Path("/metas") // Define la ruta base para todos los endpoints REST relacionados con metas.
public class MetaController { // Clase que gestiona las operaciones CRUD y específicas para la entidad Meta.

    /**
     * Método para obtener todas las metas registradas en la base de datos.
     * Responde a solicitudes GET en /metas.
     * @return Response HTTP con la lista de metas o un mensaje informativo si no hay datos.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response getMetas() {
        List<Meta> metas = new ArrayList<>(); // Lista para almacenar las metas obtenidas.

        try {
            // Llama al método DAO para obtener todas las metas desde la base de datos.
            ResultSet rs = MetaDao.getMetas();

            // Itera sobre el ResultSet para construir objetos Meta.
            while (rs.next()) {
                String descripcion = rs.getString("descripcion"); // Obtiene la descripción de la meta.
                if (descripcion == null) descripcion = ""; // Reemplaza null por cadena vacía para evitar errores.

                Meta meta = new Meta(
                    rs.getInt("id"),                    // ID único de la meta.
                    rs.getInt("usuario_id"),            // ID del usuario propietario.
                    rs.getString("nombre"),             // Nombre de la meta.
                    rs.getBigDecimal("monto"),          // Monto objetivo de la meta.
                    descripcion,                        // Descripción (validada).
                    rs.getString("fecha_limite")        // Fecha límite para alcanzar la meta.
                );
                meta.setFecha_creacion(rs.getString("fecha_creacion").substring(0, 10)); // Formatea la fecha de creación (sin hora).
                meta.setCompletada(rs.getBoolean("completada")); // Estado de completitud de la meta.

                metas.add(meta); // Agrega la meta a la lista.
            }

            // Cierra el ResultSet para liberar recursos.
            rs.close();

            // Retorna éxito con código 200, incluso si la lista está vacía, con mensaje apropiado.
            if (!metas.isEmpty()) {
                return ResponseProvider.success(metas, "Metas obtenidas con éxito.", 200);
            } else {
                return ResponseProvider.success(metas, "No hay metas registradas.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener las metas.", 500);
        }
    }

    /**
     * Método para obtener la cantidad total de metas registradas.
     * Responde a solicitudes GET en /metas/cantidad.
     * @return Response HTTP con la cantidad de metas o un mensaje de error.
     */
    @GET
    @Path("/cantidad") // Ruta específica para obtener la cantidad de metas.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response getUsuario() { // Nota: El nombre del método parece incorrecto, debería ser getCantidadMetas.
        CantidadRegistrosDTO cantidad = null; // Objeto para almacenar la cantidad de metas.

        try {
            // Llama al método DAO para obtener el conteo de metas.
            ResultSet respuesta = MetaDao.getCantidadMetas();

            // Procesa el ResultSet para construir el objeto CantidadRegistrosDTO.
            while (respuesta.next()) {
                cantidad = new CantidadRegistrosDTO(respuesta.getInt("cantidad")); // Obtiene el conteo total.
            }

            // Cierra el ResultSet para liberar recursos.
            respuesta.close();

            // Si no se obtuvo un conteo, retorna error 404.
            if (cantidad == null) {
                return ResponseProvider.error("No se pudo obtener la cantidad de metas.", 404);
            } else {
                // Si se obtuvo el conteo, retorna éxito con código 200.
                return ResponseProvider.success(cantidad, "Cantidad de metas obtenida con éxito.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener la cantidad de metas", 500);
        }
    }

    /**
     * Método para obtener un resumen de metas de un usuario con totales de aportes.
     * Responde a solicitudes GET en /metas/resumen/usuario/{usuario_id}.
     * @param usuario_id Identificador único del usuario.
     * @return Response HTTP con la lista de resúmenes de metas o un mensaje informativo.
     */
    @GET
    @Path("/resumen/usuario/{usuario_id}") // Ruta con parámetro para usuario.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response getMetasResumen(@PathParam("usuario_id") int usuario_id) {
        List<MetaResumenDTO> metas = new ArrayList<>(); // Lista para almacenar resúmenes de metas.

        try {
            // Llama al método DAO para obtener metas con totales de aportes.
            ResultSet rs = MetaDao.getMetasConTotal(usuario_id);

            // Itera sobre el ResultSet para construir objetos MetaResumenDTO.
            while (rs.next()) {
                MetaResumenDTO meta = new MetaResumenDTO(
                    rs.getInt("id"),                    // ID de la meta.
                    rs.getString("nombre"),             // Nombre de la meta.
                    rs.getBigDecimal("monto"),          // Monto objetivo.
                    rs.getString("fecha_limite"),       // Fecha límite.
                    rs.getString("fecha_creacion").substring(0, 10), // Fecha de creación (sin hora).
                    rs.getBigDecimal("total"),          // Total aportado a la meta.
                    rs.getBoolean("completada")         // Estado de completitud.
                );

                // Determina un mensaje motivacional basado en el estado de la meta.
                LocalDate fecha_actual = LocalDate.now(); // Fecha actual.
                LocalDate fecha_limite = null;
                if (meta.getFecha_limite() != null) {
                    fecha_limite = LocalDate.parse(meta.getFecha_limite()); // Parsea la fecha límite.
                }

                BigDecimal monto = meta.getMonto();
                BigDecimal total = meta.getTotal();
                int comparacion = monto.compareTo(total); // Compara monto objetivo con total aportado.

                // Asigna mensaje según el estado de la meta.
                if (comparacion <= 0) {
                    meta.setMensaje("¡Lo lograste!, Felicidades");
                } else if (fecha_limite != null && fecha_limite.isBefore(fecha_actual) && !meta.isCompletada()) {
                    meta.setMensaje("¡No te rindas!, Aún puedes actualizar la fecha.");
                } else {
                    meta.setMensaje("¡Tu puedes lograrlo!, Suerte");
                }

                metas.add(meta); // Agrega el resumen a la lista.
            }

            // Cierra el ResultSet para liberar recursos.
            rs.close();

            // Retorna éxito con código 200, incluso si la lista está vacía, con mensaje apropiado.
            if (!metas.isEmpty()) {
                return ResponseProvider.success(metas, "Metas obtenidas con éxito.", 200);
            } else {
                return ResponseProvider.success(metas, "No hay metas registradas.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener las metas.", 500);
        }
    }

    /**
     * Método para obtener los detalles de una meta específica para un usuario.
     * Responde a solicitudes GET en /metas/{id}/usuario/{usuario_id}/detalles.
     * @param id Identificador único de la meta.
     * @param usuario_id Identificador único del usuario.
     * @return Response HTTP con los detalles de la meta o un mensaje de error.
     */
    @GET
    @Path("/{id}/usuario/{usuario_id}/detalles") // Ruta con parámetros para meta y usuario.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response getMetaDetalles(
            @PathParam("id") int id,
            @PathParam("usuario_id") int usuario_id) {
        MetaDetalleDTO meta = null; // Objeto para almacenar los detalles de la meta.

        try {
            // Llama al método DAO para obtener detalles de la meta, incluyendo cantidad de aportes.
            ResultSet rs = MetaDao.getMetasCantMovimientos(id, usuario_id);

            // Procesa el ResultSet para construir el objeto MetaDetalleDTO.
            while (rs.next()) {
                String descripcion = rs.getString("descripcion"); // Obtiene la descripción.
                if (descripcion == null) descripcion = ""; // Reemplaza null por cadena vacía.

                meta = new MetaDetalleDTO(
                    rs.getInt("id"),                    // ID de la meta.
                    rs.getString("nombre"),             // Nombre de la meta.
                    descripcion,                        // Descripción (validada).
                    rs.getBigDecimal("monto"),          // Monto objetivo.
                    rs.getBigDecimal("total"),          // Total aportado.
                    rs.getString("fecha_creacion").substring(0, 10), // Fecha de creación (sin hora).
                    rs.getString("fecha_limite"),       // Fecha límite.
                    rs.getInt("cantidad_aportes")       // Cantidad de aportes asociados.
                );

                // Convierte el estado booleano completada a texto legible.
                String estado = rs.getBoolean("completada") ? "Completada" : "Incompleta";
                meta.setEstado(estado);
            }

            // Cierra el ResultSet para liberar recursos.
            rs.close();

            // Si no se encontró la meta, retorna error 404.
            if (meta == null) {
                return ResponseProvider.error("La meta no existe.", 404);
            } else {
                // Si se encontró, retorna éxito con código 200 y los detalles.
                return ResponseProvider.success(meta, "Meta obtenida con éxito.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener la meta.", 500);
        }
    }

    /**
     * Método para obtener una meta específica por su ID.
     * Responde a solicitudes GET en /metas/{id}.
     * @param id Identificador único de la meta.
     * @return Response HTTP con los datos de la meta o un mensaje de error.
     */
    @GET
    @Path("/{id}") // Ruta con parámetro para buscar una meta específica.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response getMetaById(@PathParam("id") int id) {
        Meta meta = null; // Objeto para almacenar la meta encontrada.

        try {
            // Llama al método DAO para obtener la meta por su ID.
            ResultSet rs = MetaDao.getMetaById(id);

            // Procesa el ResultSet para construir el objeto Meta.
            while (rs.next()) {
                String descripcion = rs.getString("descripcion"); // Obtiene la descripción.
                if (descripcion == null) descripcion = ""; // Reemplaza null por cadena vacía.

                meta = new Meta(
                    rs.getInt("id"),                    // ID de la meta.
                    rs.getInt("usuario_id"),            // ID del usuario propietario.
                    rs.getString("nombre"),             // Nombre de la meta.
                    rs.getBigDecimal("monto"),          // Monto objetivo.
                    descripcion,                        // Descripción (validada).
                    rs.getString("fecha_limite")        // Fecha límite.
                );
                meta.setFecha_creacion(rs.getString("fecha_creacion").substring(0, 10)); // Formatea la fecha de creación.
                meta.setCompletada(rs.getBoolean("completada")); // Estado de completitud.
            }

            // Cierra el ResultSet para liberar recursos.
            rs.close();

            // Si no se encontró la meta, retorna error 404.
            if (meta == null) {
                return ResponseProvider.error("La meta no existe.", 404);
            } else {
                // Si se encontró, retorna éxito con código 200 y los datos de la meta.
                return ResponseProvider.success(meta, "Meta obtenida con éxito.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener la meta.", 500);
        }
    }

    /**
     * Método para crear una nueva meta.
     * Responde a solicitudes POST en /metas.
     * @param metaData Objeto Meta con los datos de la nueva meta.
     * @return Response HTTP con la meta creada o un mensaje de error.
     */
    @POST
    @Validar(entidad = "Meta") // Aplica validaciones definidas para la entidad Meta.
    @Consumes(MediaType.APPLICATION_JSON) // Indica que recibe datos en formato JSON.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response createMeta(Meta metaData) {
        try {
            // Asegura que la descripción no sea null para evitar errores en la base de datos.
            if (metaData.getDescripcion() == null) metaData.setDescripcion("");

            int idGenerado = 0; // Variable para almacenar el ID generado por la base de datos.

            // Llama al método DAO para insertar la meta en la base de datos.
            ResultSet rs = MetaDao.createMeta(metaData);

            // Procesa el ResultSet para obtener el ID generado tras la inserción.
            while (rs.next()) {
                idGenerado = rs.getInt(1); // Obtiene el ID del primer campo.
                metaData.setId(idGenerado); // Asigna el ID al objeto meta.
            }

            // Cierra el ResultSet para liberar recursos.
            rs.close();

            // Si no se generó un ID (inserción fallida), retorna error 400.
            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear la meta.", 400);
            } else {
                // Si la inserción fue exitosa, retorna la meta creada con código 200.
                return ResponseProvider.success(metaData, "Meta creada con éxito.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al crear la meta.", 500);
        }
    }

    /**
     * Método para actualizar una meta existente para un usuario específico.
     * Responde a solicitudes PUT en /metas/{id}/usuario/{usuario_id}.
     * @param id Identificador único de la meta.
     * @param usuario_id Identificador único del usuario.
     * @param metaData Objeto Meta con los nuevos datos.
     * @return Response HTTP indicando éxito o error.
     */
    @PUT
    @Validar(entidad = "Meta") // Aplica validaciones definidas para la entidad Meta.
    @Path("/{id}/usuario/{usuario_id}") // Ruta con parámetros para meta y usuario.
    @Consumes(MediaType.APPLICATION_JSON) // Indica que recibe datos en formato JSON.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response updateMeta(
            @PathParam("id") int id,
            @PathParam("usuario_id") int usuario_id,
            Meta metaData) {
        try {
            // Asegura que la descripción no sea null para evitar errores en la base de datos.
            if (metaData.getDescripcion() == null) metaData.setDescripcion("");

            // Verifica si la meta existe llamando al método getMetaById.
            Response existente = getMetaById(id);

            // Si la meta no existe, retorna error 404.
            if (existente.getStatus() == 404) {
                return ResponseProvider.error("La meta no existe.", 404);
            }

            // Llama al método DAO para actualizar los datos de la meta.
            int filasAfectadas = MetaDao.updateMeta(id, usuario_id, metaData);

            // Si la actualización fue exitosa, retorna código 200 con la meta actualizada.
            if (filasAfectadas != 0) {
                metaData.setId(id); // Asegura que el ID se mantenga en el objeto.
                return ResponseProvider.success(metaData, "Meta actualizada con éxito.", 200);
            } else {
                // Si no se afectó ningún registro, recalcula el estado completada.
                gestionarMetaCompletada(id);
                return ResponseProvider.error("Error al actualizar la meta.", 400);
            }

        } catch (Exception e) {
            // Captura errores inesperados y retorna error 500.
            return ResponseProvider.error("Error interno al actualizar la meta.", 500);
        }
    }

    /**
     * Método para realizar una eliminación lógica (soft delete) de una meta.
     * Responde a solicitudes DELETE en /metas/soft/{id}.
     * @param id Identificador único de la meta.
     * @return Response HTTP indicando éxito o error.
     */
    @DELETE
    @Path("soft/{id}") // Ruta para eliminación lógica de una meta.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response softDeleteMeta(@PathParam("id") int id) {
        try {
            // Llama al método DAO para marcar la meta como eliminada (soft delete).
            int rowsAffected = MetaDao.softDeleteMeta(id);

            // Si la eliminación lógica fue exitosa, retorna código 200.
            if (rowsAffected != 0) {
                return ResponseProvider.success(null, "Meta eliminada de forma segura.", 200);
            } else {
                // Si la meta no existe, retorna error 404.
                return ResponseProvider.error("Esta meta no existe.", 404);
            }

        } catch (Exception e) {
            // Captura errores inesperados y retorna error 500.
            return ResponseProvider.error("Error interno al eliminar la meta.", 500);
        }
    }

    /**
     * Método para eliminar físicamente una meta de la base de datos, validando aportes asociados.
     * Responde a solicitudes DELETE en /metas/{id}/usuario/{usuario_id}.
     * @param id Identificador único de la meta.
     * @param usuario_id Identificador único del usuario.
     * @return Response HTTP indicando éxito o error.
     */
    @DELETE
    @Path("/{id}/usuario/{usuario_id}") // Ruta con parámetros para meta y usuario.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response deleteMeta(
            @PathParam("id") int id,
            @PathParam("usuario_id") int usuario_id) {
        try {
            // Verifica si la meta tiene aportes asociados llamando al controlador de aportes.
            Response hasAportes = AportesMetaController.getAportesByMetaId(id);

            // Si la meta tiene aportes, retorna error 409 (Conflicto).
            if (hasAportes.getStatus() == 200) {
                return ResponseProvider.error("La meta tiene aportes registrados.", 409);
            }

            // Llama al método DAO para eliminar físicamente la meta.
            int filasAfectadas = MetaDao.deleteMeta(id, usuario_id);

            // Si la eliminación fue exitosa, retorna código 200.
            if (filasAfectadas != 0) {
                return ResponseProvider.success(null, "Meta eliminada con éxito.", 200);
            } else {
                // Si la meta no existe o no pertenece al usuario, retorna error 404.
                return ResponseProvider.error("La meta no existe o no pertenece al usuario.", 404);
            }

        } catch (Exception e) {
            // Captura errores inesperados y retorna error 500.
            return ResponseProvider.error("Error interno al eliminar la meta.", 500);
        }
    }

    /**
     * Método auxiliar para actualizar el estado "completada" de una meta según sus aportes.
     * @param meta_id Identificador único de la meta.
     */
    public static void gestionarMetaCompletada(int meta_id) {
        try {
            // Llama al método DAO para obtener el monto objetivo y el total aportado.
            ResultSet rs = MetaDao.getMetaTotales(meta_id);

            // Procesa el ResultSet para comparar montos.
            while (rs.next()) {
                BigDecimal monto = rs.getBigDecimal("monto"); // Monto objetivo.
                BigDecimal total = rs.getBigDecimal("total"); // Total aportado.

                int comparacion = monto.compareTo(total); // Compara ambos valores.

                // Actualiza el estado completada según la comparación.
                if (comparacion <= 0) {
                    updateCompletada(meta_id, true); // Meta alcanzada.
                } else {
                    updateCompletada(meta_id, false); // Meta no alcanzada.
                }
            }
        } catch (SQLException e) {
            // Lanza una excepción en caso de error SQL (no capturada para propagarla al llamador).
            throw new Error("Error al verificar el estado de la meta");
        }
    }

    /**
     * Método para actualizar el campo "completada" de una meta.
     * @param id Identificador único de la meta.
     * @param completada Nuevo valor del estado completada.
     * @return Response HTTP indicando éxito o error.
     */
    public static Response updateCompletada(int id, boolean completada) {
        try {
            // Verifica si la meta existe llamando al método getMetaById.
            Response existente = getMetaById(id);
            if (existente.getStatus() == 404) {
                return ResponseProvider.error("La meta no existe.", 404);
            }

            // Llama al método DAO para actualizar el campo completada.
            int filasAfectadas = MetaDao.updateCompletada(id, completada);

            // Si la actualización fue exitosa, retorna código 200.
            if (filasAfectadas != 0) {
                return ResponseProvider.success(null, "Campo 'completada' actualizado correctamente.", 200);
            } else {
                // Si no se afectó ningún registro, retorna error 400.
                return ResponseProvider.error("Error al actualizar el campo 'completada'.", 400);
            }

        } catch (Exception e) {
            // Captura errores inesperados y retorna error 500.
            return ResponseProvider.error("Error interno al actualizar el campo 'completada'.", 500);
        }
    }
}