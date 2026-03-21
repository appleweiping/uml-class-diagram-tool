package com.mycompany.irr00_group_project.gui.commands;

import com.mycompany.irr00_group_project.gui.data.Anchor;
import com.mycompany.irr00_group_project.gui.ClassNodePaneUI;
import com.mycompany.irr00_group_project.gui.ConcreteGuiFactory;
import com.mycompany.irr00_group_project.gui.DiagramCanvasPane;
import com.mycompany.irr00_group_project.gui.MainScene;
import com.mycompany.irr00_group_project.representation.DiagramData;
import javafx.application.Platform;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;

/**
 * Unit tests for CreateArrowCommand class.
 * Covers execution and undo behaviors.
 *
 * @author Weiping Yan
 */
public class CreateArrowCommandTest {
    
    private MainScene mainScene;
    private DiagramCanvasPane canvas;
    private DiagramData model;
    private ClassNodePaneUI sourceNode;
    private Anchor sourceAnchor;
    private ClassNodePaneUI targetNode;
    private Anchor targetAnchor;
    private Point2D sourcePos;
    private Point2D targetPos;
    private Point2D midPos;
    
    @BeforeAll
    static void startToolkit() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Startup called multiple times, ignore
        }
    }

    @BeforeEach
    void setup() {
        mainScene = ConcreteGuiFactory.createMainScene();
        canvas = ConcreteGuiFactory.createDiagramCanvas(mainScene);
        model = canvas.getModel();
        sourceNode = canvas.createClassNode(0, 0, false);
        targetNode = canvas.createClassNode(0, 0, false);
        sourceAnchor = Anchor.LEFT;
        targetAnchor = Anchor.RIGHT;

        sourcePos = new Point2D(10, 20);
        targetPos = new Point2D(100, 200);
        midPos = new Point2D(55, 110);
    }

    @Test
    void testExecuteAddsConnectionAndArrow() {
        CreateArrowCommand command = new CreateArrowCommand(
                canvas, sourceNode, sourceAnchor,
                targetNode, targetAnchor, sourcePos, targetPos, midPos
        );

        command.execute();

        assertEquals(1, model.getConnections().size(), "Connection should be added to the model");
    }

    @Test
    void testUndoRemovesConnectionAndArrow() {
        CreateArrowCommand command = new CreateArrowCommand(
                canvas, sourceNode, sourceAnchor,
                targetNode, targetAnchor, sourcePos, targetPos, midPos
        );

        command.execute();
        assertEquals(1, model.getConnections().size());

        command.undo();

        assertEquals(0, model.getConnections().size(), "Connection should be removed after undo");
    }

    @Test
    void testUndoThrowsIfNeverExecuted() {
        CreateArrowCommand command = new CreateArrowCommand(
                canvas, sourceNode, sourceAnchor,
                targetNode, targetAnchor, sourcePos, targetPos, midPos
        );

        Exception exception = assertThrows(RuntimeException.class, command::undo);
        assertTrue(exception.getMessage().contains("attempted to undo"), "Should throw RuntimeException");
    }
}
