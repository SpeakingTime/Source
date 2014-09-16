package com.example.observationreunion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class BarGraph2  extends Activity{

	private String url;
    private WebView webView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
               
        setContentView(R.layout.activity_statistic_boxplot);
                
        webView = (WebView) findViewById(R.id.WebView);
        Post();
        
	}
	
	public void Post(){
		
		Bundle b = getIntent().getExtras();
    	String nbboxplots = b.getString("nbboxplots");
    	
    	int i = 0;
    	int j = 1;
    	url = "http://data-mysql.fr/boxplot/test5.php?nb=" + nbboxplots;
    	while (i < Integer.valueOf(nbboxplots) * 5 ) {
	    	String minValue = String.valueOf(getMinValue(String.valueOf(j)));
	    	String quartile1 = String.valueOf(getQuartile1(String.valueOf(j)));
	    	String median = String.valueOf(getMedian(String.valueOf(j)));
	    	String quartile3 = String.valueOf(getQuartile3(String.valueOf(j)));
	    	String maxValue = String.valueOf(getMaxValue(String.valueOf(j)));
	    	url = url + "&" + String.valueOf(i+1) + "=" + minValue + 
	    					"&" + String.valueOf(i+2) + "=" + quartile1 + 
	    					"&" + String.valueOf(i+3) + "=" + median +
	    					"&" + String.valueOf(i+4) + "=" + quartile3 + 
	    					"&" + String.valueOf(i+5) + "=" + maxValue;
	    	i = i + 5;
	    	j = j + 1;
    	}
    	
    	
    	System.out.println("url : " + url);
    	
	    webView.loadUrl(url);
	    
	}
	
	public List<Integer> getTimesList(String participantsWithSpeakingTime_Number){
		List<Integer> timesList = new ArrayList<Integer>();
    	Bundle b = getIntent().getExtras();
    	String s = b.getString("participantsWithSpeakingTime" + participantsWithSpeakingTime_Number);
    	Scanner scanner = new Scanner(s);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			int pos = line.indexOf(";", 0);
			String value = line.substring(pos+1, line.length()-1);
			int i_value = Integer.valueOf(value);
			timesList.add(i_value);
		}
		return timesList;
	}
	
	public Integer getQuartile1(String participantsWithSpeakingTime_Number){
		List<Integer> sortedValuesList = getTimesList(participantsWithSpeakingTime_Number);
		Collections.sort(sortedValuesList);
		
		Number[] numbers = new Number[sortedValuesList.size()];
		for (int i=0; i < sortedValuesList.size(); i++){
			numbers[i] = sortedValuesList.get(i);
		}
		
		System.out.println("quartile1 : " + StatisticsUtils.quartile1(numbers).intValue());
		return StatisticsUtils.quartile1(numbers).intValue();
	}
	
	public Integer getQuartile3(String participantsWithSpeakingTime_Number){
		List<Integer> sortedValuesList = getTimesList(participantsWithSpeakingTime_Number);
		Collections.sort(sortedValuesList);
		
		Number[] numbers = new Number[sortedValuesList.size()];
		for (int i=0; i < sortedValuesList.size(); i++){
			numbers[i] = sortedValuesList.get(i);
		}

		return StatisticsUtils.quartile3(numbers).intValue();
	}
	
	public Integer getMedian(String participantsWithSpeakingTime_Number){
		//List<Integer> sortedValuesList = new ArrayList<Integer>(Arrays.asList(235,235,237,238,238,239,239,239,240,241,250,251,251,253,253,255,255,255,257,260,241,243,245,247,247,249,250,250,250,250));//getTimesList();
		List<Integer> sortedValuesList = getTimesList(participantsWithSpeakingTime_Number);
		Collections.sort(sortedValuesList);
		
		Number[] numbers = new Number[sortedValuesList.size()];
		for (int i=0; i < sortedValuesList.size(); i++){
			numbers[i] = sortedValuesList.get(i);
		}

		return StatisticsUtils.median(numbers).intValue();

	}
	
	public Integer getMinValue(String participantsWithSpeakingTime_Number){
		//List<Integer> sortedValuesList = new ArrayList<Integer>(Arrays.asList(235,235,237,238,238,239,239,239,240,241,250,251,251,253,253,255,255,255,257,260,241,243,245,247,247,249,250,250,250,250));//getTimesList();
		List<Integer> sortedValuesList = getTimesList(participantsWithSpeakingTime_Number);
		Collections.sort(sortedValuesList);
		
		Number[] numbers = new Number[sortedValuesList.size()];
		for (int i=0; i < sortedValuesList.size(); i++){
			numbers[i] = sortedValuesList.get(i);
		}
		
		System.out.println("minValue : " + StatisticsUtils.minValue(numbers).intValue());
		return StatisticsUtils.minValue(numbers).intValue();
	}
	
	public Integer getMaxValue(String participantsWithSpeakingTime_Number){
		//List<Integer> sortedValuesList = new ArrayList<Integer>(Arrays.asList(235,235,237,238,238,239,239,239,240,241,250,251,251,253,253,255,255,255,257,260,241,243,245,247,247,249,250,250,250,250));//getTimesList();
		List<Integer> sortedValuesList = getTimesList(participantsWithSpeakingTime_Number);
		Collections.sort(sortedValuesList);
		
		Number[] numbers = new Number[sortedValuesList.size()];
		for (int i=0; i < sortedValuesList.size(); i++){
			numbers[i] = sortedValuesList.get(i);
		}
		
		return StatisticsUtils.maxValue(numbers).intValue();
	}
	
		
	
}
