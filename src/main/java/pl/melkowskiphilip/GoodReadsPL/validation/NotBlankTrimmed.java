package pl.melkowskiphilip.GoodReadsPL.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBlankTrimmedValidator.class)
public @interface NotBlankTrimmed {

    String message() default "Pole nie może być puste ani zawierać tylko spacji";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}