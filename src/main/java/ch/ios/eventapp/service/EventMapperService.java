package ch.ios.eventapp.service;

import ch.ios.eventapp.domain.*;
import ch.ios.eventapp.repository.*;
import ch.ios.eventapp.security.SecurityUtils;
import ch.ios.eventapp.service.dto.EventForm;
import ch.ios.eventapp.service.dto.UserEventRegistrationDTO;
import ch.ios.eventapp.web.rest.EventResource;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ch.ios.eventapp.security.SecurityUtils.getCurrentUserLogin;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Service
public class EventMapperService {

    private final Logger log = LoggerFactory.getLogger(EventMapperService.class);

    @Autowired
    private UserEventRegistrationRepository userEventRegistrationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RegistrationCategoryRepository registrationCategoryRepository;

    public EventForm mapToEventForm(Event event) {
        Optional<String> loginName = getCurrentUserLogin();
        if (!loginName.isPresent()) {
            return null;
        }

        EventForm eventForm = new EventForm();
        eventForm.setId(event.getId());
        eventForm.setCategory(event.getCategory());
        eventForm.setImpression(event.getImpression());
        eventForm.setEventEnd(event.getEventEnd());
        eventForm.setEventStart(event.getEventStart());
        eventForm.setEventImage(event.getEventImage());
        eventForm.setEventImageContentType(event.getEventImageContentType());
        eventForm.setLocationLatitude(event.getLocationLatitude());
        eventForm.setLocationLongitude(event.getLocationLongitude());
        eventForm.setName(event.getName());
        eventForm.setPrice(event.getPrice());
        eventForm.setTimestamp(event.getTimestamp());
        eventForm.setDescription(event.getDescription());
        eventForm.setPlace(event.getPlace());

        Set<UserEventRegistrationDTO> eventRegistrations = event.getUserEventRegistrationIds().stream()
            .map(EventMapperService::mapToUserEventRegistrationDTO).collect(Collectors.toSet());

        if (event.getPlace() >= 0) {
            int freePlace = eventRegistrations.stream().mapToInt(e -> {
                if (e.getPlace() == null) {
                    return 1;
                } else {
                    return e.getPlace();
                }
            }).sum();
            eventForm.setFreePlace(event.getPlace() - freePlace);
        } else {
            eventForm.setFreePlace(null);
        }
        eventForm.setEventRegistrations(eventRegistrations);
        if (event.getUserId() != null) {
            eventForm.setUserId(event.getUserId().getId());
        }
        return eventForm;
    }

    public Event mapToEvent(EventForm eventForm) {
        Event event = new Event();
        event.setId(eventForm.getId());
        event.setCategory(eventForm.getCategory());
        event.setDescription(eventForm.getDescription());
        event.setEventEnd(eventForm.getEventEnd());
        event.setEventStart(eventForm.getEventStart());
        event.setEventImage(eventForm.getEventImage());
        event.setEventImageContentType(eventForm.getEventImageContentType());
        event.setImpression(eventForm.getImpression());
        event.setLocationLatitude(eventForm.getLocationLatitude());
        event.setLocationLongitude(eventForm.getLocationLongitude());
        event.setName(eventForm.getName());
        event.setPrice(eventForm.getPrice());
        event.setTimestamp(eventForm.getTimestamp());
        event.setPlace(eventForm.getPlace());

        Set<UserEventRegistration> eventRegistrations = eventForm.getEventRegistrations().stream()
            .map(this::mapToUserEventRegistration)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet());
        event.setUserEventRegistrationIds(eventRegistrations);
        return event;
    }

    public Optional<UserEventRegistration> mapToUserEventRegistration(UserEventRegistrationDTO eventRegistrationDTO) {
        Optional<String> loginName = getCurrentUserLogin();
        if (!loginName.isPresent()) {
            return empty();
        }
        UserEventRegistration eventRegistration = new UserEventRegistration();
        Optional<Event> event = eventRepository.findById(eventRegistrationDTO.getEventId());
        Optional<User> user = userRepository.findOneByLogin(loginName.get());
        Optional<RegistrationCategory> category = registrationCategoryRepository.findById(eventRegistrationDTO.getEventRegistrationId());
        if (!event.isPresent() || !category.isPresent() || !user.isPresent()) {
            return empty();
        }
        eventRegistration.setId(eventRegistrationDTO.getId());
        eventRegistration.setUserId(user.get());
        eventRegistration.setEvent(event.get());
        eventRegistration.setRegistrationCategory(category.get());
        eventRegistration.setTimestamp(eventRegistrationDTO.getTimestamp());
        return of(eventRegistration);
    }

    public static UserEventRegistrationDTO mapToUserEventRegistrationDTO(UserEventRegistration eventRegistration) {
        UserEventRegistrationDTO eventRegistrationDTO = new UserEventRegistrationDTO();
        eventRegistrationDTO.setId(eventRegistration.getId());
        eventRegistrationDTO.setEventRegistrationId(eventRegistration.getRegistrationCategory().getId());
        eventRegistrationDTO.setUserId(eventRegistration.getUserId().getId());
        eventRegistrationDTO.setEventId(eventRegistration.getEvent().getId());
        eventRegistrationDTO.setTimestamp(eventRegistration.getTimestamp());
        return eventRegistrationDTO;
    }

}
