package backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import backend.model.EventDay;
import backend.model.EventSector;
import backend.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

	//public List<Ticket> findAllByEventDayEventSector(EventDay event_day, EventSector event_sector);
	
	@Query("select t from Ticket t where t.eventDay.id = ?1 and t.eventSector.id = ?2 and t.deleted = false")
	public List<Ticket> findAllByEventDayIDEventSectorID(Long ed_id, Long es_id);
	
	@Query("select t from Ticket t where t.eventDay.event.location.id = ?1 and t.deleted = false")
	public List<Ticket> findAllByLocation(Long loc_id);
}
