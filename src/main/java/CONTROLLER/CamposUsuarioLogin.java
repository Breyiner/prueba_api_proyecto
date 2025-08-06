// Define el paquete donde se encuentra esta clase
package CONTROLLER;

// Importa la clase Campo desde el mismo paquete, usada para definir validaciones de campos
import CONTROLLER.Campo;

// Importa las clases necesarias para usar listas
import java.util.ArrayList;
import java.util.List;

// Clase pública que define las reglas de validación para el login de usuario
public class CamposUsuarioLogin {

    // Método estático que retorna una lista de objetos Campo con las validaciones necesarias
    public static List<Campo> getCampos() {
        
        // Crea una lista vacía donde se agregarán los campos validados
        List<Campo> campos = new ArrayList<>();

        // Campo: correo del usuario
        // - Obligatorio
        // - Mínimo 3 y máximo 50 caracteres
        // - Tipo: string
        // - No se usa regex aquí, pero podría agregarse para validar formato de correo
        campos.add(new Campo("correo", true, 3, 50, "string", null));

        // Campo: contraseña del usuario
        // - Obligatorio
        // - Mínimo 8 y máximo 20 caracteres
        // - Tipo: string
        campos.add(new Campo("contrasena", true, 8, 20, "string", null));

        // Devuelve la lista con los campos y sus respectivas validaciones
        return campos;
    }

}
