package ch.ios.eventapp.repository;

import ch.ios.eventapp.domain.Impression;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Impression entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImpressionRepository extends JpaRepository<Impression, Long> {

}
