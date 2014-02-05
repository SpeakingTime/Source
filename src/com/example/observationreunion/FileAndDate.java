package com.example.observationreunion;

import java.util.Date;

public class FileAndDate {

	private String name;
	private Date date;
	
	public FileAndDate(String name, Date date){
		this.name = name;
		this.date = date;
	}

	public String getName(){
		return this.name;
	}
	
	public Date getDate(){
		return this.date;
	}
	
}
