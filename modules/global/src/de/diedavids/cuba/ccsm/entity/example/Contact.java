package de.diedavids.cuba.ccsm.entity.example;

import com.haulmont.addon.sdbmt.entity.StandardTenantEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@NamePattern("%s|name")
@Table(name = "CCSM_CONTACT")
@Entity(name = "ccsm_Contact")
public class Contact extends StandardTenantEntity {
    private static final long serialVersionUID = 3135667533062944363L;

    @Column(name = "NAME")
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}