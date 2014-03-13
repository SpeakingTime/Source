package com.example.observationreunion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import com.example.observationreunion.MyListAdapterCheckmarkEcoute.IListAdapterCheckmarkEcouteCallback;
import com.example.observationreunion.MyListAdapterCheckmarkInactif.IListAdapterCheckmarkInactifCallback;
import com.example.observationreunion.MyListAdapterCheckmarkParole.IListAdapterCheckmarkParoleCallback;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity implements /*OnItemDoubleTapLister, OnItemMoveTapLister,*/ IListAdapterCheckmarkEcouteCallback, IListAdapterCheckmarkParoleCallback, IListAdapterCheckmarkInactifCallback{
	
	//Création de la ArrayList qui nous permettra de remplir la listView d'écoute
    ArrayList<HashMap<String, Object>> listItemEcoute = new ArrayList<HashMap<String, Object>>();
    MyListView lVDataEcoute;
    /*SimpleAdapter*/MyListAdapterCheckmarkEcoute mScheduleEcoute = null;
    
    //Création de la ArrayList qui nous permettra de remplir la listView de parole
    ArrayList<HashMap<String, Object>> listItemParole = new ArrayList<HashMap<String, Object>>();
    MyListView lVDataParole;
    /*SimpleAdapter*/MyListAdapterCheckmarkParole mScheduleParole = null;
    
    //Création de la ArrayList qui nous permettra de remplir la listView d'inactif
    ArrayList<HashMap<String, Object>> listItemInactif = new ArrayList<HashMap<String, Object>>();
    MyListView lVDataInactif;
    /*SimpleAdapter*/MyListAdapterCheckmarkInactif mScheduleInactif = null;
    
    List<String> listSelectedContact = new ArrayList<String>();
    
    HashMap<String, Object> original_map_onmove = null; 
    HashMap<String, Object> modified_map_onmove = null;
    boolean flagActionDownModeEcoute = false;
    boolean flagActionDownModeParole = false;
    boolean flagActionDownModeInactif = false;
    boolean playStatus = true;
    boolean ssh = false;
    
    String meeting_name;
    String meeting_total_time;
    
	StringBuilder output = new StringBuilder();
	public String timestring = null;
    
    private static ProgressDialog dialog;
    int itemEcoutePosition = -2;
    
    long totalTimeWhenStopped = 0;
    
    boolean newParticipant = false;
    
    Timer timer;             
    TimerTask tache;
    
	private static final int NUM_COL_ID_PREFERENCES = 0;
	private static final int NUM_COL_HOST = 1;
	private static final int NUM_COL_USERNAME = 2;
	private static final int NUM_COL_TIME_INTERVAL_FOR_SAVE_FILE = 3;
	
	//public static Integer position = -1;
	
	private class ViewHolder {
        ImageView imageView;
        TextView txtDISPLAY_NAME;
        TextView txtCOMPANY_AND_TITLE;
        TextView txtCHRONOMETRE;
    }
	
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
    					Select(lVDataEcoute, listItemEcoute, mScheduleEcoute, true);
    				}
    			}
    	);
                
        Button buttonSelectAll_parole = (Button) findViewById(R.id.buttonSelectAll_parole);
        buttonSelectAll_parole.setOnClickListener( 
        		new Button.OnClickListener(){
    				
    				@Override
    				public void onClick(View v) {
    					Select(lVDataParole, listItemParole, mScheduleParole, true);
    				}
    			}
    	);
        
        Button buttonSelectAll_inactif = (Button) findViewById(R.id.buttonSelectAll_inactif);
        buttonSelectAll_inactif.setOnClickListener( 
        		new Button.OnClickListener(){
    				
    				@Override
    				public void onClick(View v) {
    					Select(lVDataInactif, listItemInactif, mScheduleInactif, true);
    				}
    			}
    	);
        
        Button buttonUnselectAll_ecoute = (Button) findViewById(R.id.buttonUnselectAll_ecoute);
        buttonUnselectAll_ecoute.setOnClickListener( 
        		new Button.OnClickListener(){
    				
    				@Override
    				public void onClick(View v) {
    					Select(lVDataEcoute, listItemEcoute, mScheduleEcoute, false);
    				}
    			}
    	);
        
        Button buttonUnselectAll_parole = (Button) findViewById(R.id.buttonUnselectAll_parole);
        buttonUnselectAll_parole.setOnClickListener( 
        		new Button.OnClickListener(){
    				
    				@Override
    				public void onClick(View v) {
    					Select(lVDataParole, listItemParole, mScheduleParole, false);
    				}
    			}
    	);
        
        Button buttonUnselectAll_inactif = (Button) findViewById(R.id.buttonUnselectAll_inactif);
        buttonUnselectAll_inactif.setOnClickListener( 
        		new Button.OnClickListener(){
    				
    				@Override
    				public void onClick(View v) {
    					Select(lVDataInactif, listItemInactif, mScheduleInactif, false);
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
    	
    	Button buttonAddMember = (Button) findViewById(R.id.buttonAddMember);
    	buttonAddMember.setOnClickListener( 
    			new Button.OnClickListener(){
    				
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					
    					Intent intent = new Intent(MainActivity.this, SelectionContactToAdd.class);
    					
    					StringBuilder selectedContact = new StringBuilder();
    					for (int i = 0; i<listSelectedContact.size(); i++){
    						selectedContact.append(listSelectedContact.get(i) + '\n');
    					}
    					intent.putExtra("selectedContact", selectedContact.toString());
    					startActivityForResult(intent, 1);
    					
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
    					String modalResult = modalDialog.showSaveFile(MainActivity.this, "Do you want to save file and quit meeting ?");    					
    					
    					if (modalResult == "Yes"){
    						
    						TextView textViewTotalTime = (TextView) findViewById (R.id.chronometerTotalTime);
			    			meeting_total_time = textViewTotalTime.getText().toString();
			    	     	if (meeting_total_time.length()==5){
			    	     		meeting_total_time = "00:" + meeting_total_time;
			    	    	}
			    	    	else if (meeting_total_time.length()==7){
			    	    		//timestring = "0:" + timestring;
			    	    		meeting_total_time = "0" + meeting_total_time;
			    	    	}
			    	     		
			    	     	if (SendToDatabase(meeting_name, meeting_total_time)){
			    	     		Log.i("SEND", "- DATA SEND -");
			    			}
			    	     	else{
			    				Log.i("SEND", "- DATA NOT SEND - BIG ERROR !!! -");
			    			}
			    	     				    	     	
			    			if (ssh == true){
			    					
			    				SendToSSH(meeting_name, output.toString());
			    					
			    			}
			    						    					
			    			finish();
    					}
    				}
    			}
    	);			
    	    	
    	lVDataEcoute = new MyListView(this.getApplicationContext());
        lVDataEcoute = (MyListView) findViewById(R.id.listViewEcoute);
        /*lVDataEcoute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
	        public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) {
	            //use POSITION to get item clicked
	        	itemEcoutePosition = position;
	        	AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
				alertDialog.setTitle("Warning");
				alertDialog.setMessage("itemEcoutePosition : " + itemEcoutePosition);
				alertDialog.show();
	        }
		});*/
        
        InitContact(savedInstanceState, this);
        
        Bundle b = getIntent().getExtras();
        meeting_name = b.getString("meetingName");
        
        /*lVDataEcoute.setOnItemDoubleClickListener(this);
        lVDataEcoute.setOnTouchListener(this);*/
        

        lVDataParole = new MyListView(this.getApplicationContext());
        lVDataParole = (MyListView) findViewById(R.id.listViewParole);
        mScheduleParole = new /*SimpleAdapter*/MyListAdapterCheckmarkParole (MainActivity.this, this.getBaseContext(), listItemParole, R.layout.affichageitem_with_chekmark_parole,
                new String[] {"img", "display_name", "company_and_title", "chronometre", "tag2"}, new int[] {R.id.IMG_parole, R.id.DISPLAY_NAME_parole, R.id.COMPANY_AND_TITLE_parole, R.id.CHRONOMETRE_parole, R.id.tag2_parole});
       
        mScheduleParole.setViewBinder(new MyViewBinder()); // VOICI LA CLE!!!!
        
  	    //On attribut Ã  notre listView l'adapter que l'on vient de crÃ©er
        lVDataParole.setAdapter(mScheduleParole);
        /*lVDataParole.setOnItemDoubleClickListener(this);
        lVDataParole.setOnTouchListener(this);*/
        
        
        lVDataInactif = new MyListView(this.getApplicationContext());
        lVDataInactif = (MyListView) findViewById(R.id.listViewInactif);
        mScheduleInactif = new /*SimpleAdapter*/MyListAdapterCheckmarkInactif (MainActivity.this, this.getBaseContext(), listItemInactif, R.layout.affichageitem_with_chekmark_inactif,
                new String[] {"img", "display_name", "company_and_title", "chronometre", "tag2"}, new int[] {R.id.IMG_inactif, R.id.DISPLAY_NAME_inactif, R.id.COMPANY_AND_TITLE_inactif, R.id.CHRONOMETRE_inactif, R.id.tag2_inactif});
       
        mScheduleInactif.setViewBinder(new MyViewBinder()); // VOICI LA CLE!!!!
  	    //On attribut Ã  notre listView l'adapter que l'on vient de crÃ©er
        lVDataInactif.setAdapter(mScheduleInactif);
        /*lVDataInactif.setOnItemDoubleClickListener(this);
        lVDataInactif.setOnTouchListener(this); */   					
		
		timer = new Timer();             
        tache = new TimerTask() {     
            @Override
                public void run() {
            	
            		MainActivity.this.runOnUiThread(new Runnable() {
	            	    public void run() {
	            	    
	            	    	TextView textViewTotalTime = (TextView) findViewById (R.id.chronometerTotalTime);
	    					meeting_total_time = textViewTotalTime.getText().toString();
	    		     		if (meeting_total_time.length()==5){
	    		     		   	meeting_total_time = "00:" + meeting_total_time;
	    		    		}
	    		    		else if (meeting_total_time.length()==7){
	    		    		  	//timestring = "0:" + timestring;
	    		    		   	meeting_total_time = "0" + meeting_total_time;
	    		    		}
	    		     			    					
	    					if (SendToDatabase(meeting_name, meeting_total_time)){
	    						Log.i("SEND", "- DATA SEND -");
	    					}
	    					else{
	    						Log.i("SEND", "- DATA NOT SEND - BIG ERROR !!! -");
	    					}
	            	    	
	            	    	// update UI here
	            	    }
            		});
	            	
            	
                }
        };
        timer.scheduleAtFixedRate(tache,0,getTimeIntervalForSaveFile());
        //timer.schedule(tache,60000);
        
      //On commence le dÃ©compte du chronometre principal
        Chronometer chronometerTotalTime = (Chronometer) findViewById(R.id.chronometerTotalTime);
        chronometerTotalTime.start();
      
        
    }
    
    /*@Override
    public void OnDoubleTap(AdapterView<?> parent, View view, int position, long id) {
    		
    }
    
    @Override
    public void OnSingleTap(AdapterView<?> parent, View view, int position, long id) {

    }*/
    
    private void Select(ListView listView, ArrayList<HashMap<String, Object>> listItem, 
    					SimpleAdapter mSchedule, Boolean bSelectAll){
    	
    	int i = 0;
		for (i = 0; i<listView.getCount(); i++){
			HashMap<String, Object> mapItemContact = (HashMap<String, Object>) listView.getItemAtPosition(i);
			listItem.remove(mapItemContact);		
			mapItemContact.put("isSelected", bSelectAll);
			listItem.add(i, mapItemContact);	
			mSchedule.notifyDataSetChanged();
		}
    	
    }
    
    /*@Override
    public void OnMoveTap(View view, MotionEvent event) {
    	
    	int position = -1;
    	
    	long width = view.getWidth();
    	ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> map_original = null;
		HashMap<String, Object> map_modified = null; 
    	
    	if (event.getAction() == MotionEvent.ACTION_UP) {
    		
    		AdapterView adapterView = (AdapterView) view;
    		
    		if ((event.getX() > width*2) && (event.getX() < width*3)){
    			if (flagActionDownModeEcoute == true){
	    			ListeningToIdle();
	    		}
	    		else if (flagActionDownModeParole == true) {
	        		SpeakingToIdle();
	    		}
	    	}
	    	else if ((event.getX() > width) && (event.getX() < width*2)){
    			if (flagActionDownModeEcoute == true) {
    				
    				    				
    				if (position == -1){
    					ListeningToSpeaking();
    				}
    				else{
    					ListeningToSpeaking(position);
    					position = -1;
    				}
    			}
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
    	
    }*/

    
    public void InitContact(Bundle savedInstanceState, Activity activity){
    	
    	//List<String> listSelectedContact = new ArrayList<String>();
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
	    		map.put("id_contact", mCursor.getString(0));
	            map.put("display_name", mCursor.getString(1));
	            map.put("isSelected", false);
	        	map.put("tag2", i);
	        	map.put("tag3", i);
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
    	
    	/*Chronometer chronometerTotalTime = (Chronometer) findViewById(R.id.chronometerTotalTime);
		String timetotalstring = chronometerTotalTime.getText().toString();
		    if (timetotalstring.length()==5){
		    	timetotalstring = "00:" + timetotalstring;
	    }
	    else if (timestring.length()==7){
	    	timetotalstring = "0" + timetotalstring;
	    }*/
    	
    	Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
    	
    	output.append("end: participants list  \r\n \r\nbegin: meeting " + formattedDate /*timetotalstring  getCurrentTimeStamp()*/ + "\r\n"); 
    	
        mScheduleEcoute = new /*SimpleAdapter*/MyListAdapterCheckmarkEcoute (MainActivity.this, this.getBaseContext(), listItemEcoute, R.layout.affichageitem_with_chekmark_ecoute,
                new String[] { "img", "display_name", "company_and_title", "chronometre", "tag2", "tag3"}, 
                new int[] { R.id.IMG_ecoute, R.id.DISPLAY_NAME_ecoute, R.id.COMPANY_AND_TITLE_ecoute, R.id.CHRONOMETRE_ecoute, R.id.tag2_ecoute,  R.id.tag3_ecoute});
        
        mScheduleEcoute.setViewBinder(new MyViewBinder()); // VOICI LA CLE!!!!
  	    
        //On attribut Ã  notre listView l'adapter que l'on vient de crÃ©er
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
    		
    	try{
    		
    		StringBuilder meeting_result = new StringBuilder();
    		meeting_result.append("meeting_name: " + meeting_name + "\r\n\r\n");
    		meeting_result.append(output.toString() + "end: meeting \r\n\r\n");
    		meeting_result.append("total_time: " + meeting_total_time);
    		    		
    		File myFile = new File(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime",meeting_name.toString() + ".txt"); //on déclare notre futur fichier
            File myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime"); //pour créer le repertoire dans lequel on va mettre notre fichier
            Boolean success=true;
            if (!myDir.exists()) {
            	success = myDir.mkdir(); //On crée le répertoire (s'il n'existe pas!!)
            }
            if (success){
               	//String data= "Ce que je veux ecrire dans mon fichier \r\n";
               	FileOutputStream output = new FileOutputStream(myFile,false); //le true est pour écrire en fin de fichier, et non l'écraser
               	output.write(meeting_result.toString().getBytes());
            }
            else {Log.e("TEST1","ERROR DE CREATION DE DOSSIER");}
    		
    		return true;
    	}
    	catch (Exception e){
    		
    		Log.i("exception", "EXCEPTION : " + e.toString());
    		return false;
    	}
    	
    }
    
    public boolean SendToSSH(String meeting_name, String file){
		
    	PreferencesBDD preferencesBdd = new PreferencesBDD(this);
    	
    	try{
			
			preferencesBdd.open();
			
			try{
			
				Cursor mCursor = preferencesBdd.getCursor();
				
		     	/*AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
				alertDialog.setTitle("preferencesBdd.open()");
				alertDialog.setMessage("preferencesBdd.open()");
				alertDialog.show();	*/
			
				
				mCursor.moveToLast();
							
				String s_ID_preferences =  mCursor.getString(NUM_COL_ID_PREFERENCES);
				String s_host =  mCursor.getString(NUM_COL_HOST);
				String s_username =  mCursor.getString(NUM_COL_USERNAME);
				//String s_password =  mCursor.getString(NUM_COL_PASSWORD);
				

				ModalDialog modalDialogSSH = new ModalDialog();
				modalDialogSSH.showSSHDialog(MainActivity.this, "Remote Server Connexion...",
											s_host, s_username/*, s_password*/, meeting_name, file);
			
			}
			catch(Exception e){
				System.out.println(e);
			}
			
			preferencesBdd.close();
			
			return true;
		}
		catch (Exception e){
    		return false;
    	}
	}
    

    /*public boolean createFile(String meeting_name){
    	
    	try{
    		
    		StringBuilder meeting_result = new StringBuilder();
    		meeting_result.append("meeting_name: " + meeting_name + "\r\n\r\n");
    		//meeting_result.append(output.toString() + "end: meeting \r\n\r\n");
    		//meeting_result.append("total_time: " + meeting_total_time);
    		    		
    		File myFile = new File(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime",meeting_name.toString() + ".txt"); //on déclare notre futur fichier
            File myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime"); //pour créer le repertoire dans lequel on va mettre notre fichier
            Boolean success=true;
            if (!myDir.exists()) {
            	success = myDir.mkdir(); //On crée le répertoire (s'il n'existe pas!!)
            }
            if (success){
               	//String data= "Ce que je veux ecrire dans mon fichier \r\n";
               	FileOutputStream output = new FileOutputStream(myFile,true); //le true est pour écrire en fin de fichier, et non l'écraser
               	output.write(meeting_result.toString().getBytes());
            }
            else {Log.e("TEST1","ERROR DE CREATION DE DOSSIER");}
    		
    		return true;

    	}    	
    	catch (Exception e){
    		
    		Log.i("exception", "Write error : " + e.toString());
    		return false;
    	}
    	
    	
    }
    
    
    public boolean writeFile(String meeting_name, String text){
    	
    	try{
    		StringBuilder meeting_result = new StringBuilder();
    		meeting_result.append(output.toString());
    		File myFile = new File(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime",meeting_name.toString() + ".txt"); //on déclare notre futur fichier
    		FileOutputStream output = new FileOutputStream(myFile,true); //le true est pour écrire en fin de fichier, et non l'écraser
           	output.write(meeting_result.toString().getBytes());
    		return true;
    	}    	
    	catch (Exception e){
    		
    		Log.i("exception", "Write error : " + e.toString());
    		return false;
    	}
    	   	
    }
    
    public boolean endFile(String meeting_name, String text, String meeting_total_time){
    	
    	try{
    		StringBuilder meeting_result = new StringBuilder();
    		meeting_result.append("end: meeting \r\n\r\n");
    		meeting_result.append("total_time: " + meeting_total_time);
    		File myFile = new File(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime",meeting_name.toString() + ".txt"); //on déclare notre futur fichier
    		FileOutputStream output = new FileOutputStream(myFile,true); //le true est pour écrire en fin de fichier, et non l'écraser
           	output.write(meeting_result.toString().getBytes());
    		return true;
    	}    	
    	catch (Exception e){
    		Log.i("exception", "Write error : " + e.toString());
    		return false;
    	}
    	   	
    }*/
    
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
    
    /*public void myClickHandlerLinearLayoutEcoute(View v) {
    	
    	LinearLayout ll = (LinearLayout)v;
    	LinearLayout ll2 = (LinearLayout) ll.getChildAt(2);
    	CheckBox cb= (CheckBox) ll2.getChildAt(0);
		int position = Integer.parseInt(ll.getTag().toString());
		
		if (cb.isChecked()){
			//cb.setBackgroundResource(Color.GREEN);
				
			HashMap<String, Object> mapItemContact = (HashMap<String, Object>) lVDataEcoute.getItemAtPosition(position);
			listItemEcoute.remove(mapItemContact);		
			mapItemContact.put("isSelected", false);
			listItemEcoute.add(position, mapItemContact);	
			mScheduleEcoute.notifyDataSetChanged();
			//cb.setChecked(false);	
				
		}
		else{
			//cb.setBackgroundResource(Color.BLUE);
			HashMap<String, Object> mapItemContact = (HashMap<String, Object>) lVDataEcoute.getItemAtPosition(position);
			listItemEcoute.remove(mapItemContact);		
			mapItemContact.put("isSelected", true);
			listItemEcoute.add(position, mapItemContact);	
			mScheduleEcoute.notifyDataSetChanged();
			//cb.setChecked(true);
		}
		
	}*/
    
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
    
    public void myClickHandlerSSH(View v) {
		
    	CheckBox cb = (CheckBox)v;
		
		if (cb.isChecked()){
			//cb.setBackgroundResource(Color.GREEN);
			ssh = true;	
			
				
		}
		else{
			//cb.setBackgroundResource(Color.BLUE);
			ssh = false;	
				
		}
		
	}

	/*@Override
	public void OnDoubleTap(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
			
	}

	@Override
	public void OnSingleTap(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}*/
	
	
	public void ListeningToSpeaking(){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> map_original = null;
		HashMap<String, Object> map_modified = null; 
		int i = 0;
		
		for (i = 0; i<lVDataEcoute.getCount();i++){
			HashMap<String, Object> map = (HashMap<String, Object>) lVDataEcoute.getItemAtPosition(i);
			
			if ((Boolean) map.get("isSelected") == true){
				listItem.add(0, map);
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
			listItemParole.add(0, map_modified);
		   	
		   	mScheduleParole.notifyDataSetChanged();
 		    
 		    output.append("    " + getTime() + ";" + /*timestring + ";" + */map_modified.get("display_name") + ";speaker;" + "\r\n");
	    }
	}
	
	public void ListeningToSpeaking(int position){
		
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> map_original = null;
		HashMap<String, Object> map_modified = null; 
		
		HashMap<String, Object> map = (HashMap<String, Object>) lVDataEcoute.getItemAtPosition(position);
		listItem.add(map);
		
		listItemEcoute.remove(listItem.get(0));
		mScheduleEcoute.notifyDataSetChanged();
			
		map_original = new HashMap<String, Object>();
		map_original = (HashMap<String, Object>) listItem.get(0);
		map_modified = new HashMap<String, Object>();
		map_modified = (HashMap<String, Object>) listItem.get(0);
		map_modified.put("chronometre", new ChronoData((ChronoData) map_original.get("chronometre"), "start"));
		map_modified.put("isSelected", false);
		listItemParole.add(0, map_modified);
		   	
		mScheduleParole.notifyDataSetChanged();
 		    
 		output.append("    " + getTime() + ";" + /*timestring + ";" + */map_modified.get("display_name") + ";speaker;" + "\r\n");
	    
	}
	
	
	public void SpeakingToListening(){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> map_original = null;
		HashMap<String, Object> map_modified = null; 
		int i = 0;
		
		for (i = 0; i<lVDataParole.getCount();i++){
			HashMap<String, Object> map = (HashMap<String, Object>) lVDataParole.getItemAtPosition(i);
			
			if ((Boolean) map.get("isSelected") == true){
				listItem.add(0, map);
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
			listItemEcoute.add(0, map_modified);
		   	
		   	mScheduleEcoute.notifyDataSetChanged();
 		    
 		    output.append("    " + getTime() + ";" + map_modified.get("display_name") + ";listener;" + "\r\n");
	    }
	}
	
	public void SpeakingToListening(int position){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> map_original = null;
		HashMap<String, Object> map_modified = null; 
		
		HashMap<String, Object> map = (HashMap<String, Object>) lVDataParole.getItemAtPosition(position);
		listItem.add(0, map);
			
		listItemParole.remove(listItem.get(0));
		mScheduleParole.notifyDataSetChanged();
		
		map_original = new HashMap<String, Object>();
		map_original = (HashMap<String, Object>) listItem.get(0);
		map_modified = new HashMap<String, Object>();
		map_modified = (HashMap<String, Object>) listItem.get(0);
		map_modified.put("chronometre", new ChronoData((ChronoData) map_original.get("chronometre"), "stop"));
		map_modified.put("isSelected", false);
		listItemEcoute.add(0, map_modified);
		   	
		mScheduleEcoute.notifyDataSetChanged();
 		    
 		output.append("    " + getTime() + ";" + map_modified.get("display_name") + ";listener;" + "\r\n");
	    
	}
	
	public void ListeningToIdle(){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> map_original = null;
		HashMap<String, Object> map_modified = null; 
		int i = 0;
		
		for (i = 0; i<lVDataEcoute.getCount();i++){
			HashMap<String, Object> map = (HashMap<String, Object>) lVDataEcoute.getItemAtPosition(i);
			
			if ((Boolean) map.get("isSelected") == true){
				listItem.add(0, map);
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
			listItemInactif.add(0, map_modified);
		   	
			mScheduleInactif.notifyDataSetChanged();
 		    
 		    output.append("    " + getTime() + ";" + map_modified.get("display_name") + ";idle;" + "\r\n");
		}
		
	}
	
	public void ListeningToIdle(int position){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> map_original = null;
		HashMap<String, Object> map_modified = null; 
		
		HashMap<String, Object> map = (HashMap<String, Object>) lVDataEcoute.getItemAtPosition(position);
		listItem.add(0, map);
			
		listItemEcoute.remove(listItem.get(0));
		mScheduleEcoute.notifyDataSetChanged();
			
		map_original = new HashMap<String, Object>();
		map_original = (HashMap<String, Object>) listItem.get(0);
		map_modified = new HashMap<String, Object>();
		map_modified = (HashMap<String, Object>) listItem.get(0);
		map_modified.put("chronometre", new ChronoData((ChronoData) map_original.get("chronometre"), "no_change"));
		map_modified.put("isSelected", false);
		listItemInactif.add(0, map_modified);
		   	
		mScheduleInactif.notifyDataSetChanged();
 		    
 		output.append("    " + getTime() + ";" + map_modified.get("display_name") + ";idle;" + "\r\n");
		
	}
	
	public void SpeakingToIdle(){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> map_original = null;
		HashMap<String, Object> map_modified = null; 
		int i = 0;
		
		for (i = 0; i<lVDataParole.getCount();i++){
			HashMap<String, Object> map = (HashMap<String, Object>) lVDataParole.getItemAtPosition(i);
			
			if ((Boolean) map.get("isSelected") == true){
				listItem.add(0, map);
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
			listItemInactif.add(0, map_modified);
		   	
		   	mScheduleInactif.notifyDataSetChanged();
 		    
 		    output.append("    " + getTime() + ";" + map_modified.get("display_name") + ";idle;" + "\r\n");
 	    }
	}
	
	public void SpeakingToIdle(int position){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> map_original = null;
		HashMap<String, Object> map_modified = null; 
		
		HashMap<String, Object> map = (HashMap<String, Object>) lVDataParole.getItemAtPosition(position);
		listItem.add(0, map);
			
		listItemParole.remove(listItem.get(0));
		mScheduleParole.notifyDataSetChanged();
			
		map_original = new HashMap<String, Object>();
		map_original = (HashMap<String, Object>) listItem.get(0);
		map_modified = new HashMap<String, Object>();
		map_modified = (HashMap<String, Object>) listItem.get(0);
		map_modified.put("chronometre", new ChronoData((ChronoData) map_original.get("chronometre"), "stop"));
		map_modified.put("isSelected", false);
		listItemInactif.add(0, map_modified);
		   	
		mScheduleInactif.notifyDataSetChanged();
 		    
 		output.append("    " + getTime() + ";" + map_modified.get("display_name") + ";idle;" + "\r\n");
 	    
	}
	
	public void IdleToListening(){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> map_original = null;
		HashMap<String, Object> map_modified = null; 
		int i = 0;
		
		for (i = 0; i<lVDataInactif.getCount();i++){
			HashMap<String, Object> map = (HashMap<String, Object>) lVDataInactif.getItemAtPosition(i);
			
			if ((Boolean) map.get("isSelected") == true){
				listItem.add(0, map);
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
			listItemEcoute.add(0, map_modified);
		   	
		   	mScheduleEcoute.notifyDataSetChanged();
 		    
 		    output.append("    " + getTime() + ";" + map_modified.get("display_name") + ";listener;" + "\r\n");
		   				
	    }
	}
	
	public void IdleToListening(int position){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> map_original = null;
		HashMap<String, Object> map_modified = null; 
		
		HashMap<String, Object> map = (HashMap<String, Object>) lVDataInactif.getItemAtPosition(position);
		listItem.add(0, map);
			
		listItemInactif.remove(listItem.get(0));
		mScheduleInactif.notifyDataSetChanged();
			
		map_original = new HashMap<String, Object>();
		map_original = (HashMap<String, Object>) listItem.get(0);
		map_modified = new HashMap<String, Object>();
		map_modified = (HashMap<String, Object>) listItem.get(0);
		map_modified.put("chronometre", new ChronoData((ChronoData) map_original.get("chronometre"), "no_change"));
		map_modified.put("isSelected", false);
		listItemEcoute.add(0, map_modified);
		   	
		mScheduleEcoute.notifyDataSetChanged();
 		    
 		output.append("    " + getTime() + ";" + map_modified.get("display_name") + ";listener;" + "\r\n");
	}
	
	public void IdleToSpeaking(){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> map_original = null;
		HashMap<String, Object> map_modified = null; 
		int i = 0;
		
		for (i = 0; i<lVDataInactif.getCount();i++){
			HashMap<String, Object> map = (HashMap<String, Object>) lVDataInactif.getItemAtPosition(i);
			
			if ((Boolean) map.get("isSelected") == true){
				listItem.add(0, map);
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
			listItemParole.add(0, map_modified);
		   	
		   	mScheduleParole.notifyDataSetChanged();

 		    output.append("    " + getTime()  + ";" + map_modified.get("display_name") + ";speaker;" + "\r\n");
		   	
	    }
	}
	
	public void IdleToSpeaking(int position){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> map_original = null;
		HashMap<String, Object> map_modified = null; 
		
		HashMap<String, Object> map = (HashMap<String, Object>) lVDataInactif.getItemAtPosition(position);
		listItem.add(0, map);

		listItemInactif.remove(listItem.get(0));
		mScheduleInactif.notifyDataSetChanged();
			
		map_original = new HashMap<String, Object>();
		map_original = (HashMap<String, Object>) listItem.get(0);
		map_modified = new HashMap<String, Object>();
		map_modified = (HashMap<String, Object>) listItem.get(0);
		map_modified.put("chronometre", new ChronoData((ChronoData) map_original.get("chronometre"), "start"));
		map_modified.put("isSelected", false);
		listItemParole.add(0, map_modified);
		   	
	   	mScheduleParole.notifyDataSetChanged();

 		output.append("    " + getTime()  + ";" + map_modified.get("display_name") + ";speaker;" + "\r\n");
		   	
	}
	
	public String getTime(){
		
		Chronometer chronometerTotalTime = (Chronometer) findViewById(R.id.chronometerTotalTime);
		String timetotalstring = chronometerTotalTime.getText().toString();
		if (timetotalstring.length()==5){
		   	timetotalstring = "00:" + timetotalstring;
		}
		else if (timestring.length()==7){
		   	timetotalstring = "0" + timetotalstring;
	    }
		return timetotalstring;
	}
	
	protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        tache.cancel();

	}
	
	/*public boolean isListViewCheck(MyListView lV){
		boolean isCheck = false;
		for (int i = 0; i < lV.getCount(); i++ ){
			
			HashMap<String, Object> map = (HashMap<String, Object>) lV.getItemAtPosition(i);
			
			if ((Boolean) map.get("isSelected") == true){
				isCheck = true;
				break;
			}
			
		}
		
		return isCheck;
		
	}*/
	
	public boolean islVDataEcouteCheck(){
		boolean isCheck = false;
		for (int i = 0; i < lVDataEcoute.getCount(); i++ ){
			
			HashMap<String, Object> map = (HashMap<String, Object>) lVDataEcoute.getItemAtPosition(i);
			
			if ((Boolean) map.get("isSelected") == true){
				isCheck = true;
				break;
			}
		}
		return isCheck;
	}
	
	public boolean islVDataParoleCheck(){
		boolean isCheck = false;
		for (int i = 0; i < lVDataParole.getCount(); i++ ){
			
			HashMap<String, Object> map = (HashMap<String, Object>) lVDataParole.getItemAtPosition(i);
			
			if ((Boolean) map.get("isSelected") == true){
				isCheck = true;
				break;
			}
		}
		return isCheck;
	}
	
	public boolean islVDataInactifCheck(){
		boolean isCheck = false;
		for (int i = 0; i < lVDataInactif.getCount(); i++ ){
			
			HashMap<String, Object> map = (HashMap<String, Object>) lVDataInactif.getItemAtPosition(i);
			
			if ((Boolean) map.get("isSelected") == true){
				isCheck = true;
				break;
			}
		}
		return isCheck;
	}
	
	/*public void myClickHandlerListViewEcoute(View v) {
		
    	LinearLayout ll = (LinearLayout)v;
    	itemEcoutePosition = Integer.valueOf(ll.getTag().toString());
    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
		alertDialog.setTitle("Warning");
		alertDialog.setMessage("itemEcoutePosition : " + itemEcoutePosition);
		alertDialog.show();
		
	}*/
	
	private boolean back_answer = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {        
       if (keyCode == KeyEvent.KEYCODE_BACK) {
          AlertDialog.Builder builder = new AlertDialog.Builder(this);
          builder.setMessage("Etes vous sûr de vouloir quitter ?")
             .setCancelable(false)
             .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                   back_answer = true;
                   finish();
                }
             })
             .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                   back_answer = false;
                }
             });
           AlertDialog alert = builder.create();
           alert.show();
        }
       return back_answer;
           
     }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	if (requestCode == 1) {

    		if(resultCode == RESULT_OK){      

    			List<String> listSelectedContactToAdd = new ArrayList<String>();
    	    	//Bundle b = getIntent().getExtras();
    	    	String s = data.getStringExtra("result");
    	    	Scanner scanner = new Scanner(s);
    			while (scanner.hasNextLine()) {
    				String id_contact = scanner.nextLine();
    				listSelectedContactToAdd.add(id_contact);
    			}			
    	    	
    	    	Uri uri = ContactsContract.Contacts.CONTENT_URI;
    			String[] projection = new String[] { ContactsContract.Contacts._ID,
    	                                        ContactsContract.Contacts.DISPLAY_NAME/*,
    	                                        ContactsContract.Contacts.Data.DATA1*/};
    	    	
    	        // works in Honeycomb
    	        String selection = null;
    	        String[] selectionArgs = null;
    	        String sortOrder = null;
    	        
    	        //output.append("begin: participants list \r\n"); 
    	        
    	        CursorLoader cursorLoader = new CursorLoader(
    	        		this, 
    	                uri, 
    	                projection, 
    	                selection, 
    	                selectionArgs, 
    	                sortOrder);

    	        Cursor mCursor = cursorLoader.loadInBackground();
    	        
    	    	mCursor.moveToFirst();
    	    	
    	    	int i = 0;
    	    	
    	    	while (mCursor.isAfterLast() == false){
    	    		
    	    		if (listSelectedContactToAdd.indexOf(mCursor.getString(0)) != -1){
    	    			
    	    			HashMap<String, Object> map = new HashMap<String, Object>();
    	   		    	Bitmap bitmap = getPhoto(mCursor.getInt(0));
    		    		bitmap = Bitmap.createScaledBitmap (bitmap, 64, 64, true);
    		    		map.put("img", bitmap);
    		            map.put("display_name", mCursor.getString(1));
    		            map.put("isSelected", false);
    		        	map.put("tag2", i);
    		        	map.put("tag3", i);
    		        	map.put("onPause", false);
    		            
    		            //output.append( "    " + mCursor.getString(1) + "\r\n");
    		        	AddContactToGroup(mCursor.getString(0));
    		        	listSelectedContact.add(mCursor.getString(0));
    		        	AddContactToFile(mCursor.getString(1));
    		        	
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
    	    	
    	    	/*Calendar c = Calendar.getInstance();
    	        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	        String formattedDate = df.format(c.getTime());
    	    	
    	    	output.append("end: participants list  \r\n \r\nbegin: meeting " + formattedDate + "\r\n"); */
    	    	
    	        mScheduleEcoute = new /*SimpleAdapter*/MyListAdapterCheckmarkEcoute (MainActivity.this, this.getBaseContext(), listItemEcoute, R.layout.affichageitem_with_chekmark_ecoute,
    	                new String[] { "img", "display_name", "company_and_title", "chronometre", "tag2", "tag3"}, 
    	                new int[] { R.id.IMG_ecoute, R.id.DISPLAY_NAME_ecoute, R.id.COMPANY_AND_TITLE_ecoute, R.id.CHRONOMETRE_ecoute, R.id.tag2_ecoute,  R.id.tag3_ecoute});
    	        
    	        mScheduleEcoute.setViewBinder(new MyViewBinder()); // VOICI LA CLE!!!!
    	  	    
    	        //On attribut Ã  notre listView l'adapter que l'on vient de crÃ©er
    	        lVDataEcoute.setAdapter(mScheduleEcoute);
    			
    	        
    			
    			
    			
    	    }
    	    if (resultCode == RESULT_CANCELED) {    
    	         //Write your code if there's no result
    	    }
    	    
    	}
    }
    
    public void AddContactToFile(String display_name){
    	
    	//Add contact in the file
    	if (newParticipant == false){    	
    		output.insert(output.indexOf("end: participants list") + 22, 
    					  "\r\n\r\n" + 
    					  "begin: new participant"  +
    					  "\r\n    " + display_name + ";" +
    					  getTime() + ";" +
    					  "\r\n" +
    					  "end: new participant");
    		newParticipant = true;
    	}
    	else{
    		output.insert(output.indexOf("end: new participant"),
    					  "    " + display_name + ";" +
      					  getTime() + ";" +
      					  "\r\n");
    	}
     	
    }
    
    public void AddContactToGroup(String id_contact){
    	
    	//CrÃ©ation d'une instance de ma classe GroupBDD
	    GroupBDD groupBdd = new GroupBDD(this);
	    groupBdd.open();
	        	
	    Bundle b = getIntent().getExtras();
	    String SGroupName = b.getString("groupName");
	    Group group = new Group(SGroupName, id_contact);
	    		
		groupBdd.insertGroup(group);
	    groupBdd.close();
    }
    
    public Integer getTimeIntervalForSaveFile(){
    	
    	PreferencesBDD preferencesBdd = new PreferencesBDD(this);
    	
    	try{
			
			preferencesBdd.open();
			
			Cursor mCursor = preferencesBdd.getCursor();
			mCursor.moveToLast();
							
			int timeintervalforsavefile =  mCursor.getInt(NUM_COL_TIME_INTERVAL_FOR_SAVE_FILE);
			
			preferencesBdd.close();
			
			return timeintervalforsavefile * 60000;
		}
		catch (Exception e){
			preferencesBdd.close();
    		return 60000;
    	}
    			
    }

 	
}