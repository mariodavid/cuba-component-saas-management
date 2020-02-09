package de.diedavids.cuba.ccsm.controllers;


import com.chargebee.models.Customer;
import com.chargebee.models.Event;
import com.chargebee.models.Subscription;
import com.chargebee.models.enums.EventType;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.TrustedClientService;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import de.diedavids.cuba.ccsm.ChangeSubscriptionRequest;
import de.diedavids.cuba.ccsm.CreateCustomerWithSubscriptionRequest;
import de.diedavids.cuba.ccsm.service.ReceviedEventService;
import de.diedavids.cuba.ccsm.service.SubscriptionService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.function.Supplier;


@RestController
@RequestMapping("/webhooks")
public class WebhookController {

    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);

    @Inject
    protected TrustedClientService trustedClientService;
    @Inject
    protected SubscriptionService subscriptionService;
    @Inject
    protected ReceviedEventService receviedEventService;
    @Inject
    protected Configuration configuration;


    @PostMapping(path = "/chargebee")
    public ResponseEntity chargebeeWebhook(
            @RequestBody String body,
            @RequestParam(name = "webhook_key") String webhookKey
    ) {

        if(!checkIfRequestIsFromChargeBee(webhookKey)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Event event = new Event(body);
        EventType eventType = event.eventType();

        return runAuthenticated(() -> {

            receviedEventService.storeEvent(
                    event.id(),
                    event.source().name(),
                    StringUtils.left(body, 4000),
                    eventType.toString(),
                    event.apiVersion().name()
            );

            if (eventType.equals(EventType.CUSTOMER_CREATED)) {
                CreateCustomerWithSubscriptionRequest request = convertToCustomerCreatedEvent(event);
                subscriptionService.createCustomerWithSubscription(request);
            }
            if (eventType.equals(EventType.SUBSCRIPTION_CHANGED)) {
                ChangeSubscriptionRequest request = convertToSubscriptionChangedEvent(event);
                subscriptionService.changeSubscription(request);
            }

            return ResponseEntity.ok(event.jsonObj.toString());
        });
    }

    private ChangeSubscriptionRequest convertToSubscriptionChangedEvent(Event event) {
        ChangeSubscriptionRequest request = new ChangeSubscriptionRequest();

        Subscription subscription = event.content().subscription();
        Customer customer = event.content().customer();

        request.setCustomerId(customer.id());
        request.setPlan(subscription.planId());

        return request;
    }


    private boolean checkIfRequestIsFromChargeBee(String webhookKey) {
        if(!"DEMO_KEY".equals(webhookKey)){
            return false;
        }
        return true;
    }


    private ResponseEntity runAuthenticated(Supplier<ResponseEntity> doIt) {
        String trustedClientPassword = configuration.getConfig(WebAuthConfig.class).getTrustedClientPassword();
        UserSession systemSession = trustedClientService.getSystemSession(trustedClientPassword);
        SecurityContext securityContext = new SecurityContext(systemSession);
        SecurityContext previousSecurityContext = AppContext.getSecurityContext();
        AppContext.setSecurityContext(securityContext);
        try {
            return doIt.get();
        }
        catch (Exception e) {
            log.error("Error while execution: " + e.getMessage(), e);
            throw e;
        }
        finally {
            AppContext.setSecurityContext(previousSecurityContext);
        }
    }

    private CreateCustomerWithSubscriptionRequest convertToCustomerCreatedEvent(Event event) {
        CreateCustomerWithSubscriptionRequest request = new CreateCustomerWithSubscriptionRequest();

        Customer customer = event.content().customer();
        request.setCustomerId(customer.id());
        request.setEmail(customer.email());
        request.setFirstName(customer.firstName());
        request.setName(customer.lastName());

        return request;
    }
}
