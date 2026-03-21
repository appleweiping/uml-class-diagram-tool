package com.mycompany.irr00_group_project.utils.iterators;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import javafx.scene.Node;
import javafx.scene.Parent;

/**
 * An iterator which can traverse all children and grandchildren of the given node.
 * This iterator is not fail safe. If the hierarchy of the root node is changed during
 * iteration, no exceptions will be thrown and the behavior is undefined.
 * 
 * @author Deniz Büyükgüral
 */
public class NodeChildIterator implements Iterator<Node> {

    private final List<Node> remainingNodes = new LinkedList<>();
    
    /**
     * Create an iterator which returns all children and grandchildren of the given node.
     * If returnRoot is set, the node itself is also returned.
     * @param node node to visits children of
     * @param returnRoot if set to true, given node is also returned. otherwise, iteration
     *        will start from the first child of the node.
     * @pre {node != null}
     * @throws NullPointerException if preconditions are violated
     */
    public NodeChildIterator(Node node, boolean returnRoot) {
        if (node == null) {
            throw new NullPointerException("node cannot be null");
        }
        
        if (returnRoot) {
            remainingNodes.add(node);
        } else if (node instanceof Parent nodeAsParent) {
            remainingNodes.addAll(nodeAsParent.getChildrenUnmodifiable().reversed());
        }
    }
    
    /**
     * Create an iterator which returns all children and grandchildren of the given node.
     * Node itself is not returned by the iterator.
     * @param node node to visits children of
     * @pre {node != null}
     * @throws NullPointerException if preconditions are violated
     */
    public NodeChildIterator(Node node) {
        this(node, false);
    }
    
    @Override
    public boolean hasNext() {
        return !remainingNodes.isEmpty();
    }

    @Override
    public Node next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        
        Node nextElement = remainingNodes.removeLast();
        if (nextElement instanceof Parent nextElementAsParent) {
            remainingNodes.addAll(nextElementAsParent.getChildrenUnmodifiable().reversed());
        }
        
        return nextElement;
    }
    
}
