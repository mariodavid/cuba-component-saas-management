package de.diedavids.cuba.ccsm.service;

import com.haulmont.addon.sdbmt.config.TenantConfig;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.security.app.Authentication;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import de.diedavids.cuba.ccsm.ChangeSubscriptionRequest;
import de.diedavids.cuba.ccsm.CreateCustomerWithSubscriptionRequest;
import de.diedavids.cuba.ccsm.core.PlanRolesExchange;
import de.diedavids.cuba.ccsm.entity.Customer;
import de.diedavids.cuba.ccsm.entity.Plan;
import de.diedavids.cuba.ccsm.entity.Subscription;
import de.diedavids.cuba.ccsm.service.steps.create_subscription.CreateCustomerStep;
import de.diedavids.cuba.ccsm.service.steps.create_subscription.CreateSubscriptionStep;
import de.diedavids.cuba.ccsm.service.steps.create_subscription.CreateTenantStep;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

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
                    createCustomerStep,
                    request.getPlan()
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

    private Plan loadPlanByExternalId(String planId) {
        return dataManager.load(Plan.class)
                .query("select e from ccsm_Plan e where e.externalId = :planCode")
                .parameter("planCode", planId)
                .view("plan-view")
                .one();
    }

    @Override
    public void changeSubscription(ChangeSubscriptionRequest request) {

        CommitContext commitContext = new CommitContext();

        Customer customer = dataManager.load(Customer.class)
                .query("select e from ccsm_Customer e where e.externalId = :customerId")
                .parameter("customerId", request.getCustomerId())
                .view("customer-view")
                .one();

        Subscription subscription = customer.getSubscriptions().get(0);

        Plan oldSelectedPlan = loadPlanByExternalId(subscription.getPlan().getExternalId());
        Plan newSelectedPlan = loadPlanByExternalId(request.getPlan());

        subscription.setPlan(newSelectedPlan);

        commitContext.addInstanceToCommit(subscription);

        User user = dataManager.reload(customer.getTenant().getAdmin(), "user.edit");


        addNewRolesToUser(commitContext, oldSelectedPlan, newSelectedPlan, user);
        removeOldRolesForUser(commitContext, oldSelectedPlan, newSelectedPlan, user);


        dataManager.commit(commitContext);

    }

    private void removeOldRolesForUser(CommitContext commitContext, Plan oldSelectedPlan, Plan newSelectedPlan, User user) {
        List<Role> rolesToRemove = planRolesExchange.calculateRolesToRemove(oldSelectedPlan, newSelectedPlan);
        List<UserRole> userRolesToRemove = planRolesExchange.determineUserRolesToRemove(user.getUserRoles(), rolesToRemove);

        userRolesToRemove
            .forEach(commitContext::addInstanceToRemove);
    }

    private void addNewRolesToUser(CommitContext commitContext, Plan oldSelectedPlan, Plan newSelectedPlan, User user) {
        List<Role> rolesToAdd = planRolesExchange.calculateRolesToAdd(oldSelectedPlan, newSelectedPlan);

        rolesToAdd.stream()
                .map(role -> createUserRole(user, role))
                .forEach(commitContext::addInstanceToCommit);
    }

    private UserRole createUserRole(User customerUser, Role role) {
        UserRole newUserRole = dataManager.create(UserRole.class);
        newUserRole.setUser(customerUser);
        newUserRole.setRole(role);
        return newUserRole;
    }


}