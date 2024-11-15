package com.adobe;

import com.adobe.entities.Lead;

/**
 * A functional interface representing a selector for a specific field from a {@link Lead} object.
 * <p>
 * This interface defines a single abstract method {@link #select(Lead)} that can be implemented
 * to extract a field (such as email, ID, etc.) from a {@link Lead} object.
 * It is intended to be used with functional programming paradigms such as lambda expressions
 * or method references to dynamically select fields of {@link Lead} objects during processing.
 * <p>
 * The interface is annotated with {@link FunctionalInterface}, which ensures that it has exactly
 * one abstract method, making it eligible to be used in lambda expressions or method references.
 *
 * @see Lead
 */
@FunctionalInterface
public interface FieldSelector {

    /**
     * Selects a specific field from the given {@link Lead} object.
     *
     * @param lead the {@link Lead} object from which a field is to be selected
     * @return the selected field as a {@link String}
     */
    String select(Lead lead);
}
