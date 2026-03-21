package com.mycompany.irr00_group_project.gui.commands;

import com.mycompany.irr00_group_project.Undo.Command;
import com.mycompany.irr00_group_project.gui.ArrowNodeUI;
import com.mycompany.irr00_group_project.gui.ClassNodePaneUI;
import com.mycompany.irr00_group_project.gui.DiagramCanvasPane;
import java.util.ArrayList;
import java.util.List;

/**
 * Command implementation for class node deletion.
 * 
 * @author Deniz Büyükgüral
 * @author Aiham Al-Ashwal
 */
public class DeleteClassNodeCommand implements Command {
    
    private final DiagramCanvasPane canvas;
    private final ClassNodePaneUI node;
    private final List<ArrowNodeUI> arrowsToDelete;

    public DeleteClassNodeCommand(ClassNodePaneUI node) {
        if (node == null) {
            throw new IllegalArgumentException("node cannot be null");
        }

        this.node = node;
        this.canvas = node.getCanvas();
        this.arrowsToDelete = new ArrayList<>();
        
        for (ArrowNodeUI arrowView : canvas.getArrows()) {
            if (arrowView.getSourceNode() == node || arrowView.getTargetNode() == node) {
                arrowsToDelete.add(arrowView);
            }
        }
    }


    @Override
    public void execute() {
        for (ArrowNodeUI arrow : arrowsToDelete) {
            canvas.removeArrow(arrow);
        }
        
        if (!canvas.removeClassNode(node)) {
            throw new RuntimeException("Could not remove node");
        }
    }

    @Override
    public void undo() {
        canvas.addClassNode(node);
        for (ArrowNodeUI arrow : arrowsToDelete) {
            canvas.addArrow(arrow);
        }
    }
}
