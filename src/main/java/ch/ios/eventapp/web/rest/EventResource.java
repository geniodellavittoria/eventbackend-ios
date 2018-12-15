package ch.ios.eventapp.web.rest;

import ch.ios.eventapp.domain.RegistrationCategory;
import ch.ios.eventapp.domain.User;
import ch.ios.eventapp.domain.UserEventRegistration;
import ch.ios.eventapp.repository.*;
import ch.ios.eventapp.service.dto.EventForm;
import ch.ios.eventapp.service.dto.UserEventRegistrationDTO;
import com.codahale.metrics.annotation.Timed;
import ch.ios.eventapp.domain.Event;
import ch.ios.eventapp.web.rest.errors.BadRequestAlertException;
import ch.ios.eventapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ch.ios.eventapp.domain.Event.mapToEvent;
import static java.time.Instant.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

/**
 * REST controller for managing Event.
 */
@RestController
@RequestMapping("/api")
public class EventResource {

    private final Logger log = LoggerFactory.getLogger(EventResource.class);

    private static final String ENTITY_NAME = "event";

    private final EventRepository eventRepository;

    private final UserEventRegistrationRepository userEventRegistrationRepository;

    @Autowired
    private RegistrationCategoryRepository registrationCategoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public EventResource(EventRepository eventRepository, UserEventRegistrationRepository userEventRegistrationRepository) {
        this.eventRepository = eventRepository;
        this.userEventRegistrationRepository = userEventRegistrationRepository;
    }

    /**
     * POST  /events : Create a new event.
     *
     * @param event the event to create
     * @return the ResponseEntity with status 201 (Created) and with body the new event, or with status 400 (Bad Request) if the event has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/events")
    @Timed
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) throws URISyntaxException {
        log.debug("REST request to save Event : {}", event);
        if (event.getId() != null) {
            throw new BadRequestAlertException("A new event cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Event result = eventRepository.save(event);
        return ResponseEntity.created(new URI("/api/events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /events : Updates an existing event.
     *
     * @param event the event to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated event,
     * or with status 400 (Bad Request) if the event is not valid,
     * or with status 500 (Internal Server Error) if the event couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/events")
    @Timed
    public ResponseEntity<Event> updateEvent(@Valid @RequestBody Event event) throws URISyntaxException {
        log.debug("REST request to update Event : {}", event);
        if (event.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Event result = eventRepository.save(event);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, event.getId().toString()))
            .body(result);
    }

    @PutMapping("/event")
    @Timed
    public ResponseEntity<Event> updateEventForm(@Valid @RequestBody EventForm event) throws URISyntaxException {
        log.debug("REST request to update Event : {}", event);
        if (event.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Optional<User> user = userRepository.findById(event.getUserId());
        if (!user.isPresent()) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        Event eventModel = mapToEvent(event);
        eventModel.userId(user.get());
        Event result = eventRepository.save(eventModel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, event.getId().toString()))
            .body(result);
    }

    /**
     * GET  /events : get all the events.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of events in body
     */
    @GetMapping("/events")
    @Timed
    public List<EventForm> getAllEvents() {
        log.debug("REST request to get all Events");
        List<Event> events = eventRepository.findAll();
        List<UserEventRegistration> eventRegistrations = userEventRegistrationRepository.findAll();
        List<EventForm> eventForms =  events.stream().map(Event::mapToEventForm).collect(Collectors.toList());

        /*eventForms.stream().map(e -> {
            Optional<UserEventRegistration> userEventRegistration = userEventRegistrationRepository.findAllByEventId(e.getId());

        })*/
        return eventForms;
    }

    /**
     * GET  /events/:id : get the "id" event.
     *
     * @param id the id of the event to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the event, or with status 404 (Not Found)
     */
    @GetMapping("/events/{id}")
    @Timed
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        log.debug("REST request to get Event : {}", id);
        Optional<Event> event = eventRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(event);
    }

    @PostMapping("/events/{id}/register")
    @Timed
    public ResponseEntity<UserEventRegistration> registerEvent(@PathVariable Long id,
                                                               @RequestBody UserEventRegistrationDTO eventRegistration) {
        log.debug("Registering for Event {}", id);

        UserEventRegistration userEventRegistration = new UserEventRegistration();
        Optional<RegistrationCategory> registrationCategory = registrationCategoryRepository
            .findById(eventRegistration.getEventRegistrationId());
        Optional<Event> event = eventRepository.findById(id);
        Optional<User> user = userRepository.findById(eventRegistration.getUserId());

        if (!event.isPresent() || !user.isPresent() || !registrationCategory.isPresent()) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        userEventRegistration.setUserId(user.get());
        userEventRegistration.setRegistrationCategory(registrationCategory.get());
        userEventRegistration.setEvent(event.get());

        if (eventRegistration.getTimestamp() == null) {
            userEventRegistration.setTimestamp(now());
        } else {
            userEventRegistration.setTimestamp(eventRegistration.getTimestamp());
        }
        UserEventRegistration savedEventRegistration= userEventRegistrationRepository
            .save(userEventRegistration);
        return new ResponseEntity<>(savedEventRegistration, OK);
    }

    @DeleteMapping("/events/{id}/unregister")
    @Timed
    public ResponseEntity<Void> unregisterEvent(@PathVariable Long id) {
        log.debug("Unregister for Event id");
        List<UserEventRegistration> events = userEventRegistrationRepository.findByUserIdIsCurrentUser();
        events.stream().filter(e -> e.getEvent().getId().equals(id))
            .findAny()
            .ifPresent(userEventRegistrationRepository::delete);
        return new ResponseEntity<>(OK);
    }

    /**
     * DELETE  /events/:id : delete the "id" event.
     *
     * @param id the id of the event to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/events/{id}")
    @Timed
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        log.debug("REST request to delete Event : {}", id);

        eventRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
