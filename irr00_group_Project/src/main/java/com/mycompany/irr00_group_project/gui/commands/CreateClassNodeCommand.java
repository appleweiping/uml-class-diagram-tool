package com.mycompany.irr00_group_project.gui.commands;

import com.mycompany.irr00_group_project.Undo.Command;
import com.mycompany.irr00_group_project.gui.ClassNodePaneUI;
import com.mycompany.irr00_group_project.gui.DiagramCanvasPane;

/**
 * Creates a new, empty node in the diagram.
 * 
 * @author Deniz Büyükgüral
 * @author Aiham Al-Ashwal
 */
public class CreateClassNodeCommand implements Command {
    private final DiagramCanvasPane canvas;
    private final double x;
    private final double y;
    
    private ClassNodePaneUI node;

    public CreateClassNodeCommand(DiagramCanvasPane canvas, double x, double y) {
        if (canvas == null) {
            throw new IllegalArgumentException("canvas cannot be null");
        }
        
        this.canvas = canvas;
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void execute() {
        if (node != null) {
            canvas.addClassNode(node);
            return;
        }
        
        node = canvas.createClassNode(x, y, true);
    }

    @Override
    public void undo() {
        if (node == null) {
            throw new RuntimeException("class node was never created");
        }
        
        canvas.removeClassNode(node);
    }
}
