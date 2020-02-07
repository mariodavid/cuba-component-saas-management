package de.diedavids.cuba.ccsm.web.screens;

import com.chargebee.Environment;
import com.chargebee.Result;
import com.chargebee.models.PortalSession;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.BrowserFrame;
import com.haulmont.cuba.gui.components.UrlResource;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.haulmont.cuba.security.global.UserSession;
import de.diedavids.cuba.ccsm.config.ChargebeeConfig;
import de.diedavids.cuba.ccsm.entity.Customer;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

@UiController("ccsm_ClientPortalScreen")
@UiDescriptor("client-portal-screen.xml")
public class ClientPortalScreen extends Screen {


    @Inject
    protected UserSession userSession;
    @Inject
    protected BrowserFrame clientPortalFrame;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected Notifications notifications;

    @Inject
    protected ChargebeeConfig chargebeeConfig;

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) throws Exception {

        Optional<Customer> currentCustomer = loadCustomerFromCurrentUser();

        if (currentCustomer.isPresent()) {

            URL url = getChargebeeClientPortalUrl(
                    currentCustomer.get().getExternalId()
            );
            clientPortalFrame.setSource(UrlResource.class).setUrl(url);

        }
        else {
            notifications.create(Notifications.NotificationType.WARNING)
                    .withCaption("Customer for current user not found");
        }

    }

    private Optional<Customer> loadCustomerFromCurrentUser() {
        return dataManager
                    .load(Customer.class)
                    .query("select e from ccsm_Customer e where e.tenant.admin = :currentUser")
                    .parameter("currentUser", userSession.getUser())
                    .optional();
    }

    private URL getChargebeeClientPortalUrl(String customerId) throws Exception {
        Environment.configure(
                chargebeeConfig.getSiteName(),
                chargebeeConfig.getApiKey()
        );

        Result result = PortalSession.create()
                .customerId(customerId)
                .request();
        PortalSession portalSession = result.portalSession();

        String accessUrl = portalSession.accessUrl();

        URL url = null;
        try {
            url = new URL(accessUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


}