package com.mycompany.irr00_group_project.validation.Strings;

import com.mycompany.irr00_group_project.validation.Severity;
import com.mycompany.irr00_group_project.validation.ValidationResult;
import com.mycompany.irr00_group_project.validation.ValidationRule;
import java.util.regex.Pattern;

/**
 * Validates if a string matches the UML stereotype format: "&lt;&lt;stereotypeName&gt;&gt;".
 * The name can contain alphanumeric characters and underscores.
 * 
 * Examples: <code>&lt;&lt;Interface&gt;&gt;
 * </code>,<code>&lt;&lt;Entity&gt;&gt;</code>
 * 
 * @author Anas mohammad Jebril Yousef Noufal
 * @author Long Pham
 */
public class StereotypeFormatValidationRule implements ValidationRule<String> {

    private static final Pattern STEREOTYPE_PATTERN = Pattern.compile("^<<[a-zA-Z0-9_]+>>$");

    @Override
    public ValidationResult validate(String textToValidate) {
        if (textToValidate == null || textToValidate.trim().isEmpty()) {
            return ValidationResult.invalid("Stereotype text cannot be empty.",
                textToValidate, Severity.ERROR);
        }

        if (!STEREOTYPE_PATTERN.matcher(textToValidate).matches()) {
            return ValidationResult.invalid(
                "Invalid stereotype format. Expected format is <<StereoName>> with no spaces.",
                textToValidate, Severity.ERROR
            );
        }

        return ValidationResult.valid();
    }
}