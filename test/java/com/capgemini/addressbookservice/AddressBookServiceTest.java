package com.capgemini.addressbookservice;

import static org.junit.Assert.assertEquals;

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

}

