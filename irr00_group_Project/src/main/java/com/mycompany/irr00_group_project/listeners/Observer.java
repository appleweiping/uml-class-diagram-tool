package com.mycompany.irr00_group_project.listeners;

/**
 * Generic observer declaration.
 * 
 * @param <T> type of the subject which will be pushed to the observer
 * 
 * @author Deniz Büyükgüral
 */
public interface Observer<T> {
    /**
     * Called by the subject when the subject has changed.
     * 
     * @param subject subject which notified the observer
     */
    public void update(T subject);
}
