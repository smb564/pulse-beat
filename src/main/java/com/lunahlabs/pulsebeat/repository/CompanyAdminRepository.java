package com.lunahlabs.pulsebeat.repository;

import com.lunahlabs.pulsebeat.domain.CompanyAdmin;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the CompanyAdmin entity.
 */
@SuppressWarnings("unused")
public interface CompanyAdminRepository extends MongoRepository<CompanyAdmin,String> {

}
