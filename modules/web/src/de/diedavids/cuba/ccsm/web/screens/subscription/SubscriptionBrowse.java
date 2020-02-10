package de.diedavids.cuba.ccsm.web.screens.subscription;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.app.core.inputdialog.InputParameter;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.ChangeSubscriptionRequest;
import de.diedavids.cuba.ccsm.entity.Plan;
import de.diedavids.cuba.ccsm.entity.Product;
import de.diedavids.cuba.ccsm.entity.Subscription;
import de.diedavids.cuba.ccsm.service.SubscriptionService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@UiController("ccsm_Subscription.browse")
@UiDescriptor("subscription-browse.xml")
@LookupComponent("subscriptionsTable")
@LoadDataBeforeShow
public class SubscriptionBrowse extends StandardLookup<Subscription> {

    @Inject
    protected GroupTable<Subscription> subscriptionsTable;
    @Inject
    protected SubscriptionService subscriptionService;
    @Inject
    protected Dialogs dialogs;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected CollectionLoader<Subscription> subscriptionsDl;

    @Subscribe("subscriptionsTable.changePlan")
    protected void onSubscriptionsTableChangePlan(Action.ActionPerformedEvent event) {

        Subscription subscription = subscriptionsTable.getSingleSelected();


        dialogs.createInputDialog(this)
                .withCaption("Select Plan")
                .withParameter(
                        InputParameter.parameter("plan")
                        .withField(this::planLookup)
                )
                .withCloseListener(closeEvent -> {

                    ChangeSubscriptionRequest request = new ChangeSubscriptionRequest();
                    request.setCustomerId(subscription.getCustomer().getExternalId());
                    request.setPlan(((Plan)closeEvent.getValue("plan")).getExternalId());
                    subscriptionService.changeSubscription(request);
                    subscriptionsDl.load();
                })
                .show();
    }

    private LookupField planLookup() {
        LookupField lookupField = uiComponents.create(LookupField.class);

        Subscription subscription = subscriptionsTable.getSingleSelected();

        Plan plan = dataManager.reload(subscription.getPlan(), "plan-view");
        Product product = dataManager.reload(plan.getProduct(), "product-view");

        List<Plan> plans = new ArrayList<>(product.getPlans());

        plans.remove(plan);

        lookupField.setOptionsList(plans);
        lookupField.setCaption("Plan");
        lookupField.setWidthFull();
        return lookupField;
    }
}