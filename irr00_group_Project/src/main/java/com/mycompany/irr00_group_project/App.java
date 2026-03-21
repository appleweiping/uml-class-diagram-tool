/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt
 * to change this license
 */

package com.mycompany.irr00_group_project;

import com.mycompany.irr00_group_project.gui.AppController;
import com.mycompany.irr00_group_project.gui.BlockUndoOnFocus;
import com.mycompany.irr00_group_project.gui.MainScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Entry point of the application.
 */
public class App extends Application {
    
    private Scene scene;
    private MainScene scenePane;
    
    private boolean mouseDown;
    
    @Override
    public void start(Stage stage) throws IOException {
        scenePane = new MainScene(stage);
        scene = new Scene(scenePane, 1280, 720);
        AppController controller = new AppController(stage, scenePane);
        scenePane.setController(controller);
        stage.setScene(scene);
        stage.setTitle("UML Class Diagram Editor");

        bindUndoListener(stage);
        
        // delegate closing to controller
        stage.setOnCloseRequest(event -> {
            event.consume();
            controller.handleClose();
        });
        stage.centerOnScreen();
        stage.show();
    }
    
    private void bindUndoListener(Stage stage) {
        stage.addEventFilter(KeyEvent.KEY_PRESSED, (e) -> {
            if (mouseDown) {
                return;
            }
            
            
            
            if (e.isControlDown() && e.getCode() == KeyCode.Z) {
                attemptUndo(e);
            } else if (e.isControlDown() && e.getCode() == KeyCode.Y) {
                attemptRedo(e);
            }
        });
        
        stage.addEventFilter(MouseEvent.MOUSE_PRESSED, (e) -> {
            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }
            
            mouseDown = true;
        });
        
        stage.addEventFilter(MouseEvent.MOUSE_RELEASED, (e) -> {
            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }
            
            mouseDown = false;
        });
    }
    
    private void attemptUndo(KeyEvent e) {
        Node node = scene.getFocusOwner();
        BlockUndoOnFocus blocker = getUndoBlocker(node);
        if (blocker != null) {
            if (!blocker.doNotConsumeOnFocus()) {
                e.consume();
                scenePane.requestFocus();
            }
            
            return;
        }

        scenePane.undo();
    }
    
    private void attemptRedo(KeyEvent e) {
        Node node = scene.getFocusOwner();
        BlockUndoOnFocus blocker = getUndoBlocker(node);
        if (blocker != null) {
            if (!blocker.doNotConsumeOnFocus()) {
                e.consume();
                scenePane.requestFocus();
            }
            
            return;
        }

        scenePane.redo();
    }
    
    /**
     * Returns the BlockUndoOnFocus in node or its parents.
     * @param node the node to check BlockUndoOnFocus for
     * @return BlockUndoOnFocus instance of node or its ancestors. null if none of them
     *         implement BlockUndoOnFocus.
     */
    private static BlockUndoOnFocus getUndoBlocker(Node node) {
        while (node != null) {
            
            if (node instanceof BlockUndoOnFocus blocker) {
                return blocker;
            }
            
            node = node.getParent();
        }
        
        return null;
    }

    public static void main(String[] args) {
        launch();
    }
}
