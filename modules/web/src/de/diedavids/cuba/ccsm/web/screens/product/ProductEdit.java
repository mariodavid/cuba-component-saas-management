package de.diedavids.cuba.ccsm.web.screens.product;

import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.entity.Product;

@UiController("ccsm_Product.edit")
@UiDescriptor("product-edit.xml")
@EditedEntityContainer("productDc")
@LoadDataBeforeShow
public class ProductEdit extends StandardEditor<Product> {
}