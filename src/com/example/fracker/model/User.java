package com.example.fracker.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

	@Expose
	@SerializedName("id")
	private long _id;

	@Expose
	@SerializedName("username")
	String _username;

	@Expose
	@SerializedName("password")
	String _password;

	@Expose
	@SerializedName("first_name")
	String _first_name;

	@Expose
	@SerializedName("last_name")
	String _last_name;

	@Expose
	@SerializedName("lat")
	String _lat;

	@Expose
	@SerializedName("lon")
	String _lon;

	long _timestamp;

	@SerializedName("groups")
	List<Group> _groups;

	public User(String username, String password) {
		_username = username;
		_password = password;
	}

	public User(String username, String password, String first_name,
			String last_name) {
		_username = username;
		_password = password;
		_first_name = first_name;
		_last_name = last_name;
	}

	public User(String username, String lat, String lon) {
		_username = username;
		_lat = lat;
		_lon = lon;
		
	}
	
	public long getId() {
		return _id;
	}

	public String getUsername() {
		return _username;
	}

	public void setUsername(String _username) {
		this._username = _username;
	}

	public String getFirst_name() {
		return _first_name;
	}

	public void setFirst_name(String _first_name) {
		this._first_name = _first_name;
	}

	public String getLast_name() {
		return _last_name;
	}

	public void setLast_name(String _last_name) {
		this._last_name = _last_name;
	}

	public String getPassword() {
		return _password;
	}

	public void setPassword(String _password) {
		this._password = _password;
	}

	public String getLat() {
		return _lat;
	}

	public void setLat(String _lat) {
		this._lat = _lat;
	}

	public String getLon() {
		return _lon;
	}

	public void setLon(String _lon) {
		this._lon = _lon;
	}

	public long getTimestamp() {
		return _timestamp;
	}

	public void setTimestamp(long _timestamp) {
		this._timestamp = _timestamp;
	}

	public List<Group> getGroups() {
		return _groups;
	}

	public void setGroups(List<Group> _groups) {
		this._groups = _groups;
	}

	@Override
	public String toString() {

		return String.format("Username: %s Lat: %s Lon: %s", _username, _lat,
				_lon);
	}
}
