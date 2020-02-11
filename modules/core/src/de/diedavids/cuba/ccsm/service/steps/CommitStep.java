package de.diedavids.cuba.ccsm.service.steps;

import com.haulmont.cuba.core.global.CommitContext;

import java.util.function.Consumer;

public interface CommitStep extends Consumer<CommitContext> {

}
