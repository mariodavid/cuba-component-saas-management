package de.diedavids.cuba.ccsm.web.screens;

import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Route;
import com.haulmont.cuba.gui.navigation.UrlParamsChangedEvent;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.haulmont.cuba.web.app.main.MainScreen;

import javax.inject.Inject;
import java.util.Map;


@Route(value = "register", root = true)
@UiController("registerScreen")
@UiDescriptor("register-screen.xml")
public class RegisterScreen extends Screen {


    @Inject
    protected Notifications notifications;
    private Map<String, String> requestParams;


    @Subscribe
    protected void onAfterShow1(AfterShowEvent event) {

        notifications.create(Notifications.NotificationType.TRAY)
                .withCaption(requestParams.get("sub_id"))
                .show();

    }

    @Subscribe
    protected void onUrlParamsChanged(UrlParamsChangedEvent event) {

        notifications.create(Notifications.NotificationType.TRAY)
                .withCaption(event.getParams().toString())
                .show();
    }
    
    
    
    
}