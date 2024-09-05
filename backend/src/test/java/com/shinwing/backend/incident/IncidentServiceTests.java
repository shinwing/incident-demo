package com.shinwing.backend.incident;

import com.shinwing.backend.incident.model.Incident;
import com.shinwing.backend.incident.model.IncidentStatus;
import com.shinwing.backend.incident.model.PaginatedResult;
import com.shinwing.backend.incident.mapper.IncidentMapper;
import com.shinwing.backend.incident.service.IncidentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IncidentServiceTest {

	@Mock
	private IncidentMapper incidentMapper;

	@InjectMocks
	private IncidentService incidentService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	// Test getIncidentsByPage method returns incidents and total count when valid offset and limit are provided
	@Test
	void testGetIncidentsByPage() {
		Long offset = 0L;
		Long limit = 10L;
		String type = "TYPE";
		String subType = "SUB_TYPE";
		String status = "STATUS";
		List<Incident> incidents = Arrays.asList(new Incident(), new Incident());
		Integer totalCount = 20;

		when(incidentMapper.findByPage(offset, limit, type, subType, status)).thenReturn(incidents);
		when(incidentMapper.count(type, subType, status)).thenReturn(totalCount);

		PaginatedResult<Incident> result = incidentService.getIncidentsByPage(offset, limit, type, subType, status);

		assertNotNull(result);
		assertEquals(incidents, result.getItems());
		assertEquals(totalCount.longValue(), result.getTotalCount());
		verify(incidentMapper, times(1)).findByPage(offset, limit, type, subType, status);
		verify(incidentMapper, times(1)).count(type, subType, status);
	}

	// Test getIncidentsByPage method exception when offset is invalid
	@Test
	void testGetIncidentsByPageWithInvalidOffset() {
		Long offset = -1L;
		Long limit = 10L;
		String type = "TYPE";
		String subType = "SUB_TYPE";
		String status = "STATUS";

		assertThrows(IllegalArgumentException.class, () -> incidentService.getIncidentsByPage(offset, limit, type, subType, status));
		verify(incidentMapper, never()).findByPage(offset, limit, type, subType, status);
		verify(incidentMapper, never()).count(type, subType, status);
	}

	// Test getIncidentsByPage method exception when limit is invalid
	@Test
	void testGetIncidentsByPageWithInvalidLimit() {
		Long offset = 0L;
		Long limit = 0L;
		String type = "TYPE";
		String subType = "SUB_TYPE";
		String status = "STATUS";

		assertThrows(IllegalArgumentException.class, () -> incidentService.getIncidentsByPage(offset, limit, type, subType, status));
		verify(incidentMapper, never()).findByPage(offset, limit, type, subType, status);
		verify(incidentMapper, never()).count(type, subType, status);
	}

	// Test getIncidentsByPage method exception when mapper throws exception
	@Test
	void testGetIncidentsByPageWithException() {
		Long offset = 0L;
		Long limit = 10L;
		String type = "TYPE";
		String subType = "SUB_TYPE";
		String status = "STATUS";
		when(incidentMapper.findByPage(offset, limit, type, subType, status)).thenThrow(new RuntimeException());

		PaginatedResult<Incident> result = incidentService.getIncidentsByPage(offset, limit, type, subType, status);

		assertNotNull(result);
		assertEquals(Collections.emptyList(), result.getItems());
		assertEquals(0L, result.getTotalCount());
		verify(incidentMapper, times(1)).findByPage(offset, limit, type, subType, status);
		verify(incidentMapper, never()).count(type, subType, status);
	}

	// Test getIncidentById method returns incident
	@Test
	void testGetIncidentById() {
		Long id = 1L;
		Incident incident = new Incident();
		when(incidentMapper.findById(id)).thenReturn(incident);

		Incident result = incidentService.getIncidentById(id);

		assertNotNull(result);
		assertEquals(incident, result);
		verify(incidentMapper, times(1)).findById(id);
	}

	// Test getIncidentById method exception when id is invalid
	@Test
	void testGetIncidentByIdWithInvalidId() {
		Long id = -1L;

		assertThrows(IllegalArgumentException.class, () -> incidentService.getIncidentById(id));
		verify(incidentMapper, never()).findById(id);
	}

	// Test getIncidentById method exception when incident is not found
	@Test
	void testGetIncidentByIdWithNotFound() {
		Long id = 1L;
		when(incidentMapper.findById(id)).thenReturn(null);

		assertThrows(RuntimeException.class, () -> incidentService.getIncidentById(id));
		verify(incidentMapper, times(1)).findById(id);
	}

	// Test getIncidentById method exception when mapper throws exception
	@Test
	void testGetIncidentByIdWithException() {
		Long id = 1L;
		when(incidentMapper.findById(id)).thenThrow(new RuntimeException());

		assertThrows(RuntimeException.class, () -> incidentService.getIncidentById(id));
		verify(incidentMapper, times(1)).findById(id);
	}

	// Test createIncident method creates incident
	@Test
	void testCreateIncident() {
		Incident incident = new Incident();
		incident.setId(1L);
		incident.setToken("TOKEN");
		when(incidentMapper.findByToken(incident.getToken())).thenReturn(null);
		when(incidentMapper.insert(incident)).thenReturn(1);

		incidentService.createIncident(incident);
		verify(incidentMapper, times(1)).findByToken(incident.getToken());
		verify(incidentMapper, times(1)).insert(incident);
	}

	// Test createIncident method throws exception when incident is null
	@Test
	void testCreateIncidentWithNullIncident() {
		Incident incident = null;

		assertThrows(IllegalArgumentException.class, () -> incidentService.createIncident(incident));
		verify(incidentMapper, never()).findByToken(any());
		verify(incidentMapper, never()).insert(any());
	}

	// Test createIncident method throws exception when token is null
	@Test
	void testCreateIncidentWithNullToken() {
		Incident incident = new Incident();
		incident.setToken(null);

		assertThrows(IllegalArgumentException.class, () -> incidentService.createIncident(incident));
		verify(incidentMapper, never()).findByToken(any());
		verify(incidentMapper, never()).insert(any());
	}

	// Test createIncident method throws exception when token is empty
	@Test
	void testCreateIncidentWithEmptyToken() {
		Incident incident = new Incident();
		incident.setToken("");

		assertThrows(IllegalArgumentException.class, () -> incidentService.createIncident(incident));
		verify(incidentMapper, never()).findByToken(any());
		verify(incidentMapper, never()).insert(any());
	}

	// Test createIncident method returns existing incident id
	@Test
	void testCreateIncidentWithExistingIncident() {
		Incident incident = new Incident();
		incident.setToken("TOKEN");
		Incident existingIncident = new Incident();
		existingIncident.setId(1L);
		when(incidentMapper.findByToken(incident.getToken())).thenReturn(existingIncident);

		long result = incidentService.createIncident(incident);

		assertEquals(1L, result);
		verify(incidentMapper, times(1)).findByToken(incident.getToken());
		verify(incidentMapper, never()).insert(any());
	}

	// Test createIncident method throws exception when mapper throws exception
	@Test
	void testCreateIncidentWithException() {
		Incident incident = new Incident();
		incident.setToken("TOKEN");
		when(incidentMapper.findByToken(incident.getToken())).thenThrow(new RuntimeException());

		assertThrows(RuntimeException.class, () -> incidentService.createIncident(incident));
		verify(incidentMapper, times(1)).findByToken(incident.getToken());
		verify(incidentMapper, never()).insert(any());
	}

	// Test updateIncident method updates incident status
	@Test
	void testUpdateIncident() {
		Incident incident = new Incident();
		incident.setId(1L);
		incident.setStatus(IncidentStatus.OPEN);

		when(incidentMapper.findById(1L)).thenReturn(new Incident());

		incidentService.updateIncident(incident);
		verify(incidentMapper, times(1)).findById(incident.getId());
		verify(incidentMapper, times(1)).update(any());
	}

	// Test updateIncident method throws exception when incident is null
	@Test
	void testUpdateIncidentWithNullIncident() {
		Incident incident = null;

		assertThrows(IllegalArgumentException.class, () -> incidentService.updateIncident(incident));
		verify(incidentMapper, never()).findById(any());
		verify(incidentMapper, never()).update(any());
	}

	// Test updateIncident method throws exception when id is zero
	@Test
	void testUpdateIncidentWithNullId() {
		Incident incident = new Incident();
		incident.setId(0L);

		assertThrows(IllegalArgumentException.class, () -> incidentService.updateIncident(incident));
		verify(incidentMapper, never()).findById(any());
		verify(incidentMapper, never()).update(any());
	}

	// Test updateIncident method throws exception when incident is not exist
	@Test
	void testUpdateIncidentWithNonExistingIncident() {
		Incident incident = new Incident();
		incident.setId(1L);
		incident.setStatus(IncidentStatus.OPEN);
		when(incidentMapper.findById(incident.getId())).thenReturn(null);

		assertThrows(RuntimeException.class, () -> incidentService.updateIncident(incident));
		verify(incidentMapper, times(1)).findById(incident.getId());
		verify(incidentMapper, never()).update(any());
	}

	// Test updateIncident method throws exception when mapper throws exception
	@Test
	void testUpdateIncidentWithException() {
		Incident incident = new Incident();
		incident.setId(1L);
		incident.setStatus(IncidentStatus.OPEN);
		when(incidentMapper.findById(incident.getId())).thenThrow(new RuntimeException());

		assertThrows(RuntimeException.class, () -> incidentService.updateIncident(incident));
		verify(incidentMapper, times(1)).findById(incident.getId());
		verify(incidentMapper, never()).update(any());
	}

	// Test deleteIncident method deletes incident
	@Test
	void testDeleteIncidentSuccessful() {
		Long id = 1L;
		when(incidentMapper.findById(id)).thenReturn(new Incident());

		incidentService.deleteIncident(id);
		verify(incidentMapper, times(1)).findById(id);
		verify(incidentMapper, times(1)).delete(any(), any());
	}

	// Test deleteIncident method when incident is not exist
	@Test
	void testDeleteIncidentNonExistingId() {
		Long id = 1L;
		when(incidentMapper.findById(id)).thenReturn(null);

		incidentService.deleteIncident(id);
		verify(incidentMapper, never()).delete(any(), any());
	}

	// Test deleteIncident method throws exception when mapper throws exception
	@Test
	void testDeleteIncidentException() {
		Long id = 1L;
		when(incidentMapper.findById(id)).thenReturn(new Incident());
		doThrow(new RuntimeException()).when(incidentMapper).delete(any(), any());

		assertThrows(RuntimeException.class, () -> incidentService.deleteIncident(id));
		verify(incidentMapper, times(1)).findById(id);
		verify(incidentMapper, times(1)).delete(any(), any());
	}
}