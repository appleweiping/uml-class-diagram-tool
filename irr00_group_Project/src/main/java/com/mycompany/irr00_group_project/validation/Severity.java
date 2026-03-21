package com.mycompany.irr00_group_project.validation;

/**
 * Defines the severity levels for validation messages.
 */
public enum Severity {
    /**
     * For informational messages that don't prevent an action.
     */
    INFORMATION,

    /**
     * For warnings that might indicate a problem but don't stop the process.
     */
    WARNING,

    /**
     * For critical errors that invalidate the operation.
     */
    ERROR
}