package com.shinwing.backend.incident.model;

/**
 * IncidentStatus is an enum class that represents the status of an incident.
 */
public enum IncidentStatus {
    OPEN("OPEN"),
    IN_PROGRESS("IN_PROGRESS"),
    CLOSED("CLOSED");

    private final String statusValue;

    IncidentStatus(String statusValue) {
        this.statusValue = statusValue;
    }

    public String getStatusValue() {
        return statusValue;
    }
}
