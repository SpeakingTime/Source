package com.example.observationreunion;

import java.util.Comparator;

public class DateComparator implements Comparator<FileAndDate> {
	
	public int compare(FileAndDate fileanddate1, FileAndDate fileanddate2) {
		int result =  fileanddate2.getDate().compareTo(fileanddate1.getDate());
		if(result == 0){
			result = fileanddate2.getName().compareTo(fileanddate1.getName());
		}
		return result;
	}

}
