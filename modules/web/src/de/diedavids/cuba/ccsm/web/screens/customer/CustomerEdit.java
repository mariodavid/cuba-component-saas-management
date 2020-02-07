package de.diedavids.cuba.ccsm.web.screens.customer;

import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.entity.Customer;

@UiController("ccsm_Customer.edit")
@UiDescriptor("customer-edit.xml")
@EditedEntityContainer("customerDc")
@LoadDataBeforeShow
public class CustomerEdit extends StandardEditor<Customer> {
}