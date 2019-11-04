package backend.model;

import javax.persistence.*;

import backend.dto.AddressDTO;

@Entity
@Table(name = "adresses")
public class Address {
	private static final String googleMapsAPIKey = "insertKeyHere";

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "street_name", nullable = true, length = 100)
	private String streetName;
	@Column(name = "street_number", nullable = true)
	private int streetNumber;
	@Column(name = "city", nullable = true, length = 70)
	private String city;
	@Column(name = "country", nullable = true, length = 40)
	private String country;
	@Column(name = "latitude", nullable = false)
	private double latitude;
	@Column(name = "longitude", nullable = false)
	private double longitude;

	public Address() {
		super();
	}

	public Address(AddressDTO a) {
		super();
		this.id = a.getId();
		this.streetName = a.getStreetName();
		this.streetNumber = a.getStreetNumber();
		this.city = a.getCity();
		this.country = a.getCountry();
		this.latitude = a.getLatitude();
		this.longitude = a.getLongitude();
	}

	public Address(Address a) {
		super();
		this.id = a.getId();
		this.streetName = a.getStreetName();
		this.streetNumber = a.getStreetNumber();
		this.city = a.getCity();
		this.country = a.getCountry();
		this.latitude = a.getLatitude();
		this.longitude = a.getLongitude();
	}

	public Address(Long id, String streetName, int streetNumber, String city,
			String country, double latitude, double longitude) {
		super();
		this.id = id;
		this.streetName = streetName;
		this.streetNumber = streetNumber;
		this.city = city;
		this.country = country;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public int getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(int streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public static String getGooglemapsapikey() {
		return googleMapsAPIKey;
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", streetName=" + streetName
				+ ", streetNumber=" + streetNumber + ", city=" + city
				+ ", country=" + country + ", latitude=" + latitude
				+ ", longitude=" + longitude + "]";
	}

}