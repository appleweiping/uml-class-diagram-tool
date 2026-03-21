package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.gui.commands.CreateArrowCommand;
import com.mycompany.irr00_group_project.gui.data.Anchor;
import com.mycompany.irr00_group_project.representation.DiagramData;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

/**
 * Pane containing four handlers for creating arrow between class nodes. Created by
 * {@code ClassNodeController}.
 *
 * @author Deniz Büyükgüral
 */
class CreateArrowPane extends Pane {
    
    private final StackPane handleContainer;
    
    private final Pane topHandle;
    private final Pane bottomHandle;
    private final Pane leftHandle;
    private final Pane rightHandle;
    
    private final Line sourceLine;
    private final Line midLine;
    private final Line targetLine;
    private ClassNodePaneUI sourceNode;

    public CreateArrowPane(ClassNodePaneUI sourceNode) {
        super();
        
        if (sourceNode == null) {
            throw new IllegalArgumentException("sourceNode cannot be null");
        }
        
        this.sourceNode = sourceNode;
        
        // Create UI
        setPickOnBounds(false);
        prefWidthProperty().bind(sourceNode.getRootRegion().widthProperty());
        prefHeightProperty().bind(sourceNode.getRootRegion().heightProperty());
        setMinWidth(USE_PREF_SIZE);
        setMinHeight(USE_PREF_SIZE);
        setMaxWidth(USE_PREF_SIZE);
        setMaxHeight(USE_PREF_SIZE);
        
        sourceLine = createLine();
        midLine = createLine();
        targetLine = createLine();
        
        getChildren().addAll(sourceLine, midLine, targetLine);
        
        handleContainer = new StackPane();
        getChildren().add(handleContainer);
        handleContainer.setPickOnBounds(false);
        handleContainer.prefWidthProperty().bind(super.widthProperty());
        handleContainer.prefHeightProperty().bind(super.heightProperty());
        handleContainer.setMinWidth(USE_PREF_SIZE);
        handleContainer.setMinHeight(USE_PREF_SIZE);
        handleContainer.setMaxWidth(USE_PREF_SIZE);
        handleContainer.setMaxHeight(USE_PREF_SIZE);
        
        topHandle = createHandle(Pos.TOP_CENTER);
        bottomHandle = createHandle(Pos.BOTTOM_CENTER);
        leftHandle = createHandle(Pos.CENTER_LEFT);
        rightHandle = createHandle(Pos.CENTER_RIGHT);
        handleContainer.getChildren().addAll(topHandle, bottomHandle, leftHandle, rightHandle);
        
        rightHandle.setRotate(90.0);
        leftHandle.setRotate(-90.0);
        bottomHandle.setRotate(180.0);
    }
    
    private static Line createLine() {
        Line line = new Line();
        line.setMouseTransparent(true);
        line.setStrokeDashOffset(10.0);
        line.getStrokeDashArray().addAll(5.0, 5.0);
        line.setVisible(false);
        
        return line;
    }
    
    private Pane createHandle(Pos direction) {
        Pane handle = new Pane();
        StackPane.setAlignment(handle, direction);
        handle.setPrefWidth(30.0);
        handle.setPrefHeight(30.0);
        handle.setMinWidth(USE_PREF_SIZE);
        handle.setMinHeight(USE_PREF_SIZE);
        handle.setMaxWidth(USE_PREF_SIZE);
        handle.setMaxHeight(USE_PREF_SIZE);
        
        if (direction.getHpos() == HPos.LEFT) {
            handle.setTranslateX(-40.0);
        } else if (direction.getHpos() == HPos.RIGHT) {
            handle.setTranslateX(40.0);
        }
        
        if (direction.getVpos() == VPos.TOP) {
            handle.setTranslateY(-40.0);
        } else if (direction.getVpos() == VPos.BOTTOM) {
            handle.setTranslateY(40.0);
        }
        
        Anchor anchorDirection = switch (direction) {
            case Pos.TOP_CENTER -> Anchor.TOP;
            case Pos.BOTTOM_CENTER -> Anchor.BOTTOM;
            case Pos.CENTER_LEFT -> Anchor.LEFT;
            case Pos.CENTER_RIGHT -> Anchor.RIGHT;
            default -> null;
        };
        
        handle.setOnMousePressed((e) -> {
            onMousePress(e, anchorDirection);
        });
        
        handle.setOnMouseDragged((e) -> onHandleDrag(e));
        
        handle.setOnMouseReleased((e) -> onHandleRelease(e));
        
        // Create graphics
        Path path = new Path();
        handle.getChildren().add(path);
        path.setMouseTransparent(true);
        path.setFill(new Color(0.6627, 1.0, 0.7882, 1.0));
        path.setStroke(Color.BLACK);
        path.setStrokeType(StrokeType.INSIDE);
        path.getElements().addAll(
                new MoveTo(10.0, 30.0),
                new LineTo(10.0, 15.0),
                new LineTo(0.0, 15.0),
                new LineTo(15.0, 0.0),
                new LineTo(30.0, 15.0),
                new LineTo(20.0, 15.0),
                new LineTo(20.0, 30.0),
                new ClosePath()
        );
        
        return handle;
    }

    private Anchor sourceAnchor;
    
    private void onHandleDrag(MouseEvent e) {
        e.consume();
        lineToMouse(e);
    }

    private void onHandleRelease(MouseEvent e) {
        e.consume();
        setLineVisible(false);

        ClosestNodeResult nodeResult = findClosestNode(e);
        if (nodeResult == null) {
            return;
        }

        Region canvasRegion = sourceNode.getCanvas().getRootPanel();
        Region sourceRegion = sourceNode.getRootRegion();
        Region targetRegion = nodeResult.node.getRootRegion();

        Transform localToCanvas;
        try {
            localToCanvas = canvasRegion.getLocalToSceneTransform().createInverse()
                    .createConcatenation(getLocalToSceneTransform());
        } catch (NonInvertibleTransformException ex) {
            ex.printStackTrace();
            return;
        }

        Point2D sourcePos = localToCanvas.transform(sourceLine.getStartX(), sourceLine.getStartY());
        Point2D finalSourcePos = new Point2D(
                sourcePos.getX() - sourceRegion.getLayoutX(),
                sourcePos.getY() - sourceRegion.getLayoutY());

        Point2D targetPos = localToCanvas.transform(targetLine.getStartX(), targetLine.getStartY());
        Point2D finalTargetPos = new Point2D(
                targetPos.getX() - targetRegion.getLayoutX(),
                targetPos.getY() - targetRegion.getLayoutY());

        Point2D finalMidPos = localToCanvas.transform(midLine.getStartX(), midLine.getStartY());

        DiagramCanvasPane canvas = sourceNode.getCanvas();

        CreateArrowCommand command = new CreateArrowCommand(
                canvas,
                sourceNode,
                sourceAnchor,
                nodeResult.node,
                nodeResult.anchor,
                finalSourcePos,
                finalTargetPos,
                finalMidPos
        );

        canvas.getMainScene().executeCommand(command);
    }
    
    static record ClosestNodeResult(ClassNodePaneUI node, Anchor anchor) {}
    
    private ClosestNodeResult findClosestNode(MouseEvent e) {
        Point2D mouseCanvasPos = sourceNode.getCanvas().getRootPanel()
                .sceneToLocal(e.getSceneX(), e.getSceneY());
        
        double x = mouseCanvasPos.getX();
        double y = mouseCanvasPos.getY();
        
        for (ClassNodePaneUI node : sourceNode.getCanvas().getNodes()) {
            if (node == sourceNode) {
                continue;
            }
            
            Region nodeRegion = node.getRootRegion();
            final double LIMIT = 30.0;
            
            double layoutX = nodeRegion.getLayoutX();
            double layoutY = nodeRegion.getLayoutY();
            double width = nodeRegion.getWidth();
            double height = nodeRegion.getHeight();
            
            boolean inHorizontalBounds = x >= layoutX && x <= layoutX + width;
            boolean inVerticalBounds = y >= layoutY && y <= layoutY + height;
            
            if (inHorizontalBounds) {
                if (Math.abs(layoutY - y) < LIMIT) {
                    return new ClosestNodeResult(node, Anchor.TOP);
                }
                if (Math.abs(y - layoutY - height) < LIMIT) {
                    return new ClosestNodeResult(node, Anchor.BOTTOM);
                }
            }
            
            if (inVerticalBounds) {
                if (Math.abs(layoutX - x) < LIMIT) {
                    return new ClosestNodeResult(node, Anchor.LEFT);
                }
                if (Math.abs(x - layoutX - width) < LIMIT) {
                    return new ClosestNodeResult(node, Anchor.RIGHT);
                }
            }
        }
        
        return null;
    }
    
    private void lineToNode(MouseEvent e, ClassNodePaneUI node, Anchor targetAnchor) {
        boolean midLineEnabled = sourceAnchor.isHorizontal() == targetAnchor.isHorizontal();
        
        Point2D nodePanePos = super.sceneToLocal(node.getRootRegion().localToScene(Point2D.ZERO));
        double nodePanePosX = nodePanePos.getX();
        double nodePanePosY = nodePanePos.getY();
        
        Point2D mousePanePos = super.sceneToLocal(e.getSceneX(), e.getSceneY());
        double mouseX = mousePanePos.getX();
        double mouseY = mousePanePos.getY();
        
        switch (targetAnchor) {
            case TOP:
                targetLine.setStartX(mouseX);
                targetLine.setStartY(nodePanePosY);
                
                if (midLineEnabled && Math.abs(sourceLine.getStartX() - targetLine.getStartX()) < 7) {
                    targetLine.setStartX(sourceLine.getStartX());
                }
                break;
                
            case BOTTOM:
                targetLine.setStartX(mouseX);
                targetLine.setStartY(nodePanePosY + node.getRootRegion().getHeight());
                
                if (midLineEnabled && Math.abs(sourceLine.getStartX() - targetLine.getStartX()) < 7) {
                    targetLine.setStartX(sourceLine.getStartX());
                }
                break;
                
            case LEFT:
                targetLine.setStartX(nodePanePosX);
                targetLine.setStartY(mouseY);
                
                if (midLineEnabled && Math.abs(sourceLine.getStartY() - targetLine.getStartY()) < 7) {
                    targetLine.setStartY(sourceLine.getStartY());
                }
                break;
                
            case RIGHT:
                targetLine.setStartX(nodePanePosX + node.getRootRegion().getWidth());
                targetLine.setStartY(mouseY);
                
                if (midLineEnabled && Math.abs(sourceLine.getStartY() - targetLine.getStartY()) < 7) {
                    targetLine.setStartY(sourceLine.getStartY());
                }
                break;
        }
        
        if (midLineEnabled) {
            double offset;
            
            if (sourceAnchor.isHorizontal()) {
                offset = (sourceLine.getStartX() + targetLine.getStartX()) / 2.0;
                
                sourceLine.setEndX(offset);
                sourceLine.setEndY(sourceLine.getStartY());
                
                targetLine.setEndX(offset);
                targetLine.setEndY(targetLine.getStartY());
            } else {
                offset = (sourceLine.getStartY() + targetLine.getStartY()) / 2.0;
                
                sourceLine.setEndX(sourceLine.getStartX());
                sourceLine.setEndY(offset);
                
                targetLine.setEndX(targetLine.getStartX());
                targetLine.setEndY(offset);
            }
            
            midLine.setStartX(sourceLine.getEndX());
            midLine.setStartY(sourceLine.getEndY());
            midLine.setEndX(targetLine.getEndX());
            midLine.setEndY(targetLine.getEndY());
            
        } else {
            if (sourceAnchor.isHorizontal()) {
                sourceLine.setEndX(targetLine.getStartX());
                sourceLine.setEndY(sourceLine.getStartY());
                
                targetLine.setEndX(targetLine.getStartX());
                targetLine.setEndY(sourceLine.getStartY());
            } else {
                sourceLine.setEndX(sourceLine.getStartX());
                sourceLine.setEndY(targetLine.getStartY());
                
                targetLine.setEndX(sourceLine.getStartX());
                targetLine.setEndY(targetLine.getStartY());
            }
        }
    }
    
    private void lineToMouse(MouseEvent e) {
        midLine.setStartX(0);
        midLine.setStartY(0);
        midLine.setEndX(0);
        midLine.setEndY(0);
        
        ClosestNodeResult node = findClosestNode(e);
        if (node != null) {
            lineToNode(e, node.node, node.anchor);
            return;
        }
        
        Point2D mousePanePos = super.sceneToLocal(e.getSceneX(), e.getSceneY());
        
        switch (sourceAnchor) {
            case Anchor.TOP:
            case Anchor.BOTTOM:
                sourceLine.setEndX(sourceLine.getStartX());
                sourceLine.setEndY(mousePanePos.getY());
                break;
                
            case Anchor.LEFT:
            case Anchor.RIGHT:
                sourceLine.setEndX(mousePanePos.getX());
                sourceLine.setEndY(sourceLine.getStartY());
                break;
        }
        
        targetLine.setStartX(sourceLine.getEndX());
        targetLine.setStartY(sourceLine.getEndY());
        
        targetLine.setEndX(mousePanePos.getX());
        targetLine.setEndY(mousePanePos.getY());
    }
    
    private void onMousePress(MouseEvent e, Anchor anchor) {
        e.consume();
        sourceAnchor = anchor;
        
        double midX = super.getLayoutX() + super.getWidth() / 2.0;
        double midY = super.getLayoutY() + super.getHeight()/ 2.0;
        
        switch (anchor) {
            case Anchor.TOP:
                sourceLine.setStartX(midX);
                sourceLine.setStartY(super.getLayoutY());
                break;
                
            case Anchor.BOTTOM:
                sourceLine.setStartX(midX);
                sourceLine.setStartY(super.getLayoutY() + super.getHeight());
                break;
            
            case Anchor.LEFT:
                sourceLine.setStartY(midY);
                sourceLine.setStartX(super.getLayoutX());
                break;
                
            case Anchor.RIGHT:
                sourceLine.setStartY(midY);
                sourceLine.setStartX(super.getLayoutX() + super.getWidth());
                break;
        }
        
        setLineVisible(true);
        lineToMouse(e);
    }
    
    private void setLineVisible(boolean visible) {
        sourceLine.setVisible(visible);
        midLine.setVisible(visible);
        targetLine.setVisible(visible);
    }

    public void setEnabled(boolean enabled) {
        setVisible(enabled);
        setDisable(!enabled);
    }
}
