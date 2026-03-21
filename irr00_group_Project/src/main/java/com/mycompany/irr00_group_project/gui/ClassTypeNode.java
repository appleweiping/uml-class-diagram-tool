package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.representation.ClassType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Created by ClassNodePane. Displays the type of the class node;
 * <<interface>>, <<abstract>> etc.
 * 
 * @author Deniz Büyükgüral
 */
class ClassTypeNode extends StackPane {
    
    
    private final Label typeLabel;
    
    private final ObjectProperty<ClassType> classTypeProperty
            = new SimpleObjectProperty<>(ClassType.NONE);
    
    public ClassTypeNode() {
        super();
        
        setMaxWidth(Double.MAX_VALUE);
        
        typeLabel = new Label();
        getChildren().add(typeLabel);
        StackPane.setAlignment(typeLabel, Pos.CENTER);
        typeLabel.setFont(Font.font(null, FontWeight.THIN, 12.0));
        typeLabel.prefWidthProperty().bind(super.prefWidthProperty());
        
        classTypeProperty.addListener((ov, oldVal, newVal) -> {
            switch (newVal) {
                case NONE -> hideNode(); 
                case ABSTRACT -> showNode("<<abstract>>");     
                case INTERFACE -> showNode("<<interface>>");     
                case ENUM -> showNode("<<enum>>");
            }
        });
        
        hideNode();
    }
    
    /**
     * Makes the node invisible and sets height to 0.
     */
    private void hideNode() {
        setMinHeight(0.0);
        setPrefHeight(0.0);
        setDisable(true);
        setVisible(false);
    }

    /**
     * Makes the node visible and sets height to minimum required height.
     * @param text text to show on the class name
     */
    private void showNode(String text) {
        typeLabel.setText(text);
        setPrefHeight(USE_COMPUTED_SIZE);
        setMinHeight(USE_PREF_SIZE);
        setDisable(false);
        setVisible(true);
    }
    
    // Properties
    
    /**
     * Get class type of the node.
     * @return class type of the node
     */
    public ClassType getClassType() {
        return classTypeProperty.get();
    }
    
    /**
     * Set class type of the node.
     * @param classType class type of the node
     */
    public void setClassType(ClassType classType) {
        classTypeProperty.set(classType);
    }
    
    /**
     * Property bound to the class type of the node.
     * @return Property bound to the class type of the node
     */
    public ObjectProperty<ClassType> classTypeProperty() {
        return classTypeProperty;
    }
}
