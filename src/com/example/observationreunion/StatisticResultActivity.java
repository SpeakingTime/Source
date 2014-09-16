package com.example.observationreunion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class StatisticResultActivity extends Activity{

	List<String> participantList = new ArrayList<String>();
	String SelectedParticipantsWithSpeakingTime = "";
	StatisticLoadFiles s = new StatisticLoadFiles();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistic_result);
		
		Bundle b = getIntent().getExtras();
    	final String filename = b.getString("selectedFile");

		Button buttonBarGraph = (Button) findViewById(R.id.buttonBarGraph);
		buttonBarGraph.setOnClickListener( 
    			new Button.OnClickListener(){
    				
    				@Override
    				public void onClick(View v) {
    					
    					RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioOrder);
    					int selectedId = radioGroup.getCheckedRadioButtonId();
    					RadioButton radioOrderButton = (RadioButton) findViewById(selectedId);
    					
    					
    					int order = -1;
    					if  (radioOrderButton.getText().toString().equalsIgnoreCase("In increasing order")){
    						order = 1;
						}
    					else if  (radioOrderButton.getText().toString().equalsIgnoreCase("In decreasing order")){
    						order = 0;
    					}
    					
    					System.out.println("order : " + order);
    					
    					BarGraph bar = new BarGraph(s.getParticipantsWithSpeakingTime(filename), order);
    					Intent barIntent = bar.getIntent(StatisticResultActivity.this);
    					startActivity(barIntent);
    				}
    			}
    	);
		
		Button buttonBarGraph1 = (Button) findViewById(R.id.buttonBarGraph1);
		buttonBarGraph1.setOnClickListener( 
    			new Button.OnClickListener(){
    				
    				@Override
    				public void onClick(View v) {
    					
    					EditText editTextTimeInterval = (EditText) findViewById(R.id.editTextTimeInterval);
    					try{
    						int timeInterval = Integer.valueOf((editTextTimeInterval.getText().toString()));
    						
    						BarGraph1 bar = new BarGraph1(s.getParticipantsWithSpeakingTime(filename), timeInterval);
        					Intent barIntent = bar.getIntent(StatisticResultActivity.this);
        					startActivity(barIntent);
    						
    					}
    					catch (Exception e){
    						AlertDialog.Builder alertDialog = new AlertDialog.Builder(StatisticResultActivity.this);
							alertDialog.setTitle("Warning");
							alertDialog.setMessage("Please, enter an integer !");
							alertDialog.show();
    					}
    						
    					
    					
    				}
    			}
    	);
		
		EditText txtStatistic = (EditText) findViewById (R.id.txtStatistic);
    	txtStatistic.setText(s.uploadFile(filename));
    
	}

}