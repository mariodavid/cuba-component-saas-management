package de.diedavids.cuba.ccsm.core;

import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.UserRole;
import de.diedavids.cuba.ccsm.entity.Plan;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component(PlanRolesExchange.NAME)
public class PlanRolesExchange {
    public static final String NAME = "ccsm_PlanRolesExchange";


    public List<Role> calculateRolesToRemove(Plan oldPlan, Plan newPlan) {
        return oldPlan.getRoles().stream()
                .filter(role -> !newPlan.getRoles().contains(role))
                .collect(Collectors.toList());
    }

    public List<Role> calculateRolesToAdd(Plan oldPlan, Plan newPlan) {
        return newPlan.getRoles().stream()
                .filter(role -> !oldPlan.getRoles().contains(role))
                .collect(Collectors.toList());
    }

    public List<UserRole> determineUserRolesToRemove(List<UserRole> userRoles, List<Role> rolesToRemove) {
        List<UserRole> result = userRoles.stream()
                .filter(userRole -> rolesToRemove.contains(userRole.getRole()))
                .collect(Collectors.toList());
        return result;
    }
}