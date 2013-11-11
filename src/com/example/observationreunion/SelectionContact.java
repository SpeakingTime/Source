package com.example.observationreunion;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SelectionContact extends Activity{
	
	ListView lvContact;
	ArrayList<HashMap<String, Object>> listItemContact = new ArrayList<HashMap<String, Object>>();
	MyListAdapter mSchedule = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selection_contact);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		
		lvContact = (ListView) findViewById(R.id.ListViewCheckBox);
		
		InitContact(this);
				
		Button buttonSelectContacts = (Button) findViewById(R.id.buttonSelectContacts);
		buttonSelectContacts.setOnClickListener( 
				new Button.OnClickListener(){
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(SelectionContact.this, MainActivity.class);
						intent.putExtra("selectedContact", ValidateSelection());
						startActivity(intent);
						finish();
					}
				
		});
		
		Button buttonCreateTheGroup = (Button) findViewById(R.id.buttonCreateTheGroup);
		buttonCreateTheGroup.setOnClickListener( 
				new Button.OnClickListener(){
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(SelectionContact.this, CreateGroupActivity.class);
						intent.putExtra("selectedContact", ValidateSelection());
						startActivity(intent);
						finish();
					}
				
		});
		
	}

	
	public void InitContact(Activity activity){
    	
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
		
		for (int i=0;i<listItemContact.size();i++){
			
			HashMap<String, Object> mapItemContact = (HashMap<String, Object>) listItemContact.get(i);
	    	
	    	if ((Boolean) mapItemContact.get("isSelected") == true){

				out.append(mapItemContact.get("id_contact") + "\n");
	    		
	    	}
			
		}
		Log.i("ValidateSelection()", "ValidateSelection() : ");
		System.out.println("ValidateSelection() : " + out.toString());
		
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
			mapItemContact.put("isSelected", true);
			listItemContact.add(position, mapItemContact);	
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
			mapItemContact.put("isSelected", false);
			listItemContact.add(position, mapItemContact);	
			mSchedule.notifyDataSetChanged();
			//Log.i("id_contact", mapItemContact.get("id_contact").toString());
			//Log.i("nom_contact", mapItemContact.get("nom_contact").toString());
			//Log.i("isSelected",  mapItemContact.get("isSelected").toString());
			//Log.i("tag",  mapItemContact.get("tag").toString());
		}
		
		
	}

}
