package CONTROLLER; // Define que esta clase pertenece al paquete CONTROLLER, encargado de la lógica de control del sistema.

import MODELO.DashboardDao; // Importa la clase DAO para consultas relacionadas con el dashboard financiero.
import MODELO.ResumenCardDTO; // Importa el DTO para resúmenes en formato de tarjeta (movimientos y metas).
import MODELO.ResumenCategoriasDTO; // Importa el DTO para resúmenes detallados por categorías.
import MODELO.ResumenMetasDTO; // Importa el DTO para resúmenes detallados de metas.
import javax.ws.rs.*; // Importa anotaciones JAX-RS para definir endpoints REST (GET, Path, etc.).
import javax.ws.rs.core.MediaType; // Define los tipos de contenido para entradas y salidas (por ejemplo, JSON).
import javax.ws.rs.core.Response; // Permite construir respuestas HTTP con códigos de estado y datos.
import java.math.BigDecimal; // Clase para manejar cálculos precisos con números decimales.
import java.sql.ResultSet; // Clase para manejar resultados de consultas SQL.
import java.sql.SQLException; // Maneja excepciones relacionadas con operaciones SQL.
import java.util.ArrayList; // Implementación de lista dinámica para almacenar objetos.
import java.util.List; // Interfaz para colecciones de tipo lista.

@Path("/dashboard") // Define la ruta base para todos los endpoints REST relacionados con el dashboard.
public class DashboardController { // Clase que gestiona las operaciones para mostrar datos financieros en el dashboard.

    /**
     * Método para obtener un resumen de movimientos financieros de un usuario en un mes específico.
     * Responde a solicitudes GET en /dashboard/movimientos/usuario/{usuario_id}/mes/{mes}.
     * @param usuario_id Identificador único del usuario.
     * @param mes Mes para el cual se solicita el resumen (1-12).
     * @return Response HTTP con la lista de resúmenes en formato tarjeta o un mensaje de error.
     */
    @GET
    @Path("/movimientos/usuario/{usuario_id}/mes/{mes}") // Ruta con parámetros para usuario y mes.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public Response getResumenMovimientos(
            @PathParam("usuario_id") int usuario_id, // ID del usuario.
            @PathParam("mes") int mes) { // Mes consultado.
        List<ResumenCardDTO> resumen = new ArrayList<>(); // Lista para almacenar tarjetas de resumen.

        try {
            // Llama al método DAO para obtener el resumen de movimientos del usuario en el mes especificado.
            ResultSet rs = DashboardDao.getResumenMovimientos(usuario_id, mes);

            // Itera sobre el ResultSet para construir objetos ResumenCardDTO.
            while (rs.next()) {
                ResumenCardDTO card = new ResumenCardDTO(
                    rs.getString("icono"),      // Icono del tipo de movimiento (ej. ícono de ingresos o gastos).
                    rs.getString("color"),      // Color asociado al tipo de movimiento.
                    rs.getString("nombre"),     // Nombre del tipo de movimiento (ej. Ingresos, Gastos).
                    rs.getBigDecimal("total"),  // Total acumulado de los movimientos.
                    "movimiento"                // Identificador del tipo de resumen.
                );
                resumen.add(card); // Agrega la tarjeta a la lista.
            }

            // Cierra el ResultSet para liberar recursos.
            rs.close();

            // Si la lista tiene resúmenes, retorna éxito con código 200.
            if (!resumen.isEmpty()) {
                return ResponseProvider.success(resumen, "Resumen de movimientos obtenido con éxito.", 200);
            } else {
                // Si no hay movimientos, retorna error 404 con mensaje informativo.
                return ResponseProvider.error("No hay movimientos registrados para este mes.", 404);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener el resumen de movimientos.", 500);
        }
    }

    /**
     * Método para obtener un resumen de aportes a metas de un usuario en un mes específico.
     * Responde a solicitudes GET en /dashboard/metas/usuario/{usuario_id}/mes/{mes}.
     * @param usuario_id Identificador único del usuario.
     * @param mes Mes para el cual se solicita el resumen (1-12).
     * @return Response HTTP con la lista de resúmenes en formato tarjeta o un mensaje de error.
     */
    @GET
    @Path("/metas/usuario/{usuario_id}/mes/{mes}") // Ruta con parámetros para usuario y mes.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public Response getResumenMetas(
            @PathParam("usuario_id") int usuario_id, // ID del usuario.
            @PathParam("mes") int mes) { // Mes consultado.
        List<ResumenCardDTO> resumen = new ArrayList<>(); // Lista para almacenar tarjetas de resumen.

        try {
            // Llama al método DAO para obtener el resumen de metas del usuario en el mes especificado.
            ResultSet rs = DashboardDao.getResumenMetas(usuario_id, mes);

            // Itera sobre el ResultSet para construir objetos ResumenCardDTO.
            while (rs.next()) {
                ResumenCardDTO card = new ResumenCardDTO(
                    rs.getString("icono"),      // Icono de la meta.
                    rs.getString("color"),      // Color asociado a la meta.
                    rs.getString("nombre"),     // Nombre de la meta.
                    rs.getBigDecimal("total"),  // Total aportado a las metas.
                    "meta"                      // Identificador del tipo de resumen.
                );
                resumen.add(card); // Agrega la tarjeta a la lista.
            }

            // Cierra el ResultSet para liberar recursos.
            rs.close();

            // Si la lista tiene resúmenes, retorna éxito con código 200.
            if (!resumen.isEmpty()) {
                return ResponseProvider.success(resumen, "Resumen de metas obtenido con éxito.", 200);
            } else {
                // Si no hay aportes a metas, retorna error 404 con mensaje informativo.
                return ResponseProvider.error("No hay aportes a metas registrados para este mes.", 404);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener el resumen de metas.", 500);
        }
    }

    /**
     * Método para obtener un resumen completo (movimientos y metas) con balance neto.
     * Responde a solicitudes GET en /dashboard/completo/usuario/{usuario_id}/mes/{mes}.
     * @param usuarioId Identificador único del usuario.
     * @param mes Mes para el cual se solicita el resumen (1-12).
     * @return Response HTTP con el resumen completo, incluyendo balance, o un mensaje de error.
     */
    @GET
    @Path("/completo/usuario/{usuario_id}/mes/{mes}") // Ruta con parámetros para usuario y mes.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public Response getResumenCompleto(
            @PathParam("usuario_id") int usuarioId, // ID del usuario.
            @PathParam("mes") int mes) { // Mes consultado.
        List<ResumenCardDTO> resumenCompleto = new ArrayList<>(); // Lista para almacenar el resumen completo.

        try {
            // Obtiene el resumen de movimientos del usuario para el mes especificado.
            ResultSet rsMovimientos = DashboardDao.getResumenMovimientos(usuarioId, mes);
            while (rsMovimientos.next()) {
                ResumenCardDTO card = new ResumenCardDTO(
                    rsMovimientos.getString("icono"),      // Icono del tipo de movimiento.
                    rsMovimientos.getString("color"),      // Color asociado.
                    rsMovimientos.getString("nombre"),     // Nombre del tipo de movimiento.
                    rsMovimientos.getBigDecimal("total"),   // Total de movimientos.
                    "movimiento"                           // Identificador del tipo.
                );
                resumenCompleto.add(card); // Agrega la tarjeta a la lista.
            }
            rsMovimientos.close(); // Cierra el ResultSet de movimientos.

            // Obtiene el resumen de metas del usuario para el mes especificado.
            ResultSet rsMetas = DashboardDao.getResumenMetas(usuarioId, mes);
            while (rsMetas.next()) {
                ResumenCardDTO card = new ResumenCardDTO(
                    rsMetas.getString("icono"),      // Icono de la meta.
                    rsMetas.getString("color"),      // Color asociado.
                    rsMetas.getString("nombre"),     // Nombre de la meta.
                    rsMetas.getBigDecimal("total"),  // Total aportado a metas.
                    "meta"                           // Identificador del tipo.
                );
                resumenCompleto.add(card); // Agrega la tarjeta a la lista.
            }
            rsMetas.close(); // Cierra el ResultSet de metas.

            // Calcula el balance neto si hay datos en el resumen.
            if (!resumenCompleto.isEmpty()) {
                // Toma el primer total como saldo inicial (por ejemplo, ingresos).
                BigDecimal balance = resumenCompleto.get(0).getTotal();

                // Resta los totales de los demás elementos (gastos y metas) para obtener el balance neto.
                for (int i = 1; i < resumenCompleto.size(); i++) {
                    balance = balance.subtract(resumenCompleto.get(i).getTotal());
                }

                // Crea una tarjeta resumen para el balance neto.
                ResumenCardDTO card = new ResumenCardDTO(
                    "ri-wallet-3-line",    // Icono predeterminado para el balance.
                    "#3367d6",             // Color azul para la tarjeta de balance.
                    "Balance",             // Nombre de la tarjeta.
                    balance,               // Valor del balance calculado.
                    "balance"              // Identificador del tipo.
                );
                resumenCompleto.add(0, card); // Inserta la tarjeta de balance al inicio de la lista.
            }

            // Si la lista tiene resúmenes, retorna éxito con código 200.
            if (!resumenCompleto.isEmpty()) {
                return ResponseProvider.success(resumenCompleto, "Resumen completo obtenido con éxito.", 200);
            } else {
                // Si no hay datos, retorna error 404 con mensaje informativo.
                return ResponseProvider.error("No hay datos registrados para este mes.", 404);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error interno al obtener el resumen completo.", 500);
        }
    }

    /**
     * Método para obtener un resumen detallado de movimientos por categoría.
     * Responde a solicitudes GET en /dashboard/categorias/usuario/{usuario_id}/mes/{mes}/tipo/{tipo_id}.
     * @param usuario_id Identificador único del usuario.
     * @param mes Mes para el cual se solicita el resumen (1-12).
     * @param tipo_id Identificador del tipo de movimiento (por ejemplo, ingresos, gastos).
     * @return Response HTTP con el resumen detallado de categorías o un mensaje de error.
     */
    @GET
    @Path("/categorias/usuario/{usuario_id}/mes/{mes}/tipo/{tipo_id}") // Ruta con parámetros para usuario, mes y tipo.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public Response getResumenCategoriasDetalle(
            @PathParam("usuario_id") int usuario_id, // ID del usuario.
            @PathParam("mes") int mes,              // Mes consultado.
            @PathParam("tipo_id") int tipo_id) {    // ID del tipo de movimiento.
        try {
            // Llama al método DAO para obtener el resumen detallado de categorías.
            ResultSet rs = DashboardDao.getResumenCategoriasDetalle(usuario_id, mes, tipo_id);
            List<ResumenCategoriasDTO> lista = new ArrayList<>(); // Lista para almacenar el resumen.

            // Itera sobre el ResultSet para construir objetos ResumenCategoriasDTO.
            while (rs.next()) {
                ResumenCategoriasDTO dto = new ResumenCategoriasDTO(
                    rs.getInt("id"),                    // ID de la categoría.
                    rs.getInt("tipo_movimiento_id"),    // ID del tipo de movimiento.
                    rs.getString("icono"),              // Icono de la categoría.
                    rs.getString("nombre"),             // Nombre de la categoría.
                    rs.getString("color"),              // Color asociado a la categoría.
                    rs.getString("color_bg"),           // Color de fondo para la categoría.
                    rs.getInt("cantidad"),              // Cantidad de movimientos en la categoría.
                    rs.getBigDecimal("total")           // Total acumulado de movimientos.
                );
                lista.add(dto); // Agrega el DTO a la lista.
            }

            // Cierra el ResultSet para liberar recursos.
            rs.close();

            // Retorna éxito con código 200, incluso si la lista está vacía (respuesta válida).
            return ResponseProvider.success(lista, "Resumen detallado de categorías obtenido con éxito.", 200);

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error al obtener resumen detallado de categorías", 500);
        }
    }

    /**
     * Método para obtener un resumen detallado de aportes a metas de un usuario en un mes.
     * Responde a solicitudes GET en /dashboard/metas/detalle/usuario/{usuario_id}/mes/{mes}.
     * @param usuarioId Identificador único del usuario.
     * @param mes Mes para el cual se solicita el resumen (1-12).
     * @return Response HTTP con el resumen detallado de metas o un mensaje de error.
     */
    @GET
    @Path("/metas/detalle/usuario/{usuario_id}/mes/{mes}") // Ruta con parámetros para usuario y mes.
    @Produces(MediaType.APPLICATION_JSON) // Especifica que la respuesta será en formato JSON.
    public Response getResumenMetasDetalle(
            @PathParam("usuario_id") int usuarioId, // ID del usuario.
            @PathParam("mes") int mes) { // Mes consultado.
        try {
            // Llama al método DAO para obtener el resumen detallado de metas.
            ResultSet rs = DashboardDao.getResumenMetasDetalle(usuarioId, mes);
            List<ResumenMetasDTO> lista = new ArrayList<>(); // Lista para almacenar el resumen.

            // Itera sobre el ResultSet para construir objetos ResumenMetasDTO.
            while (rs.next()) {
                ResumenMetasDTO dto = new ResumenMetasDTO(
                    rs.getInt("id"),                    // ID de la meta.
                    rs.getInt("tipo_movimiento_id"),    // ID del tipo de movimiento (generalmente 3 para metas).
                    rs.getString("icono"),              // Icono de la meta.
                    rs.getString("color"),              // Color asociado a la meta.
                    rs.getString("color_bg"),           // Color de fondo para la meta.
                    rs.getString("nombre"),             // Nombre de la meta.
                    rs.getInt("cantidad"),              // Cantidad de aportes a la meta.
                    rs.getBigDecimal("total")           // Total aportado a la meta.
                );
                lista.add(dto); // Agrega el DTO a la lista.
            }

            // Cierra el ResultSet para liberar recursos.
            rs.close();

            // Retorna éxito con código 200, incluso si la lista está vacía (respuesta válida).
            return ResponseProvider.success(lista, "Resumen detallado de metas obtenido con éxito.", 200);

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno.
            return ResponseProvider.error("Error al obtener resumen detallado de metas", 500);
        }
    }
}