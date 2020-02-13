package de.diedavids.cuba.ccsm.service.steps.create_subscription;

import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import de.diedavids.cuba.ccsm.CreateCustomerWithSubscriptionRequest;
import de.diedavids.cuba.ccsm.entity.Plan;
import de.diedavids.cuba.ccsm.entity.Subscription;
import de.diedavids.cuba.ccsm.entity.SubscriptionStatus;
import de.diedavids.cuba.ccsm.service.steps.CommitStep;
import de.diedavids.cuba.ccsm.service.steps.change_plan.PlanLoaderStep;

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

        plan = new PlanLoaderStep(dataManager).apply(request.getPlan());

        Subscription subscription = dataManager.create(Subscription.class);
        subscription.setCustomer(createCustomerStep.getCustomer());
        subscription.setPlan(plan);
        subscription.setStatus(SubscriptionStatus.LIVE);
        subscription.setExternalId(request.getSubscriptionId());
        commitContext.addInstanceToCommit(subscription);
    }

    @Override
    public Plan get() {
        return plan;
    }
}
