package de.diedavids.cuba.ccsm.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "CCSM_RECEIVED_EVENT")
@Entity(name = "ccsm_ReceivedEvent")
public class ReceivedEvent extends StandardEntity {
    private static final long serialVersionUID = 7309458491039114206L;

    @NotNull
    @Column(name = "TYPE_", nullable = false)
    protected String type;

    @Lob
    @NotNull
    @Column(name = "CONTENT", nullable = false)
    protected String content;

    @NotNull
    @Column(name = "EVENT_ID", nullable = false)
    protected String eventId;

    @Column(name = "SOURCE")
    protected String source;

    @Column(name = "API_VERSION")
    protected String apiVersion;

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}