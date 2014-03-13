package com.example.observationreunion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SelectionGroupToEdit extends Activity{
	
	private static final int NUM_COL_ID_GROUP = 0;
	private static final int NUM_COL_GROUP_NAME = 1;
	private static final int NUM_COL_ID_CONTACT = 2;
	
	ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	ListView lVGroupToEdit;
	SimpleAdapter mSchedule = null;
	String s_group_name_choiced = "";
	String s_group_id_choiced = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selection_group_to_edit);
		
		Button buttonSelectGroupToEdit = (Button) findViewById(R.id.buttonSelectGroupToEdit);
		buttonSelectGroupToEdit.setOnClickListener( 
				new Button.OnClickListener(){
					
					@Override
					public void onClick(View v) {
						
						
						TextView textViewGroupName = (TextView) findViewById (R.id.textView_group_name_to_edit);
						String s_group_name_choiced =  textViewGroupName.getText().toString();
						
						if (!s_group_name_choiced.equalsIgnoreCase("Select a group first")){
							Intent intent = new Intent(SelectionGroupToEdit.this, SelectionContactToEdit.class);
							intent.putExtra("selectedContact", ValidateGroupSelection());
							intent.putExtra("groupName", GetGroupName());
							intent.putExtra("groupID", GetGroupID());
							startActivity(intent);
							finish();
						}
						else {
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(SelectionGroupToEdit.this);
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
							
						TextView textViewGroupName = (TextView) findViewById (R.id.textView_group_name_to_edit);
						String s_group_name_choiced =  textViewGroupName.getText().toString();
												
						if (!s_group_name_choiced.equalsIgnoreCase("Select a group")){
							RemoveGroup();
						}
						else {
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(SelectionGroupToEdit.this);
							alertDialog.setTitle("Warning");
							alertDialog.setMessage("Please, select a group !");
							alertDialog.show();
						}
						
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
		
		lVGroupToEdit = new ListView(this.getApplicationContext());
		lVGroupToEdit = (ListView) findViewById(R.id.listViewGroupNameToEdit);
		
        mSchedule = new MyListAdapterGroup (this.getBaseContext(), listItem, R.layout.affichageitem_group,
                new String[] { "group_name", "tag_group"}, 
                new int[] { R.id.group_name, R.id.tag_group});
        
        lVGroupToEdit.setAdapter(mSchedule);
		
	}
	
	public void myClickHandlerGroup(View v) {
		
		LinearLayout ll = (LinearLayout)v;
		int position = Integer.parseInt(ll.getTag().toString());
		
		HashMap<String, Object> mapItem = (HashMap<String, Object>) lVGroupToEdit.getItemAtPosition(position);
		s_group_name_choiced = mapItem.get("group_name").toString();
		s_group_id_choiced = mapItem.get("id_group").toString();;
		
		TextView textViewGroupName = (TextView) findViewById (R.id.textView_group_name_to_edit);	
		textViewGroupName.setText(s_group_name_choiced);
		
	}

	
	public String ValidateGroupSelection(){
		
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
		
		TextView textViewGroupName = (TextView) findViewById (R.id.textView_group_name_to_edit);
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
							
			lVGroupToEdit = new ListView(this.getApplicationContext());
			lVGroupToEdit = (ListView) findViewById(R.id.listViewGroupNameToEdit);
			
	        mSchedule = null;
	        
	        mSchedule = new MyListAdapterGroup (this.getBaseContext(), listItem, R.layout.affichageitem_group,
	                new String[] { "group_name", "tag_group"}, 
	                new int[] { R.id.group_name, R.id.tag_group});
	        	        
	        lVGroupToEdit.setAdapter(mSchedule);
							
			groupBdd.close();
			return true;
		}
		else {
			return false;
		}							
	}
	
	public String GetGroupName(){
		return s_group_name_choiced;
	}
	
	public String GetGroupID(){
		return s_group_id_choiced;
	}

}
