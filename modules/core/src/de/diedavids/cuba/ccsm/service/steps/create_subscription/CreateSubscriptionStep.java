package de.diedavids.cuba.ccsm.service.steps.create_subscription;

import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import de.diedavids.cuba.ccsm.entity.Plan;
import de.diedavids.cuba.ccsm.entity.Subscription;
import de.diedavids.cuba.ccsm.entity.SubscriptionStatus;
import de.diedavids.cuba.ccsm.service.steps.CommitStep;

import java.util.function.Supplier;


public class CreateSubscriptionStep implements CommitStep, Supplier<Plan> {
    private final DataManager dataManager;
    private final CreateCustomerStep createCustomerStep;
    private final String planId;
    private Plan plan;

    public CreateSubscriptionStep(DataManager dataManager, CreateCustomerStep createCustomerStep, String planId) {

        this.dataManager = dataManager;
        this.createCustomerStep = createCustomerStep;
        this.planId = planId;
    }

    @Override
    public void accept(CommitContext commitContext) {

        plan = loadPlanByExternalId(planId);

        Subscription subscription = dataManager.create(Subscription.class);
        subscription.setCustomer(createCustomerStep.getCustomer());
        subscription.setPlan(plan);
        subscription.setStatus(SubscriptionStatus.LIVE);
        commitContext.addInstanceToCommit(subscription);
    }

    private Plan loadPlanByExternalId(String planId) {
        return dataManager.load(Plan.class)
                .query("select e from ccsm_Plan e where e.externalId = :planCode")
                .parameter("planCode", planId)
                .view("plan-view")
                .one();
    }

    @Override
    public Plan get() {
        return plan;
    }
}
