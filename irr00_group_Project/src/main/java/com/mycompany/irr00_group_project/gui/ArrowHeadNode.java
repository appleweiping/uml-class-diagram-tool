package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.gui.data.Anchor;
import com.mycompany.irr00_group_project.representation.ConnectionType;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeType;

/**
 * Icon representing the relation between source and target node.
 *
 * @author Deniz Büyükgüral
 */
class ArrowHeadNode extends Pane {
    
    private final Path inheritanceIcon;
    private final Path aggregationIcon;
    private final Path compositionIcon;
    
    private final Anchor targetAnchor;
    private final Line targetLine;
    
    private final ObjectProperty<ConnectionType> headTypeProperty
            = new SimpleObjectProperty<>(ConnectionType.ASSOCIATION);
    
    private final ObjectProperty<Node> headNodeProperty
            = new SimpleObjectProperty<>(null);
    
    private final DoubleProperty headWidthProperty
            = new SimpleDoubleProperty();
    
    private final DoubleProperty headHeightProperty
            = new SimpleDoubleProperty();
    
    /**
     * Create an arrow head instance, anchored at the edge of the target node.
     * @param targetAnchor target edge of the arrow
     * @param targetLine line that points to the target node
     * @pre {targetAnchor != null && targetLine != null}
     * @throws IllegalArgumentException if preconditions are violated
     */
    public ArrowHeadNode(Anchor targetAnchor, Line targetLine) {
        super();
        
        if (targetAnchor == null) {
            throw new IllegalArgumentException("targetAnchor cannot be null");
        }
        
        if (targetLine == null) {
            throw new IllegalArgumentException("targetLine cannot be null");
        }
        
        this.targetAnchor = targetAnchor;
        this.targetLine = targetLine;
        
        // Initialize UI
        setMouseTransparent(true);
        setPrefWidth(40.0);
        setPrefHeight(40.0);
        setMinWidth(USE_PREF_SIZE);
        setMinHeight(USE_PREF_SIZE);
        setMaxWidth(USE_PREF_SIZE);
        setMaxHeight(USE_PREF_SIZE);
        setTranslateZ(100.0);
        
        inheritanceIcon = new Path();
        getChildren().add(inheritanceIcon);
        inheritanceIcon.setFill(Color.WHITE);
        inheritanceIcon.setStroke(Color.BLACK);
        inheritanceIcon.setStrokeType(StrokeType.INSIDE);
        inheritanceIcon.setStrokeWidth(2.0);
        inheritanceIcon.setScaleX(0.75);
        inheritanceIcon.setScaleY(0.75);
        inheritanceIcon.setTranslateY(-4.0);
        inheritanceIcon.getElements().addAll(
                new MoveTo(0.0, 20.0),
                new LineTo(20.0, 0.0),
                new LineTo(40.0, 20.0),
                new ClosePath()
        );
        
        aggregationIcon = new Path();
        getChildren().add(aggregationIcon);
        aggregationIcon.setFill(Color.WHITE);
        aggregationIcon.setStroke(Color.BLACK);
        aggregationIcon.setStrokeType(StrokeType.INSIDE);
        aggregationIcon.setStrokeWidth(2.0);
        aggregationIcon.setScaleX(0.75);
        aggregationIcon.setScaleY(0.75);
        aggregationIcon.setTranslateY(-6.0);
        aggregationIcon.getElements().addAll(
                new MoveTo(20.0, 40.0),
                new LineTo(5.0, 20.0),
                new LineTo(20.0, 0.0),
                new LineTo(35.0, 20.0),
                new ClosePath()
        );
        
        compositionIcon = new Path();
        getChildren().add(compositionIcon);
        compositionIcon.setFill(Color.BLACK);
        compositionIcon.setStroke(Color.BLACK);
        compositionIcon.setStrokeType(StrokeType.INSIDE);
        compositionIcon.setStrokeWidth(2.0);
        compositionIcon.setScaleX(0.75);
        compositionIcon.setScaleY(0.75);
        compositionIcon.setTranslateY(-6.0);
        compositionIcon.getElements().addAll(
                new MoveTo(20.0, 40.0),
                new LineTo(5.0, 20.0),
                new LineTo(20.0, 0.0),
                new LineTo(35.0, 20.0),
                new ClosePath()
        );
        
        inheritanceIcon.setVisible(false);
        aggregationIcon.setVisible(false);
        compositionIcon.setVisible(false);
        
        // Initialize properties
        headTypeProperty.addListener((ov, oldValue, newValue) -> {
            setIcon(newValue);
        });
        
        headWidthProperty.bind(Bindings.createDoubleBinding(() -> {
            Node head = headNodeProperty.get();
            if (head == null) {
                return 0.0;
            }
            
            return targetAnchor.isVertical()
                    ? head.getBoundsInParent().getWidth() : head.getBoundsInParent().getHeight();
        }, headNodeProperty));
        
        headHeightProperty.bind(Bindings.createDoubleBinding(() -> {
            Node head = headNodeProperty.get();
            if (head == null) {
                return 0.0;
            }
            
            return targetAnchor.isVertical()
                    ? head.getBoundsInParent().getHeight() : head.getBoundsInParent().getWidth();
        }, headNodeProperty));
        
        // Rotate based on which side of the node this head is anchored to
        switch (targetAnchor) {
            case Anchor.RIGHT:
                setRotate(-90);
                break;
                
            case Anchor.LEFT:
                setRotate(90);
                break;
                
            case Anchor.TOP:
                setRotate(180);
                break;
        }
        
        bindPositionToTargetLine();
        setHeadType(ConnectionType.ASSOCIATION);
    }
    
    /**
     * Bind the position of this icon to the tip of the target line
     */
    private void bindPositionToTargetLine() {
        // All nodes are anchored from their upper left corner. Need to take it into account
        // when setting the position.
        switch (targetAnchor) {
            case Anchor.TOP:
                layoutXProperty().bind(
                        targetLine.startXProperty().subtract(widthProperty().divide(2.0)));
                layoutYProperty().bind(
                        targetLine.startYProperty().subtract(heightProperty()));
                break;
                
            case Anchor.BOTTOM:
                layoutXProperty().bind(
                        targetLine.startXProperty().subtract(widthProperty().divide(2.0)));
                layoutYProperty().bind(
                        targetLine.startYProperty());
                break;
                
            case Anchor.LEFT:
                layoutXProperty().bind(
                        targetLine.startXProperty().subtract(widthProperty()));
                layoutYProperty().bind(
                        targetLine.startYProperty().subtract(heightProperty().divide(2.0)));
                break;
                
            case Anchor.RIGHT:
                layoutXProperty().bind(
                        targetLine.startXProperty());
                layoutYProperty().bind(
                        targetLine.startYProperty().subtract(heightProperty().divide(2.0)));
                break;
        }
    }
    
    /**
     * Set type of icon to show.
     * @param headType type of icon to show
     */
    private void setIcon(ConnectionType headType) {
        inheritanceIcon.setVisible(headType == ConnectionType.INHERITANCE);
        aggregationIcon.setVisible(headType == ConnectionType.AGGREGATION);
        compositionIcon.setVisible(headType == ConnectionType.COMPOSITION);
        
        switch (headType) {
            case ConnectionType.INHERITANCE -> headNodeProperty.set(inheritanceIcon);
            case ConnectionType.AGGREGATION -> headNodeProperty.set(aggregationIcon);
            case ConnectionType.COMPOSITION -> headNodeProperty.set(compositionIcon);
            case ConnectionType.ASSOCIATION -> headNodeProperty.set(null);
            default -> headNodeProperty.set(null);
        }   
    }
    
    
    
    /**
     * Property bound to the displayed node.
     * @return Property bound to the displayed node
     */
    ObjectProperty<Node> headNodeProperty() {
        return headNodeProperty;
    }
    
    /**
     * Property bound to the width of the displayed node.
     * @return Property bound to the width of the displayed node
     */
    DoubleProperty headWidthProperty() {
        return headWidthProperty;
    }
    
    /**
     * Property bound to the height of the displayed node
     * @return Property bound to the height of the displayed node
     */
    DoubleProperty headHeightProperty() {
        return headHeightProperty;
    }
    
    /**
     * Property bound to the type of the head.
     * @return Property bound to the type of the head
     */
    public ObjectProperty<ConnectionType> headTypeProperty() {
        return headTypeProperty;
    }
    
    /**
     * Get type of the arrow head.
     * @return type of the arrow head
     */
    public ConnectionType getHeadType() {
        return headTypeProperty.get();
    }
    
    /**
     * Set type of the arrow head.
     * @param headType type of the arrow head. If null, no changes are made.
     */
    public void setHeadType(ConnectionType headType) {
        if (headType == null) {
            return;
        }
        
        headTypeProperty.set(headType);
    }
}
