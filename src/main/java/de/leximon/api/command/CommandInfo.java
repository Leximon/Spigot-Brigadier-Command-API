package de.leximon.api.command;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
public @interface CommandInfo {
	
	String[] names();
	String[] permissions() default {};
	
	CommandUser[] user() default CommandUser.ALL;
	
}
