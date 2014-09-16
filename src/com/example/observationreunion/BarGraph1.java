package com.example.observationreunion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

public class BarGraph1 {
	
	List<ParticipantAndSpeakingTime> participantAndSpeakingTimeList = new ArrayList<ParticipantAndSpeakingTime>();
	int timeIntervalInSec = -1;
	
	public BarGraph1(String participantsWithSpeakingTime, int timeIntervalInMin) {
		// TODO Auto-generated constructor stub
		participantAndSpeakingTimeList = getParticipantsAndSpeakingTimes(participantsWithSpeakingTime);
		timeIntervalInSec = timeIntervalInMin * 60;
	}
	
public Intent getIntent(Context context){
		
		List<Integer> MyListValue = new ArrayList<Integer>();
		
		MyListValue = getRangeSpeakingTimeList();
				
		int[] y = new int[MyListValue.size()];
		for (int i = 0; i < MyListValue.size(); i++) 
			y[i] = MyListValue.get(i);
	       
        CategorySeries series = new CategorySeries("Time class in secondes");
        for(int i=0; i < y.length; i++){
            series.add("Bar"+(i+1),y[i]);
        }
       
        XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();  // collection of series under one object.,there could any
        dataSet.addSeries(series.toXYSeries());                            // number of series
       
        //customization of the chart
   
        XYSeriesRenderer renderer = new XYSeriesRenderer();     // one renderer for one series
        renderer.setColor(Color.parseColor("#0099FF"));
        renderer.setDisplayChartValues(true);
        renderer.setChartValuesSpacing((float) 5.5d);
        renderer.setLineWidth((float) 10.5d);
           
       
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();   // collection multiple values for one renderer or series
        mRenderer.addSeriesRenderer(renderer);
        mRenderer.setChartTitle("Number of partipants per time class");
//        mRenderer.setXTitle("xValues");
        mRenderer.setYTitle("Number of partipants");
        mRenderer.setZoomButtonsVisible(true);    mRenderer.setShowLegend(true);
        mRenderer.setShowGridX(true);      // this will show the grid in  graph
        mRenderer.setShowGridY(true);             
//        mRenderer.setAntialiasing(true);
        mRenderer.setBarSpacing(.5);   // adding spacing between the line or stacks
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.BLACK);
        mRenderer.setXAxisMin(0);
        mRenderer.setYAxisMin(0);
        //mRenderer.setXAxisMax(5);
        /*mRenderer.setXAxisMax(ParticipantsList.size()+1);*/
        mRenderer.setXAxisMax(participantAndSpeakingTimeList.size()+1);
        //mRenderer.setYAxisMax(100);
        mRenderer.setYAxisMax(Math.round(getRangeMax()/10)*10+10);
//   
        
        mRenderer.setXLabels(0);
        
        for (int i = 0; i < getRangeMax(); i++){
        	String minRange = String.valueOf(i*timeIntervalInSec/60);
        	String maxRange = String.valueOf(i*timeIntervalInSec/60 + timeIntervalInSec/60);
			mRenderer.addXTextLabel(i+1, "[" + minRange + " - " + maxRange + "]");
		}
        
        /*mRenderer.addXTextLabel(1,"Income");
        mRenderer.addXTextLabel(2,"Saving");
        mRenderer.addXTextLabel(3,"Expenditure");
        mRenderer.addXTextLabel(4,"NetIncome");*/
        
        mRenderer.setPanEnabled(true, true);    // will fix the chart position
        Intent intent = ChartFactory.getBarChartIntent(context, dataSet, mRenderer,Type.DEFAULT);
       
        return intent;

	
	}

	public List<ParticipantAndSpeakingTime> getParticipantsAndSpeakingTimes(String participantsWithSpeakingTime){
	
		List<ParticipantAndSpeakingTime> listParticipantAndSpeakingTime = new ArrayList<ParticipantAndSpeakingTime>();
		Scanner scanner = new Scanner(participantsWithSpeakingTime);
		
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String participant = line.substring(0, line.indexOf(";", 0));
			int pos = line.indexOf(";", 0);
			String value = line.substring(pos+1, line.length()-1);
			int i_value = Integer.valueOf(value);
			ParticipantAndSpeakingTime participantAndSpeakingTime = new ParticipantAndSpeakingTime(participant, i_value);
			listParticipantAndSpeakingTime.add(participantAndSpeakingTime);
		}
			
		return listParticipantAndSpeakingTime;
	}
		
	public int getRangeMax(){
		int RangeMaxFinal = 0;
		for (int i =0; i < participantAndSpeakingTimeList.size(); i++){
			int RangeMax = 1;
			int j = timeIntervalInSec;
			while (participantAndSpeakingTimeList.get(i).getValue() > j) {
				j = j + timeIntervalInSec;
				RangeMax = RangeMax + 1;
			}
			if (RangeMax > RangeMaxFinal){
				RangeMaxFinal = RangeMax;
			}
		}
		return RangeMaxFinal;
	}
	
	public List<Integer> getRangeSpeakingTimeList(){
		List<Integer> listRangeSpeakingTime = new ArrayList<Integer>();
		int nbRange = getRangeMax();
		//Initialisation de la liste de Range
		for (int i = 0; i < nbRange; i++){
			listRangeSpeakingTime.add(0);
		}
		
		for (int i =0; i < participantAndSpeakingTimeList.size(); i++){
			int range = 0;
			int j = timeIntervalInSec;
			while (participantAndSpeakingTimeList.get(i).getValue() > j) {
				j = j + timeIntervalInSec;
				range = range + 1;
			}
			listRangeSpeakingTime.set(range, listRangeSpeakingTime.get(range) + 1);
		}
		return listRangeSpeakingTime;
	}
	
}
