package com.shinwing.backend.incident.model;

import com.shinwing.backend.incident.mapper.IncidentStatusTypeHandler;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Incident is a model class that represents an incident.
 * <p>
 * @Reference  doc/sql/t_incident.sql
 */
@Entity
@Setter
@Getter
@Table(name = "t_incident")
public class Incident {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "token can not be empty")
    private String token;

    @NotEmpty(message = "title can not be empty")
    private String title;

    @NotEmpty(message = "description can not be empty")
    private String description;

    @NotEmpty(message = "incident type can not be empty")
    private String incidentType;

    @NotEmpty(message = "incident sub type can not be empty")
    private String incidentSubType;

    @Enumerated(EnumType.STRING)
    private IncidentStatus status;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "incident priority can not be empty")
    private IncidentPriority priority;

    @NotEmpty(message = "incident create user can not be empty")
    private String createdBy;

    private Long createdAt;
    private Long updatedAt;
    private Long deletedAt;
}
