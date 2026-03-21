package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.gui.commands.SetArrowHeadCommand;
import com.mycompany.irr00_group_project.representation.ConnectionType;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeType;
import javafx.scene.shape.VLineTo;

/**
 * Toolbar for selecting arrow head type. Gets enabled when segment of an arrow is selected.
 *
 * @author Deniz Büyükgüral
 */
class ArrowHeadSelectorPane extends HBox {
    
    /**
     * Extended toggle button for the toolbar.
     * Can listen for selected arrow's head type change event.
     */
    class ArrowHeadToggleButton extends ToggleButton implements ChangeListener<ConnectionType> {
        
        private final ConnectionType headType;
        
        /**
         * Creates a new toggle button, which when pressed sets the type of the arrow head
         * of the selected arrow to the given argument.
         * @param headType type of connection which selected arrow is set to when the button
         *                 is pressed.
         */
        public ArrowHeadToggleButton(ConnectionType headType) {
            super();
            this.headType = headType;
            
            // Init UI
            setFocusTraversable(false);
            setPrefWidth(50.0);
            setPrefHeight(50.0);
            setMinWidth(USE_PREF_SIZE);
            setMinHeight(USE_PREF_SIZE);
            setMaxWidth(USE_PREF_SIZE);
            setMaxHeight(USE_PREF_SIZE);
            setStyle("-fx-background-radius: 0;");
            
            // Add listeners
            selectedArrowProperty.addListener((ov, oldVal, newVal) -> {
                if (oldVal != null) {
                    oldVal.headTypeProperty().removeListener(this);
                }
                
                if (newVal != null) {
                    newVal.headTypeProperty().addListener(this);
                    setSelected(newVal.getHeadType() == this.headType);
                }
            });
            
            setOnAction((e) -> {
                ArrowNode arrow = selectedArrowProperty.get();
                if (arrow.getHeadType() == headType) {
                    setSelected(true);
                    return;
                }
                
                mainScene.executeCommand(new SetArrowHeadCommand(
                        mainScene.getCanvas(),
                        arrow,
                        arrow.getHeadType(),
                        headType));
            });
        }

        /**
         * Called when connection type of the selected arrow changes.
         */
        @Override
        public void changed(
                ObservableValue<? extends ConnectionType> ov,
                ConnectionType oldVal,
                ConnectionType newVal) {
            
            setSelected(newVal == this.headType);
        }
    }
    
    private final ArrowHeadToggleButton associationButton;
    private final ArrowHeadToggleButton inheritanceButton;
    private final ArrowHeadToggleButton aggregationButton;
    private final ArrowHeadToggleButton compositionButton;
    
    private final MainScene mainScene;
    
    private final ObjectProperty<ArrowNode> selectedArrowProperty
            = new SimpleObjectProperty<>(null);
    
    /**
     * Create instance of the arrow head selector toolbar.
     * @param mainScene main scene the toolbar is created in
     * @pre {mainScene != null}
     * @throws IllegalArgumentException if preconditions are violated
     */
    public ArrowHeadSelectorPane(MainScene mainScene) {
        super();
        
        if (mainScene == null) {
            throw new IllegalArgumentException("mainScene cannot be null");
        }
        this.mainScene = mainScene;
        
        // Initialize UI
        
        setDisable(true);
        setVisible(false);
        setPrefHeight(50.0);
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(USE_PREF_SIZE);
        
        // Association button
        associationButton = new ArrowHeadToggleButton(ConnectionType.ASSOCIATION);
        getChildren().add(associationButton);
        Path associationGraphic = new Path();
        associationGraphic.setFill(Color.WHITE);
        associationGraphic.setStroke(Color.BLACK);
        associationGraphic.setStrokeType(StrokeType.INSIDE);
        associationGraphic.getElements().addAll(
                new MoveTo(20.66, 0.0),
                new VLineTo(40.0),
                new HLineTo(19.33),
                new VLineTo(0.0)
        );
        associationButton.setGraphic(associationGraphic);
        
        // Inheritance button
        inheritanceButton = new ArrowHeadToggleButton(ConnectionType.INHERITANCE);
        getChildren().add(inheritanceButton);
        Path inheritanceGraphic = new Path();
        inheritanceGraphic.setFill(Color.WHITE);
        inheritanceGraphic.setStroke(Color.BLACK);
        inheritanceGraphic.setStrokeType(StrokeType.INSIDE);
        inheritanceGraphic.setScaleX(0.66);
        inheritanceGraphic.setScaleY(0.66);
        inheritanceGraphic.getElements().addAll(
                new MoveTo(0.0, 20.0),
                new LineTo(20.0, 0.0),
                new LineTo(40.0, 20.0),
                new HLineTo(21.0),
                new VLineTo(60.0),
                new HLineTo(19.0),
                new VLineTo(20.0),
                new ClosePath()
        );
        inheritanceButton.setGraphic(inheritanceGraphic);
        
        // Aggregation button
        aggregationButton = new ArrowHeadToggleButton(ConnectionType.AGGREGATION);
        getChildren().add(aggregationButton);
        Path aggregationGraphic = new Path();
        aggregationGraphic.setFill(Color.WHITE);
        aggregationGraphic.setStroke(Color.BLACK);
        aggregationGraphic.setStrokeType(StrokeType.INSIDE);
        aggregationGraphic.setScaleX(0.66);
        aggregationGraphic.setScaleY(0.66);
        aggregationGraphic.getElements().addAll(
                new MoveTo(20.0, 40.0),
                new LineTo(5.0, 20.0),
                new LineTo(20.0, 0.0),
                new LineTo(35.0, 20.0),
                new ClosePath(),
                new MoveTo(21.0, 38.0),
                new VLineTo(60.0),
                new HLineTo(19.0),
                new VLineTo(38.0)
        );
        aggregationButton.setGraphic(aggregationGraphic);
        
        // Composition button
        compositionButton = new ArrowHeadToggleButton(ConnectionType.COMPOSITION);
        getChildren().add(compositionButton);
        Path compositionGraphic = new Path();
        compositionGraphic.setFill(Color.BLACK);
        compositionGraphic.setStroke(Color.BLACK);
        compositionGraphic.setStrokeType(StrokeType.INSIDE);
        compositionGraphic.setScaleX(0.66);
        compositionGraphic.setScaleY(0.66);
        compositionGraphic.getElements().addAll(
                new MoveTo(20.0, 40.0),
                new LineTo(5.0, 20.0),
                new LineTo(20.0, 0.0),
                new LineTo(35.0, 20.0),
                new ClosePath(),
                new MoveTo(21.0, 38.0),
                new VLineTo(60.0),
                new HLineTo(19.0),
                new VLineTo(38.0)
        );
        compositionButton.setGraphic(compositionGraphic);
        
        // Add listeners
        
        addFocusChangeListener();
        
        selectedArrowProperty.addListener((ov, oldVal, newVal) -> {
            boolean arrowSelected = newVal != null;
            setVisible(arrowSelected);
            setDisable(!arrowSelected);
        });
    }
    
    /**
     * Listens for the focus change events. When focus changes, checks if the focused
     * object is an arrow instance. If it is, selects the arrow and displays the panel.
     * Otherwise, hides the panel.
     */
    private void addFocusChangeListener() {
        // Scene has not been created yet, run later.
        Platform.runLater(() -> {
            mainScene.getScene().focusOwnerProperty().addListener((ov, oldVal, newVal) -> {
                
                Node focusedNode = newVal;
                while (focusedNode != null) {
                    
                    if (focusedNode instanceof ArrowNode arrowNode) {
                        selectedArrowProperty.set(arrowNode);
                        return;
                    }
                    
                    focusedNode = focusedNode.getParent();
                }
                
                selectedArrowProperty.set(null);
            });
        });
    }
}
