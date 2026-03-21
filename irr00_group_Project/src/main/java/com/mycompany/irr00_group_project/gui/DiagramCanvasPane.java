package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.gui.data.Anchor;
import com.mycompany.irr00_group_project.listeners.Subject;
import java.util.List;

import com.mycompany.irr00_group_project.representation.DiagramData;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/**
 * Exposes necessary methods of the diagram canvas.
 * Used to hide inherited GUI methods of the canvas.
 * 
 * @author Deniz Büyükgüral
 * @author Aiham Al-Ashwal
 */
public interface DiagramCanvasPane {

    /**
     * Gets the underlying diagram model.
     * @return the underlying diagram model
     */
    DiagramData getModel();
    
    // Root/child node getters
    
    /**
     * Get the root JavaFX node of the canvas.
     * @return the root JavaFX node of the canvas
     */
    Region getRoot();
    
    /**
     * Get the JavaFX pane of the actual canvas pane to which nodes and arrows are put.
     * @return the JavaFX pane of the actual canvas pane to which nodes and arrows are put
     */
    Pane getRootPanel();

    /**
     * Get main scene the canvas is child of.
     * @return main scene the canvas is child of
     */
    MainScene getMainScene();
    
    // Canvas size methods
    
    /**
     * Get the width of the canvas.
     * @return the width of the canvas
     */
    double getCanvasWidth();

    /**
     * Set the width of the canvas.
     * @param width target width of the canvas. The actual size after call may differ if the target
     *              size would result in class node/arrow clipping.
     * @param resizeDirection direction of resize. If set to LEFT, all nodes and arrows
     *                        will be moved to the right.
     * @pre {width != Double.NaN && width >= 0}
     * @throws IllegalArgumentException if preconditions are violated
     */
    void setCanvasWidth(double width, HPos resizeDirection);

    /**
     * Set the width of the canvas.
     * @param width target width of the canvas. The actual size after call may differ if the target
     *              size would result in class node/arrow clipping.
     * @pre {width != Double.NaN && width >= 0}
     * @throws IllegalArgumentException if preconditions are violated
     */
    void setCanvasWidth(double width);

    /**
     * Get the height of the canvas.
     * @return the height of the canvas
     */
    double getCanvasHeight();

    /**
     * Set the height of the canvas.
     * @param height target height of the canvas. The actual size after call may differ
     *               if the target size would result in class node/arrow clipping.
     * @param resizeDirection direction of resize. If set to LEFT, all nodes and arrows
     *                        will be moved to the right.
     * @pre {height != Double.NaN && height >= 0}
     * @throws IllegalArgumentException if preconditions are violated
     */
    void setCanvasHeight(double height, VPos resizeDirection);

    /**
     * Set the height of the canvas.
     * @param height target height of the canvas. The actual size after call may differ
     *               if the target size would result in class node/arrow clipping.
     * @pre {height != Double.NaN && height >= 0}
     * @throws IllegalArgumentException if preconditions are violated
     */
    void setCanvasHeight(double height);

    /**
     * Create a class node at the given position.
     * @param localX x coordinate of local position relative to canvas
     * @param localY y coordinate of local position relative to canvas
     * @param centered if true, given position will be the center of the node.
     *        otherwise, given position will be the upper left corner of the node.
     * @return the controller for the newly created class node
     */
    ClassNodePaneUI createClassNode(double localX, double localY, boolean centered);
    
    /**
     * Add back the class node created by {@code createClassNode(localX, localY, centered)}.
     * @param node the class node to add back
     * @return true if the node is added back. false if node was already present on the canvas.
     * @pre {node != null}
     * @throws IllegalArgumentException if preconditions are violated
     */
    boolean addClassNode(ClassNodePaneUI node);

    /**
     * Removes the given class node from the canvas.
     * @param node controller of the class node
     * @return true if the node was removed. false if node is not present on the canvas.
     * @pre {node != null}
     * @throws IllegalArgumentException if preconditions are violated
     */
    boolean removeClassNode(ClassNodePaneUI node);

    /**
     * Get (read only) list of nodes on the canvas.
     * @return (read only) list of nodes on the canvas
     */
    List<ClassNodePaneUI> getNodes();

    /**
     * Create an arrow from source node to the target node.
     * @param sourceNode node the arrow originates from
     * @param sourceAnchor side of the node to which the tail of the arrow will be anchored
     * @param targetNode node the arrow points to
     * @param targetAnchor side of the node to which the tip of the arrow will be anchored
     * @return reference to the instantiated arrow
     * @pre {sourceNode != null && sourceAnchor != null
     *       && targetNode != null && targetAnchor != null}
     * @throws IllegalArgumentException if preconditions were violated
     */
    ArrowNodeUI createArrow(
            ClassNodePaneUI sourceNode,
            Anchor sourceAnchor,
            ClassNodePaneUI targetNode,
            Anchor targetAnchor);
    
    /**
     * Adds the given arrow to the canvas.
     * @param arrow reference to the arrow
     * @throws IllegalArgumentException if {@code arrow == null}
     * @throws RuntimeException if arrow was already added to the diagram
     */
    void addArrow(ArrowNodeUI arrow);

    /**
     * Removes the given arrow from the canvas.
     * @param arrow reference to the arrow
     * @throws IllegalArgumentException if {@code arrow == null}
     * @throws RuntimeException if arrow is not present in the diagram
     */
    void removeArrow(ArrowNodeUI arrow);
    
    /**
     * Get (read only) list of arrows on the canvas.
     * @return (read only) list of arrows on the canvas
     */
    List<ArrowNodeUI> getArrows();

    /**
     * Removes all class nodes and arrows from the canvas.
     */
    void clear();
    
    /**
     * Turn canvas into an image.
     * @param resolutionMultiplier multiplier for image resolution. Setting to 1.0 will
     *        result in resolution being same as canvas width/height.
     * @return image of the canvas.
     * @pre {resolutionMultiplier != Double.NaN && resolutionMultiplier > 0}
     * @throws IllegalArgumentException if preconditions are violated
     */
    Image toImage(double resolutionMultiplier);
    
    /**
     * Turn canvas into an image.
     * @return image of the canvas.
     */
    default Image toImage() {
        return toImage(2.0);
    }
    /**
     * Clears the canvas and redraws it entirely from the given data model.
     * This method is the key to synchronizing the View with the Model.
     * @param data The DiagramData model to display.
     */
    void displayFromModel(DiagramData data);
}
