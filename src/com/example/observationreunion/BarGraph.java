package com.example.observationreunion;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.Window;


public class BarGraph{
	
	/*List<String> ParticipantsList = new ArrayList<String>();
	List<Integer> SpeakingTimeList = new ArrayList<Integer>();*/
	List<ParticipantAndSpeakingTime> participantAndSpeakingTimeList = new ArrayList<ParticipantAndSpeakingTime>();
	
	public BarGraph(String participantsWithSpeakingTime, int order) {
		// TODO Auto-generated constructor stub
		/*ParticipantsList =getListParticipants(participantsWithSpeakingTime);
		SpeakingTimeList = getListValues(participantsWithSpeakingTime);*/
		participantAndSpeakingTimeList = getParticipantsAndSpeakingTimes(participantsWithSpeakingTime, order);
	}


	public Intent getIntent(Context context){
		
		/*int[] y = { 124, 135, 443, 456, 234, 123, 342, 134, 123, 643, 243, 274 };
		
		CategorySeries series = new CategorySeries("Demo Bar Graph");
		for (int i = 0; i < y.length; i++) {
			series.add("Bar " + (i+1), y[i]);
		}
		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(series.toXYSeries());
		
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		
		mRenderer.setBarSpacing(1);
		mRenderer.addXTextLabel(1, "Sun");
		mRenderer.addXTextLabel(2, "Mon");
		mRenderer.addXTextLabel(3, "Tue");
		mRenderer.addXTextLabel(4, "Wed");
		mRenderer.addXTextLabel(5, "Thu");
	    mRenderer.addXTextLabel(6, "Fri");
	    mRenderer.addXTextLabel(7, "Sat");
		
	    mRenderer.addSeriesRenderer(renderer);
		
		Intent intent = ChartFactory.getBarChartIntent(context, dataset, mRenderer, 
														Type.DEFAULT);*/
		
		
		List<Integer> MyListValue = new ArrayList<Integer>();
		/*for (int i = 0; i < SpeakingTimeList.size(); i++){
			MyListValue.add(SpeakingTimeList.get(i));
		}*/
		for (int i = 0; i < participantAndSpeakingTimeList.size(); i++){
			MyListValue.add(participantAndSpeakingTimeList.get(i).getValue());
		}
		
		/*MyList.add(25);
		MyList.add(10);
		MyList.add(15);
		MyList.add(20);*/
				
		int[] y = new int[MyListValue.size()];
		for (int i = 0; i < MyListValue.size(); i++) 
			y[i] = MyListValue.get(i);
		
		//int y[] = {25,10,15,20};
	       
        CategorySeries series = new CategorySeries("Speaking time in secondes");
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
        mRenderer.setChartTitle("Speaking time per participant");
//        mRenderer.setXTitle("xValues");
        mRenderer.setYTitle("Time in secondes");
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
        mRenderer.setYAxisMax(Math.round(getMaxValue()/10)*10+10);
//   
        
        mRenderer.setXLabels(0);
        
        mRenderer.setXLabelsAngle(90);
        mRenderer.setXLabelsAlign(Align.LEFT);
        
        /*for (int i = 0; i < ParticipantsList.size(); i++){
			mRenderer.addXTextLabel(i+1, ParticipantsList.get(i));
		}*/
        for (int i = 0; i < participantAndSpeakingTimeList.size(); i++){
			mRenderer.addXTextLabel(i+1, participantAndSpeakingTimeList.get(i).getName());
		}
        
        /*mRenderer.addXTextLabel(1,"Income");
        mRenderer.addXTextLabel(2,"Saving");
        mRenderer.addXTextLabel(3,"Expenditure");
        mRenderer.addXTextLabel(4,"NetIncome");*/
        
        mRenderer.setPanEnabled(true, true);    // will fix the chart position
        Intent intent = ChartFactory.getBarChartIntent(context, dataSet, mRenderer,Type.DEFAULT);
       
        return intent;

	
	}
	
	/*public List<String> getListParticipants(String participantsWithSpeakingTime){
		List<String> listSelectedParticipants = new ArrayList<String>();
		Scanner scanner = new Scanner(participantsWithSpeakingTime);
		//System.out.println("var : " + participantsWithSpeakingTime);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String participant = line.substring(0, line.indexOf(";", 0));
			//System.out.println("name : " + participant);
			listSelectedParticipants.add(participant);
		}	
    	return listSelectedParticipants;
	}
	
	public List<Integer> getListValues(String participantsWithSpeakingTime){
		List<Integer> listSelectedValues = new ArrayList<Integer>();
		Scanner scanner = new Scanner(participantsWithSpeakingTime);
		//System.out.println("var : " + participantsWithSpeakingTime);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			int pos = line.indexOf(";", 0);
			String value = line.substring(pos+1, line.length()-1);
			//System.out.println("value : " + value);
			int i_value = Integer.valueOf(value);
			listSelectedValues.add(i_value);
		}	
    	return listSelectedValues;
		
	}*/
	
	public List<ParticipantAndSpeakingTime> getParticipantsAndSpeakingTimes(String participantsWithSpeakingTime, int order){
		
		List<ParticipantAndSpeakingTime> listParticipantAndSpeakingTime = new ArrayList<ParticipantAndSpeakingTime>();
		Scanner scanner = new Scanner(participantsWithSpeakingTime);
		
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String participant = line.substring(0, line.indexOf(";", 0));
			int pos = line.indexOf(";", 0);
			String value = line.substring(pos+1, line.length()-1);
			//System.out.println("value : " + value);
			int i_value = Integer.valueOf(value);
			ParticipantAndSpeakingTime participantAndSpeakingTime = new ParticipantAndSpeakingTime(participant, i_value);
			listParticipantAndSpeakingTime.add(participantAndSpeakingTime);
		}
		
		if (order == 0){
			Collections.sort(listParticipantAndSpeakingTime, new SpeakingTimeComparator());
		}
		else if (order == 1){
			Collections.sort(listParticipantAndSpeakingTime, new SpeakingTimeComparator2());
		}
		
		return listParticipantAndSpeakingTime;
	}
	
	/*public int getMaxValue(){
		int MaxValue = 0;
		for (int i =0; i < SpeakingTimeList.size(); i++){
			if (SpeakingTimeList.get(i) > MaxValue){
				MaxValue = SpeakingTimeList.get(i);
			}
		}
		return MaxValue;
	}*/
	
	public int getMaxValue(){
		int MaxValue = 0;
		for (int i =0; i < participantAndSpeakingTimeList.size(); i++){
			if (participantAndSpeakingTimeList.get(i).getValue() > MaxValue){
				MaxValue = participantAndSpeakingTimeList.get(i).getValue();
			}
		}
		return MaxValue;
	}

	
}
