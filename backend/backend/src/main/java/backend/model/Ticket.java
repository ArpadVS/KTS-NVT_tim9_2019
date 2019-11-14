package backend.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "tickets")
public class Ticket {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "", nullable = true)
	private boolean hasSeat;

	@Column(name = "", nullable = true)
	private int numRow;
	@Column(name = "", nullable = true)
	private int numCol;

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JsonIgnoreProperties("tickets")
	private EventDay eventDay;

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JsonIgnoreProperties("tickets")
	private Reservation reservation;

	@ManyToOne
	@JoinColumn(name = "sector_id")
	private EventSector eventSector;

	@Column(name = "deleted", nullable = false)
	private boolean deleted = false;
	
	public Ticket() {
		super();
	}

	public Ticket(Long id, boolean hasSeat, int numRow, int numCol,
			EventDay eventDay, Reservation reservation, EventSector sector) {
		super();
		this.id = id;
		this.hasSeat = hasSeat;
		this.numRow = numRow;
		this.numCol = numCol;
		this.eventDay = eventDay;
		this.reservation = reservation;
		this.eventSector = sector;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isHasSeat() {
		return hasSeat;
	}

	public void setHasSeat(boolean hasSeat) {
		this.hasSeat = hasSeat;
	}

	public int getNumRow() {
		return numRow;
	}

	public void setNumRow(int numRow) {
		this.numRow = numRow;
	}

	public int getNumCol() {
		return numCol;
	}

	public void setNumCol(int numCol) {
		this.numCol = numCol;
	}

	public EventDay getEventDay() {
		return eventDay;
	}

	public void setEventDay(EventDay eventDay) {
		this.eventDay = eventDay;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}

	public EventSector getEventSector() {
		return eventSector;
	}

	public void setEventSector(EventSector sector) {
		this.eventSector = sector;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
