package com.example.observationreunion;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SimpleAdapter.ViewBinder;

public class MyViewBinder implements ViewBinder{

	
	public boolean setViewValue(View view, Object data,
			String textRepresentation) {
		// TODO Auto-generated method stub
		
		if( (view instanceof ImageView) & (data instanceof Bitmap) ) {
			final ImageView iv = (ImageView) view;
			Bitmap bm = (Bitmap) data;
			iv.setImageBitmap(bm); 
			return true;
		}
		/*else if( (view instanceof CheckBox) & (data instanceof CheckBox) ) {
			if (((CheckBox) view).isChecked()) {
				((CheckBox) view).setChecked(false);
	        } 
			return true;
		}*/
		else if( (view instanceof Chronometer) & (data instanceof ChronoData) ) {
			
			Chronometer ch = (Chronometer) view;
			ChronoData chdata = (ChronoData) data;	
			
			if (chdata.GetStatement() == "modeEcoute"){
				ch.setBase(SystemClock.elapsedRealtime() + chdata.GetWhenStop());
				ch.stop();
				
			}else if (chdata.GetStatement() == "modeParole"){
				chdata.SetWhenStop(chdata.GetBaseTime() - SystemClock.elapsedRealtime());
				ch.setBase(SystemClock.elapsedRealtime() + chdata.GetWhenStop());
				
			}else if (chdata.GetStatement() == "start"){
				chdata.SetNeverUsed(false);
				chdata.SetStatement("modeParole");
				chdata.SetBaseTime(SystemClock.elapsedRealtime() + chdata.GetWhenStop());
				ch.setBase(SystemClock.elapsedRealtime() + chdata.GetWhenStop());
				ch.setTextColor(Color.RED);
				ch.start();
				
			}else if (chdata.GetStatement() == "stop"){
				chdata.SetStatement("modeEcoute");
				chdata.SetWhenStop(chdata.GetBaseTime() - SystemClock.elapsedRealtime());
				ch.setBase(SystemClock.elapsedRealtime() + chdata.GetWhenStop());
				ch.setTextColor(Color.WHITE);
				ch.stop();
				
			}
			else if (chdata.GetStatement() == "no_change"){
				ch.setBase(SystemClock.elapsedRealtime() + chdata.GetWhenStop());
			}
		
			return true;	
		}
			return false;
	}

	
}