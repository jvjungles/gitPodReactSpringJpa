package com.jungles.rsjapp.web.rest;

import com.jungles.rsjapp.domain.Fera;
import com.jungles.rsjapp.repository.FeraRepository;
import com.jungles.rsjapp.service.FeraService;
import com.jungles.rsjapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.jungles.rsjapp.domain.Fera}.
 */
@RestController
@RequestMapping("/api")
public class FeraResource {

    private final Logger log = LoggerFactory.getLogger(FeraResource.class);

    private static final String ENTITY_NAME = "fera";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeraService feraService;

    private final FeraRepository feraRepository;

    public FeraResource(FeraService feraService, FeraRepository feraRepository) {
        this.feraService = feraService;
        this.feraRepository = feraRepository;
    }

    /**
     * {@code POST  /feras} : Create a new fera.
     *
     * @param fera the fera to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fera, or with status {@code 400 (Bad Request)} if the fera has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/feras")
    public ResponseEntity<Fera> createFera(@RequestBody Fera fera) throws URISyntaxException {
        log.debug("REST request to save Fera : {}", fera);
        if (fera.getId() != null) {
            throw new BadRequestAlertException("A new fera cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Fera result = feraService.save(fera);
        return ResponseEntity
            .created(new URI("/api/feras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /feras/:id} : Updates an existing fera.
     *
     * @param id the id of the fera to save.
     * @param fera the fera to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fera,
     * or with status {@code 400 (Bad Request)} if the fera is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fera couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/feras/{id}")
    public ResponseEntity<Fera> updateFera(@PathVariable(value = "id", required = false) final Long id, @RequestBody Fera fera)
        throws URISyntaxException {
        log.debug("REST request to update Fera : {}, {}", id, fera);
        if (fera.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fera.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Fera result = feraService.update(fera);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fera.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /feras/:id} : Partial updates given fields of an existing fera, field will ignore if it is null
     *
     * @param id the id of the fera to save.
     * @param fera the fera to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fera,
     * or with status {@code 400 (Bad Request)} if the fera is not valid,
     * or with status {@code 404 (Not Found)} if the fera is not found,
     * or with status {@code 500 (Internal Server Error)} if the fera couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/feras/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Fera> partialUpdateFera(@PathVariable(value = "id", required = false) final Long id, @RequestBody Fera fera)
        throws URISyntaxException {
        log.debug("REST request to partial update Fera partially : {}, {}", id, fera);
        if (fera.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fera.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Fera> result = feraService.partialUpdate(fera);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fera.getId().toString())
        );
    }

    /**
     * {@code GET  /feras} : get all the feras.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feras in body.
     */
    @GetMapping("/feras")
    public List<Fera> getAllFeras() {
        log.debug("REST request to get all Feras");
        return feraService.findAll();
    }

    /**
     * {@code GET  /feras/:id} : get the "id" fera.
     *
     * @param id the id of the fera to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fera, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/feras/{id}")
    public ResponseEntity<Fera> getFera(@PathVariable Long id) {
        log.debug("REST request to get Fera : {}", id);
        Optional<Fera> fera = feraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fera);
    }

    /**
     * {@code DELETE  /feras/:id} : delete the "id" fera.
     *
     * @param id the id of the fera to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/feras/{id}")
    public ResponseEntity<Void> deleteFera(@PathVariable Long id) {
        log.debug("REST request to delete Fera : {}", id);
        feraService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
