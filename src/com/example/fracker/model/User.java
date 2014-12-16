package com.example.fracker.model;

import java.util.List;

public class User {

	String _username;
	
	String _first_name;
	
	String _last_name;
	
	String _password;
	
	String _lat; 
	
	String _lon;
	
	long _timestamp; 
	
	List<Group> _groups;
	
	public String get_username() {
		return _username;
	}
	
	public void set_username(String _username) {
		this._username = _username;
	}
	
	public String get_first_name() {
		return _first_name;
	}
	
	public void set_first_name(String _first_name) {
		this._first_name = _first_name;
	}
	
	public String get_last_name() {
		return _last_name;
	}
	
	public void set_last_name(String _last_name) {
		this._last_name = _last_name;
	}
	
	public String get_password() {
		return _password;
	}
	
	public void set_password(String _password) {
		this._password = _password;
	}
	
	public String get_lat() {
		return _lat;
	}
	
	public void set_lat(String _lat) {
		this._lat = _lat;
	}
	
	public String get_lon() {
		return _lon;
	}
	
	public void set_lon(String _lon) {
		this._lon = _lon;
	}
	
	public long get_timestamp() {
		return _timestamp;
	}
	
	public void set_timestamp(long _timestamp) {
		this._timestamp = _timestamp;
	}
	
	public List<Group> get_groups() {
		return _groups;
	}
	
	public void set_groups(List<Group> _groups) {
		this._groups = _groups;
	} 
	
	@Override
	public String toString() {
		
		return String.format("Username: %s Lat: %s Lon: %s", _username, _lat, _lon);
	}
}
