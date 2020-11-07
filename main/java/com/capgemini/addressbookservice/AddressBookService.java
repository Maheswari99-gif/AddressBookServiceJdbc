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
		String query = "update address_book set state = ? , zip = ? where first_name = ?";
		try (Connection con = JDBCDemo.getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, state);
			preparedStatement.setString(2, zip);
			preparedStatement.setString(3, firstName);
			int result = preparedStatement.executeUpdate();
			contactDetails = getContactDetails(firstName);
			if (result > 0 && contactObj != null) {
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
}
