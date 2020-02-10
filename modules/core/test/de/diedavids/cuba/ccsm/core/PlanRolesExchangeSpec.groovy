package de.diedavids.cuba.ccsm.core

import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.security.entity.Role
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.security.entity.UserRole
import de.diedavids.cuba.ccsm.IntegrationSpec
import de.diedavids.cuba.ccsm.entity.Plan

class PlanRolesExchangeSpec extends IntegrationSpec {
    private Role r1
    private Role r2
    private Role r3
    private User user
    private PlanRolesExchange sut


    void setup() {
        r1 = role("r1")
        r2 = role("r2")
        r3 = role("r3")
        user = metadata.create(User)

        sut = AppBeans.get(PlanRolesExchange.class)
    }

    def "calculateRolesToRemove returns all Roles from the old plan that are not in the new plan"() {
        given:


        Plan oldPlan = plan([r1, r2])

        Plan newPlan = plan([r2])

        expect:
        sut.calculateRolesToRemove(oldPlan, newPlan) == [r1]
    }

    def "calculateRolesToRemove returns all Roles from the old Plan if no Roles are shared"() {
        given:

        PlanRolesExchange sut = AppBeans.get(PlanRolesExchange.class)

        Plan oldPlan = plan([r1, r2])

        Plan newPlan =  plan([r3])

        expect:
        sut.calculateRolesToRemove(oldPlan, newPlan) == [r1, r2]
    }


    def "calculateRolesToRemove returns nothing if all Roles are shared"() {
        given:
        Plan oldPlan = plan([r1])
        Plan newPlan = plan([r1])

        expect:
        sut.calculateRolesToRemove(oldPlan, newPlan) == []
    }




    def "calculateRolesToAdd returns all Roles that are only in the new Plan"() {
        given:
        Plan oldPlan = plan([r1])
        Plan newPlan = plan([r1, r2])

        expect:
        sut.calculateRolesToAdd(oldPlan, newPlan) == [r2]
    }

    def "calculateRolesToAdd returns all Roles from the new Plan if no Roles are shared"() {
        given:

        PlanRolesExchange sut = AppBeans.get(PlanRolesExchange.class)

        Plan oldPlan = plan([r1, r2])

        Plan newPlan =  plan([r3])

        expect:
        sut.calculateRolesToAdd(oldPlan, newPlan) == [r3]
    }

    def "calculateRolesToAdd returns nothing if all Roles are shared"() {
        given:
        Plan oldPlan = plan([r1])
        Plan newPlan = plan([r1])

        expect:
        sut.calculateRolesToAdd(oldPlan, newPlan) == []
    }


    def "determineUserRolesToRemove finds the UserRole objects that should be removed for the Role Criteria List"() {

        given:
        def ur1 = userRole(r1)
        def ur2 = userRole(r2)
        def ur3 = userRole(r3)


        expect:
        sut.determineUserRolesToRemove(
                [ur1, ur2, ur3],
                [r1, r2]
        ) == [ur1, ur2]
    }

    def "determineUserRolesToRemove returns nothing if no User Role matches the Role Criteria List"() {

        given:
        def ur1 = userRole(r1)
        def ur2 = userRole(r2)

        expect:
        sut.determineUserRolesToRemove(
                [ur1, ur2],
                [r3]
        ) == []
    }


    def "determineUserRolesToRemove returns all User Role if all match the Role Criteria List"() {

        given:
        def ur1 = userRole(r1)
        def ur2 = userRole(r2)
        def ur3 = userRole(r3)


        expect:
        sut.determineUserRolesToRemove(
                [ur1, ur2, ur3],
                [r1, r2, r3]
        ) == [ur1, ur2, ur3]
    }

    UserRole userRole(Role role) {
        def userRole = metadata.create(UserRole)
        userRole.user = user
        userRole.role = role
        userRole
    }

    private Plan plan(List<Role> roles) {
        Plan oldPlan = metadata.create(Plan)

        oldPlan.roles = roles
        oldPlan
    }

    private Role role(String name) {
        def role = metadata.create(Role)
        role.name = name
        role
    }
}
