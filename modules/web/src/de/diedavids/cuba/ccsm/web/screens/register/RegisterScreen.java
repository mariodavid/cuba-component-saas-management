package de.diedavids.cuba.ccsm.web.screens.register;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.validation.MethodParametersValidationException;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Route;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.PasswordField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import de.diedavids.cuba.ccsm.CreateCustomerWithSubscriptionRequest;
import de.diedavids.cuba.ccsm.entity.CustomerSubscriptionRequest;
import de.diedavids.cuba.ccsm.entity.Plan;
import de.diedavids.cuba.ccsm.service.SubscriptionService;

import javax.inject.Inject;

@Route(value = "register", root = true)
@UiController("sample_RegisterScreen")
@UiDescriptor("register-screen.xml")
public class RegisterScreen extends Screen {

    @Inject
    protected CollectionLoader<Plan> plansDl;
    @Inject
    private Notifications notifications;
    @Inject
    private Messages messages;
    @Inject
    protected SubscriptionService subscriptionService;
    @Inject
    protected InstanceContainer<CustomerSubscriptionRequest> customerSubscriptionRequestDc;
    @Inject
    protected DataContext dataContext;

    @Subscribe
    protected void onInit(InitEvent event) {
        customerSubscriptionRequestDc.setItem(dataContext.create(CustomerSubscriptionRequest.class));
        plansDl.load();
    }


    @Subscribe("okBtn")
    public void onOkBtnClick(Button.ClickEvent event) {
        try {

            CreateCustomerWithSubscriptionRequest request = new CreateCustomerWithSubscriptionRequest();

            request.setName(item().getLastName());
            request.setFirstName(item().getFirstName());
            request.setCustomerId(item().getCustomerId());
            request.setOrganizationName(item().getOrganizationName());
            request.setOrganizationCode(item().getOrganizationCode());
            request.setEmail(item().getEmail());
            request.setPassword(item().getPassword());
            request.setPlan(item().getPlan());

            subscriptionService.createCustomerWithSubscription(
                    request
            );

            close(WINDOW_COMMIT_AND_CLOSE_ACTION);
        } catch (MethodParametersValidationException e) {
            notifications.create(Notifications.NotificationType.TRAY)
                    .withCaption(
                            messages.getMessage(
                                    "com.company.sample.validation",
                                    "UserExistsValidator.message"))
                    .show();
        }
    }

    private CustomerSubscriptionRequest item() {
        return customerSubscriptionRequestDc.getItem();
    }

}