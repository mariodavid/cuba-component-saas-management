package de.diedavids.cuba.ccsm.web.screens.waitinglist;

import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.entity.example.Waitinglist;

@UiController("ccsm_Waitinglist.browse")
@UiDescriptor("waitinglist-browse.xml")
@LookupComponent("waitinglistsTable")
@LoadDataBeforeShow
public class WaitinglistBrowse extends StandardLookup<Waitinglist> {
}