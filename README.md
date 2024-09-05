# Incident Management
incident management is a platform that helps organizations to manage and resolve incidents. 

## source code 
```angular2html
.
├── HELP.md
├── README.md
├── backend
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── shinwing
│       │   │           └── backend
│       │   │               └── incident
│       │   │                   ├── IncidentApplication.java
│       │   │                   ├── config
│       │   │                   │   └── MyBatisConfig.java
│       │   │                   ├── controller
│       │   │                   │   └── IncidentController.java
│       │   │                   ├── mapper
│       │   │                   │   ├── IncidentMapper.java
│       │   │                   │   ├── IncidentPriorityTypeHandler.java
│       │   │                   │   └── IncidentStatusTypeHandler.java
│       │   │                   ├── model
│       │   │                   │   ├── Incident.java
│       │   │                   │   ├── IncidentPriority.java
│       │   │                   │   ├── IncidentStatus.java
│       │   │                   │   └── PaginatedResult.java
│       │   │                   └── service
│       │   │                       └── IncidentService.java
│       │   └── resources
│       │       ├── application.properties
│       │       ├── static
│       │       └── templates
│       └── test
│           ├── e2e
│           │   ├── http-client.env.json
│           │   └── incidentControllerTest.http
│           └── java
│               └── com
│                   └── shinwing
│                       └── backend
│                           └── incident
│                               └── IncidentServiceTests.java
├── doc
│   └── sql
│       └── t_incident.sql
├── dockerfile
├── incident.iml
├── mvnw
├── mvnw.cmd
├── pom.xml

```
### Backend
- The backend is a Spring Boot application that provides RESTful APIs to manage incidents.
- Uses MyBatis to interact with the database.
- Test cases are written using JUnit and Mockito.

### Database
- All sql files in doc/sql.
- Uses H2 as the database.

### Frontend
- The frontend is a React application that provides a user interface to manage incidents.
- Main components are `IncidentList` and `IncidentDetail`.
```angular2html
frontend
└── incident-management
    ├── README.md
    ├── package-lock.json
    ├── package.json
    ├── public
    │   ├── favicon.ico
    │   ├── index.html
    │   ├── logo192.png
    │   ├── logo512.png
    │   ├── manifest.json
    │   └── robots.txt
    └── src
        ├── App.css
        ├── App.js
        ├── App.test.js
        ├── axiosConfig.js
        ├── components
        │   └── incident
        │       ├── IncidentDetail.js
        │       └── IncidentList.js
        ├── index.css
        ├── index.js
        ├── logo.svg
        ├── reportWebVitals.js
        └── setupTests.js
```

## Installation
### Build 
```bash
docker build -t incident-management-backend:1.0.0 -f backend/dockerfile .
docker build -t incident-management-front:1.0.0 -f frontend/incident-management/dockerfile .
```
### Run
```bash
docker run -d -p 8080:8080 incident-management-backend:1.0.0
docker run -d -p 80:80 incident-management-front:1.0.0 
```
### Test
```bash
curl http://127.0.0.1:8080/api/backend/incidents\?page\=1\&pageSize\=10
```

## Usage
### Access
open http://127.0.0.1 in browser
### API Endpoints
- `GET /api/incidents?page=1&pageSize=10`: List all incidents.
  - Page and pageSize are required query parameters.
  - `GET /api/incidents?page=1&pageSize=10&status={status}`: List all incidents with a specific status.
    - Status can be one of `OPEN`, `IN_PROGRESS`, `CLOSED`.
  - `GET /api/incidents?page=1&pageSize=10&type={type}`: List all incidents with a specific type.
  - `GET /api/incidents?page=1&pageSize=10&subType={subType}`: List all incidents with a specific subType.
- `GET /api/incidents/{id}`: Get an incident by ID.
- `POST /api/incidents`: Create a new incident.
- `PUT /api/incidents/{id}`: Update an existing incident.
- `DELETE /api/incidents/{id}`: Delete an incident.
  - Delete is a soft delete, the incident will be marked as deleted but not removed from the database.

### Incident Model
```json
{
  "id": 1,
  "title": "Incident Title",
  "description": "Incident Description",
  "priority": "HIGH",
  "status": "OPEN",
  "type": "INCIDENT",
  "subType": "BUG",
  "createdAt": "2021-08-01T00:00:00Z",
  "updatedAt": "2021-08-01T00:00:00Z",
  "deletedAt": null
}
```

#### Incident Priority
- `LOW`
- `MEDIUM`
- `HIGH`
- `CRITICAL`

#### Incident Status
- `OPEN`
- `IN_PROGRESS`
- `CLOSED`

### Stress Test
- Use wrk to test the backend API.
```bash
wrk -t12 -c400 -d30s curl http://127.0.0.1:8080/api/backend/incidents\?page\=1\&pageSize\=10
```

```bash
Running 30s test @ http://127.0.0.1:8080/api/backend/incidents?page=1&pageSize=10
  12 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    66.31ms  120.65ms   1.99s    93.37%
    Req/Sec     0.94k   356.40     2.01k    70.01%
  331789 requests in 30.07s, 76.31MB read
  Socket errors: connect 0, read 274, write 1, timeout 12
Requests/sec:  11033.95
Transfer/sec:      2.54MB


Running 30s test @ http://127.0.0.1:8080/api/backend/incidents?page=1&pageSize=10
  12 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    44.69ms   71.61ms   1.35s    90.68%
    Req/Sec     1.24k   276.99     2.16k    70.38%
  446161 requests in 30.04s, 102.62MB read
  Socket errors: connect 0, read 684, write 0, timeout 0
Requests/sec:  14850.05
Transfer/sec:      3.42MB

```

### Other documents
- doc/DevelopStep.md
- doc/Prd.md