package ch.ios.eventapp.web.rest;

import ch.ios.eventapp.EventApp;

import ch.ios.eventapp.domain.RegistrationCategory;
import ch.ios.eventapp.repository.RegistrationCategoryRepository;
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
import java.util.List;


import static ch.ios.eventapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RegistrationCategoryResource REST controller.
 *
 * @see RegistrationCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventApp.class)
public class RegistrationCategoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private RegistrationCategoryRepository registrationCategoryRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRegistrationCategoryMockMvc;

    private RegistrationCategory registrationCategory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RegistrationCategoryResource registrationCategoryResource = new RegistrationCategoryResource(registrationCategoryRepository);
        this.restRegistrationCategoryMockMvc = MockMvcBuilders.standaloneSetup(registrationCategoryResource)
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
    public static RegistrationCategory createEntity(EntityManager em) {
        RegistrationCategory registrationCategory = new RegistrationCategory()
            .name(DEFAULT_NAME);
        return registrationCategory;
    }

    @Before
    public void initTest() {
        registrationCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegistrationCategory() throws Exception {
        int databaseSizeBeforeCreate = registrationCategoryRepository.findAll().size();

        // Create the RegistrationCategory
        restRegistrationCategoryMockMvc.perform(post("/api/registration-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registrationCategory)))
            .andExpect(status().isCreated());

        // Validate the RegistrationCategory in the database
        List<RegistrationCategory> registrationCategoryList = registrationCategoryRepository.findAll();
        assertThat(registrationCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        RegistrationCategory testRegistrationCategory = registrationCategoryList.get(registrationCategoryList.size() - 1);
        assertThat(testRegistrationCategory.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createRegistrationCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = registrationCategoryRepository.findAll().size();

        // Create the RegistrationCategory with an existing ID
        registrationCategory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegistrationCategoryMockMvc.perform(post("/api/registration-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registrationCategory)))
            .andExpect(status().isBadRequest());

        // Validate the RegistrationCategory in the database
        List<RegistrationCategory> registrationCategoryList = registrationCategoryRepository.findAll();
        assertThat(registrationCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = registrationCategoryRepository.findAll().size();
        // set the field null
        registrationCategory.setName(null);

        // Create the RegistrationCategory, which fails.

        restRegistrationCategoryMockMvc.perform(post("/api/registration-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registrationCategory)))
            .andExpect(status().isBadRequest());

        List<RegistrationCategory> registrationCategoryList = registrationCategoryRepository.findAll();
        assertThat(registrationCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRegistrationCategories() throws Exception {
        // Initialize the database
        registrationCategoryRepository.saveAndFlush(registrationCategory);

        // Get all the registrationCategoryList
        restRegistrationCategoryMockMvc.perform(get("/api/registration-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registrationCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getRegistrationCategory() throws Exception {
        // Initialize the database
        registrationCategoryRepository.saveAndFlush(registrationCategory);

        // Get the registrationCategory
        restRegistrationCategoryMockMvc.perform(get("/api/registration-categories/{id}", registrationCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(registrationCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRegistrationCategory() throws Exception {
        // Get the registrationCategory
        restRegistrationCategoryMockMvc.perform(get("/api/registration-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegistrationCategory() throws Exception {
        // Initialize the database
        registrationCategoryRepository.saveAndFlush(registrationCategory);

        int databaseSizeBeforeUpdate = registrationCategoryRepository.findAll().size();

        // Update the registrationCategory
        RegistrationCategory updatedRegistrationCategory = registrationCategoryRepository.findById(registrationCategory.getId()).get();
        // Disconnect from session so that the updates on updatedRegistrationCategory are not directly saved in db
        em.detach(updatedRegistrationCategory);
        updatedRegistrationCategory
            .name(UPDATED_NAME);

        restRegistrationCategoryMockMvc.perform(put("/api/registration-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRegistrationCategory)))
            .andExpect(status().isOk());

        // Validate the RegistrationCategory in the database
        List<RegistrationCategory> registrationCategoryList = registrationCategoryRepository.findAll();
        assertThat(registrationCategoryList).hasSize(databaseSizeBeforeUpdate);
        RegistrationCategory testRegistrationCategory = registrationCategoryList.get(registrationCategoryList.size() - 1);
        assertThat(testRegistrationCategory.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingRegistrationCategory() throws Exception {
        int databaseSizeBeforeUpdate = registrationCategoryRepository.findAll().size();

        // Create the RegistrationCategory

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegistrationCategoryMockMvc.perform(put("/api/registration-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registrationCategory)))
            .andExpect(status().isBadRequest());

        // Validate the RegistrationCategory in the database
        List<RegistrationCategory> registrationCategoryList = registrationCategoryRepository.findAll();
        assertThat(registrationCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRegistrationCategory() throws Exception {
        // Initialize the database
        registrationCategoryRepository.saveAndFlush(registrationCategory);

        int databaseSizeBeforeDelete = registrationCategoryRepository.findAll().size();

        // Get the registrationCategory
        restRegistrationCategoryMockMvc.perform(delete("/api/registration-categories/{id}", registrationCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RegistrationCategory> registrationCategoryList = registrationCategoryRepository.findAll();
        assertThat(registrationCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegistrationCategory.class);
        RegistrationCategory registrationCategory1 = new RegistrationCategory();
        registrationCategory1.setId(1L);
        RegistrationCategory registrationCategory2 = new RegistrationCategory();
        registrationCategory2.setId(registrationCategory1.getId());
        assertThat(registrationCategory1).isEqualTo(registrationCategory2);
        registrationCategory2.setId(2L);
        assertThat(registrationCategory1).isNotEqualTo(registrationCategory2);
        registrationCategory1.setId(null);
        assertThat(registrationCategory1).isNotEqualTo(registrationCategory2);
    }
}
