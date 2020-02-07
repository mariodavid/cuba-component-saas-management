package de.diedavids.cuba.ccsm.web.screens.plan;

import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.entity.Plan;

@UiController("ccsm_Plan.browse")
@UiDescriptor("plan-browse.xml")
@LookupComponent("plansTable")
@LoadDataBeforeShow
public class PlanBrowse extends StandardLookup<Plan> {
}