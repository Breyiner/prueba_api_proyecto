package MIDDLEWARES;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

@NameBinding // Indica que esta anotación es un Name Binding
@Retention(RetentionPolicy.RUNTIME) // Disponible en tiempo de ejecución
@Target({TYPE, METHOD}) // Puede aplicarse a clases y métodos
public @interface Validar {
}

