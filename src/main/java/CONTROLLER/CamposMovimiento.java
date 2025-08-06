// Define el paquete donde se encuentra la clase
package CONTROLLER;

// Importa la clase Campo, que representa una regla de validación por atributo
import CONTROLLER.Campo;

// Importa utilidades de listas dinámicas
import java.util.ArrayList;
import java.util.List;

// Clase pública que contiene las reglas de validación para los movimientos
public class CamposMovimiento {
    
    // Método estático que devuelve una lista de campos con sus validaciones
    public static List<Campo> getCampos() {
        
        // Se declara e inicializa una lista vacía que almacenará las reglas
        List<Campo> campos = new ArrayList<>() ;

        // Campo: usuario_id
        // - No obligatorio (false)
        // - Longitud de 1 a 11 dígitos
        // - Tipo numérico
        campos.add(new Campo("usuario_id", false, 1, 11, "number", null));

        // Campo: nombre
        // - Obligatorio (true)
        // - Mínimo 3 caracteres, máximo 50
        // - Tipo texto
        campos.add(new Campo("nombre", true, 3, 50, "string", null));

        // Campo: monto
        // - Obligatorio (true)
        // - Longitud mínima de 3, máxima de 15 dígitos
        // - Tipo numérico
        campos.add(new Campo("monto", true, 3, 15, "number", null));

        // Campo: descripcion
        // - No obligatorio
        // - Longitud de 5 a 50 caracteres
        // - Tipo texto
        campos.add(new Campo("descripcion", false, 5, 50, "string", null));

        // Campo: categoria_id
        // - Obligatorio (true)
        // - Longitud de 1 a 11 dígitos
        // - Tipo numérico
        campos.add(new Campo("categoria_id", true, 1, 11, "number", null));
        
        // Se retorna la lista completa con todas las validaciones
        return campos;
    }
}
