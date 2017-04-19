package com.lunahlabs.pulsebeat.web.rest;

import com.lunahlabs.pulsebeat.PulsebeatApp;

import com.lunahlabs.pulsebeat.domain.Manager;
import com.lunahlabs.pulsebeat.repository.ManagerRepository;
import com.lunahlabs.pulsebeat.service.ManagerService;
import com.lunahlabs.pulsebeat.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ManagerResource REST controller.
 *
 * @see ManagerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PulsebeatApp.class)
public class ManagerResourceIntTest {

    private static final String DEFAULT_COMPANY_ID = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restManagerMockMvc;

    private Manager manager;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ManagerResource managerResource = new ManagerResource(managerService);
        this.restManagerMockMvc = MockMvcBuilders.standaloneSetup(managerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Manager createEntity() {
        Manager manager = new Manager()
            .companyId(DEFAULT_COMPANY_ID)
            .department(DEFAULT_DEPARTMENT)
            .userId(DEFAULT_USER_ID);
        return manager;
    }

    @Before
    public void initTest() {
        managerRepository.deleteAll();
        manager = createEntity();
    }

    @Test
    public void createManager() throws Exception {
        int databaseSizeBeforeCreate = managerRepository.findAll().size();

        // Create the Manager
        restManagerMockMvc.perform(post("/api/managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(manager)))
            .andExpect(status().isCreated());

        // Validate the Manager in the database
        List<Manager> managerList = managerRepository.findAll();
        assertThat(managerList).hasSize(databaseSizeBeforeCreate + 1);
        Manager testManager = managerList.get(managerList.size() - 1);
        assertThat(testManager.getCompanyId()).isEqualTo(DEFAULT_COMPANY_ID);
        assertThat(testManager.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testManager.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    public void createManagerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = managerRepository.findAll().size();

        // Create the Manager with an existing ID
        manager.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restManagerMockMvc.perform(post("/api/managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(manager)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Manager> managerList = managerRepository.findAll();
        assertThat(managerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllManagers() throws Exception {
        // Initialize the database
        managerRepository.save(manager);

        // Get all the managerList
        restManagerMockMvc.perform(get("/api/managers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manager.getId())))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID.toString())))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())));
    }

    @Test
    public void getManager() throws Exception {
        // Initialize the database
        managerRepository.save(manager);

        // Get the manager
        restManagerMockMvc.perform(get("/api/managers/{id}", manager.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(manager.getId()))
            .andExpect(jsonPath("$.companyId").value(DEFAULT_COMPANY_ID.toString()))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()));
    }

    @Test
    public void getNonExistingManager() throws Exception {
        // Get the manager
        restManagerMockMvc.perform(get("/api/managers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateManager() throws Exception {
        // Initialize the database
        managerService.save(manager);

        int databaseSizeBeforeUpdate = managerRepository.findAll().size();

        // Update the manager
        Manager updatedManager = managerRepository.findOne(manager.getId());
        updatedManager
            .companyId(UPDATED_COMPANY_ID)
            .department(UPDATED_DEPARTMENT)
            .userId(UPDATED_USER_ID);

        restManagerMockMvc.perform(put("/api/managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedManager)))
            .andExpect(status().isOk());

        // Validate the Manager in the database
        List<Manager> managerList = managerRepository.findAll();
        assertThat(managerList).hasSize(databaseSizeBeforeUpdate);
        Manager testManager = managerList.get(managerList.size() - 1);
        assertThat(testManager.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testManager.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testManager.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    public void updateNonExistingManager() throws Exception {
        int databaseSizeBeforeUpdate = managerRepository.findAll().size();

        // Create the Manager

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restManagerMockMvc.perform(put("/api/managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(manager)))
            .andExpect(status().isCreated());

        // Validate the Manager in the database
        List<Manager> managerList = managerRepository.findAll();
        assertThat(managerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteManager() throws Exception {
        // Initialize the database
        managerService.save(manager);

        int databaseSizeBeforeDelete = managerRepository.findAll().size();

        // Get the manager
        restManagerMockMvc.perform(delete("/api/managers/{id}", manager.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Manager> managerList = managerRepository.findAll();
        assertThat(managerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Manager.class);
    }
}
