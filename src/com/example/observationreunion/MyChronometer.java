package com.example.observationreunion;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.Chronometer;

public class MyChronometer extends Chronometer{

	long timeWhenStopped = 0;
	
	public MyChronometer(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

	}
	
	public MyChronometer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public MyChronometer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public long GettimeWhenStopped(){
		return timeWhenStopped;
	}
	
	public void SettimeWhenStopped(long _timeWhenStopped){
		timeWhenStopped = _timeWhenStopped;
	}
	
}
