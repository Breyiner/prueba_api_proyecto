// Paquete al que pertenece esta clase
package CONTROLLER;

// Importa la clase Campo, que representa una definición de validación para un campo de entrada
import CONTROLLER.Campo;

// Importa clases necesarias para trabajar con listas
import java.util.ArrayList;
import java.util.List;

// Clase pública que define las reglas de validación para cada campo del formulario de registro de usuario
public class CamposUsuario {
    
    // Método estático que retorna una lista con las validaciones para los campos de un usuario
    public static List<Campo> getCampos() {
        
        // Se crea una lista vacía para almacenar los campos con sus respectivas reglas de validación
        List<Campo> campos = new ArrayList<>();

        // Campo: nombre del usuario
        // - Obligatorio
        // - Mínimo 3, máximo 50 caracteres
        // - Tipo string
        campos.add(new Campo("nombre", true, 3, 50, "string", null));

        // Campo: apellido del usuario
        // - Obligatorio
        // - Mismo criterio que el nombre
        campos.add(new Campo("apellido", true, 3, 50, "string", null));

        // Campo: correo del usuario
        // - Obligatorio
        // - Entre 3 y 50 caracteres
        // - Tipo string (puede mejorarse con una expresión regular para validar formato de correo)
        campos.add(new Campo("correo", true, 3, 50, "string", null));

        // Campo: contraseña del usuario
        // - Obligatorio
        // - Mínimo 8, máximo 20 caracteres
        campos.add(new Campo("contrasena", false, 8, 20, "string", null));

        // Campo: género (ID relacionado con la tabla de géneros)
        // - Obligatorio
        // - Entero entre 1 y 11 dígitos
        campos.add(new Campo("genero_id", true, 1, 11, "number", null));

        // Campo: ciudad (ID relacionado con la tabla de ciudades)
        // - Obligatorio
        // - Entero entre 1 y 11 dígitos
        campos.add(new Campo("ciudad_id", true, 1, 11, "number", null));
        
        // Devuelve la lista completa de campos con sus reglas
        return campos;
    }
}
