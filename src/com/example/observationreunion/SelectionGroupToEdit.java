package com.example.observationreunion;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SelectionGroupToEdit extends Activity{
	
	private static final int NUM_COL_ID_GROUP = 0;
	private static final int NUM_COL_GROUP_NAME = 1;
	private static final int NUM_COL_ID_CONTACT = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selection_group_to_edit);
		
		Button buttonSelectGroupToEdit = (Button) findViewById(R.id.buttonSelectGroupToEdit);
		buttonSelectGroupToEdit.setOnClickListener( 
				new Button.OnClickListener(){
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(SelectionGroupToEdit.this, SelectionContactToEdit.class);
						intent.putExtra("selectedContact", ValidateGroupSelection());
						intent.putExtra("groupName", GetGroupName());
						intent.putExtra("groupID", GetGroupID());
						startActivity(intent);
					}
				});
		
		
		Spinner spinnerGroupNameToEdit = (Spinner) findViewById(R.id.spinnerGroupName_to_edit);
		
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
		spinnerGroupNameToEdit.setAdapter(adapter);
		
	}

	
	public String ValidateGroupSelection(){
		
		Spinner spinnerGroupName = (Spinner) findViewById(R.id.spinnerGroupName_to_edit);
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
		
		
	}
	
	public String GetGroupName(){
		
		Spinner spinnerGroupNameToEdit = (Spinner) findViewById(R.id.spinnerGroupName_to_edit);
		return spinnerGroupNameToEdit.getSelectedItem().toString();
		 
	}
	
	public String GetGroupID(){
		
		Spinner spinnerGroupNameToEdit = (Spinner) findViewById(R.id.spinnerGroupName_to_edit);
		return spinnerGroupNameToEdit.getSelectedItem().toString();
		 
	}
	
	
	
}
