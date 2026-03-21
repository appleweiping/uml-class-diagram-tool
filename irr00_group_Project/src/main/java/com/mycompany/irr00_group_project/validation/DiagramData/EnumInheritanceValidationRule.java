package com.mycompany.irr00_group_project.validation.DiagramData;

import com.mycompany.irr00_group_project.representation.ClassNode;
import com.mycompany.irr00_group_project.representation.ClassType;
import com.mycompany.irr00_group_project.representation.ConnectionType;
import com.mycompany.irr00_group_project.representation.DiagramData;
import com.mycompany.irr00_group_project.representation.UMLConnection;
import com.mycompany.irr00_group_project.validation.Severity;
import com.mycompany.irr00_group_project.validation.ValidationResult;
import com.mycompany.irr00_group_project.validation.ValidationRule;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Check a diagram for a class which attempts to inherit an enumeration class or an enumeration
 * inheriting another class.
 * 
 * @author Deniz Büyükgüral
 */
public class EnumInheritanceValidationRule implements ValidationRule<DiagramData> {

    /**
     * Checks a diagram for a class which inherits an enumeration or is an enumeration
     * attempting to inherit another class.
     * @param itemToValidate diagram to validate
     * @return {@code ValidationResult.valid()} if there are no classes that inherits an enum.
     *         otherwise, @code ValidationResult.invalid(msg, nodeArr, Severity.ERROR)} where
     *         nodeArr is an array of classes which inherits an enumeration.
     * @throws NullPointerException if itemToValidate is null
     */
    @Override
    public ValidationResult validate(DiagramData itemToValidate) {
        if (itemToValidate == null) {
            throw new NullPointerException();
        }
        
        Set<ClassNode> problematicNodes = new HashSet<>();
        
        for (UMLConnection conn : itemToValidate.getConnections()) {
            
            if (conn.getType() != ConnectionType.INHERITANCE) {
                continue;
            }
            
            ClassType targetType = conn.getTargetClass().getClassType();
            if (targetType == ClassType.ENUM) {
                problematicNodes.add(conn.getSourceClass());
                continue;
            }
            
            ClassType sourceType = conn.getSourceClass().getClassType();
            if (sourceType == ClassType.ENUM) {
                problematicNodes.add(conn.getSourceClass());
                continue;
            }
        }
        
        if (problematicNodes.isEmpty()) {
            return ValidationResult.valid();
        }
        
        String msg = getMessage(List.copyOf(problematicNodes));
        ClassNode[] arr = new ClassNode[problematicNodes.size()];
        problematicNodes.toArray(arr);
        return ValidationResult.invalid(msg, arr, Severity.ERROR);
    }
    
    private static String getMessage(List<ClassNode> nodes) {
        
        StringBuilder nodeStr = new StringBuilder();
        for (int i = 0; i < 3 && i < nodes.size(); i++) {
            
            if (!nodeStr.isEmpty()) {
                nodeStr.append(", ");
            }
            
            String name = nodes.get(i).getClassName();
            if (name.isEmpty()) {
                nodeStr.append("<unnamed>");
            } else {
                nodeStr.append(String.format("'%s'", name));
            }
        }
        
        if (nodes.size() > 3) {
            nodeStr.append(", [...]");
        }
        
        return String.format(
                "Class%s %s %s attempting to inherit an enum"
                        + " or %s trying to inherit another class",
                nodes.size() > 1 ? "es" : "",
                nodeStr.toString(),
                nodes.size() > 1 ? "are" : "is",
                nodes.size() > 1 ? "are enums" : "is an enum");
    }
    
}
