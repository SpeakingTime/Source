package com.example.observationreunion;

public class ParticipantAndSpeakingTime {

	private String name;
	private Integer value;
	
	public ParticipantAndSpeakingTime(String name, Integer value){
		this.name = name;
		this.value = value;
	}

	public String getName(){
		return this.name;
	}
	
	public Integer getValue(){
		return this.value;
	}
	
	
}
