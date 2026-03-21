package com.mycompany.irr00_group_project.gui.commands;

import com.mycompany.irr00_group_project.Undo.Command;
import com.mycompany.irr00_group_project.gui.ClassNodePane;
import com.mycompany.irr00_group_project.gui.DiagramCanvasPane;
import com.mycompany.irr00_group_project.representation.ClassNode;
import com.mycompany.irr00_group_project.representation.ClassType;
import javafx.geometry.Point2D;

/**
 * Command implementation for setting type of a class node.
 * 
 * @author Deniz Büyükgüral
 * @author Aiham Al-Ashwal
 */
public class SetClassTypeCommand implements Command {

    private final DiagramCanvasPane canvas;
    private final ClassNodePane node;
    private final ClassType initialType;
    private final Point2D initialSize;
    private final ClassType targetType;
    private final ClassNode modelNode;
    
    public SetClassTypeCommand(
            DiagramCanvasPane canvas,
            ClassNodePane node,
            ClassType initialType,
            ClassType targetType) {
        
        if (canvas == null) {
            throw new IllegalArgumentException("canvas cannot be null");
        }
        
        if (node == null) {
            throw new IllegalArgumentException("node cannot be null");
        }
        
        if (initialType == null) {
            throw new IllegalArgumentException("initialType cannot be null");
        }
        
        if (targetType == null) {
            throw new IllegalArgumentException("targetType cannot be null");
        }

        this.canvas = canvas;
        this.modelNode = node.getModel();
        this.node = node;
        this.initialType = initialType;
        this.initialSize = new Point2D(node.getNodeWidth(), node.getNodeHeight());
        this.targetType = targetType;
    }
    
    @Override
    public void execute() {
        node.setClassType(targetType);
        modelNode.setClassType(targetType);
    }

    @Override
    public void undo() {
        node.setClassType(initialType);
        modelNode.setClassType(initialType);
        
        if (node.getNodeWidth() != initialSize.getX()) {
            node.setNodeWidth(initialSize.getX());
        }
        
        if (node.getNodeHeight() != initialSize.getY()) {
            node.setNodeHeight(initialSize.getY());
        }
    }
}
