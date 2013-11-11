package com.example.observationreunion;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

	public class SelectionGroup extends Activity{
		
		private static final int NUM_COL_ID_GROUP = 0;
		private static final int NUM_COL_GROUP_NAME = 1;
		private static final int NUM_COL_ID_CONTACT = 2;
				
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_selection_group);
			
			Button buttonSelectGroupAndDebuteMeeting = (Button) findViewById(R.id.buttonSelectGroupAndDebuteMeeting);
			buttonSelectGroupAndDebuteMeeting.setOnClickListener( 
					new Button.OnClickListener(){
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(SelectionGroup.this, MainActivity.class);
							intent.putExtra("selectedContact", ValidateGroupSelection());
							startActivity(intent);
						}
					});
			
			Button buttonDeleteGroup = (Button) findViewById(R.id.buttonDeleteGroup);
			buttonDeleteGroup.setOnClickListener(
					new Button.OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
						
							RemoveGroup();	
							
						}
						
						
					});
			
			Spinner spinnerGroupName = (Spinner) findViewById(R.id.spinnerGroupName);
			
			GroupBDD groupBdd = new GroupBDD(this);
			groupBdd.open();
						 
			Cursor mCursor = groupBdd.getCursor();
			
			int i = 0;
			//int imax = mCursor.getCount();
			
			//String[] array_spinner = new String[imax];
			List list_spinner = new LinkedList();
			
			mCursor.moveToFirst();
			
			//initialisation de la variable indiquant le group_name courant
			String s_group_name_current = "";
						
			while (mCursor.isAfterLast() == false){
				
				String s_ID_group =  mCursor.getString(NUM_COL_ID_GROUP);
				String s_group_name =  mCursor.getString(NUM_COL_GROUP_NAME);
				String s_ID_contact =  mCursor.getString(NUM_COL_ID_CONTACT);
								
				if (!s_group_name_current.equalsIgnoreCase(mCursor.getString(NUM_COL_GROUP_NAME))) {
				
					//array_spinner[i] = s_ID_group + " " + s_group_name + " " + s_ID_contact;
					//array_spinner[i] = s_ID_group + " " + s_group_name;
					
					//list_spinner.add(s_ID_group + " " + s_group_name);
					list_spinner.add(s_group_name);
										
					//ModalDialog modalDialog = new ModalDialog();
					//modalDialog.showAlertDialog(this, array_spinner[i] + ", i = " + String.valueOf(i));
					//modalDialog.showAlertDialog(this, list_spinner.get(i).toString() );
										
					s_group_name_current = mCursor.getString(NUM_COL_GROUP_NAME);
					
					i++;
				
				}
				
	    		mCursor.moveToNext();
	    			
	    	}
			mCursor.close();
			
			String[] array_spinner = new String[i];
			
			for (int j=0;j<i;j++){
				array_spinner[j] = list_spinner.get(j).toString();
			}
			
			ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_spinner);
			spinnerGroupName.setAdapter(adapter);
			
	
		}
		
		
		
		public String ValidateGroupSelection(){
			
			Spinner spinnerGroupName = (Spinner) findViewById(R.id.spinnerGroupName);
			String s_group_name_choiced =  spinnerGroupName.getSelectedItem().toString();
			
			StringBuilder out = new StringBuilder();
			out.append("");
			
			if (!s_group_name_choiced.equalsIgnoreCase("")){
			
				ModalDialog modalDialog = new ModalDialog();
				modalDialog.showConfirmDialog(this, "Choisir ce groupe : " +
													s_group_name_choiced);
				
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
			
			
			
			/*Bundle b = getIntent().getExtras();
	    	String s = b.getString("selectedContact");
	    	Scanner scanner = new Scanner(s);
			while (scanner.hasNextLine()) {
				String id_contact = scanner.nextLine();
				//System.out.println("id_contact : " + id_contact);
			
				getContact(id_contact);
			
			}			*/
		}
		
		public boolean RemoveGroup(){
			
			Spinner spinnerGroupName = (Spinner) findViewById(R.id.spinnerGroupName);
			String s_group_name =  spinnerGroupName.getSelectedItem().toString();
			
			if (!s_group_name.equalsIgnoreCase("")){
			
				ModalDialog modalDialog = new ModalDialog();
				modalDialog.showConfirmDialog(this, "Supprimer le groupe : " +
											s_group_name);
			
				GroupBDD groupBdd = new GroupBDD(this);
				groupBdd.open();
				
				groupBdd.removeGroupWithgroup_name(s_group_name);
				//groupBdd.removeGroupWith_ID_group(17);
				
				Cursor mCursor = groupBdd.getCursor();
				mCursor.moveToFirst();
				
				//initialisation de la variable indiquant le group_name courant
				String s_group_name_current = "";
				List list_spinner = new LinkedList();
				int i =0;
				
				while (mCursor.isAfterLast() == false){
					
					String s_ID_group =  mCursor.getString(NUM_COL_ID_GROUP);
					s_group_name =  mCursor.getString(NUM_COL_GROUP_NAME);
					String s_ID_contact =  mCursor.getString(NUM_COL_ID_CONTACT);
									
					if (!s_group_name_current.equalsIgnoreCase(mCursor.getString(NUM_COL_GROUP_NAME))) {
					
						list_spinner.add(s_group_name);
						s_group_name_current = mCursor.getString(NUM_COL_GROUP_NAME);
						
						i++;
					
					}
					
		    		mCursor.moveToNext();
		    			
		    	}
				mCursor.close();
				
				String[] array_spinner = new String[i];
				
				for (int j=0;j<i;j++){
					array_spinner[j] = list_spinner.get(j).toString();
				}
				
				ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_spinner);
				spinnerGroupName.setAdapter(adapter);
				
				
				
				
				
				
				
				
				
				
				
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
