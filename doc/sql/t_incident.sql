-- t_incident table for incident management
CREATE TABLE IF NOT EXISTS backend.t_incident (
    id INT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(40) NOT NULL COMMENT 'token for avoid front-end click mis operation',
    title VARCHAR(255) NOT NULL COMMENT 'title',
    description TEXT NOT NULL COMMENT 'description',
    incident_type VARCHAR(255) NOT NULL COMMENT 'type of incident',
    incident_sub_type VARCHAR(255) NOT NULL COMMENT 'sub type of incident',
    status ENUM('OPEN', 'IN_PROGRESS', 'CLOSED') NOT NULL DEFAULT 'open' COMMENT 'status for incident',
    priority ENUM('LOW', 'MEDIUM', 'HIGH') NOT NULL DEFAULT 'low' COMMENT 'priority for incident',
    created_by VARCHAR(40)  NOT NULL COMMENT 'created by user erp',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL DEFAULT NULL,
    INDEX IDX_INCIDENT_STATUS (status),
    INDEX IDX_INCIDENT_PRIORITY (priority),
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
