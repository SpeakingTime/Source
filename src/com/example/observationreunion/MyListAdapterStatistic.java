package com.example.observationreunion;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

public class MyListAdapterStatistic extends SimpleAdapter{
	private Context context;
	private LayoutInflater mInflater;
 
 
	public MyListAdapterStatistic(Context context,
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
		
		convertView = mInflater.inflate(R.layout.affichageitem_statistic, null);
		//récupére mon linearlayout
		LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.item_statistic);
		//affecte la position de l'item comme tag
		ll.setTag(position);

		
		return super.getView(position, convertView, parent);
	}	
 
}
