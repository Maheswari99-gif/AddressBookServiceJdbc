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
						resultSet.getString("stateName"), resultSet.getString("zipCode"), resultSet.getString("phoneNumber"),
						resultSet.getString("emailId")));
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
}
