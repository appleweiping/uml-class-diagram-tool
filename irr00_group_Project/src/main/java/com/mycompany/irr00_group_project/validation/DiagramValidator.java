package com.mycompany.irr00_group_project.validation;

import com.mycompany.irr00_group_project.representation.DiagramData;
import com.mycompany.irr00_group_project.validation.DiagramData.EnumInheritanceValidationRule;
import com.mycompany.irr00_group_project.validation.DiagramData.InheritanceCycleValidationRule;
import com.mycompany.irr00_group_project.validation.DiagramData.MultiInheritanceValidationRule;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Orchestrates the validation of an entire DiagramData object.
 * It runs diagram-wide validations and delegates component-specific validations
 * to helper validators.
 * @author Anas Mohammad Jebril Yousef Noufal
 */
public class DiagramValidator {

    // Rules that applies to the whole diagram structure.
    private final ValidationRule<DiagramData> inheritanceCycleRule 
        = new InheritanceCycleValidationRule();
    private final ValidationRule<DiagramData> multiInheritanceRule
            = new MultiInheritanceValidationRule();
    private final EnumInheritanceValidationRule enumInheritanceRule
            = new EnumInheritanceValidationRule();
    
    private final ConnectionValidator connectionValidator = new ConnectionValidator();
    
    private final UmlComponentValidator componentValidator = new UmlComponentValidator();

    /**
     * Validates an entire diagram's data.
     * @param diagramData The top-level object containing all diagram information.
     * @return A list of all validation problems found. An empty list means the diagram is valid.
     */
    public List<ValidationResult> validate(DiagramData diagramData) {
        List<ValidationResult> allProblems = new ArrayList<>();
        if (diagramData == null) {
            return allProblems;
        }

        ValidationResult cycleResult = inheritanceCycleRule.validate(diagramData);
        if (!cycleResult.isValid()) {
            allProblems.add(cycleResult);
        }
        
        ValidationResult multiInhResult = multiInheritanceRule.validate(diagramData);
        if (!multiInhResult.isValid()) {
            allProblems.add(multiInhResult);
        }
        
        ValidationResult enumInhResult = enumInheritanceRule.validate(diagramData);
        if (!enumInhResult.isValid()) {
            allProblems.add(enumInhResult);
        }

        List<ValidationResult> classProblems = diagramData.getClassNodes().stream()
                .flatMap(node -> componentValidator.validate(node).stream())
                .collect(Collectors.toList());
        allProblems.addAll(classProblems);

        List<ValidationResult> connectionProblems = diagramData.getConnections().stream()
                .flatMap(connection -> connectionValidator.validate(connection).stream())
                .collect(Collectors.toList());
        allProblems.addAll(connectionProblems);

        return allProblems;
    }

}