package com.mycompany.irr00_group_project.validation;

/**
 * an ADT Represents the outcome of a validation operation.
 * This class is immutable. 
 * @author Abdul Gadaborchev
 * @author Anas Mohammad Jebril Yousef Noufal
 */
public class ValidationResult {

    private final boolean isValid;
    private final String message;
    private final Object problematicElement;
    private final Severity severity;

    /**
     * Constructs a new ValidationResult.
     *
     * @param isValid true if the validation passed, false otherwise.
     * @param message A message describing the validation outcome, especially on failure.
     * @param problematicElement Optional reference to the specific
     * element that caused a validation failure.
     */
    public ValidationResult(boolean isValid, String message,
            Object problematicElement, Severity severity) {
        this.isValid = isValid;
        this.message = message;
        this.problematicElement = problematicElement;
        this.severity  = severity;
    }

    /**
     * Checks if the validation was successful.
     * @return true if valid, false otherwise.
     */
    public boolean isValid() {
        return isValid;
    }

    /**
     * Gets the validation message.
     * Typically empty for valid results or contains an error description for invalid ones.
     * @return The validation message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the element that caused the validation to fail, if applicable.
     * @return The problematic element, or null if not applicable or if validation passed.
     */
    public Object getProblematicElement() {
        return problematicElement;
    }
    
    /**
     * Gets the severity of the validation message.
     * @return severity of the validation message
     */
    public Severity getSeverity() {
        return severity;
    }

    /**
     * Creates a ValidationResult representing a successful validation.
     * @return A new valid ValidationResult instance.
     */
    public static ValidationResult valid() {
        return new ValidationResult(true, "", null, Severity.INFORMATION);
    }

    /**
     * Creates a ValidationResult representing a failed validation.
     * @param message The error message.
     * @return A new invalid ValidationResult instance.
     */
    public static ValidationResult invalid(String message, Severity severity) {
        return new ValidationResult(false, message, null, severity);
    }

    /**
     * Creates a ValidationResult representing a failed validation, 
     * referencing the problematic element.
     * @param message The error message.
     * @param element The element that caused the failure.
     * @return A new invalid ValidationResult instance.
     */
    public static ValidationResult invalid(String message, Object element, Severity severity) {
        return new ValidationResult(false, message, element, severity);
    }
}