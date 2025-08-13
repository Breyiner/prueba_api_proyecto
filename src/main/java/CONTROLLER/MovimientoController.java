package CONTROLLER; // Define que esta clase pertenece al paquete CONTROLLER, encargado de la lógica de control del sistema.

import MODELO.MovimientoDao; // Importa la clase DAO para operaciones con la base de datos relacionadas con movimientos.
import MODELO.Movimiento; // Importa la entidad que representa un movimiento en el sistema.
import MODELO.CantidadRegistrosDTO; // Importa el DTO para devolver la cantidad total de registros.
import MODELO.MovimientoCalendarioDTO; // Importa el DTO para movimientos resumidos en formato calendario.
import MODELO.MovimientoDetalleDTO; // Importa el DTO para detalles extendidos de movimientos.
import java.sql.ResultSet; // Clase para manejar resultados de consultas SQL.
import java.sql.SQLException; // Maneja excepciones relacionadas con operaciones SQL.
import java.util.ArrayList; // Implementación de lista dinámica para almacenar objetos.
import java.util.List; // Interfaz para colecciones de tipo lista.
import javax.ws.rs.*; // Importa anotaciones JAX-RS para definir endpoints REST (GET, POST, PUT, DELETE, etc.).
import javax.ws.rs.core.MediaType; // Define los tipos de contenido para entradas y salidas (por ejemplo, JSON).
import javax.ws.rs.core.Response; // Permite construir respuestas HTTP con códigos de estado y datos.

@Path("/movimientos") // Define la ruta base para todos los endpoints REST relacionados con movimientos.
public class MovimientoController { // Clase que gestiona las operaciones CRUD y específicas para la entidad Movimiento.

    /**
     * Método para obtener todos los movimientos registrados en la base de datos.
     * Responde a solicitudes GET en /movimientos.
     * @return Response HTTP con la lista de movimientos o un mensaje informativo si no hay datos.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response getMovimientos() {
        List<Movimiento> movimientos = new ArrayList<>(); // Lista para almacenar los movimientos obtenidos.

        try {
            // Llama al método DAO para obtener todos los movimientos desde la base de datos.
            ResultSet rs = MovimientoDao.getMovimientos();

            // Itera sobre el ResultSet para construir objetos Movimiento.
            while (rs.next()) {
                String descripcion = rs.getString("descripcion"); // Obtiene la descripción del movimiento.
                if (descripcion == null) descripcion = ""; // Reemplaza null por cadena vacía para evitar errores.

                Movimiento movimiento = new Movimiento(
                    rs.getInt("id"),                // ID único del movimiento.
                    rs.getInt("usuario_id"),        // ID del usuario propietario.
                    rs.getString("nombre"),         // Nombre del movimiento.
                    rs.getBigDecimal("monto"),      // Monto del movimiento.
                    descripcion,                    // Descripción (validada).
                    rs.getInt("categoria_id")       // ID de la categoría asociada.
                );

                // Asigna la fecha de creación directamente desde la base de datos.
                movimiento.setFecha_creacion(rs.getString("fecha_creacion"));

                movimientos.add(movimiento); // Agrega el movimiento a la lista.
            }

            // Cierra el ResultSet para liberar recursos.
            rs.close();

            // Retorna éxito con código 200, incluso si la lista está vacía, con mensaje apropiado.
            if (!movimientos.isEmpty()) {
                return ResponseProvider.success(movimientos, "movimientos obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.success(movimientos, "No hay movimientos registrados.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener los movimientos", 500);
        }
    }

    /**
     * Método para obtener todos los movimientos filtrados por categoría.
     * Responde a solicitudes GET en /movimientos/categoria/{categoria_id}.
     * @param categoria_id Identificador único de la categoría.
     * @return Response HTTP con la lista de movimientos o un mensaje de error.
     */
    @GET
    @Path("categoria/{categoria_id}") // Ruta con parámetro para categoría.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response getMovimientosByCategoriaId(@PathParam("categoria_id") int categoria_id) {
        List<Movimiento> movimientos = new ArrayList<>(); // Lista para almacenar los movimientos obtenidos.

        try {
            // Llama al método DAO para obtener movimientos por categoría.
            ResultSet rs = MovimientoDao.getMovimientosBYCategoriaId(categoria_id); // Nota: "BY" en mayúscula parece un error tipográfico en el nombre del método.

            // Itera sobre el ResultSet para construir objetos Movimiento.
            while (rs.next()) {
                String descripcion = rs.getString("descripcion"); // Obtiene la descripción.
                if (descripcion == null) descripcion = ""; // Reemplaza null por cadena vacía.

                Movimiento movimiento = new Movimiento(
                    rs.getInt("id"),                // ID único del movimiento.
                    rs.getInt("usuario_id"),        // ID del usuario propietario.
                    rs.getString("nombre"),         // Nombre del movimiento.
                    rs.getBigDecimal("monto"),      // Monto del movimiento.
                    descripcion,                    // Descripción (validada).
                    rs.getInt("categoria_id")       // ID de la categoría asociada.
                );

                // Asigna la fecha de creación.
                movimiento.setFecha_creacion(rs.getString("fecha_creacion"));

                movimientos.add(movimiento); // Agrega el movimiento a la lista.
            }

            // Cierra el ResultSet para liberar recursos.
            rs.close();

            // Retorna éxito con código 200 si hay movimientos, o error 404 si la lista está vacía.
            if (!movimientos.isEmpty()) {
                return ResponseProvider.success(movimientos, "movimientos obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay movimientos registrados.", 404);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener los movimientos", 500);
        }
    }

    /**
     * Método para obtener la cantidad total de movimientos registrados.
     * Responde a solicitudes GET en /movimientos/cantidad.
     * @return Response HTTP con la cantidad de movimientos o un mensaje de error.
     */
    @GET
    @Path("/cantidad") // Ruta específica para obtener la cantidad de movimientos.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response getUsuario() { // Nota: El nombre del método parece incorrecto, debería ser getCantidadMovimientos.
        CantidadRegistrosDTO cantidad = null; // Objeto para almacenar la cantidad de movimientos.

        try {
            // Llama al método DAO para obtener el conteo de movimientos.
            ResultSet respuesta = MovimientoDao.getCantidadMovimientos();

            // Procesa el ResultSet para construir el objeto CantidadRegistrosDTO.
            while (respuesta.next()) {
                cantidad = new CantidadRegistrosDTO(respuesta.getInt("cantidad")); // Obtiene el conteo total.
            }

            // Cierra el ResultSet para liberar recursos.
            respuesta.close();

            // Si no se obtuvo un conteo, retorna error 404.
            if (cantidad == null) {
                return ResponseProvider.error("No se pudo obtener la cantidad de movimientos.", 404);
            } else {
                // Si se obtuvo el conteo, retorna éxito con código 200.
                return ResponseProvider.success(cantidad, "Cantidad de movimientos obtenida con éxito.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener la cantidad de movimientos", 500);
        }
    }

    /**
     * Método para obtener un movimiento específico por su ID.
     * Responde a solicitudes GET en /movimientos/{id}.
     * @param id Identificador único del movimiento.
     * @return Response HTTP con los datos del movimiento o un mensaje de error.
     */
    @GET
    @Path("/{id}") // Ruta con parámetro para buscar un movimiento específico.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response getMovimientoById(@PathParam("id") int id) {
        Movimiento movimiento = null; // Objeto para almacenar el movimiento encontrado.

        try {
            // Llama al método DAO para obtener el movimiento por su ID.
            ResultSet rs = MovimientoDao.getMovimientoById(id);

            // Procesa el ResultSet para construir el objeto Movimiento.
            while (rs.next()) {
                String descripcion = rs.getString("descripcion"); // Obtiene la descripción.
                if (descripcion == null) descripcion = ""; // Reemplaza null por cadena vacía.

                movimiento = new Movimiento(
                    rs.getInt("id"),                // ID único del movimiento.
                    rs.getInt("usuario_id"),        // ID del usuario propietario.
                    rs.getString("nombre"),         // Nombre del movimiento.
                    rs.getBigDecimal("monto"),      // Monto del movimiento.
                    descripcion,                    // Descripción (validada).
                    rs.getInt("categoria_id")       // ID de la categoría asociada.
                );

                // Asigna la fecha de creación.
                movimiento.setFecha_creacion(rs.getString("fecha_creacion"));
            }

            // Cierra el ResultSet para liberar recursos.
            rs.close();

            // Si no se encontró el movimiento, retorna error 404.
            if (movimiento == null) {
                return ResponseProvider.error("El movimiento no existe.", 404);
            } else {
                // Si se encontró, retorna éxito con código 200 y los datos del movimiento.
                return ResponseProvider.success(movimiento, "Movimiento obtenido con éxito.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener el movimiento", 500);
        }
    }

    /**
     * Método para obtener movimientos de un usuario, filtrados por ID de movimiento.
     * Responde a solicitudes GET en /movimientos/{id}/usuario/{usuario_id}.
     * @param id Identificador único del movimiento.
     * @param usuario_id Identificador único del usuario.
     * @return Response HTTP con la lista de movimientos o un mensaje informativo.
     */
    @GET
    @Path("{id}/usuario/{usuario_id}") // Ruta con parámetros para movimiento y usuario.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response getMovimientosByUsuario(@PathParam("id") int id, @PathParam("usuario_id") int usuario_id) {
        List<Movimiento> movimientos = new ArrayList<>(); // Lista para almacenar los movimientos obtenidos.

        try {
            // Llama al método DAO para obtener movimientos por ID y usuario.
            ResultSet rs = MovimientoDao.getMovimientosByUserId(id, usuario_id);

            // Itera sobre el ResultSet para construir objetos Movimiento.
            while (rs.next()) {
                String descripcion = rs.getString("descripcion"); // Obtiene la descripción.
                if (descripcion == null) descripcion = ""; // Reemplaza null por cadena vacía.

                Movimiento movimiento = new Movimiento(
                    rs.getInt("id"),                // ID único del movimiento.
                    rs.getInt("usuario_id"),        // ID del usuario propietario.
                    rs.getString("nombre"),         // Nombre del movimiento.
                    rs.getBigDecimal("monto"),      // Monto del movimiento.
                    descripcion,                    // Descripción (validada).
                    rs.getInt("categoria_id")       // ID de la categoría asociada.
                );

                // Asigna el tipo de movimiento y formatea la fecha de creación.
                movimiento.setTipo_movimiento_id(rs.getInt("tipo_movimiento_id"));
                movimiento.setFecha_creacion(rs.getString("fecha_creacion").substring(0, 10)); // Solo fecha sin hora.

                movimientos.add(movimiento); // Agrega el movimiento a la lista.
            }

            // Cierra el ResultSet para liberar recursos.
            rs.close();

            // Retorna éxito con código 200, incluso si la lista está vacía, con mensaje apropiado.
            if (!movimientos.isEmpty()) {
                return ResponseProvider.success(movimientos, "Movimiento del usuario obtenido con éxito.", 200);
            } else {
                return ResponseProvider.success(movimientos, "No hay movimiento registrado para el usuario.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener el movimiento", 500);
        }
    }

    /**
     * Método para obtener movimientos detallados filtrados por categoría, usuario, tipo de movimiento y mes.
     * Responde a solicitudes GET en /movimientos/categoria/{cat_id}/usuario/{usuario_id}/tipoMovimiento/{tipo_id}/mes/{mes}.
     * @param categoria_id Identificador único de la categoría.
     * @param usuario_id Identificador único del usuario.
     * @param tipo_movimiento_id Identificador del tipo de movimiento.
     * @param mes Mes para el cual se solicitan los movimientos (1-12).
     * @return Response HTTP con la lista de movimientos detallados o un mensaje informativo.
     */
    @GET
    @Path("/categoria/{cat_id}/usuario/{usuario_id}/tipoMovimiento/{tipo_id}/mes/{mes}") // Ruta con múltiples parámetros.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response getMovimientosDetailsPorCategoria(
            @PathParam("cat_id") int categoria_id,
            @PathParam("usuario_id") int usuario_id,
            @PathParam("tipo_id") int tipo_movimiento_id,
            @PathParam("mes") int mes) {
        List<MovimientoDetalleDTO> movimientos = new ArrayList<>(); // Lista para almacenar movimientos detallados.

        try {
            // Llama al método DAO para obtener movimientos detallados según los filtros.
            ResultSet rs = MovimientoDao.getMovimientosDetailsByCategoria(categoria_id, usuario_id, tipo_movimiento_id, mes);

            // Itera sobre el ResultSet para construir objetos MovimientoDetalleDTO.
            while (rs.next()) {
                MovimientoDetalleDTO movimiento = new MovimientoDetalleDTO(
                    rs.getInt("id"),                    // ID único del movimiento.
                    rs.getString("icono"),              // Icono de la categoría.
                    rs.getString("categoria"),          // Nombre de la categoría.
                    rs.getString("color"),              // Color asociado a la categoría.
                    rs.getString("color_bg"),           // Color de fondo de la categoría.
                    rs.getString("nombre"),             // Nombre del movimiento.
                    rs.getString("fecha_creacion").substring(0, 10), // Fecha de creación (sin hora).
                    rs.getBigDecimal("monto")           // Monto del movimiento.
                );

                movimientos.add(movimiento); // Agrega el DTO a la lista.
            }

            // Cierra el ResultSet para liberar recursos.
            rs.close();

            // Retorna éxito con código 200, incluso si la lista está vacía, con mensaje apropiado.
            if (!movimientos.isEmpty()) {
                return ResponseProvider.success(movimientos, "Movimientos obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.success(movimientos, "No hay movimientos registrados.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener los movimientos", 500);
        }
    }

    /**
     * Método para obtener un resumen de movimientos para vista en calendario.
     * Responde a solicitudes GET en /movimientos/usuario/{usuario_id}/tipoMovimiento/{tipo_movimiento_id}/mes/{mes}.
     * @param usuario_id Identificador único del usuario.
     * @param tipo_movimiento_id Identificador del tipo de movimiento.
     * @param mes Mes para el cual se solicitan los movimientos (1-12).
     * @return Response HTTP con la lista de movimientos resumidos o un mensaje informativo.
     */
    @GET
    @Path("usuario/{usuario_id}/tipoMovimiento/{tipo_movimiento_id}/mes/{mes}") // Ruta con parámetros.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response getMovimientosResumidos(
            @PathParam("usuario_id") int usuario_id,
            @PathParam("tipo_movimiento_id") int tipo_movimiento_id,
            @PathParam("mes") int mes) {
        List<MovimientoCalendarioDTO> movimientos = new ArrayList<>(); // Lista para almacenar movimientos resumidos.

        try {
            // Llama al método DAO para obtener movimientos resumidos (pocos campos).
            ResultSet rs = MovimientoDao.getMovimientoResumidos(usuario_id, tipo_movimiento_id, mes);

            // Itera sobre el ResultSet para construir objetos MovimientoCalendarioDTO.
            while (rs.next()) {
                MovimientoCalendarioDTO movimiento = new MovimientoCalendarioDTO(
                    rs.getInt("id"),                    // ID único del movimiento.
                    rs.getString("nombre"),             // Nombre del movimiento.
                    rs.getString("color"),              // Color asociado.
                    rs.getString("fecha_creacion").substring(0, 10) // Fecha de creación (sin hora).
                );
                movimientos.add(movimiento); // Agrega el DTO a la lista.
            }

            // Cierra el ResultSet para liberar recursos.
            rs.close();

            // Retorna éxito con código 200, incluso si la lista está vacía, con mensaje apropiado.
            if (!movimientos.isEmpty()) {
                return ResponseProvider.success(movimientos, "Movimientos obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.success(movimientos, "No hay movimientos registrados.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener los movimientos", 500);
        }
    }

    /**
     * Método para obtener movimientos de un usuario en una fecha específica.
     * Responde a solicitudes GET en /movimientos/usuario/{usuario_id}/tipoMovimiento/{tipo_movimiento_id}/fecha/{fecha}.
     * @param usuario_id Identificador único del usuario.
     * @param tipo_movimiento_id Identificador del tipo de movimiento.
     * @param fecha Fecha exacta para filtrar los movimientos (formato esperado: YYYY-MM-DD).
     * @return Response HTTP con la lista de movimientos detallados o un mensaje informativo.
     */
    @GET
    @Path("usuario/{usuario_id}/tipoMovimiento/{tipo_movimiento_id}/fecha/{fecha}") // Ruta con parámetros.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response getMovimientosByDate(
            @PathParam("usuario_id") int usuario_id,
            @PathParam("tipo_movimiento_id") int tipo_movimiento_id,
            @PathParam("fecha") String fecha) {
        List<MovimientoDetalleDTO> movimientos = new ArrayList<>(); // Lista para almacenar movimientos detallados.

        try {
            // Llama al método DAO para obtener movimientos por fecha, usuario y tipo.
            ResultSet rs = MovimientoDao.getMovimientosByDate(usuario_id, tipo_movimiento_id, fecha);

            // Itera sobre el ResultSet para construir objetos MovimientoDetalleDTO.
            while (rs.next()) {
                MovimientoDetalleDTO movimiento = new MovimientoDetalleDTO(
                    rs.getInt("id"),                    // ID único del movimiento.
                    rs.getString("icono"),              // Icono de la categoría.
                    rs.getString("categoria"),          // Nombre de la categoría.
                    rs.getString("color"),              // Color asociado a la categoría.
                    rs.getString("color_bg"),           // Color de fondo de la categoría.
                    rs.getString("nombre"),             // Nombre del movimiento.
                    rs.getString("fecha_creacion").substring(0, 10), // Fecha de creación (sin hora).
                    rs.getBigDecimal("monto")           // Monto del movimiento.
                );

                movimientos.add(movimiento); // Agrega el DTO a la lista.
            }

            // Cierra el ResultSet para liberar recursos.
            rs.close();

            // Retorna éxito con código 200, incluso si la lista está vacía, con mensaje apropiado.
            if (!movimientos.isEmpty()) {
                return ResponseProvider.success(movimientos, "Movimientos obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.success(movimientos, "No hay movimientos registrados.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener los movimientos", 500);
        }
    }

    /**
     * Método para crear un nuevo movimiento.
     * Responde a solicitudes POST en /movimientos.
     * @param movimientoData Objeto Movimiento con los datos del nuevo movimiento.
     * @return Response HTTP indicando éxito o error.
     */
    @POST
    @Validar(entidad = "Movimiento") // Aplica validaciones definidas para la entidad Movimiento.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    @Consumes(MediaType.APPLICATION_JSON) // Indica que recibe datos en formato JSON.
    public static Response createMovimiento(Movimiento movimientoData) {
        ResultSet rs = null; // Variable para almacenar el resultado de la inserción.

        try {
            int idGenerado = 0; // Variable para almacenar el ID generado por la base de datos.

            // Decide qué método DAO usar según si se proporcionó una fecha de creación.
            if (movimientoData.getFecha_creacion() == null) {
                rs = MovimientoDao.createMovimiento(movimientoData); // Usa la fecha actual en la base de datos.
            } else {
                rs = MovimientoDao.createMovimientoDate(movimientoData); // Usa la fecha especificada.
            }

            // Procesa el ResultSet para obtener el ID generado tras la inserción.
            while (rs.next()) {
                idGenerado = rs.getInt(1); // Obtiene el ID del primer campo.
                movimientoData.setId(idGenerado); // Asigna el ID al objeto movimiento.
            }

            // Cierra el ResultSet para liberar recursos.
            rs.close();

            // Si no se generó un ID (inserción fallida), retorna error 400.
            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear el movimiento.", 400);
            } else {
                // Si la inserción fue exitosa, retorna código 200.
                return ResponseProvider.success(null, "Movimiento creado con éxito.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al crear el movimiento.", 500);
        }
    }

    /**
     * Método para actualizar un movimiento existente.
     * Responde a solicitudes PUT en /movimientos/{id}/usuario/{usuario_id}.
     * @param id Identificador único del movimiento.
     * @param usuario_id Identificador único del usuario.
     * @param movimientoData Objeto Movimiento con los nuevos datos.
     * @return Response HTTP indicando éxito o error.
     */
    @PUT
    @Validar(entidad = "Movimiento") // Aplica validaciones definidas para la entidad Movimiento.
    @Path("/{id}/usuario/{usuario_id}") // Ruta con parámetros para movimiento y usuario.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    @Consumes(MediaType.APPLICATION_JSON) // Indica que recibe datos en formato JSON.
    public static Response updateMovimiento(
            @PathParam("id") int id,
            @PathParam("usuario_id") int usuario_id,
            Movimiento movimientoData) {
        try {
            // Verifica si el movimiento existe llamando al método getMovimientoById.
            Response existente = getMovimientoById(id);

            // Si el movimiento no existe, retorna error 404.
            if (existente.getStatus() == 404) {
                return ResponseProvider.error("Este movimiento no existe.", 404);
            }

            // Llama al método DAO para actualizar los datos del movimiento.
            int filasAfectadas = MovimientoDao.updateMovimiento(id, usuario_id, movimientoData);

            // Si la actualización fue exitosa, retorna código 200.
            if (filasAfectadas != 0) {
                movimientoData.setId(id); // Asegura que el ID se mantenga en el objeto.
                return ResponseProvider.success(null, "Movimiento actualizado con éxito.", 200);
            } else {
                // Si no se afectó ningún registro, retorna error 400.
                return ResponseProvider.error("Error al actualizar el movimiento.", 400);
            }

        } catch (Exception e) {
            // Captura errores inesperados y retorna error 500.
            return ResponseProvider.error("Error interno al actualizar el movimiento.", 500);
        }
    }

    /**
     * Método para realizar una eliminación lógica (soft delete) de un movimiento.
     * Responde a solicitudes DELETE en /movimientos/soft/{id}.
     * @param id Identificador único del movimiento.
     * @return Response HTTP indicando éxito o error.
     */
    @DELETE
    @Path("soft/{id}") // Ruta para eliminación lógica de un movimiento.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response softDeleteMovimiento(@PathParam("id") int id) {
        try {
            // Llama al método DAO para marcar el movimiento como eliminado (soft delete).
            int rowsAffected = MovimientoDao.softDeleteMovimiento(id);

            // Si la eliminación lógica fue exitosa, retorna código 200.
            if (rowsAffected != 0) {
                return ResponseProvider.success(null, "Movimiento eliminado de forma segura.", 200);
            } else {
                // Si el movimiento no existe, retorna error 404.
                return ResponseProvider.error("Este movimiento no existe.", 404);
            }

        } catch (Exception e) {
            // Captura errores inesperados y retorna error 500.
            return ResponseProvider.error("Error interno al eliminar el movimiento.", 500);
        }
    }

    /**
     * Método para eliminar físicamente un movimiento de la base de datos.
     * Responde a solicitudes DELETE en /movimientos/{id}/usuario/{usuario_id}.
     * @param id Identificador único del movimiento.
     * @param usuario_id Identificador único del usuario.
     * @return Response HTTP indicando éxito o error.
     */
    @DELETE
    @Path("/{id}/usuario/{usuario_id}") // Ruta con parámetros para movimiento y usuario.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public static Response deleteMovimiento(
            @PathParam("id") int id,
            @PathParam("usuario_id") int usuario_id) {
        try {
            // Llama al método DAO para eliminar físicamente el movimiento.
            int filasAfectadas = MovimientoDao.deleteMovimiento(id, usuario_id);

            // Si la eliminación fue exitosa, retorna código 200.
            if (filasAfectadas != 0) {
                return ResponseProvider.success(null, "Movimiento eliminado con éxito.", 200);
            } else {
                // Si el movimiento no existe o no pertenece al usuario, retorna error 404.
                return ResponseProvider.error("Este movimiento no existe.", 404);
            }

        } catch (Exception e) {
            // Captura errores inesperados y retorna error 500.
            return ResponseProvider.error("Error interno al eliminar el movimiento.", 500);
        }
    }
}