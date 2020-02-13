package de.diedavids.cuba.ccsm.provisioning;

import com.haulmont.cuba.core.global.Metadata;
import de.diedavids.cuba.ccsm.entity.Plan;
import de.diedavids.cuba.ccsm.entity.Product;
import lombok.Builder;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.function.Function;

@Component("ccsm_ProductProvisioning")
public class ProductProvisioning {

    @Inject
    private Metadata metadata;

    @Builder(builderMethodName = "builder")
    public Product createProductBuilder(String name, Function<Product, List<Plan>> plans) {
        Product product = metadata.create(Product.class);
        product.setName(name);
        product.setPlans(plans.apply(product));
        return product;
    }

}
