package com.matthewcasperson.azureapi.repository;

import com.matthewcasperson.azureapi.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditRepository extends JpaRepository<Audit, Long> {
    Audit saveAndFlush(Audit audit);
    List<Audit> findAll();
}
