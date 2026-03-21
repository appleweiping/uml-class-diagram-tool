package com.mycompany.irr00_group_project.validation;

import com.mycompany.irr00_group_project.gui.data.Anchor;
import com.mycompany.irr00_group_project.representation.ClassNode;
import com.mycompany.irr00_group_project.representation.ConnectionType;
import com.mycompany.irr00_group_project.representation.DiagramData;
import com.mycompany.irr00_group_project.representation.UMLConnection;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DiagramValidator class.
 * This test suite verifies the behavior of the validate() method
 * under different scenarios including null input, valid diagrams,
 * and diagrams with inheritance cycles.
 *
 * @author Weiping Yan
 * @author Long Pham
 */
public class DiagramValidatorTest {

    /**
     * Test case: diagramData is null.
     * Expectation: the validator should return an empty list with no exceptions.
     */
    @Test
    public void testValidate_withNullDiagram_returnsEmptyList() {
        DiagramValidator validator = new DiagramValidator();

        // Execute with null input
        List<ValidationResult> results = validator.validate(null);

        // The result should be an empty list
        assertTrue(results.isEmpty(), "Validation result should be empty for null input");
    }

    /**
     * Test case: diagram with two valid class nodes and a valid association connection.
     * Expectation: the validator should return an empty list indicating no errors.
     */
    @Test
    public void testValidate_validDiagram_returnsNoErrors() {
        DiagramValidator validator = new DiagramValidator();
        DiagramData diagram = new DiagramData();

        // Create two class nodes
        ClassNode nodeA = new ClassNode();
        nodeA.setClassName("A");
        ClassNode nodeB = new ClassNode();
        nodeB.setClassName("B");

        // Add nodes to the diagram
        assertTrue(diagram.addClassNode(nodeA));
        assertTrue(diagram.addClassNode(nodeB));

        // Create a valid association connection from A to B
        UMLConnection conn = new UMLConnection(nodeA, nodeB, Anchor.TOP, Anchor.TOP);
        conn.setType(ConnectionType.ASSOCIATION);
        assertTrue(diagram.addConnection(conn));

        // Validate the diagram
        List<ValidationResult> results = validator.validate(diagram);

        // There should be no validation errors
        assertTrue(results.isEmpty(), "No validation errors should be present in a valid diagram");
    }

    /**
     * Test case: diagram with a cyclic inheritance structure.
     * A -> B -> A, forming a cycle.
     * Expectation: the validator should detect the cycle and return at least one error.
     */
    @Test
    public void testValidate_inheritanceCycleDetected_returnsError() {
        DiagramData diagram = new DiagramData();

        // Create two class nodes A and B
        ClassNode nodeA = new ClassNode();
        nodeA.setClassName("A");
        ClassNode nodeB = new ClassNode();
        nodeB.setClassName("B");

        // Add both nodes to the diagram
        assertTrue(diagram.addClassNode(nodeA));
        assertTrue(diagram.addClassNode(nodeB));

        // Add inheritance A -> B
        UMLConnection conn1 = new UMLConnection(nodeA, nodeB, Anchor.TOP, Anchor.TOP);
        conn1.setType(ConnectionType.INHERITANCE);

        // Add inheritance B -> A (cycle)
        UMLConnection conn2 = new UMLConnection(nodeB, nodeA, Anchor.TOP, Anchor.TOP);
        conn2.setType(ConnectionType.INHERITANCE);

        assertTrue(diagram.addConnection(conn1));
        assertTrue(diagram.addConnection(conn2));

        DiagramValidator validator = new DiagramValidator();
        
        // Run the validation
        List<ValidationResult> results = validator.validate(diagram);

        // We expect at least one error indicating a cycle
        assertFalse(results.isEmpty(), "Should detect inheritance cycle");

        // Ensure the message indicates a cycle was found
        assertTrue(results.get(0).getMessage().toLowerCase().contains("cycle"),
            "Error message should mention cycle");
    }
    
    /**
     * Test case: diagram with both a cycle and an invalid class name.
     * Expectation: both errors should be reported.
     */
    @Test
    public void testValidate_multipleErrors_detectsAllIssues() {
        DiagramValidator validator = new DiagramValidator();
        DiagramData diagram = new DiagramData();

        // One valid and one unnamed class
        ClassNode nodeA = new ClassNode();
        nodeA.setClassName("A");
        ClassNode nodeB = new ClassNode(); // invalid

        diagram.addClassNode(nodeA);
        diagram.addClassNode(nodeB);

        // Inheritance cycle A -> B -> A
        UMLConnection c1 = new UMLConnection(nodeA, nodeB, Anchor.TOP, Anchor.TOP);
        c1.setType(ConnectionType.INHERITANCE);
        UMLConnection c2 = new UMLConnection(nodeB, nodeA, Anchor.TOP, Anchor.TOP);
        c2.setType(ConnectionType.INHERITANCE);
        diagram.addConnection(c1);
        diagram.addConnection(c2);

        List<ValidationResult> results = validator.validate(diagram);

        assertEquals(2, results.size(), "Should detect both inheritance cycle and invalid class");
    }
}
