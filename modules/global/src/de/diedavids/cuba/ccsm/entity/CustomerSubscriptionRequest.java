package de.diedavids.cuba.ccsm.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@NamePattern("%s|lastName")
@MetaClass(name = "ccsm_CustomerSubscriptionRequest")
public class CustomerSubscriptionRequest extends BaseUuidEntity {
    private static final long serialVersionUID = 7538696747115104016L;

    @MetaProperty
    protected String lastName;

    @MetaProperty
    protected String firstName;

    @NotNull
    @MetaProperty(mandatory = true)
    protected String password;

    @NotNull
    @MetaProperty(mandatory = true)
    protected String organizationName;

    @NotNull
    @MetaProperty(mandatory = true)
    protected String organizationCode;

    @Email
    @NotNull
    @MetaProperty(mandatory = true)
    protected String email;

    @NotNull
    @MetaProperty(mandatory = true)
    protected Plan plan;

    @NotNull
    @MetaProperty(mandatory = true)
    protected String customerId;

    @NotNull
    @MetaProperty(mandatory = true)
    protected String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}