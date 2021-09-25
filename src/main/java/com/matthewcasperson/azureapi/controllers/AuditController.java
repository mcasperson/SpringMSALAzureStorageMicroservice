// src/main/java/com/matthewcasperson/demo/controllers/AuditController.java

package com.matthewcasperson.azureapi.controllers;

import com.matthewcasperson.azureapi.model.Audit;
import com.matthewcasperson.azureapi.repository.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuditController {

    @Autowired
    AuditRepository auditRepository;

    @GetMapping(value = "/audit", produces = "application/json")
    public List<Audit> upload() {
        return auditRepository.findAll();
    }
}