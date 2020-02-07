package de.diedavids.cuba.ccsm.web.screens.receivedevent;

import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.entity.ReceivedEvent;

@UiController("ccsm_ReceivedEvent.browse")
@UiDescriptor("received-event-browse.xml")
@LookupComponent("receivedEventsTable")
@LoadDataBeforeShow
public class ReceivedEventBrowse extends StandardLookup<ReceivedEvent> {
}