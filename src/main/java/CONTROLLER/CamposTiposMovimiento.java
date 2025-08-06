// Define el paquete al que pertenece esta clase
package CONTROLLER;

// Importa la clase Campo que se usa para definir las validaciones de cada campo del formulario
import CONTROLLER.Campo;

// Importa las clases necesarias para trabajar con listas
import java.util.ArrayList;
import java.util.List;

// Clase pública que define los campos requeridos y sus reglas para un tipo de movimiento (por ejemplo, ingreso o gasto)
public class CamposTiposMovimiento {
    
    // Método estático que devuelve una lista con las validaciones para cada campo del tipo de movimiento
    public static List<Campo> getCampos() {
        
        // Se crea una lista vacía para almacenar los campos validados
        List<Campo> campos = new ArrayList<>() ;
        
        // Se agrega el campo "nombre":
        // - Requerido: sí (true)
        // - Mínimo: 3 caracteres
        // - Máximo: 50 caracteres
        // - Tipo de dato: string
        // - Sin expresión regular adicional
        campos.add(new Campo("nombre", true, 3, 50, "string", null));
        
        // Se agrega el campo "icono":
        // - Requerido: sí
        // - Mínimo 3, máximo 50 caracteres
        // - Tipo: string
        campos.add(new Campo("icono", true, 3, 50, "string", null));
        
        // Se agrega el campo "color":
        // - Requerido: sí
        // - Mínimo 2, máximo 7 caracteres (ej: "#F00", "#FF0000")
        // - Tipo: string
        // - Regex: debe comenzar con "#" seguido de 1 a 6 caracteres hexadecimales (números o letras de A a F, mayúsculas o minúsculas)
        campos.add(new Campo("color", true, 2, 7, "string", "^#[0-9A-Fa-f]{1,6}$"));
        
        // Retorna la lista de campos configurados con sus validaciones
        return campos;
    }
}
