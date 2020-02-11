package de.diedavids.cuba.ccsm.service.steps.create_subscription;

import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import de.diedavids.cuba.ccsm.CreateCustomerWithSubscriptionRequest;
import de.diedavids.cuba.ccsm.entity.Customer;
import de.diedavids.cuba.ccsm.service.steps.CommitStep;

public class CreateCustomerStep implements CommitStep {
    private final DataManager dataManager;
    private final CreateCustomerWithSubscriptionRequest request;
    private Customer customer;

    public CreateCustomerStep(DataManager dataManager, CreateCustomerWithSubscriptionRequest request) {

        this.dataManager = dataManager;
        this.request = request;
    }

    @Override
    public void accept(CommitContext commitContext) {

        customer = dataManager.create(Customer.class);

        customer.setExternalId(request.getCustomerId());
        customer.setName(request.getName());
        customer.setFirstName(request.getFirstName());

        commitContext.addInstanceToCommit(customer);

    }

    public Customer getCustomer() {
        return customer;
    }
}
