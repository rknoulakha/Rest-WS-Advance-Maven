package com.test.model;

import java.util.HashMap;
import java.util.Map;

import com.test.beans.User;

public class Database {

	private static Map<Long, User> user = new HashMap<Long, User>();

	public static Map<Long, User> getUser() {
		return user;
	}

}
