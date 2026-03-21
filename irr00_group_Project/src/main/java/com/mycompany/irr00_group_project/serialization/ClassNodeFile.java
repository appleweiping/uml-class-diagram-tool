package com.mycompany.irr00_group_project.serialization;

import com.mycompany.irr00_group_project.representation.ClassNode;
import com.mycompany.irr00_group_project.representation.ClassType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Serializable representation of a UML class node used for file persistence.
 * Stores essential class data such as name, attributes, operations, and layout info.
 * This class is intended for saving/loading diagrams to/from files.
 *
 * @author Aiham Al-Ashwal
 * @author Abdul Gadaborchev
 */
public class ClassNodeFile implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String className;
    private List<String> attributes;
    private List<String> operations;
    private double layoutX;
    private double layoutY;
    private double width;
    private double height;
    private ClassType classType;


    /**
     * Default constructor initializes attribute and operation lists.
     */
    public ClassNodeFile() {
        attributes = new ArrayList<>();
        operations = new ArrayList<>();
        className = "";
        classType = ClassType.NONE;
    }
    
    /**
     * Create a class node file instance from class node.
     * @param liveNode the node to clone
     * @throws NullPointerException if liveNode is null
     */
    public ClassNodeFile(ClassNode liveNode) {
        
        if (liveNode == null) {
            throw new NullPointerException();
        }
        
        setClassName(liveNode.getClassName());
        setAttributes(liveNode.getAttributes());
        setOperations(liveNode.getOperations());
        setLayoutX(liveNode.getLayoutX());
        setLayoutY(liveNode.getLayoutY());
        setWidth(liveNode.getWidth());
        setHeight(liveNode.getHeight());
        setClassType(liveNode.getClassType());
    }

    /**
     * Returns the unique identifier for this class node.
     * 
     * @return node ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this class node.
     * 
     * @param id node ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the class.
     * 
     * @return class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets the name of the class.
     * 
     * @param className class name
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Gets the list of attributes.
     * 
     * @return list of attributes
     */
    public List<String> getAttributes() {
        return attributes;
    }

    /**
     * Sets the list of attributes.
     * 
     * @param attributes list of attributes
     */
    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    /**
     * Gets the list of operations.
     * 
     * @return list of operations
     */
    public List<String> getOperations() {
        return operations;
    }
    /**
     * Returns the type of the class.
     *
     * @return the class type
     */

    public ClassType getClassType() {
        return classType;
    }

    /**
     * Sets the type of the class.
     *
     * @param classType the class type to set
     * @throws NullPointerException if classType is null
     */
    public void setClassType(ClassType classType) {
        if (classType == null) {
            throw new NullPointerException("classType must not be null");
        }
        this.classType = classType;
    }

    /**
     * Sets the list of operations.
     * 
     * @param operations list of operations
     */
    public void setOperations(List<String> operations) {
        this.operations = operations;
    }

    /**
     * Gets the X-coordinate of the node's layout.
     * 
     * @return layout X
     */
    public double getLayoutX() {
        return layoutX;
    }

    /**
     * Sets the X-coordinate of the node's layout.
     * 
     * @param layoutX layout X
     */
    public void setLayoutX(double layoutX) {
        this.layoutX = layoutX;
    }

    /**
     * Gets the Y-coordinate of the node's layout.
     * 
     * @return layout Y
     */
    public double getLayoutY() {
        return layoutY;
    }

    /**
     * Sets the Y-coordinate of the node's layout.
     * 
     * @param layoutY layout Y
     */
    public void setLayoutY(double layoutY) {
        this.layoutY = layoutY;
    }

    /**
     * Gets the width of the node.
     * 
     * @return width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Sets the width of the node.
     * 
     * @param width width
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Gets the height of the node.
     * 
     * @return height
     */
    public double getHeight() {
        return height;
    }

    /**
     * Sets the height of the node.
     * 
     * @param height height
     */
    public void setHeight(double height) {
        this.height = height;
    }
}