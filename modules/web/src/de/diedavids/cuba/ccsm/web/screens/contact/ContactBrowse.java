package de.diedavids.cuba.ccsm.web.screens.contact;

import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.entity.example.Contact;

@UiController("ccsm_Contact.browse")
@UiDescriptor("contact-browse.xml")
@LookupComponent("contactsTable")
@LoadDataBeforeShow
public class ContactBrowse extends StandardLookup<Contact> {
}