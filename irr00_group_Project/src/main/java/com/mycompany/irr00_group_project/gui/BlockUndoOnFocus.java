package com.mycompany.irr00_group_project.gui;

/**
 * Indicator interface for blocking CTRL+U/Y when the object is focused.
 * 
 * @author Deniz Büyükgüral
 */
public interface BlockUndoOnFocus {
    /**
     * Whether the key event should be consumed if the object is focused and undo key is pressed.
     * @return true if the key event is not consumed. otherwise the key event is consumed.
     */
    default boolean doNotConsumeOnFocus() {
        return false;
    }
}
