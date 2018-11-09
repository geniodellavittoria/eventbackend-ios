package ch.ios.eventapp.repository;

import ch.ios.eventapp.domain.UserEventRegistration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the UserEventRegistration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserEventRegistrationRepository extends JpaRepository<UserEventRegistration, Long> {

    @Query("select user_event_registration from UserEventRegistration user_event_registration where user_event_registration.userId.login = ?#{principal.username}")
    List<UserEventRegistration> findByUserIdIsCurrentUser();

}
