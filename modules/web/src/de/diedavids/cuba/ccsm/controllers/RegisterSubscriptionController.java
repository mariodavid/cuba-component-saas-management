package de.diedavids.cuba.ccsm.controllers;


import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.TrustedClientService;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import de.diedavids.cuba.ccsm.CreateCustomerWithSubscriptionRequest;
import de.diedavids.cuba.ccsm.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.function.Supplier;


@RestController
@RequestMapping("/subscriptions")
public class RegisterSubscriptionController {

    private static final Logger log = LoggerFactory.getLogger(RegisterSubscriptionController.class);

    @Inject
    protected TrustedClientService trustedClientService;
    @Inject
    protected SubscriptionService subscriptionService;
    @Inject
    protected Configuration configuration;


    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity registerSubscriptionViaJSON(
            @RequestBody CreateCustomerWithSubscriptionRequest request
    ) {

        return runAuthenticated(() -> {
            subscriptionService.createCustomerWithSubscription(request);
            return ResponseEntity.ok().build();
        });
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public  ResponseEntity registerSubscriptionViaForm(
            CreateCustomerWithSubscriptionRequest request
    ) {

        return runAuthenticated(() -> {
            subscriptionService.createCustomerWithSubscription(request);
            return ResponseEntity.ok().build();
        });
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
