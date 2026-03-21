
package com.mycompany.irr00_group_project.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mycompany.irr00_group_project.representation.ClassNode;
import com.mycompany.irr00_group_project.validation.Strings.CamelCaseFormatValidationRule;
import com.mycompany.irr00_group_project.validation.Strings.PascalCaseFormatValidationRule;
import com.mycompany.irr00_group_project.validation.Strings.VisibilityMarkerValidationRule;
import com.mycompany.irr00_group_project.validation.Strings.NotEmptyValidationRule;

/**
 * A specialized validator that works directly with model objects like ClassNode.
 * It uses the object's getters to retrieve data and validates all of its
 * internal components (name, attributes, operations).
 *   @author Anas mohammad Jebril Yousef Noufal
 */
public class UmlComponentValidator {

    
    private final ValidationRule<String> pascalCaseRule =
        new PascalCaseFormatValidationRule("Class name");
    private final ValidationRule<String> camelCaseRule = new CamelCaseFormatValidationRule("Name");
    private final ValidationRule<String> visibilityRule = new VisibilityMarkerValidationRule();
    private final ValidationRule<String> nonemptyRule = new  NotEmptyValidationRule("Class name");

    private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile(
        "^(?<visibility>[^a-zA-Z0-9_\\\\s])?\\s*(?<name>.+?)\\s*(:\\s*(?<type>.+))?$");

    private static final Pattern OPERATION_PATTERN = Pattern.compile(
        "^(?<visibility>[^a-zA-Z0-9_\\\\s])?\\s*(?<name>[^\\s(]+)\\s*\\((?<params>.*)\\)\\s*"
                + "(:\\s*(?<returnType>.+))?$");

    /**
     * Validates a ClassNode and all of its contents (name, attributes, operations).
     *
     * @param node The ClassNode object to validate.
     * @return A list of all validation problems found within the node.
     */
    public List<ValidationResult> validate(ClassNode node) {
        List<ValidationResult> problems = new ArrayList<>();
        if (node == null) {
            return problems;
        }
        ValidationResult emptyCheckResult = nonemptyRule.validate(node.getClassName());
        if (!emptyCheckResult.isValid()) {
            problems.add(ValidationResult.invalid(emptyCheckResult.getMessage(),
                node, emptyCheckResult.getSeverity()));
        } else {
            ValidationResult formatCheckResult = pascalCaseRule.validate(node.getClassName());
            if (!formatCheckResult.isValid()) {
                problems.add(ValidationResult.invalid(formatCheckResult.getMessage(),
                    node, formatCheckResult.getSeverity()));
            }
        }

        for (String attributeString : node.getAttributes()) {
            validateSingleAttribute(attributeString, node, problems);
        }

        for (String operationString : node.getOperations()) {
            validateSingleOperation(operationString, node, problems);
        }

        return problems;
    }

    private void validateSingleAttribute(String attributeString,
        ClassNode parentNode, List<ValidationResult> problems) {
        if (attributeString == null || attributeString.trim().isEmpty()) {
            return;
        }
        Matcher matcher = ATTRIBUTE_PATTERN.matcher(attributeString.trim());
        if (!matcher.matches()) {
            problems.add(ValidationResult.invalid("Attribute '" 
                + attributeString + "' has an invalid format.", parentNode, Severity.ERROR));
            return;
        }
        
        processSingleAttributeMatcher(matcher, attributeString, parentNode, problems);
    }
    
    private void processSingleAttributeMatcher(Matcher matcher, String attributeString,
            ClassNode parentNode, List<ValidationResult> problems) {
        
        String visibility = matcher.group("visibility");
        String name = matcher.group("name").trim();
        String type = matcher.group("type");

        if (type != null) {
            name = attributeString.substring(visibility != null ? 1 : 0,
                attributeString.indexOf(':')).trim();
        }

        if (visibility != null) {
            ValidationResult vizResult = visibilityRule.validate(visibility);
            if (!vizResult.isValid()) {
                problems.add(ValidationResult.invalid("In attribute '" 
                    + attributeString + "': " + vizResult.getMessage(),
                    parentNode, vizResult.getSeverity()));
            }
        }

        if (name != null && !name.isEmpty()) {
            ValidationResult nameResult = camelCaseRule.validate(name);
            if (!nameResult.isValid()) {
                problems.add(ValidationResult.invalid("In attribute '" 
                    + attributeString + "': " + nameResult.getMessage(),
                        parentNode, nameResult.getSeverity()));
            }
        } else {
            problems.add(ValidationResult.invalid("Attribute '" + attributeString
                + "' is missing a name.", parentNode, Severity.WARNING));
        }
    }

    private void validateSingleOperation(String operationString,
        ClassNode parentNode, List<ValidationResult> problems) {
        if (operationString == null || operationString.trim().isEmpty()) {
            return;
        }

        Matcher matcher = OPERATION_PATTERN.matcher(operationString.trim());
        if (!matcher.matches()) {
            problems.add(ValidationResult.invalid("Operation '" 
                + operationString + "' has an invalid format.", parentNode, Severity.WARNING));
            return;
        }

        String visibility = matcher.group("visibility");
        String name = matcher.group("name").trim();

        if (visibility != null) {
            ValidationResult vizResult = visibilityRule.validate(visibility);
            if (!vizResult.isValid()) {
                problems.add(ValidationResult.invalid("In operation '" 
                    + operationString + "': " + vizResult.getMessage(),
                        parentNode, vizResult.getSeverity()));
            }
        }

        if (name != null && !name.isEmpty()) {
            ValidationResult nameResult = camelCaseRule.validate(name);
            if (!nameResult.isValid()) {
                problems.add(ValidationResult.invalid("In operation '" + operationString 
                    + "': " + nameResult.getMessage(), parentNode, nameResult.getSeverity()));
            }
        }
    }
}