package com.lunahlabs.pulsebeat.web.rest;

import com.lunahlabs.pulsebeat.PulsebeatApp;

import com.lunahlabs.pulsebeat.domain.CompanyAdmin;
import com.lunahlabs.pulsebeat.repository.CompanyAdminRepository;
import com.lunahlabs.pulsebeat.service.CompanyAdminService;
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
 * Test class for the CompanyAdminResource REST controller.
 *
 * @see CompanyAdminResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PulsebeatApp.class)
public class CompanyAdminResourceIntTest {

    private static final String DEFAULT_COMPANY_ID = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_ID = "BBBBBBBBBB";

    @Autowired
    private CompanyAdminRepository companyAdminRepository;

    @Autowired
    private CompanyAdminService companyAdminService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restCompanyAdminMockMvc;

    private CompanyAdmin companyAdmin;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompanyAdminResource companyAdminResource = new CompanyAdminResource(companyAdminService);
        this.restCompanyAdminMockMvc = MockMvcBuilders.standaloneSetup(companyAdminResource)
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
    public static CompanyAdmin createEntity() {
        CompanyAdmin companyAdmin = new CompanyAdmin()
            .companyId(DEFAULT_COMPANY_ID);
        return companyAdmin;
    }

    @Before
    public void initTest() {
        companyAdminRepository.deleteAll();
        companyAdmin = createEntity();
    }

    @Test
    public void createCompanyAdmin() throws Exception {
        int databaseSizeBeforeCreate = companyAdminRepository.findAll().size();

        // Create the CompanyAdmin
        restCompanyAdminMockMvc.perform(post("/api/company-admins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyAdmin)))
            .andExpect(status().isCreated());

        // Validate the CompanyAdmin in the database
        List<CompanyAdmin> companyAdminList = companyAdminRepository.findAll();
        assertThat(companyAdminList).hasSize(databaseSizeBeforeCreate + 1);
        CompanyAdmin testCompanyAdmin = companyAdminList.get(companyAdminList.size() - 1);
        assertThat(testCompanyAdmin.getCompanyId()).isEqualTo(DEFAULT_COMPANY_ID);
    }

    @Test
    public void createCompanyAdminWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = companyAdminRepository.findAll().size();

        // Create the CompanyAdmin with an existing ID
        companyAdmin.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyAdminMockMvc.perform(post("/api/company-admins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyAdmin)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CompanyAdmin> companyAdminList = companyAdminRepository.findAll();
        assertThat(companyAdminList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllCompanyAdmins() throws Exception {
        // Initialize the database
        companyAdminRepository.save(companyAdmin);

        // Get all the companyAdminList
        restCompanyAdminMockMvc.perform(get("/api/company-admins?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyAdmin.getId())))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID.toString())));
    }

    @Test
    public void getCompanyAdmin() throws Exception {
        // Initialize the database
        companyAdminRepository.save(companyAdmin);

        // Get the companyAdmin
        restCompanyAdminMockMvc.perform(get("/api/company-admins/{id}", companyAdmin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(companyAdmin.getId()))
            .andExpect(jsonPath("$.companyId").value(DEFAULT_COMPANY_ID.toString()));
    }

    @Test
    public void getNonExistingCompanyAdmin() throws Exception {
        // Get the companyAdmin
        restCompanyAdminMockMvc.perform(get("/api/company-admins/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateCompanyAdmin() throws Exception {
        // Initialize the database
        companyAdminService.save(companyAdmin);

        int databaseSizeBeforeUpdate = companyAdminRepository.findAll().size();

        // Update the companyAdmin
        CompanyAdmin updatedCompanyAdmin = companyAdminRepository.findOne(companyAdmin.getId());
        updatedCompanyAdmin
            .companyId(UPDATED_COMPANY_ID);

        restCompanyAdminMockMvc.perform(put("/api/company-admins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompanyAdmin)))
            .andExpect(status().isOk());

        // Validate the CompanyAdmin in the database
        List<CompanyAdmin> companyAdminList = companyAdminRepository.findAll();
        assertThat(companyAdminList).hasSize(databaseSizeBeforeUpdate);
        CompanyAdmin testCompanyAdmin = companyAdminList.get(companyAdminList.size() - 1);
        assertThat(testCompanyAdmin.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
    }

    @Test
    public void updateNonExistingCompanyAdmin() throws Exception {
        int databaseSizeBeforeUpdate = companyAdminRepository.findAll().size();

        // Create the CompanyAdmin

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCompanyAdminMockMvc.perform(put("/api/company-admins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyAdmin)))
            .andExpect(status().isCreated());

        // Validate the CompanyAdmin in the database
        List<CompanyAdmin> companyAdminList = companyAdminRepository.findAll();
        assertThat(companyAdminList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteCompanyAdmin() throws Exception {
        // Initialize the database
        companyAdminService.save(companyAdmin);

        int databaseSizeBeforeDelete = companyAdminRepository.findAll().size();

        // Get the companyAdmin
        restCompanyAdminMockMvc.perform(delete("/api/company-admins/{id}", companyAdmin.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CompanyAdmin> companyAdminList = companyAdminRepository.findAll();
        assertThat(companyAdminList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyAdmin.class);
    }
}
