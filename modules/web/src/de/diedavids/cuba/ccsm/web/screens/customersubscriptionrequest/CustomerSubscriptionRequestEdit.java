package de.diedavids.cuba.ccsm.web.screens.customersubscriptionrequest;

import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.entity.CustomerSubscriptionRequest;

@UiController("ccsm_CustomerSubscriptionRequest.edit")
@UiDescriptor("customer-subscription-request-edit.xml")
@EditedEntityContainer("customerSubscriptionRequestDc")
@LoadDataBeforeShow
public class CustomerSubscriptionRequestEdit extends StandardEditor<CustomerSubscriptionRequest> {
}