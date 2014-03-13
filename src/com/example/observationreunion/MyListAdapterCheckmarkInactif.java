package com.example.observationreunion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MyListAdapterCheckmarkInactif extends SimpleAdapter implements View.OnTouchListener{
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
	
	public interface IListAdapterCheckmarkInactifCallback{
		void IdleToListening(int position);
		void IdleToListening();
		void IdleToSpeaking(int position);
		void IdleToSpeaking();
		boolean islVDataInactifCheck();
	}
	
	private IListAdapterCheckmarkInactifCallback callerActivity;
	
	public MyListAdapterCheckmarkInactif(Activity activity, Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.context = context;
		mInflater = LayoutInflater.from(context);
 
		callerActivity = (IListAdapterCheckmarkInactifCallback)activity;
		
	}
 
	@Override
	public Object getItem(int position) {
		return super.getItem(position);
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.affichageitem_with_chekmark_inactif, null);
			holder = new ViewHolder();
            holder.txtDISPLAY_NAME = (TextView) convertView.findViewById(R.id.DISPLAY_NAME_inactif);
            holder.txtCOMPANY_AND_TITLE = (TextView) convertView.findViewById(R.id.COMPANY_AND_TITLE_inactif);
            holder.txtCHRONOMETRE = (TextView) convertView.findViewById(R.id.CHRONOMETRE_inactif);
            holder.imageView = (ImageView) convertView.findViewById(R.id.IMG_inactif);
            //holder.position = position;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
		
		holder.position = position;
		
		convertView.setOnTouchListener(this);
		
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
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
	    
		long width = v.getWidth();
		
		
		//MainActivity main = new MainActivity();
		
		if (event.getAction() == MotionEvent.ACTION_UP) {
    		
    		if ((event.getRawX() > width) && (event.getRawX() < width*2)){
    			if (callerActivity.islVDataInactifCheck()){
    				try{
    					callerActivity.IdleToSpeaking();
    				}
    				catch (Exception e){
    					 Log.i("Exception", e.toString());   					
    				}
    			}
    			else{
    				try{
    					callerActivity.IdleToSpeaking(position);
    				}
    				catch (Exception e){
    					 Log.i("Exception", e.toString());   					
    				}
    			}
    		}
    		else if ((event.getRawX() < width)){
    			
    			//Log.i("width : ", String.valueOf(width));
				//Log.i("x : ", String.valueOf(event.getX()));
    			
    			if (callerActivity.islVDataInactifCheck()){
    				try{
    					callerActivity.IdleToListening();
    				}
    				catch (Exception e){
    					 Log.i("Exception", e.toString());   					
    				}
    				
    			}
    			else{
    				try{
    					callerActivity.IdleToListening(position);
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

