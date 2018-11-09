package ch.ios.eventapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A UserEventRegistration.
 */
@Entity
@Table(name = "user_event_registration")
public class UserEventRegistration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_timestamp", nullable = false)
    private Instant timestamp;

    @ManyToOne
    @JsonIgnoreProperties("userEventRegistrationIds")
    private RegistrationCategory registrationCategory;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User userId;

    @ManyToOne
    @JsonIgnoreProperties("userEventRegistrationIds")
    private Event event;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public UserEventRegistration timestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public RegistrationCategory getRegistrationCategory() {
        return registrationCategory;
    }

    public UserEventRegistration registrationCategory(RegistrationCategory registrationCategory) {
        this.registrationCategory = registrationCategory;
        return this;
    }

    public void setRegistrationCategory(RegistrationCategory registrationCategory) {
        this.registrationCategory = registrationCategory;
    }

    public User getUserId() {
        return userId;
    }

    public UserEventRegistration userId(User user) {
        this.userId = user;
        return this;
    }

    public void setUserId(User user) {
        this.userId = user;
    }

    public Event getEvent() {
        return event;
    }

    public UserEventRegistration event(Event event) {
        this.event = event;
        return this;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserEventRegistration userEventRegistration = (UserEventRegistration) o;
        if (userEventRegistration.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userEventRegistration.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserEventRegistration{" +
            "id=" + getId() +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }
}
