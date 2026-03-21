package com.mycompany.irr00_group_project.serialization;

import com.mycompany.irr00_group_project.gui.data.Anchor;
import com.mycompany.irr00_group_project.representation.ClassNode;
import com.mycompany.irr00_group_project.representation.ClassType;
import com.mycompany.irr00_group_project.representation.ConnectionType;
import com.mycompany.irr00_group_project.representation.DiagramData;
import com.mycompany.irr00_group_project.representation.UMLConnection;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for XMLDiagramSerializerTest.java.
 * 
 * @author Deniz Büyükgüral
 */
public class XMLDiagramSerializerTest {
    
    @BeforeEach
    public void setUp() {
    }
    
    private void assertDiagramEquality(XMLDiagramSerializer serializer, DiagramData diagram) {
        
        ByteArrayOutputStream diagramOut = new ByteArrayOutputStream();
        
        try {
            serializer.serializeDiagram(diagram, diagramOut);
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError("IOException thrown");
        }
        
        DiagramData readDiagram;
        ByteArrayInputStream diagramIn = new ByteArrayInputStream(diagramOut.toByteArray());
        
        try {
            readDiagram = serializer.deserializeDiagram(diagramIn);
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError("IOException thrown");
        }
        
        assertDiagramsAreEqual(diagram, readDiagram);
    }
    
    private void assertDiagramsAreEqual(DiagramData d1, DiagramData d2) {
        assertEquals(d1.getCanvasWidth(), d2.getCanvasWidth());
        assertEquals(d1.getCanvasHeight(), d2.getCanvasHeight());
        
        List<ClassNode> d1Nodes = d1.getClassNodes();
        List<ClassNode> d2Nodes = d1.getClassNodes();
        assertEquals(d1Nodes.size(), d2Nodes.size());
        for (int i = 0; i < d1Nodes.size(); i++) {
            assertClassesAreEqual(d1Nodes.get(i), d2Nodes.get(i));
        }
        
        List<UMLConnection> d1Conns = d1.getConnections();
        List<UMLConnection> d2Conns = d2.getConnections();
        assertEquals(d1Conns.size(), d2Conns.size());
        for (int i = 0; i < d1Conns.size(); i++) {
            assertConnection(d1Conns.get(i), d2Conns.get(i));
        }
    }
    
    private void assertClassesAreEqual(ClassNode c1, ClassNode c2) {
        
        assertEquals(c1.getLayoutX(), c2.getLayoutX());
        assertEquals(c1.getLayoutY(), c2.getLayoutY());
        assertEquals(c1.getWidth(), c2.getWidth());
        assertEquals(c1.getHeight(), c2.getHeight());
        
        assertEquals(c1.getClassName(), c2.getClassName());
        assertEquals(c1.getClassType(), c2.getClassType());
        assertEquals(c1.getAttributes(), c2.getAttributes());
        assertEquals(c1.getOperations(), c2.getOperations());
        
    }
    
    private void assertConnection(UMLConnection c1, UMLConnection c2) {
        
        assertClassesAreEqual(c1.getSourceClass(), c2.getSourceClass());
        assertClassesAreEqual(c1.getTargetClass(), c2.getTargetClass());
        assertEquals(c1.getSourceAnchor(), c2.getSourceAnchor());
        assertEquals(c1.getTargetAnchor(), c2.getTargetAnchor());
        
        assertEquals(c1.getSourceOffsetX(), c2.getSourceOffsetX());
        assertEquals(c1.getSourceOffsetY(), c2.getSourceOffsetY());
        assertEquals(c1.getTargetOffsetX(), c2.getTargetOffsetX());
        assertEquals(c1.getTargetOffsetY(), c2.getTargetOffsetY());
        assertEquals(c1.getMidlineX(), c2.getMidlineX());
        assertEquals(c1.getMidlineY(), c2.getMidlineY());
        
        assertEquals(c1.getType(), c2.getType());
        assertEquals(c1.getSourceMultiplicity(), c2.getSourceMultiplicity());
        assertEquals(c1.getTargetMultiplicity(), c2.getTargetMultiplicity());
        assertEquals(c1.getSourceRoleName(), c2.getSourceRoleName());
        assertEquals(c1.getTargetRoleName(), c2.getTargetRoleName());
    }
    
    /**
     * Check if the diagram data is preserved after serializing and deserializing.
     */
    @Test
    public void testDiagramDataKeptSame() throws Exception {
        
        DiagramData diagram = new DiagramData();
        diagram.setCanvasWidth(123);
        diagram.setCanvasHeight(456);
        
        ClassNode class1 = new ClassNode();
        ClassNode class2 = new ClassNode();
        ClassNode class3 = new ClassNode();
        
        class1.setClassName("NamedClass");
        class1.setClassType(ClassType.ABSTRACT);
        class1.setAttributes(List.of("attr1", "", "attr2"));
        class1.setOperations(List.of("op1", "", "op2"));
        
        class3.setLayoutX(4);
        class3.setLayoutY(4);
        class3.setWidth(132);
        class3.setHeight(94);
        
        diagram.addClassNode(class1);
        diagram.addClassNode(class2);
        diagram.addClassNode(class3);
        
        UMLConnection conn = new UMLConnection(class1, class2, Anchor.LEFT, Anchor.RIGHT);
        conn.setSourceOffsetX(1);
        conn.setSourceOffsetY(2);
        conn.setTargetOffsetX(3);
        conn.setTargetOffsetY(4);
        conn.setMidlineX(5);
        conn.setMidlineY(6);
        conn.setSourceMultiplicity("1");
        conn.setTargetMultiplicity("2");
        conn.setSourceRoleName("3");
        conn.setTargetRoleName("4");
        conn.setType(ConnectionType.INHERITANCE);
        diagram.addConnection(conn);
        
        assertDiagramEquality(new XMLDiagramSerializer(), diagram);
    }
    
    /**
     * Check whether preconditions are checked for robustness.
     */
    @Test
    public void testRobustness() {
        
        XMLDiagramSerializer serializer = new XMLDiagramSerializer();
        
        assertThrows(NullPointerException.class,
                () -> serializer.deserializeDiagram(null));
        
        assertThrows(NullPointerException.class,
                () -> serializer.serializeDiagram(new DiagramData(), null));
        
        assertThrows(NullPointerException.class,
                () -> serializer.serializeDiagram(null, new ByteArrayOutputStream()));
    }
}
