package ch.ios.eventapp.web.rest;

import ch.ios.eventapp.EventApp;

import ch.ios.eventapp.domain.UserEventRegistration;
import ch.ios.eventapp.repository.UserEventRegistrationRepository;
import ch.ios.eventapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
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

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static ch.ios.eventapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserEventRegistrationResource REST controller.
 *
 * @see UserEventRegistrationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventApp.class)
public class UserEventRegistrationResourceIntTest {

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private UserEventRegistrationRepository userEventRegistrationRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserEventRegistrationMockMvc;

    private UserEventRegistration userEventRegistration;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserEventRegistrationResource userEventRegistrationResource = new UserEventRegistrationResource(userEventRegistrationRepository);
        this.restUserEventRegistrationMockMvc = MockMvcBuilders.standaloneSetup(userEventRegistrationResource)
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
    public static UserEventRegistration createEntity(EntityManager em) {
        UserEventRegistration userEventRegistration = new UserEventRegistration()
            .timestamp(DEFAULT_TIMESTAMP);
        return userEventRegistration;
    }

    @Before
    public void initTest() {
        userEventRegistration = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserEventRegistration() throws Exception {
        int databaseSizeBeforeCreate = userEventRegistrationRepository.findAll().size();

        // Create the UserEventRegistration
        restUserEventRegistrationMockMvc.perform(post("/api/user-event-registrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userEventRegistration)))
            .andExpect(status().isCreated());

        // Validate the UserEventRegistration in the database
        List<UserEventRegistration> userEventRegistrationList = userEventRegistrationRepository.findAll();
        assertThat(userEventRegistrationList).hasSize(databaseSizeBeforeCreate + 1);
        UserEventRegistration testUserEventRegistration = userEventRegistrationList.get(userEventRegistrationList.size() - 1);
        assertThat(testUserEventRegistration.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
    }

    @Test
    @Transactional
    public void createUserEventRegistrationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userEventRegistrationRepository.findAll().size();

        // Create the UserEventRegistration with an existing ID
        userEventRegistration.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserEventRegistrationMockMvc.perform(post("/api/user-event-registrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userEventRegistration)))
            .andExpect(status().isBadRequest());

        // Validate the UserEventRegistration in the database
        List<UserEventRegistration> userEventRegistrationList = userEventRegistrationRepository.findAll();
        assertThat(userEventRegistrationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = userEventRegistrationRepository.findAll().size();
        // set the field null
        userEventRegistration.setTimestamp(null);

        // Create the UserEventRegistration, which fails.

        restUserEventRegistrationMockMvc.perform(post("/api/user-event-registrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userEventRegistration)))
            .andExpect(status().isBadRequest());

        List<UserEventRegistration> userEventRegistrationList = userEventRegistrationRepository.findAll();
        assertThat(userEventRegistrationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserEventRegistrations() throws Exception {
        // Initialize the database
        userEventRegistrationRepository.saveAndFlush(userEventRegistration);

        // Get all the userEventRegistrationList
        restUserEventRegistrationMockMvc.perform(get("/api/user-event-registrations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userEventRegistration.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));
    }
    
    @Test
    @Transactional
    public void getUserEventRegistration() throws Exception {
        // Initialize the database
        userEventRegistrationRepository.saveAndFlush(userEventRegistration);

        // Get the userEventRegistration
        restUserEventRegistrationMockMvc.perform(get("/api/user-event-registrations/{id}", userEventRegistration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userEventRegistration.getId().intValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserEventRegistration() throws Exception {
        // Get the userEventRegistration
        restUserEventRegistrationMockMvc.perform(get("/api/user-event-registrations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserEventRegistration() throws Exception {
        // Initialize the database
        userEventRegistrationRepository.saveAndFlush(userEventRegistration);

        int databaseSizeBeforeUpdate = userEventRegistrationRepository.findAll().size();

        // Update the userEventRegistration
        UserEventRegistration updatedUserEventRegistration = userEventRegistrationRepository.findById(userEventRegistration.getId()).get();
        // Disconnect from session so that the updates on updatedUserEventRegistration are not directly saved in db
        em.detach(updatedUserEventRegistration);
        updatedUserEventRegistration
            .timestamp(UPDATED_TIMESTAMP);

        restUserEventRegistrationMockMvc.perform(put("/api/user-event-registrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserEventRegistration)))
            .andExpect(status().isOk());

        // Validate the UserEventRegistration in the database
        List<UserEventRegistration> userEventRegistrationList = userEventRegistrationRepository.findAll();
        assertThat(userEventRegistrationList).hasSize(databaseSizeBeforeUpdate);
        UserEventRegistration testUserEventRegistration = userEventRegistrationList.get(userEventRegistrationList.size() - 1);
        assertThat(testUserEventRegistration.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void updateNonExistingUserEventRegistration() throws Exception {
        int databaseSizeBeforeUpdate = userEventRegistrationRepository.findAll().size();

        // Create the UserEventRegistration

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserEventRegistrationMockMvc.perform(put("/api/user-event-registrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userEventRegistration)))
            .andExpect(status().isBadRequest());

        // Validate the UserEventRegistration in the database
        List<UserEventRegistration> userEventRegistrationList = userEventRegistrationRepository.findAll();
        assertThat(userEventRegistrationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserEventRegistration() throws Exception {
        // Initialize the database
        userEventRegistrationRepository.saveAndFlush(userEventRegistration);

        int databaseSizeBeforeDelete = userEventRegistrationRepository.findAll().size();

        // Get the userEventRegistration
        restUserEventRegistrationMockMvc.perform(delete("/api/user-event-registrations/{id}", userEventRegistration.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserEventRegistration> userEventRegistrationList = userEventRegistrationRepository.findAll();
        assertThat(userEventRegistrationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserEventRegistration.class);
        UserEventRegistration userEventRegistration1 = new UserEventRegistration();
        userEventRegistration1.setId(1L);
        UserEventRegistration userEventRegistration2 = new UserEventRegistration();
        userEventRegistration2.setId(userEventRegistration1.getId());
        assertThat(userEventRegistration1).isEqualTo(userEventRegistration2);
        userEventRegistration2.setId(2L);
        assertThat(userEventRegistration1).isNotEqualTo(userEventRegistration2);
        userEventRegistration1.setId(null);
        assertThat(userEventRegistration1).isNotEqualTo(userEventRegistration2);
    }
}
