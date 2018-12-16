package ch.ios.eventapp.service.dto;

import ch.ios.eventapp.domain.Category;
import ch.ios.eventapp.domain.Impression;
import ch.ios.eventapp.domain.User;
import ch.ios.eventapp.domain.UserEventRegistration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class EventForm {
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
    private Instant timestamp;

    @NotNull
    private Instant eventStart;

    private Instant eventEnd;

    private byte[] eventImage;

    private String eventImageContentType;

    private Category category;

    private Long userId;

    private Impression impression;

    private Integer  place;

    private Integer freePlace;

    private Set<UserEventRegistrationDTO> eventRegistrations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(Double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public Double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(Double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Instant getEventStart() {
        return eventStart;
    }

    public void setEventStart(Instant eventStart) {
        this.eventStart = eventStart;
    }

    public Instant getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(Instant eventEnd) {
        this.eventEnd = eventEnd;
    }

    public byte[] getEventImage() {
        return eventImage;
    }

    public void setEventImage(byte[] eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventImageContentType() {
        return eventImageContentType;
    }

    public void setEventImageContentType(String eventImageContentType) {
        this.eventImageContentType = eventImageContentType;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Impression getImpression() {
        return impression;
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

    public Integer getFreePlace() {
        return freePlace;
    }

    public void setFreePlace(Integer freePlace) {
        this.freePlace = freePlace;
    }

    public Set<UserEventRegistrationDTO> getEventRegistrations() {
        return eventRegistrations;
    }

    public void setEventRegistrations(Set<UserEventRegistrationDTO> eventRegistrations) {
        this.eventRegistrations = eventRegistrations;
    }
}
