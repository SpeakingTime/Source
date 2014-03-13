package com.example.observationreunion;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

public class SelectionContactToAdd extends Activity{
	
	ListView lvContact;
	ArrayList<HashMap<String, Object>> listItemContact = new ArrayList<HashMap<String, Object>>();
	ArrayList<HashMap<String, Object>> listItemContactForFilter = new ArrayList<HashMap<String, Object>>();
	MyListAdapter mSchedule = null;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selection_contact_to_add);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        lvContact = (ListView) findViewById(R.id.ListViewCheckBox);
		
		InitContact(this);
		
		Button buttonAddContact = (Button) findViewById(R.id.buttonAddContact);
		buttonAddContact.setOnClickListener( 
				new Button.OnClickListener(){
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						Intent returnIntent = new Intent();
						returnIntent.putExtra("result", ValidateSelection());
						System.out.println(ValidateSelection());
						setResult(RESULT_OK,returnIntent);     
						finish();
						
					}
				
		});	
		
		Button buttonCancelAddContact = (Button) findViewById(R.id.buttonCancelAddContact);
		buttonCancelAddContact.setOnClickListener( 
				new Button.OnClickListener(){
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						Intent returnIntent = new Intent();
						setResult(RESULT_CANCELED, returnIntent);        
						finish();
						
					}
				
		});
		
		EditText editTextNameFilter = (EditText) findViewById(R.id.nameFilter);
		editTextNameFilter.addTextChangedListener(new TextWatcher() {
		
	        @Override
	        public void onTextChanged(CharSequence s, int start, int before,  int count) {
	        	
	        	listItemContact.clear(); 
	        	mSchedule.notifyDataSetChanged();
	        	
	        	for (int i = 0; i < listItemContactForFilter.size() ; i++){
	        		HashMap<String, Object> mapItemContact = (HashMap<String, Object>) listItemContactForFilter.get(i);
	        		String contactName = mapItemContact.get("nom_contact").toString();
	        		System.out.println(contactName.substring(0, s.length()) + " -- s = " + s.toString().toLowerCase());
	        		if (contactName.toLowerCase().substring(0, s.length()).equals(s.toString().toLowerCase())) {
	        			listItemContact.add(listItemContactForFilter.get(i));
	        		}
	        	}
	        }
	        
	        @Override
	        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	        
	        }
	        
	        @Override
			public void afterTextChanged(Editable arg0) {
				
			}
	        
	    });
		
	}
	
	public void InitContact(Activity activity){
    	
		//On enlève les personnes déjà présentes dans la réunion
		List<String> listSelectedContact = new ArrayList<String>();
		Bundle b = getIntent().getExtras();
        String s = b.getString("selectedContact");
    	Scanner scanner = new Scanner(s);
    	while (scanner.hasNextLine()) {
			String id_contact = scanner.nextLine();
			listSelectedContact.add(id_contact);
		}
		
    	Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] { ContactsContract.Contacts._ID,
                                        ContactsContract.Contacts.DISPLAY_NAME};
    	
        // works in Honeycomb
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;

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
    		
	    	if (listSelectedContact.indexOf(mCursor.getString(0)) == -1) {
    			HashMap<String, Object> map = new HashMap<String, Object>();
	    		map.put("id_contact", mCursor.getString(0));
	    		map.put("nom_contact", mCursor.getString(1));
	    		System.out.println(mCursor.getString(0));
	    		
	    		Bitmap bitmap = getPhoto(mCursor.getInt(0));
	    		bitmap = Bitmap.createScaledBitmap (bitmap, 64, 64, true);
	    		
	    		//Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.logo_contact);
	    		
	    		map.put("photo", bitmap);
	    		
	    		map.put("isSelected", false);
	        	map.put("tag", i);
	    		listItemContact.add(map); 
	    		listItemContactForFilter.add(map); 
	    	}
	    	
    		i++;
    		mCursor.moveToNext();
    	}
    	mCursor.close();
    	
    	/*SimpleAdapter*/ mSchedule = new /*SimpleAdapter*/ MyListAdapter (this.getBaseContext(), listItemContact, R.layout.affichageitem_checkbox,
                new String[] { "nom_contact", "photo", "tag"}, 
                new int[] { R.id.nom_contact, R.id.photo, R.id.tag});
    			//new String[] { "nom_contact", "tag"}, 
    			//new int[] { R.id.nom_contact, R.id.tag});

    	
    	mSchedule.setViewBinder(new MyViewBinder()); // VOICI LA CLE!!!!
    	
         // Set the ArrayAdapter as the ListView's adapter.  
    	lvContact.setAdapter(mSchedule);
    	
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
	
	public String ValidateSelection(){
	    
		StringBuilder out = new StringBuilder();
		out.append("");
		
		/*for (int i=0;i<lvContact.getAdapter().getCount();i++){
		    
			Log.i("ValidateSelection()", "ValidateSelection() : " + String.valueOf(i));
			
			CheckBox checkBox = (CheckBox) lvContact.getChildAt(i).findViewById(R.id.checkbox);
			
	    	HashMap<String, Object> mapItemContact = (HashMap<String, Object>) lvContact.getAdapter().getItem(i);
	    		    	
	    	if (checkBox.isChecked()){

				out.append(mapItemContact.get("id_contact") + "\n");
	    		
	    	}
			
		}*/
		
		for (int i=0;i<listItemContactForFilter.size();i++){
			
			HashMap<String, Object> mapItemContact = (HashMap<String, Object>) listItemContactForFilter.get(i);
	    	
	    	if ((Boolean) mapItemContact.get("isSelected") == true){

				out.append(mapItemContact.get("id_contact") + "\n");
	    		
	    	}
			
		}
				
		return out.toString();

	}
	
	public void myClickHandler(View v) {
		CheckBox cb = (CheckBox)v;
		int position = Integer.parseInt(cb.getTag().toString());
		View o = (View)lvContact.getChildAt(position);
		if (cb.isChecked())
		{
			//cb.setBackgroundResource(/*R.color.green*/Color.GREEN);
			
			HashMap<String, Object> mapItemContact = (HashMap<String, Object>) lvContact.getItemAtPosition(position);
			listItemContact.remove(mapItemContact);	
			listItemContactForFilter.remove(mapItemContact);	
			mapItemContact.put("isSelected", true);
			listItemContact.add(position, mapItemContact);
			listItemContactForFilter.add(position, mapItemContact);	
			mSchedule.notifyDataSetChanged();
			
				
			
			
			
			//Log.i("id_contact", mapItemContact.get("id_contact").toString());
			//Log.i("nom_contact", mapItemContact.get("nom_contact").toString());
			//Log.i("isSelected",  mapItemContact.get("isSelected").toString());
			//Log.i("tag",  mapItemContact.get("tag").toString());
		}
		else
		{
			//cb.setBackgroundResource(/*R.color.blue*/Color.BLUE);
			HashMap<String, Object> mapItemContact = (HashMap<String, Object>) lvContact.getItemAtPosition(position);
			listItemContact.remove(mapItemContact);		
			listItemContactForFilter.remove(mapItemContact);
			mapItemContact.put("isSelected", false);
			listItemContact.add(position, mapItemContact);
			listItemContactForFilter.add(position, mapItemContact);
			mSchedule.notifyDataSetChanged();
			
					
			
			//Log.i("id_contact", mapItemContact.get("id_contact").toString());
			//Log.i("nom_contact", mapItemContact.get("nom_contact").toString());
			//Log.i("isSelected",  mapItemContact.get("isSelected").toString());
			//Log.i("tag",  mapItemContact.get("tag").toString());
		}
		
		
	}


	

}
