package com.mycompany.irr00_group_project.validation;

import com.mycompany.irr00_group_project.representation.ClassNode;
import com.mycompany.irr00_group_project.representation.UMLConnection;
import com.mycompany.irr00_group_project.validation.Strings.MultiplicityStringValidationRule;
import java.util.ArrayList;
import java.util.List;

/**
 * A dedicated validator that checks the properties of a UMLConnection.
 * Its single responsibility is to validate a single connection.
 *  * @author Anas Mohammad Jebril Yousef Noufal
 */
public class ConnectionValidator {

    private final ValidationRule<String> multiplicityRule = new MultiplicityStringValidationRule();

    /**
     * Validates a single UMLConnection, checking its multiplicities and other properties.
     * @param connection The connection to validate.
     * @return A list of all validation problems found for this connection.
     */
    public List<ValidationResult> validate(UMLConnection connection) {
        List<ValidationResult> problems = new ArrayList<>();
        if (connection == null) {
            return problems;
        }

        validateMultiplicityField(connection.getSourceMultiplicity(),
            "Source Multiplicity", connection.getSourceClass(), connection, problems);
        validateMultiplicityField(connection.getTargetMultiplicity(),
            "Target Multiplicity", connection.getTargetClass(), connection, problems);


        return problems;
    }

    private void validateMultiplicityField(String multiplicity, String role,
        ClassNode connectedClass, UMLConnection connection, List<ValidationResult> problems) {
        if (multiplicity != null && !multiplicity.isEmpty()) {
            ValidationResult result = multiplicityRule.validate(multiplicity);
            if (!result.isValid()) {
                String className = (connectedClass != null) 
                    ? connectedClass.getClassName() : "unknown class";
                String message = role + " on connection with '" 
                    + className + "': " + result.getMessage();
                problems.add(ValidationResult.invalid(message, connection, result.getSeverity()));
            }
        }
    }
}