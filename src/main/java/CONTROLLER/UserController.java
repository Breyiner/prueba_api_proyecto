package CONTROLLER; // Paquete que agrupa los controladores del sistema

import MODELO.UsuarioDao;              // Capa DAO para interacción con BD para usuarios
import MODELO.CantidadRegistrosDTO;    // DTO para manejar cantidad de registros
import MODELO.PasswordDTO;             // DTO para manejo de contraseña (si aplica)
import MODELO.Usuario;                 // Entidad Usuario que representa datos de usuario
import MODELO.UsuarioDTO;              // DTO para transferencia de datos de usuario
import MODELO.UsuarioLoginDTO;         // DTO para login de usuario
import MODELO.UsuarioTablaDTO;         // DTO para datos de usuario en formato tabla
import java.sql.ResultSet;             // Para manejo de resultados de consultas SQL
import java.sql.SQLException;          // Manejo de excepciones SQL
import java.util.ArrayList;            // Implementación de lista dinámica
import java.util.List;                 // Interfaz lista para colecciones
import javax.ws.rs.Consumes;           // Anotación para tipos de contenido entrante
import javax.ws.rs.DELETE;             // Anotación para método HTTP DELETE
import javax.ws.rs.GET;                // Anotación para método HTTP GET
import javax.ws.rs.PATCH;              // Anotación para método HTTP PATCH
import javax.ws.rs.POST;               // Anotación para método HTTP POST
import javax.ws.rs.PUT;                // Anotación para método HTTP PUT
import javax.ws.rs.Path;               // Anotación para definir ruta HTTP
import javax.ws.rs.PathParam;          // Anotación para extraer parámetros de ruta
import javax.ws.rs.Produces;           // Anotación para tipo de contenido saliente
import javax.ws.rs.core.MediaType;     // Constantes para tipos de contenido (ej: JSON)
import javax.ws.rs.core.Response;      // Construcción de respuestas HTTP
import org.json.JSONException;         // Excepción para manejo de JSON
import org.mindrot.jbcrypt.BCrypt;     // Librería para encriptación de contraseñas

@Path("/usuarios") // Ruta base para todas las operaciones REST relacionadas con usuarios
public class UserController {

    /**
     * Método para obtener todos los usuarios registrados en la base de datos.
     * Responde a solicitudes GET en /usuarios.
     *
     * @return Response HTTP con lista de usuarios o mensaje indicando que no
     * hay usuarios.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public static Response getUsuarios() {
        List<Usuario> usuarios = new ArrayList<>(); // Lista para almacenar objetos Usuario

        try {
            // Llama a la capa DAO para obtener el conjunto de usuarios de la base de datos
            ResultSet respuesta = UsuarioDao.getUsuarios();

            // Itera el ResultSet para construir objetos Usuario con los datos obtenidos
            while (respuesta.next()) {
                Usuario usuario = new Usuario(
                        respuesta.getInt("id"), // ID único del usuario
                        respuesta.getString("nombre"), // Nombre del usuario
                        respuesta.getString("apellido"), // Apellido del usuario
                        respuesta.getString("correo"), // Correo electrónico
                        respuesta.getString("contrasena"), // Contraseña encriptada (ideal no enviar)
                        Integer.parseInt(respuesta.getString("genero_id")), // ID del género
                        Integer.parseInt(respuesta.getString("ciudad_id")), // ID de la ciudad
                        respuesta.getInt("rol_id"), // ID del rol asignado
                        Integer.parseInt(respuesta.getString("estado_id")) // ID del estado del usuario
                );

                // Agrega cada objeto Usuario a la lista
                usuarios.add(usuario);
            }

            // Cierra el ResultSet para liberar recursos
            respuesta.close();

            // Verifica si la lista tiene elementos
            if (!usuarios.isEmpty()) {
                // Si hay usuarios, retorna respuesta con código 200 y la lista
                return ResponseProvider.success(usuarios, "Usuarios obtenidos con éxito.", 200);
            } else {
                // Si no hay usuarios, retorna lista vacía con mensaje informativo
                return ResponseProvider.success(usuarios, "No hay usuarios registrados.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, devuelve una respuesta con código 500 y mensaje de error
            return ResponseProvider.error("Error interno al obtener los usuarios", 500);
        }
    }

    /**
     * Método para obtener usuarios filtrados por ciudad específica. Responde a
     * solicitudes GET en /usuarios/ciudad/{ciudad_id}.
     *
     * @param ciudad_id Identificador de la ciudad para filtrar usuarios
     * @return Response HTTP con lista de usuarios filtrados o mensaje si no hay
     * resultados
     */
    @GET
    @Path("ciudad/{ciudad_id}")
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public static Response getUsuariosByCiudadId(@PathParam("ciudad_id") int ciudad_id) {
        List<Usuario> usuarios = new ArrayList<>(); // Lista para almacenar objetos Usuario

        try {
            // Llama a la capa DAO para obtener usuarios filtrados por ciudad_id
            ResultSet respuesta = UsuarioDao.getUsuariosByCiudadId(ciudad_id);

            // Itera el ResultSet para construir objetos Usuario con los datos obtenidos
            while (respuesta.next()) {
                Usuario usuario = new Usuario(
                        respuesta.getInt("id"), // ID único del usuario
                        respuesta.getString("nombre"), // Nombre del usuario
                        respuesta.getString("apellido"), // Apellido del usuario
                        respuesta.getString("correo"), // Correo electrónico
                        respuesta.getString("contrasena"), // Contraseña encriptada (no recomendado enviar)
                        Integer.parseInt(respuesta.getString("genero_id")), // ID del género
                        Integer.parseInt(respuesta.getString("ciudad_id")), // ID de la ciudad
                        respuesta.getInt("rol_id"), // ID del rol asignado
                        Integer.parseInt(respuesta.getString("estado_id")) // ID del estado del usuario
                );

                // Agrega cada objeto Usuario a la lista
                usuarios.add(usuario);
            }

            // Cierra el ResultSet para liberar recursos
            respuesta.close();

            // Si hay usuarios, retorna con código 200; si no, retorna error 404
            if (!usuarios.isEmpty()) {
                return ResponseProvider.success(usuarios, "Usuarios obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay usuarios relacionados.", 404);
            }

        } catch (SQLException e) {
            // Imprime el error en consola para depuración (considerar usar logger en producción)
            System.out.print(e);
            // Retorna error 500 para indicar problema interno
            return ResponseProvider.error("Error interno al obtener los usuarios", 500);
        }
    }

    /**
     * Método para obtener usuarios filtrados por género específico. Responde a
     * solicitudes GET en /usuarios/genero/{genero_id}.
     *
     * @param genero_id Identificador del género para filtrar usuarios
     * @return Response HTTP con lista de usuarios filtrados o mensaje si no hay
     * resultados
     */
    @GET
    @Path("genero/{genero_id}")
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public static Response getUsuariosByGeneroId(@PathParam("genero_id") int genero_id) {
        List<Usuario> usuarios = new ArrayList<>(); // Lista para almacenar objetos Usuario

        try {
            // Llama a la capa DAO para obtener usuarios filtrados por genero_id
            ResultSet respuesta = UsuarioDao.getUsuariosByGeneroId(genero_id);

            // Itera el ResultSet para construir objetos Usuario con los datos obtenidos
            while (respuesta.next()) {
                Usuario usuario = new Usuario(
                        respuesta.getInt("id"), // ID único del usuario
                        respuesta.getString("nombre"), // Nombre del usuario
                        respuesta.getString("apellido"), // Apellido del usuario
                        respuesta.getString("correo"), // Correo electrónico
                        respuesta.getString("contrasena"), // Contraseña encriptada (no recomendado enviar)
                        Integer.parseInt(respuesta.getString("genero_id")), // ID del género
                        Integer.parseInt(respuesta.getString("ciudad_id")), // ID de la ciudad
                        respuesta.getInt("rol_id"), // ID del rol asignado
                        Integer.parseInt(respuesta.getString("estado_id")) // ID del estado del usuario
                );

                // Agrega cada objeto Usuario a la lista
                usuarios.add(usuario);
            }

            // Cierra el ResultSet para liberar recursos
            respuesta.close();

            // Si hay usuarios, retorna con código 200; si no, retorna error 404 con null
            if (!usuarios.isEmpty()) {
                return ResponseProvider.success(usuarios, "Usuarios obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.success(null, "No hay usuarios relacionados.", 404);
            }

        } catch (SQLException e) {
            // Retorna error 500 para indicar problema interno
            return ResponseProvider.error("Error interno al obtener los usuarios", 500);
        }
    }

    /**
     * Método para obtener usuarios en formato tabla con campos detallados para
     * UI. Responde a solicitudes GET en /usuarios/tabla. Útil para mostrar
     * listados en front-end con información desglosada (rol, género, ciudad,
     * estado).
     *
     * @return Response HTTP con lista de usuarios en formato tabla o mensaje si
     * no hay resultados
     */
    @GET
    @Path("/tabla")
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public static Response getUsuariosTabla() {
        List<UsuarioTablaDTO> usuarios = new ArrayList<>(); // Lista para almacenar objetos UsuarioTablaDTO

        try {
            // Llama a la capa DAO para obtener usuarios con datos relacionados (rol, ciudad, etc.)
            ResultSet respuesta = UsuarioDao.getUsuariosTabla();

            // Itera los resultados para crear objetos UsuarioTablaDTO con datos formateados
            while (respuesta.next()) {
                UsuarioTablaDTO usuario = new UsuarioTablaDTO(
                        respuesta.getInt("id"), // ID único del usuario
                        respuesta.getString("rol"), // Nombre descriptivo del rol (ej. Admin, Usuario)
                        respuesta.getString("nombre"), // Nombre del usuario
                        respuesta.getString("apellido"), // Apellido del usuario
                        respuesta.getString("correo"), // Correo electrónico
                        respuesta.getString("genero"), // Nombre descriptivo del género (ej. Masculino)
                        respuesta.getString("ciudad"), // Nombre descriptivo de la ciudad
                        respuesta.getString("estado") // Nombre descriptivo del estado (ej. Activo)
                );

                // Agrega cada objeto a la lista
                usuarios.add(usuario);
            }

            // Cierra el ResultSet para liberar recursos
            respuesta.close();

            // Si hay usuarios, retorna con código 200; si no, retorna lista vacía con mensaje
            if (!usuarios.isEmpty()) {
                return ResponseProvider.success(usuarios, "Usuarios obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.success(usuarios, "No hay usuarios registrados.", 200);
            }

        } catch (SQLException e) {
            // Retorna error 500 para indicar problema interno
            return ResponseProvider.error("Error interno al obtener los usuarios", 500);
        }
    }

    /**
     * Método para obtener la cantidad total de usuarios registrados. Responde a
     * solicitudes GET en /usuarios/cantidad.
     *
     * @return Response HTTP con el número total de usuarios o error si no se
     * puede obtener
     */
    @GET
    @Path("/cantidad")
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public static Response getUsuario() {
        CantidadRegistrosDTO cantidad = null; // Objeto para almacenar la cantidad de usuarios

        try {
            // Llama a la capa DAO para obtener el conteo de usuarios
            ResultSet respuesta = UsuarioDao.getCantidadUsuarios();

            // Procesa el resultado para crear el objeto CantidadRegistrosDTO
            while (respuesta.next()) {
                cantidad = new CantidadRegistrosDTO(respuesta.getInt("cantidad"));
            }

            // Cierra el ResultSet para liberar recursos
            respuesta.close();

            // Si no se obtuvo cantidad, retorna error 404; si se obtuvo, retorna con código 200
            if (cantidad == null) {
                return ResponseProvider.error("No se pudo obtener la cantidad de usuarios.", 404);
            } else {
                return ResponseProvider.success(cantidad, "Cantidad de usuarios obtenida con éxito.", 200);
            }

        } catch (SQLException e) {
            // Retorna error 500 para indicar problema interno
            return ResponseProvider.error("Error interno al obtener la cantidad de usuarios", 500);
        }
    }

    /**
     * Método para obtener un usuario específico por su ID. Responde a
     * solicitudes GET en /usuarios/{id}.
     *
     * @param id Identificador único del usuario a buscar
     * @return Response HTTP con los datos del usuario o error si no se
     * encuentra
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public static Response getUsuarioById(@PathParam("id") int id) {
        Usuario usuario = null; // Objeto para almacenar el usuario encontrado

        try {
            // Llama a la capa DAO para obtener el usuario por su ID
            ResultSet respuesta = UsuarioDao.getUsuarioById(id);

            // Procesa el resultado para crear el objeto Usuario
            while (respuesta.next()) {
                usuario = new Usuario(
                        respuesta.getInt("id"), // ID único del usuario
                        respuesta.getString("nombre"), // Nombre del usuario
                        respuesta.getString("apellido"), // Apellido del usuario
                        respuesta.getString("correo"), // Correo electrónico
                        "", // Contraseña vacía por seguridad
                        respuesta.getInt("genero_id"), // ID del género
                        respuesta.getInt("ciudad_id"), // ID de la ciudad
                        respuesta.getInt("rol_id"), // ID del rol asignado
                        respuesta.getInt("estado_id") // ID del estado del usuario
                );
            }

            // Cierra el ResultSet para liberar recursos
            respuesta.close();

            // Si no se encontró usuario, retorna error 404; si se encontró, retorna con código 200
            if (usuario == null) {
                return ResponseProvider.error("El usuario no existe.", 404);
            } else {
                return ResponseProvider.success(usuario, "Usuario obtenido con éxito.", 200);
            }

        } catch (SQLException e) {
            // Retorna error 500 para indicar problema interno
            return ResponseProvider.error("Error interno al obtener los usuarios", 500);
        }
    }

    /**
     * Método para crear un nuevo usuario. Recibe un JSON con los datos del
     * usuario y realiza validaciones antes de insertarlo en la base de datos.
     * Responde a solicitudes POST en /usuarios/register.
     *
     * @param usuarioData Objeto Usuario con los datos del nuevo usuario
     * @return Response HTTP con el usuario creado o un mensaje de error
     */
    @POST
    @Path("/register")
    @Validar(entidad = "Usuario") // Aplica validaciones definidas para la entidad Usuario
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    @Consumes(MediaType.APPLICATION_JSON) // Indica que recibe datos en formato JSON
    public static Response createUsuario(Usuario usuarioData) {
        try {
            // Verifica si el correo ya está registrado en la base de datos
            Usuario usuarioExistente = getUsuarioByCorreo(usuarioData.getCorreo());

            // Si el correo ya existe, retorna error 409 (Conflicto)
            if (usuarioExistente != null) {
                return ResponseProvider.error("Este correo ya fue registrado.", 409);
            }

            // Verifica si la ciudad existe llamando al controlador correspondiente
            Response ciudad = CiudadController.getCiudad(usuarioData.getCiudad_id());

            // Si la ciudad no existe (status diferente de 200), retorna error 409
            if (ciudad.getStatus() != 200) {
                return ResponseProvider.error("Esta ciudad no existe.", 409);
            }

            // Verifica si el género existe llamando al controlador correspondiente
            Response genero = GeneroController.getGenero(usuarioData.getGenero_id());

            // Si el género no existe (status diferente de 200), retorna error 409
            if (genero.getStatus() != 200) {
                return ResponseProvider.error("Este género no existe.", 409);
            }

            // Obtiene la contraseña en texto plano del objeto recibido
            String contrasenaText = usuarioData.getContrasena();

            // Hashea la contraseña usando BCrypt antes de almacenarla
            usuarioData.setContrasena(hashPassword(contrasenaText));

            int idGenerado = 0; // Variable para almacenar el ID generado por la base de datos

            // Inserta el usuario en la base de datos a través de la capa DAO
            ResultSet ultimoRegistro = UsuarioDao.createUsuario(usuarioData);

            // Procesa el ResultSet para obtener el ID generado tras la inserción
            while (ultimoRegistro.next()) {
                idGenerado = ultimoRegistro.getInt(1); // Obtiene el ID del primer campo
                usuarioData.setId(idGenerado); // Asigna el ID al objeto usuario
                usuarioData.setContrasena(contrasenaText); // Restaura contraseña en texto plano (opcional, no recomendado)
            }

            // Cierra el ResultSet para liberar recursos
            ultimoRegistro.close();

            // Si no se generó un ID (inserción fallida), retorna error 400
            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear el usuario.", 400);
            } else {
                // Si la inserción fue exitosa, retorna el usuario creado con código 200
                return ResponseProvider.success(usuarioData, "Usuario creado con éxito.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre un error SQL, retorna error 500 indicando problema interno
            return ResponseProvider.error("Error interno al crear el usuario.", 500);
        }
    }

    /**
     * Método para autenticar a un usuario mediante correo y contraseña.
     * Responde a solicitudes POST en /usuarios/login.
     *
     * @param usuarioData Objeto UsuarioLoginDTO con correo y contraseña
     * @return Response HTTP con datos del usuario autenticado o mensaje de
     * error
     */
    @POST
    @Path("/login") // Define la ruta para el login de usuarios: POST /usuarios/login
    @Validar(entidad = "UsuarioLogin") // Valida que los datos cumplan con las reglas de UsuarioLoginDTO
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    @Consumes(MediaType.APPLICATION_JSON) // Indica que recibe datos en formato JSON
    public static Response loginUser(UsuarioLoginDTO usuarioData) {
        try {
            // Busca el usuario en la base de datos por su correo
            Usuario usuario = getUsuarioByCorreo(usuarioData.getCorreo());

            // Si el correo no está registrado, retorna error 404
            if (usuario == null) {
                return ResponseProvider.error("Este correo no se encuentra registrado.", 404, null);
            }

            // Si el usuario está inactivo (estado_id = 2), deniega acceso con error 403 (Prohibido)
            if (usuario.getEstado_id() == 2) {
                return ResponseProvider.error("Este usuario está inactivo.", 403, null);
            }

            // Verifica si la contraseña proporcionada coincide con la almacenada (hasheada)
            if (!checkPassword(usuarioData.getContrasena(), usuario.getContrasena())) {
                // Si la contraseña no coincide, retorna error 401 (No Autorizado)
                return ResponseProvider.error("Contraseña incorrecta.", 401, null);
            } else {
                // Si la autenticación es exitosa, crea un DTO con datos seguros para la respuesta
                UsuarioDTO usuarioDto = new UsuarioDTO(
                        usuario.getId(), // ID del usuario
                        usuario.getNombre(), // Nombre del usuario
                        usuario.getApellido(), // Apellido del usuario
                        usuario.getRol_id() // ID del rol asignado
                );

                // Retorna éxito con los datos del usuario y código 200
                return ResponseProvider.success(usuarioDto, "Inicio de sesión exitoso.", 200);
            }

        } catch (JSONException e) {
            // Si ocurre un error al procesar JSON, retorna error 500
            return ResponseProvider.error("Error interno al validar el usuario.", 500);
        }
    }

    /**
     * Método para actualizar completamente un usuario existente. Responde a
     * solicitudes PUT en /usuarios/{id}.
     *
     * @param id Identificador único del usuario a actualizar
     * @param usuarioData Objeto Usuario con los nuevos datos
     * @return Response HTTP indicando éxito o error
     */
    @PUT
    @Validar(entidad = "Usuario") // Valida los datos según las reglas de la entidad Usuario
    @Path("/{id}") // Ruta con el ID del usuario a actualizar
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    @Consumes(MediaType.APPLICATION_JSON) // Indica que recibe datos en formato JSON
    public static Response updateUsuario(@PathParam("id") int id, Usuario usuarioData) {
        boolean hashValido = true; // Bandera para verificar si la actualización de la contraseña fue exitosa

        try {
            // Verifica si el usuario existe llamando al método getUsuarioById
            Response usuarioExistente = getUsuarioById(id);

            // Si el usuario no existe, retorna error 404
            if (usuarioExistente.getStatus() == 404) {
                return ResponseProvider.error("Este usuario no existe.", 404);
            }

            // Verifica si el correo está duplicado en otro usuario
            if (existEmail(id, usuarioData)) {
                return ResponseProvider.error("Este correo ya fue registrado.", 409);
            }

            // Verifica si la ciudad existe
            Response ciudad = CiudadController.getCiudad(usuarioData.getCiudad_id());

            // Si la ciudad no existe, retorna error 409
            if (ciudad.getStatus() != 200) {
                return ResponseProvider.error("Esta ciudad no existe.", 409);
            }

            // Verifica si el género existe
            Response genero = GeneroController.getGenero(usuarioData.getGenero_id());

            // Si el género no existe, retorna error 409
            if (genero.getStatus() != 200) {
                return ResponseProvider.error("Este género no existe.", 409);
            }

            // Verifica si el rol existe
            Response rol = RolController.getRol(usuarioData.getRol_id());

            // Si el rol no existe, retorna error 409
            if (rol.getStatus() != 200) {
                return ResponseProvider.error("Este rol no existe.", 409);
            }

            // Verifica si el estado existe
            Response estado = EstadoController.getEstado(usuarioData.getEstado_id());

            // Si el estado no existe, retorna error 409
            if (estado.getStatus() != 200) {
                return ResponseProvider.error("Este estado no existe.", 409);
            }

            // Si se proporciona una contraseña, la procesa y actualiza por separado
            if (usuarioData.getContrasena() != null) {
                // Obtiene la contraseña en texto plano
                String contrasenaText = usuarioData.getContrasena();

                // Hashea la contraseña usando BCrypt
                String hashContrasena = hashPassword(contrasenaText);

                // Actualiza la contraseña en la base de datos
                int rowsAffectedPswd = UsuarioDao.updateContrasena(id, hashContrasena);

                // Si la actualización de la contraseña falla, marca la bandera como falsa
                if (rowsAffectedPswd == 0) {
                    hashValido = false;
                }
            }

            // Actualiza los demás campos del usuario en la base de datos
            int rowsAffectedUsuario = UsuarioDao.updateUsuario(id, usuarioData);

            // Si la actualización de datos y contraseña (si aplica) fue exitosa, retorna código 200
            if (rowsAffectedUsuario != 0 && hashValido) {
                return ResponseProvider.success(null, "Usuario actualizado con éxito.", 200);
            } else {
                // Si alguna actualización falla, retorna error 400
                return ResponseProvider.error("Error al actualizar el usuario.", 400);
            }

        } catch (Exception e) {
            // Captura errores inesperados y retorna error 500
            return ResponseProvider.error("Error interno al actualizar el usuario.", 500);
        }
    }

    /**
     * Método para actualizar parcialmente un usuario (perfil). Responde a
     * solicitudes PATCH en /usuarios/{id}.
     *
     * @param id Identificador único del usuario a actualizar
     * @param usuarioData Objeto Usuario con los datos a actualizar parcialmente
     * @return Response HTTP indicando éxito o error
     */
    @PATCH
    @Validar(entidad = "UsuarioPerfil") // Valida los datos según las reglas de UsuarioPerfil
    @Path("/{id}") // Ruta con el ID del usuario a actualizar parcialmente
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    @Consumes(MediaType.APPLICATION_JSON) // Indica que recibe datos en formato JSON
    public static Response partialUpdateUsuario(@PathParam("id") int id, Usuario usuarioData) {
        try {
            // Verifica si el correo está duplicado en otro usuario
            if (existEmail(id, usuarioData)) {
                return ResponseProvider.error("Este correo ya fue registrado.", 409);
            }

            // Verifica si la ciudad existe
            Response ciudad = CiudadController.getCiudad(usuarioData.getCiudad_id());

            // Si la ciudad no existe, retorna error 409
            if (ciudad.getStatus() != 200) {
                return ResponseProvider.error("Esta ciudad no existe.", 409);
            }

            // Verifica si el género existe
            Response genero = GeneroController.getGenero(usuarioData.getGenero_id());

            // Si el género no existe, retorna error 409
            if (genero.getStatus() != 200) {
                return ResponseProvider.error("Este género no existe.", 409);
            }

            // Crea un objeto Usuario con solo los campos permitidos para actualización parcial
            Usuario usuarioParcial = new Usuario(
                    id, // ID del usuario
                    usuarioData.getNombre(), // Nombre actualizado
                    usuarioData.getApellido(), // Apellido actualizado
                    usuarioData.getCorreo(), // Correo actualizado
                    usuarioData.getGenero_id(), // ID del género actualizado
                    usuarioData.getCiudad_id() // ID de la ciudad actualizada
            );

            // Actualiza los campos especificados en la base de datos
            int rowsAffected = UsuarioDao.partialUpdate(id, usuarioParcial);

            // Si la actualización fue exitosa, retorna código 200
            if (rowsAffected != 0) {
                usuarioData.setId(id); // Asigna el ID al objeto para consistencia
                return ResponseProvider.success(null, "Usuario actualizado con éxito.", 200);
            } else {
                // Si la actualización falla, retorna error 400
                return ResponseProvider.error("Error al actualizar el usuario.", 400);
            }

        } catch (Exception e) {
            // Captura errores inesperados y retorna error 500
            return ResponseProvider.error("Error interno al actualizar el usuario.", 500);
        }
    }

    /**
     * Método para actualizar la contraseña de un usuario. Responde a
     * solicitudes PATCH en /usuarios/password/usuario/{usuario_id}.
     *
     * @param usuario_id Identificador único del usuario
     * @param passwordData Objeto PasswordDTO con contraseña antigua y nueva
     * @return Response HTTP indicando éxito o error
     */
    @PATCH
    @Path("/password/usuario/{usuario_id}") // Ruta para actualizar la contraseña del usuario
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    @Consumes(MediaType.APPLICATION_JSON) // Indica que recibe datos en formato JSON
    public static Response updateContrasena(@PathParam("usuario_id") int usuario_id, PasswordDTO passwordData) {
        try {
            // Verifica que la nueva contraseña y su confirmación coincidan
            if (!passwordData.getNew_password().equals(passwordData.getConfirm_password())) {
                return ResponseProvider.error("Las contraseñas no coinciden.", 400, null);
            }

            // Valida que la contraseña antigua sea correcta
            boolean passwordValid = validatePassword(usuario_id, passwordData.getOld_password());

            // Si la contraseña antigua es incorrecta, retorna error 400
            if (!passwordValid) {
                return ResponseProvider.error("Contraseña incorrecta.", 400, null);
            }

            // Hashea la nueva contraseña usando BCrypt
            String hashPassword = hashPassword(passwordData.getNew_password());

            // Actualiza la contraseña en la base de datos
            int rowsAffected = UsuarioDao.updateContrasena(usuario_id, hashPassword);

            // Si la actualización fue exitosa, retorna código 200
            if (rowsAffected != 0) {
                return ResponseProvider.success(null, "Contraseña actualizada con éxito.", 200);
            } else {
                return ResponseProvider.error("Este usuario no existe.", 404, null);
            }

        } catch (Exception e) {
            // Captura errores inesperados y retorna error 500
            return ResponseProvider.error("Error interno al actualizar la contraseña.", 500);
        }
    }

    /**
     * Método para realizar una eliminación lógica (soft delete) de un usuario.
     * Responde a solicitudes DELETE en /usuarios/soft/{id}.
     *
     * @param id Identificador único del usuario a desactivar
     * @return Response HTTP indicando éxito o error
     */
    @DELETE
    @Path("soft/{id}") // Ruta para eliminación lógica del usuario por ID
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public static Response softDeleteUsuario(@PathParam("id") int id) {
        try {
            // Ejecuta la eliminación lógica en la base de datos (cambia estado, no elimina físicamente)
            int rowsAffected = UsuarioDao.softDeleteUsuario(id);

            // Si la eliminación lógica fue exitosa, retorna código 200
            if (rowsAffected != 0) {
                return ResponseProvider.success(null, "Usuario eliminado de forma segura.", 200);
            } else {
                return ResponseProvider.error("Este usuario no existe.", 404);
            }

        } catch (Exception e) {
            // Captura errores inesperados y retorna error 500
            return ResponseProvider.error("Error interno al eliminar el usuario.", 500);
        }
    }

    /**
     * Método para realizar una eliminación física definitiva de un usuario.
     * Responde a solicitudes DELETE en /usuarios/{id}.
     *
     * @param id Identificador único del usuario a eliminar
     * @return Response HTTP indicando éxito o error
     */
    @DELETE
    @Path("/{id}") // Ruta para eliminación física por ID
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public static Response deleteUsuario(@PathParam("id") int id) {
        try {
            // Ejecuta la eliminación física en la base de datos
            int rowsAffected = UsuarioDao.deleteUsuario(id);

            // Si la eliminación fue exitosa, retorna código 200
            if (rowsAffected != 0) {
                return ResponseProvider.success(null, "Usuario eliminado con éxito.", 200);
            } else {
                return ResponseProvider.error("Este usuario no existe.", 404);
            }

        } catch (Exception e) {
            // Captura errores inesperados y retorna error 500
            return ResponseProvider.error("Error interno al eliminar el usuario.", 500);
        }
    }

    /**
     * Método auxiliar para buscar un usuario por su correo.
     *
     * @param correo Correo del usuario a buscar
     * @return Objeto Usuario si existe, o null si no se encuentra
     */
    private static Usuario getUsuarioByCorreo(String correo) {
        Usuario usuario = null; // Inicializa el objeto usuario como null

        try {
            // Consulta el usuario por correo a través de la capa DAO
            ResultSet respuesta = UsuarioDao.getUsuarioByCorreo(correo);

            // Procesa el ResultSet para construir el objeto Usuario
            while (respuesta.next()) {
                usuario = new Usuario(
                        respuesta.getInt("id"), // ID único del usuario
                        respuesta.getString("nombre"), // Nombre del usuario
                        respuesta.getString("apellido"), // Apellido del usuario
                        respuesta.getString("correo"), // Correo electrónico
                        respuesta.getString("contrasena"), // Contraseña hasheada
                        respuesta.getInt("genero_id"), // ID del género
                        respuesta.getInt("ciudad_id"), // ID de la ciudad
                        respuesta.getInt("rol_id"), // ID del rol asignado
                        respuesta.getInt("estado_id") // ID del estado del usuario
                );
            }

            // Cierra el ResultSet para liberar recursos
            respuesta.close();
            return usuario;

        } catch (SQLException e) {
            // Lanza un error para que sea manejado por el método que lo llama
            throw new Error("Error al obtener el usuario");
        }
    }

    /**
     * Método auxiliar para verificar si un correo ya está registrado por otro
     * usuario.
     *
     * @param id ID del usuario actual (para excluirlo de la comparación)
     * @param usuarioData Objeto Usuario con el correo a validar
     * @return true si el correo ya existe en otro usuario, false si no
     */
    private static boolean existEmail(int id, Usuario usuarioData) {
        boolean existe = false; // Bandera para indicar si el correo está duplicado

        try {
            // Obtiene todos los usuarios de la base de datos
            ResultSet respuesta = UsuarioDao.getUsuarios();

            // Recorre los resultados para verificar si el correo está registrado
            while (respuesta.next()) {
                // Si otro usuario (distinto ID) tiene el mismo correo, marca como duplicado
                if (respuesta.getInt("id") != id && usuarioData.getCorreo().equals(respuesta.getString("correo"))) {
                    existe = true;
                }
            }

            // Cierra el ResultSet para liberar recursos
            respuesta.close();
            return existe;

        } catch (SQLException e) {
            // Lanza un error para que sea manejado por el método que lo llama
            throw new Error("Error al validar si el correo existe");
        }
    }

    /**
     * Método auxiliar para validar la contraseña de un usuario.
     *
     * @param usuario_id ID del usuario a validar
     * @param passwordText Contraseña en texto plano a verificar
     * @return true si la contraseña es correcta, false si no
     */
    public static boolean validatePassword(int usuario_id, String passwordText) {
        try {
            // Obtiene la contraseña hasheada del usuario
            String contrasenaHash = obtenerContrasena(usuario_id);

            // Si no se encuentra la contraseña (usuario no existe), retorna false
            if (contrasenaHash == null) {
                return false;
            }

            // Verifica si la contraseña en texto plano coincide con la hasheada
            return checkPassword(passwordText, contrasenaHash); // Corregido: elimina negación (!)

        } catch (Exception e) {
            // Imprime el error para depuración (considerar logger en producción)
            System.out.println(e);
            return false;
        }
    }

    /**
     * Método auxiliar para obtener la contraseña hasheada de un usuario.
     *
     * @param id ID del usuario
     * @return Contraseña hasheada si el usuario existe, null si no
     */
    private static String obtenerContrasena(int id) {
        Usuario usuario = null; // Inicializa el objeto usuario como null

        try {
            // Consulta el usuario por ID a través de la capa DAO
            ResultSet respuesta = UsuarioDao.getUsuarioById(id);

            // Procesa el ResultSet para construir el objeto Usuario
            while (respuesta.next()) {
                usuario = new Usuario(
                        respuesta.getInt("id"), // ID único del usuario
                        respuesta.getString("nombre"), // Nombre del usuario
                        respuesta.getString("apellido"), // Apellido del usuario
                        respuesta.getString("correo"), // Correo electrónico
                        respuesta.getString("contrasena"), // Contraseña hasheada
                        respuesta.getInt("genero_id"), // ID del género
                        respuesta.getInt("ciudad_id"), // ID de la ciudad
                        respuesta.getInt("rol_id"), // ID del rol asignado
                        respuesta.getInt("estado_id") // ID del estado del usuario
                );
            }

            // Cierra el ResultSet para liberar recursos
            respuesta.close();

            // Retorna la contraseña hasheada si el usuario existe, null si no
            if (usuario == null) {
                return null;
            } else {
                return usuario.getContrasena();
            }

        } catch (SQLException e) {
            // Retorna null en caso de error SQL
            return null;
        }
    }

    /**
     * Método auxiliar para hashear una contraseña usando BCrypt.
     *
     * @param contrasenaText Contraseña en textostrukt: texto plano
     * @return Contraseña hasheada
     */
    public static String hashPassword(String contrasenaText) {
        // Genera un hash BCrypt para la contraseña en texto plano
        return BCrypt.hashpw(contrasenaText, BCrypt.gensalt());
    }

    /**
     * Método auxiliar para verificar una contraseña contra su hash.
     *
     * @param contrasenaText Contraseña en texto plano
     * @param hashedContrasena Contraseña hasheada almacenada
     * @return true si la contraseña coincide, false si no
     */
    public static boolean checkPassword(String contrasenaText, String hashedContrasena) {
        // Verifica que la contraseña en texto plano coincida con el hash
        return BCrypt.checkpw(contrasenaText, hashedContrasena);
    }
}
