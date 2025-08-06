package CONTROLLER; // Define que esta clase pertenece al paquete CONTROLLER, encargado de la lógica de control de la aplicación.

import CONTROLLER.Campo; // Importa la clase Campo, que contiene la estructura y validación de cada campo.
import java.util.ArrayList; // Importa la clase ArrayList para poder crear una lista dinámica.
import java.util.List; // Importa la interfaz List, que define una colección de elementos (en este caso, campos).

public class CamposCategorias { // Clase pública que define los campos necesarios y sus reglas de validación para una categoría.

    public static List<Campo> getCampos() { // Método estático que retorna una lista de objetos Campo con la configuración esperada para cada campo de categoría.

        List<Campo> campos = new ArrayList<>(); // Se crea una lista vacía donde se agregarán los campos a validar.

        campos.add(new Campo("nombre", true, 3, 50, "string", null)); // Se agrega el campo "nombre", obligatorio, tipo texto, mínimo 3 y máximo 50 caracteres.
        campos.add(new Campo("icono", true, 3, 50, "string", null)); // Se agrega el campo "icono", obligatorio, tipo texto, mínimo 3 y máximo 50 caracteres.
        campos.add(new Campo("tipo_movimiento_id", true, 1, 11, "number", null)); // Se agrega el campo "tipo_movimiento_id", obligatorio, numérico, entre 1 y 11 dígitos.

        return campos; // Retorna la lista con todos los campos definidos para la validación.
    }

}
