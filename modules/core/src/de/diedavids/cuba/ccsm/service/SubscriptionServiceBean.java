package de.diedavids.cuba.ccsm.service;

import com.haulmont.addon.sdbmt.config.TenantConfig;
import com.haulmont.addon.sdbmt.entity.Tenant;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.security.app.Authentication;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import de.diedavids.cuba.ccsm.ChangeSubscriptionRequest;
import de.diedavids.cuba.ccsm.CreateCustomerWithSubscriptionRequest;
import de.diedavids.cuba.ccsm.core.PlanRolesExchange;
import de.diedavids.cuba.ccsm.entity.Customer;
import de.diedavids.cuba.ccsm.entity.Plan;
import de.diedavids.cuba.ccsm.entity.Subscription;
import de.diedavids.cuba.ccsm.entity.SubscriptionStatus;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

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
            Customer customer = dataManager.create(Customer.class);
            Tenant tenant = dataManager.create(Tenant.class);

            Plan selectedPlan = loadPlanByExternalId(request.getPlan());


            customer.setExternalId(request.getCustomerId());
            customer.setName(request.getName());
            customer.setFirstName(request.getFirstName());
            customer.setTenant(tenant);

            Subscription subscription = dataManager.create(Subscription.class);
            subscription.setCustomer(customer);
            subscription.setPlan(selectedPlan);
            subscription.setStatus(SubscriptionStatus.LIVE);

            tenant.setTenantId(request.getOrganizationCode());
            tenant.setName(request.getOrganizationName());

            Group tenantRootGroup = createTenantGroupIfPossible(request.getOrganizationCode());
            tenant.setGroup(tenantRootGroup);


            User customerUser = dataManager.create(User.class);

            customerUser.setFirstName(request.getFirstName());
            customerUser.setLastName(request.getName());
            customerUser.setLogin(request.getEmail());
            customerUser.setEmail(request.getEmail());

            customerUser.setPassword(
                    passwordEncryption.getPasswordHash(customerUser.getId(), request.getPassword())
            );

            customerUser.setGroup(tenantRootGroup);
            tenant.setAdmin(customerUser);


            List<UserRole> planUserRoles = selectedPlan.getRoles()
                    .stream()
                    .map(role -> createUserRole(customerUser, role))
                    .collect(Collectors.toList());


            CommitContext commitContext = new CommitContext();


            commitContext.addInstanceToCommit(customer);
            commitContext.addInstanceToCommit(subscription);
            commitContext.addInstanceToCommit(tenant);
            commitContext.addInstanceToCommit(tenantRootGroup);

            planUserRoles
                    .forEach(commitContext::addInstanceToCommit);

            commitContext.addInstanceToCommit(customerUser);

            dataManager.commit(commitContext);

            return customer;
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


    private Group createTenantGroupIfPossible(String name) {

        Group tenantParentGroup = tenantConfig.getDefaultTenantParentGroup();
        if (tenantParentGroup == null) {
            throw new RuntimeException("Tenants default parent group doesn't exist");
        }

        if (tenantGroupExist(name, tenantParentGroup)) {
            throw new RuntimeException("Tenant Group with that name already exists");
        }

        Group group = dataManager.create(Group.class);
        group.setParent(tenantParentGroup);
        group.setName(name);

        return group;
    }

    private boolean tenantGroupExist(String groupName, Group tenantsParentGroup) {
        LoadContext<Group> ctx = new LoadContext<>(Group.class);
        ctx.setQueryString("select e from sec$Group e where e.parent = :parent and e.name = :name")
                .setParameter("parent", tenantsParentGroup)
                .setParameter("name", groupName);
        return dataManager.getCount(ctx) > 0;
    }
}