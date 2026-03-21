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
import java.util.LinkedList;
import java.util.List;

/**
 * Validation rule checking for classes with more than one outgoing inheritance connections.
 * 
 * @author Deniz Büyükgüral
 */
public class MultiInheritanceValidationRule implements ValidationRule<DiagramData> {

    /**
     * Checks the given diagram for a class that has more than one inheritance relations to
     * other classes.
     * @param itemToValidate the diagram which will be checked for multi inheritance
     * @return {@code ValidationResult.valid()} if all classes in the diagram have at most one
     *         outgoing inheritance connection. otherwise,
     * {@code ValidationResult.invalid(msg, nodeArr, Severity.ERROR)} where nodeArr is the
     * array of class nodes which have at least two outgoing inheritance connections to
     * non interface classes.
     * @throws NullPointerException if itemToValidate is null
     */
    @Override
    public ValidationResult validate(DiagramData itemToValidate) {
        HashMap<ClassNode, Integer> outgoingInheritanceCount = new HashMap<>();
        for (ClassNode node : itemToValidate.getClassNodes()) {
            
            outgoingInheritanceCount.put(node, 0);
        }
        
        List<ClassNode> problematicNodes = new LinkedList<>();
        
        for (UMLConnection conn : itemToValidate.getConnections()) {
            
            if (conn.getType() != ConnectionType.INHERITANCE) {
                continue;
            }
            
            ClassType targetType = conn.getTargetClass().getClassType();
            if (targetType != ClassType.NONE && targetType != ClassType.ABSTRACT) {
                continue;
            }
            
            int currentCount = outgoingInheritanceCount.get(conn.getSourceClass());
            if (currentCount == 1) {
                problematicNodes.addLast(conn.getSourceClass());
            }
            
            outgoingInheritanceCount.put(conn.getSourceClass(), currentCount + 1);
        }
        
        if (problematicNodes.isEmpty()) {
            return ValidationResult.valid();
        }
        
        String msg = getMessage(problematicNodes);
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
                "Class%s %s %s more than one inheritance connection to a non interface class",
                nodes.size() > 1 ? "es" : "",
                nodeStr.toString(),
                nodes.size() > 1 ? "have" : "has");
    }
}
