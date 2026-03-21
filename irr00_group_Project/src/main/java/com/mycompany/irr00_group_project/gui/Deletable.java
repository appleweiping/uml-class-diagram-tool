package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.Undo.Command;

/**
 * Common interface for controllers that can be deleted from the canvas when focused.
 * 
 * @author Deniz Büyükgüral
 */
interface Deletable {
    /**
     * Creates a command which deletes the object on execution.
     * @return instance of a command which deletes the object when executed. a new command
     *         must be instantiated on each call.
     */
    public Command createDeleteCommand();
}
