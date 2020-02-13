package de.diedavids.cuba.ccsm.service.steps;

import java.util.function.Function;

/**
 * Defines a step that loads data from the database
 *
 * @param <T> the parameter to pass in for lookup operations
 * @param <R> the result of the load operation
 */
public interface LoadStep<T, R> extends Function<T, R> {

}
