package de.diedavids.cuba.ccsm.service;

import de.diedavids.cuba.ccsm.CreateCustomerWithSubscriptionRequest;
import de.diedavids.cuba.ccsm.entity.Customer;

public interface CustomerCreationService {
    String NAME = "ccsm_CustomerCreationService";

    Customer createCustomer(CreateCustomerWithSubscriptionRequest createCustomerWithSubscriptionRequest);
}