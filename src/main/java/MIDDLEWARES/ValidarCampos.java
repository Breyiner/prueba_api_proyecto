package MIDDLEWARES;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class ValidarCampos {
    
    public static List<String> validar(JSONObject data, List<Campo> campos){
        
        List<String> errores = new ArrayList<>();
        
        for (Campo campo : campos) {
            String name = campo.getName();
            boolean required = campo.isRequired();
            int minLength = campo.getMinimum();
            int maxLength = campo.getMaximum();
            String type = campo.getType();
            String regExp = campo.getRegExp();
            
            if(required && !data.has(name)) {
                errores.add("El campo " + name + " es obligatorio.");
                continue;
            }
            
            if(!required && data.isNull(name)) continue;
            
            Object valueProperty = data.get(name);
            
            if (valueProperty.toString().trim().isEmpty()) errores.add("El campo" + name + " no puede estar vacío.");
            
            switch (type) {
                case "string" -> {
                    if (!(valueProperty instanceof String)) errores.add("El campo " + name + " solo acepta texto.");
                    
                    else {
                        String valor = (String) valueProperty;
                        int longitud = valor.length();
                        
                        if(longitud < minLength) errores.add("El campo " + name + " debe tener mínimo " + minLength + "caracteres.");
                        if(longitud > maxLength) errores.add("El campo " + name + " debe tener máximo " + maxLength + "caracteres.");
                    
                        if(regExp != null && !valor.matches(regExp)) {
                            errores.add("El campo " + name + " no tiene el formato correcto.");
                        }
                    }
                }
                
                case "number" -> {
                    if (!(valueProperty instanceof Number)) errores.add("El campo " + name + " solo acepta números.");
                }
                
                case "boolean" -> {
                    if (!(valueProperty instanceof Boolean)) errores.add("El campo " + name + " solo acepta booleanos.");
                }
                
                default -> errores.add("Este tipo de dato no está relacionado con el campo " + name);
            }
        }
        
        return errores;
    }
}
