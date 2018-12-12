package ch.ios.eventapp.web.rest;

import ch.ios.eventapp.service.dto.UserEventRegistrationDTO;
import com.codahale.metrics.annotation.Timed;
import ch.ios.eventapp.domain.UserEventRegistration;
import ch.ios.eventapp.repository.UserEventRegistrationRepository;
import ch.ios.eventapp.web.rest.errors.BadRequestAlertException;
import ch.ios.eventapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * REST controller for managing UserEventRegistration.
 */
@RestController
@RequestMapping("/api")
public class UserEventRegistrationResource {

    private final Logger log = LoggerFactory.getLogger(UserEventRegistrationResource.class);

    private static final String ENTITY_NAME = "userEventRegistration";

    private final UserEventRegistrationRepository userEventRegistrationRepository;

    public UserEventRegistrationResource(UserEventRegistrationRepository userEventRegistrationRepository) {
        this.userEventRegistrationRepository = userEventRegistrationRepository;
    }

    /**
     * POST  /user-event-registrations : Create a new userEventRegistration.
     *
     * @param userEventRegistration the userEventRegistration to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userEventRegistration, or with status 400 (Bad Request) if the userEventRegistration has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-event-registrations")
    @Timed
    public ResponseEntity<UserEventRegistration> createUserEventRegistration(@Valid @RequestBody UserEventRegistration userEventRegistration) throws URISyntaxException {
        log.debug("REST request to save UserEventRegistration : {}", userEventRegistration);
        if (userEventRegistration.getId() != null) {
            throw new BadRequestAlertException("A new userEventRegistration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserEventRegistration result = userEventRegistrationRepository.save(userEventRegistration);
        return ResponseEntity.created(new URI("/api/user-event-registrations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-event-registrations : Updates an existing userEventRegistration.
     *
     * @param userEventRegistration the userEventRegistration to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userEventRegistration,
     * or with status 400 (Bad Request) if the userEventRegistration is not valid,
     * or with status 500 (Internal Server Error) if the userEventRegistration couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-event-registrations")
    @Timed
    public ResponseEntity<UserEventRegistration> updateUserEventRegistration(@Valid @RequestBody UserEventRegistration userEventRegistration) throws URISyntaxException {
        log.debug("REST request to update UserEventRegistration : {}", userEventRegistration);
        if (userEventRegistration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserEventRegistration result = userEventRegistrationRepository.save(userEventRegistration);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userEventRegistration.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-event-registrations : get all the userEventRegistrations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userEventRegistrations in body
     */
    @GetMapping("/user-event-registrations")
    @Timed
    public List<UserEventRegistration> getAllUserEventRegistrations() {
        log.debug("REST request to get all UserEventRegistrations");
        return userEventRegistrationRepository.findAll();
    }

    @GetMapping("registrations")
    @Timed
    public List<UserEventRegistration> getEventRegistrations() {
        return userEventRegistrationRepository.findByUserIdIsCurrentUser();
    }

    @GetMapping("registrations/tagging")
    @Timed
    public List<UserEventRegistrationDTO> getTaggingEventRegistrations() {
        List<UserEventRegistration> eventRegistrations = userEventRegistrationRepository.findByUserIdIsCurrentUser();
        List<UserEventRegistrationDTO> userEventRegistrationDTOS = eventRegistrations.stream()
            .filter(e -> e.getRegistrationCategory().getId() == 2)
            .map(e -> {
                UserEventRegistrationDTO u = new UserEventRegistrationDTO();
                u.setTimestamp(e.getTimestamp());
                u.setEventRegistrationId(e.getRegistrationCategory().getId());
                u.setEventId(e.getEvent().getId());
                u.setUserId(e.getUserId().getId());
                return u;
            }).collect(toList());
        return userEventRegistrationDTOS;
    }

    @GetMapping("registrations/registration")
    @Timed
    public List<UserEventRegistrationDTO> getRegisterEventRegistrations() {
        List<UserEventRegistration> eventRegistrations = userEventRegistrationRepository.findByUserIdIsCurrentUser();
        List<UserEventRegistrationDTO> userEventRegistrationDTOS = eventRegistrations.stream()
            .filter(e -> e.getRegistrationCategory().getId() == 1)
            .map(e -> {
                UserEventRegistrationDTO u = new UserEventRegistrationDTO();
                u.setTimestamp(e.getTimestamp());
                u.setEventRegistrationId(e.getRegistrationCategory().getId());
                u.setEventId(e.getEvent().getId());
                u.setUserId(e.getUserId().getId());
                return u;
            }).collect(toList());
        return userEventRegistrationDTOS;
    }

    /**
     * GET  /user-event-registrations/:id : get the "id" userEventRegistration.
     *
     * @param id the id of the userEventRegistration to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userEventRegistration, or with status 404 (Not Found)
     */
    @GetMapping("/user-event-registrations/{id}")
    @Timed
    public ResponseEntity<UserEventRegistration> getUserEventRegistration(@PathVariable Long id) {
        log.debug("REST request to get UserEventRegistration : {}", id);
        Optional<UserEventRegistration> userEventRegistration = userEventRegistrationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userEventRegistration);
    }

    /**
     * DELETE  /user-event-registrations/:id : delete the "id" userEventRegistration.
     *
     * @param id the id of the userEventRegistration to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-event-registrations/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserEventRegistration(@PathVariable Long id) {
        log.debug("REST request to delete UserEventRegistration : {}", id);

        userEventRegistrationRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
