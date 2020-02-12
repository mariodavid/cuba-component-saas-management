package de.diedavids.cuba.ccsm.service.steps.create_subscription;

import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import de.diedavids.cuba.ccsm.CreateCustomerWithSubscriptionRequest;
import de.diedavids.cuba.ccsm.entity.Plan;
import de.diedavids.cuba.ccsm.entity.Subscription;
import de.diedavids.cuba.ccsm.entity.SubscriptionStatus;
import de.diedavids.cuba.ccsm.service.steps.CommitStep;

import java.util.function.Supplier;


public class CreateSubscriptionStep implements CommitStep, Supplier<Plan> {
    private final DataManager dataManager;
    private final CreateCustomerWithSubscriptionRequest request;
    private final CreateCustomerStep createCustomerStep;
    private Plan plan;

    public CreateSubscriptionStep(
            DataManager dataManager,
            CreateCustomerWithSubscriptionRequest request,
            CreateCustomerStep createCustomerStep
    ) {

        this.dataManager = dataManager;
        this.request = request;
        this.createCustomerStep = createCustomerStep;
    }

    @Override
    public void accept(CommitContext commitContext) {

        plan = loadPlanByExternalId(request.getPlan());

        Subscription subscription = dataManager.create(Subscription.class);
        subscription.setCustomer(createCustomerStep.getCustomer());
        subscription.setPlan(plan);
        subscription.setStatus(SubscriptionStatus.LIVE);
        subscription.setExternalId(request.getSubscriptionId());
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
