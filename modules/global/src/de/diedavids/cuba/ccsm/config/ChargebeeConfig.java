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


    @DefaultString("hanswurstattr-test")
    @Property("ccsm.chargebee.siteName")
    String getSiteName();


    @DefaultString("test_DU5zu4uNzRcd9amX2xEVAZdwoqTTnGtZ4")
    @Secret
    @Property("ccsm.chargebee.apiKey")
    String getApiKey();

}