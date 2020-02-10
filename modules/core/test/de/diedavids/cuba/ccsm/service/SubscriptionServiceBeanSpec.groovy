package de.diedavids.cuba.ccsm.service

import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.CommitContext
import com.haulmont.cuba.security.entity.Role
import com.haulmont.cuba.security.entity.RoleType
import com.haulmont.cuba.security.entity.User
import de.diedavids.cuba.ccsm.ChangeSubscriptionRequest
import de.diedavids.cuba.ccsm.CreateCustomerWithSubscriptionRequest
import de.diedavids.cuba.ccsm.IntegrationSpec
import de.diedavids.cuba.ccsm.entity.Customer
import de.diedavids.cuba.ccsm.entity.Plan
import de.diedavids.cuba.ccsm.entity.Product

class SubscriptionServiceBeanSpec extends IntegrationSpec {

    SubscriptionService sut

    void setup() {
        sut = AppBeans.get(SubscriptionService.class)

        clearTable("CCSM_SUBSCRIPTION")
        clearTable("CCSM_CUSTOMER")
        clearTable("CCSM_PLAN_ROLE_LINK")
        clearTable("CCSM_PLAN")
        clearTable("CCSM_PRODUCT")

    }

    def "changeSubscription changes the subscription plan of the customer"() {
        given:
        createProductWithPlans("SaaS Product", [
                [name: "Small", externalId: "S"],
                [name: "Medium", externalId: "M"]
        ])

        def customerId = "123" + new Random().nextInt(10000)

        and:
        sut.createCustomerWithSubscription(requestForPlan(customerId, "S"))

        when:
        sut.changeSubscription(new ChangeSubscriptionRequest(
                customerId: customerId,
                plan: "S"
        ))

        then:
        def customer = loadCustomerById(customerId)

        customer.subscriptions[0].plan.externalId == "S"
    }

    def "changeSubscription removes all role associations of the old plan and replaces them with the ones of the new plan"() {
        given:
        def role1ForPlanS = roleWithName("RoleS 1")
        def role2ForPlanS = roleWithName("RoleS 2")
        def role1ForPlanM = roleWithName("RoleM 1")
        def role2ForPlanM = roleWithName("RoleM 2")

        def product = createProductWithPlans("SaaS Product", [
                [name: "Small", externalId: "S", roles: [role1ForPlanS, role2ForPlanS]],
                [name: "Medium", externalId: "M", roles: [role1ForPlanM, role2ForPlanM]],
        ])

        def customerId = "123" + new Random().nextInt(10000)

        and:
        sut.createCustomerWithSubscription(requestForPlan(customerId, "S"))

        when:
        sut.changeSubscription(new ChangeSubscriptionRequest(
                customerId: customerId,
                plan: "S"
        ))

        then:
        def user = loadUserByCustomerId(customerId)

        user.userRoles.size() == 2

        cleanup:
        dataManager.remove(role1ForPlanS)
        dataManager.remove(role1ForPlanM)
        dataManager.remove(role2ForPlanS)
        dataManager.remove(role2ForPlanM)
    }


    private Role roleWithName(String name) {
        def role = dataManager.create(Role.class)
        role.name = name
        role.type = RoleType.STANDARD

        dataManager.commit(role)

        role
    }

    private User loadUserByCustomerId(String customerId) {
        dataManager.reload(
                loadCustomerById(customerId).tenant.admin,
                "user.edit"
        )
    }
    private Customer loadCustomerById(String customerId) {
        dataManager.load(Customer)
                .query("select e from ccsm_Customer e where e.externalId = :customerId")
                .parameter("customerId", customerId)
                .view("customer-view")
                .one()
    }

    private CreateCustomerWithSubscriptionRequest requestForPlan(String customerId, String planId) {
        new CreateCustomerWithSubscriptionRequest(
                name: "Simpson",
                firstName: "Bart",
                customerId: customerId,
                plan: planId,
                email: "bart.simpson$customerId@example.com",
                organizationName: "Simpsons AG $customerId",
                organizationCode: "simpsons_ag_$customerId",
                password: "bart"

        )
    }

    private Product createProductWithPlans(String productName, List<Map<String, Object>> planDetails) {
        def product = dataManager.create(Product)
        product.name = productName

        def plans = planDetails.collect { planDetail ->

            def plan = dataManager.create(Plan)
            plan.name = planDetail['name']
            plan.externalId = planDetail['externalId']
            plan.roles = planDetail['roles'] as List<Role> ?: [] as List<Role>
            plan.product = product

            plan
        }

        product.plans = plans

        def context = new CommitContext()

        context.addInstanceToCommit(product)

        plans.each {
            context.addInstanceToCommit(it)
        }

        dataManager.commit(context)

        dataManager.reload(product, "product-view")
    }
}
