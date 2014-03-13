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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistic_result);
		
		Bundle b = getIntent().getExtras();
    	final String filename = b.getString("selectedFile");
		
		/*Button buttonGetValue = (Button) findViewById(R.id.buttonGetValue);
		buttonGetValue.setOnClickListener( 
    			new Button.OnClickListener(){
    				
    				@Override
    				public void onClick(View v) {
    					EditText txtStatistic2 = (EditText) findViewById (R.id.txtStatistic2);
    			    	txtStatistic2.setText(getParticipantsListValue(filename));
    				}
    			}
    	);*/

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
    					
    					BarGraph bar = new BarGraph(getParticipantsWithSpeakingTime(filename), order);
    					Intent barIntent = bar.getIntent(StatisticResultActivity.this);
    					//barIntent.putExtra("SelectedParticipantsWithSpeakingTime", getParticipantsWithListValue(filename));
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
    						
    						BarGraph1 bar = new BarGraph1(getParticipantsWithSpeakingTime(filename), timeInterval);
        					Intent barIntent = bar.getIntent(StatisticResultActivity.this);
        					//barIntent.putExtra("SelectedParticipantsWithSpeakingTime", getParticipantsWithListValue(filename));
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
		
		Button buttonBarGraph2 = (Button) findViewById(R.id.buttonBarGraph2);
		buttonBarGraph2.setOnClickListener( 
    			new Button.OnClickListener(){
    				
    				@Override
    				public void onClick(View v) {
    					Intent intent = new Intent(StatisticResultActivity.this, BarGraph2.class);
    					intent.putExtra("participantsWithSpeakingTime", getParticipantsWithSpeakingTime(filename));
						startActivity(intent);
    				}
    			}
    	);
		
    	EditText txtStatistic = (EditText) findViewById (R.id.txtStatistic);
    	txtStatistic.setText(uploadFile(filename));
    	
    	//System.out.println(readFile(filename));
    	//System.out.println();
   	
	}
	
	public String uploadFile(String filename){
		
		StringBuffer objBuffer = null;
		
		try {
			FileInputStream objFile = new FileInputStream(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime" + File.separator +  filename);
			InputStreamReader objReader = new InputStreamReader(objFile);
			BufferedReader objBufferReader = new BufferedReader(objReader);
			objBuffer = new StringBuffer();
			String strLine;
			while ((strLine = objBufferReader.readLine()) != null) {
				objBuffer.append(strLine);
				objBuffer.append("\n");
			}
			objFile.close();
			
		}
		catch (FileNotFoundException objError) {
			System.out.println("Fichier non trouvé\n"+objError.toString());
		}
		catch (IOException objError) {
			System.out.println("Erreur\n"+objError.toString());
		}
		return objBuffer.toString();
	}
	
	public String readFile(String filename){
		
		StringBuffer objBuffer = null;
		boolean deb = false;
		
		try {
			FileInputStream objFile = new FileInputStream(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime" + File.separator +  filename);
			InputStreamReader objReader = new InputStreamReader(objFile);
			BufferedReader objBufferReader = new BufferedReader(objReader);
			objBuffer = new StringBuffer();
			String strLine;
			while ((strLine = objBufferReader.readLine()) != null) {
				
				if (strLine.indexOf("begin: meeting") != -1) { 
					deb = true;
				}
				else if (strLine.indexOf("end: meeting") != -1) { 
					deb = false;
				}
				else if (deb == true) {
					objBuffer.append(lineProcessing(strLine));
				}
				
			}
			objFile.close();
			
		}
		catch (FileNotFoundException objError) {
			System.out.println("Fichier non trouvé\n"+objError.toString());
		}
		catch (IOException objError) {
			System.out.println("Erreur\n"+objError.toString());
		}
		return objBuffer.toString();
		
	}
	
	public int getNbSec(String time){
		int hour = 0;
		int min = 0;
		int sec = 0;
		hour = Integer.valueOf(time.substring(0,2)) * 3600;
		min = Integer.valueOf(time.substring(3,5)) * 60;
		sec = Integer.valueOf(time.substring(6,8));
		return hour + min + sec;
	}
	
	public String lineProcessing(String strLine){
		StringBuilder result = new StringBuilder("");
		
		String time = strLine.substring(4, 12);
		//result.append(time + "-> " + getNbSec(time) + " sec\n");
		int pos = strLine.indexOf(";", 13);
		//String name = strLine.substring(13, pos);
		//result.append(name + "\n");
		String status = strLine.substring(pos+1, strLine.length()-1);
		
		if ((status.equalsIgnoreCase("listener")) || (status.equalsIgnoreCase("idle"))){
			result.append("OT | " + getNbSec(time) + "\n");
		}
		else if (status.equalsIgnoreCase("speaker")){
			result.append("SP | " + getNbSec(time) + "\n");
		}
		
		//result.append(status + "\n");
		return result.toString();
	}
	
	public boolean getStatusLine(String strLine){
		//return true if SP status or false if OT status
		boolean st = false;
		int pos = strLine.indexOf(";", 13);
		String status = strLine.substring(pos+1, strLine.length()-1);
		if ((status.equalsIgnoreCase("listener")) || (status.equalsIgnoreCase("idle"))){
			st = false;
		}
		else if (status.equalsIgnoreCase("speaker")){
			st = true;
		}
		return st;
		
	}
	
	public int getValueLine(String strLine){
		int time = getNbSec(strLine.substring(4, 12));
		return time;
	}
	
	public List<String> getParticipantList(String filename){
		
		List<String> files = new ArrayList<String>();
		StringBuffer objBuffer = null;
		boolean deb = false;
		
		try {
			FileInputStream objFile = new FileInputStream(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime" + File.separator +  filename);
			InputStreamReader objReader = new InputStreamReader(objFile);
			BufferedReader objBufferReader = new BufferedReader(objReader);
			objBuffer = new StringBuffer();
			String strLine;
			while ((strLine = objBufferReader.readLine()) != null) {
				
				if (strLine.indexOf("begin: participants list") != -1) { 
					deb = true;
				}
				else if (strLine.indexOf("end: participants list") != -1) { 
					deb = false;
				}
				else if (deb == true) {
					files.add(strLine.substring(4, strLine.length()));
				}
				
			}
			objFile.close();
			
		}
		catch (FileNotFoundException objError) {
			System.out.println("Fichier non trouvé\n"+objError.toString());
		}
		catch (IOException objError) {
			System.out.println("Erreur\n"+objError.toString());
		}
		return files;
	}
	
	public String getParticipantsListValue(String filename){
		
		StringBuilder result = new StringBuilder("");
		List<String> participantsList = new ArrayList<String>();
		
		result.append("begin: participants list\n");
		
		participantsList = getParticipantList(filename);
		
		for (int i = 0; i < participantsList.size(); i++){
			result.append(participantsList.get(i) + "\n");
			result.append(getParticipantInfos(filename, participantsList.get(i)));
		}

		result.append("end: participants list\n");
		
		return result.toString();
	}
	
	public String getParticipantInfos(String filename, String partipant_name){
		
		StringBuffer objBuffer = null;
		boolean deb = false;
		boolean isInfos = false;
		boolean ot_status = false;
		int ot_value = 0;
		int sp_value = 0;
		int speakingtime = 0;
		
		try {
			FileInputStream objFile = new FileInputStream(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime" + File.separator +  filename);
			InputStreamReader objReader = new InputStreamReader(objFile);
			BufferedReader objBufferReader = new BufferedReader(objReader);
			objBuffer = new StringBuffer();
			String strLine;
			while ((strLine = objBufferReader.readLine()) != null) {
				
				if (strLine.indexOf("begin: meeting") != -1) { 
					deb = true;
				}
				else if (strLine.indexOf("end: meeting") != -1) { 
					deb = false;
				}
				else if (deb == true) {
					
					int pos = strLine.indexOf(";", 13);
					String name = strLine.substring(13, pos);
					System.out.println("'" + name + "'" + " - " +  "'" + partipant_name + "'");
					if (name.equalsIgnoreCase(partipant_name)){
						isInfos = true;
						if (!getStatusLine(strLine) && (!ot_status)){
						// si le status est OT et que OT n'est pas initialisé
							objBuffer.append(lineProcessing(strLine));
							ot_value = getValueLine(strLine);
							speakingtime = addTime(speakingtime, ot_value,sp_value);
							ot_status = true;
						}
						else if (getStatusLine(strLine)){
							objBuffer.append(lineProcessing(strLine));
							sp_value = getValueLine(strLine);
							ot_status = false;
						}
						
					}
				}
				
				if ((strLine.indexOf("total_time:") != -1) && (isInfos)){
					String TT = strLine.substring(12, strLine.length());					
					int tt_value = getNbSec(TT);
					objBuffer.append("TT | " + tt_value + "\n");
					if (!ot_status){				
						speakingtime = addTime(speakingtime, tt_value,sp_value);
					}
					objBuffer.append("speakingtime : " + speakingtime + "\n");
				}
				
			}
			objFile.close();
			
		}
		catch (FileNotFoundException objError) {
			System.out.println("Fichier non trouvé\n"+objError.toString());
		}
		catch (IOException objError) {
			System.out.println("Erreur\n"+objError.toString());
		}
		return objBuffer.toString();
		
	}
	
	public int addTime(int time, int ot, int sp){
		return time + ot - sp;
	}
	
	public String getParticipantsWithSpeakingTime(String filename){
		
		StringBuilder result = new StringBuilder("");
		List<String> participantsList = new ArrayList<String>();
		
		//result.append("begin: participants list\n");
		
		participantsList = getParticipantList(filename);
		
		for (int i = 0; i < participantsList.size(); i++){
			//result.append(participantsList.get(i) + "\n");
			result.append(getParticipantsSpeakingTime(filename, participantsList.get(i)));
		}

		//result.append("end: participants list\n");
		
		System.out.println(result.toString());
		return result.toString();
	}
	
	public String getParticipantsSpeakingTime(String filename, String partipant_name){

		StringBuffer objBuffer = null;
		boolean deb = false;
		boolean isInfos = false;
		boolean ot_status = false;
		int ot_value = 0;
		int sp_value = 0;
		int speakingtime = 0;
		
		try {
			FileInputStream objFile = new FileInputStream(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime" + File.separator +  filename);
			InputStreamReader objReader = new InputStreamReader(objFile);
			BufferedReader objBufferReader = new BufferedReader(objReader);
			objBuffer = new StringBuffer();
			String strLine;
			while ((strLine = objBufferReader.readLine()) != null) {
				
				if (strLine.indexOf("begin: meeting") != -1) { 
					deb = true;
				}
				else if (strLine.indexOf("end: meeting") != -1) { 
					deb = false;
				}
				else if (deb == true) {
					
					int pos = strLine.indexOf(";", 13);
					String name = strLine.substring(13, pos);
					//System.out.println("'" + name + "'" + " - " +  "'" + partipant_name + "'");
					if (name.equalsIgnoreCase(partipant_name)){
						isInfos = true;
						if (!getStatusLine(strLine) && (!ot_status)){
						// si le status est OT et que OT n'est pas initialisé
							//objBuffer.append(lineProcessing(strLine));
							ot_value = getValueLine(strLine);
							speakingtime = addTime(speakingtime, ot_value,sp_value);
							ot_status = true;
						}
						else if (getStatusLine(strLine)){
							//objBuffer.append(lineProcessing(strLine));
							sp_value = getValueLine(strLine);
							ot_status = false;
						}
						
					}
				}
				
				if ((strLine.indexOf("total_time:") != -1) && (isInfos)){
					String TT = strLine.substring(12, strLine.length());					
					int tt_value = getNbSec(TT);
					//objBuffer.append("TT | " + tt_value + "\n");
					if (!ot_status){				
						speakingtime = addTime(speakingtime, tt_value,sp_value);
					}
					//objBuffer.append("speakingtime : " + speakingtime + "\n");
					objBuffer.append(partipant_name + ";" + speakingtime + ";\n");
				}
				
			}
			objFile.close();
			
		}
		catch (FileNotFoundException objError) {
			System.out.println("Fichier non trouvé\n"+objError.toString());
		}
		catch (IOException objError) {
			System.out.println("Erreur\n"+objError.toString());
		}
		return objBuffer.toString();
		
	}


}