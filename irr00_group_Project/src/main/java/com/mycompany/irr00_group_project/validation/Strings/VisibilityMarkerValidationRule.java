package com.mycompany.irr00_group_project.validation.Strings;

import com.mycompany.irr00_group_project.validation.Severity;
import com.mycompany.irr00_group_project.validation.ValidationResult;
import com.mycompany.irr00_group_project.validation.ValidationRule;
import java.util.regex.Pattern;

/**
 * Validates if a string is a valid UML visibility marker.
 * Allowed markers: "+" (public), "-" (private), "#" (protected), "~" (package).
 * @author Anas mohammad Jebril Yousef Noufal
 */
public class VisibilityMarkerValidationRule implements ValidationRule<String> {

    private static final Pattern VISIBILITY_MARKER_PATTERN = Pattern.compile("^[-+#~]$");

    @Override
    public ValidationResult validate(String textToValidate) {
        if (textToValidate == null || textToValidate.trim().isEmpty()) {
            return ValidationResult.invalid("Visibility marker cannot be empty.",
                textToValidate, Severity.WARNING);
        }

        if (!VISIBILITY_MARKER_PATTERN.matcher(textToValidate).matches()) {
            return ValidationResult
            .invalid("Invalid visibility marker. Must be one of: +, -, #, ~",
                 textToValidate, Severity.WARNING);
        }

        return ValidationResult.valid();
    }
}