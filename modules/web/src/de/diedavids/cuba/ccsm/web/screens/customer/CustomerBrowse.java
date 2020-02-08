package de.diedavids.cuba.ccsm.web.screens.customer;

import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.app.core.inputdialog.InputParameter;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.CreateCustomerWithSubscriptionRequest;
import de.diedavids.cuba.ccsm.entity.Customer;
import de.diedavids.cuba.ccsm.entity.Plan;
import de.diedavids.cuba.ccsm.service.SubscriptionService;

import javax.inject.Inject;

@UiController("ccsm_Customer.browse")
@UiDescriptor("customer-browse.xml")
@LookupComponent("customersTable")
@LoadDataBeforeShow
public class CustomerBrowse extends StandardLookup<Customer> {

    @Inject
    protected Dialogs dialogs;
    @Inject
    protected SubscriptionService subscriptionService;

    @Subscribe("customersTable.quickCreate")
    protected void onCustomersTableQuickCreate(Action.ActionPerformedEvent event) {

        dialogs.createInputDialog(this)
                .withCaption("Create Customer")
                .withParameters(
                        InputParameter.stringParameter("email")
                            .withRequired(true)
                            .withCaption("Email"),
                        InputParameter.stringParameter("password")
                            .withRequired(true)
                            .withCaption("Password"),
                        InputParameter.stringParameter("externalId")
                            .withRequired(true)
                            .withCaption("External ID"),
                        InputParameter.stringParameter("organizationName")
                            .withRequired(false)
                            .withCaption("Organization Name"),
                        InputParameter.stringParameter("organizationCode")
                            .withRequired(false)
                            .withCaption("Organization Code"),
                        InputParameter.stringParameter("firstName")
                        .withRequired(true)
                        .withCaption("First Name"),
                        InputParameter.stringParameter("lastName")
                        .withRequired(true)
                        .withCaption("Last Name"),
                        InputParameter.entityParameter("plan", Plan.class)
                        .withRequired(true)
                        .withCaption("Plan")
                )
                .withCloseListener(closeEvent -> {

                    CreateCustomerWithSubscriptionRequest request = new CreateCustomerWithSubscriptionRequest();

                    request.setName(closeEvent.getValue("lastName"));
                    request.setFirstName(closeEvent.getValue("firstName"));
                    request.setCustomerId(closeEvent.getValue("externalId"));
                    request.setOrganizationName(closeEvent.getValue("organizationName"));
                    request.setOrganizationCode(closeEvent.getValue("organizationCode"));
                    request.setEmail(closeEvent.getValue("email"));
                    request.setPassword(closeEvent.getValue("password"));
                    request.setPlan(closeEvent.getValue("plan"));

                    subscriptionService.createCustomerWithSubscription(
                            request
                    );
                })
        .show();
    }
}