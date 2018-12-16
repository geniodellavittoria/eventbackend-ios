package ch.ios.eventapp.service.dto;

import java.time.Instant;

public class UserEventRegistrationDTO {

    private Long id;

    private Long userId;

    private Long eventRegistrationId;

    private Instant timestamp;

    private Long eventId;

    private int place;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEventRegistrationId() {
        return eventRegistrationId;
    }

    public void setEventRegistrationId(Long eventRegistrationId) {
        this.eventRegistrationId = eventRegistrationId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
