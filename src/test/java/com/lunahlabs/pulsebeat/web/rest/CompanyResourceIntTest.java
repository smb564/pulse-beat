package com.lunahlabs.pulsebeat.web.rest;

import com.lunahlabs.pulsebeat.PulsebeatApp;

import com.lunahlabs.pulsebeat.domain.Company;
import com.lunahlabs.pulsebeat.repository.CompanyRepository;
import com.lunahlabs.pulsebeat.service.CompanyService;
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
 * Test class for the CompanyResource REST controller.
 *
 * @see CompanyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PulsebeatApp.class)
public class CompanyResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restCompanyMockMvc;

    private Company company;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompanyResource companyResource = new CompanyResource(companyService);
        this.restCompanyMockMvc = MockMvcBuilders.standaloneSetup(companyResource)
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
    public static Company createEntity() {
        Company company = new Company()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return company;
    }

    @Before
    public void initTest() {
        companyRepository.deleteAll();
        company = createEntity();
    }

    @Test
    public void createCompany() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Create the Company
        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate + 1);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompany.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    public void createCompanyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Create the Company with an existing ID
        company.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllCompanies() throws Exception {
        // Initialize the database
        companyRepository.save(company);

        // Get all the companyList
        restCompanyMockMvc.perform(get("/api/companies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    public void getCompany() throws Exception {
        // Initialize the database
        companyRepository.save(company);

        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(company.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    public void getNonExistingCompany() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateCompany() throws Exception {
        // Initialize the database
        companyService.save(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company
        Company updatedCompany = companyRepository.findOne(company.getId());
        updatedCompany
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restCompanyMockMvc.perform(put("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompany)))
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompany.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    public void updateNonExistingCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Create the Company

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCompanyMockMvc.perform(put("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteCompany() throws Exception {
        // Initialize the database
        companyService.save(company);

        int databaseSizeBeforeDelete = companyRepository.findAll().size();

        // Get the company
        restCompanyMockMvc.perform(delete("/api/companies/{id}", company.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Company.class);
    }
}
