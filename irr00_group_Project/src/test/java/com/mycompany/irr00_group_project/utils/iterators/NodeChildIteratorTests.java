package com.mycompany.irr00_group_project.utils.iterators;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Tests for NodeChildIterator.java.
 * 
 * @author Deniz Büyükgüral
 */
public class NodeChildIteratorTests {
    
    /**
     * Check whether the iterator returns the expected values in order.
     * @param iter iterator to check, must not have any elements removed
     * @param expectedSequence expected sequence of the elements
     */
    private void assertIterator(NodeChildIterator iter, List<Node> expectedSequence) {
        
        System.out.println("Asserting node iterator");
        System.out.println("=======================");
        
        expectedSequence = new LinkedList<>(expectedSequence);
        
        while (!expectedSequence.isEmpty()) {
            assertTrue(iter.hasNext());
            
            Node expected = expectedSequence.removeFirst();
            Node result = iter.next();
            System.out.println("Expected: " + expected + ", result: " + result);
            
            assertEquals(expected, result);
        }
        
        System.out.println("=======================");
        assertFalse(iter.hasNext());
    }
    
    /**
     * Test whether iterator throws the required exceptions under given circumstances.
     */
    @Test
    public void testCtorRobustness() {
        assertThrows(NullPointerException.class, () -> new NodeChildIterator(null));
        assertThrows(NullPointerException.class, () -> new NodeChildIterator(null, true));
        assertThrows(NullPointerException.class, () -> new NodeChildIterator(null, false));
    }
    
    /**
     * Test whether iterator refuses to return non existing element.
     */
    @Test
    public void testNextRobustnessAndBoundryCase() {
        Node emptyNode = new Region();
        NodeChildIterator iter = new NodeChildIterator(emptyNode, false);
        
        assertFalse(iter.hasNext());
        assertThrows(NoSuchElementException.class, () -> iter.next());
    }
    
    /**
     * Test whether iterator returns all children in correct order where all nodes
     * can have children.
     */
    @Test
    public void testInOrder() {
        Pane root = new Pane();
        Pane node1 = new Pane();
        Pane node11 = new Pane();
        Pane node12 = new Pane();
        Pane node121 = new Pane();
        Pane node122 = new Pane();
        Pane node2 = new Pane();
        Pane node21 = new Pane();
        Pane node3 = new Pane();
        
        root.getChildren().addAll(node1, node2, node3);
        node1.getChildren().addAll(node11, node12);
        node12.getChildren().addAll(node121, node122);
        node2.getChildren().add(node21);
        
        List<Node> expectedSequenceRootIncluded = List.of(
                root,
                node1,
                node11,
                node12,
                node121,
                node122,
                node2,
                node21,
                node3
        );
        
        List<Node> expectedSequenceRootExcluded
                = new LinkedList<>(expectedSequenceRootIncluded);
        expectedSequenceRootExcluded.removeFirst();
        
        assertIterator(new NodeChildIterator(root, true), expectedSequenceRootIncluded);
        assertIterator(new NodeChildIterator(root, false), expectedSequenceRootExcluded);
    }

    /**
     * Test whether iterator returns all children in correct order where leaf nodes.
     * cannot have children.
     */
    @Test
    public void testInOrderWithNonParent() {
        Pane root = new Pane();
        Pane node1 = new Pane();
        Pane node11 = new Pane();
        Pane node12 = new Pane();
        Line node121 = new Line();
        Line node122 = new Line();
        Pane node2 = new Pane();
        Line node21 = new Line();
        Line node3 = new Line();
        
        root.getChildren().addAll(node1, node2, node3);
        node1.getChildren().addAll(node11, node12);
        node12.getChildren().addAll(node121, node122);
        node2.getChildren().add(node21);
        
        List<Node> expectedSequenceRootIncluded = List.of(
                root,
                node1,
                node11,
                node12,
                node121,
                node122,
                node2,
                node21,
                node3
        );
        
        List<Node> expectedSequenceRootExcluded
                = new LinkedList<>(expectedSequenceRootIncluded);
        expectedSequenceRootExcluded.removeFirst();
        
        assertIterator(new NodeChildIterator(root, true), expectedSequenceRootIncluded);
        assertIterator(new NodeChildIterator(root, false), expectedSequenceRootExcluded);
    }

}
