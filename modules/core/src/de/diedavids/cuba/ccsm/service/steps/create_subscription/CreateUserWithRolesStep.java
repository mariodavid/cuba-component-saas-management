package de.diedavids.cuba.ccsm.service.steps.create_subscription;

import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import de.diedavids.cuba.ccsm.CreateCustomerWithSubscriptionRequest;
import de.diedavids.cuba.ccsm.entity.Plan;
import de.diedavids.cuba.ccsm.service.steps.CommitStep;

import java.util.List;
import java.util.stream.Collectors;

public class CreateUserWithRolesStep implements CommitStep {
    private final DataManager dataManager;
    private final PasswordEncryption passwordEncryption;
    private final CreateCustomerWithSubscriptionRequest request;
    private final CreateGroupStep createGroupStep;
    private final Plan plan;
    private User user;

    public CreateUserWithRolesStep(
            DataManager dataManager,
            PasswordEncryption passwordEncryption,
            CreateCustomerWithSubscriptionRequest request,
            CreateGroupStep createGroupStep,
            Plan plan
    ) {

        this.dataManager = dataManager;
        this.passwordEncryption = passwordEncryption;
        this.request = request;
        this.createGroupStep = createGroupStep;
        this.plan = plan;
    }

    @Override
    public void accept(CommitContext commitContext) {
        user = dataManager.create(User.class);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getName());
        user.setLogin(request.getEmail());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncryption.getPasswordHash(user.getId(), request.getPassword())
        );

        user.setGroup(createGroupStep.getGroup());

        List<UserRole> planUserRoles = plan.getRoles()
                .stream()
                .map(role -> createUserRole(user, role))
                .collect(Collectors.toList());


        planUserRoles.forEach(commitContext::addInstanceToCommit);

        commitContext.addInstanceToCommit(user);
    }

    public User getUser() {
        return user;
    }

    private UserRole createUserRole(User customerUser, Role role) {
        UserRole newUserRole = dataManager.create(UserRole.class);
        newUserRole.setUser(customerUser);
        newUserRole.setRole(role);
        return newUserRole;
    }
}
