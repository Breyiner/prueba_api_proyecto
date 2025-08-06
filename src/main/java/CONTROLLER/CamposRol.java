// Define el paquete donde está ubicada la clase
package CONTROLLER;

// Importa la clase Campo, usada para definir las validaciones de cada atributo
import CONTROLLER.Campo;

// Importa clases necesarias para manejar listas
import java.util.ArrayList;
import java.util.List;

// Clase pública que contiene las reglas de validación para los roles
public class CamposRol {
    
    // Método estático que devuelve una lista con los campos validados
    public static List<Campo> getCampos() {
        
        // Se crea una nueva lista vacía de campos de tipo Campo
        List<Campo> campos = new ArrayList<>() ;
        
        // Se agrega el campo "nombre":
        // - Obligatorio (true)
        // - Mínimo 3 caracteres, máximo 50
        // - Tipo: string
        // - No se aplica una expresión regular (null)
        campos.add(new Campo("nombre", true, 3, 50, "string", null));
        
        // Se retorna la lista completa de campos validados
        return campos;
    }
}
