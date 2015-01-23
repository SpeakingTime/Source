package com.example.observationreunion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class BarGraph1Activity extends Activity {

	List<ParticipantAndSpeakingTime> participantAndSpeakingTimeList = new ArrayList<ParticipantAndSpeakingTime>();
	int timeIntervalInSec = -1;
	
	private GraphicalView mChart;	
	private XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset() ; 
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	
	private String name;
	
	private void initChart() {
		
		List<Integer> MyListValue = new ArrayList<Integer>();
		
		MyListValue = getRangeSpeakingTimeList();
				
		int[] y = new int[MyListValue.size()];
		for (int i = 0; i < MyListValue.size(); i++) 
			y[i] = MyListValue.get(i);
	       
        CategorySeries series = new CategorySeries("Time class in secondes");
        for(int i=0; i < y.length; i++){
            series.add("Bar"+(i+1),y[i]);
        }
       
        dataSet = new XYMultipleSeriesDataset();  // collection of series under one object.,there could any
        dataSet.addSeries(series.toXYSeries());                            // number of series
       
        //customization of the chart
   
        XYSeriesRenderer renderer = new XYSeriesRenderer();     // one renderer for one series
        renderer.setColor(Color.parseColor("#0099FF"));
        renderer.setDisplayChartValues(true);
        renderer.setChartValuesSpacing((float) 5.5d);
        renderer.setLineWidth((float) 10.5d);
           
       
        mRenderer = new XYMultipleSeriesRenderer();   // collection multiple values for one renderer or series
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
        //Intent intent = ChartFactory.getBarChartIntent(context, dataSet, mRenderer,Type.DEFAULT);
       
        //return intent;
		
	}
	
	private void addSampleData() {
        /*mCurrentSeries.add(1, 2);
        mCurrentSeries.add(2, 3);
        mCurrentSeries.add(3, 2);
        mCurrentSeries.add(4, 5);
        mCurrentSeries.add(5, 4);*/
    }

    private Bitmap bitmapFromChartView(View v, int width, int height) {
    	Bitmap b = Bitmap.createBitmap(width,
    	        height, Bitmap.Config.ARGB_8888);
    	Canvas c = new Canvas(b);
    	v.layout(0, 0, width, height);
    	v.draw(c);
    	return b;
    }
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bargraph1);

        Bundle b = getIntent().getExtras();
    	String participantsWithSpeakingTime = b.getString("participantsWithSpeakingTime");
    	String stimeIntervalInMin = b.getString("timeIntervalInMin");
    	name = b.getString("name");

    	participantAndSpeakingTimeList = getParticipantsAndSpeakingTimes(participantsWithSpeakingTime);
		timeIntervalInSec = Integer.valueOf(stimeIntervalInMin) * 60;
    	
    	Button buttonSaveGraphAsImage = (Button) findViewById(R.id.buttonSaveGraphAsImage1);
    	buttonSaveGraphAsImage.setOnClickListener( 
    			new Button.OnClickListener(){
    				
    				@Override
    				public void onClick(View v) {
    					//Bitmap x = bitmapFromChartView(mChart, 500, 500);    		
    					
    					mRenderer.setZoomButtonsVisible(false);
    					
    					Bitmap bmp = Bitmap.createBitmap( 800, 600, Bitmap.Config.ARGB_8888 );
    					
    					//Bitmap bmp =Bitmap.createBitmap( mChart.getMeasuredWidth(),mChart.getMeasuredHeight(), Bitmap.Config.ARGB_8888 );
    					
    					Canvas canvas = new Canvas(bmp);
    					mChart.draw( canvas );
    					
    					File myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime Graphs"); //pour créer le repertoire dans lequel on va mettre notre fichier
    		            if (!myDir.exists()) {
    		            	myDir.mkdir(); //On crée le répertoire (s'il n'existe pas!!)
    		            }
    					
    					FileOutputStream out = null;
						try {
							out = new FileOutputStream(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime Graphs" + File.separator + name.substring(0, name.length() - 4) + "_bargraph1.jpg");
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    					bmp.compress( Bitmap.CompressFormat.JPEG, 97, out );
    					try {
							out.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    					
    					mRenderer.setZoomButtonsVisible(true);
    				}
    			}
    	);
    	
    	
    }

    protected void onResume() {
        super.onResume();
        LinearLayout layout = (LinearLayout) findViewById(R.id.chart1);
        if (mChart == null) {
            initChart();
            //addSampleData();
            //mChart = ChartFactory.getCubeLineChartView(this, mDataset, mRenderer, 0.3f);
            
            mChart = ChartFactory.getBarChartView(this, dataSet, mRenderer,Type.DEFAULT);
            
            layout.addView(mChart);
        } else {
            mChart.repaint();
        }
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
