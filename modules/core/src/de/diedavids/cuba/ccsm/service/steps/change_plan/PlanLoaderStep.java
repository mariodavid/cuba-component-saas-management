package de.diedavids.cuba.ccsm.service.steps.change_plan;

import com.haulmont.cuba.core.global.DataManager;
import de.diedavids.cuba.ccsm.entity.Plan;
import de.diedavids.cuba.ccsm.service.steps.LoadStep;

public class PlanLoaderStep implements LoadStep<String, Plan> {
    private final DataManager dataManager;

    public PlanLoaderStep(
            DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Plan apply(String planId) {
        return loadPlanByExternalId(planId);
    }

    private Plan loadPlanByExternalId(String planId) {
        return dataManager.load(Plan.class)
                .query("select e from ccsm_Plan e where e.externalId = :planCode")
                .parameter("planCode", planId)
                .view("plan-view")
                .one();
    }
}
