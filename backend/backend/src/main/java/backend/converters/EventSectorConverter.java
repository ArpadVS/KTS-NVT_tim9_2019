package backend.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backend.dto.EventSectorDTO;
import backend.exceptions.ResourceNotFoundException;
import backend.model.Event;
import backend.model.EventSector;
import backend.service.EventService;
import backend.service.SectorService;

@Component
public class EventSectorConverter {
	
	@Autowired
	SectorService sectorService;
	
	@Autowired
	EventService eventService;
	
	public EventSector EventSectorDTO2EventSector(EventSectorDTO dto) throws ResourceNotFoundException {
		EventSector es = new EventSector();
		es.setPrice(dto.getPrice());
		es.setSector(sectorService.findOne(dto.getSector_id()));
		
		return es;
	}
	
	public EventSector EventSectorDTO2EventSector2(EventSectorDTO dto) throws ResourceNotFoundException {
		EventSector es = new EventSector();
		Event e = eventService.findOneNotDeleted(dto.getEvent_id());
		es.setEvent(e);
		e.getEventSectors().add(es);
		es.setPrice(dto.getPrice());
		es.setSector(sectorService.findOneNotDeleted(dto.getSector_id()));
		
		return es;
	}
}
