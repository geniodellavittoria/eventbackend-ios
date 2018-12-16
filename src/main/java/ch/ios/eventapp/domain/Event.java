package ch.ios.eventapp.domain;

import ch.ios.eventapp.service.dto.EventForm;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import static javax.persistence.FetchType.EAGER;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "location_longitude", nullable = false)
    private Double locationLongitude;

    @NotNull
    @Column(name = "location_latitude", nullable = false)
    private Double locationLatitude;

    @Column(name = "price")
    private Float price;

    @NotNull
    @Column(name = "jhi_timestamp", nullable = false)
    private Instant timestamp;

    @NotNull
    @Column(name = "event_start", nullable = false)
    private Instant eventStart;

    @NotNull
    @Column(name = "event_end", nullable = false)
    private Instant eventEnd;

    @Lob
    @Column(name = "event_image")
    private byte[] eventImage;

    @Column(name = "event_image_content_type")
    private String eventImageContentType;

    @ManyToOne
    @JsonIgnoreProperties("ids")
    private Category category;

    @OneToMany(mappedBy = "event", fetch = EAGER)
    private Set<UserEventRegistration> userEventRegistrationIds = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("")
    private User userId;

    @ManyToOne
    @JsonIgnoreProperties("eventIds")
    private Impression impression;

    @Column(name = "place")
    private Integer place;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Event name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Event description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLocationLongitude() {
        return locationLongitude;
    }

    public Event locationLongitude(Double locationLongitude) {
        this.locationLongitude = locationLongitude;
        return this;
    }

    public void setLocationLongitude(Double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public Double getLocationLatitude() {
        return locationLatitude;
    }

    public Event locationLatitude(Double locationLatitude) {
        this.locationLatitude = locationLatitude;
        return this;
    }

    public void setLocationLatitude(Double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public Float getPrice() {
        return price;
    }

    public Event price(Float price) {
        this.price = price;
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Event timestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Instant getEventStart() {
        return eventStart;
    }

    public Event eventStart(Instant eventStart) {
        this.eventStart = eventStart;
        return this;
    }

    public void setEventStart(Instant eventStart) {
        this.eventStart = eventStart;
    }

    public Instant getEventEnd() {
        return eventEnd;
    }

    public Event eventEnd(Instant eventEnd) {
        this.eventEnd = eventEnd;
        return this;
    }

    public void setEventEnd(Instant eventEnd) {
        this.eventEnd = eventEnd;
    }

    public byte[] getEventImage() {
        return eventImage;
    }

    public Event eventImage(byte[] eventImage) {
        this.eventImage = eventImage;
        return this;
    }

    public void setEventImage(byte[] eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventImageContentType() {
        return eventImageContentType;
    }

    public Event eventImageContentType(String eventImageContentType) {
        this.eventImageContentType = eventImageContentType;
        return this;
    }

    public void setEventImageContentType(String eventImageContentType) {
        this.eventImageContentType = eventImageContentType;
    }

    public Category getCategory() {
        return category;
    }

    public Event category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<UserEventRegistration> getUserEventRegistrationIds() {
        return userEventRegistrationIds;
    }

    public Event userEventRegistrationIds(Set<UserEventRegistration> userEventRegistrations) {
        this.userEventRegistrationIds = userEventRegistrations;
        return this;
    }

    public Event addUserEventRegistrationId(UserEventRegistration userEventRegistration) {
        this.userEventRegistrationIds.add(userEventRegistration);
        userEventRegistration.setEvent(this);
        return this;
    }

    public Event removeUserEventRegistrationId(UserEventRegistration userEventRegistration) {
        this.userEventRegistrationIds.remove(userEventRegistration);
        userEventRegistration.setEvent(null);
        return this;
    }

    public void setUserEventRegistrationIds(Set<UserEventRegistration> userEventRegistrations) {
        this.userEventRegistrationIds = userEventRegistrations;
    }

    public User getUserId() {
        return userId;
    }

    public Event userId(User user) {
        this.userId = user;
        return this;
    }

    public void setUserId(User user) {
        this.userId = user;
    }

    public Impression getImpression() {
        return impression;
    }

    public Event impression(Impression impression) {
        this.impression = impression;
        return this;
    }

    public void setImpression(Impression impression) {
        this.impression = impression;
    }

    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
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
        Event event = (Event) o;
        if (event.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), event.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", locationLongitude=" + getLocationLongitude() +
            ", locationLatitude=" + getLocationLatitude() +
            ", price=" + getPrice() +
            ", timestamp='" + getTimestamp() + "'" +
            ", eventStart='" + getEventStart() + "'" +
            ", eventEnd='" + getEventEnd() + "'" +
            ", eventImage='" + getEventImage() + "'" +
            ", eventImageContentType='" + getEventImageContentType() + "'" +
            "}";
    }
}
