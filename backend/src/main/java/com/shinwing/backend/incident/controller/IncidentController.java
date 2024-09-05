package com.shinwing.backend.incident.controller;

import com.shinwing.backend.incident.model.Incident;
import com.shinwing.backend.incident.model.PaginatedResult;
import com.shinwing.backend.incident.service.IncidentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/backend")
public class IncidentController {

    private static final Logger logger = LoggerFactory.getLogger(IncidentController.class);

    @Autowired
    private IncidentService incidentService;

    @GetMapping("/incidents")
    public ResponseEntity<PaginatedResult<Incident>> getIncidentsByPage(
            @RequestParam Long page,
            @RequestParam Long pageSize,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String subType,
            @RequestParam(required = false) String status) {
        try {
            logger.info("start to get incidents by page: page={}, pageSize={}", page, pageSize);
            return ResponseEntity.ok(incidentService.getIncidentsByPage((page - 1) * pageSize, pageSize, type, subType, status));
        } catch (IllegalArgumentException e) {
            logger.warn("invalid request parameters: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            logger.error("error processing request: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/incidents/{id}")
    public ResponseEntity<Incident> getIncidentById(@PathVariable Long id) {
        try {
            logger.info("start to fetch id: {}", id);
            return ResponseEntity.ok(incidentService.getIncidentById(id));
        } catch (IllegalArgumentException e) {
            logger.warn("invalid request parameters: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            logger.error("error processing request: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/incidents")
    public ResponseEntity<Long> createIncident(@Valid @RequestBody Incident incident) {
        logger.info("start to create incident: incident={}", incident);
        try {
            logger.info("start to create incident: incident={}", incident);
            long id = incidentService.createIncident(incident);
            logger.info("incident creation processed for token: {}, id: {}", incident.getToken(), id);
            return ResponseEntity.ok(id);
        } catch (IllegalArgumentException e) {
            logger.warn("invalid request parameters: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("error processing request: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/incidents")
    public ResponseEntity<Void> updateIncident(@RequestBody Incident incident) {
        try {
            logger.info("start to update incident: incident={}", incident);
            incidentService.updateIncident(incident);
            logger.info("incident updated successfully for id: {}", incident.getId());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            logger.warn("invalid request parameters: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("error processing request: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/incidents/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Long id) {
        try {
            logger.info("start to delete incident by id: id={}", id);
            incidentService.deleteIncident(id);
            logger.info("incident deleted successfully with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            logger.warn("invalid request parameters: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("error processing request: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
