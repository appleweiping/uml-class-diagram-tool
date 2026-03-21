package com.mycompany.irr00_group_project.representation;

import com.mycompany.irr00_group_project.gui.data.Anchor;
import com.mycompany.irr00_group_project.listeners.Observer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for DiagramData.
 * 
 * @author Long Pham
 */
public class DiagramDataTest {
    
    private DiagramData diagram;

    @BeforeEach
    void setUp() {
        diagram = new DiagramData();
    }

    /**
     * Tests adding a valid class node to the diagram.
     */
    @Test
    void testAddClassNode_Valid() {
        ClassNode node = new ClassNode();
        assertTrue(diagram.addClassNode(node));
        assertTrue(diagram.getClassNodes().contains(node));
    }
    
    /**
     * Tests adding a duplicate class node returns false.
     */
    @Test
    void testAddClassNode_Duplicate() {
        ClassNode node = new ClassNode();
        diagram.addClassNode(node);
        assertFalse(diagram.addClassNode(node));
    }
    
    /**
     * Tests adding another class node already part of another diagram
     * throws an exception.
     */
    @Test
    void testAddClassNode_AlreadyInAnotherDiagram() {
        ClassNode node = new ClassNode();
        node.setParent(new DiagramData());
        assertThrows(IllegalStateException.class, () -> diagram.addClassNode(node));
    }
    
    /**
     * Tests removing an existing class node in the diagram.
     */
    @Test
    void testRemoveClassNode_Success() {
        ClassNode node = new ClassNode();
        diagram.addClassNode(node);
        assertTrue(diagram.removeClassNode(node));
        assertFalse(diagram.getClassNodes().contains(node));
    }
    
    /**
     * Tests validation rules for setting the canvas width.
     */
    @Test
    void testCanvasWidthValidation() {
        assertThrows(IllegalArgumentException.class, () -> diagram.setCanvasWidth(-1));
        assertThrows(IllegalArgumentException.class, () -> diagram.setCanvasWidth(Double.NaN));
        diagram.setCanvasWidth(800);
        assertEquals(800, diagram.getCanvasWidth());
    }
    
    /**
     * Tests validation rules for setting the canvas height.
     */
    @Test
    void testCanvasHeightValidation() {
        assertThrows(IllegalArgumentException.class, () -> diagram.setCanvasHeight(-1));
        assertThrows(IllegalArgumentException.class, () -> diagram.setCanvasHeight(Double.NaN));
        diagram.setCanvasHeight(600);
        assertEquals(600, diagram.getCanvasHeight());
    }
    
    /**
     * Tests registering and unregistering observers.
     */
    @Test
    void testRegisterAndUnregisterObserver() {
        Observer<DiagramData> observer = d -> {};
        assertTrue(diagram.registerObserver(observer));
        assertFalse(diagram.registerObserver(observer));
        assertTrue(diagram.unregisterObserver(observer));
        assertFalse(diagram.unregisterObserver(observer));
    }
    
    /**
     * Tests null connection for removeConnection.
     */
    @Test
    void testAddConnection_Null() {
        assertFalse(diagram.addConnection(null));
    }
    
    /**
     * Tests null connection for removeConnection.
     */
    @Test
    void testRemoveConnection_Null() {
        assertFalse(diagram.removeConnection(null));
    }
    
    /**
     * Tests IllegalStateException for connection.
     */
    @Test
    void testAddConnection_IllegalStateException() {
        ClassNode source = new ClassNode();
        ClassNode target = new ClassNode();
        diagram.addClassNode(source);
        diagram.addClassNode(target);
        
        UMLConnection conn = new UMLConnection(source, target, Anchor.LEFT, Anchor.RIGHT);
        
        DiagramData diagramB = new DiagramData();
        conn.setParent(diagramB);
        
        assertThrows(IllegalStateException.class, () -> diagram.addConnection(conn));
    }
    
    /**
     * Tests connection exists for hasConnection.
     */
    @Test
    void testhasConnection_ConnectionExists() {
        ClassNode source = new ClassNode();
        ClassNode target = new ClassNode();
        diagram.addClassNode(source);
        diagram.addClassNode(target);
        
        UMLConnection conn = new UMLConnection(source, target, Anchor.LEFT, Anchor.RIGHT);
        
        diagram.addConnection(conn);
        
        assertTrue(diagram.hasConnection(conn));
    }
}
