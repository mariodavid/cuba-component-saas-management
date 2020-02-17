package de.diedavids.cuba.ccsm.web.screens.limit;

import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.entity.Limit;

@UiController("ccsm_Limit.edit")
@UiDescriptor("limit-edit.xml")
@EditedEntityContainer("limitDc")
@LoadDataBeforeShow
public class LimitEdit extends StandardEditor<Limit> {
}