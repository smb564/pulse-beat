package com.lunahlabs.pulsebeat.repository;

import com.lunahlabs.pulsebeat.domain.CompanyAdmin;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the CompanyAdmin entity.
 */
@SuppressWarnings("unused")
public interface CompanyAdminRepository extends MongoRepository<CompanyAdmin,String> {
    Optional<CompanyAdmin> findByUserId(String userId);
}
