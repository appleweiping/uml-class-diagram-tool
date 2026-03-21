package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.representation.ClassType;
import com.mycompany.irr00_group_project.gui.commands.SetClassTypeCommand;
import com.mycompany.irr00_group_project.utils.FxUtils;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * Toolbar for selecting type of class node. Can switch between none, abstract, interface, enum.
 * 
 * @author Deniz Büyükgüral
 */
class ClassTypeSelectorPane extends HBox {
    
    private static final String NONE_IMAGE_URL = "images/NoneType.png";
    private static final String ABSTRACT_IMAGE_URL = "images/AbstractType.png";
    private static final String INTERFACE_IMAGE_URL = "images/InterfaceType.png";
    private static final String ENUM_IMAGE_URL = "images/EnumType.png";
    
    /**
     * Button instance in the toolbar, representing a class type.
     * Can listen to the selected node's type change event.
     */
    class ClassTypeToggleButton extends ToggleButton implements ChangeListener<ClassType> {
        
        private final ClassType classType;
        
        public ClassTypeToggleButton(ClassType classType, String imageUrl) {
            super();
            this.classType = classType;
            
            // Init UI
            setFocusTraversable(false);
            setPrefWidth(50.0);
            setPrefHeight(50.0);
            setMinWidth(USE_PREF_SIZE);
            setMinHeight(USE_PREF_SIZE);
            setMaxWidth(USE_PREF_SIZE);
            setMaxHeight(USE_PREF_SIZE);
            setPadding(new Insets(2.0));
            setStyle("-fx-background-radius: 0;");
            
            Pane buttonGraphic = new Pane();
            buttonGraphic.setBackground(FxUtils.createImageBackground(imageUrl));
            setGraphic(buttonGraphic);
            
            // Add listeners
            selectedNodeProperty.addListener((ov, oldVal, newVal) -> {
                if (oldVal != null) {
                    oldVal.classTypeProperty().removeListener(this);
                }
                
                if (newVal != null) {
                    newVal.classTypeProperty().addListener(this);
                    setSelected(newVal.getClassType() == classType);
                }
            });
            
            setOnAction((e) -> {
                ClassNodePane node = selectedNodeProperty.get();
                if (node.getClassType() == classType) {
                    setSelected(true);
                    return;
                }
                
                mainScene.executeCommand(new SetClassTypeCommand(
                        mainScene.getCanvas(),
                        node,
                        node.getClassType(),
                        classType
                ));
            });
        }
                
        // Triggered when the selected node's type changes. Use to update button status.
        @Override
        public void changed(ObservableValue<? extends ClassType> ov, ClassType oldVal, ClassType newVal) {
            setSelected(newVal == classType);
        }
    }
    
    private final ClassTypeToggleButton noneButton;
    private final ClassTypeToggleButton abstractButton;
    private final ClassTypeToggleButton interfaceButton;
    private final ClassTypeToggleButton enumButton;
    
    private final MainScene mainScene;
    
    private final ObjectProperty<ClassNodePaneUI> selectedNodeProperty
            = new SimpleObjectProperty<>(null);
    
    public ClassTypeSelectorPane(MainScene mainScene) {
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
        
        noneButton = new ClassTypeToggleButton(ClassType.NONE, NONE_IMAGE_URL);
        getChildren().add(noneButton);
        
        abstractButton = new ClassTypeToggleButton(ClassType.ABSTRACT, ABSTRACT_IMAGE_URL);
        getChildren().add(abstractButton);
        
        interfaceButton = new ClassTypeToggleButton(ClassType.INTERFACE, INTERFACE_IMAGE_URL);
        getChildren().add(interfaceButton);
        
        enumButton = new ClassTypeToggleButton(ClassType.ENUM, ENUM_IMAGE_URL);
        getChildren().add(enumButton);
        
        addFocusChangeListener();
        
        // Add listeners
        
        selectedNodeProperty.addListener((ov, oldVal, newVal) -> {
            boolean nodeSelected = newVal != null;
            setVisible(nodeSelected);
            setDisable(!nodeSelected);
        });
    }
    
    /**
     * Listen to focus changes. On focus change, check whether the selected object is a class
     * node. If it is, show the selector on the toolbar. Otherwise, hide the selector.
     */
    private void addFocusChangeListener() {
        // Must be run later as the scene may not have been created yet
        Platform.runLater(() -> {
            mainScene.getScene().focusOwnerProperty().addListener((ov, oldVal, newVal) -> {
                Node focusedObj = newVal;
                while (focusedObj != null) {

                    if (focusedObj instanceof ClassNodePaneUI classNodePane) {
                        selectedNodeProperty.set(classNodePane);
                        return;
                    }

                    focusedObj = focusedObj.getParent();
                }

                selectedNodeProperty.set(null);
            });
        });
    }
}
