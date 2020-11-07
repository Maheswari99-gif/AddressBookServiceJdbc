package com.capgemini.addressbookservice;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AddressBookRestTest {
	@Before
	public void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}

	private ContactDetails[] getContactList() {
		Response response = RestAssured.get("/contacts");
		System.out.println("Contact entries in JSON Server :\n" + response.asString());
		ContactDetails[] arrayOfContacts = new Gson().fromJson(response.asString(), ContactDetails[].class);
		return arrayOfContacts;
	}

	@Test
	public void givenAddressBookDataWhenRetrievedShouldMatchNoofEntries() {
		AddressBookService addressBookService;
		ContactDetails[] arrayOfContacts = getContactList();
		addressBookService = new AddressBookService(Arrays.asList(arrayOfContacts));
		long entries = addressBookService.countEmployees();
		assertEquals(3, entries);
	}
	private Response addContactToJsonServer(ContactDetails contact) {
		String contactJson = new Gson().toJson(contact);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(contactJson);
		return request.post("/contacts");
	}

	@Test
	public void givenContactListWhenAddedShouldMatchResponseCode() {
		AddressBookService addressBookService;
		ContactDetails[] arrayOfContacts = getContactList();
		addressBookService = new AddressBookService(Arrays.asList(arrayOfContacts));
		List<ContactDetails> contactList = new ArrayList<>();
		contactList.add(new ContactDetails(0, "Manasa", "Kakumanu", "Kukatpally", "Hyderabad", "Telangana", "123456", "123456789",
				"manu@gmail.com"));
		contactList.add(new ContactDetails(0, "Chandana", "Bandikallu", "Kukatpally", "Hyderabad", "Telagana","123456", "123456789",
				"chandana@gmail.com"));
		contactList.add(new ContactDetails(0, "Mounica", "Kasu", "Kukatpally", "Hyderabad", "Telangana","123456", "123457689",
				"monica@gmail.com"));
		for (ContactDetails contact : contactList) {
			Response response = addContactToJsonServer(contact);
			int statusCode = response.getStatusCode();
			assertEquals(201, statusCode);
			contact = new Gson().fromJson(response.asString(), ContactDetails.class);
			addressBookService.addContactDetails(contact);
		}
		long entries = addressBookService.countEmployees();
		assertEquals(6, entries);
	}
}