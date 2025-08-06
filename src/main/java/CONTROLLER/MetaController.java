package CONTROLLER; 
// Define el paquete donde está esta clase. Aquí están los controladores que gestionan las solicitudes HTTP para "metas".

import MODELO.MetaDao; 
// Importa la clase de acceso a datos para las operaciones con "Meta" (CRUD en base de datos).
import MODELO.CantidadRegistrosDTO; 
// DTO para manejar cantidad total de registros.
import MODELO.Meta; 
// Entidad Meta que representa un registro en la base de datos.
import MODELO.MetaDetalleDTO; 
// DTO para detalles específicos de una meta, con información extendida.
import MODELO.MetaResumenDTO; 
// DTO para resumen de metas con agregados.
import java.math.BigDecimal; 
// Para trabajar con números decimales con precisión.
import java.sql.ResultSet; 
// Resultado de consultas SQL.
import java.sql.SQLException; 
// Para manejar excepciones relacionadas con SQL.
import java.time.LocalDate; 
// Manejo de fechas modernas (Java 8+).
import java.util.ArrayList; 
// Implementación de lista dinámica.
import java.util.List; 
// Interfaz para colecciones de tipo lista.

import javax.ws.rs.*; 
// Anotaciones REST para definir rutas, métodos, etc.
import javax.ws.rs.core.MediaType; 
// Define los tipos de contenido MIME (aquí JSON).
import javax.ws.rs.core.Response; 
// Para construir las respuestas HTTP.

@Path("/metas") 
// Define la ruta base para este controlador: "/metas".
public class MetaController {

    @GET 
    // Indica que este método responde a solicitudes HTTP GET.
    @Produces(MediaType.APPLICATION_JSON) 
    // El método devuelve una respuesta con contenido JSON.
    public static Response getMetas() {
        List<Meta> metas = new ArrayList<>(); 
        // Lista para almacenar objetos Meta que se van a devolver.

        try {
            ResultSet rs = MetaDao.getMetas(); 
            // Ejecuta la consulta para obtener todas las metas en la BD.

            while (rs.next()) { 
            // Mientras haya registros disponibles:
                String descripcion = rs.getString("descripcion"); 
                // Obtiene el campo "descripcion" de la fila actual.
                if (descripcion == null) descripcion = ""; 
                // Si es null, se reemplaza por cadena vacía para evitar errores.

                Meta meta = new Meta( 
                // Crea un objeto Meta con datos obtenidos de la fila:
                    rs.getInt("id"), // ID de la meta
                    rs.getInt("usuario_id"), // ID del usuario dueño
                    rs.getString("nombre"), // Nombre de la meta
                    rs.getBigDecimal("monto"), // Monto objetivo
                    descripcion, // Descripción (ya validada)
                    rs.getString("fecha_limite") // Fecha límite para cumplir la meta
                );
                meta.setFecha_creacion(rs.getString("fecha_creacion").substring(0, 10)); 
                // Extrae solo la parte de fecha (sin hora) para "fecha_creacion".

                meta.setCompletada(rs.getBoolean("completada")); 
                // Estado booleano indicando si la meta está completada o no.

                metas.add(meta); 
                // Añade la meta a la lista que se devolverá.
            }
            rs.close(); 
            // Cierra el ResultSet para liberar recursos.

            if (!metas.isEmpty()) 
            // Si la lista tiene metas, responde OK con lista y mensaje de éxito.
                return ResponseProvider.success(metas, "Metas obtenidas con éxito.", 200);
            else 
            // Si la lista está vacía, responde OK con lista vacía y mensaje informativo.
                return ResponseProvider.success(metas, "No hay metas registradas.", 200);

        } catch (SQLException e) { 
        // Si ocurre un error en la consulta:
            return ResponseProvider.error("Error interno al obtener las metas.", 500); 
            // Retorna error 500 con mensaje genérico.
        }
    }

    @GET
    @Path("/cantidad") 
    // Ruta para obtener la cantidad total de metas.
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getUsuario() {
        CantidadRegistrosDTO cantidad = null; 
        // Variable para almacenar el resultado de cantidad.

        try {
            ResultSet respuesta = MetaDao.getCantidadMetas(); 
            // Ejecuta consulta para contar metas.

            while (respuesta.next()) { 
            // Solo debe retornar una fila con la cantidad total.
                cantidad = new CantidadRegistrosDTO(respuesta.getInt("cantidad")); 
                // Guarda el número total en el DTO.
            }
            respuesta.close(); // Cierra ResultSet.

            if (cantidad == null) 
            // Si no encontró registros, devuelve error 404.
                return ResponseProvider.error("No se pudo obtener la cantidad de metas.", 404);
            else 
            // Devuelve la cantidad con éxito.
                return ResponseProvider.success(cantidad, "Cantidad de metas obtenida con éxito.", 200);

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener la cantidad de metas", 500);
        }
    }

    @GET
    @Path("/resumen/usuario/{usuario_id}") 
    // Ruta para obtener resumen con totales por usuario.
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getMetasResumen(@PathParam("usuario_id") int usuario_id) {
        List<MetaResumenDTO> metas = new ArrayList<>(); 
        // Lista para almacenar resumenes.

        try {
            ResultSet rs = MetaDao.getMetasConTotal(usuario_id); 
            // Obtiene metas junto con la suma de aportes asociados.

            while (rs.next()) {
                MetaResumenDTO meta = new MetaResumenDTO(
                    rs.getInt("id"), // Id meta
                    rs.getString("nombre"), // Nombre meta
                    rs.getBigDecimal("monto"), // Monto objetivo
                    rs.getString("fecha_limite"), // Fecha límite
                    rs.getString("fecha_creacion").substring(0, 10), // Fecha creación formateada
                    rs.getBigDecimal("total"), // Total aportado
                    rs.getBoolean("completada") // Estado completada
                );

                LocalDate fecha_actual = LocalDate.now(); // Fecha actual.
                LocalDate fecha_limite = null;
                if (meta.getFecha_limite() != null) fecha_limite = LocalDate.parse(meta.getFecha_limite());
                // Parseo seguro de fecha límite.

                BigDecimal monto = meta.getMonto();
                BigDecimal total = meta.getTotal();

                int comparacion = monto.compareTo(total); 
                // Compara monto objetivo con total aportado.

                // Mensajes motivacionales basados en el estado y fechas:
                if (comparacion <= 0) meta.setMensaje("¡Lo lograste!, Felicidades");
                else if (fecha_limite != null && fecha_limite.isBefore(fecha_actual) && !meta.isCompletada())
                    meta.setMensaje("¡No te rindas!, Aún puedes actualizar la fecha.");
                else meta.setMensaje("¡Tu puedes lograrlo!, Suerte");

                metas.add(meta); // Agrega el resumen a la lista.
            }
            rs.close();

            if (!metas.isEmpty())
                return ResponseProvider.success(metas, "Metas obtenidas con éxito.", 200);
            else
                return ResponseProvider.success(metas, "No hay metas registradas.", 200);

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener las metas.", 500);
        }
    }

    @GET
    @Path("/{id}/usuario/{usuario_id}/detalles") 
    // Detalle específico de meta para usuario dado.
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getMetaDetalles(
            @PathParam("id") int id,
            @PathParam("usuario_id") int usuario_id) {
        MetaDetalleDTO meta = null;

        try {
            ResultSet rs = MetaDao.getMetasCantMovimientos(id, usuario_id); 
            // Consulta detalles con cantidad de aportes y totales.

            while (rs.next()) {
                String descripcion = rs.getString("descripcion");
                if (descripcion == null) descripcion = "";

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

                String estado = rs.getBoolean("completada") ? "Completada" : "Incompleta";
                // Estado en texto legible.
                meta.setEstado(estado);
            }
            rs.close();

            if (meta != null)
                return ResponseProvider.success(meta, "Meta obtenida con éxito.", 200);
            else
                return ResponseProvider.error("La meta no existe.", 404);

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener la meta.", 500);
        }
    }

    @GET
    @Path("/{id}") 
    // Obtener meta por su id.
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getMetaById(@PathParam("id") int id) {
        Meta meta = null;

        try {
            ResultSet rs = MetaDao.getMetaById(id);
            while (rs.next()) {
                String descripcion = rs.getString("descripcion");
                if (descripcion == null) descripcion = "";

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

            if (meta == null)
                return ResponseProvider.error("La meta no existe.", 404);
            else
                return ResponseProvider.success(meta, "Meta obtenida con éxito.", 200);

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener la meta.", 500);
        }
    }

    @POST
    @Validar(entidad = "Meta") 
    // Valida entrada para crear meta (anotación personalizada).
    @Consumes(MediaType.APPLICATION_JSON) 
    // Recibe JSON en el cuerpo.
    @Produces(MediaType.APPLICATION_JSON)
    public static Response createMeta(Meta metaData) {
        try {
            if (metaData.getDescripcion() == null) metaData.setDescripcion(""); 
            // Prevenir null en descripción.

            int idGenerado = 0;
            ResultSet rs = MetaDao.createMeta(metaData); 
            // Inserta nueva meta en BD.

            while (rs.next()) {
                idGenerado = rs.getInt(1); 
                // Obtiene id generado.
                metaData.setId(idGenerado);
            }
            rs.close();

            if (idGenerado == 0)
                return ResponseProvider.error("Error al crear la meta.", 400);
            else
                return ResponseProvider.success(metaData, "Meta creada con éxito.", 200);

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al crear la meta.", 500);
        }
    }

    @PUT
    @Validar(entidad = "Meta")
    @Path("/{id}/usuario/{usuario_id}") 
    // Ruta para actualizar meta de usuario específico.
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public static Response updateMeta(
        @PathParam("id") int id,
        @PathParam("usuario_id") int usuario_id,
        Meta metaData
    ) {
        try {
            if (metaData.getDescripcion() == null) metaData.setDescripcion("");

            Response existente = getMetaById(id); 
            // Verifica que la meta exista.
            if (existente.getStatus() == 404)
                return ResponseProvider.error("La meta no existe.", 404);

            int filasAfectadas = MetaDao.updateMeta(id, usuario_id, metaData);
            // Actualiza meta.

            if (filasAfectadas != 0) {
                metaData.setId(id);
                return ResponseProvider.success(metaData, "Meta actualizada con éxito.", 200);
            } else {
                gestionarMetaCompletada(id);
                // Recalcula campo completada si no se logró actualizar.
                return ResponseProvider.error("Error al actualizar la meta.", 400);
            }

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar la meta.", 500);
        }
    }

    @DELETE 
    @Path("soft/{id}") 
    // Eliminación lógica (soft delete) por id.
    @Produces(MediaType.APPLICATION_JSON)
    public static Response softDeleteMeta(@PathParam("id") int id) {
        try {
            int rowsAffected = MetaDao.softDeleteMeta(id); 
            // Marca meta como eliminada sin borrarla físicamente.

            if (rowsAffected != 0)
                return ResponseProvider.success(null, "Meta eliminada de forma segura.", 200);
            else
                return ResponseProvider.error("Esta meta no existe.", 404);

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al eliminar la meta.", 500);
        }
    }

    @DELETE
    @Path("/{id}/usuario/{usuario_id}") 
    // Eliminación física con validación previa de aportes asociados.
    @Produces(MediaType.APPLICATION_JSON)
    public static Response deleteMeta(
        @PathParam("id") int id,
        @PathParam("usuario_id") int usuario_id
    ) {
        try {
            Response hasAportes = AportesMetaController.getAportesByMetaId(id);
            // Verifica si la meta tiene aportes relacionados.

            if (hasAportes.getStatus() == 200) 
            // Si hay aportes, no permite eliminar y devuelve conflicto 409.
                return ResponseProvider.error("La meta tiene aportes registrados.", 409);

            int filasAfectadas = MetaDao.deleteMeta(id, usuario_id);
            // Elimina la meta físicamente.

            if (filasAfectadas != 0)
                return ResponseProvider.success(null, "Meta eliminada con éxito.", 200);
            else
                return ResponseProvider.error("La meta no existe o no pertenece al usuario.", 404);

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al eliminar la meta.", 500);
        }
    }

    public static void gestionarMetaCompletada(int meta_id) {
        // Método para actualizar el estado "completada" según montos.
        try {
            ResultSet rs = MetaDao.getMetaTotales(meta_id);
            // Obtiene monto objetivo y total aportado.

            while (rs.next()) {
                BigDecimal monto = rs.getBigDecimal("monto");
                BigDecimal total = rs.getBigDecimal("total");

                int comparacion = monto.compareTo(total);

                if (comparacion <= 0) updateCompletada(meta_id, true);
                else updateCompletada(meta_id, false);
            }
        } catch (SQLException e) {
            throw new Error("Error al verificar el estado de la meta");
        }
    }

    public static Response updateCompletada(int id, boolean completada) {
        // Actualiza la columna 'completada' de una meta.
        try {
            Response existente = getMetaById(id);
            if (existente.getStatus() == 404)
                return ResponseProvider.error("La meta no existe.", 404);

            int filasAfectadas = MetaDao.updateCompletada(id, completada);

            if (filasAfectadas != 0)
                return ResponseProvider.success(null, "Campo 'completada' actualizado correctamente.", 200);
            else
                return ResponseProvider.error("Error al actualizar el campo 'completada'.", 400);

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar el campo 'completada'.", 500);
        }
    }
}
