package de.diedavids.cuba.ccsm.service.steps;

import com.haulmont.cuba.core.global.CommitContext;

import java.util.function.Consumer;

/**
 * defines a step that commits information to the DB
 */
public interface CommitStep extends Consumer<CommitContext> {

}
