package com.mycompany.irr00_group_project.gui.commands;

import com.mycompany.irr00_group_project.Undo.Command;
import com.mycompany.irr00_group_project.gui.ClassNodePane;
import com.mycompany.irr00_group_project.representation.ClassNode;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;

/**
 * Command implementation for resizing class node.
 * 
 * @author Deniz Büyükgüral
 * @author Aiham Al-Ashwal
 */
public class ResizeClassNodeCommand implements Command {

    private final ClassNodePane node;
    private final Point2D initialSize;
    private final Point2D targetSize;
    private final Point2D initialPosition;
    private final Point2D targetPosition;
    private final ClassNode modelNode;
    
    public ResizeClassNodeCommand(ClassNodePane node, Point2D targetSize, Pos resizeDirection) {
        
        if (node == null) {
            throw new IllegalArgumentException("node cannot be null");
        }
        
        if (targetSize == null) {
            throw new IllegalArgumentException("targetSize cannot be null");
        }
        
        if (resizeDirection == null) {
            throw new IllegalArgumentException("resizeDirection cannot be null");
        }
        
        this.node = node;
        this.initialSize = new Point2D(node.getNodeWidth(), node.getNodeHeight());
        this.targetSize = targetSize;
        this.initialPosition = new Point2D(node.getX(), node.getY());
        this.modelNode = node.getModel();
        
        double targetX = node.getX();
        double targetY = node.getY();
        
        if (resizeDirection.getHpos() == HPos.LEFT) {
            double deltaX = targetSize.getX() - initialSize.getX();
            targetX -= deltaX;
        }

        if (resizeDirection.getVpos() == VPos.TOP) {
            double deltaY = targetSize.getY() - initialSize.getY();
            targetY -= deltaY;
        }
        
        this.targetPosition = new Point2D(targetX, targetY);
    }
    
    @Override
    public void execute() {
        node.setX(targetPosition.getX());
        node.setY(targetPosition.getY());
        node.setNodeWidth(targetSize.getX());
        node.setNodeHeight(targetSize.getY());
        modelNode.setLayoutX(targetPosition.getX());
        modelNode.setLayoutY(targetPosition.getY());
        modelNode.setWidth(targetSize.getX());
        modelNode.setHeight(targetSize.getY());
    }

    @Override
    public void undo() {
        node.setNodeWidth(initialSize.getX());
        node.setNodeHeight(initialSize.getY());
        node.setX(initialPosition.getX());
        node.setY(initialPosition.getY());
        modelNode.setWidth(initialSize.getX());
        modelNode.setHeight(initialSize.getY());
        modelNode.setLayoutX(initialPosition.getX());
        modelNode.setLayoutY(initialPosition.getY());
    }
}
