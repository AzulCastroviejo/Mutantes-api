package org.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidDnaSequenceValidator implements ConstraintValidator<ValidDnaSequence, String[]> {

    private static final Pattern DNA_PATTERN = Pattern.compile("^[ATCG]+$");
    private static final int MIN_SIZE = 4;

    @Override
    public void initialize(ValidDnaSequence constraintAnnotation) {
        // No necesita inicialización especial
    }

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        // Validar que no sea nulo o vacío
        if (dna == null || dna.length == 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("DNA array cannot be null or empty")
                    .addConstraintViolation();
            return false;
        }

        int n = dna.length;

        // Validar tamaño mínimo
        if (n < MIN_SIZE) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "DNA matrix must be at least " + MIN_SIZE + "x" + MIN_SIZE + ", but was " + n + "x" + n)
                    .addConstraintViolation();
            return false;
        }

        // Validar que sea una matriz cuadrada y caracteres válidos
        for (int i = 0; i < n; i++) {
            String row = dna[i];

            // Validar que la fila no sea nula
            if (row == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("DNA row at index " + i + " cannot be null")
                        .addConstraintViolation();
                return false;
            }

            // Validar que sea cuadrada (NxN)
            if (row.length() != n) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                "DNA matrix must be square (NxN). Expected " + n + " characters, but row " + i + " has " + row.length())
                        .addConstraintViolation();
                return false;
            }

            // Validar que solo contenga A, T, C, G
            if (!DNA_PATTERN.matcher(row).matches()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                "DNA row at index " + i + " contains invalid characters. Only A, T, C, G are allowed")
                        .addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}
