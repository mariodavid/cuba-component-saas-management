package de.diedavids.cuba.ccsm.controllers.handler;

import com.chargebee.models.Customer;
import com.chargebee.models.Event;
import com.chargebee.models.Subscription;
import com.chargebee.models.enums.EventType;
import de.diedavids.cuba.ccsm.ChangeSubscriptionRequest;
import de.diedavids.cuba.ccsm.service.SubscriptionService;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

@Component("ccsm_SubscriptionChangedHandler")
public class SubscriptionChangedHandler implements WebhookEventHandler {

    @Inject
    protected SubscriptionService subscriptionService;

    @Override
    public boolean supports(EventType eventType) {
        return eventType.equals(EventType.SUBSCRIPTION_CHANGED);
    }

    @Override
    public boolean handle(Event event) {

        ChangeSubscriptionRequest request = convertToSubscriptionChangedEvent(event);
        subscriptionService.changeSubscription(request);

        return true;
    }


    private ChangeSubscriptionRequest convertToSubscriptionChangedEvent(Event event) {
        ChangeSubscriptionRequest request = new ChangeSubscriptionRequest();

        Subscription subscription = event.content().subscription();
        Customer customer = event.content().customer();

        request.setCustomerId(customer.id());
        request.setPlan(subscription.planId());

        return request;
    }
}
