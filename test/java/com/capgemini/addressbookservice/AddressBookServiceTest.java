package com.capgemini.addressbookservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class AddressBookServiceTest {
	static AddressBookService serviceObj;
	static List<ContactDetails> contactsList;

	@BeforeClass
	public static void setUp() {
		//serviceObj = new AddressBookService();
		contactsList = new ArrayList<>();
	}

	@Test
	public void givenAddressBookDB_WhenRetrieved_ShouldMatchContactsCount() throws DBException {
		contactsList = serviceObj.viewAddressBookService();
		assertEquals(6, contactsList.size());
	}
	@Test
	public void givenUpdatedContacts_WhenRetrieved_ShouldBeSyncedWithDB() throws DBException {
		serviceObj.updateContactDetails("City D", "986754", "Ambani");
		boolean result = serviceObj.isSynced("Ambani");
		assertTrue(result);
	}
	@Test
	public void givenDateRange_WhenRetrieved_ShouldMatchContactsCount() throws DBException{
		contactsList = serviceObj.getContactsByDate(LocalDate.of(2015,01,01), LocalDate.now() );
		assertEquals(3, contactsList.size());
	}


}

