package CONTROLLER; // Define que esta clase pertenece al paquete CONTROLLER, encargado de gestionar la lógica del sistema (como validaciones y controladores).

import CONTROLLER.Campo; // Importa la clase Campo, que define la estructura y reglas de validación de un campo.
import java.util.ArrayList; // Importa la clase ArrayList, que permite manejar listas dinámicas.
import java.util.List; // Importa la interfaz List, usada para declarar listas de elementos.

public class CamposAportesMeta { // Clase pública que define los campos que deben ser validados cuando se trabaja con aportes a metas.

    public static List<Campo> getCampos() { // Método público y estático que retorna una lista de objetos Campo con las validaciones necesarias.

        List<Campo> campos = new ArrayList<>(); // Se crea una lista vacía de campos, donde se agregarán las reglas para validar cada campo.

        campos.add(new Campo("meta_id", false, 1, 11, "number", null)); // Se agrega el campo "meta_id", no obligatorio, valor numérico entre 1 y 11.
        campos.add(new Campo("monto", true, 3, 15, "number", null)); // Se agrega el campo "monto", obligatorio, numérico, entre 3 y 15 dígitos.
        campos.add(new Campo("descripcion", false, 5, 50, "string", null)); // Se agrega el campo "descripcion", no obligatorio, tipo texto, entre 5 y 50 caracteres.

        return campos; // Se devuelve la lista completa de campos definidos con sus respectivas reglas de validación.
    }

}
