package com.mycompany.irr00_group_project.Undo;

import com.mycompany.irr00_group_project.listeners.Observer;
import com.mycompany.irr00_group_project.listeners.Subject;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * This manager utilizes two stacks,
 * one for undoing commands and one for redoing commands, to implement the Command design pattern.
 * It allows for a configurable limit on the undo history.
 * @author Anas Mohammad Jebril Yousef Noufal
 */

public class UndoRedoManager implements Subject<UndoRedoManager> {

    private Deque<Command> undoStack = new ArrayDeque<>();
    private Deque<Command> redoStack = new ArrayDeque<>();
    private final int limit;
    private final List<Observer<? super UndoRedoManager>> observers = new ArrayList<>();

    public UndoRedoManager() {
        this(Integer.MAX_VALUE);
    }
    /**
    * Constructs a manager with a specified limit for the undo history.
    *
    * @param limit The maximum number of commands in the undo history; must be positive.
    * @post this.limit == limit && this.undoStack.isEmpty() && this.redoStack.isEmpty()
    * @throws IllegalArgumentException if  limit is not positive.
    */

    public UndoRedoManager(int limit) {

        if (limit <= 0) {
            throw new IllegalArgumentException("History limit must be a positive integer.");
        }
        this.limit = limit;
    }
    /**
    * Executes the given command, adds it to the undo history, and clears the redo history.
    * The command's execute() method is called.
    *
    * @param command The command to execute.
    * @post this.undoStack.peek() == command && this.redoStack.isEmpty()
    */

    public void executeCommand(Command command) {

        command.execute();
        if (undoStack.size() >= limit) {
            undoStack.removeLast();
        }
        undoStack.push(command);
        redoStack.clear();
        notifyObservers();
    }
    /**
    * Undoes the most recently executed command.
    * If a command is undone, its  undo() method is called.
    * @post !\old(this.undoStack.isEmpty())
    *  implies (this.redoStack.peek() == \old(this.undoStack.peek()))
     */

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
            notifyObservers();
        }
    } 
    /**
    *  Redoes the most recently undone command.
    *  If a command is redone, its execute() method is called.
    *
    *  @post !\old(this.redoStack.isEmpty())
    * implies (this.undoStack.peek() == \old(this.redoStack.peek()))
    */

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
            notifyObservers();
        }
    }

    /**
     * Gets the number of commands available to undo.
     * @return the size of the undo stack.
     */
    public int getUndoCount() {
        return undoStack.size();
    }

    /**
     * Gets the number of commands available to redo.
     * @return the size of the redo stack.
     */
    public int getRedoCount() {
        return redoStack.size();
    }
    
    /**
     * Removes all commands on the undo stack. Notifies the observers.
     */
    public void clearUndos() {
        undoStack.clear();
        notifyObservers();
    }
    
    /**
     * Removes all commands on the redo stack. Notifies the observers.
     */
    public void clearRedos() {
        redoStack.clear();
        notifyObservers();
    }

    @Override
    public boolean registerObserver(Observer<? super UndoRedoManager> observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            return true;
        }
        return false;
    }

    @Override
    public boolean unregisterObserver(Observer<? super UndoRedoManager> observer) {
        return observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer<? super UndoRedoManager> observer : observers) {
            observer.update(this);
        }
    }
}