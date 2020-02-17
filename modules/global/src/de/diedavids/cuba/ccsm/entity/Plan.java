package de.diedavids.cuba.ccsm.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.security.entity.Role;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@NamePattern("%s|name")
@Table(name = "CCSM_PLAN")
@Entity(name = "ccsm_Plan")
public class Plan extends StandardEntity {
    private static final long serialVersionUID = 8893572062847167615L;

    @NotNull
    @Column(name = "EXTERNAL_ID", nullable = false)
    protected String externalId;

    @NotNull
    @Column(name = "NAME", nullable = false)
    protected String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_ID")
    protected Product product;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "plan")
    protected List<PlanLimit> limits;

    @JoinTable(name = "CCSM_PLAN_ROLE_LINK",
            joinColumns = @JoinColumn(name = "PLAN_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    @ManyToMany
    protected List<Role> roles;

    public List<PlanLimit> getLimits() {
        return limits;
    }

    public void setLimits(List<PlanLimit> limits) {
        this.limits = limits;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}