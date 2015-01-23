package com.example.observationreunion;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MyListView extends ListView {

	
	/*private final static int MOVE_TAP = 3; 
	private final static int DOUBLE_TAP = 2;
	private final static int SINGLE_TAP = 1;
	private final static int DELAY = ViewConfiguration.getDoubleTapTimeout();
	private boolean mTookFirstEvent=false;
    private int mPositionHolder=-1;
    private int mPosition=-1;
    private OnItemMoveTapLister mOnMoveTapListener = null;
    private OnItemDoubleTapLister mOnDoubleTapListener = null;
    private AdapterView<?> mParent = null;
    private View mView = null;
    private long mId= 12315;
    private Message mMessage = null;
    private static String TAG2 = "MoveTapListView";
    private static String TAG = "DoubleTapListView";
    private MotionEvent mEvent =null;
    private Handler mHandler = new Handler(){
        
        @Override
        public void handleMessage(Message msg)
        {
        	super.handleMessage(msg);
       
        	switch(msg.what){
        		case SINGLE_TAP:
        			//Log.i(TAG, "Single tap entry");
        			mOnDoubleTapListener.OnSingleTap(mParent, mView, mPosition, mId);
        			break;
        		case DOUBLE_TAP:
        			//Log.i(TAG, "Double tap entry");
        			mOnDoubleTapListener.OnDoubleTap(mParent, mView, mPosition, mId);
        			break;
        		case MOVE_TAP:
        	        //Log.i(TAG2, "Move tap entry");
        	        mOnMoveTapListener.OnMoveTap(mView, mEvent);
       				break;	
        	}
        }
    };*/
	
	public MyListView(Context context) {
		super(context);
		//removeSelector();
	}
	
	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//removeSelector();	
	}
	
	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		//removeSelector();	
	}
	
	/*public void setOnTouchListener (OnItemMoveTapLister listener){
		mOnMoveTapListener = listener;
		
		if(mOnMoveTapListener==null)
			throw new IllegalArgumentException("OnMoveTapListener cannot be null");
		else{
		
			setOnTouchListener(new OnTouchListener() {
				float x,y;
	
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					mView = v;
					mEvent = event;	
										
					mMessage = mMessage == null? new Message() : mHandler.obtainMessage();
					
					try {
						mHandler.removeMessages(SINGLE_TAP);
						mHandler.removeMessages(DOUBLE_TAP);
					}
					catch (Exception e){
						System.out.print(e.toString());
					}
					
					mMessage.what = MOVE_TAP;
					mHandler.sendMessageAtFrontOfQueue(mMessage);
					
					return false;			
				}
		    });  
		}


	
	}*/
	
	/*public void setOnItemDoubleClickListener(OnItemDoubleTapLister listener ){
		mOnDoubleTapListener = listener;
		if(mOnDoubleTapListener==null)
			throw new IllegalArgumentException("OnItemDoubleTapListener cannot be null");
		else{
			setOnItemClickListener(new OnItemClickListener(){
			
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				mParent = parent;
				mView = view;
				mPosition = position;
				mId=id;
				if(!mTookFirstEvent) {
					mPositionHolder=position;
					mTookFirstEvent=true;
					mMessage = mMessage == null? new Message() : mHandler.obtainMessage();
					mHandler.removeMessages(SINGLE_TAP);
					mMessage.what = SINGLE_TAP;
					mHandler.sendMessageDelayed(mMessage, DELAY);
				}
				else{
					if(mPositionHolder == position){
						mHandler.removeMessages(SINGLE_TAP);
						mPosition = position;
						mMessage = mHandler.obtainMessage();
						mMessage.what=DOUBLE_TAP;
						mHandler.sendMessageAtFrontOfQueue(mMessage);
					
						mTookFirstEvent=false;
						
					}
					else{
						mMessage = mHandler.obtainMessage();
						mHandler.removeMessages(SINGLE_TAP);
						mTookFirstEvent=true;
						mMessage.what = SINGLE_TAP;
						mPositionHolder = position;
						mHandler.sendMessageDelayed(mMessage, DELAY);
					}
				}
						
			}});
		}
	
	}*/
	
	/*public void removeSelector()
	{
		setSelector(android.R.color.transparent); // optional
		//TODO solution for double tap selector needed.
	
	}
	
	public interface OnItemDoubleTapLister{
		public void OnDoubleTap(AdapterView<?> parent, View view, int position, long id);
		public void OnSingleTap(AdapterView<?> parent, View view, int position, long id);
	} 


	public interface OnItemMoveTapLister{
		public void OnMoveTap(View view, MotionEvent event);
	}*/
	
}
