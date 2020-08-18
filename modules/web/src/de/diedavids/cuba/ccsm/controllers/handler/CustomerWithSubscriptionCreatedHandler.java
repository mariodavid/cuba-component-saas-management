package de.diedavids.cuba.ccsm.controllers.handler;

import com.chargebee.models.Customer;
import com.chargebee.models.Event;
import com.chargebee.models.Subscription;
import com.chargebee.models.enums.EventType;
import de.diedavids.cuba.ccsm.CreateCustomerWithSubscriptionRequest;
import de.diedavids.cuba.ccsm.service.SubscriptionService;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

@Component("ccsm_CustomerWithSubscriptionCreatedHandler")
public class CustomerWithSubscriptionCreatedHandler implements WebhookEventHandler {

    @Inject
    protected SubscriptionService subscriptionService;

    @Override
    public boolean supports(EventType eventType) {
        return eventType.equals(EventType.SUBSCRIPTION_CREATED);
    }

    @Override
    public boolean handle(Event event) {

        CreateCustomerWithSubscriptionRequest request = convertToCreateCustomerWithSubscriptionRequest(event);
        subscriptionService.createCustomerWithSubscription(request);

        return true;
    }


    private CreateCustomerWithSubscriptionRequest convertToCreateCustomerWithSubscriptionRequest(Event event) {
        CreateCustomerWithSubscriptionRequest request = new CreateCustomerWithSubscriptionRequest();

        Customer customer = event.content().customer();
        request.setCustomerId(customer.id());
        request.setEmail(customer.email());
        request.setFirstName(customer.firstName());
        request.setName(customer.lastName());
        request.setOrganizationName(customer.firstName() + " " + customer.lastName());
        request.setOrganizationCode(customer.id());
        request.setPassword(customer.email());


        Subscription subscription = event.content().subscription();
        request.setPlan(subscription.planId());

        return request;
    }
}
