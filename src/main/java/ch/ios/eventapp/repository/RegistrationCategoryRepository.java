package ch.ios.eventapp.repository;

import ch.ios.eventapp.domain.RegistrationCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RegistrationCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegistrationCategoryRepository extends JpaRepository<RegistrationCategory, Long> {

}
