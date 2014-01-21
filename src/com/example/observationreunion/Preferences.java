package com.example.observationreunion;

public class Preferences {

	private int _ID_preferences;
	private String host;
	private String username;
	private String password;
 
	public Preferences(){}
 
	public Preferences(String host, String username, String password){
		this.host = host;
		this.username = username;
		this.password = password;
	}
 
	public int get_ID_preferences() {
		return _ID_preferences;
	}
 
	public void set_ID_preferences(int _ID_preferences) {
		this._ID_preferences = _ID_preferences;
	}
 
	public String gethost() {
		return host;
	}
 
	public void sethost(String host) {
		this.host = host;
	}
 
	public String getusername() {
		return username;
	}
 
	public void setusername(String username) {
		this.username = username;
	}
	
	public String getpassword() {
		return password;
	}
 
	public void setpassword(String password) {
		this.password = password;
	}
 
	public String toString(){
		return "_ID_preferences : " + _ID_preferences + "\nhost : " + host + "\nusername : " + username
				+ "\npassword" + password;
	}
	
}
