package com.example.observationreunion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.example.observationreunion.MyListView.OnItemDoubleTapLister;
import com.example.observationreunion.MyListView.OnItemMoveTapLister;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemDoubleTapLister, OnItemMoveTapLister{
	
	//CrÈation de la ArrayList qui nous permettra de remplir la listView d'Ècoute
    ArrayList<HashMap<String, Object>> listItemEcoute = new ArrayList<HashMap<String, Object>>();
    MyListView lVDataEcoute;
    /*SimpleAdapter*/MyListAdapterCheckmarkEcoute mScheduleEcoute = null;
    
    //CrÈation de la ArrayList qui nous permettra de remplir la listView de parole
    ArrayList<HashMap<String, Object>> listItemParole = new ArrayList<HashMap<String, Object>>();
    MyListView lVDataParole;
    /*SimpleAdapter*/MyListAdapterCheckmarkParole mScheduleParole = null;
    
    //CrÈation de la ArrayList qui nous permettra de remplir la listView d'inactif
    ArrayList<HashMap<String, Object>> listItemInactif = new ArrayList<HashMap<String, Object>>();
    MyListView lVDataInactif;
    /*SimpleAdapter*/MyListAdapterCheckmarkInactif mScheduleInactif = null;
    
    HashMap<String, Object> original_map_onmove = null; 
    HashMap<String, Object> modified_map_onmove = null;
    Boolean flagActionDownModeEcoute = false;
    Boolean flagActionDownModeParole = false;
    Boolean flagActionDownModeInactif = false;
    Boolean playStatus = true;
    
    String meeting_total_time;
    
	StringBuilder output = new StringBuilder();
	public String timestring = null;
    
    private static ProgressDialog dialog;
    int position = 0;
    
    long totalTimeWhenStopped = 0;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);        
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    	
        Button buttonSelectAll_ecoute = (Button) findViewById(R.id.buttonSelectAll_ecoute);
        buttonSelectAll_ecoute.setOnClickListener( 
    			new Button.OnClickListener(){
    				
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					
    					int i = 0;
    					
    	                    
    					for (i = 0; i<lVDataEcoute.getCount(); i++){
    						HashMap<String, Object> mapItemContact = (HashMap<String, Object>) lVDataEcoute.getItemAtPosition(i);
    	        			listItemEcoute.remove(mapItemContact);		
    	        			mapItemContact.put("isSelected", true);
    	        			listItemEcoute.add(i, mapItemContact);	
    	        			mScheduleEcoute.notifyDataSetChanged();
    					}
    				}
    			}
    	);
                
        Button buttonSelectAll_parole = (Button) findViewById(R.id.buttonSelectAll_parole);
        buttonSelectAll_parole.setOnClickListener( 
        		new Button.OnClickListener(){
    				
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					
    					int i = 0;
    					
    	                    
    					for (i = 0; i<lVDataParole.getCount(); i++){
    						HashMap<String, Object> mapItemContact = (HashMap<String, Object>) lVDataParole.getItemAtPosition(i);
    	        			listItemParole.remove(mapItemContact);		
    	        			mapItemContact.put("isSelected", true);
    	        			listItemParole.add(i, mapItemContact);	
    	        			mScheduleParole.notifyDataSetChanged();
    					}
    				}
    			}
    	);
        
        Button buttonSelectAll_inactif = (Button) findViewById(R.id.buttonSelectAll_inactif);
        buttonSelectAll_inactif.setOnClickListener( 
        		new Button.OnClickListener(){
    				
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					
    					int i = 0;
    					
    	                    
    					for (i = 0; i<lVDataInactif.getCount(); i++){
    						HashMap<String, Object> mapItemContact = (HashMap<String, Object>) lVDataInactif.getItemAtPosition(i);
    	        			listItemInactif.remove(mapItemContact);		
    	        			mapItemContact.put("isSelected", true);
    	        			listItemInactif.add(i, mapItemContact);	
    	        			mScheduleInactif.notifyDataSetChanged();
    					}
    				}
    			}
    	);
        

        ImageButton imageButtonPause = (ImageButton) findViewById(R.id.imageButtonPause);
        imageButtonPause.setOnClickListener( 
    			new Button.OnClickListener(){
    				
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					
    					if (playStatus == true) {
    					
	    					int i = 0;
	    					for (i = 0; i<lVDataParole.getCount(); i++){
	    						HashMap<String, Object> mapItemContact = (HashMap<String, Object>) lVDataParole.getItemAtPosition(i);
	    	        			listItemParole.remove(mapItemContact);		
	    	        			mapItemContact.put("onPause", true);
	    	        			mapItemContact.put("chronometre", new ChronoData((ChronoData) mapItemContact.get("chronometre"), "stop"));
	    	        			listItemParole.add(i, mapItemContact);	
	    	        			mScheduleParole.notifyDataSetChanged();
	    					}
	    					
	    					Chronometer chronometerTotalTime = (Chronometer) findViewById(R.id.chronometerTotalTime);
	    					totalTimeWhenStopped = chronometerTotalTime.getBase() - SystemClock.elapsedRealtime();
	    					chronometerTotalTime.stop();
	    					
	    					playStatus = false;
    					}
    					
    					//ModalDialog modalDialog = new ModalDialog();
						//modalDialog.showWarningDialog(MainActivity.this, "SpeakingTime is on pause !");
    					
    				}
    			}
    	);
        
        ImageButton imageButtonPlay = (ImageButton) findViewById(R.id.imageButtonPlay);
        imageButtonPlay.setOnClickListener( 
    			new Button.OnClickListener(){
    				
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					
    					if (playStatus == false){
	    					int i = 0;
	    					for (i = 0; i<lVDataParole.getCount(); i++){
	    						HashMap<String, Object> mapItemContact = (HashMap<String, Object>) lVDataParole.getItemAtPosition(i);
	    	        			listItemParole.remove(mapItemContact);		
	    	        			mapItemContact.put("onPause", false);
	    	        			mapItemContact.put("chronometre", new ChronoData((ChronoData) mapItemContact.get("chronometre"), "start"));
	    	        			listItemParole.add(i, mapItemContact);	
	    	        			mScheduleParole.notifyDataSetChanged();
	    					}
	    					
	    					Chronometer chronometerTotalTime = (Chronometer) findViewById(R.id.chronometerTotalTime);
	    					chronometerTotalTime.setBase(SystemClock.elapsedRealtime() + totalTimeWhenStopped);
	    					chronometerTotalTime.start();
	    					
	    					playStatus = true;
    					}
    				}
    			}
    	);
        
    	Button buttonSendToDatabase = (Button) findViewById(R.id.buttonSendToDatabase);
    	buttonSendToDatabase.setOnClickListener( 
    			new Button.OnClickListener(){
    				
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					
    					ModalDialog modalDialog = new ModalDialog();
    					String meeting_name = modalDialog.showEditTextDialog(MainActivity.this, "Give a name to this meeting");    					
    					
    					if (meeting_name != "Cancel"){
    						
    						if (meeting_name.equalsIgnoreCase("")){
    							
    							AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
    							alertDialog.setTitle("Warning : GIVE A VALID NAME");
    							alertDialog.setMessage("DATA NOT SEND !");
    							alertDialog.show();
    							
    						}
    						else {
    						
	    						TextView textViewTotalTime = (TextView) findViewById (R.id.chronometerTotalTime);
			    				meeting_total_time = textViewTotalTime.getText().toString();
			    	     		if (meeting_total_time.length()==5){
			    	     		   	meeting_total_time = "00:" + meeting_total_time;
			    	    		}
			    	    		else if (meeting_total_time.length()==7){
			    	    		  	//timestring = "0:" + timestring;
			    	    		   	meeting_total_time = "0" + meeting_total_time;
			    	    		}
			    	     		    
			    				dialog = ProgressDialog.show(MainActivity.this, "Send Data", "sending");
			    					    					
			    				if (SendToDatabase(meeting_name, meeting_total_time)){
			    					Log.i("SEND", "- DATA SEND -");
			    				}
			    				else{
			    					Log.i("SEND", "- DATA NOT SEND - BIG ERROR !!! -");
			    				}
			    					
			    				/*CheckBox chkSendbySSH = (CheckBox) findViewById(R.id.ChkAlertDialogPerso);
			    			    if (chkSendbySSH.isChecked()) {
			    			        System.out.println("Send by SSH : true.");
			    			    }
			    			    else {
			    			      	System.out.println("Send by SSH : false.");
			    			    }*/
			
			    					
			    				dialog.dismiss();
			    					
			    				finish();
    						}
	    					
    					}
    				}
    					
    					
    					
    			}
    	);			
    
    	
        
        
        lVDataEcoute = new MyListView(this.getApplicationContext());
        lVDataEcoute = (MyListView) findViewById(R.id.listViewEcoute);
                
        InitContact(savedInstanceState, this);
        
        //lVDataEcoute.setClickable(true);
        lVDataEcoute.setOnItemDoubleClickListener(this);
        lVDataEcoute.setOnTouchListener(this);
        

        lVDataParole = new MyListView(this.getApplicationContext());
        lVDataParole = (MyListView) findViewById(R.id.listViewParole);
        mScheduleParole = new /*SimpleAdapter*/MyListAdapterCheckmarkParole (this.getBaseContext(), listItemParole, R.layout.affichageitem_with_chekmark_parole,
                new String[] {"img", "display_name", "company_and_title", "chronometre", "tag2"}, new int[] {R.id.IMG_parole, R.id.DISPLAY_NAME_parole, R.id.COMPANY_AND_TITLE_parole, R.id.CHRONOMETRE_parole, R.id.tag2_parole});
       
        mScheduleParole.setViewBinder(new MyViewBinder()); // VOICI LA CLE!!!!
  	    //On attribut √† notre listView l'adapter que l'on vient de cr√©er
        lVDataParole.setAdapter(mScheduleParole);
        lVDataParole.setOnItemDoubleClickListener(this);
        lVDataParole.setOnTouchListener(this);
        
        
        lVDataInactif = new MyListView(this.getApplicationContext());
        lVDataInactif = (MyListView) findViewById(R.id.listViewInactif);
        mScheduleInactif = new /*SimpleAdapter*/MyListAdapterCheckmarkInactif (this.getBaseContext(), listItemInactif, R.layout.affichageitem_with_chekmark_inactif,
                new String[] {"img", "display_name", "company_and_title", "chronometre", "tag2"}, new int[] {R.id.IMG_inactif, R.id.DISPLAY_NAME_inactif, R.id.COMPANY_AND_TITLE_inactif, R.id.CHRONOMETRE_inactif, R.id.tag2_inactif});
       
        mScheduleInactif.setViewBinder(new MyViewBinder()); // VOICI LA CLE!!!!
  	    //On attribut √† notre listView l'adapter que l'on vient de cr√©er
        lVDataInactif.setAdapter(mScheduleInactif);
        lVDataInactif.setOnItemDoubleClickListener(this);
        lVDataInactif.setOnTouchListener(this);
        
        
        
        //On commence le d√©compte du chronometre principal
        Chronometer chronometerTotalTime = (Chronometer) findViewById(R.id.chronometerTotalTime);
        chronometerTotalTime.start();
        
        
    }

    /*@Override
    public void OnDoubleTap(AdapterView<?> parent, View view, int position, long id) {
    	HashMap<String, Object> original_map = (HashMap<String, Object>) parent.getItemAtPosition(position);
        
    	HashMap<String, Object> modified_map = new HashMap<String, Object>();
    	modified_map.put("display_name", original_map.get("display_name"));
    	modified_map.put("company_and_title", original_map.get("company_and_title"));
    	modified_map.put("img", original_map.get("img"));
    	
    	if (parent.getAdapter() == mScheduleEcoute){
    		listItemEcoute.remove(original_map);
    		mScheduleEcoute.notifyDataSetChanged();
    		
    		modified_map.put("chronometre", new ChronoData((ChronoData) original_map.get("chronometre"), "start"));
    		
    		listItemParole.add(modified_map);
    		mScheduleParole.notifyDataSetChanged();
    	}
    	else if (parent.getAdapter() == mScheduleParole){
    		listItemParole.remove(original_map);
    		mScheduleParole.notifyDataSetChanged();
    		
    		modified_map.put("chronometre", new ChronoData((ChronoData) original_map.get("chronometre"), "stop"));
    		
    		listItemEcoute.add(modified_map);
    		mScheduleEcoute.notifyDataSetChanged();
    		
    	}
    	else if (parent.getAdapter() == mScheduleInactif){
    		listItemInactif.remove(original_map);
    		mScheduleInactif.notifyDataSetChanged();
    		
    		modified_map.put("chronometre", new ChronoData((ChronoData) original_map.get("chronometre"), "start"));
    		
    		listItemParole.add(modified_map);
    		mScheduleParole.notifyDataSetChanged();
    	}
    	
    }*/
    
    /*@Override
    public void OnSingleTap(AdapterView<?> parent, View view, int position, long id) {

    }*/
    
    @Override
    public void OnMoveTap(View view, MotionEvent event) {
    	
		/*long width = view.getWidth();
		int first = 0;
        int last = 0;
        int itemHeight = 0;
        int position = 0;
        
        int statusBarHeight = 0;
        int titleBarHeight = 0;
        
        if (event.getAction() == MotionEvent.ACTION_UP) {
			
        	if ((event.getX() > width*2) && (event.getX() < width*3) && flagActionDownModeInactif == false){
				//System.out.println("event.getX() : " + event.getX());

				
        		if (flagActionDownModeEcoute == true) {
        			listItemEcoute.remove(original_map_onmove);
       			
        			mScheduleEcoute.notifyDataSetChanged();
	    		
        			modified_map_onmove.put("chronometre", new ChronoData((ChronoData) original_map_onmove.get("chronometre"), "no_change"));
	    		
        			listItemInactif.add(modified_map_onmove);
        			mScheduleInactif.notifyDataSetChanged();
        		}
        		else if (flagActionDownModeParole == true) {
        			listItemParole.remove(original_map_onmove);
        			
        			mScheduleParole.notifyDataSetChanged();
	    		
        			modified_map_onmove.put("chronometre", new ChronoData((ChronoData) original_map_onmove.get("chronometre"), "stop"));
	    		
        			listItemInactif.add(modified_map_onmove);
        			mScheduleInactif.notifyDataSetChanged();
        		}
        		
        		output.append("    " + getCurrentTimeStamp() + ";" + timestring + ";" + modified_map_onmove.get("display_name") + ";idle;" + "\r\n");
		    		
		    }
        	else if ((event.getX() > width) && (event.getX() < width*2)  && flagActionDownModeParole == false){
				//System.out.println("event.getX() : " + event.getX());

				if (flagActionDownModeEcoute == true) {
					listItemEcoute.remove(original_map_onmove);
					
					mScheduleEcoute.notifyDataSetChanged();
	    		
					modified_map_onmove.put("chronometre", new ChronoData((ChronoData) original_map_onmove.get("chronometre"), "start"));
	    		
					listItemParole.add(modified_map_onmove);
					mScheduleParole.notifyDataSetChanged();
				}
				else if (flagActionDownModeInactif == true) {
					listItemInactif.remove(original_map_onmove);
					mScheduleInactif.notifyDataSetChanged();
	    		
					modified_map_onmove.put("chronometre", new ChronoData((ChronoData) original_map_onmove.get("chronometre"), "start"));
	    		
					listItemParole.add(modified_map_onmove);
					mScheduleParole.notifyDataSetChanged();
					
				}
				
				output.append("    " + getCurrentTimeStamp() + ";" + timestring + ";" + modified_map_onmove.get("display_name")  + ";speaker;" + "\r\n");
					
		    		
		    }
			else if ((event.getX() < width)  && flagActionDownModeEcoute == false){
				//System.out.println("event.getX() : " + event.getX());
			
				if (flagActionDownModeParole == true) {
					listItemParole.remove(original_map_onmove);
					mScheduleParole.notifyDataSetChanged();
	    		
					modified_map_onmove.put("chronometre", new ChronoData((ChronoData) original_map_onmove.get("chronometre"), "stop"));
	    		
					listItemEcoute.add(modified_map_onmove);
					mScheduleEcoute.notifyDataSetChanged();
				}
				else if (flagActionDownModeInactif == true) {
					listItemInactif.remove(original_map_onmove);
					mScheduleInactif.notifyDataSetChanged();
	    		
					modified_map_onmove.put("chronometre", new ChronoData((ChronoData) original_map_onmove.get("chronometre"), "no_change"));
	    		
					listItemEcoute.add(modified_map_onmove);
					mScheduleEcoute.notifyDataSetChanged();
				}
				
				output.append("    " + getCurrentTimeStamp() + ";" + timestring + ";" + modified_map_onmove.get("display_name") + ";listener;" + "\r\n");
		    		
		    }
			
			flagActionDownModeEcoute = false;
			flagActionDownModeParole = false;
			flagActionDownModeInactif = false;
			
			
        }
		else if (event.getAction() == MotionEvent.ACTION_DOWN) {
		
			
			AdapterView adapterView = (AdapterView) view;
			
			statusBarHeight = getStatusBarHeight();
	        titleBarHeight = getTitleBarHeight();
			System.out.println("statusBarHeight : " + String.valueOf(statusBarHeight));
			System.out.println("titleBarHeight : " + String.valueOf(titleBarHeight));
				        
			if (adapterView.getAdapter() == mScheduleEcoute){
				
				first = lVDataEcoute.getFirstVisiblePosition();
		     	last = lVDataEcoute.getLastVisiblePosition();
		        itemHeight = lVDataEcoute.getHeight() / (last - first + 1) + lVDataEcoute.getDividerHeight();
				position = (int) ((event.getY() - statusBarHeight - titleBarHeight) / itemHeight) //- 1;
		        
				
				
		        //Log.i("TAG", "position : " + String.valueOf(position));
		        
		        original_map_onmove = (HashMap<String, Object>) lVDataEcoute.getItemAtPosition(position);
								
		    	modified_map_onmove = new HashMap<String, Object>();
		    	modified_map_onmove.put("display_name", original_map_onmove.get("display_name"));
		    	modified_map_onmove.put("company_and_title", original_map_onmove.get("company_and_title"));
		    	modified_map_onmove.put("img", original_map_onmove.get("img"));
		    	
		    	View convertView = null;
     		    LinearLayout linearlayout = (LinearLayout) mScheduleEcoute.getView(position, convertView, lVDataEcoute);
     		    LinearLayout linearlayout2 = (LinearLayout) linearlayout.getChildAt(1);
     		    Chronometer mychronometer= (Chronometer) linearlayout2.getChildAt(2);
     		    timestring = mychronometer.getText().toString();
     		    if (timestring.length()==5){
     		    	timestring = "00:" + timestring;
    		    }
    		    else if (timestring.length()==7){
    		    	//timestring = "0:" + timestring;
    		    	timestring = "0" + timestring;
    		    }
     		    //output.append("    " + getCurrentTimeStamp() + ";" + timestring + ";" + modified_map_onmove.get("display_name") + ";listener;" + "\r\n");
				
		    	flagActionDownModeEcoute = true;

			}
			else if (adapterView.getAdapter() == mScheduleParole){
				
				first = lVDataParole.getFirstVisiblePosition();
		        last = lVDataParole.getLastVisiblePosition();
		        itemHeight = lVDataParole.getHeight() / (last - first + 1) + lVDataParole.getDividerHeight();
		        position = (int) ((event.getY()  - statusBarHeight - titleBarHeight) / itemHeight) //-1;
				
		        original_map_onmove = (HashMap<String, Object>) lVDataParole.getItemAtPosition(position);
		        
		        modified_map_onmove = new HashMap<String, Object>();
		        modified_map_onmove.put("display_name", original_map_onmove.get("display_name"));
		        modified_map_onmove.put("company_and_title", original_map_onmove.get("company_and_title"));
		        modified_map_onmove.put("img", original_map_onmove.get("img"));
		        
		        View convertView = null;
     		    LinearLayout linearlayout = (LinearLayout) mScheduleParole.getView(position, convertView, lVDataParole);
     		    LinearLayout linearlayout2 = (LinearLayout) linearlayout.getChildAt(1);
     		    Chronometer mychronometer= (Chronometer) linearlayout2.getChildAt(2);
     		    timestring = mychronometer.getText().toString();
     		    if (timestring.length()==5){
     		    	timestring = "00:" + timestring;
    		    }
    		    else if (timestring.length()==7){
    		    	//timestring = "0:" + timestring;
    		    	timestring = "0" + timestring;
    		    }
     		    //output.append("    " + getCurrentTimeStamp() + ";" + timestring + ";" + modified_map_onmove.get("display_name")  + ";speaker;" + "\r\n");
		        
		        flagActionDownModeParole = true;
		    }
			else if (adapterView.getAdapter() == mScheduleInactif){
				
				first = lVDataInactif.getFirstVisiblePosition();
		        last = lVDataInactif.getLastVisiblePosition();
		        itemHeight = lVDataInactif.getHeight() / (last - first + 1) + lVDataInactif.getDividerHeight();
		        position = (int) ((event.getY() - statusBarHeight - titleBarHeight) / itemHeight) //- 1;
		        
		        //Log.i("TAG", "position : " + String.valueOf(position));
		        
		        original_map_onmove = (HashMap<String, Object>) lVDataInactif.getItemAtPosition(position);
		        
		    	modified_map_onmove = new HashMap<String, Object>();
		    	modified_map_onmove.put("display_name", original_map_onmove.get("display_name"));
		    	modified_map_onmove.put("company_and_title", original_map_onmove.get("company_and_title"));
		    	modified_map_onmove.put("img", original_map_onmove.get("img"));
		    	
		    	View convertView = null;
     		    LinearLayout linearlayout = (LinearLayout) mScheduleInactif.getView(position, convertView, lVDataInactif);
     		    LinearLayout linearlayout2 = (LinearLayout) linearlayout.getChildAt(1);
     		    Chronometer mychronometer= (Chronometer) linearlayout2.getChildAt(2);
     		    timestring = mychronometer.getText().toString();
     		    if (timestring.length()==5){
     		    	timestring = "00:" + timestring;
    		    }
    		    else if (timestring.length()==7){
    		    	//timestring = "0:" + timestring;
    		    	timestring = "0" + timestring;
    		    }
     		    //output.append("    " + getCurrentTimeStamp() + ";" + timestring + ";" + modified_map_onmove.get("display_name") + ";idle;" + "\r\n");
				
		    	flagActionDownModeInactif = true;
			}
			
		}
		else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			AdapterView adapterView = (AdapterView) view;
			
		}*/
    	
    	long width = view.getWidth();
    	ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> map_original = null;
		HashMap<String, Object> map_modified = null; 
    	
    	if (event.getAction() == MotionEvent.ACTION_UP) {
    		
    		//if (listItem.size()>0){
    		
    		AdapterView adapterView = (AdapterView) view;
    		
    		if ((event.getX() > width*2) && (event.getX() < width*3)){
    			
	    		if (flagActionDownModeEcoute == true){
	    				
	    			/*int i = 0;
	    			for (i = 0; i<lVDataEcoute.getCount();i++){
		    			HashMap<String, Object> map = (HashMap<String, Object>) lVDataEcoute.getItemAtPosition(i);
		    				
		    			View convertView = null;
		         		LinearLayout linearlayout = (LinearLayout) mScheduleEcoute.getView(i, convertView, lVDataEcoute);
		         		LinearLayout linearlayout2 = (LinearLayout) linearlayout.getChildAt(0);
		         		CheckBox checkMark= (CheckBox) linearlayout2.getChildAt(1);
		         		    		    				
		    			if ((Boolean) map.get("isSelected") == false){
		    				listItem.add(map);
		    				System.out.println("display_name : " +  map.get("display_name").toString());
		    			}
		    		}
	    				
	    			for (i=0; i<listItem.size(); i++){
	    					
	    				listItemEcoute.remove(listItem.get(i));
	    				listItem.get(i).put("chronometre", new ChronoData((ChronoData) listItem.get(i).get("chronometre"), "no_change"));
	    				listItem.get(i).put("isSelected", false);
	    							
	            		listItemInactif.add(listItem.get(i));
	            			
	    			}
	    				
	    			//mScheduleEcoute.notifyDataSetChanged();
	    			//mScheduleInactif.notifyDataSetChanged();*/
	    			
	    			ListeningToIdle();
	    			
	    				
	    		}
	    		else if (flagActionDownModeParole == true) {
	        		
	    			SpeakingToIdle();
	    			
	    		}
	    		/*int i = 0;
	    		for (i=0; i<listItem.size() ;i++){
	    			listItemEcoute.remove((HashMap<String, Object>) listItem.get(i));
	    			System.out.println();
	    			AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
					alertDialog.setTitle("listItem.get(" + String.valueOf(i) + ")");
					alertDialog.setMessage("display_name : " + listItem.get(i).get("display_name").toString());
					alertDialog.show();
	    				
	    		}
	    			
	    		mScheduleEcoute.notifyDataSetChanged();*/
	    		
	    	}
	    	else if ((event.getX() > width) && (event.getX() < width*2)){
    			if (flagActionDownModeEcoute == true) {
	    				
	    			/*int i = 0;
	    			for (i = 0; i<lVDataEcoute.getCount();i++){
	    				HashMap<String, Object> map = (HashMap<String, Object>) lVDataEcoute.getItemAtPosition(i);
		    			
	    				View convertView = null;
	    				LinearLayout linearlayout = (LinearLayout) mScheduleEcoute.getView(i, convertView, lVDataEcoute);
		         		LinearLayout linearlayout2 = (LinearLayout) linearlayout.getChildAt(0);
		         		CheckBox checkMark= (CheckBox) linearlayout2.getChildAt(1);
		         		    		    				
		    			if ((Boolean) map.get("isSelected") == false){
		         		//if (checkMark.isChecked() == false){
		    				listItem.add(map);
		    				System.out.println("display_name : " +  map.get("display_name").toString());
		    			}
		    		}
	    				
	    					
	    				
	    		    for (i=0; i<listItem.size(); i++){
	    		    	
	    		    	listItemEcoute.remove(listItem.get(i));
	    					
	    				map_original = new HashMap<String, Object>();
	    				map_original = (HashMap<String, Object>) listItem.get(i);
	    				map_modified = new HashMap<String, Object>();
	    				map_modified = (HashMap<String, Object>) listItem.get(i);
	    				map_modified.put("chronometre", new ChronoData((ChronoData) map_original.get("chronometre"), "start"));
	    				map_modified.put("isSelected", false);
	    				listItemParole.add(map_modified);
	    				
	    				String chronoDataStatement = ((ChronoData) map_modified.get("chronometre")).GetStatement();
	    					
		    		    mScheduleParole.notifyDataSetChanged();
		    		    
	    				
	    		    }*/
    				
    				
    				/*map_original = (HashMap<String, Object>) lVDataEcoute.getItemAtPosition(position);
    				listItemEcoute.remove(map_original);
					map_modified = new HashMap<String, Object>();
    				map_modified = (HashMap<String, Object>) map_original;
    				map_modified.put("chronometre", new ChronoData((ChronoData) map_original.get("chronometre"), "start"));
    				//map_modified.put("isSelected", false);
    				listItemParole.add(map_modified);*/
    				
    				ListeningToSpeaking();
    				
    						
	    		}
	    					    				
	    		//mScheduleEcoute.notifyDataSetChanged();
	    		//mScheduleParole.notifyDataSetChanged();
    			else if (flagActionDownModeInactif == true) {
    				
    				IdleToSpeaking();
    				
    			}	
	    				
    		}
    			
    				
	    		
	    	else if ((event.getX() < width)){
	    		
	    		if (flagActionDownModeParole == true) {
 				
    				SpeakingToListening();
   						
	    		}
 
    			else if (flagActionDownModeInactif == true) {
    				
    				IdleToListening();
    				
    			}
    			
	    	}
	    		
	    	flagActionDownModeEcoute = false;
			flagActionDownModeParole = false;
			flagActionDownModeInactif = false;
	    //}
    	}
    	else if (event.getAction() == MotionEvent.ACTION_DOWN) {
					
			AdapterView adapterView = (AdapterView) view;
			
    		if (adapterView.getAdapter() == mScheduleEcoute){
	    		
    			flagActionDownModeEcoute = true;
    			
    				
    		}
    		else if (adapterView.getAdapter() == mScheduleParole){
    		
    			flagActionDownModeParole = true;
    				
    		}
    		else if (adapterView.getAdapter() == mScheduleInactif){
    			
    			flagActionDownModeInactif = true;
    		}
    		
    	}
    	else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			AdapterView adapterView = (AdapterView) view;
			
		}
    	
    	
    	
    	
    }

    
    public void InitContact(Bundle savedInstanceState, Activity activity){
    	
    	List<String> listSelectedContact = new ArrayList<String>();
    	Bundle b = getIntent().getExtras();
    	String s = b.getString("selectedContact");
    	Scanner scanner = new Scanner(s);
		while (scanner.hasNextLine()) {
			String id_contact = scanner.nextLine();
			System.out.println("id_contact : " + id_contact);
			listSelectedContact.add(id_contact);
		}			
    	
    	Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[] { ContactsContract.Contacts._ID,
                                        ContactsContract.Contacts.DISPLAY_NAME/*,
                                        ContactsContract.Contacts.Data.DATA1*/};
    	
        // works in Honeycomb
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        
        output.append("begin: participants list \r\n"); 
        
        CursorLoader cursorLoader = new CursorLoader(
                activity, 
                uri, 
                projection, 
                selection, 
                selectionArgs, 
                sortOrder);

        Cursor mCursor = cursorLoader.loadInBackground();
        
    	mCursor.moveToFirst();
    	
    	int i = 0;
    	
    	while (mCursor.isAfterLast() == false){
    		
    		if (listSelectedContact.indexOf(mCursor.getString(0)) != -1){
    			
    			HashMap<String, Object> map = new HashMap<String, Object>();
   		    	Bitmap bitmap = getPhoto(mCursor.getInt(0));
	    		bitmap = Bitmap.createScaledBitmap (bitmap, 64, 64, true);
	    		map.put("img", bitmap);
	            map.put("display_name", mCursor.getString(1));
	            map.put("isSelected", false);
	        	map.put("tag2", i);
	        	map.put("onPause", false);
	            
	            output.append( "    " + mCursor.getString(1) + "\r\n");
	            	            
	            Cursor orgCur = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
	            									  new String[] {ContactsContract.CommonDataKinds.Organization.COMPANY,
	            													ContactsContract.CommonDataKinds.Organization.TITLE}, 
	            									  ContactsContract.Data.CONTACT_ID + " = " + mCursor.getString(0)
	            									  	+ " AND ContactsContract.Data.MIMETYPE = '"
	            									  	+ ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE
	            									  	+ "'",
	            									  null,
	            									  null);
	            String company = null;
	            String title = null;
	            if (orgCur.moveToFirst()) {
	            	company = orgCur.getString(0);
	            	title = orgCur.getString(1);
	            	
	            	Log.i("company", "company : " + orgCur.getString(0));
	            	Log.i("title", "title : " + orgCur.getString(1));
	            		            	
	            }
	            orgCur.close();
	            
	            if (company != null){
	            	if (title != null){
		            	map.put("company_and_title", company + " (" + title + ")");
		            }
		            else{
		            	map.put("company_and_title", company);
		            }	
	            }
	            else {
	            	map.put("company_and_title", "");
	            }
	            
	            map.put("chronometre", new ChronoData(SystemClock.elapsedRealtime(), 0, "modeEcoute"));
	            //map.put("chronometre", new ChronoData(SystemClock.elapsedRealtime(), 0, "start"));
	            
	            listItemEcoute.add(map); 
	            
	            
	            
	        }
    		
    		i++;
    		mCursor.moveToNext();
    	}
    	mCursor.close();
    	
    	Chronometer chronometerTotalTime = (Chronometer) findViewById(R.id.chronometerTotalTime);
		String timetotalstring = chronometerTotalTime.getText().toString();
		    if (timetotalstring.length()==5){
		    	timetotalstring = "00:" + timetotalstring;
	    }
	    else if (timestring.length()==7){
	    	timetotalstring = "0" + timetotalstring;
	    }
    	
    	output.append("end: participants list  \r\n \r\nbegin: meeting " + timetotalstring /* getCurrentTimeStamp()*/ + "\r\n"); 
    	
        mScheduleEcoute = new /*SimpleAdapter*/MyListAdapterCheckmarkEcoute (this.getBaseContext(), listItemEcoute, R.layout.affichageitem_with_chekmark_ecoute,
                new String[] { "img", "display_name", "company_and_title", "chronometre", "tag2"}, 
                new int[] { R.id.IMG_ecoute, R.id.DISPLAY_NAME_ecoute, R.id.COMPANY_AND_TITLE_ecoute, R.id.CHRONOMETRE_ecoute, R.id.tag2_ecoute});
        
        mScheduleEcoute.setViewBinder(new MyViewBinder()); // VOICI LA CLE!!!!
  	    
        //On attribut √† notre listView l'adapter que l'on vient de cr√©er
        lVDataEcoute.setAdapter(mScheduleEcoute); 
        
        
        //Start2();
        
    }
    
    public Bitmap getPhoto(long userId ) {
        Uri photoUri = null;
        ContentResolver cr = this.getContentResolver();
        photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, userId);
        Bitmap defaultPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.logo_contact);
        if (photoUri != null) {
            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(
                    cr, photoUri);
            if (input != null) {
                return BitmapFactory.decodeStream(input);
            }
        } else {

            return defaultPhoto;
        }

        return defaultPhoto;
    }
    
    public boolean SendToDatabase(String meeting_name, String meeting_total_time){
    	
    	Integer _idmax = 0;    	
    	
    	try{
    		//http post
 		    /*String S_Id = "_id3";
 		    String SDisplay_Name = "test_display_name3";//ETNom.getText().toString();
 		    String SCompany_and_Title = "test_company_and_title3";//ETPrenom.getText().toString();
 		    String SChronometre = "01:01:01";//ETAge.getText().toString();
    			
 		    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
 		    		
 		    nameValuePairs.add(new BasicNameValuePair("_ID", S_Id));
 		    nameValuePairs.add(new BasicNameValuePair("display_name", SDisplay_Name));
 		    nameValuePairs.add(new BasicNameValuePair("company_and_title", SCompany_and_Title));
 		    nameValuePairs.add(new BasicNameValuePair("chronometre", SChronometre));*/
    		
    		//http post
    		
    		/*for (int i=0; i<listItemEcoute.size(); i++){
    			
    			HashMap<String, Object> map = (HashMap<String, Object>) listItemEcoute.get(i);		
    			
     		    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
     		    		
     		    nameValuePairs.add(new BasicNameValuePair("_ID", String.valueOf(i+100)));
     		    nameValuePairs.add(new BasicNameValuePair("display_name", map.get("display_name").toString()));
     		    nameValuePairs.add(new BasicNameValuePair("company_and_title", map.get("company_and_title").toString()));
     		    nameValuePairs.add(new BasicNameValuePair("chronometre", map.get("chronometre").toString()));
        		
     		    HttpClient httpclient = new DefaultHttpClient();
     		    //HttpPost httppost = new HttpPost("http://88.161.78.93:8080/add.php");
     		    
     		    HttpPost httppost = new HttpPost("http://213.186.33.3/~datamysq/timespeaking/add.php");
     		    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));                                  
     		    Log.i("Connexion", "Wait...");
     		    HttpResponse response = httpclient.execute(httppost);
     		    Log.i("Connexion", "Done");
     		    
    		}*/
 		    
    		/*for (int i=0; i<lVDataEcoute.getCount(); i++){
    			
    			HashMap<String, Object> map = (HashMap<String, Object>) lVDataEcoute.getItemAtPosition(i);		
    			
     		    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
     		    		
     		    nameValuePairs.add(new BasicNameValuePair("_ID", String.valueOf(i+500)));
     		    nameValuePairs.add(new BasicNameValuePair("display_name", map.get("display_name").toString()));
     		    nameValuePairs.add(new BasicNameValuePair("company_and_title", map.get("company_and_title").toString()));
     		    
     		    MyViewBinder myviewbinder = (MyViewBinder) lVDataEcoute.getItemAtPosition(i);// findViewById(R.id.CHRONOMETRE);
     		    String timestring = "test";//chronometer.getText().toString();
    		    
     		    
     		    /*LinearLayout linearlayout = (LinearLayout) lVDataEcoute.getItemAtPosition(i);
     		    LinearLayout linearlayout2 = (LinearLayout) linearlayout.getChildAt(1);
     		    Chronometer mychronometer= (Chronometer) linearlayout2.getChildAt(2);
     		    String timestring = mychronometer.getText().toString();*/
     		    /*nameValuePairs.add(new BasicNameValuePair("chronometre", timestring) );
        		
     		    HttpClient httpclient = new DefaultHttpClient();
     		    //HttpPost httppost = new HttpPost("http://88.161.78.93:8080/add.php");
     		    
     		    HttpPost httppost = new HttpPost("http://213.186.33.3/~datamysq/timespeaking/add.php");
     		    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));                                  
     		    Log.i("Connexion", "Wait...");
     		    HttpResponse response = httpclient.execute(httppost);
     		    Log.i("Connexion", "Done");
     		    
    		}*/
    		
    		
 		    
    		
    		//r√©cup√©ration de l'_id max
			InputStream is = null;
			/*HttpClient httpclient_idmax = new DefaultHttpClient();
			
			//HttpPost httppost_idmax = new HttpPost("http://213.186.33.3/~datamysq/timespeaking/last_id.php"); //OVH
			HttpPost httppost_idmax = new HttpPost("http://195.221.173.83/speakingtime/last_id.php"); //CentOs Cirad
			//HttpPost httppost_idmax = new HttpPost("http://195.221.173.83/timespeaking/last_id.php"); //CentOs Perso
 		    HttpResponse response_idmax = httpclient_idmax.execute(httppost_idmax);//httpclient_idmax.execute(httppost_idmax);
			
 		    HttpEntity entity_idmax = response_idmax.getEntity();
			is = entity_idmax.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			
			_idmax = Integer.valueOf(reader.readLine().toString().trim());
			
			
			Log.i("_idmax", "max _id : " + String.valueOf(_idmax));
			
			
			is.close();*/
			
    		/*for (int i=0; i<mScheduleEcoute.getCount(); i++){
    			
    			_idmax = _idmax +1;
    			
    			HashMap<String, Object> map = (HashMap<String, Object>) mScheduleEcoute.getItem(i);		
    			
     		    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
     		    		
     		    nameValuePairs.add(new BasicNameValuePair("_ID", String.valueOf(_idmax)));
     		    nameValuePairs.add(new BasicNameValuePair("display_name", map.get("display_name").toString()));
     		    nameValuePairs.add(new BasicNameValuePair("company_and_title", map.get("company_and_title").toString()));
     		    
     		    View convertView = null;
     		    LinearLayout linearlayout = (LinearLayout) mScheduleEcoute.getView(i, convertView, lVDataEcoute);
     		    LinearLayout linearlayout2 = (LinearLayout) linearlayout.getChildAt(1);
     		    Chronometer mychronometer= (Chronometer) linearlayout2.getChildAt(2);
     		    String timestring = mychronometer.getText().toString();
     		    if (timestring.length()==5){
     		    	timestring = "00:" + timestring;
    		    }
    		    else if (timestring.length()==7){
    		    	//timestring = "0:" + timestring;
    		    	timestring = "0" + timestring;
    		    }
     		    nameValuePairs.add(new BasicNameValuePair("chronometre", timestring) );
        		
     		    HttpClient httpclient = new DefaultHttpClient();
     		    //HttpPost httppost = new HttpPost("http://213.186.33.3/~datamysq/timespeaking/add.php"); //OVH
     		    HttpPost httppost = new HttpPost("http://195.221.173.83/speakingtime/add.php"); //CentOs Cirad
     		    //HttpPost httppost = new HttpPost("http://195.221.173.83/timespeaking/add.php"); //CentOs Perso
     		    //!httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));                                  
     		    Log.i("Connexion add.php", "Wait...");
        		Log.i("Connexion", nameValuePairs.get(0).toString());
    		    Log.i("Connexion", nameValuePairs.get(1).toString());
    		    Log.i("Connexion", nameValuePairs.get(2).toString());
    		    HttpResponse response = httpclient.execute(httppost);
    		    Log.i("Connexion add.php", "Done");
     		    
     		    //Envoi de l'image associ√©e
     		    try{
     		    	ImageView imageview = (ImageView) linearlayout.getChildAt(0);
     		    	
     		    	Bitmap bitmapOrg = ((BitmapDrawable) imageview.getDrawable()).getBitmap();

     		    	
     		    	//Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.logo_contact);
     		    	ByteArrayOutputStream bao = new ByteArrayOutputStream();
		            bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 90, bao);
		            byte [] ba = bao.toByteArray();
		            String ba1 = Base64.encodeBytes(ba);
		            ArrayList<NameValuePair> nameValuePairsImage = new ArrayList<NameValuePair>();
 		            nameValuePairsImage.add(new BasicNameValuePair("image",ba1));
 		            nameValuePairsImage.add(new BasicNameValuePair("id", String.valueOf(_idmax)));
     		    	
 		           try{
		            	//HttpClient httpclient = new DefaultHttpClient();
		            	//HttpPost httppost2 = new HttpPost("http://10.0.2.2/img/base.php");
		            	//HttpPost httppost_image = new HttpPost("http://213.186.33.3/~datamysq/timespeaking/img/image.php"); //OVH  
 		        	    
 		        	    HttpPost httppost_image = new HttpPost("http://195.221.173.83/timespeaking/img/image.php"); //CentOs Cirad
        	        	//HttpPost httppost_image = new HttpPost("http://195.221.173.83/timespeaking/img/image.php"); //CentOs Perso
 		        	    httppost_image.setEntity(new UrlEncodedFormEntity(nameValuePairsImage));			 		        	  
		        	    HttpResponse response_image = httpclient.execute(httppost_image);
		        	   
		        	    //HttpEntity entity_image = response_image.getEntity();
		        	    //is = entity.getContent();

		           }
 		           catch(Exception e){
		        		  Log.i("image_exception", "IMAGE EXCEPTION : " + e.toString());
		           }
     		    	
     		    	
     		    	
     		    }
     		    catch (Exception e){
     		    	
     		    }
     		    
     		    
    		
    		}*/
    		
    		/*for (int i=0; i<mScheduleParole.getCount(); i++){
    			
    			_idmax = _idmax +1;
    			
    			HashMap<String, Object> map = (HashMap<String, Object>) mScheduleParole.getItem(i);		
    			
     		    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
     		    		
     		    nameValuePairs.add(new BasicNameValuePair("_ID", String.valueOf(_idmax)));
     		    nameValuePairs.add(new BasicNameValuePair("display_name", map.get("display_name").toString()));
     		    nameValuePairs.add(new BasicNameValuePair("company_and_title", map.get("company_and_title").toString()));
     		    
     		    View convertView = null;
     		    LinearLayout linearlayout = (LinearLayout) mScheduleParole.getView(i, convertView, lVDataParole);
     		    LinearLayout linearlayout2 = (LinearLayout) linearlayout.getChildAt(1);
     		    Chronometer mychronometer= (Chronometer) linearlayout2.getChildAt(2);
     		    String timestring = mychronometer.getText().toString();
     		    if (timestring.length()==5){
     		    	timestring = "00:" + timestring;
    		    }
    		    else if (timestring.length()==7){
    		    	//timestring = "0:" + timestring;
    		    	timestring = "0" + timestring;
    		    }
     		    nameValuePairs.add(new BasicNameValuePair("chronometre", timestring) );
     		    
     		    output.append("    " +  getCurrentTimeStamp() + ";" + timestring + ";" + map.get("display_name").toString() + ";speaker;" + "\r\n"); 
     		    
     		    HttpClient httpclient = new DefaultHttpClient();
     		    //HttpPost httppost = new HttpPost("http://213.186.33.3/~datamysq/timespeaking/add.php"); //OVH
        		HttpPost httppost = new HttpPost("http://195.221.173.83/timespeaking/add.php"); //CentOs Cirad
                //HttpPost httppost = new HttpPost("http://195.221.173.83/timespeaking/add.php"); //CentOs Perso
     		    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));                                  
     		    Log.i("Connexion", "Wait...");
     		    Log.i("Connexion", nameValuePairs.get(0).toString());
     		    Log.i("Connexion", nameValuePairs.get(1).toString());
     		    Log.i("Connexion", nameValuePairs.get(2).toString());
     		    HttpResponse response = httpclient.execute(httppost);
     		    Log.i("Connexion", "Done"); 
     		    
     		    //Envoi de l'image associ√©e
     		    try{
     		    	ImageView imageview = (ImageView) linearlayout.getChildAt(0);
     		    	
     		    	Bitmap bitmapOrg = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
     		    	
     		    	//Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.logo_contact);
     		    	ByteArrayOutputStream bao = new ByteArrayOutputStream();
		            bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 90, bao);
		            byte [] ba = bao.toByteArray();
		            String ba1 = Base64.encodeBytes(ba);

		            Log.i("Connexion", "Image=" + ba1);
		            
		            ArrayList<NameValuePair> nameValuePairsImage = new ArrayList<NameValuePair>();
 		            nameValuePairsImage.add(new BasicNameValuePair("image",ba1));
 		            nameValuePairsImage.add(new BasicNameValuePair("id", String.valueOf(_idmax)));
     		    	
 		            try{
		            	//HttpClient httpclient = new DefaultHttpClient();
		            	//HttpPost httppost2 = new HttpPost("http://10.0.2.2/img/base.php");
		            	//HttpPost httppost_image = new HttpPost("http://213.186.33.3/~datamysq/timespeaking/img/image.php"); //OVH
 		        	    HttpPost httppost_image = new HttpPost("http://195.221.173.83/speakingtime/img/image.php"); //CentOs Cirad
 		            	//HttpPost httppost_image = new HttpPost("http://195.221.173.83/timespeaking/img/image.php"); //CentOs Perso
		            	httppost_image.setEntity(new UrlEncodedFormEntity(nameValuePairsImage));			 		        	  
		        	    HttpResponse response_image = httpclient.execute(httppost_image);
		        	    //HttpEntity entity_image = response_image.getEntity();
		        	    //is = entity.getContent();
		            }
 		            catch(Exception e){
		        		  Log.i("image_exception", "IMAGE EXCEPTION : " + e.toString());
 		            }
     		     }
     		     catch (Exception e){
     		    	
     		     }
     		    
     		    
     		    
    		}*/
    		
    		
    		StringBuilder meeting_result = new StringBuilder();
    		meeting_result.append("meeting_name: " + meeting_name + "\r\n\r\n");
    		meeting_result.append(output.toString() + "end: meeting \r\n\r\n");
    		meeting_result.append("total_time: " + meeting_total_time);
    		    		
    		/*ArrayList<NameValuePair> nameValuePairs_output = new ArrayList<NameValuePair>();
    		nameValuePairs_output.add(new BasicNameValuePair("meeting_name", meeting_name));
 		    nameValuePairs_output.add(new BasicNameValuePair("meeting_total_time", meeting_total_time));
 		    nameValuePairs_output.add(new BasicNameValuePair("output", meeting_result.toString()));
 		    HttpClient httpclient = new DefaultHttpClient();
    		//HttpPost httppost_result_file = new HttpPost("http://213.186.33.3/~datamysq/timespeaking/result_file.php"); //OVH
 		    //HttpPost httppost_result_file = new HttpPost("http://195.221.173.83/timespeaking/result_file.php"); //CentOs Cirad
            HttpPost httppost_result_file = new HttpPost("http://193.51.119.104/speakingtime/result_file.php"); //CentOs Perso
 		    httppost_result_file.setEntity(new UrlEncodedFormEntity(nameValuePairs_output));                                  
		    Log.i("Connexion result_file.php", "Wait...");
		    Log.i("Connexion result_file.php", "Result_file : " + output.toString());
		    HttpResponse response_result_file = httpclient.execute(httppost_result_file);
		    Log.i("Connexion result_file.php", "Done");*/
		    
		    File myFile = new File(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime",meeting_name.toString() + ".txt"); //on dÈclare notre futur fichier
            File myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime"); //pour crÈer le repertoire dans lequel on va mettre notre fichier
            Boolean success=true;
            if (!myDir.exists()) {
            	success = myDir.mkdir(); //On crÈe le rÈpertoire (s'il n'existe pas!!)
            }
            if (success){
               	//String data= "Ce que je veux ecrire dans mon fichier \r\n";
               	FileOutputStream output = new FileOutputStream(myFile,true); //le true est pour Ècrire en fin de fichier, et non l'Ècraser
               	//output.write(data.getBytes());
               	output.write(meeting_result.toString().getBytes());
            }
            else {Log.e("TEST1","ERROR DE CREATION DE DOSSIER");}
    				    		    
    		/*for (int i=0; i<lVDataParole.getCount(); i++){
    			
    			HashMap<String, Object> map = (HashMap<String, Object>) lVDataParole.getItemAtPosition(i);		
    			
     		    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
     		    		
     		    nameValuePairs.add(new BasicNameValuePair("_ID", String.valueOf(i+600)));
     		    nameValuePairs.add(new BasicNameValuePair("display_name", map.get("display_name").toString()));
     		    nameValuePairs.add(new BasicNameValuePair("company_and_title", map.get("company_and_title").toString()));
     		    
     		    /*LinearLayout linearlayout = (LinearLayout) lVDataParole.getItemAtPosition(i);
     		    LinearLayout linearlayout2 = (LinearLayout) linearlayout.getChildAt(1);
     		    Chronometer mychronometer= (Chronometer) linearlayout2.getChildAt(2);
     		    String timestring = mychronometer.getText().toString();
     		    nameValuePairs.add(new BasicNameValuePair("chronometre", timestring) );*/
        		
     		    /*HttpClient httpclient = new DefaultHttpClient();
     		    //HttpPost httppost = new HttpPost("http://88.161.78.93:8080/add.php");
     		    
     		    HttpPost httppost = new HttpPost("http://213.186.33.3/~datamysq/timespeaking/add.php");
     		    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));                                  
     		    Log.i("Connexion", "Wait...");
     		    HttpResponse response = httpclient.execute(httppost);
     		    Log.i("Connexion", "Done");
     		    
    		}*/

 		            
 		    /*try{
 		          Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.sermon);
 		          ByteArrayOutputStream bao = new ByteArrayOutputStream();
 		          bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 90, bao);
 		          byte [] ba = bao.toByteArray();
 		          String ba1 = Base64.encodeBytes(ba);
 		          ArrayList<NameValuePair> nameValuePairsImage = new ArrayList<NameValuePair>();
	 		      nameValuePairsImage.add(new BasicNameValuePair("image",ba1));
	 		            
	 		      nameValuePairsImage.add(new BasicNameValuePair("Nom", SNom + "_" + SPrenom));
	 		            
	 		            
	 		      try{
	 		      	  //HttpClient httpclient = new DefaultHttpClient();
	 		          //HttpPost httppost2 = new HttpPost("http://10.0.2.2/img/base.php");
	 		          HttpPost httppost2 = new HttpPost("http://88.161.78.93:8080/img/base.php");
	 		          httppost2.setEntity(new UrlEncodedFormEntity(nameValuePairsImage));			 		        	  
	 		       	  HttpResponse response2 = httpclient.execute(httppost2);
	 		          HttpEntity entity = response.getEntity();
	 		       	  is = entity.getContent();

	 		       }
	 		       catch(Exception e){
	 		          AlertDialog.Builder alert2 = new AlertDialog.Builder(Activity2.this);
	 		          alert2.setTitle("Erreur");
	 		       	  alert2.setMessage("Base64.encodeBase64String");
	 		          alert2.show();	
     	           }
	 		            
	 		            
 		    }
 		    catch (Exception e){
 		            	
 		    }*/
    		
    		return true;
    	}
    	catch (Exception e){
    		
    		Log.i("exception", "EXCEPTION : " + e.toString());
    		return false;
    	}
    	
    }
    
    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
    
    public int getStatusBarHeight() {
	    Rect r = new Rect();
	    Window w = getWindow();
	    w.getDecorView().getWindowVisibleDisplayFrame(r);
	    return r.top;
	}
	 
	public int getTitleBarHeight() {
	    int viewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
	    return (viewTop - getStatusBarHeight());
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void myClickHandlerEcoute(View v) {
		
    	CheckBox cb = (CheckBox)v;
		int position = Integer.parseInt(cb.getTag().toString());
		
		if (cb.isChecked()){
			//cb.setBackgroundResource(Color.GREEN);
				
			HashMap<String, Object> mapItemContact = (HashMap<String, Object>) lVDataEcoute.getItemAtPosition(position);
			listItemEcoute.remove(mapItemContact);		
			mapItemContact.put("isSelected", true);
			listItemEcoute.add(position, mapItemContact);	
			mScheduleEcoute.notifyDataSetChanged();
				
		}
		else{
			//cb.setBackgroundResource(Color.BLUE);
			HashMap<String, Object> mapItemContact = (HashMap<String, Object>) lVDataEcoute.getItemAtPosition(position);
			listItemEcoute.remove(mapItemContact);		
			mapItemContact.put("isSelected", false);
			listItemEcoute.add(position, mapItemContact);	
			mScheduleEcoute.notifyDataSetChanged();
				
		}
		
	}
    
    public void myClickHandlerParole(View v) {
		
    	CheckBox cb = (CheckBox)v;
		int position = Integer.parseInt(cb.getTag().toString());
		
		if (cb.isChecked()){
			//cb.setBackgroundResource(Color.GREEN);
				
			HashMap<String, Object> mapItemContact = (HashMap<String, Object>) lVDataParole.getItemAtPosition(position);
			listItemParole.remove(mapItemContact);		
			mapItemContact.put("isSelected", true);
			listItemParole.add(position, mapItemContact);	
			mScheduleParole.notifyDataSetChanged();
				
		}
		else{
			//cb.setBackgroundResource(Color.BLUE);
			HashMap<String, Object> mapItemContact = (HashMap<String, Object>) lVDataParole.getItemAtPosition(position);
			listItemParole.remove(mapItemContact);		
			mapItemContact.put("isSelected", false);
			listItemParole.add(position, mapItemContact);	
			mScheduleParole.notifyDataSetChanged();
				
		}
		
	}
    
    public void myClickHandlerInactif(View v) {
		
    	CheckBox cb = (CheckBox)v;
		int position = Integer.parseInt(cb.getTag().toString());
		
		if (cb.isChecked()){
			//cb.setBackgroundResource(Color.GREEN);
				
			HashMap<String, Object> mapItemContact = (HashMap<String, Object>) lVDataInactif.getItemAtPosition(position);
			listItemInactif.remove(mapItemContact);		
			mapItemContact.put("isSelected", true);
			listItemInactif.add(position, mapItemContact);	
			mScheduleInactif.notifyDataSetChanged();
				
		}
		else{
			//cb.setBackgroundResource(Color.BLUE);
			HashMap<String, Object> mapItemContact = (HashMap<String, Object>) lVDataInactif.getItemAtPosition(position);
			listItemInactif.remove(mapItemContact);		
			mapItemContact.put("isSelected", false);
			listItemInactif.add(position, mapItemContact);	
			mScheduleInactif.notifyDataSetChanged();
				
		}
		
	}

	@Override
	public void OnDoubleTap(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
			
	}

	@Override
	public void OnSingleTap(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void ListeningToSpeaking(){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> map_original = null;
		HashMap<String, Object> map_modified = null; 
		int i = 0;
		
		for (i = 0; i<lVDataEcoute.getCount();i++){
			HashMap<String, Object> map = (HashMap<String, Object>) lVDataEcoute.getItemAtPosition(i);
			
			if ((Boolean) map.get("isSelected") == true){
				listItem.add(map);
				System.out.println("display_name : " +  map.get("display_name").toString());
			}
		}
			
		for (i=0; i<listItem.size(); i++){
	    	
			listItemEcoute.remove(listItem.get(i));
			mScheduleEcoute.notifyDataSetChanged();
			
			map_original = new HashMap<String, Object>();
			map_original = (HashMap<String, Object>) listItem.get(i);
			map_modified = new HashMap<String, Object>();
			map_modified = (HashMap<String, Object>) listItem.get(i);
			map_modified.put("chronometre", new ChronoData((ChronoData) map_original.get("chronometre"), "start"));
			map_modified.put("isSelected", false);
			listItemParole.add(map_modified);
		   	
		   	mScheduleParole.notifyDataSetChanged();
		   	
		   	int position = listItemParole.size()-1;
		   	View convertView = null;
 		    LinearLayout linearlayout = (LinearLayout) mScheduleParole.getView(position, convertView, lVDataParole);
 		    LinearLayout linearlayout2 = (LinearLayout) linearlayout.getChildAt(1);
 		    Chronometer mychronometer= (Chronometer) linearlayout2.getChildAt(2);
 		    timestring = mychronometer.getText().toString();
 		    if (timestring.length()==5){
 		    	timestring = "00:" + timestring;
		    }
		    else if (timestring.length()==7){
		    	timestring = "0" + timestring;
		    }
 		    
 		    Chronometer chronometerTotalTime = (Chronometer) findViewById(R.id.chronometerTotalTime);
 		    String timetotalstring = chronometerTotalTime.getText().toString();
 		    if (timetotalstring.length()==5){
 		    	timetotalstring = "00:" + timetotalstring;
		    }
		    else if (timestring.length()==7){
		    	timetotalstring = "0" + timetotalstring;
		    }
 		    
 		    output.append("    " + /*getCurrentTimeStamp()*/ timetotalstring + ";" + timestring + ";" + map_modified.get("display_name") + ";speaker;" + "\r\n");
		   	  	
	    }
	}
	
	
	public void SpeakingToListening(){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> map_original = null;
		HashMap<String, Object> map_modified = null; 
		int i = 0;
		
		for (i = 0; i<lVDataParole.getCount();i++){
			HashMap<String, Object> map = (HashMap<String, Object>) lVDataParole.getItemAtPosition(i);
			
			if ((Boolean) map.get("isSelected") == true){
				listItem.add(map);
				System.out.println("display_name : " +  map.get("display_name").toString());
			}
		}
			
		for (i=0; i<listItem.size(); i++){
	    	
			listItemParole.remove(listItem.get(i));
			mScheduleParole.notifyDataSetChanged();
			
			map_original = new HashMap<String, Object>();
			map_original = (HashMap<String, Object>) listItem.get(i);
			map_modified = new HashMap<String, Object>();
			map_modified = (HashMap<String, Object>) listItem.get(i);
			map_modified.put("chronometre", new ChronoData((ChronoData) map_original.get("chronometre"), "stop"));
			map_modified.put("isSelected", false);
			listItemEcoute.add(map_modified);
		   	
		   	mScheduleEcoute.notifyDataSetChanged();
		   	
		   	int position = listItemEcoute.size()-1;
		   	View convertView = null;
 		    LinearLayout linearlayout = (LinearLayout) mScheduleEcoute.getView(position, convertView, lVDataEcoute);
 		    LinearLayout linearlayout2 = (LinearLayout) linearlayout.getChildAt(1);
 		    Chronometer mychronometer= (Chronometer) linearlayout2.getChildAt(2);
 		    timestring = mychronometer.getText().toString();
 		    if (timestring.length()==5){
 		    	timestring = "00:" + timestring;
		    }
		    else if (timestring.length()==7){
		    	timestring = "0" + timestring;
		    }
 		    
 		    Chronometer chronometerTotalTime = (Chronometer) findViewById(R.id.chronometerTotalTime);
		    String timetotalstring = chronometerTotalTime.getText().toString();
		    if (timetotalstring.length()==5){
		    	timetotalstring = "00:" + timetotalstring;
		    }
		    else if (timestring.length()==7){
		    	timetotalstring = "0" + timetotalstring;
		    }
 		    
 		    output.append("    " + timetotalstring /*getCurrentTimeStamp()*/ + ";" + timestring + ";" + map_modified.get("display_name") + ";listener;" + "\r\n");
		   				
	    }
	}
	
	public void ListeningToIdle(){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> map_original = null;
		HashMap<String, Object> map_modified = null; 
		int i = 0;
		
		for (i = 0; i<lVDataEcoute.getCount();i++){
			HashMap<String, Object> map = (HashMap<String, Object>) lVDataEcoute.getItemAtPosition(i);
			
			if ((Boolean) map.get("isSelected") == true){
				listItem.add(map);
				System.out.println("display_name : " +  map.get("display_name").toString());
			}
		}
			
		for (i=0; i<listItem.size(); i++){
	    	
			listItemEcoute.remove(listItem.get(i));
			mScheduleEcoute.notifyDataSetChanged();
			
			map_original = new HashMap<String, Object>();
			map_original = (HashMap<String, Object>) listItem.get(i);
			map_modified = new HashMap<String, Object>();
			map_modified = (HashMap<String, Object>) listItem.get(i);
			map_modified.put("chronometre", new ChronoData((ChronoData) map_original.get("chronometre"), "no_change"));
			map_modified.put("isSelected", false);
			listItemInactif.add(map_modified);
		   	
			mScheduleInactif.notifyDataSetChanged();
		   	
			int position = listItemInactif.size()-1;
		   	View convertView = null;
 		    LinearLayout linearlayout = (LinearLayout) mScheduleInactif.getView(position, convertView, lVDataInactif);
 		    LinearLayout linearlayout2 = (LinearLayout) linearlayout.getChildAt(1);
 		    Chronometer mychronometer= (Chronometer) linearlayout2.getChildAt(2);
 		    timestring = mychronometer.getText().toString();
 		    if (timestring.length()==5){
 		    	timestring = "00:" + timestring;
		    }
		    else if (timestring.length()==7){
		    	timestring = "0" + timestring;
		    }
 		    
 		    Chronometer chronometerTotalTime = (Chronometer) findViewById(R.id.chronometerTotalTime);
		    String timetotalstring = chronometerTotalTime.getText().toString();
		    if (timetotalstring.length()==5){
		    	timetotalstring = "00:" + timetotalstring;
		    }
		    else if (timestring.length()==7){
		    	timetotalstring = "0" + timetotalstring;
		    }
 		    
 		    output.append("    " + timetotalstring /*getCurrentTimeStamp()*/ + ";" + timestring + ";" + map_modified.get("display_name") + ";idle;" + "\r\n");
 		    
 		    /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
			alertDialog.setTitle("chronometre");
			alertDialog.setMessage( map_modified.get("display_name").toString());
			alertDialog.show();*/
		   
		   				
		}
		
	}
	
	public void SpeakingToIdle(/*int position*/){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> map_original = null;
		HashMap<String, Object> map_modified = null; 
		int i = 0;
		
		for (i = 0; i<lVDataParole.getCount();i++){
			HashMap<String, Object> map = (HashMap<String, Object>) lVDataParole.getItemAtPosition(i);
			
			if ((Boolean) map.get("isSelected") == true){
				listItem.add(map);
				System.out.println("display_name : " +  map.get("display_name").toString());
			}
		}
			
		for (i=0; i<listItem.size(); i++){
	    	
			listItemParole.remove(listItem.get(i));
			mScheduleParole.notifyDataSetChanged();
			
			map_original = new HashMap<String, Object>();
			map_original = (HashMap<String, Object>) listItem.get(i);
			map_modified = new HashMap<String, Object>();
			map_modified = (HashMap<String, Object>) listItem.get(i);
			map_modified.put("chronometre", new ChronoData((ChronoData) map_original.get("chronometre"), "stop"));
			map_modified.put("isSelected", false);
			listItemInactif.add(map_modified);
		   	
		   	mScheduleInactif.notifyDataSetChanged();
		   	
		   	int position = listItemInactif.size()-1;
		   	View convertView = null;
 		    LinearLayout linearlayout = (LinearLayout) mScheduleInactif.getView(position, convertView, lVDataInactif);
 		    LinearLayout linearlayout2 = (LinearLayout) linearlayout.getChildAt(1);
 		    Chronometer mychronometer= (Chronometer) linearlayout2.getChildAt(2);
 		    timestring = mychronometer.getText().toString();
 		    if (timestring.length()==5){
 		    	timestring = "00:" + timestring;
		    }
		    else if (timestring.length()==7){
		    	timestring = "0" + timestring;
		    }
 		    
 		    Chronometer chronometerTotalTime = (Chronometer) findViewById(R.id.chronometerTotalTime);
		    String timetotalstring = chronometerTotalTime.getText().toString();
		    if (timetotalstring.length()==5){
		    	timetotalstring = "00:" + timetotalstring;
		    }
		    else if (timestring.length()==7){
		    	timetotalstring = "0" + timetotalstring;
		    }
 		    
 		    output.append("    " + timetotalstring /*getCurrentTimeStamp()*/ + ";" + timestring + ";" + map_modified.get("display_name") + ";idle;" + "\r\n");
 		    
		   				
	    }
	}
	
	public void IdleToListening(){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> map_original = null;
		HashMap<String, Object> map_modified = null; 
		int i = 0;
		
		for (i = 0; i<lVDataInactif.getCount();i++){
			HashMap<String, Object> map = (HashMap<String, Object>) lVDataInactif.getItemAtPosition(i);
			
			if ((Boolean) map.get("isSelected") == true){
				listItem.add(map);
				System.out.println("display_name : " +  map.get("display_name").toString());
			}
		}
			
		for (i=0; i<listItem.size(); i++){
	    	
			listItemInactif.remove(listItem.get(i));
			mScheduleInactif.notifyDataSetChanged();
			
			map_original = new HashMap<String, Object>();
			map_original = (HashMap<String, Object>) listItem.get(i);
			map_modified = new HashMap<String, Object>();
			map_modified = (HashMap<String, Object>) listItem.get(i);
			map_modified.put("chronometre", new ChronoData((ChronoData) map_original.get("chronometre"), "no_change"));
			map_modified.put("isSelected", false);
			listItemEcoute.add(map_modified);
		   	
		   	mScheduleEcoute.notifyDataSetChanged();
		   	
		   	int position = listItemEcoute.size()-1;
		   	View convertView = null;
 		    LinearLayout linearlayout = (LinearLayout) mScheduleEcoute.getView(position, convertView, lVDataEcoute);
 		    LinearLayout linearlayout2 = (LinearLayout) linearlayout.getChildAt(1);
 		    Chronometer mychronometer= (Chronometer) linearlayout2.getChildAt(2);
 		    timestring = mychronometer.getText().toString();
 		    if (timestring.length()==5){
 		    	timestring = "00:" + timestring;
		    }
		    else if (timestring.length()==7){
		    	timestring = "0" + timestring;
		    }
 		    
 		    Chronometer chronometerTotalTime = (Chronometer) findViewById(R.id.chronometerTotalTime);
		    String timetotalstring = chronometerTotalTime.getText().toString();
		    if (timetotalstring.length()==5){
		    	timetotalstring = "00:" + timetotalstring;
		    }
		    else if (timestring.length()==7){
		    	timetotalstring = "0" + timetotalstring;
		    }
 		    
 		    output.append("    " + timetotalstring /*getCurrentTimeStamp()*/ + ";" + timestring + ";" + map_modified.get("display_name") + ";listener;" + "\r\n");
		   				
	    }
	}
	
	public void IdleToSpeaking(/*int position*/){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> map_original = null;
		HashMap<String, Object> map_modified = null; 
		int i = 0;
		
		for (i = 0; i<lVDataInactif.getCount();i++){
			HashMap<String, Object> map = (HashMap<String, Object>) lVDataInactif.getItemAtPosition(i);
			
			if ((Boolean) map.get("isSelected") == true){
				listItem.add(map);
				System.out.println("display_name : " +  map.get("display_name").toString());
			}
		}
			
		for (i=0; i<listItem.size(); i++){
	    	
			listItemInactif.remove(listItem.get(i));
			mScheduleInactif.notifyDataSetChanged();
			
			map_original = new HashMap<String, Object>();
			map_original = (HashMap<String, Object>) listItem.get(i);
			map_modified = new HashMap<String, Object>();
			map_modified = (HashMap<String, Object>) listItem.get(i);
			map_modified.put("chronometre", new ChronoData((ChronoData) map_original.get("chronometre"), "start"));
			map_modified.put("isSelected", false);
			listItemParole.add(map_modified);
		   	
		   	mScheduleParole.notifyDataSetChanged();

		   	int position = listItemParole.size()-1;
		   	View convertView = null;
 		    LinearLayout linearlayout = (LinearLayout) mScheduleParole.getView(position, convertView, lVDataParole);
 		    LinearLayout linearlayout2 = (LinearLayout) linearlayout.getChildAt(1);
 		    Chronometer mychronometer= (Chronometer) linearlayout2.getChildAt(2);
 		    timestring = mychronometer.getText().toString();
 		    if (timestring.length()==5){
 		    	timestring = "00:" + timestring;
		    }
		    else if (timestring.length()==7){
		    	timestring = "0" + timestring;
		    }
		    	
		    Chronometer chronometerTotalTime = (Chronometer) findViewById(R.id.chronometerTotalTime);
			String timetotalstring = chronometerTotalTime.getText().toString();
			if (timetotalstring.length()==5){
			   	timetotalstring = "00:" + timetotalstring;
			}
			else if (timestring.length()==7){
			   	timetotalstring = "0" + timetotalstring;
		    }
 		    
 		    output.append("    " + timetotalstring /*getCurrentTimeStamp()*/ + ";" + timestring + ";" + map_modified.get("display_name") + ";speaker;" + "\r\n");
 		    
		   	
	    }
	}
	
	protected void onDestroy() {
        super.onDestroy(); }
   
}

