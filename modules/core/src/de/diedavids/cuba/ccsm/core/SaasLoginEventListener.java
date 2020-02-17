package de.diedavids.cuba.ccsm.core;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.security.auth.events.UserLoggedInEvent;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import de.diedavids.cuba.ccsm.entity.Customer;
import de.diedavids.cuba.ccsm.entity.Plan;
import de.diedavids.cuba.ccsm.entity.PlanLimit;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Component("ccsm_SaasLoginEventListener")
public class SaasLoginEventListener implements ApplicationListener<UserLoggedInEvent> {


    @Inject
    protected DataManager dataManager;

    @Override
    public void onApplicationEvent(UserLoggedInEvent event) {
        UserSession userSession = event.getUserSession();
        if (userSession != null) {

            User user = userSession.getUser();

            Optional<Customer> customer= dataManager.load(Customer.class)
                    .query("select e from ccsm_Customer e where e.tenant.admin = :user")
                    .parameter("user", user)
                    .view("customer-view")
                    .optional();

            if (customer.isPresent()) {

                Plan currentPlan = customer.get().getSubscriptions().get(0).getPlan();


                List<PlanLimit> planLimits = dataManager.load(PlanLimit.class)
                        .query("select e from ccsm_PlanLimit e where e.plan = :plan")
                        .parameter("plan", currentPlan)
                        .view("planLimit-view")
                        .list();

                userSession.setAttribute("currentPlan", currentPlan);

                planLimits
                        .forEach(planLimit ->
                                userSession.setAttribute("limit." + planLimit.getLimit().getCode(), planLimit.getValue())
                        );

            }

        }
    }
}
