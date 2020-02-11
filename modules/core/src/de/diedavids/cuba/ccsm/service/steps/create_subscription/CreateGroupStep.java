package de.diedavids.cuba.ccsm.service.steps.create_subscription;

import com.haulmont.addon.sdbmt.config.TenantConfig;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.security.entity.Group;
import de.diedavids.cuba.ccsm.service.steps.CommitStep;

public class CreateGroupStep implements CommitStep {
    private final DataManager dataManager;
    private final TenantConfig tenantConfig;
    private final String organizationCode;
    private Group group;

    public CreateGroupStep(DataManager dataManager, TenantConfig tenantConfig, String organizationCode) {

        this.dataManager = dataManager;
        this.tenantConfig = tenantConfig;
        this.organizationCode = organizationCode;
    }

    @Override
    public void accept(CommitContext commitContext) {
        group = createTenantGroupIfPossible(organizationCode);

        commitContext.addInstanceToCommit(group);
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

    public Group getGroup() {
        return group;
    }

}
