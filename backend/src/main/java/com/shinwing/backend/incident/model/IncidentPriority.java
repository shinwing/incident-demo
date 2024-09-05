package com.shinwing.backend.incident.model;

/**
 * IncidentPriority is an enum class that represents the priority of an incident.
 */
public enum IncidentPriority {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH");

    private final String priorityValue;

    IncidentPriority(String priorityValue) {
        this.priorityValue = priorityValue;
    }

    public String getPriorityValue() {
        return priorityValue;
    }
}
