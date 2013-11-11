package com.example.observationreunion;

public class Group {

	private int _ID_group;
	private String group_name;
	private String _ID_contact;
 
	public Group(){}
 
	public Group(String group_name, String _ID_contact){
		this.group_name = group_name;
		this._ID_contact = _ID_contact;
	}
 
	public int get_ID_group() {
		return _ID_group;
	}
 
	public void set_ID_group(int _ID_group) {
		this._ID_group = _ID_group;
	}
 
	public String getgroup_name() {
		return group_name;
	}
 
	public void setgroup_name(String group_name) {
		this.group_name = group_name;
	}
 
	public String get_ID_contact() {
		return _ID_contact;
	}
 
	public void set_ID_contact(String _ID_contact) {
		this._ID_contact = _ID_contact;
	}
 
	public String toString(){
		return "_ID_group : " + _ID_contact + "\ngroup_name : " + group_name + "\n_ID_contact : " + _ID_contact;
	}
	
}
	

