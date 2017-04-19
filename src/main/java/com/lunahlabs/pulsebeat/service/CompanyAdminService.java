package com.lunahlabs.pulsebeat.service;

import com.lunahlabs.pulsebeat.domain.CompanyAdmin;
import com.lunahlabs.pulsebeat.repository.CompanyAdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing CompanyAdmin.
 */
@Service
public class CompanyAdminService {

    private final Logger log = LoggerFactory.getLogger(CompanyAdminService.class);
    
    private final CompanyAdminRepository companyAdminRepository;

    public CompanyAdminService(CompanyAdminRepository companyAdminRepository) {
        this.companyAdminRepository = companyAdminRepository;
    }

    /**
     * Save a companyAdmin.
     *
     * @param companyAdmin the entity to save
     * @return the persisted entity
     */
    public CompanyAdmin save(CompanyAdmin companyAdmin) {
        log.debug("Request to save CompanyAdmin : {}", companyAdmin);
        CompanyAdmin result = companyAdminRepository.save(companyAdmin);
        return result;
    }

    /**
     *  Get all the companyAdmins.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<CompanyAdmin> findAll(Pageable pageable) {
        log.debug("Request to get all CompanyAdmins");
        Page<CompanyAdmin> result = companyAdminRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one companyAdmin by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public CompanyAdmin findOne(String id) {
        log.debug("Request to get CompanyAdmin : {}", id);
        CompanyAdmin companyAdmin = companyAdminRepository.findOne(id);
        return companyAdmin;
    }

    /**
     *  Delete the  companyAdmin by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete CompanyAdmin : {}", id);
        companyAdminRepository.delete(id);
    }
}
