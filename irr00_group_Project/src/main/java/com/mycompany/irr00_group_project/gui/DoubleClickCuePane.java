package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.listeners.Observer;
import com.mycompany.irr00_group_project.representation.DiagramData;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * A panel with a gray dashed border which is visible when the canvas is empty.
 * Displays "Double click to create a class" to give a new user info
 * on what to do.
 * 
 * @author Deniz Büyükgüral
 */
public class DoubleClickCuePane extends StackPane implements ImageProcessed, Observer<DiagramData> {

    private static final Border DEFAULT_BORDER = new Border(new BorderStroke(
            Color.GRAY,
            BorderStrokeStyle.DASHED,
            CornerRadii.EMPTY,
            new BorderWidths(12.0)
    ));
    
    private final DiagramCanvasPane canvas;
    
    // Hide the pane on image export
    
    @Override
    public void onPreImageExport() {
        setVisible(false);
    }

    @Override
    public void onPostImageExport() {
        setVisible(canvas.getModel().getClassNodes().isEmpty());
    }
    
    /**
     * Create a new cue pane. The pane will fit to the parent region.
     * @param canvas model of the canvas will be used for checking class node count
     * @param parent pane will fit to the given parent
     */
    public DoubleClickCuePane(DiagramCanvasPane canvas, Region parent) {
        
        if (canvas == null) {
            throw new NullPointerException("canvas cannot be null");
        }
        
        this.canvas = canvas;
        
        // Init UI
        setMouseTransparent(true);
        setBorder(DEFAULT_BORDER);
        
        prefWidthProperty().bind(parent.widthProperty());
        prefHeightProperty().bind(parent.heightProperty());

        Label label = new Label("Double click to create a class");
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setTextFill(Color.GRAY);
        label.setFont(Font.font(null, FontWeight.BOLD, 48.0));
        
        getChildren().add(label);
        StackPane.setAlignment(label, Pos.CENTER);
        
        canvas.getModel().registerObserver(this);
        setVisible(canvas.getModel().getClassNodes().isEmpty());
    }

    /**
     * We check if there are any classes in the diagram.
     * @param subject the updated diagram
     */
    @Override
    public void update(DiagramData subject) {
        setVisible(subject.getClassNodes().isEmpty());
    }
}
