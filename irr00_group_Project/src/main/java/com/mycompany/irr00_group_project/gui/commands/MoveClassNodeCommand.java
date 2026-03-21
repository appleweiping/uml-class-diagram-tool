package com.mycompany.irr00_group_project.gui.commands;

import com.mycompany.irr00_group_project.Undo.Command;
import com.mycompany.irr00_group_project.gui.ClassNodePane;

/**
 * Command implementation for moving a class node.
 * 
 * @author Deniz Büyükgüral
 * @author Aiham Al-Ashwal
 */
public class MoveClassNodeCommand implements Command {

    private final ClassNodePane node;
    private final double initialX;
    private final double initialY;
    private final double targetX;
    private final double targetY;
    
    public MoveClassNodeCommand(
            ClassNodePane node,
            double initialX,
            double initialY,
            double targetX,
            double targetY) {

        this.node = node;
        this.initialX = initialX;
        this.initialY = initialY;
        this.targetX = targetX;
        this.targetY = targetY;
    }
    
    @Override
    public void execute() {
        node.moveNode(targetX, targetY);
    }

    @Override
    public void undo() {
        node.moveNode(initialX, initialY);
    }
}
