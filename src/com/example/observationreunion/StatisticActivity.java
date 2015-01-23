package com.example.observationreunion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class StatisticActivity extends Activity{
	
	StatisticLoadFiles s = new StatisticLoadFiles();
	
	List<FileAndDate> filesList = new ArrayList<FileAndDate>();	
	
	ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	ListView lVStatistic;
	SimpleAdapter mSchedule = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
				
		Bundle b = getIntent().getExtras();
    	String statistic_type = b.getString("statistic_type");
    	System.out.println("   statistic_type : " + statistic_type);
    	
    	if (statistic_type.equals("histograms")) {
    		setContentView(R.layout.activity_statistic);
    	}
    	else if (statistic_type.equals("boxplots")) {
    		setContentView(R.layout.activity_statistic_with_checkmark);
    	}
    		    	
		filesList = s.LoadFiles();
			
		for (int i = 0; i < filesList.size(); i++){
			FileAndDate fileAndDate = filesList.get(i);
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("file_name", fileAndDate.getName());
			map.put("date_file", s.getDateFile(fileAndDate.getName()));
			
			if (statistic_type.equals("boxplots")) {
				map.put("isSelected", false);
				map.put("tag_checkbox", i);
			}
			
			map.put("tag_group", i);
			listItem.add(map);
		}
		
		if (statistic_type.equals("histograms")) {
		
			lVStatistic = new ListView(this.getApplicationContext());
			lVStatistic = (ListView) findViewById(R.id.listViewStatistic);
			
	        mSchedule = new MyListAdapterStatistic (this.getBaseContext(), listItem, R.layout.affichageitem_statistic,
	                new String[] { "file_name", "date_file", "tag_statistic"}, 
	                new int[] { R.id.file_name, R.id.date_file, R.id.tag_statistic});
	        
	        lVStatistic.setAdapter(mSchedule);
	        
		}
    	else if (statistic_type.equals("boxplots")) {
    		
    		lVStatistic = new ListView(this.getApplicationContext());
    		lVStatistic = (ListView) findViewById(R.id.listViewStatisticWithCheckmark);
    		
            mSchedule = new MyListAdapterStatisticWithCheckMark (this.getBaseContext(), listItem, R.layout.affichageitem_statistic_with_checkmark,
                    new String[] { "file_name", "date_file", "tag_statistic", "tag_checkbox"}, 
                    new int[] { R.id.file_name, R.id.date_file, R.id.tag_statistic, R.id.tag_checkbox});
            
            lVStatistic.setAdapter(mSchedule);
    		
            Button buttonBarGraph2 = (Button) findViewById(R.id.buttonBarGraph2);
            buttonBarGraph2.setOnClickListener( 
    				new Button.OnClickListener(){
    					
    					@Override
    					public void onClick(View v) {
    						// TODO Auto-generated method stub
    						int nbboxplots = 0;
    						
    						for (int i = 0; i<lVStatistic.getCount();i++){
    							HashMap<String, Object> map = (HashMap<String, Object>) lVStatistic.getItemAtPosition(i);
    							
    							if ((Boolean) map.get("isSelected") == true){
    								nbboxplots = nbboxplots + 1;
    							}    							
    						}
    						
    						Intent intent =null;
    						String reunionName = "";
    						
    						int j = 0;
    						
    						for (int i = 0; i<lVStatistic.getCount();i++){
    							HashMap<String, Object> map = (HashMap<String, Object>) lVStatistic.getItemAtPosition(i);
    							reunionName = map.get("file_name").toString();
    							
    							if ((Boolean) map.get("isSelected") == true){
    								j = j + 1;
        							if (j == 1) {
    									intent = new Intent(StatisticActivity.this, BarGraph2Activity.class);
    									intent.putExtra("nbboxplots", String.valueOf(nbboxplots));
    								}
    								System.out.println("participantsWithSpeakingTime" + String.valueOf(j));
    								intent.putExtra("participantsWithSpeakingTime" + String.valueOf(j), s.getParticipantsWithSpeakingTime(map.get("file_name").toString()));
    								intent.putExtra("reunionName" + String.valueOf(j), reunionName.substring(0, reunionName.length()-4));
    								System.out.println("reunionName" + String.valueOf(j) + ": " + reunionName.substring(0, reunionName.length()-4));
    							}    							
    						}
    						
    						if (j > 0){
    							
    							startActivity(intent);
    						}
    					}
    				
    		});		
    	}
		

		
		
		
		
	}
	
	public void myClickHandlerStatistic(View v) {
		
		LinearLayout ll = (LinearLayout)v;
		int position = Integer.parseInt(ll.getTag().toString());
		
		HashMap<String, Object> mapItem = (HashMap<String, Object>) lVStatistic.getItemAtPosition(position);
		String filename = mapItem.get("file_name").toString();
		System.out.println("filename : " + filename);
		
		Intent intent = new Intent(StatisticActivity.this, StatisticResultActivity.class);
		intent.putExtra("selectedFile", filename);
		startActivity(intent);
		
	}
	
    public void myClickHandlerStatisticCheckBox(View v) {
		
    	CheckBox cb = (CheckBox)v;
		int position = Integer.parseInt(cb.getTag().toString());
		
		if (cb.isChecked()){
			HashMap<String, Object> mapItem = (HashMap<String, Object>) lVStatistic.getItemAtPosition(position);
			listItem.remove(mapItem);		
			mapItem.put("isSelected", true);
			listItem.add(position, mapItem);	
			mSchedule.notifyDataSetChanged();
				
		}
		else{
			HashMap<String, Object> mapItem = (HashMap<String, Object>) lVStatistic.getItemAtPosition(position);
			listItem.remove(mapItem);		
			mapItem.put("isSelected", false);
			listItem.add(position, mapItem);	
			mSchedule.notifyDataSetChanged();
				
		}
    }
	
}
