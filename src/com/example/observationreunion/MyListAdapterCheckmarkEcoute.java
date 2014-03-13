package com.example.observationreunion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.observationreunion.MyListView.OnItemMoveTapLister;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MyListAdapterCheckmarkEcoute extends SimpleAdapter implements View.OnTouchListener/*View.OnClickListener*/{
	private Context context;
	private LayoutInflater mInflater;
	private int position = -1;
	
	private class ViewHolder {
        ImageView imageView;
        TextView txtDISPLAY_NAME;
        TextView txtCOMPANY_AND_TITLE;
        TextView txtCHRONOMETRE;
        Integer position;
    }
	
	public interface IListAdapterCheckmarkEcouteCallback{
		void ListeningToSpeaking(int position);
		void ListeningToSpeaking();
		void ListeningToIdle(int position);
		void ListeningToIdle();
		boolean islVDataEcouteCheck();
	}
	
	private IListAdapterCheckmarkEcouteCallback callerActivity;
	
	public MyListAdapterCheckmarkEcoute(Activity activity, Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.context = context;
		mInflater = LayoutInflater.from(context);
		
		callerActivity = (IListAdapterCheckmarkEcouteCallback)activity;
 
	}
 
	@Override
	public Object getItem(int position) {
		return super.getItem(position);
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if (convertView == null) {
		
			convertView = mInflater.inflate(R.layout.affichageitem_with_chekmark_ecoute, null);
			holder = new ViewHolder();
            holder.txtDISPLAY_NAME = (TextView) convertView.findViewById(R.id.DISPLAY_NAME_ecoute);
            holder.txtCOMPANY_AND_TITLE = (TextView) convertView.findViewById(R.id.COMPANY_AND_TITLE_ecoute);
            holder.txtCHRONOMETRE = (TextView) convertView.findViewById(R.id.CHRONOMETRE_ecoute);
            holder.imageView = (ImageView) convertView.findViewById(R.id.IMG_ecoute);
            //holder.position = position;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
		
		holder.position = position;
		
		convertView.setOnTouchListener(this);
		//convertView.setOnClickListener(this);
		
		
		//récupére ma checkbox
		CheckBox cb = (CheckBox) convertView.findViewById(R.id.selection_chekbox_ecoute);
		//affecte la position de l'item comme tag
		cb.setTag(position);
	
		/*LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.item_ecoute);
		ll.setTag(position);*/
		
		
		HashMap<String, Object> map = (HashMap<String, Object>) getItem(position);
		if ((Boolean) map.get("isSelected") == true ){
			cb.setChecked(true);
		}
		else cb.setChecked(false);
		
		/*AlertDialog.Builder alertDialog = new AlertDialog.Builder(context.());
		alertDialog.setTitle("Warning");
		alertDialog.setMessage("itemEcoutePosition : " + position);
		alertDialog.show();*/
		
		//Log.i("position : ", String.valueOf(position) );
		
		
		View v = super.getView(position, convertView, parent);
        
		//stopChrono(v);// starting for each row
				
		return v;
		
	}	
	
	/*public void onClick(View v)
    {
		ViewHolder holder;
		holder = (ViewHolder) v.getTag();
    	
    	MainActivity main = new MainActivity();
    	main.position = holder.position;
    	Log.i("context", "Position : " + main.position);
    }*/
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
	    
		long width = v.getWidth();
		
		
		//MainActivity main = new MainActivity();
		
		if (event.getAction() == MotionEvent.ACTION_UP) {
    		
    		if ((event.getRawX() > width*2) && (event.getRawX() < width*3)){
    			if (callerActivity.islVDataEcouteCheck()){
    				try{
    					callerActivity.ListeningToIdle();
    				}
    				catch (Exception e){
    					 Log.i("Exception", e.toString());   					
    				}
    			}
    			else{
    				try{
    					callerActivity.ListeningToIdle(position);
    				}
    				catch (Exception e){
    					 Log.i("Exception", e.toString());   					
    				}
    			}
    		}
    		else if ((event.getRawX() > width) && (event.getRawX() < width*2)){
    			if (callerActivity.islVDataEcouteCheck()){
    				try{ 
    					callerActivity.ListeningToSpeaking();
    				}
    				catch (Exception e){
    					Log.i("Exception", e.toString());   					
    				}
    			}
    			else{
    				try {
    					callerActivity.ListeningToSpeaking(position);
    				}
    				catch (Exception e){
    					 Log.i("Exception", e.toString());   					
    				}
    			}
    		}
    		
    	}
    	else if(event.getAction()==MotionEvent.ACTION_DOWN){
	    	ViewHolder holder;
			holder = (ViewHolder) v.getTag();
	    	position = holder.position;
			return true;
	    }
	    return false;
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
