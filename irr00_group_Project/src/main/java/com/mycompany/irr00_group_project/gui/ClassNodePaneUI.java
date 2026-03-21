package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.representation.ClassType;
import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.Region;

/**
 * Extension of ClassNodePane interface, exposing necessary UI related methods.
 *
 * @author Deniz Büyükgüral
 */
public interface ClassNodePaneUI extends ClassNodePane {
    
    // References to nodes
    
    /**
     * Get the canvas that node is attached to.
     * @return canvas that node is attached to
     */
    DiagramCanvasPane getCanvas();
    
    /**
     * Get the root JavaFX node of the class node.
     * @return the root JavaFX node of the class node
     */
    Region getRootRegion();
    
    // Properties
    
    /**
     * Property bound to the type of the class.
     * @return Property bound to the type of the class
     */
    ObjectProperty<ClassType> classTypeProperty();
}
