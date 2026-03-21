package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.gui.commands.CreateClassNodeCommand;
import com.mycompany.irr00_group_project.gui.commands.ResizeCanvasCommand;
import com.mycompany.irr00_group_project.gui.data.Anchor;
import com.mycompany.irr00_group_project.gui.eventData.ResizeEvent;
import com.mycompany.irr00_group_project.representation.ClassNode;
import com.mycompany.irr00_group_project.representation.DiagramData;
import com.mycompany.irr00_group_project.representation.UMLConnection;
import com.mycompany.irr00_group_project.utils.iterators.NodeChildIterator;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

/**
 * Graphical instance of a diagram canvas. Has fixed width and height. Can be resized by
 * the user.
 *
 * @author Deniz Büyükgüral
 * @author Aiham Al-Ashwal
 */
class ConcreteDiagramCanvasPane extends StackPane implements DiagramCanvasPane {

    private static final double MIN_CANVAS_WIDTH = 50;
    private static final double MIN_CANVAS_HEIGHT = 50;

    private DiagramData model = new DiagramData();
    
    @Override
    public DiagramData getModel() {
        return model;
    }
    
    final Pane rootPane;
    private final Pane nodeRegion;
    private final Pane arrowRegion;

    private ResizePane resizePane;
    private final MainScene mainScene;

    private final List<ClassNodePaneUI> classNodes = new LinkedList<>();
    private final List<ArrowNodeUI> arrows = new LinkedList<>();

    public ConcreteDiagramCanvasPane(MainScene mainScene) {
        super();
        
        if (mainScene == null) {
            throw new IllegalArgumentException("mainScene cannot be null");
        }

        this.mainScene = mainScene;

        StackPane centerPane = new StackPane();
        getChildren().add(centerPane);

        // Setup root pane, canvas for the diagram
        rootPane = new Pane();
        centerPane.getChildren().add(rootPane);
        StackPane.setAlignment(rootPane, Pos.CENTER);
        rootPane.setPrefWidth(600.0);
        rootPane.setPrefHeight(400.0);
        rootPane.setMinWidth(USE_PREF_SIZE);
        rootPane.setMinHeight(USE_PREF_SIZE);
        rootPane.setMaxWidth(USE_PREF_SIZE);
        rootPane.setMaxHeight(USE_PREF_SIZE);
        rootPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        rootPane.setOnMouseClicked((e) -> onClick(e));

        arrowRegion = new Pane();
        rootPane.getChildren().add(arrowRegion);
        arrowRegion.setPickOnBounds(false);
        arrowRegion.prefWidthProperty().bind(rootPane.prefWidthProperty());
        arrowRegion.prefHeightProperty().bind(rootPane.prefHeightProperty());

        nodeRegion = new Pane();
        rootPane.getChildren().add(nodeRegion);
        nodeRegion.setPickOnBounds(false);
        nodeRegion.prefWidthProperty().bind(rootPane.prefWidthProperty());
        nodeRegion.prefHeightProperty().bind(rootPane.prefHeightProperty());

        createResizePane();
        createCuePane();
        
        // Bind model to view
        rootPane.prefWidthProperty().addListener(
                (ov, oldVal, newVal) -> model.setCanvasWidth((double) newVal));
        
        rootPane.prefHeightProperty().addListener(
                (ov, oldVal, newVal) -> model.setCanvasHeight((double) newVal));
    }


    private void createResizePane() {
        resizePane = new ResizePane(rootPane, rootPane);
        rootPane.getChildren().add(resizePane);

        resizePane.setMinResizeWidth(MIN_CANVAS_WIDTH);
        resizePane.setMinResizeHeight(MIN_CANVAS_HEIGHT);
        resizePane.addResizeListener((ResizeEvent e) -> {
            mainScene.executeCommand(new ResizeCanvasCommand(
                    this,
                    e.width(),
                    e.height(),
                    e.resizeDirection())
            );
        });

        resizePane.setEnabled(true);
    }
    
    private void createCuePane() {
        DoubleClickCuePane cuePane = new DoubleClickCuePane(this, rootPane);
        rootPane.getChildren().add(cuePane);
    }

    @Override
    public Region getRoot() {
        return this;
    }
    
    @Override
    public Pane getRootPanel() {
        return rootPane;
    }

    @Override
    public MainScene getMainScene() {
        return mainScene;
    }

    private void moveDiagramComponents(double deltaX, double deltaY) {
        for (ClassNodePane node : getNodes()) {
            node.moveNode(node.getX() + deltaX, node.getY() + deltaY);

            for (ArrowNode arrow : arrows) {
                double midlineX = arrow.getMidlineX();
                double midlineY = arrow.getMidlineY();

                if (!Double.isNaN(midlineX)) {
                    arrow.setMidlineX(midlineX + deltaX);
                }

                if (!Double.isNaN(midlineY)) {
                    arrow.setMidlineY(midlineY + deltaY);
                }
            }
        }
    }

    private double getMaxHorizontalShrink(HPos shrinkDirection) {
        double maxShrink = getCanvasWidth();
        if (shrinkDirection == HPos.CENTER) {
            return maxShrink;
        }

        for (ClassNodePane node : getNodes()) {
            if (shrinkDirection == HPos.LEFT) {
                maxShrink = Math.min(maxShrink, node.getX());
            } else {
                maxShrink = Math.min(maxShrink, getCanvasWidth() - node.getX() - node.getNodeWidth());
            }

            for (ArrowNode arrow : arrows) {
                double midlineX = arrow.getMidlineX();
                if (Double.isNaN(midlineX)) {
                    continue;
                }

                if (shrinkDirection == HPos.LEFT) {
                    maxShrink = Math.min(maxShrink, midlineX);
                } else {
                    maxShrink = Math.min(maxShrink, getCanvasWidth() - midlineX);
                }
            }
        }

        maxShrink = Math.clamp(maxShrink, 0, getCanvasWidth());
        return maxShrink;
    }

    private double getMaxVerticalShrink(VPos shrinkDirection) {
        double maxShrink = getCanvasHeight();
        if (shrinkDirection == VPos.CENTER) {
            return maxShrink;
        }

        for (ClassNodePane node : getNodes()) {
            if (shrinkDirection == VPos.TOP) {
                maxShrink = Math.min(maxShrink, node.getY());
            } else {
                maxShrink = Math.min(maxShrink, getCanvasHeight() - node.getY() - node.getNodeHeight());
            }

            for (ArrowNode arrow : arrows) {
                double midlineY = arrow.getMidlineY();
                if (Double.isNaN(midlineY)) {
                    continue;
                }

                if (shrinkDirection == VPos.TOP) {
                    maxShrink = Math.min(maxShrink, midlineY);
                } else {
                    maxShrink = Math.min(maxShrink, getCanvasHeight() - midlineY);
                }
            }
        }

        maxShrink = Math.clamp(maxShrink, 0, getCanvasHeight());
        return maxShrink;
    }

    @Override
    public double getCanvasWidth() {
        return rootPane.getPrefWidth();
    }

    @Override
    public void setCanvasWidth(double width, HPos resizeDirection) {
        if (Double.isNaN(width) || width < 0) {
            throw new IllegalArgumentException("width must be non negative");
        }

        // No resize
        if (resizeDirection == HPos.CENTER) {
            return;
        }

        double deltaX = width - getCanvasWidth();
        boolean extending = deltaX > 0;

        if (resizeDirection == HPos.LEFT) {
            if (extending) {
                // Extending the canvas towards left. Need to move all nodes and arrows to the right.
                rootPane.setPrefWidth(width);
                moveDiagramComponents(deltaX, 0);
            } else {
                // Shrink the canvas from left. May need to limit the shrink value if shrinking
                // would intersect a node/arrow.
                double maxShrink = getMaxHorizontalShrink(resizeDirection);
                deltaX = Math.max(deltaX, -maxShrink);

                moveDiagramComponents(deltaX, 0);
                rootPane.setPrefWidth(getCanvasWidth() + deltaX);
           }
        } else {
            if (extending) {
                // Extending the canvas towards right. No need to move components.
                rootPane.setPrefWidth(width);
            } else {
                // Shrink the canvas from left. May need to limit the shrink value if shrinking
                // would intersect a node/arrow.
                double maxShrink = getMaxHorizontalShrink(resizeDirection);
                deltaX = Math.max(deltaX, -maxShrink);

                rootPane.setPrefWidth(getCanvasWidth() + deltaX);
            }
        }

        // Small hack. If a node is scaled, anchor goes to the middle.
        relocate(1, 1);
        relocate(0, 0);
    }

    @Override
    public void setCanvasWidth(double width) {
        setCanvasWidth(width, HPos.RIGHT);
    }

    @Override
    public double getCanvasHeight() {
        return rootPane.getPrefHeight();
    }

    @Override
    public void setCanvasHeight(double height, VPos resizeDirection) {
        if (Double.isNaN(height) || height < 0) {
            throw new IllegalArgumentException("height must be non negative");
        }

        // No resize
        if (resizeDirection == VPos.CENTER) {
            return;
        }

        double deltaY = height - getCanvasHeight();
        boolean extending = deltaY > 0;

        if (resizeDirection == VPos.TOP) {
            if (extending) {
                rootPane.setPrefHeight(height);
                moveDiagramComponents(0, deltaY);
            } else {
                double maxShrink = getMaxVerticalShrink(resizeDirection);
                deltaY = Math.max(deltaY, -maxShrink);

                moveDiagramComponents(0, deltaY);
                rootPane.setPrefHeight(getCanvasHeight() + deltaY);
            }
        } else {
            if (extending) {
                rootPane.setPrefHeight(height);
            } else {
                double maxShrink = getMaxVerticalShrink(resizeDirection);
                deltaY = Math.max(deltaY, -maxShrink);

                rootPane.setPrefHeight(getCanvasHeight() + deltaY);
            }
        }

        // Small hack. If a node is scaled, anchor goes to the middle.
        relocate(1, 1);
        relocate(0, 0);
    }

    @Override
    public void setCanvasHeight(double height) {
        setCanvasHeight(height, VPos.BOTTOM);
    }

    /**
     * Create a class node at the given position.
     * @param localX x coordinate of local position relative to canvas
     * @param localY y coordinate of local position relative to canvas
     * @param centered if true, given position will be the center of the node.
     *        otherwise, given position will be the upper left corner of the node.
     * @return the controller for the newly created class node
     */
    @Override
    public ClassNodePaneUI createClassNode(double localX, double localY, boolean centered) {
        ClassNode nodeModel = new ClassNode();
        nodeModel.setWidth(150);
        nodeModel.setHeight(105);
        
        if (centered) {
            localX = Math.round(localX - nodeModel.getWidth()/ 2);
            localX = Math.max(0, localX);
            
            localY = Math.round(localY - nodeModel.getHeight()/ 2);
            localY = Math.max(0, localY);
        }
        
        nodeModel.setLayoutX(localX);
        nodeModel.setLayoutY(localY);
        
        ClassNodePaneUI classNodeController = new ConcreteClassNodePane(this, nodeModel);
        nodeRegion.getChildren().add(classNodeController.getRootRegion());

        classNodes.add(classNodeController);
        model.addClassNode(nodeModel);
        return classNodeController;
    }

    @Override
    public boolean addClassNode(ClassNodePaneUI node) {
        if (node == null) {
            throw new IllegalArgumentException("node cannot be null");
        }

        if (classNodes.contains(node)) {
            return false;
        }
        
        if (!model.addClassNode(node.getModel())) {
            return false;
        }

        nodeRegion.getChildren().add(node.getRootRegion());
        classNodes.add(node);
        return true;
    }

    @Override
    public boolean removeClassNode(ClassNodePaneUI node) {
        if (node == null || !classNodes.contains(node)) {
            return false;
        }
        
        if (!model.removeClassNode(node.getModel())) {
            return false;
        }
        
        nodeRegion.getChildren().remove(node.getRootRegion());
        classNodes.remove(node);
        return true;
    }

    @Override
    public List<ClassNodePaneUI> getNodes() {
        return Collections.unmodifiableList(classNodes);
    }

    @Override
    public ArrowNodeUI createArrow(
            ClassNodePaneUI sourceNode,
            Anchor sourceAnchor,
            ClassNodePaneUI targetNode,
            Anchor targetAnchor) {

        UMLConnection arrowModel = new UMLConnection(
                sourceNode.getModel(),
                targetNode.getModel(),
                sourceAnchor,
                targetAnchor);
        
        ArrowNodeUI arrow = new ConcreteArrowNode(
                this,
                arrowModel,
                sourceNode,
                sourceAnchor,
                targetNode,
                targetAnchor);
        
        arrowRegion.getChildren().add(arrow.getRoot());
        arrows.add(arrow);
        model.addConnection(arrowModel);
        
        return arrow;
    }

    @Override
    public void addArrow(ArrowNodeUI arrow) {
        if (arrow == null) {
            throw new IllegalArgumentException("arrow cannot be null");
        }

        if (arrows.contains(arrow)) {
            throw new IllegalStateException("arrow already added");
        }
        
        arrowRegion.getChildren().add(arrow.getRoot());
        arrows.add(arrow);
        model.removeConnection(arrow.getModel());
    }

    @Override
    public void removeArrow(ArrowNodeUI arrow) {
        if (arrow == null) {
            throw new IllegalArgumentException("arrow cannot be null");
        }
        
        if (!arrows.remove(arrow)) {
            throw new IllegalStateException("could not remove arrow");
        }
        
        arrowRegion.getChildren().remove(arrow.getRoot());
        model.removeConnection(arrow.getModel());
    }
    
    @Override
    public List<ArrowNodeUI> getArrows() {
        return Collections.unmodifiableList(arrows);
    }

    private void onClick(MouseEvent e) {
        e.consume();

        if (!e.isStillSincePress()) {
            return;
        }

        if (e.getButton() != MouseButton.PRIMARY) {
            return;
        }

        if (e.getClickCount() != 2) {
            return;
        }

        mainScene.executeCommand(new CreateClassNodeCommand(this, e.getX(), e.getY()));
    }

    /**
     * Removes all class nodes and arrows from the canvas.
     */
    @Override
    public void clear() {
        while (!arrows.isEmpty()) {
            ArrowNodeUI arrow = arrows.getLast();
            removeArrow(arrow);
        }
        
        while (!classNodes.isEmpty()) {
            ClassNodePaneUI node = classNodes.getLast();
            removeClassNode(node);
        }
    }
    
    @Override
    public Image toImage(double resolutionMultiplier) {
        
        if (Double.isNaN(resolutionMultiplier)) {
            throw new IllegalArgumentException("resolutionMultiplier cannot be NaN");
        }
        
        if (resolutionMultiplier <= 0) {
            throw new IllegalArgumentException("resolutionMultiplier must be positive");
        }
        
        // Pre-processing
        mainScene.requestFocus();
        resizePane.setEnabled(false);
        
        List<ImageProcessed> processedNodes = new LinkedList<>();
        NodeChildIterator iter = new NodeChildIterator(rootPane, false);
        while (iter.hasNext()) {
            Node child = iter.next();
            
            if (!(child instanceof ImageProcessed imageProcessed)) {
                continue;
            }
            
            processedNodes.add(imageProcessed);
            imageProcessed.onPreImageExport();
        }
        
        SnapshotParameters params = new SnapshotParameters();
        params.setTransform(Transform.scale(resolutionMultiplier, resolutionMultiplier));
        
        WritableImage image = rootPane.snapshot(params, null);
        
        // Post-processing
        resizePane.setEnabled(true);
        for (ImageProcessed processedNode : processedNodes) {
            processedNode.onPostImageExport();
        }
        
        return image;
    }
    
    @Override
    public void displayFromModel(DiagramData data) {
        clear();
        
        setCanvasWidth(data.getCanvasWidth());
        setCanvasHeight(data.getCanvasHeight());

        Map<ClassNode, ClassNodePaneUI> modelToViewMap = new HashMap<>();

        for (ClassNode nodeModel : data.getClassNodes()) {
            ClassNodePaneUI nodeView
                    = createClassNode(nodeModel.getLayoutX(), nodeModel.getLayoutY(), false);
            modelToViewMap.put(nodeModel, nodeView);
            
            nodeView.setX(nodeModel.getLayoutX());
            nodeView.setY(nodeModel.getLayoutY());
            nodeView.setNodeWidth(nodeModel.getWidth());
            nodeView.setNodeHeight(nodeModel.getHeight());
            nodeView.setClassType(nodeModel.getClassType());
            nodeView.setClassNameText(nodeModel.getClassName());
            nodeView.setAttributesText(nodeModel.getAttributesFlat());
            nodeView.setOperationsText(nodeModel.getOperationsFlat());
        }

        for (UMLConnection connectionModel : data.getConnections()) {
            ClassNodePaneUI sourcePane = modelToViewMap.get(connectionModel.getSourceClass());
            ClassNodePaneUI targetPane = modelToViewMap.get(connectionModel.getTargetClass());

            if (sourcePane != null && targetPane != null) {
                ArrowNodeUI arrowView = createArrow(
                        sourcePane,
                        connectionModel.getSourceAnchor(),
                        targetPane,
                        connectionModel.getTargetAnchor()
                );

                // Set the arrow's properties from the model
                arrowView.setHeadType(connectionModel.getType());
                arrowView.setSourceMultiplicity(connectionModel.getSourceMultiplicity());
                arrowView.setTargetMultiplicity(connectionModel.getTargetMultiplicity());
                arrowView.setSourceRoleName(connectionModel.getSourceRoleName());
                arrowView.setTargetRoleName(connectionModel.getTargetRoleName());
                
                arrowView.setSourceX(connectionModel.getSourceOffsetX());
                arrowView.setSourceY(connectionModel.getSourceOffsetY());
                arrowView.setTargetX(connectionModel.getTargetOffsetX());
                arrowView.setTargetY(connectionModel.getTargetOffsetY());
                arrowView.setMidlineX(connectionModel.getMidlineX());
                arrowView.setMidlineY(connectionModel.getMidlineY());
            }
        }
    }
}
