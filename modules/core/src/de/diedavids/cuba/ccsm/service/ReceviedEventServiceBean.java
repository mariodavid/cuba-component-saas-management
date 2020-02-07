package de.diedavids.cuba.ccsm.service;

import com.chargebee.models.Event;
import com.haulmont.cuba.core.global.DataManager;
import de.diedavids.cuba.ccsm.entity.ReceivedEvent;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service(ReceviedEventService.NAME)
public class ReceviedEventServiceBean implements ReceviedEventService {

    @Inject
    protected DataManager dataManager;

    @Override
    public void storeEvent(
            String eventId,
            String eventSource,
            String eventBody,
            String eventType,
            String apiVersion
    ) {
        ReceivedEvent receivedEvent = dataManager.create(ReceivedEvent.class);

        receivedEvent.setContent(eventBody);
        receivedEvent.setType(eventType);
        receivedEvent.setApiVersion(apiVersion);
        receivedEvent.setEventId(eventId);
        receivedEvent.setSource(eventSource);

        dataManager.commit(receivedEvent);

    }
}