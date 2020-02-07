package de.diedavids.cuba.ccsm.web.screens.receivedevent;

import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.entity.ReceivedEvent;

@UiController("ccsm_ReceivedEvent.edit")
@UiDescriptor("received-event-edit.xml")
@EditedEntityContainer("receivedEventDc")
@LoadDataBeforeShow
public class ReceivedEventEdit extends StandardEditor<ReceivedEvent> {
}