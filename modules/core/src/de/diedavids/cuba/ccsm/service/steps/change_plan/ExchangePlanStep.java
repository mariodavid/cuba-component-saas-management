package de.diedavids.cuba.ccsm.service.steps.change_plan;

import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import de.diedavids.cuba.ccsm.ChangeSubscriptionRequest;
import de.diedavids.cuba.ccsm.core.PlanRolesExchange;
import de.diedavids.cuba.ccsm.entity.Plan;
import de.diedavids.cuba.ccsm.entity.Subscription;
import de.diedavids.cuba.ccsm.service.steps.CommitStep;

import java.util.List;

public class ExchangePlanStep implements CommitStep {

    private final DataManager dataManager;
    private final PlanRolesExchange planRolesExchange;
    private final UserAndSubscription userAndSubscription;
    private final ChangeSubscriptionRequest request;

    public ExchangePlanStep(
            DataManager dataManager,
            PlanRolesExchange planRolesExchange,
            UserAndSubscription userAndSubscription,
            ChangeSubscriptionRequest request
    ) {
        this.dataManager = dataManager;
        this.planRolesExchange = planRolesExchange;
        this.userAndSubscription = userAndSubscription;
        this.request = request;
    }

    @Override
    public void accept(CommitContext commitContext) {
        Subscription subscription = userAndSubscription.getSubscription();
        User user = userAndSubscription.getUser();

        Plan newSelectedPlan = loadPlan(request.getPlan());

        Plan oldSelectedPlan = loadPlan(subscription.getPlan().getExternalId());
        addNewRolesToUser(commitContext, oldSelectedPlan, newSelectedPlan, user);
        removeOldRolesForUser(commitContext, oldSelectedPlan, newSelectedPlan, user);


        subscription.setPlan(newSelectedPlan);
        commitContext.addInstanceToCommit(subscription);

    }

    private Plan loadPlan(String plan) {
        PlanLoaderStep newPlanLoader = new PlanLoaderStep(dataManager);
        return newPlanLoader.apply(plan);
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
