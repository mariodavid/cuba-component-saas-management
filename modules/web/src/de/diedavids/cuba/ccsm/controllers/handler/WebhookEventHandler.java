package de.diedavids.cuba.ccsm.controllers.handler;

import com.chargebee.models.Event;
import com.chargebee.models.enums.EventType;

public interface WebhookEventHandler {

    boolean supports(EventType eventType);

    boolean handle(Event event);

}
