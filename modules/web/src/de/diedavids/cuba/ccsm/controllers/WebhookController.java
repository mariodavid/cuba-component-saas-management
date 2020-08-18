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
import de.diedavids.cuba.ccsm.controllers.handler.WebhookEventHandler;
import de.diedavids.cuba.ccsm.service.ReceviedEventService;
import de.diedavids.cuba.ccsm.service.SubscriptionService;
import java.util.List;
import java.util.Optional;
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

    @Inject
    protected List<WebhookEventHandler> eventHandlers;



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
                    body,
                    eventType.toString(),
                    event.apiVersion().name()
            );

            final Optional<WebhookEventHandler> eventHandler = eventHandlers.stream()
                .filter(handler -> handler.supports(eventType))
                .findFirst();

            final Boolean handlerResult = eventHandler
                .map(handler -> handler.handle(event))
                .orElse(false);

            if (handlerResult) {
                return ResponseEntity.ok(event.jsonObj.toString());
            }
            else {
                return ResponseEntity.unprocessableEntity().build();
            }
        });
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

}
