package com.mycompany.irr00_group_project.validation.Strings;

import com.mycompany.irr00_group_project.validation.Severity;
import com.mycompany.irr00_group_project.validation.ValidationResult;
import com.mycompany.irr00_group_project.validation.ValidationRule;
import java.util.regex.Pattern;
/**
 * Validates if a string follows the PascalCase naming convention.
 * PascalCase means:
 * 1. Starts with an uppercase letter.
 * 2. Followed by zero or more alphanumeric characters (letters or digits).
 * Examples: "ClassName", "AnotherExample", "Order1", "User"
 * @author Anas mohammad Jebril Yousef Noufal
 */

public class PascalCaseFormatValidationRule implements ValidationRule<String> {

    // https://stackoverflow.com/questions/2103596/regex-that-matches-camel-and-pascal-case
    private static final Pattern PASCAL_CASE_PATTERN = Pattern
        .compile("^[A-Z](([a-z0-9]+[A-Z]?)*)$"); // Pascal Case - digits allowed source 
    
    private final String fieldName; 
    
    public PascalCaseFormatValidationRule(String fieldname) {
        this.fieldName = fieldname;
    }

    @Override
    public ValidationResult validate(String textToValidate) {

        if (!PASCAL_CASE_PATTERN.matcher(textToValidate).matches()) {
            return ValidationResult.invalid(
            fieldName + " must be in PascalCase (e.g., MyExample, AnotherWord). " 
            + "It should start with an uppercase letter followed by alphanumeric characters.",
            textToValidate, Severity.WARNING);
        }
        return ValidationResult.valid();
    }
}
