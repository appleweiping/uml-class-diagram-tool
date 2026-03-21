package com.mycompany.irr00_group_project.gui.commands;

import com.mycompany.irr00_group_project.Undo.Command;
import com.mycompany.irr00_group_project.gui.ArrowNodeUI;
import com.mycompany.irr00_group_project.gui.ClassNodePaneUI;
import com.mycompany.irr00_group_project.gui.DiagramCanvasPane;
import com.mycompany.irr00_group_project.gui.data.Anchor;
import com.mycompany.irr00_group_project.representation.ConnectionType;
import com.mycompany.irr00_group_project.representation.UMLConnection;
import javafx.geometry.Point2D;

/**
 * Command implementation for arrow creation between two nodes.
 * 
 * @author Deniz Büyükgüral
 * @author Aiham Al-Ashwal
 */
public class CreateArrowCommand implements Command {
    
    private final DiagramCanvasPane canvas;
    private final ClassNodePaneUI sourceNode;
    private final Anchor sourceAnchor;
    private final ClassNodePaneUI targetNode;
    private final Anchor targetAnchor;
    private final Point2D sourcePos;
    private final Point2D targetPos;
    private final Point2D midPos;

    private UMLConnection connectionData;
    private ArrowNodeUI arrowPane;

    public CreateArrowCommand(
            DiagramCanvasPane canvas,
            ClassNodePaneUI sourceNode,
            Anchor sourceAnchor,
            ClassNodePaneUI targetNode,
            Anchor targetAnchor,
            Point2D sourcePos,
            Point2D targetPos,
            Point2D midPos) {
        
        this.canvas = canvas;
        this.sourceNode = sourceNode;
        this.sourceAnchor = sourceAnchor;
        this.targetNode = targetNode;
        this.targetAnchor = targetAnchor;
        this.sourcePos = sourcePos;
        this.targetPos = targetPos;
        this.midPos = midPos;
    }

    @Override
    public void execute() {
        if (arrowPane != null) {
            canvas.addArrow(arrowPane);
            return;
        }

        connectionData = new UMLConnection(
                sourceNode.getModel(),
                targetNode.getModel(),
                sourceAnchor,
                targetAnchor);
        
        connectionData.setType(ConnectionType.ASSOCIATION);
        if (sourceAnchor.isVertical()) {
            connectionData.setSourceOffsetX(sourcePos.getX());
        } else {
            connectionData.setSourceOffsetY(sourcePos.getY());
        }
        if (targetAnchor.isVertical()) {
            connectionData.setTargetOffsetX(targetPos.getX());
        } else {
            connectionData.setTargetOffsetY(targetPos.getY());
        }
        if (sourceAnchor.isHorizontal() == targetAnchor.isHorizontal()) {
            if (sourceAnchor.isHorizontal()) {
                connectionData.setMidlineX(midPos.getX());
            } else {
                connectionData.setMidlineY(midPos.getY());
            }
        }
        
        arrowPane = canvas.createArrow(sourceNode, sourceAnchor, targetNode, targetAnchor);

        // Apply the visual geometry
        arrowPane.setSourceX(sourcePos.getX());
        arrowPane.setSourceY(sourcePos.getY());
        arrowPane.setTargetX(targetPos.getX());
        arrowPane.setTargetY(targetPos.getY());
        arrowPane.setMidlineX(midPos.getX());
        arrowPane.setMidlineY(midPos.getY());
    }

    @Override
    public void undo() {
        if (connectionData != null && arrowPane != null) {
            canvas.removeArrow(arrowPane);
        } else {
            throw new RuntimeException("attempted to undo arrow creation but arrow was never created");
        }
    }
}
