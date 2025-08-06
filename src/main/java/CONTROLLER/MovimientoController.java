package CONTROLLER;  // Paquete que contiene los controladores REST para la entidad Movimiento

// Importamos el DAO para acceder a datos de Movimientos
import MODELO.MovimientoDao;
// Importamos el modelo Movimiento
import MODELO.Movimiento;
// DTO para devolver la cantidad total de registros
import MODELO.CantidadRegistrosDTO;
// DTO para movimientos resumidos en formato calendario
import MODELO.MovimientoCalendarioDTO;
// DTO para detalles extendidos de movimientos
import MODELO.MovimientoDetalleDTO;

import java.sql.ResultSet;       // Clase para manejar resultados de consultas SQL
import java.sql.SQLException;   // Excepción para errores SQL
import java.util.ArrayList;     // Implementación concreta de List
import java.util.List;          // Interfaz para listas

// Librerías para la creación de servicios REST con JAX-RS
import javax.ws.rs.*;             
import javax.ws.rs.core.MediaType;  // Define tipo MIME para respuestas (JSON en este caso)
import javax.ws.rs.core.Response;   // Construcción y envío de respuestas HTTP

// Define la ruta base para todos los métodos del controlador
@Path("/movimientos")
public class MovimientoController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)  // Responde en formato JSON
    // Endpoint para obtener todos los movimientos registrados en la base de datos
    public static Response getMovimientos() {
        List<Movimiento> movimientos = new ArrayList<>();  // Lista donde se almacenarán los movimientos obtenidos

        try {
            // Invoca el método DAO para obtener todos los movimientos
            ResultSet rs = MovimientoDao.getMovimientos();

            // Recorre el conjunto de resultados fila por fila
            while (rs.next()) {
                // Extrae la descripción, si es null asigna cadena vacía para evitar NullPointerException
                String descripcion = rs.getString("descripcion");
                if (descripcion == null) descripcion = "";

                // Crea un objeto Movimiento con los datos obtenidos en la fila actual
                Movimiento movimiento = new Movimiento(
                    rs.getInt("id"),                 // ID único del movimiento
                    rs.getInt("usuario_id"),         // ID del usuario propietario del movimiento
                    rs.getString("nombre"),          // Nombre o título del movimiento
                    rs.getBigDecimal("monto"),       // Monto asociado al movimiento
                    descripcion,                    // Descripción (puede estar vacía)
                    rs.getInt("categoria_id")        // ID de la categoría del movimiento
                );

                // Asigna la fecha de creación directamente desde la base de datos
                movimiento.setFecha_creacion(rs.getString("fecha_creacion"));

                // Agrega el objeto movimiento a la lista de resultados
                movimientos.add(movimiento);
            }

            // Cierra el ResultSet para liberar recursos
            rs.close();

            // Retorna la lista con un mensaje y código HTTP 200 OK
            if (!movimientos.isEmpty()) {
                return ResponseProvider.success(movimientos, "movimientos obtenidos con éxito.", 200);
            } else {
                // Aunque la lista esté vacía, se devuelve con estado 200 para indicar que no hay errores
                return ResponseProvider.success(movimientos, "No hay movimientos registrados.", 200);
            }

        } catch (SQLException e) {
            // En caso de error en la base de datos, se retorna error HTTP 500 (Error interno)
            return ResponseProvider.error("Error interno al obtener los movimientos", 500);
        }
    }

    @GET
    @Path("/cantidad") // Extiende la ruta base, accediendo a /movimientos/cantidad
    @Produces(MediaType.APPLICATION_JSON) // Responde en JSON
    // Endpoint para obtener la cantidad total de movimientos
    public static Response getUsuario() {
        CantidadRegistrosDTO cantidad = null;  // Variable para almacenar el DTO con la cantidad

        try {
            // Ejecuta la consulta que devuelve el conteo total de movimientos
            ResultSet respuesta = MovimientoDao.getCantidadMovimientos();

            // Procesa el resultado (debe tener una única fila con la cantidad)
            while (respuesta.next()) {
                cantidad = new CantidadRegistrosDTO(respuesta.getInt("cantidad"));
            }

            // Cierra el ResultSet para evitar fugas de memoria
            respuesta.close();

            // Si no se obtuvo resultado, responde con error 404 (No encontrado)
            if (cantidad == null) {
                return ResponseProvider.error("No se pudo obtener la cantidad de movimientos.", 404);
            } else {
                // Retorna la cantidad con mensaje y código HTTP 200 OK
                return ResponseProvider.success(cantidad, "Cantidad de movimientos obtenida con éxito.", 200);
            }

        } catch (SQLException e) {
            // Error interno de base de datos -> respuesta con código 500
            return ResponseProvider.error("Error interno al obtener la cantidad de movimientos", 500);
        }
    }

    @GET
    @Path("/{id}")  // Ruta con parámetro ID para obtener movimiento específico
    @Produces(MediaType.APPLICATION_JSON)
    // Obtiene un movimiento específico mediante su ID
    public static Response getMovimientoById(@PathParam("id") int id) {
        Movimiento movimiento = null; // Variable para almacenar el movimiento encontrado

        try {
            // Ejecuta la consulta para obtener movimiento por ID
            ResultSet rs = MovimientoDao.getMovimientoById(id);

            // Si la consulta devuelve una fila, construye el objeto Movimiento
            while (rs.next()) {
                // Extrae la descripción y la ajusta para evitar valores nulos
                String descripcion = rs.getString("descripcion");
                if (descripcion == null) descripcion = "";

                // Construye el objeto con los datos recuperados
                movimiento = new Movimiento(
                    rs.getInt("id"),
                    rs.getInt("usuario_id"),
                    rs.getString("nombre"),
                    rs.getBigDecimal("monto"),
                    descripcion,
                    rs.getInt("categoria_id")
                );

                // Establece la fecha de creación
                movimiento.setFecha_creacion(rs.getString("fecha_creacion"));
            }

            // Cierra el ResultSet
            rs.close();

            // Si no se encontró el movimiento, devuelve error 404
            if (movimiento == null) {
                return ResponseProvider.error("El movimiento no existe.", 404);
            } else {
                // Si se encontró, devuelve el movimiento con código 200 OK
                return ResponseProvider.success(movimiento, "Movimiento obtenido con éxito.", 200);
            }

        } catch (SQLException e) {
            // En caso de error interno, responde error 500
            return ResponseProvider.error("Error interno al obtener el movimiento", 500);
        }
    }

    @GET
    @Path("{id}/usuario/{usuario_id}") // Ruta con dos parámetros: movimiento y usuario
    @Produces(MediaType.APPLICATION_JSON)
    // Obtiene movimientos filtrados por ID de movimiento y usuario
    public static Response getMovimientosByUsuario(@PathParam("id") int id, @PathParam("usuario_id") int usuario_id) {
        List<Movimiento> movimientos = new ArrayList<>(); // Lista para almacenar resultados

        try {
            // Ejecuta consulta para obtener movimientos según movimiento y usuario
            ResultSet rs = MovimientoDao.getMovimientosByUserId(id, usuario_id);

            // Itera sobre resultados para crear objetos Movimiento
            while (rs.next()) {
                // Evita null en descripción
                String descripcion = rs.getString("descripcion");
                if (descripcion == null) descripcion = "";

                Movimiento movimiento = new Movimiento(
                    rs.getInt("id"),
                    rs.getInt("usuario_id"),
                    rs.getString("nombre"),
                    rs.getBigDecimal("monto"),
                    descripcion,
                    rs.getInt("categoria_id")
                );

                // Asigna tipo de movimiento y fecha (solo fecha sin hora)
                movimiento.setTipo_movimiento_id(rs.getInt("tipo_movimiento_id"));
                movimiento.setFecha_creacion(rs.getString("fecha_creacion").substring(0, 10));

                // Añade a la lista
                movimientos.add(movimiento);
            }

            // Cierra recursos
            rs.close();

            // Retorna resultados o mensaje indicando que no hay movimientos para el usuario
            if (!movimientos.isEmpty()) {
                return ResponseProvider.success(movimientos, "Movimiento del usuario obtenido con éxito.", 200);
            } else {
                return ResponseProvider.success(movimientos, "No hay movimiento registrado para el usuario.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener el movimiento", 500);
        }
    }

    @GET
    @Path("/categoria/{cat_id}/usuario/{usuario_id}/tipoMovimiento/{tipo_id}/mes/{mes}")
    @Produces(MediaType.APPLICATION_JSON)
    // Obtiene movimientos filtrados por categoría, usuario, tipo de movimiento y mes
    public static Response getMovimientosPorCategoria(
        @PathParam("cat_id") int categoria_id,
        @PathParam("usuario_id") int usuario_id,
        @PathParam("tipo_id") int tipo_movimiento_id,
        @PathParam("mes") int mes
    ) {
        List<MovimientoDetalleDTO> movimientos = new ArrayList<>(); // Lista para DTOs detallados

        try {
            // Ejecuta la consulta filtrada según los parámetros recibidos
            ResultSet rs = MovimientoDao.getMovimientosByCategoria(categoria_id, usuario_id, tipo_movimiento_id, mes);

            // Construye objetos DTO para cada fila del resultado
            while(rs.next()) {
                MovimientoDetalleDTO movimiento = new MovimientoDetalleDTO(
                    rs.getInt("id"),
                    rs.getString("icono"),
                    rs.getString("categoria"),
                    rs.getString("color"),
                    rs.getString("color_bg"),
                    rs.getString("nombre"),
                    rs.getString("fecha_creacion").substring(0, 10), // Solo fecha
                    rs.getBigDecimal("monto")
                );

                movimientos.add(movimiento);
            }

            // Cierra ResultSet
            rs.close();

            // Devuelve la lista con mensaje adecuado
            if (!movimientos.isEmpty()) {
                return ResponseProvider.success(movimientos, "Movimientos obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.success(movimientos, "No hay movimientos registrados.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los movimientos", 500);
        }
    }

    @GET
    @Path("usuario/{usuario_id}/tipoMovimiento/{tipo_movimiento_id}/mes/{mes}")
    @Produces(MediaType.APPLICATION_JSON)
    // Obtiene un resumen simple de movimientos para vista en calendario
    public static Response getMovimientosResumidos(
        @PathParam("usuario_id") int usuario_id,
        @PathParam("tipo_movimiento_id") int tipo_movimiento_id,
        @PathParam("mes") int mes)
    {
        List<MovimientoCalendarioDTO> movimientos = new ArrayList<>();  // Lista para DTOs simplificados

        try {
            // Ejecuta consulta que devuelve pocos campos (id, nombre, color, fecha)
            ResultSet rs = MovimientoDao.getMovimientoResumidos(usuario_id, tipo_movimiento_id, mes);

            // Construye DTO para cada resultado
            while(rs.next()) {
                MovimientoCalendarioDTO movimiento = new MovimientoCalendarioDTO(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("color"),
                    rs.getString("fecha_creacion").substring(0, 10) // Solo fecha sin hora
                );
                movimientos.add(movimiento);
            }

            rs.close();

            // Devuelve los movimientos encontrados o mensaje vacío
            if (!movimientos.isEmpty()) {
                return ResponseProvider.success(movimientos, "Movimientos obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.success(movimientos, "No hay movimientos registrados.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los movimientos", 500);
        }
    }

    @GET
    @Path("usuario/{usuario_id}/tipoMovimiento/{tipo_movimiento_id}/fecha/{fecha}")
    @Produces(MediaType.APPLICATION_JSON)
    // Obtiene movimientos para un usuario, tipo y fecha exacta
    public static Response getMovimientosByDate(
        @PathParam("usuario_id") int usuario_id,
        @PathParam("tipo_movimiento_id") int tipo_movimiento_id,
        @PathParam("fecha") String fecha)
    {
        List<MovimientoDetalleDTO> movimientos = new ArrayList<>(); // Lista para movimientos detallados

        try {
            // Consulta movimientos según usuario, tipo y fecha exacta
            ResultSet rs = MovimientoDao.getMovimientosByDate(usuario_id, tipo_movimiento_id, fecha);

            // Construye DTO para cada resultado
            while(rs.next()) {
                MovimientoDetalleDTO movimiento = new MovimientoDetalleDTO(
                    rs.getInt("id"),
                    rs.getString("icono"),
                    rs.getString("categoria"),
                    rs.getString("color"),
                    rs.getString("color_bg"),
                    rs.getString("nombre"),
                    rs.getString("fecha_creacion").substring(0, 10),
                    rs.getBigDecimal("monto")
                );

                movimientos.add(movimiento);
            }

            rs.close();

            // Devuelve resultados o mensaje de lista vacía
            if (!movimientos.isEmpty()) {
                return ResponseProvider.success(movimientos, "Movimientos obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.success(movimientos, "No hay movimientos registrados.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los movimientos", 500);
        }
    }

    @POST
    @Validar(entidad = "Movimiento") // Anotación personalizada para validar entrada de Movimiento
    @Produces(MediaType.APPLICATION_JSON)  // Responde JSON
    @Consumes(MediaType.APPLICATION_JSON)  // Recibe JSON
    // Crea un nuevo movimiento, con o sin fecha explícita
    public static Response createMovimiento(Movimiento movimientoData) {
        ResultSet rs = null;  // Para obtener resultado de la inserción

        try {
            int idGenerado = 0; // Guardará el ID generado por la BD

            if(movimientoData.getFecha_creacion() == null) {
                // Si no se envía fecha, se usa fecha actual en BD
                rs = MovimientoDao.createMovimiento(movimientoData);
            } else {
                // Si se envía fecha, se inserta con la fecha especificada
                rs = MovimientoDao.createMovimientoDate(movimientoData);
            }

            // Obtiene el ID generado tras la inserción
            while (rs.next()) {
                idGenerado = rs.getInt(1);
                movimientoData.setId(idGenerado);
            }

            rs.close();

            // Si no se generó ID, hubo un error en la creación
            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear el movimiento.", 400);
            } else {
                return ResponseProvider.success(null, "Movimiento creado con éxito.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al crear el movimiento.", 500);
        }
    }

    @PUT
    @Validar(entidad = "Movimiento")  // Valida entrada Movimiento
    @Path("/{id}/usuario/{usuario_id}") // Ruta para actualizar un movimiento específico
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    // Actualiza un movimiento existente, previo chequeo que exista
    public static Response updateMovimiento(
        @PathParam("id") int id,
        @PathParam("usuario_id") int usuario_id,
        Movimiento movimientoData
    ) {
        try {
            // Verifica si el movimiento existe
            Response existente = getMovimientoById(id);

            if (existente.getStatus() == 404) // No existe
                return ResponseProvider.error("Este movimiento no existe.", 404);

            // Actualiza el movimiento en la base de datos
            int filasAfectadas = MovimientoDao.updateMovimiento(id, usuario_id, movimientoData);

            if (filasAfectadas != 0) {
                movimientoData.setId(id);
                return ResponseProvider.success(null, "Movimiento actualizado con éxito.", 200);
            } else {
                return ResponseProvider.error("Error al actualizar el movimiento.", 400);
            }

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar el movimiento.", 500);
        }
    }

    @DELETE
    @Path("soft/{id}") // Ruta para eliminación lógica (soft delete)
    @Produces(MediaType.APPLICATION_JSON)
    // Elimina un movimiento de forma lógica (no lo borra físicamente)
    public static Response softDeleteMovimiento(@PathParam("id") int id) {
        try {
            // Marca como eliminado el movimiento
            int rowsAffected = MovimientoDao.softDeleteMovimiento(id);

            if (rowsAffected != 0) {
                return ResponseProvider.success(null, "Movimiento eliminado de forma segura.", 200);
            } else {
                return ResponseProvider.error("Este movimiento no existe.", 404);
            }

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al eliminar el movimiento.", 500);
        }
    }

    @DELETE
    @Path("/{id}/usuario/{usuario_id}") // Ruta para eliminación física definitiva
    @Produces(MediaType.APPLICATION_JSON)
    // Elimina físicamente un movimiento, validando que pertenezca al usuario
    public static Response deleteMovimiento(
        @PathParam("id") int id,
        @PathParam("usuario_id") int usuario_id
    ) {
        try {
            // Ejecuta eliminación física en BD
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
