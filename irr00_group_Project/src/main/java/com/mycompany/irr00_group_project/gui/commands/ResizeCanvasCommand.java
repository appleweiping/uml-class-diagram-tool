package com.mycompany.irr00_group_project.gui.commands;

import com.mycompany.irr00_group_project.Undo.Command;
import com.mycompany.irr00_group_project.gui.DiagramCanvasPane;
import javafx.geometry.Pos;

/**
 * Command implementation for resizing diagram canvas.
 * 
 * @author Deniz Büyükgüral
 * @author Aiham Al-Ashwal
 */
public class ResizeCanvasCommand implements Command {
    private final DiagramCanvasPane canvas;
    private final double initialWidth;
    private final double initialHeight;
    private final double targetWidth;
    private final double targetHeight;
    private final Pos resizeDirection;
    
    public ResizeCanvasCommand(
            DiagramCanvasPane canvas,
            double targetWidth,
            double targetHeight,
            Pos resizeDirection) {
        
        this.canvas = canvas;
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
        
        initialWidth = canvas.getCanvasWidth();
        initialHeight = canvas.getCanvasHeight();
        
        this.resizeDirection = resizeDirection;
    }
    
    @Override
    public void execute() {
        canvas.setCanvasWidth(targetWidth, resizeDirection.getHpos());
        canvas.setCanvasHeight(targetHeight, resizeDirection.getVpos());
    }

    @Override
    public void undo() {
        canvas.setCanvasWidth(initialWidth, resizeDirection.getHpos());
        canvas.setCanvasHeight(initialHeight, resizeDirection.getVpos());
    }
}
