package com.example.observationreunion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PreferencesActivity extends Activity{
	
	private static final int NUM_COL_ID_PREFERENCES = 0;
	private static final int NUM_COL_HOST = 1;
	private static final int NUM_COL_USERNAME = 2;
	private static final int NUM_COL_TIME_INTERVAL_FOR_SAVE_FILE = 3;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);
		
		Button buttonCancelPreferences = (Button) findViewById(R.id.buttonCancelPreferences);
		buttonCancelPreferences.setOnClickListener( 
				new Button.OnClickListener(){
					
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		
		Button buttonSavePreferences = (Button) findViewById(R.id.buttonSavePreferences);
		buttonSavePreferences.setOnClickListener( 
				new Button.OnClickListener(){
					
					@Override
					public void onClick(View v) {
						try{
							SavePreferences();
							finish();
						}
						catch (NumberFormatException e){ 
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(PreferencesActivity.this);
							alertDialog.setTitle("Warning");
							alertDialog.setMessage("Please, enter an integer in time interval!");
							alertDialog.show();	
						}
					}
				});
		
		
		PreferencesBDD preferencesBdd = new PreferencesBDD(this);
		preferencesBdd.open();
		
		try{
		
			Cursor mCursor = preferencesBdd.getCursor();
			
			mCursor.moveToLast();
						
			String s_ID_preferences =  mCursor.getString(NUM_COL_ID_PREFERENCES);
			String s_host =  mCursor.getString(NUM_COL_HOST);
			String s_username =  mCursor.getString(NUM_COL_USERNAME);
			String s_timeintervalforsavefile =  mCursor.getString(NUM_COL_TIME_INTERVAL_FOR_SAVE_FILE);
				
			EditText editTextHost = (EditText) findViewById(R.id.host);
		    editTextHost.setText(s_host); 
		    EditText editTextUsername = (EditText) findViewById(R.id.username);
		    editTextUsername.setText(s_username);
		    EditText editTextTimeIntervalForSaveFile = (EditText) findViewById(R.id.timeintervalforsavefile);
		    editTextTimeIntervalForSaveFile.setText(s_timeintervalforsavefile);
				
		}
		catch(Exception e){
			System.out.println(e);
		}
		
		preferencesBdd.close();
			
	}
	
	
	public Boolean EditPreferences(){
	    
    	try {
	    	//Création d'une instance de ma classe GroupBDD
	    	PreferencesBDD preferencesBdd = new PreferencesBDD(this);
	    	preferencesBdd.open();
	    	preferencesBdd.removePreferencesWith_ID_preferences(0); 
	    	preferencesBdd.close();

	    	return true;
    	}
    	catch (Exception e){
    		return false;
    	}
	}
    	
	public Boolean SavePreferences() throws NumberFormatException{
		
		try {
	    	//Création d'une instance de ma classe PreferencesBDD
	    	PreferencesBDD preferencesBdd = new PreferencesBDD(this);

	    	EditText editTextHost = (EditText) findViewById(R.id.host);
	    	String host = editTextHost.getText().toString(); 
	    	EditText editTextUsername = (EditText) findViewById(R.id.username);
	    	String username = editTextUsername.getText().toString();
	    	EditText editTextTimeIntervalForSaveFile = (EditText) findViewById(R.id.timeintervalforsavefile);
	    	String timeintervalforsavefile = editTextTimeIntervalForSaveFile.getText().toString();
	    	
	    	int timeIntervalForSaveFile = Integer.valueOf((editTextTimeIntervalForSaveFile.getText().toString()));
	    	
	    	//Création des pr�ferences
		    Preferences preferences = new Preferences(host, username, timeintervalforsavefile);
	    		
		    //On ouvre la base de données pour écrire dedans
		    preferencesBdd.open();
		    //On insère le livre que l'on vient de créer
		    preferencesBdd.insertPreferences(preferences);
		    	
		    preferencesBdd.close();
	    	
	    	return true;
    	}
		catch (NumberFormatException e){
			throw new NumberFormatException();
		}
    	catch (Exception e){
    		return false;
    	}
		
	}
	
}
