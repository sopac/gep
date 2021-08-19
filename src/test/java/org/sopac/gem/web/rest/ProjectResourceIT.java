package org.sopac.gem.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
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
import org.sopac.gem.domain.Project;
import org.sopac.gem.domain.enumeration.Currency;
import org.sopac.gem.domain.enumeration.Status;
import org.sopac.gem.repository.ProjectRepository;
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
 * Integration tests for the {@link ProjectResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProjectResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Status DEFAULT_STATUS = Status.ACTIVE;
    private static final Status UPDATED_STATUS = Status.CLOSED;

    private static final Currency DEFAULT_TOTAL_BUDGET_CURRENCY = Currency.USD;
    private static final Currency UPDATED_TOTAL_BUDGET_CURRENCY = Currency.EUR;

    private static final Double DEFAULT_TOTAL_BUDGET = 1D;
    private static final Double UPDATED_TOTAL_BUDGET = 2D;

    private static final String DEFAULT_TOTAL_BUDGET_BREAKDOWN = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL_BUDGET_BREAKDOWN = "BBBBBBBBBB";

    private static final String DEFAULT_SUSTAINABLE_DEVELOPMENT_GOAL = "AAAAAAAAAA";
    private static final String UPDATED_SUSTAINABLE_DEVELOPMENT_GOAL = "BBBBBBBBBB";

    private static final String DEFAULT_FRAMEWORK_RESILIENT_DEVELOPMENT_PACIFIC = "AAAAAAAAAA";
    private static final String UPDATED_FRAMEWORK_RESILIENT_DEVELOPMENT_PACIFIC = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_COST_RECOVERY_COVERAGE = "AAAAAAAAAA";
    private static final String UPDATED_FULL_COST_RECOVERY_COVERAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/projects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProjectRepository projectRepository;

    @Mock
    private ProjectRepository projectRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjectMockMvc;

    private Project project;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createEntity(EntityManager em) {
        Project project = new Project()
            .title(DEFAULT_TITLE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .status(DEFAULT_STATUS)
            .totalBudgetCurrency(DEFAULT_TOTAL_BUDGET_CURRENCY)
            .totalBudget(DEFAULT_TOTAL_BUDGET)
            .totalBudgetBreakdown(DEFAULT_TOTAL_BUDGET_BREAKDOWN)
            .sustainableDevelopmentGoal(DEFAULT_SUSTAINABLE_DEVELOPMENT_GOAL)
            .frameworkResilientDevelopmentPacific(DEFAULT_FRAMEWORK_RESILIENT_DEVELOPMENT_PACIFIC)
            .fullCostRecoveryCoverage(DEFAULT_FULL_COST_RECOVERY_COVERAGE);
        return project;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createUpdatedEntity(EntityManager em) {
        Project project = new Project()
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .totalBudgetCurrency(UPDATED_TOTAL_BUDGET_CURRENCY)
            .totalBudget(UPDATED_TOTAL_BUDGET)
            .totalBudgetBreakdown(UPDATED_TOTAL_BUDGET_BREAKDOWN)
            .sustainableDevelopmentGoal(UPDATED_SUSTAINABLE_DEVELOPMENT_GOAL)
            .frameworkResilientDevelopmentPacific(UPDATED_FRAMEWORK_RESILIENT_DEVELOPMENT_PACIFIC)
            .fullCostRecoveryCoverage(UPDATED_FULL_COST_RECOVERY_COVERAGE);
        return project;
    }

    @BeforeEach
    public void initTest() {
        project = createEntity(em);
    }

    @Test
    @Transactional
    void createProject() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();
        // Create the Project
        restProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isCreated());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate + 1);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testProject.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testProject.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testProject.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProject.getTotalBudgetCurrency()).isEqualTo(DEFAULT_TOTAL_BUDGET_CURRENCY);
        assertThat(testProject.getTotalBudget()).isEqualTo(DEFAULT_TOTAL_BUDGET);
        assertThat(testProject.getTotalBudgetBreakdown()).isEqualTo(DEFAULT_TOTAL_BUDGET_BREAKDOWN);
        assertThat(testProject.getSustainableDevelopmentGoal()).isEqualTo(DEFAULT_SUSTAINABLE_DEVELOPMENT_GOAL);
        assertThat(testProject.getFrameworkResilientDevelopmentPacific()).isEqualTo(DEFAULT_FRAMEWORK_RESILIENT_DEVELOPMENT_PACIFIC);
        assertThat(testProject.getFullCostRecoveryCoverage()).isEqualTo(DEFAULT_FULL_COST_RECOVERY_COVERAGE);
    }

    @Test
    @Transactional
    void createProjectWithExistingId() throws Exception {
        // Create the Project with an existing ID
        project.setId(1L);

        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectRepository.findAll().size();
        // set the field null
        project.setTitle(null);

        // Create the Project, which fails.

        restProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProjects() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].totalBudgetCurrency").value(hasItem(DEFAULT_TOTAL_BUDGET_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].totalBudget").value(hasItem(DEFAULT_TOTAL_BUDGET.doubleValue())))
            .andExpect(jsonPath("$.[*].totalBudgetBreakdown").value(hasItem(DEFAULT_TOTAL_BUDGET_BREAKDOWN.toString())))
            .andExpect(jsonPath("$.[*].sustainableDevelopmentGoal").value(hasItem(DEFAULT_SUSTAINABLE_DEVELOPMENT_GOAL)))
            .andExpect(
                jsonPath("$.[*].frameworkResilientDevelopmentPacific").value(hasItem(DEFAULT_FRAMEWORK_RESILIENT_DEVELOPMENT_PACIFIC))
            )
            .andExpect(jsonPath("$.[*].fullCostRecoveryCoverage").value(hasItem(DEFAULT_FULL_COST_RECOVERY_COVERAGE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProjectsWithEagerRelationshipsIsEnabled() throws Exception {
        when(projectRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProjectMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(projectRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProjectsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(projectRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProjectMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(projectRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get the project
        restProjectMockMvc
            .perform(get(ENTITY_API_URL_ID, project.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(project.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.totalBudgetCurrency").value(DEFAULT_TOTAL_BUDGET_CURRENCY.toString()))
            .andExpect(jsonPath("$.totalBudget").value(DEFAULT_TOTAL_BUDGET.doubleValue()))
            .andExpect(jsonPath("$.totalBudgetBreakdown").value(DEFAULT_TOTAL_BUDGET_BREAKDOWN.toString()))
            .andExpect(jsonPath("$.sustainableDevelopmentGoal").value(DEFAULT_SUSTAINABLE_DEVELOPMENT_GOAL))
            .andExpect(jsonPath("$.frameworkResilientDevelopmentPacific").value(DEFAULT_FRAMEWORK_RESILIENT_DEVELOPMENT_PACIFIC))
            .andExpect(jsonPath("$.fullCostRecoveryCoverage").value(DEFAULT_FULL_COST_RECOVERY_COVERAGE));
    }

    @Test
    @Transactional
    void getNonExistingProject() throws Exception {
        // Get the project
        restProjectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project
        Project updatedProject = projectRepository.findById(project.getId()).get();
        // Disconnect from session so that the updates on updatedProject are not directly saved in db
        em.detach(updatedProject);
        updatedProject
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .totalBudgetCurrency(UPDATED_TOTAL_BUDGET_CURRENCY)
            .totalBudget(UPDATED_TOTAL_BUDGET)
            .totalBudgetBreakdown(UPDATED_TOTAL_BUDGET_BREAKDOWN)
            .sustainableDevelopmentGoal(UPDATED_SUSTAINABLE_DEVELOPMENT_GOAL)
            .frameworkResilientDevelopmentPacific(UPDATED_FRAMEWORK_RESILIENT_DEVELOPMENT_PACIFIC)
            .fullCostRecoveryCoverage(UPDATED_FULL_COST_RECOVERY_COVERAGE);

        restProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProject.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProject))
            )
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testProject.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testProject.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testProject.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProject.getTotalBudgetCurrency()).isEqualTo(UPDATED_TOTAL_BUDGET_CURRENCY);
        assertThat(testProject.getTotalBudget()).isEqualTo(UPDATED_TOTAL_BUDGET);
        assertThat(testProject.getTotalBudgetBreakdown()).isEqualTo(UPDATED_TOTAL_BUDGET_BREAKDOWN);
        assertThat(testProject.getSustainableDevelopmentGoal()).isEqualTo(UPDATED_SUSTAINABLE_DEVELOPMENT_GOAL);
        assertThat(testProject.getFrameworkResilientDevelopmentPacific()).isEqualTo(UPDATED_FRAMEWORK_RESILIENT_DEVELOPMENT_PACIFIC);
        assertThat(testProject.getFullCostRecoveryCoverage()).isEqualTo(UPDATED_FULL_COST_RECOVERY_COVERAGE);
    }

    @Test
    @Transactional
    void putNonExistingProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, project.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(project))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(project))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProjectWithPatch() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project using partial update
        Project partialUpdatedProject = new Project();
        partialUpdatedProject.setId(project.getId());

        partialUpdatedProject
            .title(UPDATED_TITLE)
            .status(UPDATED_STATUS)
            .totalBudgetCurrency(UPDATED_TOTAL_BUDGET_CURRENCY)
            .fullCostRecoveryCoverage(UPDATED_FULL_COST_RECOVERY_COVERAGE);

        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProject))
            )
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testProject.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testProject.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testProject.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProject.getTotalBudgetCurrency()).isEqualTo(UPDATED_TOTAL_BUDGET_CURRENCY);
        assertThat(testProject.getTotalBudget()).isEqualTo(DEFAULT_TOTAL_BUDGET);
        assertThat(testProject.getTotalBudgetBreakdown()).isEqualTo(DEFAULT_TOTAL_BUDGET_BREAKDOWN);
        assertThat(testProject.getSustainableDevelopmentGoal()).isEqualTo(DEFAULT_SUSTAINABLE_DEVELOPMENT_GOAL);
        assertThat(testProject.getFrameworkResilientDevelopmentPacific()).isEqualTo(DEFAULT_FRAMEWORK_RESILIENT_DEVELOPMENT_PACIFIC);
        assertThat(testProject.getFullCostRecoveryCoverage()).isEqualTo(UPDATED_FULL_COST_RECOVERY_COVERAGE);
    }

    @Test
    @Transactional
    void fullUpdateProjectWithPatch() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project using partial update
        Project partialUpdatedProject = new Project();
        partialUpdatedProject.setId(project.getId());

        partialUpdatedProject
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .totalBudgetCurrency(UPDATED_TOTAL_BUDGET_CURRENCY)
            .totalBudget(UPDATED_TOTAL_BUDGET)
            .totalBudgetBreakdown(UPDATED_TOTAL_BUDGET_BREAKDOWN)
            .sustainableDevelopmentGoal(UPDATED_SUSTAINABLE_DEVELOPMENT_GOAL)
            .frameworkResilientDevelopmentPacific(UPDATED_FRAMEWORK_RESILIENT_DEVELOPMENT_PACIFIC)
            .fullCostRecoveryCoverage(UPDATED_FULL_COST_RECOVERY_COVERAGE);

        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProject))
            )
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testProject.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testProject.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testProject.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProject.getTotalBudgetCurrency()).isEqualTo(UPDATED_TOTAL_BUDGET_CURRENCY);
        assertThat(testProject.getTotalBudget()).isEqualTo(UPDATED_TOTAL_BUDGET);
        assertThat(testProject.getTotalBudgetBreakdown()).isEqualTo(UPDATED_TOTAL_BUDGET_BREAKDOWN);
        assertThat(testProject.getSustainableDevelopmentGoal()).isEqualTo(UPDATED_SUSTAINABLE_DEVELOPMENT_GOAL);
        assertThat(testProject.getFrameworkResilientDevelopmentPacific()).isEqualTo(UPDATED_FRAMEWORK_RESILIENT_DEVELOPMENT_PACIFIC);
        assertThat(testProject.getFullCostRecoveryCoverage()).isEqualTo(UPDATED_FULL_COST_RECOVERY_COVERAGE);
    }

    @Test
    @Transactional
    void patchNonExistingProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, project.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(project))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(project))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        int databaseSizeBeforeDelete = projectRepository.findAll().size();

        // Delete the project
        restProjectMockMvc
            .perform(delete(ENTITY_API_URL_ID, project.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
