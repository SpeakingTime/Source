package com.example.observationreunion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

public class MyListAdapter extends SimpleAdapter{
	private Context context;
	private LayoutInflater mInflater;
 
 
	public MyListAdapter(Context context,
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
		
		convertView = mInflater.inflate(R.layout.affichageitem_checkbox, null);
		//récupére ma checkbox
		CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkbox);
		//affecte la position de l'item comme tag
		cb.setTag(position);
		
		HashMap<String, Object> map = (HashMap<String, Object>) getItem(position);
		if ((Boolean) map.get("isSelected") == true ){
			//Log.i("", map.get("tag").toString());
			cb.setChecked(true);
		}
		else cb.setChecked(false);
		
		/*ImageView imgView = (ImageView) convertView.findViewById(R.id.photo);
		imgView.setImageBitmap((Bitmap) map.get("photo"));*/ 
		
		return super.getView(position, convertView, parent);
	}	
 
}
