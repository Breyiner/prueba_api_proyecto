package CONTROLLER; // Indica que esta clase forma parte del paquete CONTROLLER, que agrupa las clases relacionadas con la lógica de control de la aplicación.

public class Campo { // Define una clase pública llamada Campo, que representa las reglas de validación de un campo (por ejemplo, uno recibido en un formulario).

    private final String name; // Nombre del campo, como "correo", "nombre", etc.
    private final boolean required; // Define si este campo es obligatorio (true) o no (false).
    private final int minimum; // Valor mínimo permitido. Si es texto, es la longitud mínima; si es número, es el valor mínimo.
    private final int maximum; // Valor máximo permitido. Igual que el anterior, depende del tipo.
    private final String type; // Tipo de dato que se espera: puede ser "string", "numero" o "booleano".
    private final String regExp; // Expresión regular usada para validar el formato del campo (por ejemplo, formato de correo electrónico).

    public Campo(String name, boolean required, int minimum, int maximum, String type, String regExp) { // Constructor que permite crear una instancia de Campo con todos sus atributos definidos.
        this.name = name; // Se asigna el nombre del campo.
        this.required = required; // Se indica si es obligatorio o no.
        this.minimum = minimum; // Se asigna el valor mínimo permitido.
        this.maximum = maximum; // Se asigna el valor máximo permitido.
        this.type = type; // Se define el tipo de dato que se espera.
        this.regExp = regExp; // Se asigna la expresión regular para validación del contenido.
    }

    public String getRegExp() { // Método que devuelve la expresión regular del campo.
        return regExp; // Retorna el valor del atributo regExp.
    }

    public String getName() { // Método que devuelve el nombre del campo.
        return name; // Retorna el valor del atributo name.
    }

    public boolean isRequired() { // Método que indica si el campo es obligatorio.
        return required; // Retorna true si el campo es obligatorio, false si no lo es.
    }

    public int getMinimum() { // Método que devuelve el valor mínimo permitido para el campo.
        return minimum; // Retorna el valor del atributo minimum.
    }

    public int getMaximum() { // Método que devuelve el valor máximo permitido para el campo.
        return maximum; // Retorna el valor del atributo maximum.
    }

    public String getType() { // Método que devuelve el tipo de dato que debe tener el campo.
        return type; // Retorna el valor del atributo type.
    }
}
