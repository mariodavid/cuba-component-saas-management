package de.diedavids.cuba.ccsm.service;

import com.haulmont.addon.sdbmt.config.TenantConfig;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.security.app.Authentication;
import de.diedavids.cuba.ccsm.ChangeSubscriptionRequest;
import de.diedavids.cuba.ccsm.CreateCustomerWithSubscriptionRequest;
import de.diedavids.cuba.ccsm.core.PlanRolesExchange;
import de.diedavids.cuba.ccsm.entity.Customer;
import de.diedavids.cuba.ccsm.service.steps.change_plan.ExchangePlanStep;
import de.diedavids.cuba.ccsm.service.steps.change_plan.SubscriptionAndUserLoaderStep;
import de.diedavids.cuba.ccsm.service.steps.create_subscription.CreateCustomerStep;
import de.diedavids.cuba.ccsm.service.steps.create_subscription.CreateSubscriptionStep;
import de.diedavids.cuba.ccsm.service.steps.create_subscription.CreateTenantStep;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service(SubscriptionService.NAME)
public class SubscriptionServiceBean implements SubscriptionService {

    @Inject
    protected DataManager dataManager;

    @Inject
    protected TenantConfig tenantConfig;

    @Inject
    protected PasswordEncryption passwordEncryption;
    @Inject
    protected Authentication authentication;

    @Inject
    protected PlanRolesExchange planRolesExchange;

    @Override
    public Customer createCustomerWithSubscription(
            CreateCustomerWithSubscriptionRequest request
    ) {

        return authentication.withSystemUser(() -> {

            CommitContext commitContext = new CommitContext();

            CreateCustomerStep createCustomerStep = new CreateCustomerStep(
                    dataManager,
                    request
            );
            createCustomerStep.accept(commitContext);

            CreateSubscriptionStep createSubscriptionStep = new CreateSubscriptionStep(
                    dataManager,
                    request,
                    createCustomerStep
            );
            createSubscriptionStep.accept(commitContext);

            CreateTenantStep createTenantStep = new CreateTenantStep(
                    dataManager,
                    request,
                    tenantConfig,
                    passwordEncryption,
                    createCustomerStep,
                    createSubscriptionStep
            );

            createTenantStep.accept(commitContext);


            dataManager.commit(commitContext);

            return createCustomerStep.getCustomer();
        });

    }

    @Override
    public void changeSubscription(ChangeSubscriptionRequest request) {

        CommitContext commitContext = new CommitContext();

        SubscriptionAndUserLoaderStep subscriptionAndUserLoader = new SubscriptionAndUserLoaderStep(
                dataManager,
                request
        );
        subscriptionAndUserLoader.accept(commitContext);

        ExchangePlanStep exchangePlanStep = new ExchangePlanStep(
                dataManager,
                planRolesExchange,
                subscriptionAndUserLoader,
                request
        );
        exchangePlanStep.accept(commitContext);

        dataManager.commit(commitContext);

    }


}