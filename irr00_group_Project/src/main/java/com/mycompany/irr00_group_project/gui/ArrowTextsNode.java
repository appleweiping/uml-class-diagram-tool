package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.gui.commands.SetArrowTextCommand;
import com.mycompany.irr00_group_project.gui.data.Anchor;
import com.mycompany.irr00_group_project.representation.UMLConnection;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Group containing four text fields of an arrow. Created by {@code ArrowController}
 *
 * @author Deniz Büyükgüral
 */
public class ArrowTextsNode extends Group {
    
    /**
     * Extension of TextField for arrow text fields. Stores initial value of the field when the
     * field is focused. Once the focus is lost, compares the new value to the
     * initial value. Sends command to undo manager if the text was changed.
     */
    class ArrowTextField extends TextField implements ImageProcessed, BlockUndoOnFocus {
        
        @Override
        public boolean doNotConsumeOnFocus() {
            return true;
        }
        
        private static final Border DEFAULT_BORDER = new Border(new BorderStroke(
                Color.GRAY,
                BorderStrokeStyle.DASHED,
                CornerRadii.EMPTY,
                BorderWidths.DEFAULT
        ));
        
        public ArrowTextField() {
            super();
            
            setFocusTraversable(false);
            setPrefColumnCount(1);
            setPrefHeight(12.0);
            setMinWidth(USE_PREF_SIZE);
            setMinHeight(USE_PREF_SIZE);
            setMaxWidth(USE_PREF_SIZE);
            setMaxHeight(USE_PREF_SIZE);
            setBorder(DEFAULT_BORDER);
            setStyle("-fx-background-color: transparent;");
            setPadding(Insets.EMPTY);
            setFont(Font.font(10.0));
            
            bindListeners();
        }
        
        private String initialText = "";
        
        private void bindListeners() {
            focusedProperty().addListener((ov, oldVal, newVal) -> {
                if (newVal) {
                    initialText = getText();
                } else {
                    String newText = getText();
                    if (newText.equals(initialText)) {
                        return;
                    }
                    
                    arrow.getSourceNode().getCanvas().getMainScene().executeCommand(
                            new SetArrowTextCommand(arrow, this, initialText, getText()));
                }
            });
        }
        
        @Override
        public void onPreImageExport() {
            setBorder(Border.EMPTY);
        }

        @Override
        public void onPostImageExport() {
            setBorder(DEFAULT_BORDER);
        }
    }
    
    private final ArrowTextField sourceTop;
    private final ArrowTextField sourceBottom;
    private final ArrowTextField targetTop;
    private final ArrowTextField targetBottom;
    
    private final ArrowNodeUI arrow;
    
    /**
     * Instantiate text fields for an arrow.
     * @param arrow the arrow text fields are bound to
     * @pre {arrow != null}
     * @throws IllegalArgumentException if preconditions are violated
     */
    public ArrowTextsNode(ArrowNodeUI arrow) {
        super();
        
        if (arrow == null) {
            throw new IllegalArgumentException("arrow cannot be null");
        }
        
        this.arrow = arrow;
        
        // Initialize UI
        sourceTop = new ArrowTextField();
        sourceBottom = new ArrowTextField();
        targetTop = new ArrowTextField();
        targetBottom = new ArrowTextField();
        
        getChildren().addAll(sourceTop, sourceBottom, targetTop, targetBottom);
        
        bindTextPositions();
        
        Platform.runLater(() -> {
            applyCss();
            bindFieldWidths();
        });
        
        UMLConnection model = arrow.getModel();
        
        // Bind model to view
        sourceTop.textProperty().addListener((ov, oldVal, newVal)
                -> model.setSourceMultiplicity(newVal));
        
        targetTop.textProperty().addListener((ov, oldVal, newVal)
                -> model.setTargetMultiplicity(newVal));
        
        sourceBottom.textProperty().addListener((ov, oldVal, newVal)
                -> model.setSourceRoleName(newVal));
        
        targetBottom.textProperty().addListener((ov, oldVal, newVal)
                -> model.setTargetRoleName(newVal));
    }
    
    /**
     * Get the inner node containing the text of the text field.
     * @param field TextArea or TextField whose text content will be returned
     * @return node containing the text of the text field
     * @pre field is initialized before calling
     */
    private static Text getTextNode(TextInputControl field) {
        return (Text) field.lookup(".text");
    }
    
    /**
     * Bounds minimum width of the text field to the width of the inner text.
     * @param field the field whose width will be bound to its text width
     */
    private static void bindFieldWidthToText(TextField field) {
        Text text = getTextNode(field);
        
        field.minWidthProperty().bind(Bindings.createDoubleBinding(() -> {
            return text.getLayoutBounds().getWidth() + 5;
        }, text.boundsInLocalProperty(), text.textProperty()));
    }
    
    /**
     * Binds width of all text fields to their content.
     */
    private void bindFieldWidths() {
        bindFieldWidthToText(sourceTop);
        bindFieldWidthToText(sourceBottom);
        bindFieldWidthToText(targetTop);
        bindFieldWidthToText(targetBottom);
    }
    
    /**
     * Bind position of the text fields with respect to the anchored side of the node.
     * Since all JavaFX nodes are anchored at their upper left corner, anchor must be
     * taken into account.
     */
    private void bindTextPositions() {
        Line sourceLine = arrow.getSourceLine();
        Line targetLine = arrow.getTargetLine();
        
        final double NODE_OFFSET = 2.0;
        final double LINE_OFFSET = 3.0;
        
        // Bind source text fields
        switch (arrow.getSourceAnchor()) {
            case Anchor.LEFT:
                sourceTop.setAlignment(Pos.CENTER_RIGHT);
                sourceBottom.setAlignment(Pos.CENTER_RIGHT);
                
                sourceTop.layoutXProperty().bind(
                        sourceLine.startXProperty()
                                .subtract(NODE_OFFSET)
                                .subtract(sourceTop.widthProperty()));
                
                sourceTop.layoutYProperty().bind(
                        sourceLine.startYProperty()
                                .subtract(LINE_OFFSET)
                                .subtract(sourceTop.heightProperty()));
                
                sourceBottom.layoutXProperty().bind(
                        sourceLine.startXProperty()
                                .subtract(NODE_OFFSET)
                                .subtract(sourceBottom.widthProperty()));
                
                sourceBottom.layoutYProperty().bind(
                        sourceLine.startYProperty()
                                .add(LINE_OFFSET));
                break;
            
            case Anchor.RIGHT:
                sourceTop.layoutXProperty().bind(
                        sourceLine.startXProperty()
                                .add(NODE_OFFSET));
                
                sourceTop.layoutYProperty().bind(
                        sourceLine.startYProperty()
                                .subtract(LINE_OFFSET)
                                .subtract(sourceTop.heightProperty()));
                
                sourceBottom.layoutXProperty().bind(
                        sourceLine.startXProperty()
                                .add(NODE_OFFSET));
                
                sourceBottom.layoutYProperty().bind(
                        sourceLine.startYProperty()
                                .add(LINE_OFFSET));
                break;
            
            case Anchor.TOP:
                sourceTop.setAlignment(Pos.CENTER_RIGHT);
                
                sourceTop.layoutXProperty().bind(
                        sourceLine.startXProperty()
                                .subtract(LINE_OFFSET)
                                .subtract(sourceTop.widthProperty()));
                
                sourceTop.layoutYProperty().bind(
                        sourceLine.startYProperty()
                                .subtract(NODE_OFFSET)
                                .subtract(sourceTop.heightProperty()));
                
                sourceBottom.layoutXProperty().bind(
                        sourceLine.startXProperty()
                                .add(LINE_OFFSET));
                
                sourceBottom.layoutYProperty().bind(
                        sourceLine.startYProperty()
                                .subtract(NODE_OFFSET)
                                .subtract(sourceBottom.heightProperty()));
                break;
                
            case Anchor.BOTTOM:
                sourceTop.setAlignment(Pos.CENTER_RIGHT);
                
                sourceTop.layoutXProperty().bind(
                        sourceLine.startXProperty()
                                .subtract(LINE_OFFSET)
                                .subtract(sourceTop.widthProperty()));
                
                sourceTop.layoutYProperty().bind(
                        sourceLine.startYProperty()
                                .add(NODE_OFFSET));
                
                sourceBottom.layoutXProperty().bind(
                        sourceLine.startXProperty()
                                .add(LINE_OFFSET));
                
                sourceBottom.layoutYProperty().bind(
                        sourceLine.startYProperty()
                                .add(NODE_OFFSET));
                break;
        }
        
        // Bind target text fields
        switch (arrow.getTargetAnchor()) {    
            case LEFT:
                targetTop.setAlignment(Pos.CENTER_RIGHT);
                targetBottom.setAlignment(Pos.CENTER_RIGHT);
                
                targetTop.layoutXProperty().bind(
                        targetLine.startXProperty()
                                .subtract(NODE_OFFSET)
                                .subtract(arrow.headWidthProperty())
                                .subtract(targetTop.widthProperty()));
                
                targetTop.layoutYProperty().bind(
                        targetLine.startYProperty()
                                .subtract(LINE_OFFSET)
                                .subtract(targetTop.heightProperty()));
                
                targetBottom.layoutXProperty().bind(
                        targetLine.startXProperty()
                                .subtract(NODE_OFFSET)
                                .subtract(arrow.headWidthProperty())
                                .subtract(targetBottom.widthProperty()));
                
                targetBottom.layoutYProperty().bind(
                        targetLine.startYProperty()
                                .add(LINE_OFFSET));
                break;
                
            case RIGHT:
                targetTop.layoutXProperty().bind(
                        targetLine.startXProperty()
                                .add(NODE_OFFSET)
                                .add(arrow.headWidthProperty()));
                
                targetTop.layoutYProperty().bind(
                        targetLine.startYProperty()
                                .subtract(LINE_OFFSET)
                                .subtract(targetTop.heightProperty()));
                
                targetBottom.layoutXProperty().bind(
                        targetLine.startXProperty()
                                .add(NODE_OFFSET)
                                .add(arrow.headWidthProperty()));
                
                targetBottom.layoutYProperty().bind(
                        targetLine.startYProperty()
                                .add(LINE_OFFSET));
                break;
                
            case TOP:
                targetTop.setAlignment(Pos.CENTER_RIGHT);
                
                targetTop.layoutXProperty().bind(
                        targetLine.startXProperty()
                                .subtract(LINE_OFFSET)
                                .subtract(targetTop.widthProperty()));
                
                targetTop.layoutYProperty().bind(
                        targetLine.startYProperty()
                                .subtract(NODE_OFFSET)
                                .subtract(targetTop.heightProperty())
                                .subtract(arrow.headHeightProperty()));
                
                targetBottom.layoutXProperty().bind(
                        targetLine.startXProperty()
                                .add(LINE_OFFSET));
                
                targetBottom.layoutYProperty().bind(
                        targetLine.startYProperty()
                                .subtract(NODE_OFFSET)
                                .subtract(targetBottom.heightProperty())
                                .subtract(arrow.headHeightProperty()));
                break;
                
            case BOTTOM:
                targetTop.setAlignment(Pos.CENTER_RIGHT);
                
                targetTop.layoutXProperty().bind(
                        targetLine.startXProperty()
                                .subtract(LINE_OFFSET)
                                .subtract(targetTop.widthProperty()));
                
                targetTop.layoutYProperty().bind(
                        targetLine.startYProperty()
                                .add(NODE_OFFSET)
                                .add(arrow.headHeightProperty()));
                
                targetBottom.layoutXProperty().bind(
                        targetLine.startXProperty()
                                .add(LINE_OFFSET));
                
                targetBottom.layoutYProperty().bind(
                        targetLine.startYProperty()
                                .add(NODE_OFFSET)
                                .add(arrow.headHeightProperty()));
                break;
        }
    }

    /**
     * Get the upper text of the source line.
     * @return upper text of the source line
     */
    public String getSourceMultiplicity() {
        return sourceTop.getText();
    }

    /**
     * Get the upper text of the target line.
     * @return upper text of the target line.
     */
    public String getTargetMultiplicity() {
        return targetTop.getText();
    }

    /**
     * Set the upper text of the source line.
     * @param text upper text of the source line
     */
    public void setSourceMultiplicity(String text) {
        this.sourceTop.setText(text);
    }

    /**
     * Set the upper text of the target line.
     * @param text upper text of the target line
     */
    public void setTargetMultiplicity(String text) {
        this.targetTop.setText(text);
    }

    /**
     * Get lower text of the source line.
     * @return lower text of the source line
     */
    public String getSourceRoleName() {
        return sourceBottom.getText();
    }
    
    /**
     * Set lower text of the source line.
     * @param text lower text of the source line
     */
    public void setSourceRoleName(String text) {
        sourceBottom.setText(text);
    }
    
    /**
     * Get lower text of the target line.
     * @return lower text of the target line
     */
    public String getTargetRoleName() {
        return targetBottom.getText();
    }
    
    /**
     * Set lower text of the target line.
     * @param text lower text of the target line
     */
    public void setTargetRoleName(String text) {
        targetBottom.setText(text);
    }

    public TextInputControl getSourceTopField() {
        return sourceTop;
    }

    public TextInputControl getSourceBottomField() {
        return sourceBottom;
    }

    public TextInputControl getTargetTopField() {
        return targetTop;
    }

    public TextInputControl getTargetBottomField() {
        return targetBottom;
    }
}
