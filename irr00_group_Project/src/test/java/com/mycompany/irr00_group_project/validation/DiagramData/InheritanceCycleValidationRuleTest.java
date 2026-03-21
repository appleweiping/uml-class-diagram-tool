package com.mycompany.irr00_group_project.validation.DiagramData;

import com.mycompany.irr00_group_project.gui.data.Anchor;
import com.mycompany.irr00_group_project.representation.ClassNode;
import com.mycompany.irr00_group_project.representation.ConnectionType;
import com.mycompany.irr00_group_project.representation.DiagramData;
import com.mycompany.irr00_group_project.representation.UMLConnection;
import com.mycompany.irr00_group_project.validation.ValidationResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for inheritance cycle validation rule.
 * 
 * @author Weiping Yan
 */
public class InheritanceCycleValidationRuleTest {
    
    // Shortcuts for edge adjacency matrices
    private static final ConnectionType X = null;
    private static final ConnectionType A = ConnectionType.AGGREGATION;
    private static final ConnectionType I = ConnectionType.INHERITANCE;
    private static final ConnectionType S = ConnectionType.ASSOCIATION;
    private static final ConnectionType C = ConnectionType.COMPOSITION;
    
    @Test
    public void testRobustness() {
        assertThrows(IllegalArgumentException.class,
                () -> new InheritanceCycleValidationRule().validate(null));
    }
    
    @Test
    public void testEmptyDiagram() {
        DiagramData emptyDiagram = new DiagramData();
        
        ValidationResult result = new InheritanceCycleValidationRule().validate(emptyDiagram);
        assertTrue(result.isValid());
    }
    
    @Test
    public void testGoodWeatherValid1() {
        DiagramDataFactory factory = new DiagramDataFactory();
        
        factory.addNodesFromAdjacencyMatric(
                new ConnectionType[][] {
                    new ConnectionType[] {X, A, X, X},
                    new ConnectionType[] {X, X, A, X},
                    new ConnectionType[] {X, X, X, A},
                    new ConnectionType[] {A, X, X, X},
                }
        );
        
        DiagramData diagram = factory.getDiagram();
        ValidationResult result = new InheritanceCycleValidationRule().validate(diagram);
        assertTrue(result.isValid());
    }
    
    @Test
    public void testGoodWeatherInvalid1() {
        DiagramDataFactory factory = new DiagramDataFactory();
        
        factory.addNodesFromAdjacencyMatric(
                new ConnectionType[][] {
                    new ConnectionType[] {X, I, X, X},
                    new ConnectionType[] {X, X, I, X},
                    new ConnectionType[] {X, X, X, I},
                    new ConnectionType[] {I, X, X, X},
                }
        );
        
        DiagramData diagram = factory.getDiagram();
        ValidationResult result = new InheritanceCycleValidationRule().validate(diagram);
        assertTrue(!result.isValid());
    }

    @Test
    public void testMultipleInheritanceNoCycle() {
        DiagramDataFactory factory = new DiagramDataFactory();
        
        factory.addNodesFromAdjacencyMatric(
            new ConnectionType[][] {
                new ConnectionType[] {X, I, I},
                new ConnectionType[] {X, X, X},
                new ConnectionType[] {X, X, X},
            }
        );

        DiagramData diagram = factory.getDiagram();
        ValidationResult result = new InheritanceCycleValidationRule().validate(diagram);
        assertTrue(result.isValid());
    }

    @Test
    public void testSingleClassNoInheritance() {
        DiagramDataFactory factory = new DiagramDataFactory();
        
        factory.addNodesFromAdjacencyMatric(new ConnectionType[][] {
            new ConnectionType[] { X }
        });
        
        DiagramData diagram = factory.getDiagram();
        ValidationResult result = new InheritanceCycleValidationRule().validate(diagram);
        assertTrue(result.isValid());
    }

    @Test
    public void testLongCycle() {
        DiagramDataFactory factory = new DiagramDataFactory();
        
        factory.addNodesFromAdjacencyMatric(new ConnectionType[][] {
            new ConnectionType[] { X, I, X, X },
            new ConnectionType[] { X, X, I, X },
            new ConnectionType[] { X, X, X, I },
            new ConnectionType[] { I, X, X, X }
        });
        
        DiagramData diagram = factory.getDiagram();
        ValidationResult result = new InheritanceCycleValidationRule().validate(diagram);
        assertFalse(result.isValid());
    }

    @Test
    public void testMultipleInheritanceWithCycle() {
        DiagramDataFactory factory = new DiagramDataFactory();
        
        factory.addNodesFromAdjacencyMatric(new ConnectionType[][] {
            new ConnectionType[] { X, I, I },
            new ConnectionType[] { X, X, X },
            new ConnectionType[] { I, X, X }
        });
        
        DiagramData diagram = factory.getDiagram();
        ValidationResult result = new InheritanceCycleValidationRule().validate(diagram);
        assertFalse(result.isValid());
    }

}