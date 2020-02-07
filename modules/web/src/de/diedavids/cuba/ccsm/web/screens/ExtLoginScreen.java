package de.diedavids.cuba.ccsm.web.screens;

import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.navigation.UrlParamsChangedEvent;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.haulmont.cuba.web.app.login.LoginScreen;

import javax.inject.Inject;


@UiController("login")
@UiDescriptor("ext-login-screen.xml")
public class ExtLoginScreen extends LoginScreen {
    @Inject
    protected Notifications notifications;

    @Subscribe
    protected void onUrlParamsChanged(UrlParamsChangedEvent event) {

        notifications.create(Notifications.NotificationType.TRAY)
                .withCaption(event.getParams().toString())
                .show();
    }

}