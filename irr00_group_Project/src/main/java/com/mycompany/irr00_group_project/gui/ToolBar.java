package com.mycompany.irr00_group_project.gui;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

/**
 * Pane containing buttons and controls for modifying the focused object.
 * 
 * @author Deniz Büyükgüral
 */
class ToolBar extends HBox {
    
    private final MainScene mainScene;
    
    private final AnchorPane toolbarContainer;
    private ClassTypeSelectorPane classTypeSelector;
    private ArrowHeadSelectorPane arrowHeadSelector;
    
    public ToolBar(MainScene mainScene) {
        
        if (mainScene == null) {
            throw new IllegalArgumentException("mainScene cannot be null");
        }
        
        this.mainScene = mainScene;
        
        // Init UI
        setPrefHeight(50.0);
        setBackground(new Background(
                new BackgroundFill(Color.web("#EBEEF2"), CornerRadii.EMPTY, Insets.EMPTY)));
        
        getChildren().add(new Separator(Orientation.VERTICAL));
        
        // Create toolbar container
        toolbarContainer = new AnchorPane();
        getChildren().add(toolbarContainer);
        toolbarContainer.setPrefHeight(50.0);
        HBox.setHgrow(toolbarContainer, Priority.ALWAYS);
        
        initializeClassTypeController();
        initializeArrowHeadSelectorController();
    }
    
    private void initializeClassTypeController() {
        classTypeSelector = new ClassTypeSelectorPane(mainScene);
        toolbarContainer.getChildren().add(classTypeSelector);
        AnchorPane.setLeftAnchor(classTypeSelector, 0.0);
        AnchorPane.setRightAnchor(classTypeSelector, 0.0);
        AnchorPane.setTopAnchor(classTypeSelector, 0.0);
        AnchorPane.setBottomAnchor(classTypeSelector, 0.0);
    }
    
    private void initializeArrowHeadSelectorController() {
        arrowHeadSelector = new ArrowHeadSelectorPane(mainScene);
        toolbarContainer.getChildren().add(arrowHeadSelector);
        AnchorPane.setLeftAnchor(arrowHeadSelector, 0.0);
        AnchorPane.setRightAnchor(arrowHeadSelector, 0.0);
        AnchorPane.setTopAnchor(arrowHeadSelector, 0.0);
        AnchorPane.setBottomAnchor(arrowHeadSelector, 0.0);
    }
}
