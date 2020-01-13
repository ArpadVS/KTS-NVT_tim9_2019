package backend.controller;

import static backend.constants.HallConstants.*;
import static backend.constants.LocationConstants.LOCATION_ID_NON_EXISTENT;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import backend.converters.AddressConverter;
import backend.dto.AddressDTO;
import backend.dto.HallDTO;
import backend.dto.HallUpdateDTO;
import backend.dto.SectorDTO;
import backend.dto.SittingSectorDTO;
import backend.dto.StandingSectorDTO;
import backend.exceptions.ResourceNotFoundException;
import backend.model.Address;
import backend.model.Hall;
import backend.model.Location;
import backend.model.Sector;
import backend.model.UserTokenState;
import backend.security.auth.JwtAuthenticationRequest;
import backend.service.AddressService;
import backend.service.HallService;
import backend.service.LocationService;
import backend.service.SectorService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class HallControllerIntegrationTest {

	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	HallService hallService;
	
	@Autowired
	LocationService locationService;
	
	@Autowired
	SectorService sectorService;
	
	private String accessToken;
	private HttpHeaders headers = new HttpHeaders();
	
	@Before
	public void login() {
		ResponseEntity<UserTokenState> login = 
				restTemplate.postForEntity("/auth/login", 
						new JwtAuthenticationRequest("admin", "admin"), 
						UserTokenState.class);
		accessToken = login.getBody().getAccessToken();
		headers.add("Authorization", "Bearer "+accessToken);
	}
	@Test
	public void testGetAllHalls() {
		ResponseEntity<Hall[]> responseEntity = restTemplate.getForEntity("/api/hall", Hall[].class);
		Hall[] halls = responseEntity.getBody();
		Hall h0 = halls[0];
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(halls);
		assertNotEquals(0, halls.length);
		assertEquals(4, halls.length);
		assertEquals(DB_HALL_ID, h0.getId());
		assertEquals(DB_HALL_NAME, h0.getName());
	}
	
	
	@Test
	public void testGetHall() {
		ResponseEntity<Hall> responseEntity = restTemplate.getForEntity("/api/hall/"+DB_HALL_ID, Hall.class);
		Hall found = responseEntity.getBody();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(found);
		assertEquals(DB_HALL_ID, found.getId());
		assertEquals(DB_HALL_NAME, found.getName());
	}
	
	@Test
	public void testGetAddressNonExistent() {
		ResponseEntity<String> responseEntity = restTemplate.getForEntity("/api/hall/"+HALL_ID_NON_EXISTENT, String.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertTrue(responseEntity.getBody().contains("Could not find requested hall"));
	}
	
	@Test
	public void testDelete() throws ResourceNotFoundException {
		int size = hallService.findAllNotDeleted().size();
		
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/hall/" + DB_HALL_TO_BE_DELETED2, 
	            HttpMethod.DELETE, new HttpEntity<Object>(headers), String.class);
		
		
		assertEquals(size-1, hallService.findAllNotDeleted().size()); 
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Successfully deleted hall", responseEntity.getBody());
		
		Hall deleted = hallService.findOne(DB_HALL_TO_BE_DELETED2);
		assertEquals(DB_HALL_TO_BE_DELETED2, deleted.getId());
		assertTrue(deleted.isDeleted());
	}
	
	@Test
	public void testDeleteException() {
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/hall/"+DB_HALL_DELETED,
				HttpMethod.DELETE, new HttpEntity<Object>(headers), String.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertTrue(responseEntity.getBody().contains("Could not find requested hall"));
	}
	
	@Test
	@Transactional
	public void testCreate() throws ResourceNotFoundException {
		int size = hallService.findAllNotDeleted().size(); //sve neobrisane dvorane
		ArrayList<SectorDTO> sectors = new ArrayList<>();
		SittingSectorDTO sit = new SittingSectorDTO("sit123", 9, 12);
		StandingSectorDTO stand = new StandingSectorDTO("stand123", 1000);
		sectors.add(sit);
		sectors.add(stand);
		NEW_HALL_DTO.setSectors(sectors);
		System.out.println(NEW_HALL_DTO.getSectors().size());
		HttpEntity<HallDTO> httpEntity = new HttpEntity<HallDTO>(NEW_HALL_DTO, headers);
		
		ResponseEntity<Hall> responseEntity = restTemplate.exchange("/api/hall/"+NEW_HALL_LOCATION_ID, HttpMethod.POST, httpEntity, Hall.class);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Hall created = responseEntity.getBody();
		assertNotNull(created);
		assertEquals(NEW_HALL_DTO.getName(), created.getName());
		
		assertFalse(created.isDeleted());
		assertFalse(created.getSectors().isEmpty());
		assertEquals(sectors.size(), created.getSectors().size());
		
		Hall found = hallService.findOne(created.getId());
		assertNotNull(found);
		assertEquals(NEW_HALL_DTO.getName(), found.getName());
		assertFalse(found.isDeleted());
		assertEquals(NEW_HALL_LOCATION_ID, found.getLocation().getId());
		assertFalse(found.getSectors().isEmpty());
		assertEquals(sectors.size(), found.getSectors().size());
		assertEquals(size+1, hallService.findAllNotDeleted().size());
		
		
	}
	
	@Test
	public void testCreate_LocationNotFound() {
		ArrayList<SectorDTO> sectors = new ArrayList<>();
		SittingSectorDTO sit = new SittingSectorDTO("sit123", 9, 12);
		StandingSectorDTO stand = new StandingSectorDTO("stand123", 1000);
		sectors.add(sit);
		sectors.add(sit);
		NEW_HALL_DTO.setSectors(sectors);
		HttpEntity<HallDTO> httpEntity = new HttpEntity<HallDTO>(NEW_HALL_DTO, headers);
		
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/hall/"+LOCATION_ID_NON_EXISTENT, HttpMethod.POST, httpEntity, String.class);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertTrue(responseEntity.getBody().contains("Could not find requested location"));
	}

	@Test
	public void testUpdate() throws ResourceNotFoundException {
		int size = hallService.findAll().size();
		HallUpdateDTO dto = new HallUpdateDTO("novo ime");
		HttpEntity<HallUpdateDTO> httpEntity = new HttpEntity<HallUpdateDTO>(dto, headers);
		
		ResponseEntity<Hall> responseEntity = restTemplate.exchange("/api/hall/"+DB_HALL_TO_BE_UPDATED, HttpMethod.PUT, httpEntity, Hall.class);
		Hall updated = responseEntity.getBody();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(updated);
		assertEquals(dto.getName(), updated.getName());
		assertFalse(updated.isDeleted());
		
		Hall found = hallService.findOne(DB_HALL_TO_BE_UPDATED);
		assertNotNull(found);
		assertEquals(dto.getName(), found.getName());
		assertFalse(found.isDeleted());
		assertEquals(size, hallService.findAll().size()); //nije se dodavao novi vec je izmenjen postojeci
	}
	
	@Test 
	public void testUpdate_NotFoundException() {
		HallUpdateDTO dto = new HallUpdateDTO("novo ime");
		HttpEntity<HallUpdateDTO> httpEntity = new HttpEntity<HallUpdateDTO>(dto, headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange("/api/hall/"+HALL_ID_NON_EXISTENT, HttpMethod.PUT, httpEntity, String.class);
		System.out.println("******upd not found");
		System.out.println(responseEntity.getBody());
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertTrue(responseEntity.getBody().contains("Could not find requested hall"));
	}
//	
//	
//	@Test
//	@Ignore
//	public void testGetAllAddressesPageable() {
//		
//	}
}
