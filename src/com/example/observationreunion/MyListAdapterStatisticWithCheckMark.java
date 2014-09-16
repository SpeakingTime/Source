package com.example.observationreunion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

public class MyListAdapterStatisticWithCheckMark  extends SimpleAdapter{
	private Context context;
	private LayoutInflater mInflater;
 
 
	public MyListAdapterStatisticWithCheckMark(Context context,
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
		
		convertView = mInflater.inflate(R.layout.affichageitem_statistic_with_checkmark, null);
		//récupére mon linearlayout
		//LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.item_statistic_with_checkmark);
		//affecte la position de l'item comme tag
		//ll.setTag(position);

		//récupére ma checkbox
		CheckBox cb = (CheckBox) convertView.findViewById(R.id.statistic_checkbox);
		//affecte la position de l'item comme tag
		cb.setTag(position);
	
		/*LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.item_ecoute);
		ll.setTag(position);*/
		
		
		HashMap<String, Object> map = (HashMap<String, Object>) getItem(position);
		if ((Boolean) map.get("isSelected") == true ){
			cb.setChecked(true);
		}
		else cb.setChecked(false);
		
		return super.getView(position, convertView, parent);
	}	
 
}
