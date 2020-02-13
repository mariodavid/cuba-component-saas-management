package de.diedavids.cuba.ccsm.service.steps.change_plan

import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.CommitContext
import com.haulmont.cuba.core.global.EntityStates
import com.haulmont.cuba.security.entity.Role
import com.haulmont.cuba.security.entity.RoleType
import com.haulmont.cuba.security.entity.User
import de.diedavids.cuba.ccsm.ChangeSubscriptionRequest
import de.diedavids.cuba.ccsm.CreateCustomerWithSubscriptionRequest
import de.diedavids.cuba.ccsm.IntegrationSpec
import de.diedavids.cuba.ccsm.entity.Customer
import de.diedavids.cuba.ccsm.entity.Plan
import de.diedavids.cuba.ccsm.entity.Product
import de.diedavids.cuba.ccsm.service.SubscriptionService

class PlanLoaderStepIntegrationSpec extends IntegrationSpec {

    PlanLoaderStep sut
    EntityStates entityStates

    void setup() {
        sut = new PlanLoaderStep(dataManager)
        entityStates = AppBeans.get(EntityStates.class)
    }

    def "Plan Loader loads a plan by its external ID"() {
        given:
        createProductWithPlans("SaaS Product", [
                [name: "Small", externalId: "S"],
                [name: "Medium", externalId: "M"]
        ])

        when:
        Plan plan = sut.apply("S")

        then:
        plan.externalId == "S"

        and:
        entityStates.isLoadedWithView(plan, "plan-view")
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
