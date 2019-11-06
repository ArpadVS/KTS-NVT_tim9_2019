package backend.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@DiscriminatorValue("user")
public class RegisteredUser extends User {
	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnoreProperties("buyer")
	//@JsonBackReference
	private Set<Reservation> reservations = new HashSet<>();

	public RegisteredUser() {
		super();
	}

	public RegisteredUser(Set<Reservation> reservations) {
		super();
		this.reservations = reservations;
	}

	public Set<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(Set<Reservation> reservations) {
		this.reservations = reservations;
	}

}
