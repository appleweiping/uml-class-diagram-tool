package com.mycompany.irr00_group_project.Undo;

/**
 * Abstract declaration of a commands interface .
 * in order to use the  command design pattern
 * @author Anas Mohammad Jebril Yousef Noufal
 */
public interface Command {
    void execute();

    void undo();
    
}