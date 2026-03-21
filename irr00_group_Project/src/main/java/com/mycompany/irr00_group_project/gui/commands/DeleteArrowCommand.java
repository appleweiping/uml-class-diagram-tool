package com.mycompany.irr00_group_project.gui.commands;

import com.mycompany.irr00_group_project.Undo.Command;
import com.mycompany.irr00_group_project.gui.ArrowNodeUI;
import com.mycompany.irr00_group_project.gui.DiagramCanvasPane;

/**
 * Command implementation for arrow deletion.
 * 
 * @author Deniz Büyükgüral
 * @author Aiham Al-Ashwal
 */
public class DeleteArrowCommand implements Command {
    
    private final ArrowNodeUI arrow;
    private final DiagramCanvasPane canvas;
    
    public DeleteArrowCommand(ArrowNodeUI arrow) {
        if (arrow == null) {
            throw new IllegalArgumentException("arrow cannot be null");
        }

        this.arrow = arrow;
        this.canvas = arrow.getSourceNode().getCanvas();
    }

    @Override
    public void execute() {
        canvas.removeArrow(arrow);
    }

    @Override
    public void undo() {
        canvas.addArrow(arrow);
    }
}
