package com.mycompany.irr00_group_project.validation.Strings;

import com.mycompany.irr00_group_project.validation.Severity;
import com.mycompany.irr00_group_project.validation.ValidationResult;
import com.mycompany.irr00_group_project.validation.ValidationRule;
import java.util.regex.Pattern;

/**
 * Validates if a string follows the camelCase naming convention.
 * Used for UML Attribute, methods, and Parameter names.
 * Examples: "customerName", "calculateTotal"
 * 
 * @author Anas mohammad Jebril Yousef Noufal
 */
public class CamelCaseFormatValidationRule implements ValidationRule<String> {

    private static final Pattern CAMEL_CASE_PATTERN = Pattern
        .compile("^[a-z][a-z0-9]*(([A-Z][a-z0-9]+)*[A-Z]?|([a-z0-9]+[A-Z])*|[A-Z])$");
    private final String fieldName;

    /**
     * Instantiate a new camel case validation rule.
     * @param fieldName name of the field to be used in the validation message
     */
    public CamelCaseFormatValidationRule(String fieldName) {
        this.fieldName = (fieldName == null 
            || fieldName.trim().isEmpty()) ? "Input text" : fieldName.trim();
    }

    public CamelCaseFormatValidationRule() {
        this("Input text");
    }

    @Override
    public ValidationResult validate(String textToValidate) {
        if (textToValidate == null || textToValidate.trim().isEmpty()) {
            return ValidationResult.invalid(fieldName + " cannot be empty.",
                 textToValidate, Severity.WARNING);
        }

        if (!CAMEL_CASE_PATTERN.matcher(textToValidate).matches()) {
            return ValidationResult.invalid(
                fieldName 
                + " must be in camelCase (e.g., myAttribute)."
                + "It must start with a lowercase letter "
                + "and be followed by alphanumeric characters.",
                textToValidate,
                Severity.WARNING
            );
        }

        return ValidationResult.valid();
    }
}