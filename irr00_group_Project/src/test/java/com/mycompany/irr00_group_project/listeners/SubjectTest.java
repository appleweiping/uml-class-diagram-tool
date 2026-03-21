package com.mycompany.irr00_group_project.listeners;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the Subject interface using a simple concrete implementation.
 * This test uses a DummySubject class that implements the Subject interface,
 * and a DummyObserver to track update calls.
 *
 * @author Weiping Yan
 */
public class SubjectTest {

    private DummySubject subject;
    private DummyObserver observer;

    /**
     * Instantiates necessary objects before each unit test.
     */
    @BeforeEach
    void setUp() {
        subject = new DummySubject();
        observer = new DummyObserver();
    }

    @Test
    void testRegisterObserver_successful() {
        // Should return true on first registration
        assertTrue(subject.registerObserver(observer));
    }

    @Test
    void testRegisterObserver_nullObserver() {
        // Should return false when null is passed
        assertFalse(subject.registerObserver(null));
    }

    @Test
    void testRegisterObserver_duplicate() {
        subject.registerObserver(observer);
        // Should return false on duplicate registration
        assertFalse(subject.registerObserver(observer));
    }

    @Test
    void testUnregisterObserver_successful() {
        subject.registerObserver(observer);
        // Should return true when observer is removed successfully
        assertTrue(subject.unregisterObserver(observer));
    }

    @Test
    void testUnregisterObserver_notRegistered() {
        // Removing an observer that wasn't registered should return false
        assertFalse(subject.unregisterObserver(observer));
    }

    @Test
    void testUnregisterObserver_null() {
        // Null input should return false
        assertFalse(subject.unregisterObserver(null));
    }

    @Test
    void testNotifyObservers_callsUpdate() {
        subject.registerObserver(observer);
        subject.notifyObservers();
        // After notify, observer should be marked as updated
        assertTrue(observer.wasUpdated);
    }

    /**
     * A simple implementation of {@code Subject<T>} for testing purposes.
     */
    private static class DummySubject implements Subject<DummySubject> {

        private final List<Observer<? super DummySubject>> observers = new ArrayList<>();

        @Override
        public boolean registerObserver(Observer<? super DummySubject> observer) {
            return observer != null && !observers.contains(observer) && observers.add(observer);
        }

        @Override
        public boolean unregisterObserver(Observer<? super DummySubject> observer) {
            return observer != null && observers.remove(observer);
        }

        @Override
        public void notifyObservers() {
            for (Observer<? super DummySubject> observer : observers) {
                observer.update(this);
            }
        }
    }

    /**
     * A mock observer that tracks whether update() was called.
     */
    private static class DummyObserver implements Observer<DummySubject> {
        boolean wasUpdated = false;

        @Override
        public void update(DummySubject subject) {
            wasUpdated = true;
        }
    }
}
