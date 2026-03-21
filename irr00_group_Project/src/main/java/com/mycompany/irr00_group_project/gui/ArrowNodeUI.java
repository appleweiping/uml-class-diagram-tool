package com.mycompany.irr00_group_project.gui;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Group;
import javafx.scene.shape.Line;

/**
 * Extension of ArrowNode interface, exposing necessary UI related methods.
 * 
 * @author Deniz Büyükgüral
 */
public interface ArrowNodeUI extends ArrowNode {
    
    // Root/child node getters
    
    /**
     * Get the root node of the instantiated JavaFX object.
     * @return the root node of the instantiated JavaFX object.
     */
    Group getRoot();
    
    @Override
    ClassNodePaneUI getSourceNode();
    
    @Override
    ClassNodePaneUI getTargetNode();
    
    /**
     * Get the JavaFX line object of the source line.
     * @return JavaFX line object of the source line.
     */
    Line getSourceLine();
    
    /**
     * Get the JavaFX line object of the target line.
     * @return JavaFX line object of the target line.
     */
    Line getTargetLine();
    
    // Properties
    
    /**
     * Property bound to the offset of the source line, relative to the anchored edge of the
     * source node.
     * @return Property bound to the offset of the source line, relative to the anchored edge of the
     *         source node. Can take values between 0 and length of the anchored edge.
     */
    DoubleProperty sourceOffsetProperty();
    
    /**
     * Property bound to the offset of the target line, relative to the anchored edge of the
     * target node.
     * @return Property bound to the offset of the target line, relative to the anchored edge of the
     *         target node. Can take values between 0 and length of the anchored edge.
     */
    DoubleProperty targetOffsetProperty();
    
    /**
     * Property bound to the offset of the middle line, relative to the canvas space.
     * @return Property bound to the offset of the middle line, relative to the canvas space.
     *         Can take values between 0 and length of the relative canvas edge.
     */
    DoubleProperty midOffsetProperty();
    
    /**
     * Property bound to the width of the active arrow head.
     * @return Property bound to the width of the active arrow head.
     */
    DoubleProperty headWidthProperty();
    
    /**
     * Property bound to the height of the active arrow head.
     * @return Property bound to the height of the active arrow head.
     */
    DoubleProperty headHeightProperty();
}
