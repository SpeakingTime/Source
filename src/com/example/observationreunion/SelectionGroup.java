package com.example.observationreunion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

	public class SelectionGroup extends Activity{
		
		private static final int NUM_COL_ID_GROUP = 0;
		private static final int NUM_COL_GROUP_NAME = 1;
		private static final int NUM_COL_ID_CONTACT = 2;
		
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		ListView lVGroup;
		SimpleAdapter mSchedule = null;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_selection_group);
			
			Button buttonSelectGroupAndDebuteMeeting = (Button) findViewById(R.id.buttonSelectGroupAndDebuteMeeting);
			buttonSelectGroupAndDebuteMeeting.setOnClickListener( 
					new Button.OnClickListener(){
						
						@Override
						public void onClick(View v) {
							TextView textViewGroupName = (TextView) findViewById (R.id.textView_group_name);
							String s_group_name_choiced =  textViewGroupName.getText().toString();
							
							if (!s_group_name_choiced.equalsIgnoreCase("Select a group")){
								Intent intent = new Intent(SelectionGroup.this, MainActivity.class);
								intent.putExtra("selectedContact", ValidateGroupSelection());
								startActivity(intent);
							}
							else {
								AlertDialog.Builder alertDialog = new AlertDialog.Builder(SelectionGroup.this);
    							alertDialog.setTitle("Warning");
    							alertDialog.setMessage("Please, select a group !");
    							alertDialog.show();
							}
						}
					});
			
			Button buttonDeleteGroup = (Button) findViewById(R.id.buttonDeleteGroup);
			buttonDeleteGroup.setOnClickListener(
					new Button.OnClickListener(){

						@Override
						public void onClick(View v) {
							RemoveGroup();	
						}
						
						
					});

			GroupBDD groupBdd = new GroupBDD(this);
			groupBdd.open();
			
			Cursor mCursor = groupBdd.getCursor();
			
			int i = 0;
					
			mCursor.moveToFirst();
			
			//initialisation de la variable indiquant le group_name courant
			String s_group_name_current = "";
						
			while (mCursor.isAfterLast() == false){
				
				String s_ID_group =  mCursor.getString(NUM_COL_ID_GROUP);
				String s_group_name =  mCursor.getString(NUM_COL_GROUP_NAME);
				String s_ID_contact =  mCursor.getString(NUM_COL_ID_CONTACT);
								
				if (!s_group_name_current.equalsIgnoreCase(mCursor.getString(NUM_COL_GROUP_NAME))) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("id_group", s_ID_group);
					map.put("group_name", s_group_name);
					map.put("tag_group", i);
					listItem.add(map); 
					s_group_name_current = mCursor.getString(NUM_COL_GROUP_NAME);
					i++;
				}
				
	    		mCursor.moveToNext();
	    			
	    	}
			mCursor.close();
					
			lVGroup = new ListView(this.getApplicationContext());
	        lVGroup = (ListView) findViewById(R.id.listViewContactsGroup_s);
			
	        mSchedule = new MyListAdapterGroup (this.getBaseContext(), listItem, R.layout.affichageitem_group,
	                new String[] { "group_name", "tag_group"}, 
	                new int[] { R.id.group_name, R.id.tag_group});
	        
	        lVGroup.setAdapter(mSchedule);
	
		}
		
		public void myClickHandlerGroup(View v) {
			
			LinearLayout ll = (LinearLayout)v;
			int position = Integer.parseInt(ll.getTag().toString());
			
			HashMap<String, Object> mapItem = (HashMap<String, Object>) lVGroup.getItemAtPosition(position);
			String groupname = mapItem.get("group_name").toString();
			TextView textViewGroupName = (TextView) findViewById (R.id.textView_group_name);	
			textViewGroupName.setText(groupname);
			
		}
					
		public String ValidateGroupSelection(){
			
			TextView textViewGroupName = (TextView) findViewById (R.id.textView_group_name);
			String s_group_name_choiced =  textViewGroupName.getText().toString();
			
			StringBuilder out = new StringBuilder();
			out.append("");
			
			if (!s_group_name_choiced.equalsIgnoreCase("")){
				
				GroupBDD groupBdd = new GroupBDD(this);
				groupBdd.open();						 
				Cursor mCursor = groupBdd.getCursor();
				mCursor.moveToFirst();
				
				while (mCursor.isAfterLast() == false){
					
					String s_ID_group =  mCursor.getString(NUM_COL_ID_GROUP);
					String s_group_name =  mCursor.getString(NUM_COL_GROUP_NAME);
					String s_ID_contact =  mCursor.getString(NUM_COL_ID_CONTACT);
									
					if (s_group_name_choiced.equalsIgnoreCase(mCursor.getString(NUM_COL_GROUP_NAME))) {
					
						out.append(s_ID_contact + "\n");
					
					}
					
		    		mCursor.moveToNext();
		    			
		    	}
				mCursor.close();
			}
			
			Log.i("ValidateSelection()", "ValidateSelection() : ");
			System.out.println("ValidateSelection() : " + out.toString());
			
			return out.toString();
		}
		
		public boolean RemoveGroup(){
			
			TextView textViewGroupName = (TextView) findViewById (R.id.textView_group_name);
			String s_group_name =  textViewGroupName.getText().toString();
			
			if (!s_group_name.equalsIgnoreCase("")){
			
				ModalDialog modalDialog = new ModalDialog();
				modalDialog.showConfirmDialog(this, "Supprimer le groupe : " +
											s_group_name);
			
				GroupBDD groupBdd = new GroupBDD(this);
				groupBdd.open();
				
				groupBdd.removeGroupWithgroup_name(s_group_name);
				
				Cursor mCursor = groupBdd.getCursor();
				mCursor.moveToFirst();
				
				//initialisation de la variable indiquant le group_name courant
				String s_group_name_current = "";

				int i =0;
				
				listItem.clear();
				
				while (mCursor.isAfterLast() == false){
					
					String s_ID_group =  mCursor.getString(NUM_COL_ID_GROUP);
					s_group_name =  mCursor.getString(NUM_COL_GROUP_NAME);
					String s_ID_contact =  mCursor.getString(NUM_COL_ID_CONTACT);
									
					if (!s_group_name_current.equalsIgnoreCase(mCursor.getString(NUM_COL_GROUP_NAME))) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("id_group", s_ID_group);
						map.put("group_name", s_group_name);
						map.put("tag_group", i);
						listItem.add(map); 
						s_group_name_current = mCursor.getString(NUM_COL_GROUP_NAME);
						i++;
					}
					mCursor.moveToNext();
		    	}
				mCursor.close();
								
				lVGroup = new ListView(this.getApplicationContext());
		        lVGroup = (ListView) findViewById(R.id.listViewContactsGroup_s);
				
		        mSchedule = null;
		        
		        mSchedule = new MyListAdapterGroup (this.getBaseContext(), listItem, R.layout.affichageitem_group,
		                new String[] { "group_name", "tag_group"}, 
		                new int[] { R.id.group_name, R.id.tag_group});
		        	        
		        lVGroup.setAdapter(mSchedule);
								
				groupBdd.close();
				return true;
			}
			else {
				return false;
			}							
		}

		public HashMap<String,Object> getContact(String id_phone_contact){
			
			try {
				HashMap<String,Object> map = new HashMap<String,Object>();
			
				Cursor mCursor = getContentResolver().query(
									ContactsContract.Data.CONTENT_URI,
									new String[] {ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME}, 
									ContactsContract.Contacts._ID + " = " + id_phone_contact + "'",
									null,
									null);
				
				String s_id = null;
		        //String s_display_name = null;
		        if (mCursor.moveToFirst()) {
		        	s_id = mCursor.getString(0);
		        	//s_display_name = mCursor.getString(1);
		            return map;
		        }
		        else {
		        	return null;
		        }
			}
			catch (Exception e){
				return null;
			}
		}
}
