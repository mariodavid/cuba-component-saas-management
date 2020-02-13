package de.diedavids.cuba.ccsm.service.steps.change_plan

import com.haulmont.addon.sdbmt.entity.Tenant
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.CommitContext
import com.haulmont.cuba.core.global.EntityStates
import com.haulmont.cuba.security.entity.Group
import com.haulmont.cuba.security.entity.Role
import com.haulmont.cuba.security.entity.User
import de.diedavids.cuba.ccsm.IntegrationSpec
import de.diedavids.cuba.ccsm.entity.Customer
import de.diedavids.cuba.ccsm.entity.Plan
import de.diedavids.cuba.ccsm.entity.Product
import de.diedavids.cuba.ccsm.entity.Subscription
import de.diedavids.cuba.ccsm.entity.SubscriptionStatus
import de.diedavids.cuba.ccsm.provisioning.ProductProvisioning

import java.util.function.Function

class UserAndSubscriptionLoaderStepIntegrationSpec extends IntegrationSpec {

    SubscriptionAndUserLoaderStep sut
    EntityStates entityStates
    ProductProvisioning productProvisioning

    void setup() {
        sut = new SubscriptionAndUserLoaderStep(dataManager)
        entityStates = AppBeans.get(EntityStates.class)

        productProvisioning = AppBeans.get(ProductProvisioning)
    }

    def "User and Subscription Loader loads both values by the customers external ID"() {
        given:

        CommitContext commitContext = new CommitContext()

        def randomizer = "" + new Random().nextInt(10000)
        Customer customer = dataManager.create(Customer)

        customer.externalId = "EID-${randomizer}"
        customer.firstName = "Bob"
        customer.name = "Builder"


        def subscription = dataManager.create(Subscription)
        subscription.externalId = "S-${randomizer}"
        subscription.status = SubscriptionStatus.LIVE
        subscription.customer = customer

        Product product = createProductWithPlans("SaaS Product - -${randomizer}", [
                [name: "Small-${randomizer}", externalId: "S-${randomizer}"],
                [name: "Medium-${randomizer}", externalId: "M-${randomizer}"]
        ])

        subscription.plan = product.plans[0]

        customer.subscriptions = [
                subscription
        ]

        Tenant tenant = dataManager.create(Tenant)

        User user = dataManager.create(User)
        user.firstName = "Bob-${randomizer}"
        user.lastName = "Builder-${randomizer}"

        user.login = "bob-${randomizer}"

        Group group = dataManager.load(Group).id(UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93")).one()
        Group tenantGroup = dataManager.create(Group)
        tenantGroup.parent = group
        tenantGroup.name = "G-${randomizer}"

        user.group = tenantGroup

        tenant.admin = user
        tenant.group = tenantGroup
        tenant.name = "tenant-${randomizer}"

        customer.tenant = tenant

        commitContext.addInstanceToCommit(user)
        commitContext.addInstanceToCommit(tenant)
        commitContext.addInstanceToCommit(tenantGroup)
        commitContext.addInstanceToCommit(subscription)
        commitContext.addInstanceToCommit(customer)

        dataManager.commit(commitContext)

        when:
        UserAndSubscription userAndSubscription = sut.apply("EID-${randomizer}")

        then:
        userAndSubscription.user == user

        and:
        userAndSubscription.subscription == subscription

        and:
        entityStates.isLoadedWithView(userAndSubscription.user, "user.edit")
    }

    private Product createProductWithPlans(String productName, List<Map<String, Object>> planDetails) {

        def context = new CommitContext()

        def plansSupplier = new Function<Product, List<Plan>>() {

            @Override
            List<Plan> apply(Product product) {

                planDetails.collect { planDetail ->

                    def plan = dataManager.create(Plan)
                    plan.name = planDetail['name']
                    plan.externalId = planDetail['externalId']
                    plan.roles = planDetail['roles'] as List<Role> ?: [] as List<Role>
                    plan.product = product

                    context.addInstanceToCommit(plan)

                    plan
                }

            }
        }

        Product product = productProvisioning.builder()
                .name(productName)
                .plans(plansSupplier)
                .build()


        context.addInstanceToCommit(product)


        dataManager.commit(context)

        dataManager.reload(product, "product-view")
    }
}
