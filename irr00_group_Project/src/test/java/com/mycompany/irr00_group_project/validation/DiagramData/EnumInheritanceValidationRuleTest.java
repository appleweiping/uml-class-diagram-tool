package com.mycompany.irr00_group_project.validation.DiagramData;

import com.mycompany.irr00_group_project.representation.ClassNode;
import com.mycompany.irr00_group_project.representation.ClassType;
import com.mycompany.irr00_group_project.representation.ConnectionType;
import com.mycompany.irr00_group_project.representation.DiagramData;
import com.mycompany.irr00_group_project.validation.ValidationResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EnumInheritanceValidationCheck.java.
 * 
 * @author Deniz Büyükgüral
 */
public class EnumInheritanceValidationRuleTest {
    
    // Shortcuts for edge adjacency matrices
    private static final ConnectionType X = null;
    private static final ConnectionType A = ConnectionType.AGGREGATION;
    private static final ConnectionType I = ConnectionType.INHERITANCE;
    private static final ConnectionType S = ConnectionType.ASSOCIATION;
    private static final ConnectionType C = ConnectionType.COMPOSITION;
    
    @Test
    public void testRobustness() {
        assertThrows(NullPointerException.class,
                () -> new EnumInheritanceValidationRule().validate(null));
    }
    
    @Test
    public void testGoodWeatherValid1() {
        DiagramDataFactory factory = new DiagramDataFactory();
        
        factory.addNodesFromAdjacencyMatric(new ConnectionType[][] {
            new ConnectionType[] {X, I, I, I},
            new ConnectionType[] {X, X, X, X},
            new ConnectionType[] {X, X, X, X},
            new ConnectionType[] {X, X, X, X},
        }, new ClassType[] {
            ClassType.NONE, ClassType.NONE, ClassType.ABSTRACT, ClassType.INTERFACE});
        
        DiagramData diagram = factory.getDiagram();
        EnumInheritanceValidationRule rule = new EnumInheritanceValidationRule();
        ValidationResult result = rule.validate(diagram);
        assertTrue(result.isValid());
    }
    
    @Test
    public void testGoodWeatherInvalid1() {
        DiagramDataFactory factory = new DiagramDataFactory();
        
        factory.addNodesFromAdjacencyMatric(new ConnectionType[][] {
            new ConnectionType[] {X, I},
            new ConnectionType[] {X, X},
        }, new ClassType[] {
            ClassType.NONE, ClassType.ENUM});
        
        DiagramData diagram = factory.getDiagram();
        EnumInheritanceValidationRule rule = new EnumInheritanceValidationRule();
        ValidationResult result = rule.validate(diagram);
        assertFalse(result.isValid());
        
        System.out.println(result.getMessage());
        
        ClassNode[] nodes = (ClassNode[]) result.getProblematicElement();
        assertEquals(nodes.length, 1);
        assertEquals(nodes[0], factory.getNode(0));
    }
    
    @Test
    public void testGoodWeatherInvalid2() {
        DiagramDataFactory factory = new DiagramDataFactory();
        
        factory.addNodesFromAdjacencyMatric(new ConnectionType[][] {
            new ConnectionType[] {X, I},
            new ConnectionType[] {X, X},
        }, new ClassType[] {
            ClassType.ENUM, ClassType.NONE});
        
        DiagramData diagram = factory.getDiagram();
        EnumInheritanceValidationRule rule = new EnumInheritanceValidationRule();
        ValidationResult result = rule.validate(diagram);
        assertFalse(result.isValid());
        
        System.out.println(result.getMessage());
        
        ClassNode[] nodes = (ClassNode[]) result.getProblematicElement();
        assertEquals(nodes.length, 1);
        assertEquals(nodes[0], factory.getNode(0));
    }
    
    @Test
    public void testGoodWeatherInvalid3() {
        DiagramDataFactory factory = new DiagramDataFactory();
        
        factory.addNodesFromAdjacencyMatric(new ConnectionType[][] {
            new ConnectionType[] {X, I},
            new ConnectionType[] {X, X},
        }, new ClassType[] {
            ClassType.ENUM, ClassType.ENUM});
        
        DiagramData diagram = factory.getDiagram();
        EnumInheritanceValidationRule rule = new EnumInheritanceValidationRule();
        ValidationResult result = rule.validate(diagram);
        assertFalse(result.isValid());
        
        System.out.println(result.getMessage());
        
        ClassNode[] nodes = (ClassNode[]) result.getProblematicElement();
        assertEquals(nodes.length, 1);
        assertEquals(nodes[0], factory.getNode(0));
    }
}
