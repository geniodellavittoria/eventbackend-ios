package ch.ios.eventapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import ch.ios.eventapp.domain.Impression;
import ch.ios.eventapp.repository.ImpressionRepository;
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
 * REST controller for managing Impression.
 */
@RestController
@RequestMapping("/api")
public class ImpressionResource {

    private final Logger log = LoggerFactory.getLogger(ImpressionResource.class);

    private static final String ENTITY_NAME = "impression";

    private final ImpressionRepository impressionRepository;

    public ImpressionResource(ImpressionRepository impressionRepository) {
        this.impressionRepository = impressionRepository;
    }

    /**
     * POST  /impressions : Create a new impression.
     *
     * @param impression the impression to create
     * @return the ResponseEntity with status 201 (Created) and with body the new impression, or with status 400 (Bad Request) if the impression has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/impressions")
    @Timed
    public ResponseEntity<Impression> createImpression(@Valid @RequestBody Impression impression) throws URISyntaxException {
        log.debug("REST request to save Impression : {}", impression);
        if (impression.getId() != null) {
            throw new BadRequestAlertException("A new impression cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Impression result = impressionRepository.save(impression);
        return ResponseEntity.created(new URI("/api/impressions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /impressions : Updates an existing impression.
     *
     * @param impression the impression to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated impression,
     * or with status 400 (Bad Request) if the impression is not valid,
     * or with status 500 (Internal Server Error) if the impression couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/impressions")
    @Timed
    public ResponseEntity<Impression> updateImpression(@Valid @RequestBody Impression impression) throws URISyntaxException {
        log.debug("REST request to update Impression : {}", impression);
        if (impression.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Impression result = impressionRepository.save(impression);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, impression.getId().toString()))
            .body(result);
    }

    /**
     * GET  /impressions : get all the impressions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of impressions in body
     */
    @GetMapping("/impressions")
    @Timed
    public List<Impression> getAllImpressions() {
        log.debug("REST request to get all Impressions");
        return impressionRepository.findAll();
    }

    /**
     * GET  /impressions/:id : get the "id" impression.
     *
     * @param id the id of the impression to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the impression, or with status 404 (Not Found)
     */
    @GetMapping("/impressions/{id}")
    @Timed
    public ResponseEntity<Impression> getImpression(@PathVariable Long id) {
        log.debug("REST request to get Impression : {}", id);
        Optional<Impression> impression = impressionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(impression);
    }

    /**
     * DELETE  /impressions/:id : delete the "id" impression.
     *
     * @param id the id of the impression to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/impressions/{id}")
    @Timed
    public ResponseEntity<Void> deleteImpression(@PathVariable Long id) {
        log.debug("REST request to delete Impression : {}", id);

        impressionRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
