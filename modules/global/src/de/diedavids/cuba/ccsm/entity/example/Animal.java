package de.diedavids.cuba.ccsm.entity.example;

import com.haulmont.addon.sdbmt.entity.StandardTenantEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.validation.groups.UiCrossFieldChecks;
import de.diedavids.cuba.ccsm.validation.HasCounterLimit;

import javax.persistence.*;
import javax.validation.groups.Default;

@HasCounterLimit(
        groups = {Default.class, UiCrossFieldChecks.class},
        limit = "ANIMAL_LIMIT"
)
@NamePattern("%s|name")
@Table(name = "CCSM_ANIMAL")
@Entity(name = "ccsm_Animal")
public class Animal extends StandardTenantEntity {
    private static final long serialVersionUID = -8741012426221841692L;

    @Column(name = "NAME")
    protected String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IMAGE_ID")
    protected FileDescriptor image;

    public FileDescriptor getImage() {
        return image;
    }

    public void setImage(FileDescriptor image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}