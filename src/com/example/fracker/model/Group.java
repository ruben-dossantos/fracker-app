package com.example.fracker.model;

import java.util.List;

public class Group {

	String _name;
	
	String _password;
	
	List<User> _users;

	public Group(String groupName, String password) {
		_name = groupName;
		_password = password;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public String get_password() {
		return _password;
	}

	public void set_password(String _password) {
		this._password = _password;
	}

	public List<User> get_users() {
		return _users;
	}

	public void set_users(List<User> _users) {
		this._users = _users;
	}
	
	@Override
	public String toString() {
		
		return String.format("Group: %s", _name);
	}
	
}
