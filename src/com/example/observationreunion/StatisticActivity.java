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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class StatisticActivity extends Activity{
	
	//List<HashMap<String, Object>> filesList = new ArrayList<HashMap<String, Object>>();
	List<FileAndDate> filesList = new ArrayList<FileAndDate>();	
	
	//List<File> filesList = new ArrayList<File>();
	
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
			//HashMap<String, Object> file = filesList.get(i);
			FileAndDate fileAndDate = filesList.get(i);
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			//map.put("file_name", file.get("file_name"));
			//map.put("date_file", file.get("date_file"));
			map.put("file_name", fileAndDate.getName());
			map.put("date_file", getDateFile(fileAndDate.getName()));
			
			//System.out.println("FileName " + String.valueOf(i) + " : "  + file.getName());			
			
			map.put("tag_group", i);
			listItem.add(map);
		}
		
		/*filesList = LoadFiles();
		
		for (int i = 0; i < filesList.size(); i++){
			File file = filesList.get(i);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("file_name", file.getName());
			map.put("date_file", getDateFile(file.getName()));
			
			System.out.println("FileName " + String.valueOf(i) + " : "  + file.getName());			
			
			map.put("tag_group", i);
			listItem.add(map);
		}*/
		
		
		lVStatistic = new ListView(this.getApplicationContext());
		lVStatistic = (ListView) findViewById(R.id.listViewStatistic);
		
        mSchedule = new MyListAdapterStatistic (this.getBaseContext(), listItem, R.layout.affichageitem_statistic,
                new String[] { "file_name", "date_file", "tag_statistic"}, 
                new int[] { R.id.file_name, R.id.date_file, R.id.tag_statistic});
        
        lVStatistic.setAdapter(mSchedule);
		
		
		
	}
	
	/*public List<File> LoadFiles(){
		
		List<File> files = new ArrayList<File>();
		File myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime"); 
		File[] allFiles = myDir.listFiles();
		if (allFiles != null) {
			for (int i=0; i < allFiles.length; i++) {
				files.add(0, allFiles[i]);
				
			}
		}
		return files;
	}*/
	
	/*public List<HashMap<String, Object>> LoadFiles(){
		
		List<HashMap<String, Object>> filesList = new ArrayList<HashMap<String, Object>>();
		
		List<File> files = new ArrayList<File>();
		File myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime"); 
		File[] allFiles = myDir.listFiles();
		if (allFiles != null) {
			for (int i=0; i < allFiles.length; i++) {
				
				String str_date = getDateFile(allFiles[i].getName());
				SimpleDateFormat formatter ; 
				Date date = null ; 
				formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					date = formatter.parse(str_date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(date.toString());
				
				//if (date.after(datecompare)){
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("file_name", allFiles[i].getName());
					map.put("date_file", getDateFile(allFiles[i].getName()));
					filesList.add(map);
				//}
			}
		}
		return filesList;
		
	}*/
	
	
	public List<FileAndDate> LoadFiles(){
		
		List<FileAndDate> filesList = new ArrayList<FileAndDate>();
		
		List<File> files = new ArrayList<File>();
		File myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime"); 
		File[] allFiles = myDir.listFiles();
		if (allFiles != null) {
			for (int i=0; i < allFiles.length; i++) {
				
				String str_date = getDateFile(allFiles[i].getName());
				SimpleDateFormat formatter ; 
				Date date = null ; 
				formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					date = formatter.parse(str_date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(date.toString());
				
				//if (date.after(datecompare)){
					/*HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("file_name", allFiles[i].getName());
					map.put("date_file", getDateFile(allFiles[i].getName()));
					filesList.add(map);*/
					FileAndDate fileAndDate = new FileAndDate(allFiles[i].getName(), date);
					filesList.add(fileAndDate);
				//}
			}
		}
		
		Collections.sort(filesList, new DateComparator());
		
		return filesList;
		
	}
	
	public String getDateFile(String filename){
		
		boolean dateFound = false;
		String dateFile = null;
		
		try {
			FileInputStream objFile = new FileInputStream(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime" + File.separator +  filename);
			InputStreamReader objReader = new InputStreamReader(objFile);
			BufferedReader objBufferReader = new BufferedReader(objReader);
			String strLine;
			while ((strLine = objBufferReader.readLine()) != null) {
				
				if ((strLine.indexOf("begin: meeting") != -1) && (dateFound == false)) { 
					dateFound = true;
					dateFile = strLine.substring(15, strLine.length());
					
				}
				
				
			}
			objFile.close();
			
		}
		catch (FileNotFoundException objError) {
			System.out.println("Fichier non trouvé\n"+objError.toString());
		}
		catch (IOException objError) {
			System.out.println("Erreur\n"+objError.toString());
		}
		
		return dateFile;
		
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
