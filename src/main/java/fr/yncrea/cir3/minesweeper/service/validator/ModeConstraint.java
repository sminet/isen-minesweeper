package fr.yncrea.cir3.minesweeper.service.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = ModeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModeConstraint {
	String message() default "Nombre de mines invalide";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
