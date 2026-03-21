package com.mycompany.irr00_group_project.validation;

import com.mycompany.irr00_group_project.gui.data.Anchor;
import com.mycompany.irr00_group_project.representation.ClassNode;
import com.mycompany.irr00_group_project.representation.UMLConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ConnectionValidator class.
 * Verifies that it correctly validates the properties of a UMLConnection.
 * 
 * @author Anas Mohammad Jebril Yousef Noufal
 */
class ConnectionValidatorTest {

    private ConnectionValidator validator;
    private ClassNode sourceNode;
    private ClassNode targetNode;

    /**
     * Initializes a new ConnectionValidator and class nodes 
     * before each test.
     */
    @BeforeEach
    void setUp() {
        validator = new ConnectionValidator();
        sourceNode = new ClassNode();
        sourceNode.setClassName("SourceClass");
        targetNode = new ClassNode();
        targetNode.setClassName("TargetClass");
    }

    @Test
    void testValidConnectionShouldHaveNoProblems() {
        UMLConnection connection
                = new UMLConnection(sourceNode, targetNode, Anchor.TOP, Anchor.TOP);
        connection.setSourceMultiplicity("1");
        connection.setTargetMultiplicity("0..*");

        List<ValidationResult> problems = validator.validate(connection);

        assertTrue(problems.isEmpty(),
             "A connection with valid multiplicities should have no problems.");
    }

    @Test
    void testConnectionWithInvalidSourceMultiplicityShouldReportError() {
        UMLConnection connection
                = new UMLConnection(sourceNode, targetNode, Anchor.TOP, Anchor.TOP);
        connection.setSourceMultiplicity("5..1"); // Invalid range
        connection.setTargetMultiplicity("1");

        List<ValidationResult> problems = validator.validate(connection);

        assertEquals(1, problems.size(), "Should find exactly one problem.");
        assertTrue(problems.get(0).getMessage().contains(
            "Source Multiplicity"), "The error message should specify the source side.");
        assertTrue(problems.get(0).getMessage().contains(
            "lower bound must be less than"), "The error message should describe the range issue.");
    }

    @Test
    void testConnectionWithInvalidTargetMultiplicityShouldReportError() {
        UMLConnection connection
                = new UMLConnection(sourceNode, targetNode, Anchor.TOP, Anchor.TOP);
        connection.setSourceMultiplicity("1");
        connection.setTargetMultiplicity("invalid"); // Invalid format

        List<ValidationResult> problems = validator.validate(connection);

        assertEquals(1, problems.size(), "Should find exactly one problem.");
        assertTrue(problems.get(0)
            .getMessage().contains(
                    "Target Multiplicity"), "The error message should specify the target side.");
        assertTrue(problems.get(0).getMessage().contains(
                "Invalid multiplicity format"),
                "The error message should describe the format issue.");
    }
    
    @Test
    void testConnectionWithTwoInvalidMultiplicitiesShouldReportBoth() {
        UMLConnection connection
                = new UMLConnection(sourceNode, targetNode, Anchor.TOP, Anchor.TOP);
        connection.setSourceMultiplicity("bad-source");
        connection.setTargetMultiplicity("1..0");

        List<ValidationResult> problems = validator.validate(connection);

        assertEquals(2, problems.size(),
             "Should find two problems, one for each invalid multiplicity.");
        assertTrue(problems.stream().anyMatch(p -> p.getMessage().contains("Source Multiplicity")),
             "Should contain a source multiplicity error.");
        assertTrue(problems.stream().anyMatch(p -> p.getMessage().contains("Target Multiplicity")),
             "Should contain a target multiplicity error.");
    }

    @Test
    void testConnectionWithNullMultiplicitiesShouldHaveNoProblems() {
        UMLConnection connection
                = new UMLConnection(sourceNode, targetNode, Anchor.TOP, Anchor.TOP);
        connection.setSourceMultiplicity(""); 
        connection.setTargetMultiplicity(""); 

        List<ValidationResult> problems = validator.validate(connection);

        assertTrue(problems.isEmpty(),
             "Empty or null multiplicities should not cause validation errors.");
    }
    
    @Test
    void testNullConnectionShouldReturnEmptyList() {
        List<ValidationResult> problems = validator.validate(null);

        assertNotNull(problems, "The result should not be null.");
        assertTrue(problems.isEmpty(),
             "A null connection should result in an empty list of problems.");
    }
}