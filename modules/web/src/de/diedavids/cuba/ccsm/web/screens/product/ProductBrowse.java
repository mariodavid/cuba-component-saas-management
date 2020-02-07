package de.diedavids.cuba.ccsm.web.screens.product;

import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.entity.Product;

@UiController("ccsm_Product.browse")
@UiDescriptor("product-browse.xml")
@LookupComponent("productsTable")
@LoadDataBeforeShow
public class ProductBrowse extends StandardLookup<Product> {
}