package com.example.observationreunion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

public class MyListAdapterCheckmarkInactif extends SimpleAdapter{
	private Context context;
	private LayoutInflater mInflater;
 
 
	public MyListAdapterCheckmarkInactif(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.context = context;
		mInflater = LayoutInflater.from(context);
 
	}
 
	@Override
	public Object getItem(int position) {
		return super.getItem(position);
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		convertView = mInflater.inflate(R.layout.affichageitem_with_chekmark_inactif, null);
	
		
		/*LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.item);
		ll.setTag(position);*/
		
		/*ImageView iv = (ImageView) convertView.findViewById(R.id.IMG);
		iv.setTag(position);*/
		
		
		  
		//récupére ma checkbox
		CheckBox cb = (CheckBox) convertView.findViewById(R.id.selection_chekbox_inactif);
		//affecte la position de l'item comme tag
		cb.setTag(position);
		
		HashMap<String, Object> map = (HashMap<String, Object>) getItem(position);
		if ((Boolean) map.get("isSelected") == true ){
			//Log.i("", map.get("tag").toString());
			cb.setChecked(true);
		}
		else cb.setChecked(false);
		
		//return super.getView(position, convertView, parent);
		
		View v = super.getView(position, convertView, parent);
		        
		//stopChrono(v);// starting for each row
		        
		return v;
	}	
	
	private void  stopChrono(View v){
		
	    //Here we need to know structure of layout to get our element 
	    LinearLayout vwParentRow = (LinearLayout)v;
	    LinearLayout ChildTop = (LinearLayout) vwParentRow.getChildAt(1);
	
	    //Now, we get chronometer and could work with it as we wont
	    Chronometer chrono = (Chronometer) ChildTop.getChildAt(2);
	    //chrono.setBase(SystemClock.elapsedRealtime());
	    chrono.setTextColor(Color.WHITE);
	    chrono.stop();
	
	 }
 
}

