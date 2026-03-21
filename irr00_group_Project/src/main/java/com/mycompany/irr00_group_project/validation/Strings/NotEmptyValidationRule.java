package com.mycompany.irr00_group_project.validation.Strings;

import com.mycompany.irr00_group_project.validation.Severity;
import com.mycompany.irr00_group_project.validation.ValidationResult;
import com.mycompany.irr00_group_project.validation.ValidationRule;

/**
 * Validates that a string is not null or empty (after trimming whitespace).
 * @author Anas mohammad Jebril Yousef Noufal
 */
public class NotEmptyValidationRule implements ValidationRule<String> {

    private final String fieldName;

    public NotEmptyValidationRule(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public ValidationResult validate(String itemToValidate) {
        if (itemToValidate == null || itemToValidate.trim().isEmpty()) {
            return ValidationResult.invalid(fieldName + " cannot be empty.", Severity.WARNING);
        }
        return ValidationResult.valid();
    }
}