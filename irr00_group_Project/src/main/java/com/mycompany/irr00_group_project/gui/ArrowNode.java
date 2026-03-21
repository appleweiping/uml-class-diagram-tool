package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.gui.data.Anchor;
import com.mycompany.irr00_group_project.representation.ConnectionType;
import com.mycompany.irr00_group_project.representation.UMLConnection;
import javafx.beans.property.ObjectProperty;

/**
 * Exposes necessary methods of the arrow node. Used to hide inherited GUI methods of arrow node.
 * 
 * @author Deniz Büyükgüral
 * @author Aiham Al-Ashwal
 */
public interface ArrowNode {
    
    // Arrow field getters and setters
    
    /**
     * Gets the underlying connection model.
     * @return the underlying connection model
     */
    UMLConnection getModel();
    
    /**
     * Get the node which the arrow originates from.
     * @return the node which the arrow originates from
     */
    ClassNodePane getSourceNode();
    
    /**
     * Get the node which the arrow points to.
     * @return the node which the arrow points to
     */
    ClassNodePane getTargetNode();

    /**
     * Get upper text at the source node side.
     * @return upper text at the source node side.
     */
    String getSourceMultiplicity();
    
    /**
     * Set upper text at the source node side.
     * @param text the new upper text at the source node side
     * @return true if text was changed, false otherwise
     * @pre {text != null}
     * @throws IllegalArgumentException if preconditions are violated
     */
    boolean setSourceMultiplicity(String text);

    /**
     * Get upper text at the target node side.
     * @return upper text at the target node side.
     */
    String getTargetMultiplicity();

    /**
     * Set upper text at the target node side.
     * @param text the new upper text at the target node side
     * @return true if text was changed, false otherwise
     * @pre {text != null}
     * @throws IllegalArgumentException if preconditions are violated
     */
    boolean setTargetMultiplicity(String text);
    
    /**
     * Get lower text at the source node side.
     * @return lower text at the source node side.
     */
    String getSourceRoleName();
    
    /**
     * Set lower text at the source node side.
     * @param text the new lower text at the source node side
     * @return true if text was changed, false otherwise
     * @pre {text != null}
     * @throws IllegalArgumentException if preconditions are violated
     */
    boolean setSourceRoleName(String text);
    
    /**
     * Get lower text at the target node side.
     * @return lower text at the target node side.
     */
    String getTargetRoleName();
    
    /**
     * Set lower text at the target node side.
     * @param text the new lower text at the target node side
     * @return true if text was changed, false otherwise
     * @pre {text != null}
     * @throws IllegalArgumentException if preconditions are violated
     */
    boolean setTargetRoleName(String text);
    
    /**
     * Get type of relation between source and target node.
     * @return type of relation between source and target node
     */
    ConnectionType getHeadType();
    
    /**
     * Set type of relation between source and target node.
     * @param connectionType new type of relation between source and target node
     * @return true if relation type was changed, false otherwise
     * @pre {connectionType != null}
     * @throws IllegalArgumentException if preconditions are violated
     */
    boolean setHeadType(ConnectionType connectionType);
    
    /**
     * Property bound to the type of relation between source and target nodes.
     * @return Property bound to the type of relation between source and target nodes
     */
    ObjectProperty<ConnectionType> headTypeProperty();
    
    // Transform related methods
    
    /**
     * Get the x position of the source arrow, relative to the source node edge.
     * @return x position of the source arrow, between [0, {@code sourceNode.getNodeWidth()}].
     *         0 is the left side of the node while {@code sourceNode.getNodeWidth()} is
     *         the right side of the node.
     */
    double getSourceX();
    
    /**
     * Set the x position of the source arrow, relative to the source node edge.
     * @param offset x position of the source arrow, between [0, {@code sourceNode.getNodeWidth()}].
     *               0 is the left side of the node while {@code sourceNode.getNodeWidth()} is
     *               the right side of the node.
     * @pre {offset != Double.NaN}
     * @throws IllegalArgumentException if preconditions are violated
     */
    void setSourceX(double offset);
    
    /**
     * Get the y position of the source arrow, relative to the source node edge.
     * @return y position of the source arrow, between [0, {@code sourceNode.getNodeHeight()}].
     *         0 is the upper side of the node while {@code sourceNode.getNodeHeight()} is
     *         the lower side of the node.
     */
    double getSourceY();
    
    /**
     * Set the y position of the source arrow, relative to the source node edge.
     * @param offset y position of the source arrow, between [0, {@code sourceNode.getNodeHeight()}].
     *               0 is the upper side of the node while {@code sourceNode.getNodeHeight()} is
     *               the lower side of the node.
     * @pre {offset != Double.NaN}
     * @throws IllegalArgumentException if preconditions are violated
     */
    void setSourceY(double offset);
    
    /**
     * Get the x position of the target arrow, relative to the target node edge.
     * @return x position of the target arrow, between [0, {@code targetNode.getNodeWidth()}].
     *         0 is the left side of the node while {@code targetNode.getNodeWidth()} is
     *         the right side of the node.
     */
    double getTargetX();
    
    /**
     * Set the x position of the target arrow, relative to the target node edge.
     * @param offset x position of the target arrow, between [0, {@code targetNode.getNodeWidth()}].
     *               0 is the left side of the node while {@code targetNode.getNodeWidth()} is
     *               the right side of the node.
     * @pre {offset != Double.NaN}
     * @throws IllegalArgumentException if preconditions are violated
     */
    void setTargetX(double offset);
    
    /**
     * Get the y position of the target arrow, relative to the target node edge.
     * @return y position of the target arrow, between [0, {@code targetNode.getNodeHeight()}].
     *         0 is the upper side of the node while {@code targetNode.getNodeHeight()} is
     *         the lower side of the node.
     */
    double getTargetY();
    
    /**
     * Set the y position of the target arrow, relative to the target node edge.
     * @param offset y position of the target arrow, between [0, {@code targetNode.getNodeHeight()}].
     *               0 is the upper side of the node while {@code targetNode.getNodeHeight()} is
     *               the lower side of the node.
     * @pre {offset != Double.NaN}
     * @throws IllegalArgumentException if preconditions are violated
     */
    void setTargetY(double offset);
    
    /**
     * Get the x position of the middle line, relative to the canvas space.
     * @return x position of the middle arrow, between [0, {@code canvas.getCanvasWidth()}].
     *         0 is the left side of the canvas while {@code canvas.getCanvasWidth()} is
     *         the right side of the canvas.
     */
    double getMidlineX();
    
    /**
     * Set the x position of the middle line, relative to the canvas space.
     * @param x x position of the middle line, between [0, {@code canvas.getCanvasWidth()}].
     *               0 is the left side of the canvas while {@code canvas.getCanvasWidth()} is
     *               the right side of the canvas.
     * @pre {x != Double.NaN}
     * @throws IllegalArgumentException if preconditions are violated
     */
    void setMidlineX(double x);
    
    /**
     * Get the y position of the middle line, relative to the canvas space.
     * @return y position of the middle arrow, between [0, {@code canvas.getCanvasHeight()}].
     *         0 is the upper side of the canvas while {@code canvas.getCanvasHeight()} is
     *         the lower side of the canvas.
     */
    double getMidlineY();
    
    /**
     * Set the x position of the middle line, relative to the canvas space.
     * @param y y position of the middle line, between [0, {@code canvas.getCanvasHeight()}].
     *               0 is the upper side of the canvas while {@code canvas.getCanvasHeight()} is
     *               the lower side of the canvas.
     * @pre {y != Double.NaN}
     * @throws IllegalArgumentException if preconditions are violated
     */
    void setMidlineY(double y);
    
    /**
     * Get to which side of the source node the source line is anchored to.
     * @return side of source node to which the source line is anchored to
     */
    Anchor getSourceAnchor();
    
    /**
     * Get to which side of the target node the target line is anchored to.
     * @return side of target node to which the target line is anchored to
     */
    Anchor getTargetAnchor();
}
