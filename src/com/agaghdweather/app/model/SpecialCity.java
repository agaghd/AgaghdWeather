package com.agaghdweather.app.model;

public class SpecialCity {
	
	String countyName;
	String countyCode;
	
	public SpecialCity(String countyName, String countyCode) {
		super();
		this.countyName = countyName;
		this.countyCode = countyCode;
	}
	public SpecialCity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public String getCountyCode() {
		return countyCode;
	}
	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}
	
}
