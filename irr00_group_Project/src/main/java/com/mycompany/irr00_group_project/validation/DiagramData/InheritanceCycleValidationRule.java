package com.mycompany.irr00_group_project.validation.DiagramData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mycompany.irr00_group_project.representation.ClassNode;
import com.mycompany.irr00_group_project.representation.ConnectionType;
import com.mycompany.irr00_group_project.representation.DiagramData;
import com.mycompany.irr00_group_project.representation.UMLConnection;
import com.mycompany.irr00_group_project.validation.Severity;
import com.mycompany.irr00_group_project.validation.ValidationResult;
import com.mycompany.irr00_group_project.validation.ValidationRule;

/**
 * A validation rule that checks for inheritance cycles in a UML diagram.
 * An inheritance cycle occurs when a class inherits (directly or indirectly) from itself,
 * forming a loop in the class hierarchy. This class implements the
 * {@link ValidationRule} interface for {@link DiagramData}.
 * For example, in the case A → B → C → A, this rule would detect the cycle and
 * return a {@link ValidationResult} indicating the error.
 *
 * 
 * @author Abdul Gadaborchev
 * @author Anas Mohammad Jebril Yousef Noufal
 */
public class InheritanceCycleValidationRule implements ValidationRule<DiagramData> {

    /**
     * Checks a diagram for inheritance cycle. An inheritance cycle is a cycle in a graph
     * where vertices are the class nodes and directed edges are directed inheritance connections.
     * @param itemToValidate diagram to validate
     * @pre itemToValidate != null
     * @return {@code ValidationResult.valid()} if there are no inheritance cycles.
        Otherwise, {@code ValidationResult.invalid(msg, nodeArr, Severity.ERROR)}
        where nodeArr is the array of nodes in the cycle (if multiple cycles exist,
        result can be returned after finding any of the cycles).
     * @throws IllegalArgumentException if the preconditions are violated

     */
    @Override
    public ValidationResult validate(DiagramData itemToValidate) {
        if (itemToValidate == null) {
            throw new IllegalArgumentException("Diagram data must not be null.");
        }

        List<UMLConnection> inheritanceOnly = new ArrayList<>();
        
        for (UMLConnection conn: itemToValidate.getConnections()) {
            if (conn.getType() == ConnectionType.INHERITANCE) {
                inheritanceOnly.add(conn);
            }
        }

        Map<ClassNode, List<ClassNode>> inheritanceMap = new HashMap<>();
        
        for (UMLConnection conn: inheritanceOnly) {
            ClassNode child = conn.getSourceClass();
            ClassNode parent = conn.getTargetClass();
            inheritanceMap.computeIfAbsent(child, k -> new ArrayList<>()).add(parent);
        }
        
        Set<ClassNode> visited = new HashSet<>();  
        Set<ClassNode> recStack = new HashSet<>();
        
        for (ClassNode child : inheritanceMap.keySet()) {
            if (!visited.contains(child)) {
                ValidationResult result 
                    = detectCycle(child, inheritanceMap, visited, recStack);                
                if (!result.isValid()) {
                    return result;
                }
            }
        }
        return ValidationResult.valid(); 
    }

    private ValidationResult detectCycle(
        ClassNode className,
        Map<ClassNode, List<ClassNode>> inheritanceGraph,
        Set<ClassNode> visited,
        Set<ClassNode> recStack
    ) {
        if (recStack.contains(className)) {
            List<ClassNode> nodesInCycle = new ArrayList<>(recStack);
            return ValidationResult.invalid(
                "Inheritance cycle detected involving class: " + className.getClassName(),
                nodesInCycle.toArray(new ClassNode[0]),
                Severity.ERROR
            );
        }
        if (visited.contains(className)) {
            return ValidationResult.valid(); 
        }

        recStack.add(className);

        List<ClassNode> parents = inheritanceGraph.get(className);
        if (parents != null) {
            for (ClassNode p : parents) {
                if (p != null) {
                    ValidationResult result = detectCycle(p, inheritanceGraph, visited, recStack);
                    if (!result.isValid()) {
                        return result;
                    }
                }
            }
        }


        recStack.remove(className);
        visited.add(className);
        

        return ValidationResult.valid();
        
        
    }
}
