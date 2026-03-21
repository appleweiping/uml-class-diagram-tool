package com.mycompany.irr00_group_project.gui.data;

/**
 * Represents how the arrow can be anchored onto a node.
 * 
 * @author 20241533
 */
public enum Anchor {
    TOP(false, true), BOTTOM(false, true), LEFT(true, false), RIGHT(true, false);

    private final boolean horizontal;
    private final boolean vertical;
    
    private Anchor(boolean horizontal, boolean vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }
    
    public boolean isHorizontal() {
        return horizontal;
    }
    
    public boolean isVertical() {
        return vertical;
    }
}
