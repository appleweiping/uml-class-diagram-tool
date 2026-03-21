package com.mycompany.irr00_group_project.validation;

/**
 * Defines a generic strategy for validating an item of a specific type.
 * Implementations of this interface encapsulate a specific validation algorithm.
 *
 * @param <T> The type of the item to be validated.
 * @author Anas mohammad Jebril Yousef Noufal
 */
public interface ValidationRule<T> {

    ValidationResult validate(T itemToValidate);
}