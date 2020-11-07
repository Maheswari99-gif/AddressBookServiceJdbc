package com.capgemini.addressbookservice;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressBookService {
	ContactDetails contactDetails = null;
	private List<ContactDetails> contactList;

	public AddressBookService(List<ContactDetails> contactList) {
		this.contactList = new ArrayList<>(contactList);
	}

	private List<ContactDetails> getResultSet(ResultSet resultSet) throws DBException {
		List<ContactDetails> contactList = new ArrayList<>();
		try {
			while (resultSet.next()) {
				contactList.add(new ContactDetails(resultSet.getInt("id"), resultSet.getString("firstName"),
						resultSet.getString("lastName"), resultSet.getString("address"), resultSet.getString("city"),
						resultSet.getString("stateName"), resultSet.getString("zipCode"),
						resultSet.getString("phoneNumber"), resultSet.getString("emailId")));
			}
		} catch (Exception e) {
			throw new DBException("SQL Exception", DBExceptionType.SQL_EXCEPTION);
		}
		return contactList;
	}

	/**
	 * UC16
	 */
	public List<ContactDetails> viewAddressBookService() throws DBException {
		List<ContactDetails> contactsList = new ArrayList<>();
		String query = "select * from addressbookservice";
		try (Connection con = JDBCDemo.getConnection()) {
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			contactsList = getResultSet(resultSet);
		} catch (Exception e) {
			throw new DBException("SQL Exception", DBExceptionType.SQL_EXCEPTION);
		}
		return contactsList;
	}

	/**
	 * usecase17
	 * 
	 * @param fName
	 * @return
	 * @throws DBServiceException
	 */
	public List<ContactDetails> getContactsByName(String firstName) throws DBException {
		List<ContactDetails> contactsListByName = new ArrayList<>();
		String query = "select * from address_book where firstName = ?";
		try (Connection con = JDBCDemo.getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, firstName);
			ResultSet resultSet = preparedStatement.executeQuery();
			contactsListByName = getResultSet(resultSet);
		} catch (Exception e) {
			throw new DBException("SQL Exception", DBExceptionType.SQL_EXCEPTION);
		}
		System.out.println(contactsListByName);
		return contactsListByName;
	}

	public void updateContactDetails(String state, String zip, String firstName) throws DBException {
		String query = "update addressbookservice set state = ? , zip = ? where first_name = ?";
		try (Connection con = JDBCDemo.getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, state);
			preparedStatement.setString(2, zip);
			preparedStatement.setString(3, firstName);
			int result = preparedStatement.executeUpdate();
			contactDetails = getContactDetails(firstName);
			if (result > 0 && contactDetails != null) {
				contactDetails.setState(state);
				contactDetails.setZip(zip);
			}
		} catch (Exception e) {
			throw new DBException("SQL Exception", DBExceptionType.SQL_EXCEPTION);
		}
	}

	public ContactDetails getContactDetails(String firstName) throws DBException {
		return viewAddressBookService().stream().filter(e -> e.getFirstName().equals(firstName)).findFirst()
				.orElse(null);
	}

	public boolean isSynced(String firstName) throws DBException {
		try {
			return getContactsByName(firstName).get(0).equals(getContactDetails(firstName));
		} catch (IndexOutOfBoundsException e) {
		} catch (Exception e) {
			throw new DBException("SQL Exception", DBExceptionType.SQL_EXCEPTION);
		}
		return false;
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DBServiceException
	 */

	public List<ContactDetails> getContactsByDate(LocalDate startDate, LocalDate endDate) throws DBException {
		List<ContactDetails> contactsListByDate = new ArrayList<>();
		String query = "select * from addressbookservice where date between ? and  ?";
		try (Connection con = JDBCDemo.getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setDate(1, Date.valueOf(startDate));
			preparedStatement.setDate(2, Date.valueOf(endDate));
			ResultSet resultSet = preparedStatement.executeQuery();
			contactsListByDate = getResultSet(resultSet);
		} catch (Exception e) {
			throw new DBException("SQL Exception", DBExceptionType.SQL_EXCEPTION);
		}
		return contactsListByDate;
	}

	/**
	 * 
	 * @param column
	 * @return
	 * @throws DBServiceException
	 */

	public Map<String, Integer> countContactsByState(String column) throws DBException {
		Map<String, Integer> contactsCount = new HashMap<>();
		String query = String.format("SELECT state, COUNT(first_name) AS count FROM contact group by state;");
		try (Connection con = JDBCDemo.getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				contactsCount.put(resultSet.getString(1), resultSet.getInt(2));
			}
		} catch (Exception e) {
			throw new DBException("SQL Exception", DBExceptionType.SQL_EXCEPTION);
		}
		return contactsCount;
	}

	/**
	 * 
	 * @param firstName
	 * @param lastName
	 * @param address_name
	 * @param addressType
	 * @param address
	 * @param city
	 * @param state
	 * @param zip
	 * @param phoneNo
	 * @param email
	 * @param date
	 * @return
	 * @throws DBServiceException
	 */
	public List<ContactDetails> insertNewContacts(String firstName, String lastName, String address_name,
			String addressType, String address, String city, String state, String zip, String phoneNumber, String email,
			String date) throws DBException {
		String sql = String.format(
				"insert into address_book (firstName,lastName,address_name,address_type,address,city,state,zip,phoneNumber,email,date_added)"
						+ " values ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s');",
				firstName, lastName, address_name, addressType, address, city, state, zip, phoneNumber, email, date);
		try (Connection con = JDBCDemo.getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			int result = preparedStatement.executeUpdate();
			if (result == 1)
				contactDetails = new ContactDetails(firstName, lastName, address_name, addressType, address, city,
						state, zip, phoneNumber, email, date);
			viewAddressBookService().add(contactDetails);
		} catch (Exception e) {
			throw new DBException("SQL Exception", DBExceptionType.SQL_EXCEPTION);
		}
		return viewAddressBookService();
	}

	/**
	 * 
	 * @param contactList
	 * @throws DBException
	 */
	public void insertNewContactsWithThreads(List<ContactDetails> contactList) throws DBException {
		Map<Integer, Boolean> contactAditionStatus = new HashMap<>();
		contactList.forEach(contact -> {
			Runnable task = () -> {
				contactAditionStatus.put(contact.hashCode(), false);
				System.out.println("Contact which is  being added : " + contact.getFirstName());
				try {
					insertNewContacts(contact.getFirstName(), contact.getLastName(), contact.getAddress_name(),
							contact.getAddressType(), contact.getAddress(), contact.getCity(), contact.getState(),
							contact.getZip(), contact.getPhoneNumber(), contact.getEmailId(), contact.getDate());
				} catch (DBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				contactAditionStatus.put(contact.hashCode(), true);
				System.out.println("Contact added : " + contact.getFirstName());
			};
			Thread thread = new Thread(task, contact.getFirstName());
			thread.start();
		});

		while (contactAditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public long countEmployees() {
		return contactList.size();
	}

	public void addContactDetails(ContactDetails contact) {
		contactList.add(contact);
	}
}