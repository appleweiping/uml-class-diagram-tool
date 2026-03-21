package com.mycompany.irr00_group_project.gui.commands;

import com.mycompany.irr00_group_project.Undo.Command;
import javafx.beans.property.DoubleProperty;

/**
 * Command implementation for moving a part of the arrow.
 * 
 * @author Deniz Büyükgüral
 * @author Aiham Al-Ashwal
 */
public class MoveArrowCommand implements Command {

    private final DoubleProperty offset;
    private final double previousValue;
    private final double newValue;
    
    /**
     * Create a new move arrow command.
     * @param offset offset property of the arrow segment
     * @param previousValue initial offset
     * @param newValue new offset
     * @pre offset != null
     * @throws IllegalArgumentException if offset == null
     */
    public MoveArrowCommand(
            DoubleProperty offset,
            double previousValue,
            double newValue) {
        
        if (offset == null) {
            throw new IllegalArgumentException("offset cannot be null");
        }
        
        this.offset = offset;
        this.previousValue = previousValue;
        this.newValue = newValue;
    }
    
    @Override
    public void execute() {
        offset.set(newValue);
    }

    @Override
    public void undo() {
        offset.set(previousValue);
    }
}
