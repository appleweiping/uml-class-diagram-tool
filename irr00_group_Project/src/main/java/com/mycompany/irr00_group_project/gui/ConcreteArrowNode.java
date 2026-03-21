package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.Undo.Command;
import com.mycompany.irr00_group_project.gui.commands.DeleteArrowCommand;
import com.mycompany.irr00_group_project.gui.commands.MoveArrowCommand;
import com.mycompany.irr00_group_project.gui.data.Anchor;
import com.mycompany.irr00_group_project.representation.ConnectionType;
import com.mycompany.irr00_group_project.representation.UMLConnection;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Graphical instance of an arrow. Has a source and target class node. Has
 * a head at the target node representing the relation between source
 * and target node.
 *
 * @author Deniz Büyükgüral
 */
class ConcreteArrowNode extends Group implements ArrowNodeUI, Deletable {
    
    /**
     * Used to select and move segments of the arrow.
     */
    class FocusLine extends Line {
        private static final Color FOCUS_DISABLED_COLOR = new Color(0, 0, 0, 0);
        private static final Color FOCUS_HOVER_COLOR = new Color(0.0, 0.6, 1.0, 0.25);
        private static final Color FOCUS_ENABLED_COLOR = new Color(0.0, 0.6, 1.0, 0.61);
    
        private final ClassNodePaneUI node;
        private final Anchor anchor;
        private final DoubleProperty offset;
        private final Line referenceLine;
        
        private double initialOffset;
        
        public FocusLine(
                Line line,
                ClassNodePaneUI node,
                Anchor anchor,
                DoubleProperty offset,
                Line referenceLine) {
            
            super();
            
            this.node = node;
            this.anchor = anchor;
            this.offset = offset;
            this.referenceLine = referenceLine;
            
            // Style
            setStroke(new Color(0.0, 0.0, 0.0, 0.0));
            setStrokeWidth(6.0);
            setOnKeyPressed((e) -> onKeyPress(e));
            
            // Bind position to underlying line position
            startXProperty().bind(line.startXProperty());
            startYProperty().bind(line.startYProperty());
            endXProperty().bind(line.endXProperty());
            endYProperty().bind(line.endYProperty());
            
            bindFocusListener();
        }
        
        /**
         * Add mouse and focus listeners required to move the line with mouse drag.
         */
        private void bindFocusListener() {
            focusedProperty().addListener((ov, oldVal, newVal) -> {
                setStroke(newVal ? FOCUS_ENABLED_COLOR : FOCUS_DISABLED_COLOR);
            });
            
            setOnMouseEntered((e) -> {
                if (isFocused()) {
                    return;
                }
                
                setStroke(FOCUS_HOVER_COLOR);
            });
            
            setOnMouseExited((e) -> {
                if (isFocused()) {
                    return;
                }
                
                setStroke(FOCUS_DISABLED_COLOR);
            });

            setOnMousePressed((e) -> {
                if (!isFocused()) {
                    return;
                }

                e.consume();
                
                initialOffset = offset.get();
            });

            setOnMouseDragged((e) -> {
                if (!isFocused()) {
                    return;
                }
                
                e.consume();
                
                Point2D canvasPos = canvas.getRootPanel().sceneToLocal(e.getSceneX(), e.getSceneY());
                Point2D refLinePos = null;
                if (referenceLine != null) {
                    refLinePos = canvas.getRootPanel().sceneToLocal(referenceLine.localToScene(
                            referenceLine.getStartX(), referenceLine.getStartY())
                    );
                }
                
                double canvasX = canvasPos.getX();
                double canvasY = canvasPos.getY();
                
                switch (anchor) {
                    case TOP:
                    case BOTTOM:
                        if (node == null) {
                            offset.set(Math.clamp(canvasX, 0, canvas.getCanvasWidth()));
                            return;
                        }
                        
                        if (refLinePos != null) {
                            if (Math.abs(canvasX - refLinePos.getX()) < 15) {
                                canvasX = refLinePos.getX();
                            }
                        }
                        
                        double nodeX = node.getRootRegion().getLayoutX();
                        double nodeW = node.getRootRegion().getWidth();
                        offset.set(Math.clamp(canvasX - nodeX, 0, nodeW));
                        break;
                        
                    case LEFT:
                    case RIGHT:
                        if (node == null) {
                            offset.set(Math.clamp(canvasY, 0, canvas.getCanvasHeight()));
                            return;
                        }
                        
                        if (refLinePos != null) {
                            if (Math.abs(canvasY - refLinePos.getY()) < 5) {
                                canvasY = refLinePos.getY();
                            }
                        }
                        
                        double nodeY = node.getRootRegion().getLayoutY();
                        double nodeH = node.getRootRegion().getHeight();
                        offset.set(Math.clamp(canvasY - nodeY, 0, nodeH));
                        break;
                }
            });

            setOnMouseReleased((e) -> {
                if (!isFocused()) {
                    return;
                }

                e.consume();
                
                if (!e.isStillSincePress()) {
                    canvas.getMainScene().executeCommand(
                            new MoveArrowCommand(offset, initialOffset, offset.get()));
                }
            });

            setOnMouseClicked((e) -> {
                if (!e.isStillSincePress()) {
                    return;
                }

                requestFocus();
                e.consume();
            });
        }
    }

    private final UMLConnection model;
    
    // GUI Elements
    private final Line sourceLine;
    private final Line midLine;
    private final Line targetLine;
    private final FocusLine sourceFocus;
    private final FocusLine midFocus;
    private final FocusLine targetFocus;
    private final ArrowHeadNode arrowHead;
    private final ArrowTextsNode textFields;
    
    private final DiagramCanvasPane canvas;
    private final ClassNodePaneUI sourceNode;
    private final Anchor sourceAnchor;
    private final ClassNodePaneUI targetNode;
    private final Anchor targetAnchor;
    
    private final boolean midLineEnabled;
    
    // Offset of the source line relative to the source node edge
    private final DoubleProperty sourceOffset = new SimpleDoubleProperty(0);
    // Offset of the target line relative to the target node edge
    private final DoubleProperty targetOffset = new SimpleDoubleProperty(0);
    // Offset of the middle line relative to the canvas space
    private final DoubleProperty midOffset = new SimpleDoubleProperty(0);
    
    @Override
    public UMLConnection getModel() {
        return model;
    }
    
    @Override
    public Command createDeleteCommand() {
        return new DeleteArrowCommand(this);
    }
    
    public ConcreteArrowNode(
            DiagramCanvasPane canvas,
            UMLConnection model,
            ClassNodePaneUI sourceNode,
            Anchor sourceAnchor,
            ClassNodePaneUI targetNode,
            Anchor targetAnchor) {
        
        super();
        
        if (canvas == null) {
            throw new IllegalArgumentException("canvas cannot be null");
        }
        
        if (model == null) {
            throw new IllegalArgumentException("model cannot be null");
        }
        
        if (sourceNode == null) {
            throw new IllegalArgumentException("sourceNode cannot be null");
        }
        
        if (sourceAnchor == null) {
            throw new IllegalArgumentException("sourceAnchor cannot be null");
        }
        
        if (targetNode == null) {
            throw new IllegalArgumentException("targetNode cannot be null");
        }
        
        if (targetAnchor == null) {
            throw new IllegalArgumentException("targetAnchor cannot be null");
        }
        
        this.canvas = canvas;
        this.model = model;
        this.sourceNode = sourceNode;
        this.sourceAnchor = sourceAnchor;
        this.targetNode = targetNode;
        this.targetAnchor = targetAnchor;
        
        midLineEnabled = sourceAnchor.isHorizontal() == targetAnchor.isHorizontal();
        
        // Create UI
        sourceLine = createLine();
        midLine = createLine();
        targetLine = createLine();
        getChildren().addAll(sourceLine, midLine, targetLine);
        
        if (midLineEnabled) {
            sourceFocus = new FocusLine(sourceLine, sourceNode, sourceAnchor, sourceOffset, targetLine);
            targetFocus = new FocusLine(targetLine, targetNode, targetAnchor, targetOffset, sourceLine);
            midFocus = new FocusLine(midLine, null, sourceAnchor.isHorizontal() ? Anchor.TOP : Anchor.RIGHT, midOffset, null);
            
            getChildren().addAll(sourceFocus, midFocus, targetFocus);
        } else {
            sourceFocus = new FocusLine(sourceLine, sourceNode, sourceAnchor, sourceOffset, null);
            targetFocus = new FocusLine(targetLine, targetNode, targetAnchor, targetOffset, null);
            midFocus =  new FocusLine(midLine, null, sourceAnchor.isHorizontal() ? Anchor.TOP : Anchor.RIGHT, midOffset, null);
            midFocus.setDisable(true);
            midFocus.setVisible(false);
            
            getChildren().addAll(sourceFocus, targetFocus);
        }
        
        arrowHead = new ArrowHeadNode(targetAnchor, targetLine);
        getChildren().add(arrowHead);
        
        textFields = new ArrowTextsNode(this);
        getChildren().add(textFields);
        
        bindLinePositions();
        bindLinesToNodes();
        setLineCursors();
        
        // Bind model to view
        arrowHead.setHeadType(model.getType());
        arrowHead.headTypeProperty().addListener((ov, oldVal, newVal) -> model.setType(newVal));
        
        if (sourceAnchor.isHorizontal()) {
            setSourceY(model.getSourceOffsetY());
            sourceOffset.addListener((ov, oldVal, newVal)
                    -> model.setSourceOffsetY((double) newVal));
        } else {
            setSourceX(model.getSourceOffsetY());
            sourceOffset.addListener((ov, oldVal, newVal)
                    -> model.setSourceOffsetX((double) newVal));
        }
        
        if (targetAnchor.isHorizontal()) {
            targetOffset.addListener((ov, oldVal, newVal)
                    -> model.setTargetOffsetY((double) newVal));
        } else {
            targetOffset.addListener((ov, oldVal, newVal)
                    -> model.setTargetOffsetX((double) newVal));
        }
        
        if (midLineEnabled && sourceAnchor.isHorizontal()) {
            midOffset.addListener((ov, oldVal, newVal)
                    -> model.setMidlineX((double) newVal));
        } else if (midLineEnabled && sourceAnchor.isVertical()) {
            midOffset.addListener((ov, oldVal, newVal)
                    -> model.setMidlineY((double) newVal));
        }
    }
    
    private Line createLine() {
        Line line = new Line();
        line.setStrokeWidth(2.0);
        
        return line;
    }
    
    /**
     * Bind line ends to the appropriate properties.
     * Arrow can be relocated by moving line starts.
     */
    private void bindLinePositions() {
        if (midLineEnabled) {
            sourceLine.endXProperty().bind(midLine.startXProperty());
            sourceLine.endYProperty().bind(midLine.startYProperty());

            if (sourceAnchor.isHorizontal()) {
                midLine.startYProperty().bind(sourceLine.startYProperty());
                midLine.endYProperty().bind(targetLine.startYProperty());
                midLine.endXProperty().bind(midLine.startXProperty());
            } else {
                midLine.startXProperty().bind(sourceLine.startXProperty());
                midLine.endXProperty().bind(targetLine.startXProperty());
                midLine.endYProperty().bind(midLine.startYProperty());
            }
            
            targetLine.endXProperty().bind(midLine.endXProperty());
            targetLine.endYProperty().bind(midLine.endYProperty());
            
        } else {
            midLine.setDisable(true);
            midLine.setVisible(false);
            
            if (sourceAnchor.isHorizontal()) {
                sourceLine.endYProperty().bind(sourceLine.startYProperty());
                sourceLine.endXProperty().bind(targetLine.startXProperty());
                
                targetLine.endXProperty().bind(targetLine.startXProperty());
                targetLine.endYProperty().bind(sourceLine.startYProperty());
            } else {
                sourceLine.endXProperty().bind(sourceLine.startXProperty());
                sourceLine.endYProperty().bind(targetLine.startYProperty());
                
                targetLine.endYProperty().bind(targetLine.startYProperty());
                targetLine.endXProperty().bind(sourceLine.startXProperty());
            }
        }
    }
    
    private void bindLinesToNodes() {
        bindLineToNode(sourceLine, sourceNode, sourceAnchor, sourceOffset);
        bindLineToNode(targetLine, targetNode, targetAnchor, targetOffset);
        
        if (midLineEnabled) {
            bindMidLinePosition();
        }
    }
    
    /**
     * Bind the given line to the edge of the node. The edge is determined by anchor.
     * Offset provides the position relative to edge of the node.
     * @param line line which will be bound to the node
     * @param node node to which the line will be bound to
     * @param anchor side of the node to which the line will be anchored
     * @param offset property bound to the position of the line
     */
    private void bindLineToNode(
            Line line,
            ClassNodePaneUI node,
            Anchor anchor,
            DoubleProperty offset) {
        
        // Bind the offset independent axis of the line
        switch (anchor) {
            case Anchor.LEFT:
                line.startXProperty().bind(
                        node.getRootRegion().layoutXProperty());
                break;
               
            case Anchor.RIGHT:
                line.startXProperty().bind(
                        node.getRootRegion().layoutXProperty()
                                .add(node.getRootRegion().widthProperty()));
                break;
            
            case Anchor.TOP:
                line.startYProperty().bind(
                        node.getRootRegion().layoutYProperty());
                break;
                
            case Anchor.BOTTOM:
                line.startYProperty().bind(
                        node.getRootRegion().layoutYProperty()
                                .add(node.getRootRegion().heightProperty()));
                break;
        }
        
        // Bind the offset dependent axis of the line
        if (anchor.isHorizontal()) {
            line.startYProperty().bind(Bindings.createDoubleBinding(() -> {
                
                double offsetVal = offset.get();
                if (offsetVal < 0) {
                    offsetVal = 0;
                } else if (offsetVal > node.getRootRegion().getHeight()) {
                    offsetVal = node.getRootRegion().getHeight();
                }
                
                return node.getRootRegion().getLayoutY() + offsetVal;
            }, node.getRootRegion().layoutYProperty(), node.getRootRegion().heightProperty(), offset));
        } else {
            line.startXProperty().bind(Bindings.createDoubleBinding(() -> {
                
                double offsetVal = offset.get();
                if (offsetVal < 0) {
                    offsetVal = 0;
                } else if (offsetVal > node.getRootRegion().getWidth()) {
                    offsetVal = node.getRootRegion().getWidth();
                }
                
                return node.getRootRegion().getLayoutX() + offsetVal;
            }, node.getRootRegion().layoutXProperty(), node.getRootRegion().widthProperty(), offset));
        }
    }

    private void bindMidLinePosition() {
        if (sourceAnchor.isHorizontal()) {
            midLine.startXProperty().bind(midOffset);
        } else {
            midLine.startYProperty().bind(midOffset);
        }
    }
    
    private void setLineCursors() {
        setLineCursor(sourceFocus, sourceAnchor);
        setLineCursor(targetFocus, targetAnchor);
        
        if (midLineEnabled) {
            setLineCursor(midFocus, sourceAnchor.isHorizontal() ? Anchor.TOP : Anchor.RIGHT);
        }
    }
    
    private void setLineCursor(Line line, Anchor anchor) {
        if (anchor.isHorizontal()) {
            line.cursorProperty().bind(Bindings.createObjectBinding(() -> {
                return line.isFocused() ? Cursor.V_RESIZE : null;
            }, line.focusedProperty()));
        } else {
            line.cursorProperty().bind(Bindings.createObjectBinding(() -> {
                return line.isFocused() ? Cursor.H_RESIZE : null;
            }, line.focusedProperty()));
        }
    }

    @Override
    public DoubleProperty headWidthProperty() {
        return arrowHead.headWidthProperty();
    }

    @Override
    public DoubleProperty headHeightProperty() {
        return arrowHead.headHeightProperty();
    }
    
    @Override
    public ConnectionType getHeadType() {
        return arrowHead.getHeadType();
    }
    
    @Override
    public boolean setHeadType(ConnectionType connectionType) {
        if (connectionType == null) {
            throw new IllegalArgumentException("connectionType cannot be null");
        }
        
        if (getHeadType() == connectionType) {
            return false;
        }
        
        arrowHead.setHeadType(connectionType);
        return true;
    }
    
    @Override
    public ObjectProperty<ConnectionType> headTypeProperty() {
        return arrowHead.headTypeProperty();
    }
    
    @Override
    public DoubleProperty sourceOffsetProperty() {
        return sourceOffset;
    }
    
    @Override
    public DoubleProperty targetOffsetProperty() {
        return targetOffset;
    }
    
    @Override
    public DoubleProperty midOffsetProperty() {
        return midOffset;
    }
    
    @Override
    public Group getRoot() {
        return this;
    }
    
    @Override
    public Anchor getSourceAnchor() {
        return sourceAnchor;
    }
    
    @Override
    public Line getSourceLine() {
        return sourceLine;
    }
    
    @Override
    public double getSourceX() {
        if (sourceAnchor.isHorizontal()) {
            return Double.NaN;
        }
        
        return sourceOffset.get();
    }
    
    @Override
    public void setSourceX(double offset) {
        if (sourceAnchor.isHorizontal()) {
            return;
        }
        
        sourceOffset.set(offset);
    }
    
    @Override
    public double getSourceY() {
        if (sourceAnchor.isVertical()) {
            return Double.NaN;
        }
        
        return sourceOffset.get();
    }
    
    @Override
    public void setSourceY(double offset) {
        if (sourceAnchor.isVertical()) {
            return;
        }
        
        sourceOffset.set(offset);
    }
    
    @Override
    public Anchor getTargetAnchor() {
        return targetAnchor;
    }
    
    @Override
    public Line getTargetLine() {
        return targetLine;
    }
    
    @Override
    public double getTargetX() {
        if (targetAnchor.isHorizontal()) {
            return Double.NaN;
        }
        
        return targetOffset.get();
    }
    
    @Override
    public void setTargetX(double offset) {
        if (targetAnchor.isHorizontal()) {
            return;
        }
        
        targetOffset.set(offset);
    }
    
    @Override
    public double getTargetY() {
        if (targetAnchor.isVertical()) {
            return Double.NaN;
        }
        
        return targetOffset.get();
    }
    
    @Override
    public void setTargetY(double offset) {
        if (targetAnchor.isVertical()) {
            return;
        }
        
        targetOffset.set(offset);
    }
    
    @Override
    public double getMidlineX() {
        if (!midLineEnabled || sourceAnchor.isVertical()) {
            return Double.NaN;
        }
        
        return midOffset.get();
    }
    
    @Override
    public void setMidlineX(double x) {
        if (!midLineEnabled || sourceAnchor.isVertical()) {
            return;
        }
        
        x = Math.clamp(x, 0, canvas.getCanvasWidth());
        midOffset.set(x);
    }
    
    @Override
    public double getMidlineY() {
        if (!midLineEnabled || sourceAnchor.isHorizontal()) {
            return Double.NaN;
        }
        
        return midOffset.get();
    }
    
    @Override
    public void setMidlineY(double y) {
        if (!midLineEnabled || sourceAnchor.isHorizontal()) {
            return;
        }
        
        y = Math.clamp(y, 0, canvas.getCanvasHeight());
        midOffset.set(y);
    }
    
    @Override
    public ClassNodePaneUI getSourceNode() {
        return sourceNode;
    }
    
    @Override
    public ClassNodePaneUI getTargetNode() {
        return targetNode;
    }

    @Override
    public String getSourceMultiplicity() {
        return textFields.getSourceMultiplicity();
    }

    @Override
    public String getTargetMultiplicity() {
        return textFields.getTargetMultiplicity();
    }
    
    @Override
    public String getSourceRoleName() {
        return textFields.getSourceRoleName();
    }

    @Override
    public String getTargetRoleName() {
        return textFields.getTargetRoleName();
    }
    
    @Override
    public boolean setSourceMultiplicity(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }
        
        if (textFields.getSourceMultiplicity().equals(text)) {
            return false;
        }
        
        this.textFields.setSourceMultiplicity(text);
        return true;
    }

    @Override
    public boolean setTargetMultiplicity(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }
        
        if (textFields.getTargetMultiplicity().equals(text)) {
            return false;
        }
        
        this.textFields.setTargetMultiplicity(text);
        return true;
    }
    
    @Override
    public boolean setSourceRoleName(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }
        
        if (textFields.getSourceRoleName().equals(text)) {
            return false;
        }
        
        this.textFields.setSourceRoleName(text);
        return true;
    }

    @Override
    public boolean setTargetRoleName(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }
        
        if (textFields.getTargetRoleName().equals(text)) {
            return false;
        }
        
        this.textFields.setTargetRoleName(text);
        return true;
    }

    private void onKeyPress(KeyEvent e) {
        switch (e.getCode()) {
            case KeyCode.DELETE:
                getSourceNode().getCanvas().getMainScene()
                        .executeCommand(createDeleteCommand());
                break;
        }
    }
}
