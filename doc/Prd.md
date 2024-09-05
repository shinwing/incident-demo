## Incident Management Demo Prd

### 1. Introduction
- Incident management is a platform that helps organizations to manage and resolve incidents.
- mvp version only needs to implement the most basic functions.

### 2. Features
- Incident management provides the following features:
  - Create an incident
  - Update an incident
  - Delete an incident
  - Get an incident by id
  - Get all incidents
  - Get incidents by status
---
- An incident has the following attributes:
  - id is a unique identifier for an incident. 
  - title is a short description of the incident.
  - description is a detailed description of the incident.
  - type is the type of the incident. such as INCIDENT, REQUEST, PROBLEM, CHANGE.
  - subType is the subtype of the incident. such as BUG, FEATURE, SERVICE_REQUEST, QUESTION.
  - priority is the priority of the incident. such as LOW, MEDIUM, HIGH.
  - status is the status of the incident. such as OPEN, IN_PROGRESS, CLOSED.

### 3. Other Description
- Due to time constraints, only the basic functionality has been implemented, and further improvements will be made in the future.
  - The status transitions of Incidents are not restricted; you can update the status freely.
  - There is no automatic closure functionality; it needs to be closed manually.
  - The delete function performs a soft delete by marking the record as deleted but does not actually remove it.
  - tThe type should be an enum, but it is currently represented as a string.
  - Token handling is currently only a simple uniqueness check, which is relatively rudimentary.
- The project is a simple demo project, and the code quality is not high. It is only used to demonstrate the basic functions of the project.
- The front-end capabilities are limited, so the current front-end interface is quite rough.
- Due to the use of an in-memory database, performance is currently excellent, and no additional caching has been implemented.



