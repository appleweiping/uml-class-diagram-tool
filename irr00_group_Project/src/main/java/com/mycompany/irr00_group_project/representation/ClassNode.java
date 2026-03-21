/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt
 * to change this license
 *
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java
 * to edit this template
 */

package com.mycompany.irr00_group_project.representation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Class node for UML diagram, including its
 * attributes and operations.
 * 
 * @author Long Pham
 * @author Aiham Al-Ashwal
 * @author Abdul Gadaborchev
 * 
 */
public class ClassNode {
    
    private DiagramData parent;
    
    private String className = "";
    private ClassType classType = ClassType.NONE;
    private final List<String> attributes = new ArrayList<>();
    private final List<String> operations = new ArrayList<>();
    private double layoutX;
    private double layoutY;
    private double width;
    private double height;

    /**
     * Create an empty class.
     */
    public ClassNode() {
    }
    
    /**
     * Get the parent diagram the class is located in.
     * @return diagram in which the class is located. null if class is not in any diagram.
     */
    DiagramData getParent() {
        return parent;
    }
    
    /**
     * Set the parent diagram of the class.
     * @param parent the new parent diagram of the class
     */
    void setParent(DiagramData parent) {
        this.parent = parent;
    }

    // Getters
    
    /**
     * Get type of class.
     * @return type of class
     */
    public ClassType getClassType() { 
        return classType; 
    }

    /**
     * Get class name.
     * @return class name
     */
    public String getClassName() { 
        return className; 
    }

    /**
     * Get (read only) list of class attributes.
     * @return (read only) list of class attributes
     */
    public List<String> getAttributes() {
        return Collections.unmodifiableList(attributes); 
    }
    
    /**
     * Get all attributes joined by new line characters.
     * @return all attributes joined by new line characters
     */
    public String getAttributesFlat() {
        StringBuilder attr = new StringBuilder();
        for (String attribute : attributes) {
            if (!attr.isEmpty()) {
                attr.append('\n');
            }
            
            attr.append(attribute);
        }
        
        return attr.toString();
    }

    /**
     * Get (read only) list of class operations.
     * @return (read only) list of class operations
     */
    public List<String> getOperations() {
        return Collections.unmodifiableList(operations); 
    }
    
    /**
     * Get all operations joined by new line characters.
     * @return all operations joined by new line characters
     */
    public String getOperationsFlat() {
        StringBuilder op = new StringBuilder();
        for (String operation : operations) {
            if (!op.isEmpty()) {
                op.append('\n');
            }
            
            op.append(operation);
        }
        
        return op.toString();
    }

    /**
     * Get x position of the class.
     * @return x position of the class, relative to canvas space
     */
    public double getLayoutX() { 
        return layoutX; 
    }

    /**
     * Get y position of the class.
     * @return y position of the class, relative to canvas space
     */
    public double getLayoutY() {
        return layoutY; 
    }

    /**
     * Get width of the class.
     * @return width of the class
     */
    public double getWidth() { 
        return width; 
    }

    /**
     * Get height of the class.
     * @return height of the class
     */
    public double getHeight() {
        return height; 
    }

    // Setters
    
    /**
     * Sets the name of the class.
     * 
     * @param className name of the class to set
     * @throws NullPointerException if className is null
    */
    public void setClassName(String className) {
        if (className == null) {
            throw new NullPointerException();
        }
        this.className = className;
        
        if (parent != null) {
            parent.notifyObservers();
        }
    }

    /**
     * Sets the type of the class (e.g., class, interface, enum).
     * 
     * @param classType the class type to get
     * @throws NullPointerException if classType is null
     */
    public void setClassType(ClassType classType) { 
        if (classType == null) {
            throw new NullPointerException();
        }
        this.classType = classType;
        
        if (parent != null) {
            parent.notifyObservers();
        }
    }

    /**
     * Sets the attributes of the class.
     * @param attributes Attributes of the diagram.
     * @throws NullPointerException if attributes is null or contains a null string
     */
    
    public void setAttributes(Collection<String> attributes) {
        if (attributes == null) {
            throw new NullPointerException();
        }
        
        for (String s : attributes) {
            if (s == null) {
                throw new NullPointerException();
            }
        }
        
        this.attributes.clear();
        this.attributes.addAll(attributes);
        
        if (parent != null) {
            parent.notifyObservers();
        }
    }

    /**
     * Sets the operation (methods) of the class.
     * @param operations a collection of operation strings
     * @throws NullPointerException if operations is null or contains a null string
     */
    public void setOperations(Collection<String> operations) {
        if (operations == null) {
            throw new NullPointerException();
        }
        
        for (String s : operations) {
            if (s == null) {
                throw new NullPointerException();
            }
        }
        
        this.operations.clear();
        this.operations.addAll(operations);
        
        if (parent != null) {
            parent.notifyObservers();
        }
    }

    /**
     * Sets x position of the class relative to canvas space.
     * @param layoutX the x-coordinate
     * @throws IllegalArgumentException if layoutX is NaN or negative
     */
    public void setLayoutX(double layoutX) {
        if (Double.isNaN(layoutX) || layoutX < 0) {
            throw new IllegalArgumentException();
        }
        this.layoutX = layoutX; 
    }
 
    /**
     * Sets y position of the class relative to canvas space.
     * @param layoutY the y-coordinate
     * @throws IllegalArgumentException if layoutY is NaN or negative
     */
    public void setLayoutY(double layoutY) { 
        if (Double.isNaN(layoutY) || layoutY < 0) {
            throw new IllegalArgumentException();
        }
        this.layoutY = layoutY; 
    }

    /**
     * Sets the width of the class.
     * @param width the width value
     * @throws IllegalArgumentException if width is NaN or negative
     */
    public void setWidth(double width) { 
        if (Double.isNaN(width) || width < 0) {
            throw new IllegalArgumentException();
        }
        this.width = width; 
    }

    /**
     * Sets the height of the class diagram element.
     * @param height the height value
     * @throws IllegalArgumentException if height is NaN or negative
     */
    public void setHeight(double height) { 
        if (Double.isNaN(height) || height < 0) {
            throw new IllegalArgumentException();
        }    
        this.height = height; 
    }
}
