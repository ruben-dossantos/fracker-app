package com.example.fracker.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Friend {
	
	@Expose
	@SerializedName("username")
	private
	String _name;
	
	@Expose
	@SerializedName("distance")
	private
	double _distance;
	
	public Friend(String name, double distance){
		_name = name;
		_distance = distance;
	}

	public double get_distance() {
		return _distance;
	}

	void set_distance(double _distance) {
		this._distance = _distance;
	}

	public String get_name() {
		return _name;
	}

	void set_name(String _name) {
		this._name = _name;
	}

	@Override
	public String toString() {

		return String.format("%s", _name);
	}
}
