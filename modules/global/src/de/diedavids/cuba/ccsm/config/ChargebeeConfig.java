package de.diedavids.cuba.ccsm.config;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultInt;
import com.haulmont.cuba.core.config.defaults.DefaultString;
import com.haulmont.cuba.core.global.Secret;

@Source(type = SourceType.DATABASE)
public interface ChargebeeConfig extends Config {


    @DefaultString("hans-wurst-test")
    @Property("ccsm.chargebee.siteName")
    String getSiteName();


    @DefaultString("test_d0aZ3cn1xeBXR9IDKOiDTzq9aE8nmnmM")
    @Secret
    @Property("ccsm.chargebee.apiKey")
    String getApiKey();

}