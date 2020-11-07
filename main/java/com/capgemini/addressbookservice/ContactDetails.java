package com.capgemini.addressbookservice;

public class ContactDetails {
	private int id;
	private String firstName;
	private String lastName, emailId, city;
	private String address, zip, state;
	private String phoneNumber;
	private String addressType, address_name;
	private String date;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getCity() {
		return city;
	}

	public void setCityName(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getAddress_name() {
		return address_name;
	}

	public void setAddress_name(String address_name) {
		this.address_name = address_name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public ContactDetails(int id, String firstName, String lastName, String address, String city, String state,
			String zip, String phoneNumber, String emailId) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNumber = phoneNumber;
		this.emailId = emailId;
	}

	public ContactDetails(String firstName, String lastName, String address_name, String addressType, String address,
			String city, String state, String zip, String phoneNo, String email, String date) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = email;
		this.city = city;
		this.address = address;
		this.zip = zip;
		this.state = state;
		this.phoneNumber = phoneNo;
		this.addressType = addressType;
		this.address_name = address_name;
	}

	public ContactDetails(int id, String firstName, String lastName, String address_name, String addressType,
			String address, String city, String state, String zip, String phoneNumber, String emailId) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.city = city;
		this.address = address;
		this.zip = zip;
		this.state = state;
		this.phoneNumber = phoneNumber;
		this.addressType = addressType;
		this.address_name = address_name;
	}

	@Override
	public String toString() {
		return "Contacts [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", emailId=" + emailId
				+ ", city=" + city + ", address=" + address + ", zip=" + zip + ", state=" + state + ", phoneNumber="
				+ phoneNumber + ", addressType=" + addressType + ", address_name=" + address_name + "]";
	}

	public boolean equals(Object obj) {
		ContactDetails conObj = (ContactDetails) obj;
		if (conObj.getFirstName().equals(this.firstName) && conObj.getLastName().equals(this.lastName))
			return true;
		else
			return false;
	}
}