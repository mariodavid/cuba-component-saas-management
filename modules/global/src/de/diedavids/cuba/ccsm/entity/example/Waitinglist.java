package de.diedavids.cuba.ccsm.entity.example;

import com.haulmont.addon.sdbmt.entity.StandardTenantEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NamePattern("%s: %s - %s|position,animal,contact")
@Table(name = "CCSM_WAITINGLIST")
@Entity(name = "ccsm_Waitinglist")
public class Waitinglist extends StandardTenantEntity {
    private static final long serialVersionUID = -6779827804663656621L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ANIMAL_ID")
    protected Animal animal;

    @Column(name = "POSITION_")
    protected Integer position;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CONTACT_ID")
    protected Contact contact;

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
}