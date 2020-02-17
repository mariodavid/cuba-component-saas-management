package de.diedavids.cuba.ccsm.web.screens.limit;

import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.entity.Limit;

@UiController("ccsm_Limit.browse")
@UiDescriptor("limit-browse.xml")
@LookupComponent("limitsTable")
@LoadDataBeforeShow
public class LimitBrowse extends StandardLookup<Limit> {
}