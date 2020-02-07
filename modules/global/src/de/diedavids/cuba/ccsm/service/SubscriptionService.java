package de.diedavids.cuba.ccsm.service;

import de.diedavids.cuba.ccsm.CreateCustomerWithSubscriptionRequest;
import de.diedavids.cuba.ccsm.entity.Customer;

public interface SubscriptionService {
    String NAME = "ccsm_SubscriptionService";

    Customer createCustomerWithSubscription(CreateCustomerWithSubscriptionRequest createCustomerWithSubscriptionRequest);
}