package com.lunahlabs.pulsebeat.repository;

import com.lunahlabs.pulsebeat.domain.Company;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Company entity.
 */
@SuppressWarnings("unused")
public interface CompanyRepository extends MongoRepository<Company,String> {

}
