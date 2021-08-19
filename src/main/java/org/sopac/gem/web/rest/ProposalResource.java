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
import org.sopac.gem.domain.Proposal;
import org.sopac.gem.repository.ProposalRepository;
import org.sopac.gem.web.rest.errors.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.sopac.gem.domain.Proposal}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProposalResource {

    private final Logger log = LoggerFactory.getLogger(ProposalResource.class);

    private static final String ENTITY_NAME = "proposal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProposalRepository proposalRepository;

    public ProposalResource(ProposalRepository proposalRepository) {
        this.proposalRepository = proposalRepository;
    }

    /**
     * {@code POST  /proposals} : Create a new proposal.
     *
     * @param proposal the proposal to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new proposal, or with status {@code 400 (Bad Request)} if the proposal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/proposals")
    public ResponseEntity<Proposal> createProposal(@Valid @RequestBody Proposal proposal) throws URISyntaxException {
        log.debug("REST request to save Proposal : {}", proposal);
        if (proposal.getId() != null) {
            throw new BadRequestAlertException("A new proposal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Proposal result = proposalRepository.save(proposal);
        return ResponseEntity
            .created(new URI("/api/proposals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /proposals/:id} : Updates an existing proposal.
     *
     * @param id the id of the proposal to save.
     * @param proposal the proposal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proposal,
     * or with status {@code 400 (Bad Request)} if the proposal is not valid,
     * or with status {@code 500 (Internal Server Error)} if the proposal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/proposals/{id}")
    public ResponseEntity<Proposal> updateProposal(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Proposal proposal
    ) throws URISyntaxException {
        log.debug("REST request to update Proposal : {}, {}", id, proposal);
        if (proposal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proposal.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!proposalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Proposal result = proposalRepository.save(proposal);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, proposal.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /proposals/:id} : Partial updates given fields of an existing proposal, field will ignore if it is null
     *
     * @param id the id of the proposal to save.
     * @param proposal the proposal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proposal,
     * or with status {@code 400 (Bad Request)} if the proposal is not valid,
     * or with status {@code 404 (Not Found)} if the proposal is not found,
     * or with status {@code 500 (Internal Server Error)} if the proposal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/proposals/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Proposal> partialUpdateProposal(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Proposal proposal
    ) throws URISyntaxException {
        log.debug("REST request to partial update Proposal partially : {}, {}", id, proposal);
        if (proposal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proposal.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!proposalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Proposal> result = proposalRepository
            .findById(proposal.getId())
            .map(
                existingProposal -> {
                    if (proposal.getTitle() != null) {
                        existingProposal.setTitle(proposal.getTitle());
                    }
                    if (proposal.getDescription() != null) {
                        existingProposal.setDescription(proposal.getDescription());
                    }
                    if (proposal.getStatus() != null) {
                        existingProposal.setStatus(proposal.getStatus());
                    }
                    if (proposal.getDivsionEndorsement() != null) {
                        existingProposal.setDivsionEndorsement(proposal.getDivsionEndorsement());
                    }
                    if (proposal.getPacificCommunityEndorsement() != null) {
                        existingProposal.setPacificCommunityEndorsement(proposal.getPacificCommunityEndorsement());
                    }
                    if (proposal.getTotalBudgetCurrency() != null) {
                        existingProposal.setTotalBudgetCurrency(proposal.getTotalBudgetCurrency());
                    }
                    if (proposal.getTotalBudget() != null) {
                        existingProposal.setTotalBudget(proposal.getTotalBudget());
                    }
                    if (proposal.getTotalBudgetBreakdown() != null) {
                        existingProposal.setTotalBudgetBreakdown(proposal.getTotalBudgetBreakdown());
                    }
                    if (proposal.getKeyThematicAreas() != null) {
                        existingProposal.setKeyThematicAreas(proposal.getKeyThematicAreas());
                    }
                    if (proposal.getLessonsLearntBestPractices() != null) {
                        existingProposal.setLessonsLearntBestPractices(proposal.getLessonsLearntBestPractices());
                    }
                    if (proposal.getFullCostRecoveryCoverage() != null) {
                        existingProposal.setFullCostRecoveryCoverage(proposal.getFullCostRecoveryCoverage());
                    }
                    if (proposal.getNotes() != null) {
                        existingProposal.setNotes(proposal.getNotes());
                    }

                    return existingProposal;
                }
            )
            .map(proposalRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, proposal.getId().toString())
        );
    }

    /**
     * {@code GET  /proposals} : get all the proposals.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of proposals in body.
     */
    @GetMapping("/proposals")
    public List<Proposal> getAllProposals(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Proposals");
        return proposalRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /proposals/:id} : get the "id" proposal.
     *
     * @param id the id of the proposal to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the proposal, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/proposals/{id}")
    public ResponseEntity<Proposal> getProposal(@PathVariable Long id) {
        log.debug("REST request to get Proposal : {}", id);
        Optional<Proposal> proposal = proposalRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(proposal);
    }

    /**
     * {@code DELETE  /proposals/:id} : delete the "id" proposal.
     *
     * @param id the id of the proposal to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/proposals/{id}")
    public ResponseEntity<Void> deleteProposal(@PathVariable Long id) {
        log.debug("REST request to delete Proposal : {}", id);
        proposalRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
