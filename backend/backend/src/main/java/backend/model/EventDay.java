package backend.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "event_days")
public class EventDay {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false, length = 70)
	private String name;

	@Column(name = "description", nullable = true)
	private String description;

	@Column(name = "day_date", nullable = false)
	private Date date;

	@Column(name = "status", nullable = true)
	private EventStatus status;

	@OneToMany(mappedBy = "eventDay", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnoreProperties("eventDay")
	@JsonBackReference
	private Set<Ticket> tickets = new HashSet<>();

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JsonIgnoreProperties("eventDays")
	private Event event;

	public EventDay() {
		super();
	}

	public EventDay(Long id, String name, String description, Date date,
			EventStatus status, Set<Ticket> tickets, Event event) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.date = date;
		this.status = status;
		this.tickets = tickets;
		this.event = event;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public EventStatus getStatus() {
		return status;
	}

	public void setStatus(EventStatus status) {
		this.status = status;
	}

	public Set<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(Set<Ticket> tickets) {
		this.tickets = tickets;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	@Override
	public String toString() {
		return "EventDay [id=" + id + ", name=" + name + ", description="
				+ description + ", date=" + date + ", status=" + status
				+ ", tickets=" + tickets + ", event=" + event + "]";
	}

}
