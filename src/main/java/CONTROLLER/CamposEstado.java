// Indica que esta clase pertenece al paquete CONTROLLER
package CONTROLLER;

// Importa la clase Campo, que representa una regla de validación
import CONTROLLER.Campo;

// Importa las clases necesarias para trabajar con listas en Java
import java.util.ArrayList;
import java.util.List;

// Define la clase pública CamposEstado
public class CamposEstado {
    
    // Método estático que retorna una lista de campos con sus respectivas validaciones
    public static List<Campo> getCampos() {
        
        // Se crea una lista vacía que almacenará objetos de tipo Campo
        List<Campo> campos = new ArrayList<>();
        
        // Se agrega un campo con las siguientes reglas de validación:
        // - nombre del campo: "nombre"
        // - es obligatorio: true
        // - longitud mínima: 3 caracteres
        // - longitud máxima: 50 caracteres
        // - tipo de dato: "string"
        // - expresión regular: null (no se aplica ninguna regla de patrón)
        campos.add(new Campo("nombre", true, 3, 50, "string", null));
        
        // Se retorna la lista completa de campos definidos
        return campos;
    }
}
