package com.mycompany.irr00_group_project.representation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ClassNode.
 * This class ensures the correct behavior of the UML class representation including
 * setting names, types, attributes, operations, layout properties, and outgoing connections.
 *
 * @author Weiping Yan
 */
public class ClassNodeTest {

    private ClassNode node;

    /**
     * Set up a new ClassNode instance before each test.
     */
    @BeforeEach
    public void setUp() {
        node = new ClassNode();
    }

    /**
     * Tests setting and getting the class name.
     */
    @Test
    public void testSetAndGetClassName() {
        node.setClassName("Student");
        assertEquals("Student", node.getClassName());
    }

    /**
     * Tests that setting a null class name throws NullPointerException.
     */
    @Test
    public void testSetClassName_Null_ThrowsException() {
        assertThrows(NullPointerException.class, () -> node.setClassName(null));
    }

    /**
     * Tests setting and getting class type.
     */
    @Test
    public void testSetAndGetClassType() {
        node.setClassType(ClassType.INTERFACE);
        assertEquals(ClassType.INTERFACE, node.getClassType());
    }

    /**
     * Tests that setting a null class type throws NullPointerException.
     */
    @Test
    public void testSetClassType_Null_ThrowsException() {
        assertThrows(NullPointerException.class, () -> node.setClassType(null));
    }

    /**
     * Tests setting and retrieving attributes.
     */
    @Test
    public void testSetAndGetAttributes() {
        node.setAttributes(Arrays.asList("+name: String", "-age: int"));
        List<String> attributes = node.getAttributes();
        assertEquals(2, attributes.size());
        assertTrue(attributes.contains("+name: String"));
    }

    /**
     * Tests that attributes list is unmodifiable from the outside.
     */
    @Test
    public void testAttributesUnmodifiable() {
        node.setAttributes(Collections.singletonList("+id: int"));
        assertThrows(UnsupportedOperationException.class,
                () -> node.getAttributes().add("newAttr"));
    }

    /**
     * Tests setting null attributes throws exception.
     */
    @Test
    public void testSetAttributes_Null_ThrowsException() {
        assertThrows(NullPointerException.class, () -> node.setAttributes(null));
        assertThrows(NullPointerException.class, () -> node.setAttributes(
                List.of("attr1", null, "attr2")));
    }

    /**
     * Tests setting and retrieving operations.
     */
    @Test
    public void testSetAndGetOperations() {
        node.setOperations(Arrays.asList("+getName(): String", "-setAge(int): void"));
        List<String> ops = node.getOperations();
        assertEquals(2, ops.size());
        assertTrue(ops.contains("+getName(): String"));
    }
    
    /**
     * Tests setting null operations throws exception.
     */
    @Test
    public void testSetOperations_Null_ThrowsException() {
        assertThrows(NullPointerException.class, () -> node.setOperations(null));
        assertThrows(NullPointerException.class, () -> node.setOperations(
                List.of("op1", null, "op2")));
    }

    /**
     * Tests layout X and Y setting.
     */
    @Test
    public void testLayoutCoordinates() {
        node.setLayoutX(100.5);
        node.setLayoutY(200.25);
        assertEquals(100.5, node.getLayoutX());
        assertEquals(200.25, node.getLayoutY());
    }

    /**
     * Tests width and height setting.
     */
    @Test
    public void testSizeSetters() {
        node.setWidth(300);
        node.setHeight(150);
        assertEquals(300, node.getWidth());
        assertEquals(150, node.getHeight());
    }

    /**
     * Tests that layout setters reject NaN values.
     */
    @Test
    public void testSetLayoutX_NaN_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> node.setLayoutX(Double.NaN));
    }

    @Test
    public void testSetLayoutY_NaN_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> node.setLayoutY(Double.NaN));
    }

    @Test
    public void testSetWidth_NaN_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> node.setWidth(Double.NaN));
    }

    @Test
    public void testSetHeight_NaN_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> node.setHeight(Double.NaN));
    }
}
