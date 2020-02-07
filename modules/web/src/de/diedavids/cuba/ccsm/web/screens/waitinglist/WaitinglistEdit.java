package de.diedavids.cuba.ccsm.web.screens.waitinglist;

import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.entity.example.Waitinglist;

@UiController("ccsm_Waitinglist.edit")
@UiDescriptor("waitinglist-edit.xml")
@EditedEntityContainer("waitinglistDc")
@LoadDataBeforeShow
public class WaitinglistEdit extends StandardEditor<Waitinglist> {
}