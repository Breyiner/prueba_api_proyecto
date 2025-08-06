package CONTROLLER; // Paquete de controladores REST

import MODELO.DashboardDao; // DAO para consultas de dashboard
import MODELO.ResumenCardDTO; // DTO para resumenes en formato tarjeta
import MODELO.ResumenCategoriasDTO; // DTO para resumen detallado por categorías
import MODELO.ResumenMetasDTO; // DTO para resumen detallado de metas
import javax.ws.rs.*; // Anotaciones JAX-RS para definir endpoints REST
import javax.ws.rs.core.MediaType; // Para definir tipos de contenido (JSON)
import javax.ws.rs.core.Response; // Para construir respuestas HTTP
import java.math.BigDecimal; // Para manejo exacto de números decimales
import java.sql.ResultSet; // Resultado de consultas SQL
import java.sql.SQLException; // Excepciones SQL
import java.util.ArrayList; // Implementación dinámica de listas
import java.util.List; // Interfaz para listas

@Path("/dashboard") // Ruta base para este controlador REST
public class DashboardController { // Controlador para endpoints del dashboard financiero

    @GET // Método HTTP GET
    @Path("/movimientos/usuario/{usuario_id}/mes/{mes}") // Endpoint para resumen de movimientos según usuario y mes
    @Produces(MediaType.APPLICATION_JSON) // Responde con JSON
    public Response getResumenMovimientos(
            @PathParam("usuario_id") int usuario_id, // Parámetro path: ID usuario
            @PathParam("mes") int mes) { // Parámetro path: mes consultado
        List<ResumenCardDTO> resumen = new ArrayList<>(); // Lista para almacenar tarjetas resumen

        try {
            ResultSet rs = DashboardDao.getResumenMovimientos(usuario_id, mes); // Ejecuta consulta movimientos
            while (rs.next()) { // Itera sobre cada fila del resultado
                ResumenCardDTO card = new ResumenCardDTO( // Crea DTO con los datos de la fila
                    rs.getString("icono"), // Icono del tipo de movimiento
                    rs.getString("color"), // Color asociado
                    rs.getString("nombre"), // Nombre del tipo de movimiento
                    rs.getBigDecimal("total"), // Total calculado
                    "movimiento" // Indica que es un resumen de movimiento
                );
                resumen.add(card); // Agrega DTO a la lista
            }
            rs.close(); // Cierra el ResultSet para liberar recursos

            if (!resumen.isEmpty()) { // Si hay resultados
                return ResponseProvider.success(resumen, "Resumen de movimientos obtenido con éxito.", 200); // Respuesta exitosa
            } else { // Si no hay resultados
                return ResponseProvider.error("No hay movimientos registrados para este mes.", 404); // Error 404: no encontrado
            }

        } catch (SQLException e) { // Captura errores SQL
            return ResponseProvider.error("Error interno al obtener el resumen de movimientos.", 500); // Error 500 interno
        }
    }

    @GET
    @Path("/metas/usuario/{usuario_id}/mes/{mes}") // Endpoint para resumen de metas por usuario y mes
    @Produces(MediaType.APPLICATION_JSON) // Responde JSON
    public Response getResumenMetas(
            @PathParam("usuario_id") int usuario_id, // ID usuario
            @PathParam("mes") int mes) { // Mes consultado
        List<ResumenCardDTO> resumen = new ArrayList<>(); // Lista para tarjetas resumen

        try {
            ResultSet rs = DashboardDao.getResumenMetas(usuario_id, mes); // Ejecuta consulta resumen metas
            while (rs.next()) { // Itera filas resultado
                ResumenCardDTO card = new ResumenCardDTO( // Crea DTO resumen meta
                    rs.getString("icono"),
                    rs.getString("color"),
                    rs.getString("nombre"),
                    rs.getBigDecimal("total"),
                    "meta" // Indica que es una meta
                );
                resumen.add(card); // Agrega DTO a la lista
            }
            rs.close(); // Cierra ResultSet

            if (!resumen.isEmpty()) { // Si hay resultados
                return ResponseProvider.success(resumen, "Resumen de metas obtenido con éxito.", 200); // Respuesta exitosa
            } else {
                return ResponseProvider.error("No hay aportes a metas registrados para este mes.", 404); // No hay datos
            }

        } catch (SQLException e) { // Captura errores SQL
            return ResponseProvider.error("Error interno al obtener el resumen de metas.", 500); // Error interno
        }
    }

    @GET
    @Path("/completo/usuario/{usuario_id}/mes/{mes}") // Endpoint para resumen completo: movimientos + metas
    @Produces(MediaType.APPLICATION_JSON) // Respuesta JSON
    public Response getResumenCompleto(
            @PathParam("usuario_id") int usuarioId, // ID usuario
            @PathParam("mes") int mes) { // Mes consultado
        List<ResumenCardDTO> resumenCompleto = new ArrayList<>(); // Lista para resumen completo

        try {
            // Obtener resumen movimientos
            ResultSet rsMovimientos = DashboardDao.getResumenMovimientos(usuarioId, mes);
            while (rsMovimientos.next()) {
                ResumenCardDTO card = new ResumenCardDTO(
                    rsMovimientos.getString("icono"),
                    rsMovimientos.getString("color"),
                    rsMovimientos.getString("nombre"),
                    rsMovimientos.getBigDecimal("total"),
                    "movimiento"
                );
                resumenCompleto.add(card);
            }
            rsMovimientos.close(); // Cierra ResultSet movimientos

            // Obtener resumen metas
            ResultSet rsMetas = DashboardDao.getResumenMetas(usuarioId, mes);
            while (rsMetas.next()) {
                ResumenCardDTO card = new ResumenCardDTO(
                    rsMetas.getString("icono"),
                    rsMetas.getString("color"),
                    rsMetas.getString("nombre"),
                    rsMetas.getBigDecimal("total"),
                    "meta"
                );
                resumenCompleto.add(card);
            }
            rsMetas.close(); // Cierra ResultSet metas
            
            // Calcular balance neto: saldo inicial igual al primer total
            BigDecimal balance = resumenCompleto.get(0).getTotal();
            
            // Resta los totales de los demás para balancear
            for (int i = 1; i < resumenCompleto.size(); i++) {
                balance = balance.subtract(resumenCompleto.get(i).getTotal());
            }
            
            // Crear tarjeta resumen para balance neto
            ResumenCardDTO card = new ResumenCardDTO(
                    "ri-wallet-3-line", // Icono wallet
                    "#3367d6",          // Color azul
                    "Balance",          // Nombre tarjeta
                    balance,            // Valor balance calculado
                    "balance"           // Tipo balance
            );
            resumenCompleto.add(0, card); // Inserta al inicio la tarjeta balance

            if (!resumenCompleto.isEmpty()) { // Si hay datos
                return ResponseProvider.success(resumenCompleto, "Resumen completo obtenido con éxito.", 200); // OK
            } else {
                return ResponseProvider.error("No hay datos registrados para este mes.", 404); // No hay datos
            }

        } catch (SQLException e) { // Captura errores SQL
            return ResponseProvider.error("Error interno al obtener el resumen completo.", 500); // Error interno
        }
    }
    
    @GET
    @Path("/categorias/usuario/{usuario_id}/mes/{mes}/tipo/{tipo_id}") // Endpoint resumen detallado categorías
    @Produces(MediaType.APPLICATION_JSON) // Responde JSON
    public Response getResumenCategoriasDetalle(
            @PathParam("usuario_id") int usuario_id, // ID usuario
            @PathParam("mes") int mes,               // Mes consultado
            @PathParam("tipo_id") int tipo_id) {    // Tipo movimiento (ingreso, gasto, etc)
        try {
            ResultSet rs = DashboardDao.getResumenCategoriasDetalle(usuario_id, mes, tipo_id); // Consulta detallada
            List<ResumenCategoriasDTO> lista = new ArrayList<>(); // Lista para DTOs

            while (rs.next()) { // Itera resultados
                ResumenCategoriasDTO dto = new ResumenCategoriasDTO(
                    rs.getInt("id"),                   // ID categoría
                    rs.getInt("tipo_movimiento_id"),  // Tipo movimiento
                    rs.getString("icono"),             // Icono categoría
                    rs.getString("nombre"),            // Nombre categoría
                    rs.getString("color"),             // Color
                    rs.getString("color_bg"),          // Color fondo
                    rs.getInt("cantidad"),             // Cantidad movimientos
                    rs.getBigDecimal("total")          // Total monto
                );
                lista.add(dto); // Añade a lista
            }

            return ResponseProvider.success(lista, "Resumen completo obtenido con éxito.", 200); // OK con datos
        } catch (SQLException e) {
            return ResponseProvider.error("Error al obtener resumen detallado de las categorias", 500); // Error DB
        }
    }
    
    @GET
    @Path("/metas/detalle/usuario/{usuario_id}/mes/{mes}") // Endpoint resumen detallado metas
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResumenMetasDetalle(
            @PathParam("usuario_id") int usuarioId,
            @PathParam("mes") int mes) {
        try {
            ResultSet rs = DashboardDao.getResumenMetasDetalle(usuarioId, mes); // Consulta detallada metas
            List<ResumenMetasDTO> lista = new ArrayList<>();

            while (rs.next()) { // Lee cada fila
                ResumenMetasDTO dto = new ResumenMetasDTO(
                    rs.getInt("id"),                // ID meta
                    rs.getInt("tipo_movimiento_id"), // Tipo movimiento (siempre 3)
                    rs.getString("icono"),          // Icono
                    rs.getString("color"),          // Color
                    rs.getString("color_bg"),       // Fondo color
                    rs.getString("nombre"),         // Nombre meta
                    rs.getInt("cantidad"),          // Cantidad aportes
                    rs.getBigDecimal("total")       // Total aportado
                );
                lista.add(dto); // Agrega DTO a lista
            }

            return ResponseProvider.success(lista, "Resumen completo obtenido con éxito.", 200); // OK con datos
        } catch (SQLException e) {
            return ResponseProvider.error("Error al obtener resumen detallado de metas", 500); // Error DB
        }
    }

}
