package com.lunahlabs.pulsebeat.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lunahlabs.pulsebeat.domain.CompanyAdmin;
import com.lunahlabs.pulsebeat.service.CompanyAdminService;
import com.lunahlabs.pulsebeat.web.rest.util.HeaderUtil;
import com.lunahlabs.pulsebeat.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CompanyAdmin.
 */
@RestController
@RequestMapping("/api")
public class CompanyAdminResource {

    private final Logger log = LoggerFactory.getLogger(CompanyAdminResource.class);

    private static final String ENTITY_NAME = "companyAdmin";
        
    private final CompanyAdminService companyAdminService;

    public CompanyAdminResource(CompanyAdminService companyAdminService) {
        this.companyAdminService = companyAdminService;
    }

    /**
     * POST  /company-admins : Create a new companyAdmin.
     *
     * @param companyAdmin the companyAdmin to create
     * @return the ResponseEntity with status 201 (Created) and with body the new companyAdmin, or with status 400 (Bad Request) if the companyAdmin has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/company-admins")
    @Timed
    public ResponseEntity<CompanyAdmin> createCompanyAdmin(@RequestBody CompanyAdmin companyAdmin) throws URISyntaxException {
        log.debug("REST request to save CompanyAdmin : {}", companyAdmin);
        if (companyAdmin.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new companyAdmin cannot already have an ID")).body(null);
        }
        CompanyAdmin result = companyAdminService.save(companyAdmin);
        return ResponseEntity.created(new URI("/api/company-admins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /company-admins : Updates an existing companyAdmin.
     *
     * @param companyAdmin the companyAdmin to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated companyAdmin,
     * or with status 400 (Bad Request) if the companyAdmin is not valid,
     * or with status 500 (Internal Server Error) if the companyAdmin couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/company-admins")
    @Timed
    public ResponseEntity<CompanyAdmin> updateCompanyAdmin(@RequestBody CompanyAdmin companyAdmin) throws URISyntaxException {
        log.debug("REST request to update CompanyAdmin : {}", companyAdmin);
        if (companyAdmin.getId() == null) {
            return createCompanyAdmin(companyAdmin);
        }
        CompanyAdmin result = companyAdminService.save(companyAdmin);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, companyAdmin.getId().toString()))
            .body(result);
    }

    /**
     * GET  /company-admins : get all the companyAdmins.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of companyAdmins in body
     */
    @GetMapping("/company-admins")
    @Timed
    public ResponseEntity<List<CompanyAdmin>> getAllCompanyAdmins(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CompanyAdmins");
        Page<CompanyAdmin> page = companyAdminService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/company-admins");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /company-admins/:id : get the "id" companyAdmin.
     *
     * @param id the id of the companyAdmin to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the companyAdmin, or with status 404 (Not Found)
     */
    @GetMapping("/company-admins/{id}")
    @Timed
    public ResponseEntity<CompanyAdmin> getCompanyAdmin(@PathVariable String id) {
        log.debug("REST request to get CompanyAdmin : {}", id);
        CompanyAdmin companyAdmin = companyAdminService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(companyAdmin));
    }

    /**
     * DELETE  /company-admins/:id : delete the "id" companyAdmin.
     *
     * @param id the id of the companyAdmin to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/company-admins/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompanyAdmin(@PathVariable String id) {
        log.debug("REST request to delete CompanyAdmin : {}", id);
        companyAdminService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
