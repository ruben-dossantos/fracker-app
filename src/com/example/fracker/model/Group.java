package com.example.fracker.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Group {

	@Expose
	@SerializedName("id")
	private long _id;

	@Expose
	@SerializedName("name")
	String _name;

	@Expose
	@SerializedName("password")
	String _password;

	@SerializedName("users")
	List<User> _users;

	public Group(String groupName, String password) {
		_name = groupName;
		_password = password;
	}

	public long getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}

	public void setName(String _name) {
		this._name = _name;
	}

	public String getPassword() {
		return _password;
	}

	public void setPassword(String _password) {
		this._password = _password;
	}

	public List<User> getUsers() {
		return _users;
	}

	public void setUsers(List<User> _users) {
		this._users = _users;
	}

	@Override
	public String toString() {

		return String.format("%s", _name);
	}

}
