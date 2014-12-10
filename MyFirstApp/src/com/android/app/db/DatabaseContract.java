package com.android.app.db;

import android.provider.BaseColumns;

public class DatabaseContract {

	// To prevent someone from accidentally instantiating the contract class,
	// give it an empty constructor.
	public DatabaseContract() {
	}

	/* Inner class that defines the table contents */
	public static abstract class User implements BaseColumns {
		public static final String TABLE_NAME = "users";
		public static final String COLUMN_USER_NAME = "username";
		public static final String COLUMN_EMAIL = "email";
		public static final String COLUMN_PASSWORD = "password";
		public static final String COLUMN_Gender = "gender";

	}
}