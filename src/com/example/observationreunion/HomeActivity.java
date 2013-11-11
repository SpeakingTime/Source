package com.example.observationreunion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends Activity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		Button buttonSelectGroup = (Button) findViewById(R.id.buttonSelectGroup);
		buttonSelectGroup.setOnClickListener( 
				new Button.OnClickListener(){
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(HomeActivity.this, SelectionGroup.class);
						startActivity(intent);
					}
				
		});
		
		Button buttonEditGroup = (Button) findViewById(R.id.buttonEditGroup);
		buttonEditGroup.setOnClickListener( 
				new Button.OnClickListener(){
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(HomeActivity.this, SelectionGroupToEdit.class);
						startActivity(intent);
					}
				
		});
		
		Button buttonCreateGroupOrSelectContacts = (Button) findViewById(R.id.buttonCreateGroupOrSelectContacts);
		buttonCreateGroupOrSelectContacts.setOnClickListener( 
				new Button.OnClickListener(){
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(HomeActivity.this, SelectionContact.class);
						startActivity(intent);
					}
				
		});
	}
	
	
}
