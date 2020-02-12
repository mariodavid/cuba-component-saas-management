package de.diedavids.cuba.ccsm.service.steps.change_plan;

import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import de.diedavids.cuba.ccsm.entity.Plan;
import de.diedavids.cuba.ccsm.service.steps.CommitStep;

public class PlanLoaderStep implements CommitStep {
    private final DataManager dataManager;
    private final String planId;
    private Plan plan;

    public PlanLoaderStep(
            DataManager dataManager,
            String planId) {

        this.dataManager = dataManager;
        this.planId = planId;
    }

    @Override
    public void accept(CommitContext commitContext) {

        plan = loadPlanByExternalId(planId);
    }

    private Plan loadPlanByExternalId(String planId) {
        return dataManager.load(Plan.class)
                .query("select e from ccsm_Plan e where e.externalId = :planCode")
                .parameter("planCode", planId)
                .view("plan-view")
                .one();
    }

    public Plan getPlan() {
        return plan;
    }
}
