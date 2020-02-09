package de.diedavids.cuba.ccsm.service

import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.CommitContext
import de.diedavids.cuba.ccsm.ChangeSubscriptionRequest
import de.diedavids.cuba.ccsm.CreateCustomerWithSubscriptionRequest
import de.diedavids.cuba.ccsm.IntegrationSpec
import de.diedavids.cuba.ccsm.entity.Customer
import de.diedavids.cuba.ccsm.entity.Plan
import de.diedavids.cuba.ccsm.entity.Product

class SubscriptionServiceBeanSpec extends IntegrationSpec {


    def "changeSubscription changes the subscription plan"() {
        given:
        def sut = AppBeans.get(SubscriptionService.class)


        and:
        def product = dataManager.create(Product)
        product.name = "SaaS Product"

        def planS = dataManager.create(Plan)
        planS.name = "Small"
        planS.externalId = "S"
        planS.product = product

        def planM = dataManager.create(Plan)
        planM.name = "Medium"
        planM.externalId = "M"
        planM.product = product

        product.plans = [
                planS, planM
        ]

        def context = new CommitContext()

        context.addInstanceToCommit(product)
        context.addInstanceToCommit(planS)
        context.addInstanceToCommit(planM)

        dataManager.commit(context)


        def customerId = "123" + new Random().nextInt(10000)
        and:
        sut.createCustomerWithSubscription(new CreateCustomerWithSubscriptionRequest(
                name: "Simpson",
                firstName: "Bart",
                customerId: customerId,
                plan: "M",
                email: "bart.simpson$customerId@example.com",
                organizationName: "Simpsons AG $customerId",
                organizationCode: "simpsons_ag_$customerId",
                password: "bart"

        ))

        and:
        def request = new ChangeSubscriptionRequest(
                customerId: customerId,
                plan: "S"
        )

        when:
        sut.changeSubscription(request)


        then:
        def customer = dataManager.load(Customer)
                .query("select e from ccsm_Customer e where e.externalId = :customerId")
                .parameter("customerId", customerId)
                .view("customer-view")
                .one()

        customer.subscriptions[0].plan.externalId == "S"
    }
}
