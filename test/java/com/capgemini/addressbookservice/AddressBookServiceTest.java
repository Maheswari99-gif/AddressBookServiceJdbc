package com.capgemini.addressbookservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class AddressBookServiceTest {
	static AddressBookService serviceObj;
	static List<ContactDetails> contactsList;
	static Map<String, Integer> countOfContacts;

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
	@Test
	public void givenAddressDB_WhenRetrievedCountByCity_ShouldReturnCountGroupedByState() throws DBException {
		countOfContacts = serviceObj.countContactsByState("state");
		assertEquals(1, countOfContacts.get("State A"), 0);

	}
	@Test
	public void givenContactData_WhenAddedToDB_ShouldSyncWithDB() throws DBException {
		serviceObj.insertNewContacts("keerthi", "Guntupalli", "Friend_Book", "Friend", "Guntur", "AP", "AP", "534321",
				"123489764", "keerthi@gmail.com", "2017-05-09");
		boolean result = serviceObj.isSynced("Keerthi");
		assertTrue(result);
	}
	@Test
	public void givenMultipleContact_WhenAdded_ShouldSyncWithDB() throws DBException {
		ContactDetails[] contArr= {
								new ContactDetails("Mahi","Koyyani","Family_Book","Family","Palakollu",
										"AP","AP","123456","1200120012", "mahi@gmail.com","2014-04-04"),
								new ContactDetails("Hari","Koyyani","Family_Book","Family","Palakollu","AP",
										"AP","123456","1200120012","hari@gmail.com","2016-07-19"),
								new ContactDetails("Sri","Dokkala","Friend_Book","Friend","Palakollu","AP",
										"AP", "123456","1200120012","sri@yahoo.com","2018-06-05"),
		};
		serviceObj.insertNewContactsWithThreads(Arrays.asList(contArr));
		boolean result1 = serviceObj.isSynced("Mahi");
		boolean result2 = serviceObj.isSynced("Hari");
		boolean result3 = serviceObj.isSynced("Sri");
		assertTrue(result1);
		assertTrue(result2);
		assertTrue(result3);
	}


}

