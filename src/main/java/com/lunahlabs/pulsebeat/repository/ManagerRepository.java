package com.lunahlabs.pulsebeat.repository;

import com.lunahlabs.pulsebeat.domain.Manager;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Manager entity.
 */
@SuppressWarnings("unused")
public interface ManagerRepository extends MongoRepository<Manager,String> {

}
