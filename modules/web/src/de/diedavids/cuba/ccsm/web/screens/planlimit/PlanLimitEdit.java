package de.diedavids.cuba.ccsm.web.screens.planlimit;

import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.entity.PlanLimit;

@UiController("ccsm_PlanLimit.edit")
@UiDescriptor("plan-limit-edit.xml")
@EditedEntityContainer("planLimitDc")
@LoadDataBeforeShow
public class PlanLimitEdit extends StandardEditor<PlanLimit> {
}