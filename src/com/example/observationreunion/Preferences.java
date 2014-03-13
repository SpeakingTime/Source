package com.example.observationreunion;

public class Preferences {

	private int _ID_preferences;
	private String host;
	private String username;
	private String timeintervalforsavefile;

	public Preferences(){}
 
	public Preferences(String host, String username, String timeintervalforsavefile){
		this.host = host;
		this.username = username;
		this.timeintervalforsavefile = timeintervalforsavefile;
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

	public String gettimeintervalforsavefile() {
		return timeintervalforsavefile;
	}
 
	public void settimeintervalforsavefile(String timeintervalforsavefile) {
		this.timeintervalforsavefile = timeintervalforsavefile;
	}
	
	public String toString(){
		return "_ID_preferences : " + _ID_preferences + "\nhost : " + host + "\nusername : " + username;
	}
	
}
