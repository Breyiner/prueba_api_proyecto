package CONTROLLER;

import DAO.UsuarioDao;
import MODEL.Usuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/usuarios")
public class UserController {
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuarios() {
        
        List<Usuario> usuarios = new ArrayList<Usuario>();
        
        try {
            
            ResultSet respuesta = UsuarioDao.getUsuarios();
            
            while (respuesta.next()) {     
                
                Usuario usuario = new Usuario(
                    Integer.parseInt(respuesta.getString("id")),
                    respuesta.getString("nombre"),
                    respuesta.getString("apellido"),
                    respuesta.getString("correo"),
                    respuesta.getString("contrasean"),
                    Integer.parseInt(respuesta.getString("genero_id")),
                    Integer.parseInt(respuesta.getString("ciudad_id")),
                    Integer.parseInt(respuesta.getString("estado_id"))
                );
            
                usuarios.add(usuario);
            }
            
            respuesta.close();
            
            return Response.ok(usuarios).build();
            
        } catch (SQLException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuario(@PathParam("id") int id) {
        
        Usuario usuario = null;
        
        try {
            
            ResultSet respuesta = UsuarioDao.getUsuarioById(id);
            
            while (respuesta.next()) { 
                usuario = new Usuario(
                    respuesta.getInt("id"),
                    respuesta.getString("nombre"),
                    respuesta.getString("apellido"),
                    respuesta.getString("correo"),
                    respuesta.getString("contrasean"),
                    respuesta.getInt("genero_id"),
                    respuesta.getInt("ciudad_id"),
                    respuesta.getInt("estado_id")
                );
            }
            respuesta.close();
            
            return Response.ok(usuario).build();
            
        } catch (SQLException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    @GET
    @Path("correo/{correo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuarioByCorreo(@PathParam("correo") String correo) {
        System.out.println(correo);
        Usuario usuario = null;
        
        try {
            
            ResultSet respuesta = UsuarioDao.getUsuarioByCorreo(correo);
            
            while (respuesta.next()) { 
                usuario = new Usuario(
                    respuesta.getInt("id"),
                    respuesta.getString("nombre"),
                    respuesta.getString("apellido"),
                    respuesta.getString("correo"),
                    respuesta.getString("contrasean"),
                    respuesta.getInt("genero_id"),
                    respuesta.getInt("ciudad_id"),
                    respuesta.getInt("estado_id")
                );
            }
            
            respuesta.close();
            
            return Response.ok(usuario).build();
            
        } catch (SQLException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUsuario(Usuario usuarioData) {
        
        try {
            int idGenerado = 0;
            
            ResultSet ultimoRegistro = UsuarioDao.createUsuario(usuarioData);
            
            if (ultimoRegistro.next()) idGenerado = ultimoRegistro.getInt(1);
            
            usuarioData.setId(idGenerado);
            
            return Response.status(Response.Status.CREATED).entity(usuarioData).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        
    }
    
    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUsuario(@PathParam("id") int id, Usuario usuarioData) {
     
       try {
            
            int rowsAffected = UsuarioDao.updateUsuario(id, usuarioData);
            
            if (rowsAffected != 0) 
                return Response.ok("Usuario actualizado con éxito").build();
            
            else 
                return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error interno del servidor").build();
        }
        
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUsuario(@PathParam("id") int id) {
     
        try {
            
            int rowsAffected = UsuarioDao.deleteUsuario(id);
            
            if (rowsAffected != 0) 
                return Response.status(Response.Status.NO_CONTENT).entity("Usuario eliminado").build();
            
            else 
                return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error interno del servidor").build();
        }
    }
}