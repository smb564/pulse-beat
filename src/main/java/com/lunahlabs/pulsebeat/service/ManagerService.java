package com.lunahlabs.pulsebeat.service;

import com.lunahlabs.pulsebeat.domain.Manager;
import com.lunahlabs.pulsebeat.repository.ManagerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing Manager.
 */
@Service
public class ManagerService {

    private final Logger log = LoggerFactory.getLogger(ManagerService.class);
    
    private final ManagerRepository managerRepository;

    public ManagerService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    /**
     * Save a manager.
     *
     * @param manager the entity to save
     * @return the persisted entity
     */
    public Manager save(Manager manager) {
        log.debug("Request to save Manager : {}", manager);
        Manager result = managerRepository.save(manager);
        return result;
    }

    /**
     *  Get all the managers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<Manager> findAll(Pageable pageable) {
        log.debug("Request to get all Managers");
        Page<Manager> result = managerRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one manager by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Manager findOne(String id) {
        log.debug("Request to get Manager : {}", id);
        Manager manager = managerRepository.findOne(id);
        return manager;
    }

    /**
     *  Delete the  manager by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Manager : {}", id);
        managerRepository.delete(id);
    }
}
