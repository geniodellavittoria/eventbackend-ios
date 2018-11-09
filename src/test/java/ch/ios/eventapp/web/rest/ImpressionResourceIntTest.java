package ch.ios.eventapp.web.rest;

import ch.ios.eventapp.EventApp;

import ch.ios.eventapp.domain.Impression;
import ch.ios.eventapp.repository.ImpressionRepository;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;


import static ch.ios.eventapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ImpressionResource REST controller.
 *
 * @see ImpressionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventApp.class)
public class ImpressionResourceIntTest {

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private ImpressionRepository impressionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restImpressionMockMvc;

    private Impression impression;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ImpressionResource impressionResource = new ImpressionResource(impressionRepository);
        this.restImpressionMockMvc = MockMvcBuilders.standaloneSetup(impressionResource)
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
    public static Impression createEntity(EntityManager em) {
        Impression impression = new Impression()
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return impression;
    }

    @Before
    public void initTest() {
        impression = createEntity(em);
    }

    @Test
    @Transactional
    public void createImpression() throws Exception {
        int databaseSizeBeforeCreate = impressionRepository.findAll().size();

        // Create the Impression
        restImpressionMockMvc.perform(post("/api/impressions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(impression)))
            .andExpect(status().isCreated());

        // Validate the Impression in the database
        List<Impression> impressionList = impressionRepository.findAll();
        assertThat(impressionList).hasSize(databaseSizeBeforeCreate + 1);
        Impression testImpression = impressionList.get(impressionList.size() - 1);
        assertThat(testImpression.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testImpression.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createImpressionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = impressionRepository.findAll().size();

        // Create the Impression with an existing ID
        impression.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restImpressionMockMvc.perform(post("/api/impressions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(impression)))
            .andExpect(status().isBadRequest());

        // Validate the Impression in the database
        List<Impression> impressionList = impressionRepository.findAll();
        assertThat(impressionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllImpressions() throws Exception {
        // Initialize the database
        impressionRepository.saveAndFlush(impression);

        // Get all the impressionList
        restImpressionMockMvc.perform(get("/api/impressions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(impression.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
    
    @Test
    @Transactional
    public void getImpression() throws Exception {
        // Initialize the database
        impressionRepository.saveAndFlush(impression);

        // Get the impression
        restImpressionMockMvc.perform(get("/api/impressions/{id}", impression.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(impression.getId().intValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    public void getNonExistingImpression() throws Exception {
        // Get the impression
        restImpressionMockMvc.perform(get("/api/impressions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateImpression() throws Exception {
        // Initialize the database
        impressionRepository.saveAndFlush(impression);

        int databaseSizeBeforeUpdate = impressionRepository.findAll().size();

        // Update the impression
        Impression updatedImpression = impressionRepository.findById(impression.getId()).get();
        // Disconnect from session so that the updates on updatedImpression are not directly saved in db
        em.detach(updatedImpression);
        updatedImpression
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restImpressionMockMvc.perform(put("/api/impressions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedImpression)))
            .andExpect(status().isOk());

        // Validate the Impression in the database
        List<Impression> impressionList = impressionRepository.findAll();
        assertThat(impressionList).hasSize(databaseSizeBeforeUpdate);
        Impression testImpression = impressionList.get(impressionList.size() - 1);
        assertThat(testImpression.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testImpression.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingImpression() throws Exception {
        int databaseSizeBeforeUpdate = impressionRepository.findAll().size();

        // Create the Impression

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImpressionMockMvc.perform(put("/api/impressions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(impression)))
            .andExpect(status().isBadRequest());

        // Validate the Impression in the database
        List<Impression> impressionList = impressionRepository.findAll();
        assertThat(impressionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteImpression() throws Exception {
        // Initialize the database
        impressionRepository.saveAndFlush(impression);

        int databaseSizeBeforeDelete = impressionRepository.findAll().size();

        // Get the impression
        restImpressionMockMvc.perform(delete("/api/impressions/{id}", impression.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Impression> impressionList = impressionRepository.findAll();
        assertThat(impressionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Impression.class);
        Impression impression1 = new Impression();
        impression1.setId(1L);
        Impression impression2 = new Impression();
        impression2.setId(impression1.getId());
        assertThat(impression1).isEqualTo(impression2);
        impression2.setId(2L);
        assertThat(impression1).isNotEqualTo(impression2);
        impression1.setId(null);
        assertThat(impression1).isNotEqualTo(impression2);
    }
}
