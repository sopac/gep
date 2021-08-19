package org.sopac.gem.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopac.gem.IntegrationTest;
import org.sopac.gem.domain.Proposal;
import org.sopac.gem.domain.enumeration.Currency;
import org.sopac.gem.domain.enumeration.Status;
import org.sopac.gem.repository.ProposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ProposalResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProposalResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.ACTIVE;
    private static final Status UPDATED_STATUS = Status.CLOSED;

    private static final Boolean DEFAULT_DIVSION_ENDORSEMENT = false;
    private static final Boolean UPDATED_DIVSION_ENDORSEMENT = true;

    private static final Boolean DEFAULT_PACIFIC_COMMUNITY_ENDORSEMENT = false;
    private static final Boolean UPDATED_PACIFIC_COMMUNITY_ENDORSEMENT = true;

    private static final Currency DEFAULT_TOTAL_BUDGET_CURRENCY = Currency.USD;
    private static final Currency UPDATED_TOTAL_BUDGET_CURRENCY = Currency.EUR;

    private static final Double DEFAULT_TOTAL_BUDGET = 1D;
    private static final Double UPDATED_TOTAL_BUDGET = 2D;

    private static final String DEFAULT_TOTAL_BUDGET_BREAKDOWN = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL_BUDGET_BREAKDOWN = "BBBBBBBBBB";

    private static final String DEFAULT_KEY_THEMATIC_AREAS = "AAAAAAAAAA";
    private static final String UPDATED_KEY_THEMATIC_AREAS = "BBBBBBBBBB";

    private static final String DEFAULT_LESSONS_LEARNT_BEST_PRACTICES = "AAAAAAAAAA";
    private static final String UPDATED_LESSONS_LEARNT_BEST_PRACTICES = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_COST_RECOVERY_COVERAGE = "AAAAAAAAAA";
    private static final String UPDATED_FULL_COST_RECOVERY_COVERAGE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/proposals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProposalRepository proposalRepository;

    @Mock
    private ProposalRepository proposalRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProposalMockMvc;

    private Proposal proposal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proposal createEntity(EntityManager em) {
        Proposal proposal = new Proposal()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .divsionEndorsement(DEFAULT_DIVSION_ENDORSEMENT)
            .pacificCommunityEndorsement(DEFAULT_PACIFIC_COMMUNITY_ENDORSEMENT)
            .totalBudgetCurrency(DEFAULT_TOTAL_BUDGET_CURRENCY)
            .totalBudget(DEFAULT_TOTAL_BUDGET)
            .totalBudgetBreakdown(DEFAULT_TOTAL_BUDGET_BREAKDOWN)
            .keyThematicAreas(DEFAULT_KEY_THEMATIC_AREAS)
            .lessonsLearntBestPractices(DEFAULT_LESSONS_LEARNT_BEST_PRACTICES)
            .fullCostRecoveryCoverage(DEFAULT_FULL_COST_RECOVERY_COVERAGE)
            .notes(DEFAULT_NOTES);
        return proposal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proposal createUpdatedEntity(EntityManager em) {
        Proposal proposal = new Proposal()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .divsionEndorsement(UPDATED_DIVSION_ENDORSEMENT)
            .pacificCommunityEndorsement(UPDATED_PACIFIC_COMMUNITY_ENDORSEMENT)
            .totalBudgetCurrency(UPDATED_TOTAL_BUDGET_CURRENCY)
            .totalBudget(UPDATED_TOTAL_BUDGET)
            .totalBudgetBreakdown(UPDATED_TOTAL_BUDGET_BREAKDOWN)
            .keyThematicAreas(UPDATED_KEY_THEMATIC_AREAS)
            .lessonsLearntBestPractices(UPDATED_LESSONS_LEARNT_BEST_PRACTICES)
            .fullCostRecoveryCoverage(UPDATED_FULL_COST_RECOVERY_COVERAGE)
            .notes(UPDATED_NOTES);
        return proposal;
    }

    @BeforeEach
    public void initTest() {
        proposal = createEntity(em);
    }

    @Test
    @Transactional
    void createProposal() throws Exception {
        int databaseSizeBeforeCreate = proposalRepository.findAll().size();
        // Create the Proposal
        restProposalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proposal)))
            .andExpect(status().isCreated());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeCreate + 1);
        Proposal testProposal = proposalList.get(proposalList.size() - 1);
        assertThat(testProposal.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testProposal.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProposal.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProposal.getDivsionEndorsement()).isEqualTo(DEFAULT_DIVSION_ENDORSEMENT);
        assertThat(testProposal.getPacificCommunityEndorsement()).isEqualTo(DEFAULT_PACIFIC_COMMUNITY_ENDORSEMENT);
        assertThat(testProposal.getTotalBudgetCurrency()).isEqualTo(DEFAULT_TOTAL_BUDGET_CURRENCY);
        assertThat(testProposal.getTotalBudget()).isEqualTo(DEFAULT_TOTAL_BUDGET);
        assertThat(testProposal.getTotalBudgetBreakdown()).isEqualTo(DEFAULT_TOTAL_BUDGET_BREAKDOWN);
        assertThat(testProposal.getKeyThematicAreas()).isEqualTo(DEFAULT_KEY_THEMATIC_AREAS);
        assertThat(testProposal.getLessonsLearntBestPractices()).isEqualTo(DEFAULT_LESSONS_LEARNT_BEST_PRACTICES);
        assertThat(testProposal.getFullCostRecoveryCoverage()).isEqualTo(DEFAULT_FULL_COST_RECOVERY_COVERAGE);
        assertThat(testProposal.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createProposalWithExistingId() throws Exception {
        // Create the Proposal with an existing ID
        proposal.setId(1L);

        int databaseSizeBeforeCreate = proposalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProposalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proposal)))
            .andExpect(status().isBadRequest());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = proposalRepository.findAll().size();
        // set the field null
        proposal.setTitle(null);

        // Create the Proposal, which fails.

        restProposalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proposal)))
            .andExpect(status().isBadRequest());

        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProposals() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList
        restProposalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposal.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].divsionEndorsement").value(hasItem(DEFAULT_DIVSION_ENDORSEMENT.booleanValue())))
            .andExpect(jsonPath("$.[*].pacificCommunityEndorsement").value(hasItem(DEFAULT_PACIFIC_COMMUNITY_ENDORSEMENT.booleanValue())))
            .andExpect(jsonPath("$.[*].totalBudgetCurrency").value(hasItem(DEFAULT_TOTAL_BUDGET_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].totalBudget").value(hasItem(DEFAULT_TOTAL_BUDGET.doubleValue())))
            .andExpect(jsonPath("$.[*].totalBudgetBreakdown").value(hasItem(DEFAULT_TOTAL_BUDGET_BREAKDOWN.toString())))
            .andExpect(jsonPath("$.[*].keyThematicAreas").value(hasItem(DEFAULT_KEY_THEMATIC_AREAS)))
            .andExpect(jsonPath("$.[*].lessonsLearntBestPractices").value(hasItem(DEFAULT_LESSONS_LEARNT_BEST_PRACTICES.toString())))
            .andExpect(jsonPath("$.[*].fullCostRecoveryCoverage").value(hasItem(DEFAULT_FULL_COST_RECOVERY_COVERAGE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProposalsWithEagerRelationshipsIsEnabled() throws Exception {
        when(proposalRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProposalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(proposalRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProposalsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(proposalRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProposalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(proposalRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getProposal() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get the proposal
        restProposalMockMvc
            .perform(get(ENTITY_API_URL_ID, proposal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proposal.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.divsionEndorsement").value(DEFAULT_DIVSION_ENDORSEMENT.booleanValue()))
            .andExpect(jsonPath("$.pacificCommunityEndorsement").value(DEFAULT_PACIFIC_COMMUNITY_ENDORSEMENT.booleanValue()))
            .andExpect(jsonPath("$.totalBudgetCurrency").value(DEFAULT_TOTAL_BUDGET_CURRENCY.toString()))
            .andExpect(jsonPath("$.totalBudget").value(DEFAULT_TOTAL_BUDGET.doubleValue()))
            .andExpect(jsonPath("$.totalBudgetBreakdown").value(DEFAULT_TOTAL_BUDGET_BREAKDOWN.toString()))
            .andExpect(jsonPath("$.keyThematicAreas").value(DEFAULT_KEY_THEMATIC_AREAS))
            .andExpect(jsonPath("$.lessonsLearntBestPractices").value(DEFAULT_LESSONS_LEARNT_BEST_PRACTICES.toString()))
            .andExpect(jsonPath("$.fullCostRecoveryCoverage").value(DEFAULT_FULL_COST_RECOVERY_COVERAGE))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProposal() throws Exception {
        // Get the proposal
        restProposalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProposal() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();

        // Update the proposal
        Proposal updatedProposal = proposalRepository.findById(proposal.getId()).get();
        // Disconnect from session so that the updates on updatedProposal are not directly saved in db
        em.detach(updatedProposal);
        updatedProposal
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .divsionEndorsement(UPDATED_DIVSION_ENDORSEMENT)
            .pacificCommunityEndorsement(UPDATED_PACIFIC_COMMUNITY_ENDORSEMENT)
            .totalBudgetCurrency(UPDATED_TOTAL_BUDGET_CURRENCY)
            .totalBudget(UPDATED_TOTAL_BUDGET)
            .totalBudgetBreakdown(UPDATED_TOTAL_BUDGET_BREAKDOWN)
            .keyThematicAreas(UPDATED_KEY_THEMATIC_AREAS)
            .lessonsLearntBestPractices(UPDATED_LESSONS_LEARNT_BEST_PRACTICES)
            .fullCostRecoveryCoverage(UPDATED_FULL_COST_RECOVERY_COVERAGE)
            .notes(UPDATED_NOTES);

        restProposalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProposal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProposal))
            )
            .andExpect(status().isOk());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
        Proposal testProposal = proposalList.get(proposalList.size() - 1);
        assertThat(testProposal.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testProposal.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProposal.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProposal.getDivsionEndorsement()).isEqualTo(UPDATED_DIVSION_ENDORSEMENT);
        assertThat(testProposal.getPacificCommunityEndorsement()).isEqualTo(UPDATED_PACIFIC_COMMUNITY_ENDORSEMENT);
        assertThat(testProposal.getTotalBudgetCurrency()).isEqualTo(UPDATED_TOTAL_BUDGET_CURRENCY);
        assertThat(testProposal.getTotalBudget()).isEqualTo(UPDATED_TOTAL_BUDGET);
        assertThat(testProposal.getTotalBudgetBreakdown()).isEqualTo(UPDATED_TOTAL_BUDGET_BREAKDOWN);
        assertThat(testProposal.getKeyThematicAreas()).isEqualTo(UPDATED_KEY_THEMATIC_AREAS);
        assertThat(testProposal.getLessonsLearntBestPractices()).isEqualTo(UPDATED_LESSONS_LEARNT_BEST_PRACTICES);
        assertThat(testProposal.getFullCostRecoveryCoverage()).isEqualTo(UPDATED_FULL_COST_RECOVERY_COVERAGE);
        assertThat(testProposal.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingProposal() throws Exception {
        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();
        proposal.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proposal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proposal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProposal() throws Exception {
        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();
        proposal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proposal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProposal() throws Exception {
        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();
        proposal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proposal)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProposalWithPatch() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();

        // Update the proposal using partial update
        Proposal partialUpdatedProposal = new Proposal();
        partialUpdatedProposal.setId(proposal.getId());

        partialUpdatedProposal
            .totalBudget(UPDATED_TOTAL_BUDGET)
            .totalBudgetBreakdown(UPDATED_TOTAL_BUDGET_BREAKDOWN)
            .keyThematicAreas(UPDATED_KEY_THEMATIC_AREAS)
            .notes(UPDATED_NOTES);

        restProposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProposal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProposal))
            )
            .andExpect(status().isOk());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
        Proposal testProposal = proposalList.get(proposalList.size() - 1);
        assertThat(testProposal.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testProposal.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProposal.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProposal.getDivsionEndorsement()).isEqualTo(DEFAULT_DIVSION_ENDORSEMENT);
        assertThat(testProposal.getPacificCommunityEndorsement()).isEqualTo(DEFAULT_PACIFIC_COMMUNITY_ENDORSEMENT);
        assertThat(testProposal.getTotalBudgetCurrency()).isEqualTo(DEFAULT_TOTAL_BUDGET_CURRENCY);
        assertThat(testProposal.getTotalBudget()).isEqualTo(UPDATED_TOTAL_BUDGET);
        assertThat(testProposal.getTotalBudgetBreakdown()).isEqualTo(UPDATED_TOTAL_BUDGET_BREAKDOWN);
        assertThat(testProposal.getKeyThematicAreas()).isEqualTo(UPDATED_KEY_THEMATIC_AREAS);
        assertThat(testProposal.getLessonsLearntBestPractices()).isEqualTo(DEFAULT_LESSONS_LEARNT_BEST_PRACTICES);
        assertThat(testProposal.getFullCostRecoveryCoverage()).isEqualTo(DEFAULT_FULL_COST_RECOVERY_COVERAGE);
        assertThat(testProposal.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void fullUpdateProposalWithPatch() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();

        // Update the proposal using partial update
        Proposal partialUpdatedProposal = new Proposal();
        partialUpdatedProposal.setId(proposal.getId());

        partialUpdatedProposal
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .divsionEndorsement(UPDATED_DIVSION_ENDORSEMENT)
            .pacificCommunityEndorsement(UPDATED_PACIFIC_COMMUNITY_ENDORSEMENT)
            .totalBudgetCurrency(UPDATED_TOTAL_BUDGET_CURRENCY)
            .totalBudget(UPDATED_TOTAL_BUDGET)
            .totalBudgetBreakdown(UPDATED_TOTAL_BUDGET_BREAKDOWN)
            .keyThematicAreas(UPDATED_KEY_THEMATIC_AREAS)
            .lessonsLearntBestPractices(UPDATED_LESSONS_LEARNT_BEST_PRACTICES)
            .fullCostRecoveryCoverage(UPDATED_FULL_COST_RECOVERY_COVERAGE)
            .notes(UPDATED_NOTES);

        restProposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProposal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProposal))
            )
            .andExpect(status().isOk());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
        Proposal testProposal = proposalList.get(proposalList.size() - 1);
        assertThat(testProposal.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testProposal.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProposal.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProposal.getDivsionEndorsement()).isEqualTo(UPDATED_DIVSION_ENDORSEMENT);
        assertThat(testProposal.getPacificCommunityEndorsement()).isEqualTo(UPDATED_PACIFIC_COMMUNITY_ENDORSEMENT);
        assertThat(testProposal.getTotalBudgetCurrency()).isEqualTo(UPDATED_TOTAL_BUDGET_CURRENCY);
        assertThat(testProposal.getTotalBudget()).isEqualTo(UPDATED_TOTAL_BUDGET);
        assertThat(testProposal.getTotalBudgetBreakdown()).isEqualTo(UPDATED_TOTAL_BUDGET_BREAKDOWN);
        assertThat(testProposal.getKeyThematicAreas()).isEqualTo(UPDATED_KEY_THEMATIC_AREAS);
        assertThat(testProposal.getLessonsLearntBestPractices()).isEqualTo(UPDATED_LESSONS_LEARNT_BEST_PRACTICES);
        assertThat(testProposal.getFullCostRecoveryCoverage()).isEqualTo(UPDATED_FULL_COST_RECOVERY_COVERAGE);
        assertThat(testProposal.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingProposal() throws Exception {
        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();
        proposal.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, proposal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proposal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProposal() throws Exception {
        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();
        proposal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proposal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProposal() throws Exception {
        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();
        proposal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(proposal)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProposal() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        int databaseSizeBeforeDelete = proposalRepository.findAll().size();

        // Delete the proposal
        restProposalMockMvc
            .perform(delete(ENTITY_API_URL_ID, proposal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
