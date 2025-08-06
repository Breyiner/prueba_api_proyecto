// Define el paquete al que pertenece esta clase
package CONTROLLER;

// Importa la clase Campo que contiene la estructura de validación
import CONTROLLER.Campo;

// Importa las clases necesarias para trabajar con listas
import java.util.ArrayList;
import java.util.List;

// Declaración pública de la clase CamposGenero
public class CamposGenero {
    
    // Método estático que retorna una lista de reglas de validación para la entidad "Género"
    public static List<Campo> getCampos() {
        
        // Se inicializa una lista vacía de objetos Campo
        List<Campo> campos = new ArrayList<>();
        
        // Se añade una regla de validación para el campo "nombre":
        // - Obligatorio: true
        // - Longitud mínima: 3 caracteres
        // - Longitud máxima: 50 caracteres
        // - Tipo de dato: string
        // - Sin expresión regular (null)
        campos.add(new Campo("nombre", true, 3, 50, "string", null));
        
        // Se retorna la lista de campos con sus respectivas validaciones
        return campos;
    }
}
