package com.example.observationreunion;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PreferencesActivity extends Activity{
	
	private static final int NUM_COL_ID_PREFERENCES = 0;
	private static final int NUM_COL_HOST = 1;
	private static final int NUM_COL_USERNAME = 2;
	private static final int NUM_COL_PASSWORD = 3;

	
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
						SavePreferences();
						finish();
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
			String s_password =  mCursor.getString(NUM_COL_PASSWORD);
				
			EditText editTextHost = (EditText) findViewById(R.id.host);
		    editTextHost.setText(s_host); 
		    EditText editTextUsername = (EditText) findViewById(R.id.username);
		    editTextUsername.setText(s_username);
		    EditText editTextPassword = (EditText) findViewById(R.id.password);
		    editTextPassword.setText(s_password);
				
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
    	
	public Boolean SavePreferences(){
		
		try {
	    	//Création d'une instance de ma classe PreferencesBDD
	    	PreferencesBDD preferencesBdd = new PreferencesBDD(this);

	    	EditText editTextHost = (EditText) findViewById(R.id.host);
	    	String host = editTextHost.getText().toString(); 
	    	EditText editTextUsername = (EditText) findViewById(R.id.username);
	    	String username = editTextUsername.getText().toString();
	    	EditText editTextPassword = (EditText) findViewById(R.id.password);
	    	String password = editTextPassword.getText().toString();
	    	
	    	//Création des pr�ferences
		    Preferences preferences = new Preferences(host, username, password);
	    		
		    //On ouvre la base de données pour écrire dedans
		    preferencesBdd.open();
		    //On insère le livre que l'on vient de créer
		    preferencesBdd.insertPreferences(preferences);
		    	
		    preferencesBdd.close();
	    	
	    	return true;
    	}
    	catch (Exception e){
    		return false;
    	}
		
	}
	
}
