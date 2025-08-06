// Define el paquete donde está esta clase
package CONTROLLER;

// Importa la clase Campo que se usa para definir reglas de validación
import CONTROLLER.Campo;

// Importa las clases para trabajar con listas
import java.util.ArrayList;
import java.util.List;

// Clase pública que define los campos y sus validaciones para el perfil de usuario
public class CamposUsuarioPerfil {
    
    // Método estático que devuelve la lista de campos a validar
    public static List<Campo> getCampos() {
        
        // Crea una lista vacía para almacenar los campos
        List<Campo> campos = new ArrayList<>();
        
        // Agrega campo "nombre"
        // Obligatorio, mínimo 3 caracteres, máximo 50, tipo cadena (string), sin expresión regular
        campos.add(new Campo("nombre", true, 3, 50, "string", null));
        
        // Agrega campo "apellido"
        // Obligatorio, mínimo 3 caracteres, máximo 50, tipo string, sin expresión regular
        campos.add(new Campo("apellido", true, 3, 50, "string", null));
        
        // Agrega campo "correo"
        // Obligatorio, mínimo 3 caracteres, máximo 50, tipo string, sin expresión regular (podría agregarse validación de formato)
        campos.add(new Campo("correo", true, 3, 50, "string", null));
        
        // Agrega campo "genero_id"
        // Obligatorio, mínimo 1 dígito, máximo 11 dígitos, tipo número, sin expresión regular
        campos.add(new Campo("genero_id", true, 1, 11, "number", null));
        
        // Agrega campo "ciudad_id"
        // Obligatorio, mínimo 1 dígito, máximo 11 dígitos, tipo número, sin expresión regular
        campos.add(new Campo("ciudad_id", true, 1, 11, "number", null));
        
        // Devuelve la lista con todos los campos y sus reglas de validación
        return campos;
    }
    
}
