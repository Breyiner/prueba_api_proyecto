// Paquete al que pertenece esta clase
package CONTROLLER;

// Importa la clase Campo desde el mismo paquete (probablemente una clase que define atributos y reglas de validación)
import CONTROLLER.Campo;

// Importa las clases necesarias para trabajar con listas
import java.util.ArrayList;
import java.util.List;

// Define la clase pública CamposCiudad
public class CamposCiudad {
    
    // Método estático que devuelve una lista de objetos Campo
    public static List<Campo> getCampos() {
        
        // Crea una nueva lista vacía de tipo Campo
        List<Campo> campos = new ArrayList<>() ;
        
        // Agrega un nuevo campo a la lista con las siguientes reglas:
        // - nombre del campo: "nombre"
        // - es obligatorio: true
        // - longitud mínima: 3
        // - longitud máxima: 50
        // - tipo de dato: "string"
        // - expresión regular: null (no se aplica ninguna)
        campos.add(new Campo("nombre", true, 3, 50, "string", null));
        
        // Retorna la lista de campos configurados
        return campos;
    }
}
