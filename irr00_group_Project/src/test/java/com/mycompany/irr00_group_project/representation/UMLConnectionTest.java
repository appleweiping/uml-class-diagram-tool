package com.mycompany.irr00_group_project.representation;

import com.mycompany.irr00_group_project.gui.data.Anchor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the UMLConnection class.
 * These tests verify constructor validation, setter/getter consistency,
 * and equality logic.
 *
 * @author Weiping Yan
 */
public class UMLConnectionTest {

    /**
     * Tests the constructor with valid input.
     */
    @Test
    void testConstructorValid() {
        ClassNode source = new ClassNode();
        ClassNode target = new ClassNode();
        UMLConnection connection = new UMLConnection(source, target, Anchor.LEFT, Anchor.RIGHT);
        assertEquals(source, connection.getSourceClass());
        assertEquals(target, connection.getTargetClass());
    }

    /**
     * Verifies that creating a connection with null values throws an exception.
     */
    @Test
    void testConstructorNulls() {
        ClassNode node1 = new ClassNode();
        ClassNode node2 = new ClassNode();
        assertThrows(NullPointerException.class,
                () -> new UMLConnection(null, node1, Anchor.LEFT, Anchor.RIGHT));
        
        assertThrows(NullPointerException.class,
                () -> new UMLConnection(node1, null, Anchor.LEFT, Anchor.RIGHT));
        
        assertThrows(NullPointerException.class,
                () -> new UMLConnection(node1, node2, null, Anchor.RIGHT));
        
        assertThrows(NullPointerException.class,
                () -> new UMLConnection(node1, node2, Anchor.LEFT, null));
    }

    /**
     * Verifies that a class cannot connect to itself.
     */
    @Test
    void testConstructorSameNode() {
        ClassNode node = new ClassNode();
        assertThrows(IllegalArgumentException.class,
                () -> new UMLConnection(node, node, Anchor.LEFT, Anchor.RIGHT));
    }

    /**
     * Tests setting and getting connection type.
     */
    @Test
    void testConnectionTypeSetGet() {
        UMLConnection conn
                = new UMLConnection(new ClassNode(), new ClassNode(), Anchor.LEFT, Anchor.RIGHT);
        conn.setType(ConnectionType.ASSOCIATION);
        assertEquals(ConnectionType.ASSOCIATION, conn.getType());
    }

    /**
     * Tests multiplicity, role name, and anchor setters/getters.
     */
    @Test
    void testSettersAndGetters() {
        UMLConnection conn
                = new UMLConnection(new ClassNode(), new ClassNode(), Anchor.LEFT, Anchor.RIGHT);

        conn.setSourceMultiplicity("1");
        conn.setTargetMultiplicity("0..*");
        conn.setSourceRoleName("src");
        conn.setTargetRoleName("dest");

        assertEquals("1", conn.getSourceMultiplicity());
        assertEquals("0..*", conn.getTargetMultiplicity());
        assertEquals("src", conn.getSourceRoleName());
        assertEquals("dest", conn.getTargetRoleName());
        assertEquals(Anchor.LEFT, conn.getSourceAnchor());
        assertEquals(Anchor.RIGHT, conn.getTargetAnchor());
    }

    /**
     * Tests layout-related fields (offsets and midlines).
     */
    @Test
    void testOffsetsAndMidline() {
        UMLConnection conn
                = new UMLConnection(new ClassNode(), new ClassNode(), Anchor.LEFT, Anchor.RIGHT);

        conn.setSourceOffsetX(10.5);
        conn.setSourceOffsetY(20.5);
        conn.setTargetOffsetX(30.5);
        conn.setTargetOffsetY(40.5);
        conn.setMidlineX(50.5);
        conn.setMidlineY(60.5);

        assertEquals(10.5, conn.getSourceOffsetX());
        assertEquals(20.5, conn.getSourceOffsetY());
        assertEquals(30.5, conn.getTargetOffsetX());
        assertEquals(40.5, conn.getTargetOffsetY());
        assertEquals(50.5, conn.getMidlineX());
        assertEquals(60.5, conn.getMidlineY());
    }

    /**
     * Tests equality and hashCode.
     */
    @Test
    void testEqualsAndHashCode() {
        ClassNode a = new ClassNode();
        a.setClassName("A");
        ClassNode b = new ClassNode();
        b.setClassName("B");

        UMLConnection c1 = new UMLConnection(a, b, Anchor.LEFT, Anchor.RIGHT);
        UMLConnection c2 = new UMLConnection(a, b, Anchor.LEFT, Anchor.RIGHT);

        c1.setType(ConnectionType.INHERITANCE);
        c2.setType(ConnectionType.INHERITANCE);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    /**
     * Tests that different types or directions are not equal.
     */
    @Test
    void testEqualsDifferentCases() {
        ClassNode a = new ClassNode();
        a.setClassName("A");
        ClassNode b = new ClassNode();
        b.setClassName("B");

        UMLConnection c1 = new UMLConnection(a, b, Anchor.LEFT, Anchor.RIGHT);
        UMLConnection c2 = new UMLConnection(b, a, Anchor.LEFT, Anchor.RIGHT);

        assertNotEquals(c1, c2);
    }
}
