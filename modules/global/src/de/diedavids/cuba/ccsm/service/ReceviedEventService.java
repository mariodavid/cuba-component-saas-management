package de.diedavids.cuba.ccsm.service;

public interface ReceviedEventService {
    String NAME = "ccsm_ReceviedEventService";

    void storeEvent(
            String eventId,
            String eventSource,
            String eventBody,
            String eventType,
            String apiVersion
    );
}