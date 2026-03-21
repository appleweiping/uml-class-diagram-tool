package com.mycompany.irr00_group_project.validation.DiagramData;

import com.mycompany.irr00_group_project.gui.data.Anchor;
import com.mycompany.irr00_group_project.representation.ClassNode;
import com.mycompany.irr00_group_project.representation.ClassType;
import com.mycompany.irr00_group_project.representation.ConnectionType;
import com.mycompany.irr00_group_project.representation.DiagramData;
import com.mycompany.irr00_group_project.representation.UMLConnection;
import com.mycompany.irr00_group_project.validation.ValidationResult;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for MultiInheritanceValidationRule.java.
 * 
 * @author Deniz Büyükgüral
 */
public class MultiInheritanceValidationRuleTests {
    
    // Shortcuts for edge adjacency matrices
    private static final ConnectionType X = null;
    private static final ConnectionType A = ConnectionType.AGGREGATION;
    private static final ConnectionType I = ConnectionType.INHERITANCE;
    private static final ConnectionType S = ConnectionType.ASSOCIATION;
    private static final ConnectionType C = ConnectionType.COMPOSITION;
    
    @Test
    public void testRobustness() {
        assertThrows(NullPointerException.class,
                () -> new MultiInheritanceValidationRule().validate(null));
    }
    
    @Test
    public void testEmptyDiagram() {
        DiagramData emptyDiagram = new DiagramData();
        
        ValidationResult result = new MultiInheritanceValidationRule().validate(emptyDiagram);
        assertTrue(result.isValid());
    }
    
    @Test
    public void testGoodWeatherValid1() {
        DiagramDataFactory factory = new DiagramDataFactory();
        
        factory.addNodesFromAdjacencyMatric(
                new ConnectionType[][] {
                    new ConnectionType[] {X, I, X, X},
                    new ConnectionType[] {X, X, I, X},
                    new ConnectionType[] {X, X, X, I},
                    new ConnectionType[] {I, X, X, X},
                },
                new ClassType[] {ClassType.NONE, ClassType.NONE, ClassType.NONE, ClassType.NONE}
        );
        
        DiagramData diagram = factory.getDiagram();
        ValidationResult result = new MultiInheritanceValidationRule().validate(diagram);
        assertTrue(result.isValid());
    }
    
    @Test
    public void testGoodWeatherValid2() {
        DiagramDataFactory factory = new DiagramDataFactory();
        
        factory.addNodesFromAdjacencyMatric(
                new ConnectionType[][] {
                    new ConnectionType[] {X, I, I, I},
                    new ConnectionType[] {X, X, X, X},
                    new ConnectionType[] {X, X, X, X},
                    new ConnectionType[] {X, X, X, X},
                },
                new ClassType[] {ClassType.NONE, ClassType.INTERFACE,
                                    ClassType.INTERFACE, ClassType.NONE}
        );
        
        DiagramData diagram = factory.getDiagram();
        ValidationResult result = new MultiInheritanceValidationRule().validate(diagram);
        assertTrue(result.isValid());
    }
    
    @Test
    public void testGoodWeatherValid3() {
        DiagramDataFactory factory = new DiagramDataFactory();
        
        factory.addNodesFromAdjacencyMatric(
                new ConnectionType[][] {
                    new ConnectionType[] {X, S, X, S},
                    new ConnectionType[] {X, X, X, X},
                    new ConnectionType[] {X, X, X, X},
                    new ConnectionType[] {X, X, X, X},
                },
                new ClassType[] {ClassType.NONE, ClassType.NONE, ClassType.NONE, ClassType.NONE}
        );
        
        DiagramData diagram = factory.getDiagram();
        ValidationResult result = new MultiInheritanceValidationRule().validate(diagram);
        assertTrue(result.isValid());
    }
    
    @Test
    public void testGoodWeatherInvalid1() {
        DiagramDataFactory factory = new DiagramDataFactory();
        
        factory.addNodesFromAdjacencyMatric(
                new ConnectionType[][] {
                    new ConnectionType[] {X, I, X, I},
                    new ConnectionType[] {X, X, X, X},
                    new ConnectionType[] {X, X, X, X},
                    new ConnectionType[] {X, X, X, X},
                }
        );
        
        DiagramData diagram = factory.getDiagram();
        ValidationResult result = new MultiInheritanceValidationRule().validate(diagram);
        assertFalse(result.isValid());
        
        ClassNode[] nodes = (ClassNode[]) result.getProblematicElement();
        assertEquals(nodes.length, 1);
        assertEquals(nodes[0], factory.getNode(0));
    }
}
