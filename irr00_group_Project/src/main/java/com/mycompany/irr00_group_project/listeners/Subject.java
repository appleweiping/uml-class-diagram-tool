package com.mycompany.irr00_group_project.listeners;

/**
 * Declaration of subject which can notify observers.
 * 
 * @param <T> type of the subject which will be pushed to the observers.
 * 
 * @author Deniz Büyükgüral
 */
public interface Subject<T> {
    /**
     * Registers the given observer which will be updated when the subject changes.
     * 
     * @param observer observer to be registered
     * @return true if observer is not null and was not registered before. otherwise false.
     */
    public boolean registerObserver(Observer<? super T> observer);
    
    /**
     * Unregister the given observer which was added with {@code registerObserver(observer)}.
     * 
     * @param observer observer to be unregistered
     * @return true if observer is not null and was registered prior to the call. otherwise false.
     */
    public boolean unregisterObserver(Observer<? super T> observer);
    
    /**
     * Call the update method of all the registered observers. Push the subject reference
     * to each observer.
     */
    public void notifyObservers();
}
