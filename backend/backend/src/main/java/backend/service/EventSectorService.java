package backend.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import backend.exceptions.ResourceNotFoundException;
import backend.model.Address;
import backend.model.EventSector;
import backend.repository.EventSectorRepository;

@Service
public class EventSectorService {
	@Autowired
	private EventSectorRepository eventSectorRepository;

	public EventSector save(EventSector b) {
		return eventSectorRepository.save(b);
	}

	public EventSector findOne(Long id) throws ResourceNotFoundException {
		return eventSectorRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Could not find requested event sector"));
	}

	public EventSector findOneNotDeleted(Long id) throws ResourceNotFoundException {
		EventSector es = eventSectorRepository.findByIdAndDeleted(id, false)
				.orElseThrow(() -> new ResourceNotFoundException("Could not find requested event sector"));
		return es;
	}

	public List<EventSector> findAllNotDeleted() {
		return eventSectorRepository.findAllByDeleted(false);
	}

	public List<EventSector> findAll() {
		return eventSectorRepository.findAll();
	}

	public Page<EventSector> findAllNotDeleted(Pageable page){
		return eventSectorRepository.findAllByDeleted(false, page);
	}
	
	public Page<EventSector> findAll(Pageable page) {
		return eventSectorRepository.findAll(page);
	}

	@Transactional
	public void remove(Long id) {
		eventSectorRepository.deleteById(id);
	}

	public void delete(Long ID) throws ResourceNotFoundException {
		EventSector es = findOneNotDeleted(ID);
		save(es);
	}

	public EventSector update(Long id, double price) throws ResourceNotFoundException {
		EventSector es = findOneNotDeleted(id);
		es.setPrice(price);
		return save(es);

	}
}
