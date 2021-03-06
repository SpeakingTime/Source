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
import java.util.List;

import android.os.Environment;

public class StatisticLoadFiles {

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
				
				FileAndDate fileAndDate = new FileAndDate(allFiles[i].getName(), date);
				filesList.add(fileAndDate);
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
			System.out.println("Fichier non trouv�\n"+objError.toString());
		}
		catch (IOException objError) {
			System.out.println("Erreur\n"+objError.toString());
		}
		
		return dateFile;
		
	}
	
	
	public String uploadFile(String filename){
	
		StringBuffer objBuffer = null;
		
		try {
			FileInputStream objFile = new FileInputStream(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime" + File.separator +  filename);
			InputStreamReader objReader = new InputStreamReader(objFile);
			BufferedReader objBufferReader = new BufferedReader(objReader);
			objBuffer = new StringBuffer();
			String strLine;
			while ((strLine = objBufferReader.readLine()) != null) {
				objBuffer.append(strLine);
				objBuffer.append("\n");
			}
			objFile.close();
			
		}
		catch (FileNotFoundException objError) {
			System.out.println("Fichier non trouv�\n"+objError.toString());
		}
		catch (IOException objError) {
			System.out.println("Erreur\n"+objError.toString());
		}
		return objBuffer.toString();
	}

	public String readFile(String filename){
		
		StringBuffer objBuffer = null;
		boolean deb = false;
		
		try {
			FileInputStream objFile = new FileInputStream(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime" + File.separator +  filename);
			InputStreamReader objReader = new InputStreamReader(objFile);
			BufferedReader objBufferReader = new BufferedReader(objReader);
			objBuffer = new StringBuffer();
			String strLine;
			while ((strLine = objBufferReader.readLine()) != null) {
				
				if (strLine.indexOf("begin: meeting") != -1) { 
					deb = true;
				}
				else if (strLine.indexOf("end: meeting") != -1) { 
					deb = false;
				}
				else if (deb == true) {
					objBuffer.append(lineProcessing(strLine));
				}
				
			}
			objFile.close();
			
		}
		catch (FileNotFoundException objError) {
			System.out.println("Fichier non trouv�\n"+objError.toString());
		}
		catch (IOException objError) {
			System.out.println("Erreur\n"+objError.toString());
		}
		return objBuffer.toString();
		
	}

	public int getNbSec(String time){
		int hour = 0;
		int min = 0;
		int sec = 0;
		hour = Integer.valueOf(time.substring(0,2)) * 3600;
		min = Integer.valueOf(time.substring(3,5)) * 60;
		sec = Integer.valueOf(time.substring(6,8));
		return hour + min + sec;
	}
	
	public String lineProcessing(String strLine){
		StringBuilder result = new StringBuilder("");
		
		String time = strLine.substring(4, 12);
		int pos = strLine.indexOf(";", 13);
		String status = strLine.substring(pos+1, strLine.length()-1);
		
		if ((status.equalsIgnoreCase("listener")) || (status.equalsIgnoreCase("idle"))){
			result.append("OT | " + getNbSec(time) + "\n");
		}
		else if (status.equalsIgnoreCase("speaker")){
			result.append("SP | " + getNbSec(time) + "\n");
		}
		
		return result.toString();
	}
	
	public boolean getStatusLine(String strLine){
		//return true if SP status or false if OT status
		boolean st = false;
		int pos = strLine.indexOf(";", 13);
		String status = strLine.substring(pos+1, strLine.length()-1);
		if ((status.equalsIgnoreCase("listener")) || (status.equalsIgnoreCase("idle"))){
			st = false;
		}
		else if (status.equalsIgnoreCase("speaker")){
			st = true;
		}
		return st;
		
	}
	
	public int getValueLine(String strLine){
		int time = getNbSec(strLine.substring(4, 12));
		return time;
	}
	
	public List<String> getParticipantList(String filename){
		
		List<String> files = new ArrayList<String>();
		StringBuffer objBuffer = null;
		boolean deb = false;
		
		try {
			FileInputStream objFile = new FileInputStream(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime" + File.separator +  filename);
			InputStreamReader objReader = new InputStreamReader(objFile);
			BufferedReader objBufferReader = new BufferedReader(objReader);
			objBuffer = new StringBuffer();
			String strLine;
			
			while ((strLine = objBufferReader.readLine()) != null) {
				
				if (strLine.indexOf("begin: participants list") != -1) { 
					deb = true;
				}
				else if (strLine.indexOf("end: participants list") != -1) { 
					deb = false;
				}
				else if (deb == true) {
					files.add(strLine.substring(4, strLine.length()));
				}
				
			}
			objFile.close();
			
		}
		catch (FileNotFoundException objError) {
			System.out.println("Fichier non trouv�\n"+objError.toString());
		}
		catch (IOException objError) {
			System.out.println("Erreur\n"+objError.toString());
		}
		return files;
	}
	
	public String getParticipantsListValue(String filename){
		
		StringBuilder result = new StringBuilder("");
		List<String> participantsList = new ArrayList<String>();
		
		result.append("begin: participants list\n");
		
		participantsList = getParticipantList(filename);
		
		for (int i = 0; i < participantsList.size(); i++){
			result.append(participantsList.get(i) + "\n");
			result.append(getParticipantInfos(filename, participantsList.get(i)));
		}
	
		result.append("end: participants list\n");
		
		return result.toString();
	}
	
	public String getParticipantInfos(String filename, String partipant_name){
		
		StringBuffer objBuffer = null;
		boolean deb = false;
		boolean isInfos = false;
		boolean ot_status = false;
		int ot_value = 0;
		int sp_value = 0;
		int speakingtime = 0;
		
		try {
			FileInputStream objFile = new FileInputStream(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime" + File.separator +  filename);
			InputStreamReader objReader = new InputStreamReader(objFile);
			BufferedReader objBufferReader = new BufferedReader(objReader);
			objBuffer = new StringBuffer();
			String strLine;
			while ((strLine = objBufferReader.readLine()) != null) {
				
				if (strLine.indexOf("begin: meeting") != -1) { 
					deb = true;
				}
				else if (strLine.indexOf("end: meeting") != -1) { 
					deb = false;
				}
				else if (deb == true) {
					
					int pos = strLine.indexOf(";", 13);
					String name = strLine.substring(13, pos);
					System.out.println("'" + name + "'" + " - " +  "'" + partipant_name + "'");
					if (name.equalsIgnoreCase(partipant_name)){
						isInfos = true;
						if (!getStatusLine(strLine) && (!ot_status)){
						// si le status est OT et que OT n'est pas initialis�
							objBuffer.append(lineProcessing(strLine));
							ot_value = getValueLine(strLine);
							speakingtime = addTime(speakingtime, ot_value,sp_value);
							ot_status = true;
						}
						else if (getStatusLine(strLine)){
							objBuffer.append(lineProcessing(strLine));
							sp_value = getValueLine(strLine);
							ot_status = false;
						}
						
					}
				}
				
				if ((strLine.indexOf("total_time:") != -1) && (isInfos)){
					String TT = strLine.substring(12, strLine.length());					
					int tt_value = getNbSec(TT);
					objBuffer.append("TT | " + tt_value + "\n");
					if (!ot_status){				
						speakingtime = addTime(speakingtime, tt_value,sp_value);
					}
					objBuffer.append("speakingtime : " + speakingtime + "\n");
				}
				
			}
			objFile.close();
			
		}
		catch (FileNotFoundException objError) {
			System.out.println("Fichier non trouv�\n"+objError.toString());
		}
		catch (IOException objError) {
			System.out.println("Erreur\n"+objError.toString());
		}
		return objBuffer.toString();
		
	}
	
	public int addTime(int time, int ot, int sp){
		return time + ot - sp;
	}
	
	public String getParticipantsWithSpeakingTime(String filename){
		
		StringBuilder result = new StringBuilder("");
		List<String> participantsList = new ArrayList<String>();
		
		participantsList = getParticipantList(filename);
		
		for (int i = 0; i < participantsList.size(); i++){
			result.append(getParticipantsSpeakingTime(filename, participantsList.get(i)));
		}
	
		//System.out.println(result.toString());
		return result.toString();
	}
	
	public String getParticipantsSpeakingTime(String filename, String partipant_name){
	
		StringBuffer objBuffer = null;
		boolean deb = false;
		boolean isInfos = false;
		boolean ot_status = false;
		int ot_value = 0;
		int sp_value = 0;
		int speakingtime = 0;
		
		try {
			FileInputStream objFile = new FileInputStream(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime" + File.separator +  filename);
			InputStreamReader objReader = new InputStreamReader(objFile);
			BufferedReader objBufferReader = new BufferedReader(objReader);
			objBuffer = new StringBuffer();
			String strLine;
			while ((strLine = objBufferReader.readLine()) != null) {
				
				if (strLine.indexOf("begin: meeting") != -1) { 
					deb = true;
				}
				else if (strLine.indexOf("end: meeting") != -1) { 
					deb = false;
				}
				else if (deb == true) {
					
					int pos = strLine.indexOf(";", 13);
					String name = strLine.substring(13, pos);
					if (name.equalsIgnoreCase(partipant_name)){
						isInfos = true;
						
						
						
						if (!getStatusLine(strLine) && (!ot_status)){
						// si le status est OT et que OT n'est pas initialis�
							if (sp_value != 0) {
								ot_value = getValueLine(strLine);
								speakingtime = addTime(speakingtime, ot_value,sp_value);
							}
							ot_status = true;
						}
						else if (getStatusLine(strLine)){
							sp_value = getValueLine(strLine);
							ot_status = false;
						}
						
						
						
						
					}
				}
				
				if (strLine.indexOf("total_time:") != -1) {
					if (isInfos) {
						String TT = strLine.substring(12, strLine.length());					
						int tt_value = getNbSec(TT);
						if (!ot_status){				
							speakingtime = addTime(speakingtime, tt_value,sp_value);
						}
						objBuffer.append(partipant_name + ";" + speakingtime + ";\n");
					}
					else {
						objBuffer.append(partipant_name + ";0;\n");
					}
				}
				
			}
			objFile.close();
			
		}
		catch (FileNotFoundException objError) {
			System.out.println("Fichier non trouv�\n"+objError.toString());
		}
		catch (IOException objError) {
			System.out.println("Erreur\n"+objError.toString());
		}
		return objBuffer.toString();
		
	}
	
	
}
