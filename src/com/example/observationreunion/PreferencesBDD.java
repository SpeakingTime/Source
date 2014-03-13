package com.example.observationreunion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PreferencesBDD {

	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "speakingtime.db";
 
	private static final String TABLE_PREFERENCES = "ts_preferences";
	private static final String COL_ID_PREFERENCES = "_ID_preferences";
	private static final int NUM_COL_ID_PREFERENCES = 0;
	private static final String COL_HOST = "host";
	private static final int NUM_COL_HOST = 1;
	private static final String COL_USERNAME = "username";
	private static final int NUM_COL_USERNAME = 2;
	private static final String COL_TIME_INTERVAL_FOR_SAVE_FILE = "timeintervalforsavefile";
	private static final int NUM_COL_TIME_INTERVAL_FOR_SAVE_FILE = 3;
	
	private SQLiteDatabase bdd;
 
	private MyBaseSQLite myBaseSQLite;
 
	public PreferencesBDD(Context context){
		//On créer la BDD et sa table
		myBaseSQLite = new MyBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
	}
	
	public void open(){
		//on ouvre la BDD en écriture
		bdd = myBaseSQLite.getWritableDatabase();
	}
 
	public void close(){
		//on ferme l'accès à la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
 
	public long insertPreferences(Preferences preferences){
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associée à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(COL_HOST, preferences.gethost());
		values.put(COL_USERNAME, preferences.getusername());
		values.put(COL_TIME_INTERVAL_FOR_SAVE_FILE, preferences.gettimeintervalforsavefile());
		//on insère l'objet dans la BDD via le ContentValues
		return bdd.insert(TABLE_PREFERENCES, null, values);
	}
 
	public int updatePreferences(int _ID_preferences, Preferences preferences){
		//La mise à jour d'un group dans la BDD fonctionne plus ou moins comme une insertion
		//il faut simple préciser quel group on doit mettre à jour grâce à l'_ID_group
		ContentValues values = new ContentValues();
		values.put(COL_HOST, preferences.gethost());
		values.put(COL_USERNAME, preferences.getusername());
		values.put(COL_TIME_INTERVAL_FOR_SAVE_FILE, preferences.gettimeintervalforsavefile());
		return bdd.update(TABLE_PREFERENCES, values, COL_ID_PREFERENCES + " = " + _ID_preferences, null);
	}
 
	public int removePreferencesWith_ID_preferences(int _ID_preferences){
		//Suppression d'un group de la BDD grâce à l'ID
		return bdd.delete(TABLE_PREFERENCES, COL_ID_PREFERENCES + " = " + _ID_preferences, null);
	}
	
	public Cursor getCursor(){		
		Cursor c = bdd.query(TABLE_PREFERENCES,
				new String[] {COL_ID_PREFERENCES, COL_HOST, COL_USERNAME, COL_TIME_INTERVAL_FOR_SAVE_FILE}, null, null, null, null, null);		
		return c;
	}
	
}
