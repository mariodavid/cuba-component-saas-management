package de.diedavids.cuba.ccsm.service.steps.create_subscription;

import com.haulmont.addon.sdbmt.config.TenantConfig;
import com.haulmont.addon.sdbmt.entity.Tenant;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.PasswordEncryption;
import de.diedavids.cuba.ccsm.CreateCustomerWithSubscriptionRequest;
import de.diedavids.cuba.ccsm.entity.Plan;
import de.diedavids.cuba.ccsm.service.steps.CommitStep;

import java.util.function.Supplier;

public class CreateTenantStep implements CommitStep {

    private final DataManager dataManager;
    private final CreateCustomerWithSubscriptionRequest request;
    private final TenantConfig tenantConfig;
    private final PasswordEncryption passwordEncryption;
    private final CreateCustomerStep createCustomerStep;
    private final Supplier<Plan> planSupplier;

    public CreateTenantStep(
            DataManager dataManager,
            CreateCustomerWithSubscriptionRequest request,
            TenantConfig tenantConfig,
            PasswordEncryption passwordEncryption,
            CreateCustomerStep createCustomerStep,
            Supplier<Plan> planSupplier
    ) {

        this.dataManager = dataManager;
        this.request = request;
        this.tenantConfig = tenantConfig;
        this.passwordEncryption = passwordEncryption;
        this.createCustomerStep = createCustomerStep;
        this.planSupplier = planSupplier;
    }

    @Override
    public void accept(CommitContext commitContext) {
        Tenant tenant = dataManager.create(Tenant.class);

        tenant.setTenantId(request.getOrganizationCode());
        tenant.setName(request.getOrganizationName());


        createCustomerStep.getCustomer().setTenant(tenant);

        CreateGroupStep createGroupStep = new CreateGroupStep(dataManager, tenantConfig, request.getOrganizationCode());
        createGroupStep.accept(commitContext);

        tenant.setGroup(createGroupStep.getGroup());

        CreateUserWithRolesStep createUserWithRolesStep = new CreateUserWithRolesStep(
                dataManager,
                passwordEncryption,
                request,
                createGroupStep,
                planSupplier.get()
        );
        createUserWithRolesStep.accept(commitContext);

        tenant.setAdmin(createUserWithRolesStep.getUser());


        commitContext.addInstanceToCommit(tenant);
    }


}
