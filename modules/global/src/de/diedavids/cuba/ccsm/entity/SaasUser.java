package de.diedavids.cuba.ccsm.entity;

import com.haulmont.addon.sdbmt.entity.TenantUser;
import com.haulmont.cuba.core.entity.annotation.Extends;
import com.haulmont.cuba.core.global.validation.groups.UiCrossFieldChecks;
import de.diedavids.cuba.ccsm.validation.HasCounterLimit;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.groups.Default;


@DiscriminatorValue("ccsm_SaasUser")
@HasCounterLimit(
        groups = {Default.class, UiCrossFieldChecks.class},
        limit = "USER_LIMIT"
)
@Extends(TenantUser.class)
@Entity(name = "ccsm_SaasUser")
public class SaasUser extends TenantUser {
    private static final long serialVersionUID = 6138861699337179134L;
}
// cubasdbmt$TenantUser