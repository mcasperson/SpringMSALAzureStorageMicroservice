// src/main/java/com/matthewcasperson/azureapi/repository/AuditRepository.java

package com.matthewcasperson.azureapi.repository;

import com.matthewcasperson.azureapi.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<Audit, Long> {

}
