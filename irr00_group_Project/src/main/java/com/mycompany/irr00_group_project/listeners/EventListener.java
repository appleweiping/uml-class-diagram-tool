package com.mycompany.irr00_group_project.listeners;

/**
 * Abstract declaration of an event handler.
 * @param <T> type of object to observe
 * 
 * @author Deniz Büyükgüral
 */
@FunctionalInterface
public interface EventListener<T> {
    /**
     * Called when the registered event occurs.
     * @param obj data passed by the emitter
     */
    public void handle(T obj);
}
