package com.example.observationreunion;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class CreateGroupActivity extends Activity{

	ListView lvContact;
	ArrayList<HashMap<String, Object>> listItemContact = new ArrayList<HashMap<String, Object>>();
	SimpleAdapter mSchedule = null;
	
	List<String> listSelectedContact = new ArrayList<String>();
		
	private static ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_group);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        final EditText editTextGroupName = (EditText) findViewById(R.id.editTextGroupName);
                
    	Button buttonCreateAndSaveToLocalDatabase = (Button) findViewById(R.id.buttonCreateAndSaveToLocalDatabase);
    	buttonCreateAndSaveToLocalDatabase.setOnClickListener( 
    			new Button.OnClickListener(){
    				
    				@Override
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					
    					if (editTextGroupName.getText().length() == 0){
    						/*AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateGroupActivity.this);
    						alertDialog.setTitle("Warning");
    						alertDialog.setMessage("You must give a valid group name");
    						alertDialog.show();*/
    						ModalDialog modalDialog = new ModalDialog();
    						modalDialog.showWarningDialog(CreateGroupActivity.this, "You must give a valid group name.");
    					}
    					else {
	    					dialog = ProgressDialog.show(CreateGroupActivity.this, "Send Data", "sending");
	    					//if (CreateAndSendGroupToDatabase()){
	    					if (CreateAndSendGroupToMyBaseGroupSQLite()){
	    						Log.i("SEND", "- DATA SEND -");
	    						/*AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateGroupActivity.this);
	    						alertDialog.setTitle("Saving Group :");
	    						alertDialog.setMessage("DONE !");
	    						alertDialog.show();*/
	    						ModalDialog modalDialog = new ModalDialog();
	    						modalDialog.showWarningDialog(CreateGroupActivity.this, "Saving group : " + editTextGroupName.getText());
	    						
	    					}
	    					else{
	    						Log.i("SEND", "- DATA NOT SEND - BIG ERROR !!! -");
	    					}
	    					dialog.dismiss();
	    					finish();
    					}
    				}
    			}
    	);
		
		lvContact = new MyListView(this.getApplicationContext());
        lvContact = (MyListView) findViewById(R.id.listViewContactsGroup_c);
        
        InitContact(savedInstanceState, this);
	
	}
	
    public void InitContact(Bundle savedInstanceState, Activity activity){
    	
    	//listSelectedContact = new ArrayList<String>();
    	Bundle b = getIntent().getExtras();
    	String s = b.getString("selectedContact");
    	Scanner scanner = new Scanner(s);
		while (scanner.hasNextLine()) {
			String id_contact = scanner.nextLine();
			System.out.println("id_contact : " + id_contact);
			listSelectedContact.add(id_contact);
		}			
    	
    	Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[] { ContactsContract.Contacts._ID,
                                        ContactsContract.Contacts.DISPLAY_NAME/*,
                                        ContactsContract.Contacts.Data.DATA1*/};
    	
        // works in Honeycomb
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;

        CursorLoader cursorLoader = new CursorLoader(
                activity, 
                uri, 
                projection, 
                selection, 
                selectionArgs, 
                sortOrder);

        Cursor mCursor = cursorLoader.loadInBackground();
        
    	mCursor.moveToFirst();
    	
    	while (mCursor.isAfterLast() == false){
    		
    		if (listSelectedContact.indexOf(mCursor.getString(0)) != -1){
    			
    			HashMap<String, Object> map = new HashMap<String, Object>();
   		    	Bitmap bitmap = getPhoto(mCursor.getInt(0));
	    		bitmap = Bitmap.createScaledBitmap (bitmap, 64, 64, true);
	    		map.put("id_phone_contact", mCursor.getString(0));
	    		
	    		/* AlertDialog.Builder alertdialog= new AlertDialog.Builder(activity);
	    		alertdialog.setTitle("ContactsContract.Contacts._ID");
	    		alertdialog.setMessage(mCursor.getString(0).toString());
	    		alertdialog.show(); */
	    		
	    		map.put("img", bitmap);
	            map.put("display_name", mCursor.getString(1));
	            
	            Cursor orgCur = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
	            									  new String[] {ContactsContract.CommonDataKinds.Organization.COMPANY,
	            													ContactsContract.CommonDataKinds.Organization.TITLE}, 
	            									  ContactsContract.Data.CONTACT_ID + " = " + mCursor.getString(0)
	            									  	+ " AND ContactsContract.Data.MIMETYPE = '"
	            									  	+ ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE
	            									  	+ "'",
	            									  null,
	            									  null);
	            String company = null;
	            String title = null;
	            if (orgCur.moveToFirst()) {
	            	company = orgCur.getString(0);
	            	title = orgCur.getString(1);
	            	
	            	Log.i("company", "company : " + orgCur.getString(0));
	            	Log.i("title", "title : " + orgCur.getString(1));
	            		            	
	            }
	            orgCur.close();
	            
	            if (company != null){
	            	if (title != null){
		            	map.put("company_and_title", company + " (" + title + ")" );
		            }
		            else{
		            	map.put("company_and_title", company);
		            }	
	            }
	            else {
	            	map.put("company_and_title", "");
	            }
	            	            
	            map.put("chronometre", new ChronoData(SystemClock.elapsedRealtime(), 0, "modeEcoute"));
	            listItemContact.add(map); 
	        }
    		
    		mCursor.moveToNext();
    	}
    	mCursor.close();
    	
        mSchedule = new SimpleAdapter (this.getBaseContext(), listItemContact, R.layout.affichageitem,
                new String[] { "img", "display_name", "company_and_title", "chronometre"}, 
                new int[] { R.id.IMG, R.id.DISPLAY_NAME, R.id.COMPANY_AND_TITLE, R.id.CHRONOMETRE});
        
        mSchedule.setViewBinder(new MyViewBinder()); // VOICI LA CLE!!!!
  	    
        //On attribut à notre listView l'adapter que l'on vient de créer
        lvContact.setAdapter(mSchedule); 
    }
    
    public Bitmap getPhoto(long userId ) {
        Uri photoUri = null;
        ContentResolver cr = this.getContentResolver();
        photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, userId);
        Bitmap defaultPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.logo_contact);
        if (photoUri != null) {
            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(
                    cr, photoUri);
            if (input != null) {
                return BitmapFactory.decodeStream(input);
            }
        } else {

            return defaultPhoto;
        }

        return defaultPhoto;
    }
		
	/*public Boolean CreateAndSendGroupToDatabase(){
		
		Integer _idgroupmax = 0;
		
		EditText editTextGroupName = (EditText) findViewById(R.id.editTextGroupName);
		String SGroupName = editTextGroupName.getText().toString() ;
		
		//récupération de l'_idgroup max
		try {
			InputStream is = null;
			HttpClient httpclient_idgroupmax = new DefaultHttpClient();
			HttpPost httppost_idgroupmax = new HttpPost("http://213.186.33.3/~datamysq/timespeaking/last_id_group.php");
 		    HttpResponse response_idgroupmax = httpclient_idgroupmax.execute(httppost_idgroupmax);//httpclient_idmax.execute(httppost_idmax);
			HttpEntity entity_idgroupmax = response_idgroupmax.getEntity();
			is = entity_idgroupmax.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			
			_idgroupmax = Integer.valueOf(reader.readLine().toString().trim());
			
			Log.i("_idgroupmax", "max _id_group : " + String.valueOf(_idgroupmax));
			is.close();
		}
		catch (Exception e) {
			Log.i("_idgroupmax Execption", "Erreur lors de la création de _idgroupmax");
			return false;
		}
		
		_idgroupmax = _idgroupmax +1;
		
		for (int i=0; i<listSelectedContact.size(); i++){
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    		
 		    nameValuePairs.add(new BasicNameValuePair("_ID_group", String.valueOf(_idgroupmax)));
 		    nameValuePairs.add(new BasicNameValuePair("group_name", SGroupName));
 		    nameValuePairs.add(new BasicNameValuePair("_ID_contact", listSelectedContact.get(i).toString()));
 		    
 		    try {
	 		    HttpClient httpclient = new DefaultHttpClient();
			    HttpPost httppost = new HttpPost("http://213.186.33.3/~datamysq/timespeaking/add_group.php");
			    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));                                  
			    Log.i("Connexion", "Wait...");
			    HttpResponse response = httpclient.execute(httppost);
			    Log.i("Connexion", "Done");
 		    }
 		    catch (Exception e){
 		    	Log.i("send_data_exception", "send_data_exception : " + e.toString());
 		    	return false;
 		    }
	 		    
		}
				
		return true;
	}*/
	
    public Boolean CreateAndSendGroupToMyBaseGroupSQLite(){
    
    	EditText editTextGroupName = (EditText) findViewById(R.id.editTextGroupName);
    	String SGroupName = editTextGroupName.getText().toString() ;
    	
    	try {
	    	//Création d'une instance de ma classe GroupBDD
	    	GroupBDD groupBdd = new GroupBDD(this);
	    	
	    	//for (int i=0; i<listSelectedContact.size(); i++){
	    	for (int i=0; i<listItemContact.size(); i++){
	    	
	    		HashMap map = new HashMap<String,Object>();
	    		map = listItemContact.get(i);
	    		
		    	//Création d'un group
		    	//Group group = new Group(SGroupName, listSelectedContact.get(i).toString());
	    		Group group = new Group(SGroupName, map.get("id_phone_contact").toString());
	    		
		    	//On ouvre la base de données pour écrire dedans
		    	groupBdd.open();
		    	//On insère le livre que l'on vient de créer
		    	groupBdd.insertGroup(group);
		    	
		    	groupBdd.close();
	    	
	    	}
	    	
	    	return true;
    	}
    	catch (Exception e){
    		return false;
    	}
    	
	
    }
    
}