package com.mycompany.irr00_group_project.gui;

import com.mycompany.irr00_group_project.Undo.Command;
import com.mycompany.irr00_group_project.Undo.UndoRedoManager;
import com.mycompany.irr00_group_project.listeners.Observer;
import com.mycompany.irr00_group_project.representation.ClassNode;
import com.mycompany.irr00_group_project.representation.DiagramData;
import com.mycompany.irr00_group_project.representation.UMLConnection;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.*;

/**
 * Main scene contains root objects such as menu bars, diagram scroll pane and toolbars.
 * 
 * @author Deniz Büyükgüral
 * @author Aiham Al-Ashwal
 */
public class MainScene extends GridPane implements Observer<UndoRedoManager> {

    private final Stage primaryStage;

    private final ToolBar toolBar;
    private final ValidationPane validationPane;
    private final BottomBar bottombar;
    
    private final MenuBar menuBar;
    private final MenuItem undoItem;
    private final MenuItem redoItem;
    private final MenuItem deleteMenuItem;
    private final MenuItem saveFile;
    private final MenuItem openFile;
    private final MenuItem exportImageItem;
    private final MenuItem closeItem;
    
    private final ScrollPane rootPane;
    private final Group rootPaneContent;
    private boolean hasUnsavedChanges = false;
    
    private DiagramCanvasPane canvas;
    
    private UndoRedoManager undoManager = new UndoRedoManager();
    
    public MainScene(Stage primaryStage) {
        super();
        
        this.primaryStage = primaryStage;
        getRowConstraints().addAll(
                new RowConstraints(USE_PREF_SIZE, 25.0, USE_PREF_SIZE, Priority.NEVER, null, true),
                new RowConstraints(USE_PREF_SIZE, 50.0, USE_PREF_SIZE, Priority.NEVER, null, true),
                new RowConstraints(10.0, 300.0, Double.MAX_VALUE, Priority.ALWAYS, null, true),
                new RowConstraints(0.0, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.NEVER, null, true),
                new RowConstraints(USE_PREF_SIZE, 20.0, USE_PREF_SIZE, Priority.NEVER, null, false)
        );
        
        getColumnConstraints().addAll(
                new ColumnConstraints(100, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, Priority.SOMETIMES, null, true)
        );
        
        // Create viewport
        rootPane = new ScrollPane();
        add(rootPane, 0, 2);
        rootPane.setBackground(new Background(new BackgroundFill(Color.web("#F0F5F9"), CornerRadii.EMPTY, Insets.EMPTY)));
        
        // Create viewport content
        rootPaneContent = new Group();
        rootPane.setContent(rootPaneContent);
        
        canvas = new ConcreteDiagramCanvasPane(this);
        rootPaneContent.getChildren().add(canvas.getRoot());
        
        toolBar = new ToolBar(this);
        add(toolBar, 0, 1);
        
        // Create validation pane
        ConcreteValidationPane concreteValidationPane = new ConcreteValidationPane(this);
        validationPane = concreteValidationPane;
        add(concreteValidationPane, 0, 3);
        
        // Create bottom bar
        bottombar = new BottomBar(this);
        add(bottombar, 0, 4);
        
        // Create menu bar
        menuBar = new MenuBar();
        add(menuBar, 0, 0);
        
        undoItem = new MenuItem("Undo");
        undoItem.setDisable(true);
        undoItem.setOnAction((e) -> undo());
        redoItem = new MenuItem("Redo");
        redoItem.setDisable(true);
        redoItem.setOnAction((e) -> redo());
        deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction((e) -> onDelete());
        deleteMenuItem.setDisable(true);
        saveFile = new MenuItem("Save file");
        openFile = new MenuItem("Open file");
        exportImageItem = new MenuItem("Export as Image...");
        closeItem = new MenuItem("Close");

        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        editMenu.getItems().addAll(undoItem, redoItem, new SeparatorMenuItem(), deleteMenuItem);
        fileMenu.getItems().addAll(openFile, saveFile, new SeparatorMenuItem(), exportImageItem,new SeparatorMenuItem(), closeItem);
        menuBar.getMenus().addAll(fileMenu, editMenu);
        
        addFocusChangeListener();
        undoManager.registerObserver(this);
    }
    
    public void executeCommand(Command command) {
        undoManager.executeCommand(command);
        this.hasUnsavedChanges = true;
        updateWindowTitle();
    }

    public void undo() {
        undoManager.undo();
    }
    
    public void redo() {
        undoManager.redo();
    }

    @Override
    public void update(UndoRedoManager subject) {
        undoItem.setDisable(subject.getUndoCount() <= 0);
        redoItem.setDisable(subject.getRedoCount() <= 0);
    }

    // Property bound to the current selected node which can be deleted
    private final ObjectProperty<Deletable> focusedDeletable
            = new SimpleObjectProperty<>(null);
    
    /**
     * Listen to focus changes. Check whether the focused object is a Deletable instance.
     */
    private void addFocusChangeListener() {
        
        Platform.runLater(() -> {
            getScene().focusOwnerProperty().addListener((ov, oldVal, newVal) -> {
                Node focusedNode = newVal;
                while (focusedNode != null) {
                    
                    if (focusedNode instanceof Deletable deletable) {
                        focusedDeletable.set(deletable);
                        return;
                    }
                    
                    focusedNode = focusedNode.getParent();
                }
                
                focusedDeletable.set(null);
            });
        });
        
        focusedDeletable.addListener((ov, oldVal, newVal) -> {
            deleteMenuItem.setDisable(newVal == null);
        });
    }
    
    private void onDelete() {
        Deletable deletable = focusedDeletable.get();
        if (deletable != null) {
            executeCommand(deletable.createDeleteCommand());
        }
    }

    public void setHasUnsavedChanges(boolean hasChanges) {
        this.hasUnsavedChanges = hasChanges;
        updateWindowTitle();
    }

    /**
     * Connects the menu actions to the provided controller.
     */
    public void setController(AppController controller) {
        saveFile.setOnAction(e -> controller.handleSaveAs());
        openFile.setOnAction(e -> controller.handleOpen());
        exportImageItem.setOnAction(e -> controller.handleExportAsImage());
        closeItem.setOnAction(e -> controller.handleClose());
    }

    /**
     * Clears the canvas and displays the state of the given DiagramData model.
     */
    public void displayDiagram(DiagramData data) {
        canvas.displayFromModel(data);
        undoManager.clearUndos();
        undoManager.clearRedos();
        
        updateWindowTitle();
    }
    
    /**
     * Get the scroll pane the diagram is in.
     * @return scroll pane the diagram is in
     */
    ScrollPane getRootPane() {
        return rootPane;
    }
    
    /**
     * Get the diagram canvas of the scene.
     * @return the diagram canvas of the scene
     */
    DiagramCanvasPane getCanvas() {
        return canvas;
    }
    
    /**
     * Get the validation pane of the scene.
     * @return validation pane of the scene
     */
    ValidationPane getValidationPane() {
        return validationPane;
    }
    public boolean hasUnsavedChanges() {
        return this.hasUnsavedChanges;
    }

    private void updateWindowTitle() {
        String title = "UML Class Diagram Editor";
        if (primaryStage.getTitle() != null && !primaryStage.getTitle().endsWith(" *") && !primaryStage.getTitle().equals(title)) {
            // If there's already a file name, use that as the base
            title = primaryStage.getTitle();
        }

        if (hasUnsavedChanges && !title.endsWith(" *")) {
            primaryStage.setTitle(title + " *");
        } else if (!hasUnsavedChanges && title.endsWith(" *")) {
            primaryStage.setTitle(title.substring(0, title.length() - 2));
        }
    }
}
