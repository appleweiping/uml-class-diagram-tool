package com.mycompany.irr00_group_project.gui.commands;

import com.mycompany.irr00_group_project.Undo.Command;
import com.mycompany.irr00_group_project.gui.ArrowNodeUI;
import javafx.scene.control.TextInputControl;

/**
 * Command implementation for setting arrow text fields.
 *
 * @author Deniz Büyükgüral
 * @author Aiham Al-Ashwal
 */
public class SetArrowTextCommand implements Command {
    
    private final ArrowNodeUI arrowPane;
    private final TextInputControl textField;
    private final String oldText;
    private final String newText;

    public SetArrowTextCommand(ArrowNodeUI arrow, TextInputControl textField, String oldText, String newText) {
        this.arrowPane = arrow;
        this.textField = textField;
        this.oldText = oldText;
        this.newText = newText;
    }

    @Override
    public void execute() {
        textField.setText(newText);
    }

    @Override
    public void undo() {
        textField.setText(oldText);
    }
}