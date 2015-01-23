package com.example.observationreunion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class BarGraphActivity extends Activity {
    
	List<ParticipantAndSpeakingTime> participantAndSpeakingTimeList = new ArrayList<ParticipantAndSpeakingTime>();
	
	private GraphicalView mChart;	
	private XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset() ; 
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	
	//private String name;

    /*private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

    private XYSeries mCurrentSeries;

    private XYSeriesRenderer mCurrentRenderer;*/

    private void initChart() {
        /*mCurrentSeries = new XYSeries("Sample Data");
        mDataset.addSeries(mCurrentSeries);
        mCurrentRenderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(mCurrentRenderer);*/
    	
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
        setContentView(R.layout.activity_bargraph);

        Bundle b = getIntent().getExtras();
    	String participantsWithSpeakingTime = b.getString("participantsWithSpeakingTime");
    	String order = b.getString("order");
    	//name = b.getString("name");
    	participantAndSpeakingTimeList = getParticipantsAndSpeakingTimes(participantsWithSpeakingTime, Integer.valueOf(order));

    	Button buttonSaveGraphAsImage = (Button) findViewById(R.id.buttonSaveGraphAsImage);
    	buttonSaveGraphAsImage.setOnClickListener( 
    			new Button.OnClickListener(){
    				
    				@Override
    				public void onClick(View v) {
    					//Bitmap x = bitmapFromChartView(mChart, 500, 500);    		
    					
    					mRenderer.setZoomButtonsVisible(false);
    					
    					/*Bitmap bmp = Bitmap.createBitmap( 800, 600, Bitmap.Config.ARGB_8888 );
    					
    					//Bitmap bmp =Bitmap.createBitmap( mChart.getMeasuredWidth(),mChart.getMeasuredHeight(), Bitmap.Config.ARGB_8888 );
    					
    					Canvas canvas = new Canvas(bmp);
    					mChart.draw( canvas );
    					
    					File myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime Graphs"); //pour créer le repertoire dans lequel on va mettre notre fichier
    		            if (!myDir.exists()) {
    		            	myDir.mkdir(); //On crée le répertoire (s'il n'existe pas!!)
    		            }
    					
    					FileOutputStream out = null;
						try {
							out = new FileOutputStream(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime Graphs" + File.separator + name.substring(0, name.length() - 4) + "_bargraph.jpg");
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
						}*/
    					
    					ModalDialog modalDialog = new ModalDialog();
						String boxplots_name = modalDialog.showModalDialogReunionName(BarGraphActivity.this, "Give a name to this graph");    					
						
						if (boxplots_name != "Cancel"){
							if (boxplots_name.equalsIgnoreCase("")){
								
								AlertDialog.Builder alertDialog = new AlertDialog.Builder(BarGraphActivity.this);
								alertDialog.setTitle("Warning");
								alertDialog.setMessage("GIVE A VALID NAME !");
								alertDialog.show();
													
							}
							else {
								
								if (SaveGraphOnDisk(boxplots_name) == "error") {
		    						AlertDialog.Builder alertDialog = new AlertDialog.Builder(BarGraphActivity.this);
									alertDialog.setTitle("Warning");
									alertDialog.setMessage("The graph can't be saved !");
									alertDialog.show();
		    					}
																
							}
						}
    					
    					
    					
    					mRenderer.setZoomButtonsVisible(true);
    				}
    			}
    	);
    	
    	
    	Button buttonSendGraphByEMail = (Button) findViewById(R.id.buttonSendGraphByEMail);
    	buttonSendGraphByEMail.setOnClickListener( 
    			new Button.OnClickListener(){
    				
    				@Override
    				public void onClick(View v) {
    					
    					
    					ModalDialog modalDialog = new ModalDialog();
						String boxplots_name = modalDialog.showModalDialogReunionName(BarGraphActivity.this, "Give a name to this graph");    					
						
						if (boxplots_name != "Cancel"){
							if (boxplots_name.equalsIgnoreCase("")){
								
								AlertDialog.Builder alertDialog = new AlertDialog.Builder(BarGraphActivity.this);
								alertDialog.setTitle("Warning");
								alertDialog.setMessage("GIVE A VALID NAME !");
								alertDialog.show();
													
							}
							else {
								
								ModalDialog modalDialogSendByEmail = new ModalDialog();
						    	
						    	String attachmentPath = SaveGraphOnDisk(boxplots_name);
						    	
						    	if (attachmentPath == "error") {
		    						AlertDialog.Builder alertDialog = new AlertDialog.Builder(BarGraphActivity.this);
									alertDialog.setTitle("Warning");
									alertDialog.setMessage("The graph can't be saved !");
									alertDialog.show();
		    					}
						    	else {
						    		String modalDialogResult = modalDialogSendByEmail.showModalDialogSendByEmail(BarGraphActivity.this, "Send graph by e-mail...", attachmentPath);
								
						    		if (modalDialogResult == "error"){
								
										AlertDialog.Builder alertDialog = new AlertDialog.Builder(BarGraphActivity.this);
										alertDialog.setTitle("Warning");
										alertDialog.setMessage("The graph has not been sent by e-mail. Verify yours parameters");
										alertDialog.show();
										
									}
									else if (modalDialogResult == "Cancel"){
										
									}
						    	}
																
							}
						}
    					
				    	
        			}
    			}
        );   	
    	
    }

    protected void onResume() {
        super.onResume();
        LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
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
	
	public int getMaxValue(){
		int MaxValue = 0;
		for (int i =0; i < participantAndSpeakingTimeList.size(); i++){
			if (participantAndSpeakingTimeList.get(i).getValue() > MaxValue){
				MaxValue = participantAndSpeakingTimeList.get(i).getValue();
			}
		}
		return MaxValue;
	}
	
	public String SaveGraphOnDisk(String boxplots_name){
		
		String Status = Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime Graphs" + File.separator + boxplots_name + ".jpg";
		
		Bitmap bmp = Bitmap.createBitmap( 800, 600, Bitmap.Config.ARGB_8888 );
		
		//Bitmap bmp =Bitmap.createBitmap( mChart.getMeasuredWidth(),mChart.getMeasuredHeight(), Bitmap.Config.ARGB_8888 );
		
		Canvas canvas = new Canvas(bmp);
		
		mRenderer.setZoomButtonsVisible(false); 
		mChart.draw( canvas );
		mRenderer.setZoomButtonsVisible(true); 
		
		File myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime Graphs"); //pour créer le repertoire dans lequel on va mettre notre fichier
        if (!myDir.exists()) {
        	myDir.mkdir(); //On crée le répertoire (s'il n'existe pas!!)
        }
		
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime Graphs" + File.separator + boxplots_name + ".jpg");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Status = "error";
		}
		bmp.compress( Bitmap.CompressFormat.JPEG, 97, out );
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Status = "error";
		}
		
		return Status;
	}
	
	
}
