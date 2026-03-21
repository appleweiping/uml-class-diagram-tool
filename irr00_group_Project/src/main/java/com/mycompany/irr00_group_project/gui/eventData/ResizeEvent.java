package com.mycompany.irr00_group_project.gui.eventData;

import javafx.geometry.Pos;

/**
 * Event data passed to the listeners when resize event is emitted.
 * 
 * @author Deniz Büyükgüral
 */
public record ResizeEvent(double width, double height, Pos resizeDirection) {}
