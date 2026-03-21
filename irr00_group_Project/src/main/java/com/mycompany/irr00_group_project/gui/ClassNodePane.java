package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.representation.ClassNode;
import com.mycompany.irr00_group_project.representation.ClassType;

/**
 * Exposes necessary methods of the class node pane. Used to hide inherited GUI methods of
 * class node pane.
 *
 * @author Deniz Büyükgüral
 */
public interface ClassNodePane {
    // Transform related methods
    
    /**
     * Get the underlying model of the class node pane
     * @return the underlying model of the class node pane
     */
    ClassNode getModel();
    
    /**
     * Get the x-position of the upper left corner of the class node,
     * relative to the canvas space.
     * 
     * @return x position of upper left corner of the class node,
     *         relative to the canvas space.
     */
    double getX();
    
    /**
     * Set the x-position of the upper left corner of the class node,
     * relative to the canvas space.
     * 
     * @param x x position of upper left corner of the class node,
     *          relative to the canvas space.
     * @pre {x != Double.NaN}
     * @throws IllegalArgumentException if preconditions are violated
     */
    void setX(double x);
    
    /**
     * Get the y-position of the upper left corner of the class node,
     * relative to the canvas space.
     * 
     * @return y position of upper left corner of the class node,
     *         relative to the canvas space.
     */
    double getY();
    
    /**
     * Set the y-position of the upper left corner of the class node,
     * relative to the canvas space.
     * 
     * @param y y position of upper left corner of the class node,
     *          relative to the canvas space.
     * @pre {y != Double.NaN}
     * @throws IllegalArgumentException if preconditions are violated
     */
    void setY(double y);
    
    /**
     * Moves the node to the given position, relative to the canvas space.
     * @param targetX x position of upper left corner of the class node, relative
     *                to the canvas space
     * @param targetY y position of upper left corner of the class node, relative
     *                to the canvas space
     * @pre {y != Double.NaN}
     * @throws IllegalArgumentException if preconditions are violated
     */
    void moveNode(double targetX, double targetY);
    
    /**
     * Get width of the class node.
     * @return width of the class node
     */
    double getNodeWidth();
    
    /**
     * Set width of the class node.
     * @param width width of the class node
     * @pre {width != Double.NaN}
     * @throws IllegalArgumentException if preconditions are violated
     * @post node resized. may not be resized to the given dimensions if below the minimum size.
     */
    void setNodeWidth(double width);
    
    /**
     * Get height of the class node.
     * @return height of the class node
     */
    double getNodeHeight();

    /**
     * Set height of the class node.
     * @param height height of the class node
     * @pre {height != Double.NaN}
     * @throws IllegalArgumentException if preconditions are violated
     * @post node resized. may not be resized to the given dimensions if below the minimum size.
     */
    void setNodeHeight(double height);
    
    // Data related methods

    /**
     * Get name of the class node.
     * @return name of the class node
     */
    String getClassNameText();
    
    /**
     * Set name of the class node.
     * @param text name of the class node
     * @pre {text != null}
     * @throws IllegalArgumentException if preconditions are violated
     */
    void setClassNameText(String text);

    /**
     * Get attributes of the class node.
     * @return attributes of the class node
     */
    String getAttributesText();
    
    /**
     * Set attributes of the class node.
     * @param text attributes of the class node
     * @pre {text != null}
     * @throws IllegalArgumentException if preconditions are violated
     */
    void setAttributesText(String text);

    /**
     * Get operations of the class node.
     * @return operations of the class node
     */
    String getOperationsText();

    /**
     * Set operations of the class node.
     * @param text operations of the class node
     * @pre {text != null}
     * @throws IllegalArgumentException if preconditions are violated
     */
    void setOperationsText(String text);
    
    /**
     * Get type of the class.
     * @return type of the class
     */
    ClassType getClassType();
    
    /**
     * Set type of the class.
     * @param newClassType type of the class
     * @pre {newClassType != null}
     * @throws IllegalArgumentException if preconditions are violated
     */
    void setClassType(ClassType newClassType);
}
