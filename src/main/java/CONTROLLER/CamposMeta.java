// Define el paquete donde se encuentra esta clase
package CONTROLLER;

// Importa la clase Campo, que contiene la estructura de una regla de validación
import CONTROLLER.Campo;

// Importa utilidades para trabajar con listas dinámicas
import java.util.ArrayList;
import java.util.List;

// Clase pública que expone los campos requeridos para validar una "Meta"
public class CamposMeta {
    
    // Método estático que retorna una lista con las validaciones de cada campo
    public static List<Campo> getCampos() {
        
        // Se inicializa una lista vacía de campos
        List<Campo> campos = new ArrayList<>() ;

        // Se agregan los campos con sus respectivas validaciones:

        // Campo: usuario_id
        // - No es obligatorio (false)
        // - Mínimo 1 dígito, máximo 11
        // - Tipo numérico
        campos.add(new Campo("usuario_id", false, 1, 11, "number", null));

        // Campo: nombre
        // - Obligatorio (true)
        // - Mínimo 3 caracteres, máximo 50
        // - Tipo texto
        campos.add(new Campo("nombre", true, 3, 50, "string", null));

        // Campo: monto
        // - Obligatorio (true)
        // - Longitud de 3 a 15 dígitos
        // - Tipo numérico
        campos.add(new Campo("monto", true, 3, 15, "number", null));

        // Campo: descripción
        // - No es obligatorio
        // - De 5 a 50 caracteres
        // - Tipo texto
        campos.add(new Campo("descripcion", false, 5, 50, "string", null));

        // Campo: fecha_limite
        // - No es obligatorio
        // - Debe tener exactamente 10 caracteres (formato "yyyy-MM-dd")
        // - Tipo fecha
        // - Expresión regular que valida fechas en formato ISO: AAAA-MM-DD
        campos.add(new Campo("fecha_limite", false, 10, 10, "date", "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$"));
        
        // Se retorna la lista de campos con sus reglas de validación
        return campos;
    }
}
