package com.capgemini.addressbookservice;

public class DBException extends Exception {
	DBExceptionType exceptionType;

	public DBException(String message, DBExceptionType exceptionType) {
		super(message);
		this.exceptionType = exceptionType;
	}
}

enum DBExceptionType {
	SQL_EXCEPTION, CLASSNOTFOUNDEXCEPTION
}
