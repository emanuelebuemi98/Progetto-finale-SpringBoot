package it.corso.jwt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.ws.rs.NameBinding;

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
//Indica dove era utilizzata l'annotation
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Secured {
	String role() default "all"; //definiamo i permessi di default a tutti
}
