package com.mycompany.irr00_group_project.gui.commands;

import com.mycompany.irr00_group_project.Undo.Command;
import com.mycompany.irr00_group_project.gui.ClassNodePane;
import com.mycompany.irr00_group_project.gui.DiagramCanvasPane;
import javafx.geometry.Point2D;
import javafx.scene.control.TextInputControl;

/**
 * Command implementation for class node name/operation/attribute text changes.
 * 
 * @author Deniz Büyükgüral
 * @author Aiham Al-Ashwal
 */
public class SetClassNodeTextCommand implements Command {
    
    private final DiagramCanvasPane canvas;
    
    private final ClassNodePane node;
    private final TextInputControl field;
    
    private final String initialText;
    private final Point2D initialSize;
    
    private final String newText;
    private final Point2D newSize;
    
    public SetClassNodeTextCommand(
            DiagramCanvasPane canvas,
            ClassNodePane node,
            TextInputControl field,
            String initialText,
            Point2D initialSize,
            String newText,
            Point2D newSize) {
        
        if (canvas == null) {
            throw new IllegalArgumentException("canvas cannot be null");
        }
        
        if (node == null) {
            throw new IllegalArgumentException("initialSize cannot be null");
        }
        
        if (field == null) {
            throw new IllegalArgumentException("initialSize cannot be null");
        }
        
        if (initialSize == null) {
            throw new IllegalArgumentException("initialSize cannot be null");
        }
        
        if (newSize == null) {
            throw new IllegalArgumentException("initialSize cannot be null");
        }

        this.canvas = canvas;
        this.node = node;
        this.field = field;
        this.initialText = initialText;
        this.initialSize = initialSize;
        this.newText = newText;
        this.newSize = newSize;
    }
    
    @Override
    public void execute() {
        node.setNodeWidth(newSize.getX());
        node.setNodeHeight(newSize.getY());
        field.setText(newText);
    }

    @Override
    public void undo() {
        field.setText(initialText);
        node.setNodeWidth(initialSize.getX());
        node.setNodeHeight(initialSize.getY());
    }
}
