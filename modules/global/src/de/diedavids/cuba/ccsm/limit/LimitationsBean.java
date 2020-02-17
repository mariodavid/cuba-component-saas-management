package de.diedavids.cuba.ccsm.limit;


import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.global.UserSession;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component(Limitations.NAME)
public class LimitationsBean implements Limitations {

    private static final String LIMIT_PREFIX = "limit.";

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected Logger log;

    @Override
    public boolean exceededLimit(String limitCode, int value) {

        Integer limitValue = limitValue(limitCode);

        if (limitValue == null) {

            UserSession userSession = userSessionSource.getUserSession();
            log.info(
                    "Limit has not been set for requested key {} and user session [{}: {}]",
                    limitCode,
                    userSession.getCurrentOrSubstitutedUser().getLogin(),
                    userSession.getId()
            );
            return false;
        }

        return value > limitValue;

    }

    private Integer limitValue(String limitKey) {
        return userSessionSource.getUserSession().getAttribute(LIMIT_PREFIX + limitKey);
    }

    @Override
    public Integer getLimit(String limitCode) {
        return limitValue(limitCode);
    }

    @Override
    public boolean hasLimit(String limitCode) {
        return limitValue(limitCode) != null;
    }
}
