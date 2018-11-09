package ch.ios.eventapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import ch.ios.eventapp.domain.RegistrationCategory;
import ch.ios.eventapp.repository.RegistrationCategoryRepository;
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

/**
 * REST controller for managing RegistrationCategory.
 */
@RestController
@RequestMapping("/api")
public class RegistrationCategoryResource {

    private final Logger log = LoggerFactory.getLogger(RegistrationCategoryResource.class);

    private static final String ENTITY_NAME = "registrationCategory";

    private final RegistrationCategoryRepository registrationCategoryRepository;

    public RegistrationCategoryResource(RegistrationCategoryRepository registrationCategoryRepository) {
        this.registrationCategoryRepository = registrationCategoryRepository;
    }

    /**
     * POST  /registration-categories : Create a new registrationCategory.
     *
     * @param registrationCategory the registrationCategory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new registrationCategory, or with status 400 (Bad Request) if the registrationCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/registration-categories")
    @Timed
    public ResponseEntity<RegistrationCategory> createRegistrationCategory(@Valid @RequestBody RegistrationCategory registrationCategory) throws URISyntaxException {
        log.debug("REST request to save RegistrationCategory : {}", registrationCategory);
        if (registrationCategory.getId() != null) {
            throw new BadRequestAlertException("A new registrationCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RegistrationCategory result = registrationCategoryRepository.save(registrationCategory);
        return ResponseEntity.created(new URI("/api/registration-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /registration-categories : Updates an existing registrationCategory.
     *
     * @param registrationCategory the registrationCategory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated registrationCategory,
     * or with status 400 (Bad Request) if the registrationCategory is not valid,
     * or with status 500 (Internal Server Error) if the registrationCategory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/registration-categories")
    @Timed
    public ResponseEntity<RegistrationCategory> updateRegistrationCategory(@Valid @RequestBody RegistrationCategory registrationCategory) throws URISyntaxException {
        log.debug("REST request to update RegistrationCategory : {}", registrationCategory);
        if (registrationCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RegistrationCategory result = registrationCategoryRepository.save(registrationCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, registrationCategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /registration-categories : get all the registrationCategories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of registrationCategories in body
     */
    @GetMapping("/registration-categories")
    @Timed
    public List<RegistrationCategory> getAllRegistrationCategories() {
        log.debug("REST request to get all RegistrationCategories");
        return registrationCategoryRepository.findAll();
    }

    /**
     * GET  /registration-categories/:id : get the "id" registrationCategory.
     *
     * @param id the id of the registrationCategory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the registrationCategory, or with status 404 (Not Found)
     */
    @GetMapping("/registration-categories/{id}")
    @Timed
    public ResponseEntity<RegistrationCategory> getRegistrationCategory(@PathVariable Long id) {
        log.debug("REST request to get RegistrationCategory : {}", id);
        Optional<RegistrationCategory> registrationCategory = registrationCategoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(registrationCategory);
    }

    /**
     * DELETE  /registration-categories/:id : delete the "id" registrationCategory.
     *
     * @param id the id of the registrationCategory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/registration-categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteRegistrationCategory(@PathVariable Long id) {
        log.debug("REST request to delete RegistrationCategory : {}", id);

        registrationCategoryRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
