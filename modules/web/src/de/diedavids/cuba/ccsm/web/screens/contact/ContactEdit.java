package de.diedavids.cuba.ccsm.web.screens.contact;

import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.entity.example.Contact;

@UiController("ccsm_Contact.edit")
@UiDescriptor("contact-edit.xml")
@EditedEntityContainer("contactDc")
@LoadDataBeforeShow
public class ContactEdit extends StandardEditor<Contact> {
}