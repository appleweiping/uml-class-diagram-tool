package com.mycompany.irr00_group_project.representation;

/**
 * Represents the type of connection between two classes in a UML diagram.
 *
 * The available connection types include:
 * - INHERITANCE: A relationship where one class inherits from another.
 * - ASSOCIATION: A relationship where two classes are associated with each other.
 * - AGGREGATION: A relationship where one class is a whole and another is a part,
 *   but with loose coupling.
 * - COMPOSITION: A relationship where one class is a whole and another is a part,
 *   with strong ownership and lifecycle dependency.
 */
public enum ConnectionType {
    INHERITANCE,
    ASSOCIATION,
    AGGREGATION,
    COMPOSITION
}