package CONTROLLER;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class ValidarCampos {
    
    // Método que valida un JSONObject basado en una lista de reglas (campos)
    public static List<String> validar(JSONObject data, List<Campo> campos){
        
        List<String> errores = new ArrayList<>(); // Lista para acumular mensajes de error
        
        // Recorre cada campo con sus reglas para validarlo contra el JSONObject
        for (Campo campo : campos) {
            String name = campo.getName();          // Nombre del campo a validar
            boolean required = campo.isRequired();  // Si es obligatorio
            int minLength = campo.getMinimum();     // Tamaño mínimo (para strings)
            int maxLength = campo.getMaximum();     // Tamaño máximo (para strings)
            String type = campo.getType();          // Tipo esperado: string, number, boolean, date
            String regExp = campo.getRegExp();      // Expresión regular para validación de formato
            
            // Si el campo es obligatorio y no está presente, se añade error y pasa al siguiente campo
            if(required && !data.has(name)) {
                errores.add("El campo " + name + " es obligatorio.");
                continue; // No sigue validando este campo porque no está presente
            }
            
            // Si no es obligatorio y está presente pero es nulo, se omite la validación
            if(!required && data.isNull(name)) continue;
            
            // Obtiene el valor del campo del JSONObject
            Object valueProperty = data.get(name);
            
            // Si el valor está vacío (string vacío o espacios), añade error
            if (valueProperty.toString().trim().isEmpty()) 
                errores.add("El campo " + name + " no puede estar vacío.");
            
            // Valida según el tipo esperado
            switch (type) {
                
                case "string" -> {
                    // Verifica que sea un String
                    if (!(valueProperty instanceof String)) 
                        errores.add("El campo " + name + " solo acepta texto.");
                    else {
                        String valor = (String) valueProperty;
                        int longitud = valor.length();
                        
                        // Valida longitud mínima
                        if(longitud < minLength) 
                            errores.add("El campo " + name + " debe tener mínimo " + minLength + " caracteres.");
                        
                        // Valida longitud máxima
                        if(longitud > maxLength) 
                            errores.add("El campo " + name + " debe tener máximo " + maxLength + " caracteres.");
                        
                        // Valida formato con expresión regular si se definió
                        if(regExp != null && !valor.matches(regExp)) {
                            errores.add("El campo " + name + " no tiene el formato correcto.");
                        }
                    }
                }
                
                case "number" -> {
                    // Verifica que sea instancia de Number
                    if (!(valueProperty instanceof Number)) 
                        errores.add("El campo " + name + " solo acepta números.");
                }
                
                case "boolean" -> {
                    // Verifica que sea instancia de Boolean
                    if (!(valueProperty instanceof Boolean)) 
                        errores.add("El campo " + name + " solo acepta booleanos.");
                }
                
                case "date" -> {
                    // Asume que el valor es string para la fecha
                    String valor = (String) valueProperty;
                    // Valida formato de fecha con expresión regular si existe
                    if(regExp != null && !valor.matches(regExp)) {
                        errores.add("El campo " + name + " no tiene el formato correcto.");
                    }
                }
                
                // Si el tipo no coincide con ninguno esperado, añade error genérico
                default -> errores.add("Este tipo de dato no está relacionado con el campo " + name);
            }
        }
        
        // Devuelve la lista con todos los errores encontrados
        return errores;
    }
}
