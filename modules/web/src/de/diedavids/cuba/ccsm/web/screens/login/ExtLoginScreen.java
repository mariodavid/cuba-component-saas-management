package de.diedavids.cuba.ccsm.web.screens.login;

import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.web.app.login.LoginScreen;
import de.diedavids.cuba.ccsm.web.screens.register.RegisterScreen;
import de.diedavids.cuba.ccsm.web.screens.restorepassword.RestorePasswordScreen;

import javax.inject.Inject;

/**
 * Extended login screen.
 */
@UiController("login")
@UiDescriptor("ext-login-screen.xml")
public class ExtLoginScreen extends LoginScreen {

    @Inject
    protected ScreenBuilders screenBuilders;
    /**
     * Injected component from the base screen.
     */
    @Inject
    private Button loginButton;

    @Subscribe("registerBtn")
    public void onRegisterBtnClick(Button.ClickEvent event) {

        RegisterScreen registerScreen = screens.create(RegisterScreen.class, OpenMode.DIALOG);

        registerScreen.addAfterCloseListener(afterCloseEvent -> {
            CloseAction closeAction = afterCloseEvent.getCloseAction();
            if (closeAction == WINDOW_COMMIT_AND_CLOSE_ACTION) {
                loginButton.focus();
            }
        });

        registerScreen.show();
    }

    @Subscribe("restorePasswordBtn")
    public void onRestorePasswordBtnClick(Button.ClickEvent event) {
        RestorePasswordScreen restorePasswordScreen = screens.create(RestorePasswordScreen.class, OpenMode.DIALOG);

        restorePasswordScreen.addAfterCloseListener(afterCloseEvent -> {
            CloseAction closeAction = afterCloseEvent.getCloseAction();
            if (closeAction == WINDOW_COMMIT_AND_CLOSE_ACTION) {
                loginField.setValue(restorePasswordScreen.getLogin());
                passwordField.setValue(null);
                passwordField.focus();
            }
        });

        restorePasswordScreen.show();
    }
}
