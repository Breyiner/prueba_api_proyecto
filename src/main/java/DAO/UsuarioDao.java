package DAO;

import MODEL.Usuario;
import UTILS.ConnectionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDao {
    
    private static final ConnectionDB conn = new ConnectionDB();
    
    public static ResultSet getUsuarios() {
        
        Connection connection = conn.connect();
        
        try {
            
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM usuarios");
            ResultSet respuesta = pstm.executeQuery();
            
            return respuesta;
            
        } catch (SQLException e) {
            return null;
        }
    }
    
    public static ResultSet getUsuarioById(int id) {
        
        Connection connection = conn.connect();
        
        try {
            
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM usuarios WHERE id = ?");
            pstm.setInt(1, id);
            ResultSet respuesta = pstm.executeQuery();
            
            return respuesta;
            
        } catch (SQLException e) {
            return null;
        }
    }
    
    public static ResultSet getUsuarioByCorreo(String correo) {
        
        Connection connection = conn.connect();
        
        try {
            
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM usuarios WHERE correo = ?");
            pstm.setString(1, correo);
            ResultSet respuesta = pstm.executeQuery();
            
            return respuesta;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
   
    public static ResultSet createUsuario(Usuario usuarioData) {
        
        Connection connection = conn.connect();
        
        String query = "INSERT INTO usuarios (nombre, apellido, correo, contrasean, genero_id, ciudad_id, estado_id) VALUES "
                + "(?,?,?,?,?,?,?)";
        
        try {
            
            PreparedStatement pstm = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            
            pstm.setString(1, usuarioData.getNombre());
            pstm.setString(2, usuarioData.getApellido());
            pstm.setString(3, usuarioData.getCorreo());
            pstm.setString(4, usuarioData.getContrasena());
            pstm.setInt(5, usuarioData.getGenero_id());
            pstm.setInt(6, usuarioData.getCiudad_id());
            pstm.setInt(7, usuarioData.getEstado_id());

            pstm.executeUpdate();
            
            ResultSet generatedKeys = pstm.getGeneratedKeys();
            
            return generatedKeys;
            
        } catch (SQLException e) {
            return null;
        }
    }
    
    public static int updateUsuario(int id, Usuario usuarioData) {
        Connection connection = conn.connect();
        
        String query = "UPDATE usuarios SET nombre = ?, apellido = ?, correo = ?, contrasean = ?, genero_id = ?, ciudad_id = ?, estado_id = ? "
                + "WHERE id = ?";
        
        try {
            
            PreparedStatement pstm = connection.prepareStatement(query);
            
            pstm.setString(1, usuarioData.getNombre());
            pstm.setString(2, usuarioData.getApellido());
            pstm.setString(3, usuarioData.getCorreo());
            pstm.setString(4, usuarioData.getContrasena());
            pstm.setInt(5, usuarioData.getGenero_id());
            pstm.setInt(6, usuarioData.getCiudad_id());
            pstm.setInt(7, usuarioData.getEstado_id());
            pstm.setInt(8, id);
            
            int affectedRow = pstm.executeUpdate();
            
            return affectedRow;
            
        } catch (SQLException e) {
            return 0;
        }
    }
    
    public static int deleteUsuario(int id) {
        
        Connection connection = conn.connect();
        
        try {
            
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM usuarios WHERE id = ?");
            pstm.setInt(1, id);
            int affectedRows = pstm.executeUpdate();
            
            return affectedRows;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }
}
