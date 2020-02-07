package de.diedavids.cuba.ccsm.service;

import com.haulmont.addon.sdbmt.config.TenantConfig;
import com.haulmont.addon.sdbmt.entity.Tenant;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import de.diedavids.cuba.ccsm.CreateCustomerWithSubscriptionRequest;
import de.diedavids.cuba.ccsm.entity.Customer;
import de.diedavids.cuba.ccsm.entity.Plan;
import de.diedavids.cuba.ccsm.entity.Subscription;
import de.diedavids.cuba.ccsm.entity.SubscriptionStatus;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;

@Service(CustomerCreationService.NAME)
public class CustomerCreationServiceBean implements CustomerCreationService {

    @Inject
    protected DataManager dataManager;

    @Inject
    protected TenantConfig tenantConfig;


    @Inject
    protected PasswordEncryption passwordEncryption;

    @Override
    public Customer createCustomer(CreateCustomerWithSubscriptionRequest request) {
        Customer customer = dataManager.create(Customer.class);
        Tenant tenant = dataManager.create(Tenant.class);

        Plan selectedPlan = dataManager.reload(request.getPlan(), "plan-view");


        CommitContext commitContext = new CommitContext();


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

        UserRole userRole = dataManager.create(UserRole.class);
        userRole.setUser(customerUser);
        userRole.setRole(tenantConfig.getDefaultTenantRole());

        selectedPlan.getRoles()
                .stream()
                .map(role -> createUserRole(customerUser, role))
                .forEach(commitContext::addInstanceToCommit);


        customerUser.setUserRoles(Collections.singletonList(userRole));

        tenant.setAdmin(customerUser);


        commitContext.addInstanceToCommit(customer);
        commitContext.addInstanceToCommit(subscription);
        commitContext.addInstanceToCommit(tenant);
        commitContext.addInstanceToCommit(tenantRootGroup);
        commitContext.addInstanceToCommit(userRole);
        commitContext.addInstanceToCommit(customerUser);

        dataManager.commit(commitContext);

        return customer;
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