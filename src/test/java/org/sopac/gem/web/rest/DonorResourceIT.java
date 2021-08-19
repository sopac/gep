package org.sopac.gem.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sopac.gem.IntegrationTest;
import org.sopac.gem.domain.Donor;
import org.sopac.gem.repository.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link DonorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DonorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_DONOR_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_DONOR_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_SECTOR = "AAAAAAAAAA";
    private static final String UPDATED_SECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_KEY_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_KEY_CONTACT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/donors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDonorMockMvc;

    private Donor donor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Donor createEntity(EntityManager em) {
        Donor donor = new Donor()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .url(DEFAULT_URL)
            .city(DEFAULT_CITY)
            .donorCategory(DEFAULT_DONOR_CATEGORY)
            .sector(DEFAULT_SECTOR)
            .keyContact(DEFAULT_KEY_CONTACT);
        return donor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Donor createUpdatedEntity(EntityManager em) {
        Donor donor = new Donor()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .city(UPDATED_CITY)
            .donorCategory(UPDATED_DONOR_CATEGORY)
            .sector(UPDATED_SECTOR)
            .keyContact(UPDATED_KEY_CONTACT);
        return donor;
    }

    @BeforeEach
    public void initTest() {
        donor = createEntity(em);
    }

    @Test
    @Transactional
    void createDonor() throws Exception {
        int databaseSizeBeforeCreate = donorRepository.findAll().size();
        // Create the Donor
        restDonorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(donor)))
            .andExpect(status().isCreated());

        // Validate the Donor in the database
        List<Donor> donorList = donorRepository.findAll();
        assertThat(donorList).hasSize(databaseSizeBeforeCreate + 1);
        Donor testDonor = donorList.get(donorList.size() - 1);
        assertThat(testDonor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDonor.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDonor.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testDonor.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testDonor.getDonorCategory()).isEqualTo(DEFAULT_DONOR_CATEGORY);
        assertThat(testDonor.getSector()).isEqualTo(DEFAULT_SECTOR);
        assertThat(testDonor.getKeyContact()).isEqualTo(DEFAULT_KEY_CONTACT);
    }

    @Test
    @Transactional
    void createDonorWithExistingId() throws Exception {
        // Create the Donor with an existing ID
        donor.setId(1L);

        int databaseSizeBeforeCreate = donorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDonorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(donor)))
            .andExpect(status().isBadRequest());

        // Validate the Donor in the database
        List<Donor> donorList = donorRepository.findAll();
        assertThat(donorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = donorRepository.findAll().size();
        // set the field null
        donor.setName(null);

        // Create the Donor, which fails.

        restDonorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(donor)))
            .andExpect(status().isBadRequest());

        List<Donor> donorList = donorRepository.findAll();
        assertThat(donorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDonors() throws Exception {
        // Initialize the database
        donorRepository.saveAndFlush(donor);

        // Get all the donorList
        restDonorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(donor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].donorCategory").value(hasItem(DEFAULT_DONOR_CATEGORY)))
            .andExpect(jsonPath("$.[*].sector").value(hasItem(DEFAULT_SECTOR)))
            .andExpect(jsonPath("$.[*].keyContact").value(hasItem(DEFAULT_KEY_CONTACT.toString())));
    }

    @Test
    @Transactional
    void getDonor() throws Exception {
        // Initialize the database
        donorRepository.saveAndFlush(donor);

        // Get the donor
        restDonorMockMvc
            .perform(get(ENTITY_API_URL_ID, donor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(donor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.donorCategory").value(DEFAULT_DONOR_CATEGORY))
            .andExpect(jsonPath("$.sector").value(DEFAULT_SECTOR))
            .andExpect(jsonPath("$.keyContact").value(DEFAULT_KEY_CONTACT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDonor() throws Exception {
        // Get the donor
        restDonorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDonor() throws Exception {
        // Initialize the database
        donorRepository.saveAndFlush(donor);

        int databaseSizeBeforeUpdate = donorRepository.findAll().size();

        // Update the donor
        Donor updatedDonor = donorRepository.findById(donor.getId()).get();
        // Disconnect from session so that the updates on updatedDonor are not directly saved in db
        em.detach(updatedDonor);
        updatedDonor
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .city(UPDATED_CITY)
            .donorCategory(UPDATED_DONOR_CATEGORY)
            .sector(UPDATED_SECTOR)
            .keyContact(UPDATED_KEY_CONTACT);

        restDonorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDonor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDonor))
            )
            .andExpect(status().isOk());

        // Validate the Donor in the database
        List<Donor> donorList = donorRepository.findAll();
        assertThat(donorList).hasSize(databaseSizeBeforeUpdate);
        Donor testDonor = donorList.get(donorList.size() - 1);
        assertThat(testDonor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDonor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDonor.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testDonor.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testDonor.getDonorCategory()).isEqualTo(UPDATED_DONOR_CATEGORY);
        assertThat(testDonor.getSector()).isEqualTo(UPDATED_SECTOR);
        assertThat(testDonor.getKeyContact()).isEqualTo(UPDATED_KEY_CONTACT);
    }

    @Test
    @Transactional
    void putNonExistingDonor() throws Exception {
        int databaseSizeBeforeUpdate = donorRepository.findAll().size();
        donor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, donor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(donor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Donor in the database
        List<Donor> donorList = donorRepository.findAll();
        assertThat(donorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDonor() throws Exception {
        int databaseSizeBeforeUpdate = donorRepository.findAll().size();
        donor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(donor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Donor in the database
        List<Donor> donorList = donorRepository.findAll();
        assertThat(donorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDonor() throws Exception {
        int databaseSizeBeforeUpdate = donorRepository.findAll().size();
        donor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(donor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Donor in the database
        List<Donor> donorList = donorRepository.findAll();
        assertThat(donorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDonorWithPatch() throws Exception {
        // Initialize the database
        donorRepository.saveAndFlush(donor);

        int databaseSizeBeforeUpdate = donorRepository.findAll().size();

        // Update the donor using partial update
        Donor partialUpdatedDonor = new Donor();
        partialUpdatedDonor.setId(donor.getId());

        partialUpdatedDonor.city(UPDATED_CITY);

        restDonorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDonor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDonor))
            )
            .andExpect(status().isOk());

        // Validate the Donor in the database
        List<Donor> donorList = donorRepository.findAll();
        assertThat(donorList).hasSize(databaseSizeBeforeUpdate);
        Donor testDonor = donorList.get(donorList.size() - 1);
        assertThat(testDonor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDonor.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDonor.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testDonor.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testDonor.getDonorCategory()).isEqualTo(DEFAULT_DONOR_CATEGORY);
        assertThat(testDonor.getSector()).isEqualTo(DEFAULT_SECTOR);
        assertThat(testDonor.getKeyContact()).isEqualTo(DEFAULT_KEY_CONTACT);
    }

    @Test
    @Transactional
    void fullUpdateDonorWithPatch() throws Exception {
        // Initialize the database
        donorRepository.saveAndFlush(donor);

        int databaseSizeBeforeUpdate = donorRepository.findAll().size();

        // Update the donor using partial update
        Donor partialUpdatedDonor = new Donor();
        partialUpdatedDonor.setId(donor.getId());

        partialUpdatedDonor
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .city(UPDATED_CITY)
            .donorCategory(UPDATED_DONOR_CATEGORY)
            .sector(UPDATED_SECTOR)
            .keyContact(UPDATED_KEY_CONTACT);

        restDonorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDonor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDonor))
            )
            .andExpect(status().isOk());

        // Validate the Donor in the database
        List<Donor> donorList = donorRepository.findAll();
        assertThat(donorList).hasSize(databaseSizeBeforeUpdate);
        Donor testDonor = donorList.get(donorList.size() - 1);
        assertThat(testDonor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDonor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDonor.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testDonor.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testDonor.getDonorCategory()).isEqualTo(UPDATED_DONOR_CATEGORY);
        assertThat(testDonor.getSector()).isEqualTo(UPDATED_SECTOR);
        assertThat(testDonor.getKeyContact()).isEqualTo(UPDATED_KEY_CONTACT);
    }

    @Test
    @Transactional
    void patchNonExistingDonor() throws Exception {
        int databaseSizeBeforeUpdate = donorRepository.findAll().size();
        donor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, donor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(donor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Donor in the database
        List<Donor> donorList = donorRepository.findAll();
        assertThat(donorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDonor() throws Exception {
        int databaseSizeBeforeUpdate = donorRepository.findAll().size();
        donor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(donor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Donor in the database
        List<Donor> donorList = donorRepository.findAll();
        assertThat(donorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDonor() throws Exception {
        int databaseSizeBeforeUpdate = donorRepository.findAll().size();
        donor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(donor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Donor in the database
        List<Donor> donorList = donorRepository.findAll();
        assertThat(donorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDonor() throws Exception {
        // Initialize the database
        donorRepository.saveAndFlush(donor);

        int databaseSizeBeforeDelete = donorRepository.findAll().size();

        // Delete the donor
        restDonorMockMvc
            .perform(delete(ENTITY_API_URL_ID, donor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Donor> donorList = donorRepository.findAll();
        assertThat(donorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
