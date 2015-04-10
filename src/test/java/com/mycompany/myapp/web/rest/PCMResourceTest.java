package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.PCM;
import com.mycompany.myapp.repository.PCMRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PCMResource REST controller.
 *
 * @see PCMResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PCMResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private PCMRepository pCMRepository;

    private MockMvc restPCMMockMvc;

    private PCM pCM;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PCMResource pCMResource = new PCMResource();
        ReflectionTestUtils.setField(pCMResource, "pCMRepository", pCMRepository);
        this.restPCMMockMvc = MockMvcBuilders.standaloneSetup(pCMResource).build();
    }

    @Before
    public void initTest() {
        pCM = new PCM();
        pCM.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createPCM() throws Exception {
        int databaseSizeBeforeCreate = pCMRepository.findAll().size();

        // Create the PCM
        restPCMMockMvc.perform(post("/api/pCMs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pCM)))
                .andExpect(status().isCreated());

        // Validate the PCM in the database
        List<PCM> pCMs = pCMRepository.findAll();
        assertThat(pCMs).hasSize(databaseSizeBeforeCreate + 1);
        PCM testPCM = pCMs.get(pCMs.size() - 1);
        assertThat(testPCM.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(pCMRepository.findAll()).hasSize(0);
        // set the field null
        pCM.setName(null);

        // Create the PCM, which fails.
        restPCMMockMvc.perform(post("/api/pCMs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pCM)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<PCM> pCMs = pCMRepository.findAll();
        assertThat(pCMs).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllPCMs() throws Exception {
        // Initialize the database
        pCMRepository.saveAndFlush(pCM);

        // Get all the pCMs
        restPCMMockMvc.perform(get("/api/pCMs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pCM.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getPCM() throws Exception {
        // Initialize the database
        pCMRepository.saveAndFlush(pCM);

        // Get the pCM
        restPCMMockMvc.perform(get("/api/pCMs/{id}", pCM.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(pCM.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPCM() throws Exception {
        // Get the pCM
        restPCMMockMvc.perform(get("/api/pCMs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePCM() throws Exception {
        // Initialize the database
        pCMRepository.saveAndFlush(pCM);
		
		int databaseSizeBeforeUpdate = pCMRepository.findAll().size();

        // Update the pCM
        pCM.setName(UPDATED_NAME);
        restPCMMockMvc.perform(put("/api/pCMs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pCM)))
                .andExpect(status().isOk());

        // Validate the PCM in the database
        List<PCM> pCMs = pCMRepository.findAll();
        assertThat(pCMs).hasSize(databaseSizeBeforeUpdate);
        PCM testPCM = pCMs.get(pCMs.size() - 1);
        assertThat(testPCM.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deletePCM() throws Exception {
        // Initialize the database
        pCMRepository.saveAndFlush(pCM);
		
		int databaseSizeBeforeDelete = pCMRepository.findAll().size();

        // Get the pCM
        restPCMMockMvc.perform(delete("/api/pCMs/{id}", pCM.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PCM> pCMs = pCMRepository.findAll();
        assertThat(pCMs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
