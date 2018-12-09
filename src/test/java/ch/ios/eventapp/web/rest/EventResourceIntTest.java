package ch.ios.eventapp.web.rest;

import ch.ios.eventapp.EventApp;

import ch.ios.eventapp.domain.Event;
import ch.ios.eventapp.repository.EventRepository;
import ch.ios.eventapp.service.PassService;
import ch.ios.eventapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static ch.ios.eventapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EventResource REST controller.
 *
 * @see EventResource
 */
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventApp.class)
public class EventResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_LOCATION_LONGITUDE = 1D;
    private static final Double UPDATED_LOCATION_LONGITUDE = 2D;

    private static final Double DEFAULT_LOCATION_LATITUDE = 1D;
    private static final Double UPDATED_LOCATION_LATITUDE = 2D;

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EVENT_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EVENT_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EVENT_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EVENT_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final byte[] DEFAULT_EVENT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_EVENT_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_EVENT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_EVENT_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEventMockMvc;

    private Event event;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EventResource eventResource = new EventResource(eventRepository, mock(PassService.class));
        this.restEventMockMvc = MockMvcBuilders.standaloneSetup(eventResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createEntity(EntityManager em) {
        Event event = new Event()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .locationLongitude(DEFAULT_LOCATION_LONGITUDE)
            .locationLatitude(DEFAULT_LOCATION_LATITUDE)
            .price(DEFAULT_PRICE)
            .timestamp(DEFAULT_TIMESTAMP)
            .eventStart(DEFAULT_EVENT_START)
            .eventEnd(DEFAULT_EVENT_END)
            .eventImage(DEFAULT_EVENT_IMAGE)
            .eventImageContentType(DEFAULT_EVENT_IMAGE_CONTENT_TYPE);
        return event;
    }

    @Before
    public void initTest() {
        event = createEntity(em);
    }

    @Test
    @Transactional
    public void createEvent() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // Create the Event
        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isCreated());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate + 1);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEvent.getLocationLongitude()).isEqualTo(DEFAULT_LOCATION_LONGITUDE);
        assertThat(testEvent.getLocationLatitude()).isEqualTo(DEFAULT_LOCATION_LATITUDE);
        assertThat(testEvent.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testEvent.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testEvent.getEventStart()).isEqualTo(DEFAULT_EVENT_START);
        assertThat(testEvent.getEventEnd()).isEqualTo(DEFAULT_EVENT_END);
        assertThat(testEvent.getEventImage()).isEqualTo(DEFAULT_EVENT_IMAGE);
        assertThat(testEvent.getEventImageContentType()).isEqualTo(DEFAULT_EVENT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // Create the Event with an existing ID
        event.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setName(null);

        // Create the Event, which fails.

        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setLocationLongitude(null);

        // Create the Event, which fails.

        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationLatitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setLocationLatitude(null);

        // Create the Event, which fails.

        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setTimestamp(null);

        // Create the Event, which fails.

        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEventStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setEventStart(null);

        // Create the Event, which fails.

        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEventEndIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setEventEnd(null);

        // Create the Event, which fails.

        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEvents() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList
        restEventMockMvc.perform(get("/api/events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].locationLongitude").value(hasItem(DEFAULT_LOCATION_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].locationLatitude").value(hasItem(DEFAULT_LOCATION_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].eventStart").value(hasItem(DEFAULT_EVENT_START.toString())))
            .andExpect(jsonPath("$.[*].eventEnd").value(hasItem(DEFAULT_EVENT_END.toString())))
            .andExpect(jsonPath("$.[*].eventImageContentType").value(hasItem(DEFAULT_EVENT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].eventImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_EVENT_IMAGE))));
    }
    
    @Test
    @Transactional
    public void getEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(event.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.locationLongitude").value(DEFAULT_LOCATION_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.locationLatitude").value(DEFAULT_LOCATION_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.eventStart").value(DEFAULT_EVENT_START.toString()))
            .andExpect(jsonPath("$.eventEnd").value(DEFAULT_EVENT_END.toString()))
            .andExpect(jsonPath("$.eventImageContentType").value(DEFAULT_EVENT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.eventImage").value(Base64Utils.encodeToString(DEFAULT_EVENT_IMAGE)));
    }

    @Test
    @Transactional
    public void getNonExistingEvent() throws Exception {
        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event
        Event updatedEvent = eventRepository.findById(event.getId()).get();
        // Disconnect from session so that the updates on updatedEvent are not directly saved in db
        em.detach(updatedEvent);
        updatedEvent
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .locationLongitude(UPDATED_LOCATION_LONGITUDE)
            .locationLatitude(UPDATED_LOCATION_LATITUDE)
            .price(UPDATED_PRICE)
            .timestamp(UPDATED_TIMESTAMP)
            .eventStart(UPDATED_EVENT_START)
            .eventEnd(UPDATED_EVENT_END)
            .eventImage(UPDATED_EVENT_IMAGE)
            .eventImageContentType(UPDATED_EVENT_IMAGE_CONTENT_TYPE);

        restEventMockMvc.perform(put("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEvent)))
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEvent.getLocationLongitude()).isEqualTo(UPDATED_LOCATION_LONGITUDE);
        assertThat(testEvent.getLocationLatitude()).isEqualTo(UPDATED_LOCATION_LATITUDE);
        assertThat(testEvent.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testEvent.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testEvent.getEventStart()).isEqualTo(UPDATED_EVENT_START);
        assertThat(testEvent.getEventEnd()).isEqualTo(UPDATED_EVENT_END);
        assertThat(testEvent.getEventImage()).isEqualTo(UPDATED_EVENT_IMAGE);
        assertThat(testEvent.getEventImageContentType()).isEqualTo(UPDATED_EVENT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Create the Event

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc.perform(put("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeDelete = eventRepository.findAll().size();

        // Get the event
        restEventMockMvc.perform(delete("/api/events/{id}", event.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Event.class);
        Event event1 = new Event();
        event1.setId(1L);
        Event event2 = new Event();
        event2.setId(event1.getId());
        assertThat(event1).isEqualTo(event2);
        event2.setId(2L);
        assertThat(event1).isNotEqualTo(event2);
        event1.setId(null);
        assertThat(event1).isNotEqualTo(event2);
    }
}
