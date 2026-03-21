package com.mycompany.irr00_group_project.gui.commands;

import com.mycompany.irr00_group_project.Undo.Command;
import com.mycompany.irr00_group_project.gui.ArrowNode;
import com.mycompany.irr00_group_project.gui.DiagramCanvasPane;
import com.mycompany.irr00_group_project.representation.ConnectionType;

/**
 * Command implementation for setting arrow head.
 *
 * @author Deniz Büyükgüral
 * @author Aiham Al-Ashwal
 */
public class SetArrowHeadCommand implements Command {

    private final DiagramCanvasPane canvas;
    private final ArrowNode arrow;
    private final ConnectionType initialConnectionType;
    private final ConnectionType targetConnectionType;

    public SetArrowHeadCommand(DiagramCanvasPane canvas, ArrowNode arrow, ConnectionType initialConnectionType, ConnectionType targetConnectionType) {
        this.canvas = canvas;
        this.arrow = arrow;
        this.initialConnectionType = initialConnectionType;
        this.targetConnectionType = targetConnectionType;
    }

    @Override
    public void execute() {
        arrow.setHeadType(targetConnectionType);
    }

    @Override
    public void undo() {
        arrow.setHeadType(initialConnectionType);
    }
}