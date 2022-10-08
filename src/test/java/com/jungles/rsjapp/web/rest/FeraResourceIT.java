package com.jungles.rsjapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jungles.rsjapp.IntegrationTest;
import com.jungles.rsjapp.domain.Fera;
import com.jungles.rsjapp.repository.FeraRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FeraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FeraResourceIT {

    private static final String DEFAULT_FERA_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FERA_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/feras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FeraRepository feraRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeraMockMvc;

    private Fera fera;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fera createEntity(EntityManager em) {
        Fera fera = new Fera().feraName(DEFAULT_FERA_NAME);
        return fera;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fera createUpdatedEntity(EntityManager em) {
        Fera fera = new Fera().feraName(UPDATED_FERA_NAME);
        return fera;
    }

    @BeforeEach
    public void initTest() {
        fera = createEntity(em);
    }

    @Test
    @Transactional
    void createFera() throws Exception {
        int databaseSizeBeforeCreate = feraRepository.findAll().size();
        // Create the Fera
        restFeraMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fera))
            )
            .andExpect(status().isCreated());

        // Validate the Fera in the database
        List<Fera> feraList = feraRepository.findAll();
        assertThat(feraList).hasSize(databaseSizeBeforeCreate + 1);
        Fera testFera = feraList.get(feraList.size() - 1);
        assertThat(testFera.getFeraName()).isEqualTo(DEFAULT_FERA_NAME);
    }

    @Test
    @Transactional
    void createFeraWithExistingId() throws Exception {
        // Create the Fera with an existing ID
        fera.setId(1L);

        int databaseSizeBeforeCreate = feraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeraMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fera))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fera in the database
        List<Fera> feraList = feraRepository.findAll();
        assertThat(feraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFeras() throws Exception {
        // Initialize the database
        feraRepository.saveAndFlush(fera);

        // Get all the feraList
        restFeraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fera.getId().intValue())))
            .andExpect(jsonPath("$.[*].feraName").value(hasItem(DEFAULT_FERA_NAME)));
    }

    @Test
    @Transactional
    void getFera() throws Exception {
        // Initialize the database
        feraRepository.saveAndFlush(fera);

        // Get the fera
        restFeraMockMvc
            .perform(get(ENTITY_API_URL_ID, fera.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fera.getId().intValue()))
            .andExpect(jsonPath("$.feraName").value(DEFAULT_FERA_NAME));
    }

    @Test
    @Transactional
    void getNonExistingFera() throws Exception {
        // Get the fera
        restFeraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFera() throws Exception {
        // Initialize the database
        feraRepository.saveAndFlush(fera);

        int databaseSizeBeforeUpdate = feraRepository.findAll().size();

        // Update the fera
        Fera updatedFera = feraRepository.findById(fera.getId()).get();
        // Disconnect from session so that the updates on updatedFera are not directly saved in db
        em.detach(updatedFera);
        updatedFera.feraName(UPDATED_FERA_NAME);

        restFeraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFera.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFera))
            )
            .andExpect(status().isOk());

        // Validate the Fera in the database
        List<Fera> feraList = feraRepository.findAll();
        assertThat(feraList).hasSize(databaseSizeBeforeUpdate);
        Fera testFera = feraList.get(feraList.size() - 1);
        assertThat(testFera.getFeraName()).isEqualTo(UPDATED_FERA_NAME);
    }

    @Test
    @Transactional
    void putNonExistingFera() throws Exception {
        int databaseSizeBeforeUpdate = feraRepository.findAll().size();
        fera.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fera.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fera))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fera in the database
        List<Fera> feraList = feraRepository.findAll();
        assertThat(feraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFera() throws Exception {
        int databaseSizeBeforeUpdate = feraRepository.findAll().size();
        fera.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fera))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fera in the database
        List<Fera> feraList = feraRepository.findAll();
        assertThat(feraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFera() throws Exception {
        int databaseSizeBeforeUpdate = feraRepository.findAll().size();
        fera.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeraMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fera))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fera in the database
        List<Fera> feraList = feraRepository.findAll();
        assertThat(feraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeraWithPatch() throws Exception {
        // Initialize the database
        feraRepository.saveAndFlush(fera);

        int databaseSizeBeforeUpdate = feraRepository.findAll().size();

        // Update the fera using partial update
        Fera partialUpdatedFera = new Fera();
        partialUpdatedFera.setId(fera.getId());

        restFeraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFera.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFera))
            )
            .andExpect(status().isOk());

        // Validate the Fera in the database
        List<Fera> feraList = feraRepository.findAll();
        assertThat(feraList).hasSize(databaseSizeBeforeUpdate);
        Fera testFera = feraList.get(feraList.size() - 1);
        assertThat(testFera.getFeraName()).isEqualTo(DEFAULT_FERA_NAME);
    }

    @Test
    @Transactional
    void fullUpdateFeraWithPatch() throws Exception {
        // Initialize the database
        feraRepository.saveAndFlush(fera);

        int databaseSizeBeforeUpdate = feraRepository.findAll().size();

        // Update the fera using partial update
        Fera partialUpdatedFera = new Fera();
        partialUpdatedFera.setId(fera.getId());

        partialUpdatedFera.feraName(UPDATED_FERA_NAME);

        restFeraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFera.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFera))
            )
            .andExpect(status().isOk());

        // Validate the Fera in the database
        List<Fera> feraList = feraRepository.findAll();
        assertThat(feraList).hasSize(databaseSizeBeforeUpdate);
        Fera testFera = feraList.get(feraList.size() - 1);
        assertThat(testFera.getFeraName()).isEqualTo(UPDATED_FERA_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingFera() throws Exception {
        int databaseSizeBeforeUpdate = feraRepository.findAll().size();
        fera.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fera.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fera))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fera in the database
        List<Fera> feraList = feraRepository.findAll();
        assertThat(feraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFera() throws Exception {
        int databaseSizeBeforeUpdate = feraRepository.findAll().size();
        fera.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fera))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fera in the database
        List<Fera> feraList = feraRepository.findAll();
        assertThat(feraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFera() throws Exception {
        int databaseSizeBeforeUpdate = feraRepository.findAll().size();
        fera.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeraMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fera))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fera in the database
        List<Fera> feraList = feraRepository.findAll();
        assertThat(feraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFera() throws Exception {
        // Initialize the database
        feraRepository.saveAndFlush(fera);

        int databaseSizeBeforeDelete = feraRepository.findAll().size();

        // Delete the fera
        restFeraMockMvc
            .perform(delete(ENTITY_API_URL_ID, fera.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fera> feraList = feraRepository.findAll();
        assertThat(feraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
