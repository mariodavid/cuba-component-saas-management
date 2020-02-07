package de.diedavids.cuba.ccsm.web.screens.subscription;

import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.entity.Subscription;

@UiController("ccsm_Subscription.browse")
@UiDescriptor("subscription-browse.xml")
@LookupComponent("subscriptionsTable")
@LoadDataBeforeShow
public class SubscriptionBrowse extends StandardLookup<Subscription> {
}