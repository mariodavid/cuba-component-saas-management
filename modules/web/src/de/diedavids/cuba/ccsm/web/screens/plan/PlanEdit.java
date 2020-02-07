package de.diedavids.cuba.ccsm.web.screens.plan;

import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.entity.Plan;

@UiController("ccsm_Plan.edit")
@UiDescriptor("plan-edit.xml")
@EditedEntityContainer("planDc")
@LoadDataBeforeShow
public class PlanEdit extends StandardEditor<Plan> {
}