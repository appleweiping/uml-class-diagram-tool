package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.representation.ClassNode;
import com.mycompany.irr00_group_project.representation.ClassType;
import com.mycompany.irr00_group_project.Undo.Command;
import com.mycompany.irr00_group_project.gui.commands.DeleteClassNodeCommand;
import com.mycompany.irr00_group_project.gui.commands.MoveClassNodeCommand;
import com.mycompany.irr00_group_project.gui.commands.ResizeClassNodeCommand;
import com.mycompany.irr00_group_project.gui.commands.SetClassNodeTextCommand;
import com.mycompany.irr00_group_project.gui.eventData.ResizeEvent;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Graphical instance of a class node in the diagram.
 * 
 * @author Deniz Büyükgüral
 */
class ConcreteClassNodePane extends StackPane
        implements ClassNodePaneUI, Deletable {
    
    /**
     * Extension of text field. Saves initial state of the node on focus.
     * Compares current state to the initial state on focus loss. Executes command
     * if current state differs from the initial state.
     */
    class ClassNodeTextField extends TextField implements ImageProcessed, BlockUndoOnFocus {
        
        @Override
        public boolean doNotConsumeOnFocus() {
            return true;
        }
        
        public ClassNodeTextField() {
            super();
            addListeners();
        }
        
        private double initialWidth;
        private double initialHeight;
        private String initialText;
        
        private void addListeners() {
            focusedProperty().addListener((ov, oldVal, newVal) -> {
                if (newVal) {
                    initialWidth = getNodeWidth();
                    initialHeight = getNodeHeight();
                    initialText = getText();
                } else {
                    double newWidth = getNodeWidth();
                    double newHeight = getNodeHeight();
                    String newText = getText();
                    
                    boolean noChange = newWidth == initialWidth
                            && newHeight == initialHeight
                            && newText.equals(initialText);
                    
                    if (noChange) {
                        return;
                    }
                    
                    SetClassNodeTextCommand command = new SetClassNodeTextCommand(
                            canvas,
                            ConcreteClassNodePane.this,
                            this,
                            initialText,
                            new Point2D(initialWidth, initialHeight),
                            newText,
                            new Point2D(newWidth, newHeight)
                    );
                    
                    getCanvas().getMainScene().executeCommand(command);
                }
            });
        }
        
        private String promptTextBeforeExport = null;

        @Override
        public void onPreImageExport() {
            promptTextBeforeExport = getPromptText();
            setPromptText("");
        }

        @Override
        public void onPostImageExport() {
            if (promptTextBeforeExport == null) {
                return;
            }
            
            setPromptText(promptTextBeforeExport);
        }
    }
    
    /**
     * Extension of text area. Saves initial state of the node on focus.
     * Compares current state to the initial state on focus loss. Executes command
     * if current state differs from the initial state.
     */
    class ClassNodeTextArea extends TextArea implements ImageProcessed, BlockUndoOnFocus {
        
        @Override
        public boolean doNotConsumeOnFocus() {
            return true;
        }
        
        public ClassNodeTextArea() {
            super();
            addListeners();
        }
        
        private double initialWidth;
        private double initialHeight;
        private String initialText;
        
        private void addListeners() {
            focusedProperty().addListener((ov, oldVal, newVal) -> {
                if (newVal) {
                    initialWidth = getNodeWidth();
                    initialHeight = getNodeHeight();
                    initialText = getText();
                } else {
                    double newWidth = getNodeWidth();
                    double newHeight = getNodeHeight();
                    String newText = getText();
                    
                    boolean noChange = newWidth == initialWidth
                            && newHeight == initialHeight
                            && newText.equals(initialText);
                    
                    if (noChange) {
                        return;
                    }
                    
                    SetClassNodeTextCommand command = new SetClassNodeTextCommand(
                            canvas,
                            ConcreteClassNodePane.this,
                            this,
                            initialText,
                            new Point2D(initialWidth, initialHeight),
                            newText,
                            new Point2D(newWidth, newHeight)
                    );
                    
                    getCanvas().getMainScene().executeCommand(command);
                }
            });
        }
        
        private String promptTextBeforeExport = null;

        @Override
        public void onPreImageExport() {
            promptTextBeforeExport = getPromptText();
            setPromptText("");
        }

        @Override
        public void onPostImageExport() {
            if (promptTextBeforeExport == null) {
                return;
            }
            
            setPromptText(promptTextBeforeExport);
        }
    }
    
    @Override
    public Command createDeleteCommand() {
        return new DeleteClassNodeCommand(this);
    }
    
    private static final double MIN_NODE_WIDTH = 100;
    private static final double MIN_NODE_HEIGHT = 105;
    
    private final ClassNode model;
    
    private final VBox container;
    private final Pane selectionPane;
    
    private final ClassTypeNode classType;
    private final ClassNodeTextField className;
    private final ClassNodeTextArea attributes;
    private final ClassNodeTextArea operations;
    
    private ResizePane resizePane;
    
    private CreateArrowPane arrowPane;
    
    private DiagramCanvasPane canvas;
    
    
    
    @Override
    public ClassNode getModel() {
        return model;
    }
    
    /**
     * Create a controller for class node view
     * @param canvas the diagram canvas the node is created in
     * @pre {canvas != null}
     * @throws IllegalArgumentException if preconditions are violated
     */
    public ConcreteClassNodePane(DiagramCanvasPane canvas, ClassNode model) {
        super();
        
        if (canvas == null) {
            throw new IllegalArgumentException("canvas cannot be null");
        }
        
        if (model == null) {
            throw new IllegalArgumentException("model cannot be null");
        }
        
        this.canvas = canvas;
        this.model = model;
        
        // Setup the root pane
        setPrefWidth(model.getWidth());
        setPrefHeight(model.getHeight());
        setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        setMinWidth(USE_PREF_SIZE);
        setMinHeight(USE_PREF_SIZE);
        setMaxWidth(USE_PREF_SIZE);
        setMaxHeight(USE_PREF_SIZE);
        
        container = new VBox();
        getChildren().add(container);
        VBox.setMargin(container, new Insets(5.0));
        StackPane.setAlignment(container, Pos.TOP_CENTER);
        container.setAlignment(Pos.TOP_CENTER);
        container.setOnKeyPressed((e) -> onKeyPress(e));
        
        classType = new ClassTypeNode();
        container.getChildren().add(classType);
        VBox.setVgrow(classType, Priority.NEVER);
        
        classType.minHeightProperty().addListener((ov, oldVal, newVal)
                -> ensureSize(MIN_NODE_WIDTH, getMinimumHeight()));
        
        className = new ClassNodeTextField();
        container.getChildren().add(className);
        VBox.setVgrow(className, Priority.NEVER);
        className.setPromptText("Class Name");
        className.setStyle("-fx-background-color: transparent;");
        className.setFocusTraversable(false);
        className.setFont(Font.font(null, FontWeight.BOLD, 12.0));
        className.setAlignment(Pos.CENTER);
        className.setMinHeight(25.0);
        className.setMaxWidth(Double.MAX_VALUE);
        className.setMaxHeight(USE_PREF_SIZE);
        
        container.getChildren().add(createHorizontalSeparator());
        
        attributes = new ClassNodeTextArea();
        container.getChildren().add(attributes);
        VBox.setVgrow(attributes, Priority.ALWAYS);
        attributes.setPromptText("Attributes");
        attributes.setStyle("-fx-background-color: transparent;");
        attributes.setFocusTraversable(false);
        attributes.setPrefHeight(25.0);
        attributes.setMinHeight(USE_PREF_SIZE);
        attributes.setMaxWidth(Double.MAX_VALUE);
        attributes.setMaxHeight(USE_PREF_SIZE);
        
        container.getChildren().add(createHorizontalSeparator());
        
        operations = new ClassNodeTextArea();
        container.getChildren().add(operations);
        VBox.setVgrow(operations, Priority.ALWAYS);
        operations.setPromptText("Operations");
        operations.setStyle("-fx-background-color: transparent;");
        operations.setFocusTraversable(false);
        operations.setPrefHeight(25.0);
        operations.setMinHeight(USE_PREF_SIZE);
        operations.setMaxWidth(Double.MAX_VALUE);
        operations.setMaxHeight(Double.MAX_VALUE);
        
        // Black border overlay
        Pane overlay = new Pane();
        getChildren().add(overlay);
        overlay.setMouseTransparent(true);
        overlay.prefWidthProperty().bind(super.prefWidthProperty());
        overlay.prefHeightProperty().bind(super.prefHeightProperty());
        overlay.setBorder(new Border(new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(2)
        )));
        
        // Invisible selection pane
        selectionPane = new Pane();
        getChildren().add(selectionPane);
        selectionPane.prefWidthProperty().bind(super.widthProperty());
        selectionPane.prefHeightProperty().bind(super.heightProperty());
        
        selectionPane.setOnMouseClicked((e) -> onSelectionPaneClick(e));
        selectionPane.setOnMousePressed((e) -> onSelectionPanePress(e));
        selectionPane.setOnMouseDragged((e) -> onSelectionPaneDrag(e));
        selectionPane.setOnMouseReleased((e) -> onSelectionPaneRelease(e));
        
        addFocusListeners();
        createResizePane();
        createArrowPane();
        
        // Get data from model
        setWidth(model.getWidth());
        setHeight(model.getHeight());
        setLayoutX(model.getLayoutX());
        setLayoutY(model.getLayoutY());
        className.setText(model.getClassName());
        classType.setClassType(model.getClassType());
        attributes.setText(model.getAttributesFlat());
        operations.setText(model.getOperationsFlat());
        
        // Bind view to model
        prefWidthProperty().addListener((ov, oldVal, newVal) -> model.setWidth((double) newVal));
        prefHeightProperty().addListener((ov, oldVal, newVal) -> model.setHeight((double) newVal));
        layoutXProperty().addListener((ov, oldVal, newVal) -> model.setLayoutX((double) newVal));
        layoutYProperty().addListener((ov, oldVal, newVal) -> model.setLayoutY((double) newVal));
        className.textProperty().addListener((ov, oldVal, newVal) -> model.setClassName(newVal));
        classType.classTypeProperty().addListener(
                (ov, oldVal, newVal) -> model.setClassType(newVal));
        
        attributes.textProperty().addListener((ov, oldVal, newVal) -> {
            model.setAttributes(Arrays.asList(newVal.split("\\r?\\n")));
        });
        operations.textProperty().addListener((ov, oldVal, newVal) -> {
            model.setOperations(Arrays.asList(newVal.split("\\r?\\n")));
        });
        
        // Setup text fields later
        setVisible(false);
        Platform.runLater(() -> {
            className.applyCss();
            attributes.applyCss();
            operations.applyCss();
            
            bindTextFieldSizeToContent(className);
            bindTextFieldSizeToContent(attributes);
            bindTextFieldSizeToContent(operations);
            
            disableScrollBars(attributes);
            disableScrollBars(operations);
            
            setVisible(true);
        });
    }
    
    private Pane createHorizontalSeparator() {
        Pane separator = new Pane();
        VBox.setVgrow(separator, Priority.NEVER);
        
        separator.setPrefHeight(2.0);
        separator.setMinHeight(USE_PREF_SIZE);
        separator.setMaxWidth(Double.MAX_VALUE);
        separator.setBackground(new Background(
                new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        
        return separator;
    }
    
    @Override
    public Region getRootRegion() {
        return this;
    }
    
    @Override
    public DiagramCanvasPane getCanvas() {
        return canvas;
    }
    
    /**
     * Returns the minimum width the node must take so that all the text fits the node.
     * @return minimum width based on the text bounds
     */
    public double getMinimumWidth() {
        double width = className.getMinWidth();
        width = Math.max(width, attributes.getMinWidth());
        width = Math.max(width, operations.getMinWidth());
        
        return Math.max(MIN_NODE_WIDTH, width);
    }
    
    /**
     * Returns the minimum height the node must take so that all the text fits the node.
     * @return minimum height based on the text bounds
     */
    public double getMinimumHeight() {
        double height = 0;
        for (Node n : container.getChildren()) {
            height += ((Region) n).minHeight(Double.MAX_VALUE);
        }
        
        return Math.max(MIN_NODE_HEIGHT, height);
    }
    
    /**
     * Ensures that the node's size is equal to or grater than the given bounds
     * @param width minimum width of the node
     * @param height minimum height of the node
     */
    private void ensureSize(double width, double height) {
        setNodeWidth(Math.max(width, getNodeWidth()));
        setNodeHeight(Math.max(height, getNodeHeight()));
    }
    
    private static Node getTextNode(TextInputControl field) {
        if (field == null) {
            return null;
        }
        
        if (field instanceof TextField) {
            return field.lookup(".text");
        }
        
        for (Node node : field.lookupAll(".text")) {
            if (node.getParent() instanceof Group) {
                return node;
            }
        }
        
        return null;
    }
    
    /**
     * Adds required listeners to the text field so that node expands if there is not
     * enough space for the text.
     * @param field text field to which the listeners will be attached
     */
    private void bindTextFieldSizeToContent(TextInputControl field) {
        Text content = (Text) getTextNode(field);
        
        if (content == null) {
            return;
        }
        
        Text dummyText = new Text();
        dummyText.textProperty().bind(field.textProperty());
        dummyText.fontProperty().bind(field.fontProperty());
        
        field.minHeightProperty().bind(Bindings.createDoubleBinding(() -> {
            return Math.max(16, content.getBoundsInLocal().getHeight() + 8);
        }, content.boundsInLocalProperty()));
        
        field.minHeightProperty().addListener((ov, oldVal, newVal) -> {
            ensureSize(MIN_NODE_WIDTH, getMinimumHeight());
        });
        
        field.minWidthProperty().bind(Bindings.createDoubleBinding(() -> {
            return dummyText.getBoundsInLocal().getWidth() + 25;
        }, dummyText.boundsInLocalProperty()));
        
        field.minWidthProperty().addListener((ov, oldVal, newVal) -> {
            ensureSize((double) newVal, MIN_NODE_HEIGHT);
        });
    }
    
    private static void disableScrollBars(TextArea field) {
        ScrollPane scrollPane = (ScrollPane) field.lookup(".scroll-pane");
        if (scrollPane != null) {
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        }
        field.setWrapText(false);
    }
    
    private void createResizePane() {
        resizePane = new ResizePane(this, canvas.getRootPanel());
        getChildren().add(resizePane);
        
        resizePane.setMinResizeWidth(getMinimumWidth());
        resizePane.setMinResizeHeight(getMinimumHeight());
        resizePane.addResizeListener((ResizeEvent e) -> {
            double width = e.width();
            if (e.resizeDirection().getHpos() == HPos.LEFT) {
                width = Math.min(width, getX() + getNodeWidth());
            } else if (e.resizeDirection().getHpos() == HPos.RIGHT) {
                width = Math.min(width, getCanvas().getCanvasWidth() - getX());
            }
            
            double height = e.height();
            if (e.resizeDirection().getVpos() == VPos.TOP) {
                height = Math.min(height, getY() + getNodeHeight());
            } else {
                height = Math.min(height, getCanvas().getCanvasHeight() - getY());
            }
            
            canvas.getMainScene().executeCommand(new ResizeClassNodeCommand(
                    this,
                    new Point2D(width, height),
                    e.resizeDirection()
            ));
        });
        
        resizePane.setEnabled(false);
        selectionPane.toFront();
    }
    
    private void createArrowPane() {
        arrowPane = new CreateArrowPane(this);
        getChildren().add(arrowPane);
        
        arrowPane.setEnabled(false);
        selectionPane.toFront();
    }
    
    /**
     * Returns true if any three of the text fields are focused.
     */
    private boolean anyTextFieldFocused() {
        return className.isFocused()
                || attributes.isFocused()
                || operations.isFocused();
    }
    
    /**
     * Called when a text field's focus changes. Used to re-enable the selection
     * blocker if all text fields lost focus.
     * @param focused new state of the focus
     */
    private void onTextFieldFocusChange(boolean focused) {
        if (focused || anyTextFieldFocused()) {
            return;
        }
        
        resizePane.setMinResizeWidth(getMinimumWidth());
        resizePane.setMinResizeHeight(getMinimumHeight());
        selectionPane.setDisable(false);
    }
    
    private void addFocusListeners() {
        // Disable resize/arrow pane when root pane focus is lost
        container.focusedProperty().addListener((focused, oldValue, newValue) -> {
            resizePane.setEnabled(newValue);
            arrowPane.setEnabled(newValue);
        });
        
        // Re-enable selection blocker when all text field focus are lost
        className.focusedProperty().addListener((focused, oldValue, newValue) -> {
            onTextFieldFocusChange(newValue);
        });
        
        attributes.focusedProperty().addListener((focused, oldValue, newValue) -> {
            onTextFieldFocusChange(newValue);
        });
        
        operations.focusedProperty().addListener((focused, oldValue, newValue) -> {
            onTextFieldFocusChange(newValue);
        });
    }
    
    /**
     * Called when user double clicks the node, requesting to edit a text field. The
     * text field under the cursor is then focused and the selection blocker is disabled.
     * @param localX mouse x position relative to container bounds
     * @param localY mouse y position relative to container bounds
     */
    private void focusTextField(double localX, double localY) {
        Node clickedNode = null;
        for (Node child : container.getChildren()) {
            if (!child.getBoundsInParent().contains(localX, localY)) {
                continue;
            }
            
            clickedNode = child;
            break;
        }
        
        if (clickedNode == null) {
            return;
        }
        
        if (clickedNode instanceof TextInputControl) {
            clickedNode.requestFocus();
            selectionPane.setDisable(true);
        }
    }
    
    private void onSelectionPaneClick(MouseEvent e) {
        e.consume();
        
        if (!e.isStillSincePress()) {
            return;
        }
        
        if (e.getButton() != MouseButton.PRIMARY) {
            return;
        }
        
        if (e.getClickCount() == 1) {
            if (!container.isFocused()) {
                container.requestFocus();
            } else {
                focusTextField(e.getX(), e.getY());
            }
        } else if (e.getClickCount() == 2) {
            focusTextField(e.getX(), e.getY());
        }
    }
    
    private double dragInitialNodeX;
    private double dragInitialNodeY;
    private double dragInitialMouseX;
    private double dragInitialMouseY;
    
    private void onSelectionPanePress(MouseEvent e) {
        if (!container.isFocused()) {
            return;
        }
        
        if (e.getButton() != MouseButton.PRIMARY) {
            return;
        }
        
        e.consume();
        
        dragInitialNodeX = getLayoutX();
        dragInitialNodeY = getLayoutY();
        
        Point2D canvasPos = canvas.getRootPanel().sceneToLocal(e.getSceneX(), e.getSceneY());
        dragInitialMouseX = canvasPos.getX();
        dragInitialMouseY = canvasPos.getY();
    }
    
    private void onSelectionPaneDrag(MouseEvent e) {
        if (!container.isFocused()) {
            return;
        }
        
        if (e.getButton() != MouseButton.PRIMARY) {
            return;
        }
        
        e.consume();
        
        selectionPane.setCursor(Cursor.CLOSED_HAND);
        getScene().setCursor(Cursor.NW_RESIZE);
        
        Point2D canvasPos = canvas.getRootPanel().sceneToLocal(e.getSceneX(), e.getSceneY());
        double deltaX = canvasPos.getX() - dragInitialMouseX;
        double deltaY = canvasPos.getY() - dragInitialMouseY;
        
        moveNode(dragInitialNodeX + deltaX, dragInitialNodeY + deltaY);
    }
    
    private void onSelectionPaneRelease(MouseEvent e) {
        if (!container.isFocused()) {
            return;
        }
        
        if (e.getButton() != MouseButton.PRIMARY) {
            return;
        }
        
        e.consume();
        
        if (e.isStillSincePress()) {
            return;
        }
        
        selectionPane.setCursor(null);
        getScene().setCursor(Cursor.DEFAULT);
        
        Point2D canvasPos = canvas.getRootPanel().sceneToLocal(e.getSceneX(), e.getSceneY());
        double deltaX = canvasPos.getX() - dragInitialMouseX;
        double deltaY = canvasPos.getY() - dragInitialMouseY;
        
        getCanvas().getMainScene().executeCommand(new MoveClassNodeCommand(
                this,
                dragInitialNodeX,
                dragInitialNodeY,
                dragInitialNodeX + deltaX,
                dragInitialNodeY + deltaY));
    }
    
    private void onKeyPress(KeyEvent e) {
        switch (e.getCode()) {
            case KeyCode.DELETE:
                canvas.getMainScene().executeCommand(createDeleteCommand());
                break;
        }
    }

    @Override
    public void moveNode(double targetX, double targetY) {
        if (Double.isNaN(targetX)) {
            throw new IllegalArgumentException("targetX cannot be NaN");
        }
        
        if (Double.isNaN(targetY)) {
            throw new IllegalArgumentException("targetY cannot be NaN");
        }
        
        Parent parent = getParent();
        double parentWidth = parent.getLayoutBounds().getWidth();
        double parentHeight = parent.getLayoutBounds().getHeight();
        
        double xLimit = Math.max(0, parentWidth - getPrefWidth());
        double yLimit = Math.max(0, parentHeight - getPrefHeight());
        
        targetX = Math.clamp(targetX, 0, xLimit);
        targetY = Math.clamp(targetY, 0, yLimit);
        
        setLayoutX(targetX);
        setLayoutY(targetY);
    }
    
    @Override
    public double getX() {
        return getLayoutX();
    }
    
    @Override
    public void setX(double x) {
        moveNode(x, getY());
    }
    
    @Override
    public double getY() {
        return getLayoutY();
    }
    
    @Override
    public void setY(double y) {
        moveNode(getX(), y);
    }
    
    @Override
    public double getNodeWidth() {
        return getPrefWidth();
    }
    
    @Override
    public void setNodeWidth(double width) {
        if (Double.isNaN(width)) {
            throw new IllegalArgumentException("width cannot be null");
        }
        
        setPrefWidth(Math.max(getMinimumWidth(), width));
    }
    
    @Override
    public double getNodeHeight() {
        return getPrefHeight();
    }

    @Override
    public void setNodeHeight(double height) {
        if (Double.isNaN(height)) {
            throw new IllegalArgumentException("height cannot be null");
        }
        
        setPrefHeight(Math.max(getMinimumHeight(), height));
    }

    @Override
    public String getClassNameText() {
        return className.getText();
    }

    @Override
    public String getAttributesText() {
        return attributes.getText();
    }

    @Override
    public String getOperationsText() {
        return operations.getText();
    }

    @Override
    public void setClassNameText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }
        
        this.className.setText(text);
    }

    @Override
    public void setAttributesText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }
        
        this.attributes.setText(text);
    }

    @Override
    public void setOperationsText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }
        
        this.operations.setText(text);
    }

    // Properties
    
    @Override
    public ClassType getClassType() {
        return classType.getClassType();
    }
    
    @Override
    public void setClassType(ClassType newClassType) {
        if (newClassType == null) {
            throw new IllegalArgumentException("newClassType cannot be null");
        }
        
        classType.setClassType(newClassType);
    }
    
    @Override
    public ObjectProperty<ClassType> classTypeProperty() {
        return classType.classTypeProperty();
    }

    /**
     * Sets the initial position of the node without clamping it to the parent's bounds.
     * This is used specifically for loading a diagram to ensure nodes are placed
     * at their saved coordinates, even before the canvas is fully rendered.
     * @param x The absolute X coordinate for the node.
     * @param y The absolute Y coordinate for the node.
     */
    public void setInitialPosition(double x, double y) {
        setLayoutX(x);
        setLayoutY(y);
    }
}
