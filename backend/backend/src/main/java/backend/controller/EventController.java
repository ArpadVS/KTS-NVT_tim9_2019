package backend.controller;
//can copypaste everywhere
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import backend.converters.EventConverter;
<<<<<<< HEAD
import backend.converters.UserConverter;
import backend.dto.*;
=======
import backend.dto.EventDTO;
import backend.model.Event;
import backend.service.EventService;
import backend.service.FileUploadService;
>>>>>>> master

@RestController
@RequestMapping("/api/event")
public class EventController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	EventService eventService;
	
	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	EventConverter eventConverter;
	/* saving event */
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SYS_ADMIN')")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Event createEvent(@Valid @RequestBody EventDTO dto) {
		Event event = eventConverter.EventDTO2Event(dto);
		return eventService.save(event);
	}
	

	/* get all events, permitted for all */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Event> getAllEventes() {
		return eventService.findAll();
	}

	/* get an event by id, permitted for all */
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> getEvent(
			@PathVariable(value = "id") Long eventId) {
		Event event = eventService.findOne(eventId);

		if (event == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(event);
	}
	
	/* update event by id */
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SYS_ADMIN')")
	@PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> updateEvent(
			@PathVariable(value = "id") Long eventId,
			@Valid @RequestBody EventDTO dto) {
		Event e = eventConverter.EventDTO2Event(dto);
		return eventService.update(eventId, e);
	}
	

	
	/*add image to event*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping(value = "/addImage/{id}",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EventDTO> addImage(@PathVariable("id") Long eventId,@RequestParam("file")MultipartFile file) {
		Event event = eventService.findOne(eventId);
		event.getImagePaths().add(fileUploadService.imageUpload(file));
		eventService.save(event);
		return new ResponseEntity<>(EventConverter.Event2EventDTO(event), HttpStatus.OK);
		
	}
	
	/*add video to event*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping(value = "/addVideo/{id}",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EventDTO> addVideo(@PathVariable("id") Long eventId,@RequestParam("file")MultipartFile file) {
		Event event = eventService.findOne(eventId);
		event.getVideoPaths().add(fileUploadService.videoUpload(file));
		eventService.save(event);
		return new ResponseEntity<>(EventConverter.Event2EventDTO(event), HttpStatus.OK);
		
	}
	
	
	

	/* delete event */
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SYS_ADMIN')")
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteEvent(
			@PathVariable(value = "id") Long eventId) {
		logger.debug("Deleted" + eventId);
		return eventService.delete(eventId);
	}
}
