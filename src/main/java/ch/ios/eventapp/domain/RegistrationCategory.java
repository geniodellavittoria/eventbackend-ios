package ch.ios.eventapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A RegistrationCategory.
 */
@Entity
@Table(name = "registration_category")
public class RegistrationCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @OneToMany(mappedBy = "registrationCategory")
    private Set<UserEventRegistration> userEventRegistrationIds = new HashSet<>();
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

    public RegistrationCategory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserEventRegistration> getUserEventRegistrationIds() {
        return userEventRegistrationIds;
    }

    public RegistrationCategory userEventRegistrationIds(Set<UserEventRegistration> userEventRegistrations) {
        this.userEventRegistrationIds = userEventRegistrations;
        return this;
    }

    public RegistrationCategory addUserEventRegistrationId(UserEventRegistration userEventRegistration) {
        this.userEventRegistrationIds.add(userEventRegistration);
        userEventRegistration.setRegistrationCategory(this);
        return this;
    }

    public RegistrationCategory removeUserEventRegistrationId(UserEventRegistration userEventRegistration) {
        this.userEventRegistrationIds.remove(userEventRegistration);
        userEventRegistration.setRegistrationCategory(null);
        return this;
    }

    public void setUserEventRegistrationIds(Set<UserEventRegistration> userEventRegistrations) {
        this.userEventRegistrationIds = userEventRegistrations;
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
        RegistrationCategory registrationCategory = (RegistrationCategory) o;
        if (registrationCategory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), registrationCategory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RegistrationCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
