package ch.ios.eventapp.service.dto;

import java.time.Instant;

public class UserEventRegistrationDTO {

    private Long userId;

    private Long eventRegistrationId;

    private Instant timestamp;

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
}
