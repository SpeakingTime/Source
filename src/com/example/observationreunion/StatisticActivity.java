package com.example.observationreunion;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class StatisticActivity extends Activity{
	
	List<File> filesList = new ArrayList<File>();
	ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	ListView lVStatistic;
	SimpleAdapter mSchedule = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistic);
		
		filesList = LoadFiles();
		
		for (int i = 0; i < filesList.size(); i++){
			File file = filesList.get(i);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("file_name", file.getName());
			
			System.out.println("FileName " + String.valueOf(i) + " : "  + file.getName());			
			
			map.put("tag_group", i);
			listItem.add(map);
		}
		
		
		lVStatistic = new ListView(this.getApplicationContext());
		lVStatistic = (ListView) findViewById(R.id.listViewStatistic);
		
        mSchedule = new MyListAdapterStatistic (this.getBaseContext(), listItem, R.layout.affichageitem_statistic,
                new String[] { "file_name", "tag_statistic"}, 
                new int[] { R.id.file_name, R.id.tag_statistic});
        
        lVStatistic.setAdapter(mSchedule);
		
		
		
	}
	
	public List<File> LoadFiles(){
		
		List<File> files = new ArrayList<File>();
		File myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime"); 
		File[] allFiles = myDir.listFiles();
		if (allFiles != null) {
			for (int i=0; i < allFiles.length; i++) {
				files.add(allFiles[i]);
				
			}
		}
		return files;
	}
	
	public void myClickHandlerStatistic(View v) {
		
		LinearLayout ll = (LinearLayout)v;
		int position = Integer.parseInt(ll.getTag().toString());
		
		HashMap<String, Object> mapItem = (HashMap<String, Object>) lVStatistic.getItemAtPosition(position);
		String filename = mapItem.get("file_name").toString();
		
		Intent intent = new Intent(StatisticActivity.this, StatisticResultActivity.class);
		intent.putExtra("selectedFile", filename);
		startActivity(intent);
		
		//TextView textViewGroupName = (TextView) findViewById (R.id.textView_group_name);	
		//textViewGroupName.setText(groupname);
		
	}
}
