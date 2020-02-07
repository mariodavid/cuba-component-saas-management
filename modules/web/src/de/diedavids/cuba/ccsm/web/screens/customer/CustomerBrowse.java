package de.diedavids.cuba.ccsm.web.screens.customer;

import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.app.core.inputdialog.InputParameter;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.CreateCustomerWithSubscriptionRequest;
import de.diedavids.cuba.ccsm.entity.Customer;
import de.diedavids.cuba.ccsm.entity.Plan;
import de.diedavids.cuba.ccsm.service.CustomerCreationService;

import javax.inject.Inject;

@UiController("ccsm_Customer.browse")
@UiDescriptor("customer-browse.xml")
@LookupComponent("customersTable")
@LoadDataBeforeShow
public class CustomerBrowse extends StandardLookup<Customer> {

    @Inject
    protected Dialogs dialogs;
    @Inject
    protected CustomerCreationService customerCreationService;
    @Inject
    protected DataContext dataContext;

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

                    CreateCustomerWithSubscriptionRequest createCustomerWithSubscriptionRequest = new CreateCustomerWithSubscriptionRequest();

                    createCustomerWithSubscriptionRequest.setName(closeEvent.getValue("lastName"));
                    createCustomerWithSubscriptionRequest.setFirstName(closeEvent.getValue("firstName"));
                    createCustomerWithSubscriptionRequest.setCustomerId(closeEvent.getValue("externalId"));
                    createCustomerWithSubscriptionRequest.setOrganizationName(closeEvent.getValue("organizationName"));
                    createCustomerWithSubscriptionRequest.setOrganizationCode(closeEvent.getValue("organizationCode"));
                    createCustomerWithSubscriptionRequest.setEmail(closeEvent.getValue("email"));
                    createCustomerWithSubscriptionRequest.setPassword(closeEvent.getValue("password"));
                    createCustomerWithSubscriptionRequest.setPlan(closeEvent.getValue("plan"));
                    customerCreationService.createCustomer(
                            createCustomerWithSubscriptionRequest
                    );


                })
        .show();
    }
}