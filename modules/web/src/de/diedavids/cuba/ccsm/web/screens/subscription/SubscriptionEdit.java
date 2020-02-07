package de.diedavids.cuba.ccsm.web.screens.subscription;

import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.entity.Subscription;

@UiController("ccsm_Subscription.edit")
@UiDescriptor("subscription-edit.xml")
@EditedEntityContainer("subscriptionDc")
@LoadDataBeforeShow
public class SubscriptionEdit extends StandardEditor<Subscription> {
}