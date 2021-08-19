package org.sopac.gem.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopac.gem.domain.Donor;
import org.sopac.gem.repository.DonorRepository;
import org.sopac.gem.web.rest.errors.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.sopac.gem.domain.Donor}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DonorResource {

    private final Logger log = LoggerFactory.getLogger(DonorResource.class);

    private static final String ENTITY_NAME = "donor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DonorRepository donorRepository;

    public DonorResource(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    /**
     * {@code POST  /donors} : Create a new donor.
     *
     * @param donor the donor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new donor, or with status {@code 400 (Bad Request)} if the donor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/donors")
    public ResponseEntity<Donor> createDonor(@Valid @RequestBody Donor donor) throws URISyntaxException {
        log.debug("REST request to save Donor : {}", donor);
        if (donor.getId() != null) {
            throw new BadRequestAlertException("A new donor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Donor result = donorRepository.save(donor);
        return ResponseEntity
            .created(new URI("/api/donors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /donors/:id} : Updates an existing donor.
     *
     * @param id the id of the donor to save.
     * @param donor the donor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated donor,
     * or with status {@code 400 (Bad Request)} if the donor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the donor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/donors/{id}")
    public ResponseEntity<Donor> updateDonor(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Donor donor)
        throws URISyntaxException {
        log.debug("REST request to update Donor : {}, {}", id, donor);
        if (donor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, donor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!donorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Donor result = donorRepository.save(donor);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, donor.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /donors/:id} : Partial updates given fields of an existing donor, field will ignore if it is null
     *
     * @param id the id of the donor to save.
     * @param donor the donor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated donor,
     * or with status {@code 400 (Bad Request)} if the donor is not valid,
     * or with status {@code 404 (Not Found)} if the donor is not found,
     * or with status {@code 500 (Internal Server Error)} if the donor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/donors/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Donor> partialUpdateDonor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Donor donor
    ) throws URISyntaxException {
        log.debug("REST request to partial update Donor partially : {}, {}", id, donor);
        if (donor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, donor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!donorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Donor> result = donorRepository
            .findById(donor.getId())
            .map(
                existingDonor -> {
                    if (donor.getName() != null) {
                        existingDonor.setName(donor.getName());
                    }
                    if (donor.getDescription() != null) {
                        existingDonor.setDescription(donor.getDescription());
                    }
                    if (donor.getUrl() != null) {
                        existingDonor.setUrl(donor.getUrl());
                    }
                    if (donor.getCity() != null) {
                        existingDonor.setCity(donor.getCity());
                    }
                    if (donor.getDonorCategory() != null) {
                        existingDonor.setDonorCategory(donor.getDonorCategory());
                    }
                    if (donor.getSector() != null) {
                        existingDonor.setSector(donor.getSector());
                    }
                    if (donor.getKeyContact() != null) {
                        existingDonor.setKeyContact(donor.getKeyContact());
                    }

                    return existingDonor;
                }
            )
            .map(donorRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, donor.getId().toString())
        );
    }

    /**
     * {@code GET  /donors} : get all the donors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of donors in body.
     */
    @GetMapping("/donors")
    public List<Donor> getAllDonors() {
        log.debug("REST request to get all Donors");
        return donorRepository.findAll();
    }

    /**
     * {@code GET  /donors/:id} : get the "id" donor.
     *
     * @param id the id of the donor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the donor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/donors/{id}")
    public ResponseEntity<Donor> getDonor(@PathVariable Long id) {
        log.debug("REST request to get Donor : {}", id);
        Optional<Donor> donor = donorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(donor);
    }

    /**
     * {@code DELETE  /donors/:id} : delete the "id" donor.
     *
     * @param id the id of the donor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/donors/{id}")
    public ResponseEntity<Void> deleteDonor(@PathVariable Long id) {
        log.debug("REST request to delete Donor : {}", id);
        donorRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
