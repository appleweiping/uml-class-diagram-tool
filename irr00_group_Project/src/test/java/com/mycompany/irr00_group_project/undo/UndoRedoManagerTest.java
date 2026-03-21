package com.mycompany.irr00_group_project.undo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mycompany.irr00_group_project.Undo.Command;
import com.mycompany.irr00_group_project.Undo.UndoRedoManager;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UndoRedoManager.
 * Verifies command execution, undo/redo behavior, history limit, and observer notifications.
 * @auther Anas Mohammad Jebril Yousef Noufal
 * @author Weiping Yan
 */
public class UndoRedoManagerTest {

    private UndoRedoManager manager;
    private DummyCommand command1;
    private DummyCommand command2;

    /**
     * Instantiates necessary objects before each unit test.
     */
    @BeforeEach
    void setUp() {
        manager = new UndoRedoManager();
        command1 = new DummyCommand();
        command2 = new DummyCommand();
    }

    /**
     * Test that a command is executed and pushed onto the undo stack.
     */
    @Test
    void testExecuteCommandAddsToUndoStack() {
        manager.executeCommand(command1);
        assertTrue(command1.executed);
        assertEquals(1, manager.getUndoCount());
        assertEquals(0, manager.getRedoCount());
    }

    /**
     * Test that undo works properly and moves command to redo stack.
     */
    @Test
    void testUndoMovesCommandToRedoStack() {
        manager.executeCommand(command1);
        manager.undo();
        assertTrue(command1.undone);
        assertEquals(0, manager.getUndoCount());
        assertEquals(1, manager.getRedoCount());
    }

    /**
     * Test that redo re-executes a command and moves it back to undo stack.
     */
    @Test
    void testRedoMovesCommandToUndoStack() {
        manager.executeCommand(command1);
        manager.undo();
        manager.redo();
        assertEquals(1, manager.getUndoCount());
        assertEquals(0, manager.getRedoCount());
        assertEquals(2, command1.executeCallCount); // should have been executed twice
    }

    /**
     * Test undo and redo behavior when stacks are empty.
     * Should not throw any exceptions.
     */
    @Test
    void testUndoRedoOnEmptyStacks() {
        assertDoesNotThrow(() -> manager.undo());
        assertDoesNotThrow(() -> manager.redo());
    }

    /**
     * Test that history limit works: oldest command is dropped.
     */
    @Test
    void testUndoHistoryLimit() {
        UndoRedoManager limited = new UndoRedoManager(1);
        limited.executeCommand(command1);
        limited.executeCommand(command2);
        assertEquals(1, limited.getUndoCount());
        assertFalse(limited.getUndoCount() > 1);
    }
    
    /**
     * Test that the undo count is set to zero when undos are cleared.
     */
    @Test
    void testUndoClear() {
        manager.executeCommand(command1);
        manager.executeCommand(command2);
        assertEquals(manager.getUndoCount(), 2);
        
        manager.clearUndos();
        assertEquals(manager.getUndoCount(), 0);
    }
    
    /**
     * Test that the redo count is set to zero when redos are cleared.
     */
    @Test
    void testRedoClear() {
        manager.executeCommand(command1);
        manager.executeCommand(command2);
        assertEquals(manager.getUndoCount(), 2);
        
        manager.undo();
        manager.undo();
        assertEquals(manager.getUndoCount(), 0);
        assertEquals(manager.getRedoCount(), 2);
        
        manager.clearRedos();
        assertEquals(manager.getRedoCount(), 0);
    }

    // A dummy command implementation used for testing
    static class DummyCommand implements Command {
        boolean executed = false;
        boolean undone = false;
        int executeCallCount = 0;

        @Override
        public void execute() {
            executed = true;
            executeCallCount++;
        }

        @Override
        public void undo() {
            undone = true;
        }
    }
}
