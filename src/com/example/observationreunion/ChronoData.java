package com.example.observationreunion;

public class ChronoData {

	private long basetime = 0;
	private long whenstop = 0;
	private String statement = "stop";
	private Boolean neverUsed;
	
	public ChronoData(long _basetime, long _whenstop, String _statement){
		basetime = _basetime;
		whenstop = _whenstop;
		statement = _statement;
		neverUsed = true;
	}
	
	public ChronoData(ChronoData oldchronodata, String _statement){
		//créer un ChronoData avec l'inverse du statement du ChronoData rentré
		//en paramètre et neverUsed à false
		basetime = oldchronodata.basetime;
		whenstop = oldchronodata.whenstop;
		statement = _statement;
		neverUsed = false;
	}
	
	public ChronoData(String _statement){
		statement = _statement;
		neverUsed = false;
	}
	
	public long GetBaseTime(){
		return basetime;
	}
	
	public long GetWhenStop(){
		return whenstop;
	}
	
	public String GetStatement(){
		return statement;
	}
	
	public Boolean GetNeverUsed(){
		return neverUsed;
	}
	
	public void SetBaseTime(long _basetime){
		basetime = _basetime;
	}
	
	public void SetWhenStop(long _whenstop){
		whenstop = _whenstop;
	}
	
	public void SetStatement(String _statement){
		statement = _statement;
	}
	
	public void SetNeverUsed(Boolean _neverUsed){
		neverUsed = _neverUsed;
	}
}
