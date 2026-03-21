package com.mycompany.irr00_group_project.representation;

import com.mycompany.irr00_group_project.listeners.Observer;
import com.mycompany.irr00_group_project.listeners.Subject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents the entire UML diagram data model, consisting of class nodes and UML connections.
 * 
 * <p>Each {@link ClassNode} represents a class in the diagram, and each {@link UMLConnection}
 * represents a UML relationship (such as association or inheritance) between classes.</p>
 * 
 * The subject is updated when there is a structural change in the diagram.
 * 
 * @author Aiham Al-Ashwal
 */
public class DiagramData implements Subject<DiagramData> {
    
    private final List<ClassNode> classNodes = new ArrayList<>();
    private final List<UMLConnection> connections = new ArrayList<>();
    private double canvasWidth = 600.0;
    private double canvasHeight = 400.0;
    
    /**
     * Gets the (read only) list of class nodes in the diagram.
     * 
     * @return (read only) list of class nodes
     */
    public List<ClassNode> getClassNodes() {
        return Collections.unmodifiableList(classNodes);
    }

    /**
     * Gets the (read only) list of UML connections in the diagram.
     * 
     * @return (read only) list of UML connections
     */
    public List<UMLConnection> getConnections() {
        return Collections.unmodifiableList(connections);
    }

    /**
     * Adds a class node to the diagram if it is not {@code null} and not already present.
     * 
     * @param node the class node to add
     * @return {@code true} if the node was added; {@code false} otherwise
     * @throws IllegalStateException if the given node is already added to another diagram
     */
    public boolean addClassNode(ClassNode node) {
        if (node == null || classNodes.contains(node)) {
            return false;
        }
        
        if (node.getParent() != null) {
            throw new IllegalStateException("given class already added to another diagram");
        }
        
        node.setParent(this);
        classNodes.add(node);
        
        notifyObservers();
        
        return true;
    }
    
    /**
     * Get whether the given class can be removed from the diagram.
     * @param node the node to check for
     * @return true if the node is in the diagram and has no connections attached to it.
     *         otherwise false.
     */
    public boolean canRemoveClassNode(ClassNode node) {
        if (node == null || !classNodes.contains(node)) {
            return false;
        }
        
        for (UMLConnection conn : connections) {
            if (conn.getSourceClass() == node || conn.getTargetClass() == node) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Removes a class node from the diagram if it exists.
     * 
     * @param node the class node to remove
     * @pre {canRemoveClassNode(node) == true}
     * @return {@code true} if the node was removed; {@code false} otherwise
     */
    public boolean removeClassNode(ClassNode node) {
        if (node == null || !classNodes.contains(node)) {
            return false;
        }
        
        if (node.getParent() != this) {
            throw new IllegalStateException(
                    "class is in the classes list, but parent is incorrect");
        }
        
        node.setParent(null);
        classNodes.remove(node);
        
        notifyObservers();
        
        return true;
    }
    
    /**
     * Get whether the given node is contained in the diagram.
     * @param node the node to be searched for
     * @return true if node is not null and contained in the diagram. false otherwise.
     */
    public boolean hasClassNode(ClassNode node) {
        if (node == null) {
            return false;
        }
        
        return node.getParent() == this;
    }
    
    /**
     * Add the given UML connection to the graph.
     * @param conn the connection to be added
     * @return true if conn was added successfully. false if conn was null or was already added.
     * @throws IllegalStateException if the given connection is already added to another diagram
     */
    public boolean addConnection(UMLConnection conn) {
        if (conn == null || connections.contains(conn)) {
            return false;
        }
        
        if (conn.getParent() != null) {
            throw new IllegalStateException("given connection is already in another diagram");
        }
        
        conn.setParent(this);
        connections.add(conn);
        
        notifyObservers();
        
        return true;
    }
    
    /**
     * Remove the given connection from the diagram.
     * @param conn the connection to remove
     * @return true if the connection was removed successfully. false if conn was null or was
     *         already removed.
     */
    public boolean removeConnection(UMLConnection conn) {
        if (conn == null || !connections.contains(conn)) {
            return false;
        }
        
        if (conn.getParent() != this) {
            throw new IllegalStateException(
                    "connection is in the connections list, but parent is incorrect");
        }
        
        conn.setParent(null);
        connections.remove(conn);
        
        notifyObservers();
        
        return true;
    }
    
    /**
     * Get whether the given connection is in the diagram.
     * @param conn the connection to search for
     * @return true if the diagram contains the connection. false if conn is null or not in the
     *         diagram.
     */
    public boolean hasConnection(UMLConnection conn) {
        if (conn == null) {
            return false;
        }
        
        return connections.contains(conn);
    }

    /**
     * Returns the width of the canvas.
     *
     * @return the canvas width
     */
    public double getCanvasWidth() {
        return canvasWidth;
    }

    /**
     * Sets the width of the canvas.
     *
     * @param canvasWidth the width to set
     * @throws IllegalArgumentException if canvasWidth is NaN or negative
     */
    public void setCanvasWidth(double canvasWidth) {
        if (Double.isNaN(canvasWidth) || canvasWidth < 0) {
            throw new IllegalArgumentException();
        }
        
        this.canvasWidth = canvasWidth;
    }

    /**
     * Returns the height of the canvas.
     *
     * @return the canvas height
     */
    public double getCanvasHeight() {
        return canvasHeight;
    }

    /**
     * Sets the height of the canvas.
     *
     * @param canvasHeight the height to set
     * @throws IllegalArgumentException if canvasWidth is NaN or negative
     */
    public void setCanvasHeight(double canvasHeight) {
        if (Double.isNaN(canvasHeight) || canvasHeight < 0) {
            throw new IllegalArgumentException();
        }
        
        this.canvasHeight = canvasHeight;
    }
    
    private final List<Observer<? super DiagramData>> observers = new LinkedList<>();

    @Override
    public boolean registerObserver(Observer<? super DiagramData> observer) {
        if (observer == null || observers.contains(observer)) {
            return false;
        }
        
        return observers.add(observer);
    }

    @Override
    public boolean unregisterObserver(Observer<? super DiagramData> observer) {
        if (observer == null) {
            return false;
        }
        
        return observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer<? super DiagramData> observer : observers) {
            observer.update(this);
        }
    }
}

