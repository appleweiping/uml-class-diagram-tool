package com.mycompany.irr00_group_project.validation;

import com.mycompany.irr00_group_project.representation.ClassNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the UmlComponentValidator class.
 * These tests define the expected behavior for validating ClassNode objects.
 *  @author Anas mohammad Jebril Yousef Noufal
 */
class UmlComponentValidatorTest {

    private UmlComponentValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UmlComponentValidator();
    }

    @Test
    void testValidNode() {
        ClassNode validNode = new ClassNode();
        validNode.setClassName("ValidClassName");
        validNode.setAttributes(List.of("+attributeOne: String", "#attributeTwo: int"));
        validNode.setOperations(List.of("-operationOne()", "~operationTwo(): void"));

        List<ValidationResult> problems = validator.validate(validNode);

        assertTrue(problems.isEmpty(), "A valid node should have zero validation problems.");
    }

    @Test
    void testValidNode2() {
        ClassNode validNode = new ClassNode();
        validNode.setClassName("ValidClassName");
        validNode.setOperations(List.of("+draw()", "+erase()"));

        List<ValidationResult> problems = validator.validate(validNode);

        assertTrue(problems.isEmpty(), "A valid node should have zero validation problems.");
    }

    @Test
    void testInvalidClassName() {
        ClassNode node = new ClassNode();
        node.setClassName("invalidClassName"); 

        List<ValidationResult> problems = validator.validate(node);

        assertEquals(1, problems.size(), "Should find exactly one problem.");
        assertTrue(problems.get(0).getMessage().contains("must be in PascalCase"));
    }

    @Test
    void testEmptyClassName() {
        ClassNode node = new ClassNode();
        node.setClassName(""); 

        List<ValidationResult> problems = validator.validate(node);

        assertEquals(1, problems.size(), "Should find exactly one problem.");
        assertTrue(problems.get(0).getMessage().contains("cannot be empty."));
    }

    @Test
    void testInvalidAttributeName() {
        ClassNode node = new ClassNode();
        node.setClassName("MyClass");
        node.setAttributes(List.of("-InvalidName: String")); 

        List<ValidationResult> problems = validator.validate(node);

        ValidationResult attributeError = problems.stream()
                .filter(p -> p.getMessage().contains("In attribute") 
                && 
                p.getMessage().contains("must be in camelCase"))
                .findFirst()
                .orElse(null);

        assertNotNull(attributeError, "An error for the invalid attribute name should be found.");
    }

    @Test
    void testInvalidAttributeVisibility() {
        ClassNode node = new ClassNode();
        node.setClassName("MyClass");
        node.setAttributes(List.of("*invalidVisibility: boolean")); // Invalid visibility

        List<ValidationResult> problems = validator.validate(node);

        ValidationResult visibilityError = problems.stream()
                .filter(p -> p.getMessage().contains("In attribute") 
                && p.getMessage().contains("Invalid visibility marker"))
                .findFirst()
                .orElse(null);
        
        assertNotNull(visibilityError,
            "An error for the invalid visibility marker should be found.");
    }

    @Test
    void testInvalidOperationName() {
        ClassNode node = new ClassNode();
        node.setClassName("MyClass");
        node.setOperations(List.of("+InvalidOperation()")); 

        List<ValidationResult> problems = validator.validate(node);

        ValidationResult operationError = problems.stream()
                .filter(p -> p.getMessage().contains("In operation") 
                && p.getMessage().contains("must be in camelCase"))
                .findFirst()
                .orElse(null);

        assertNotNull(operationError, "An error for the invalid operation name should be found.");
    }

    @Test
    void testMalformedOperation() {
        ClassNode node = new ClassNode();
        node.setClassName("MyClass");
        node.setOperations(List.of("+malformedOperation")); // Missing ()

        List<ValidationResult> problems = validator.validate(node);

        ValidationResult formatError = problems.stream()
                .filter(p -> p.getMessage().contains("has an invalid format"))
                .findFirst()
                .orElse(null);

        assertNotNull(formatError, "An error for the malformed operation should be found.");
    }

    @Test
    void testEmptyLists() {
        ClassNode node = new ClassNode();
        node.setClassName("EmptyNode");
        List<ValidationResult> problems = validator.validate(node);

        assertTrue(problems.isEmpty(
            
        ), "A node with empty lists should have no validation problems.");
    }
}