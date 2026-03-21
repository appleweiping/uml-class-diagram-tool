package com.mycompany.irr00_group_project.validation.Strings;

import java.util.regex.Pattern;

import com.mycompany.irr00_group_project.validation.Severity;
import com.mycompany.irr00_group_project.validation.ValidationResult;
import com.mycompany.irr00_group_project.validation.ValidationRule;

/**
 * Validates common UML multiplicity string formats.
 * This includes a semantic check to ensure that in a range "m..n", m is not greater than n.
 * Examples: "*", "1", "0..1", "1..*", "5..10"
 * @author Anas mohammad Jebril Yousef Noufal
 */
public class MultiplicityStringValidationRule implements ValidationRule<String> {

    private static final Pattern MULTIPLICITY_PATTERN = Pattern
        .compile("^(\\*|\\d+|\\d+\\.\\.(\\*|\\d+))$");
    private static final String ERROR_MESSAGE_FORMAT =
        "Invalid multiplicity format. Examples: '*', '1', '0..1', '1..*', 'm..n'.";
    private static final String ERROR_MESSAGE_ORDER =
        "Invalid multiplicity range: lower bound must be less than or equal to upper bound"
            + " (e.g., '1..5', not '5..1').";

    @Override
    public ValidationResult validate(String textToValidate) {
        if (textToValidate == null || textToValidate.trim().isEmpty()) {
            return ValidationResult.invalid("Multiplicity cannot be empty.",
                 textToValidate, Severity.ERROR);
        }

        if (!MULTIPLICITY_PATTERN.matcher(textToValidate).matches()) {
            return ValidationResult.invalid(ERROR_MESSAGE_FORMAT, textToValidate, Severity.ERROR);
        }

        if (textToValidate.matches("\\d+\\.\\.\\d+")) {
            String[] parts = textToValidate.split("\\.\\.");
            try {
                int lower = Integer.parseInt(parts[0]);
                int upper = Integer.parseInt(parts[1]);
                if (lower > upper) {
                    return ValidationResult.invalid(ERROR_MESSAGE_ORDER,
                        textToValidate, Severity.ERROR);
                }
            } catch (NumberFormatException e) {
                // just for safety, the regex the regex should filter this 
                return ValidationResult
                .invalid("Invalid numbers found in multiplicity range.",
                     textToValidate, Severity.ERROR);
            }
        }

        return ValidationResult.valid();
    }
}