package com.shinwing.backend.incident.service;

import com.shinwing.backend.incident.mapper.IncidentMapper;
import com.shinwing.backend.incident.model.Incident;
import com.shinwing.backend.incident.model.IncidentStatus;
import com.shinwing.backend.incident.model.PaginatedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

@Service
public class IncidentService {

    private static final Logger logger = LoggerFactory.getLogger(IncidentService.class);

    @Autowired
    private IncidentMapper incidentMapper;

    @Cacheable("incidents")
    public PaginatedResult<Incident> getIncidentsByPage(Long offset, Long limit, String type, String subType, String status) {
        logger.info("get incidents by page: offset={}, limit={}, type={}, subType={}, status={}", offset, limit, type, subType, status);
        if (offset == null || offset < 0) {
            throw new IllegalArgumentException("offset must be a positive number.");
        }
        if (limit == null || limit <= 0) {
            throw new IllegalArgumentException("limit must be a positive number.");
        }

        try {
            List<Incident> incidents =  incidentMapper.findByPage(offset, limit, type, subType, status);
            Integer totalCount = incidentMapper.count(type, subType, status);
            logger.info("get incidents by page, result size is={}", totalCount);
            return new PaginatedResult<>(incidents, totalCount.longValue());
        } catch (Exception e) {
            logger.error("Error fetching incidents: ", e);
            return new PaginatedResult<>(Collections.emptyList(), 0L);
        }
    }

    @Cacheable("incident")
    public Incident getIncidentById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id must be a positive number.");
        }

        try {
            logger.info("get incident with id: {}", id);
            Incident incident = incidentMapper.findById(id);
            if (incident == null) {
                throw new IllegalArgumentException("incident not found for id: " + id);
            }
            return incident;
        } catch (Exception e) {
            logger.error("error fetching incident: ", e);
            throw new RuntimeException("error fetching incident", e);
        }
    }

    public long createIncident(Incident incident) {
        if (incident == null || incident.getToken() == null || incident.getToken().isEmpty()) {
            throw new IllegalArgumentException("incident and token must not be null or empty.");
        }

        try {
            // avoid duplicate incidents with the same token
            logger.info("checking for existing incident with token: {}", incident.getToken());
            Incident existingIncident = incidentMapper.findByToken(incident.getToken());
            if (existingIncident != null) {
                logger.warn("incident with token {} already exists. Skipping insert.", incident.getToken());
                return existingIncident.getId();
            }

            logger.info("creating new incident with token: {}", incident.getToken());
            incident.setStatus(IncidentStatus.OPEN);
            incidentMapper.insert(incident);
            return incident.getId();
        } catch (Exception e) {
            logger.error("error creating incident: ", e);
            throw new RuntimeException("error creating incident", e);
        }
    }

    public void updateIncident(Incident incident) {
        if (incident == null || incident.getId() == null || incident.getId() <= 0) {
            throw new IllegalArgumentException("incident must not be null or empty.");
        }

        try {
            logger.info("checking for existing incident with id: {}", incident.getId());
            Incident existingIncident = incidentMapper.findById(incident.getId());
            if (existingIncident == null) {
                logger.warn("incident with id {} does not exist. Skipping update.", incident.getId());
                throw new IllegalArgumentException("incident with specified id does not exist.");
            }
            if (incident.getTitle() != null && !incident.getTitle().isEmpty()) {
                existingIncident.setTitle(incident.getTitle());
            }
            if (incident.getDescription() != null && !incident.getDescription().isEmpty()) {
                existingIncident.setDescription(incident.getDescription());
            }
            if (incident.getStatus() != null) {
                existingIncident.setStatus(incident.getStatus());
            }
            if (incident.getPriority() != null) {
                existingIncident.setPriority(incident.getPriority());
            }
            logger.info("updating incident with id: {}", incident.getId());
            incidentMapper.update(existingIncident);
        } catch (Exception e) {
            logger.error("error updating incident: ", e);
            throw new RuntimeException("error updating incident", e);
        }
    }

    public void deleteIncident(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("incident ID must not be null.");
        }

        try {
            logger.info("checking for existing incident with ID: {}", id);
            Incident existingIncident = incidentMapper.findById(id);
            if (existingIncident == null) {
                logger.warn("incident with ID {} does not exist. Skipping delete.", id);
                return ;
            }
            LocalDateTime now = LocalDateTime.now();
            Instant instant = now.atZone(ZoneId.systemDefault()).toInstant();
            long timestamp = instant.toEpochMilli();

            logger.info("deleting incident with id: {}, ts: {}", id, timestamp);
            incidentMapper.delete(id, timestamp);
        } catch (Exception e) {
            logger.error("error deleting incident: ", e);
            throw new RuntimeException("error deleting incident", e);
        }
    }
}
