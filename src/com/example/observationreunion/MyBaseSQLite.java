package com.example.observationreunion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MyBaseSQLite extends SQLiteOpenHelper{
	
	private static final String TABLE_GROUP = "ts_group";
	private static final String COL_ID_GROUP = "_ID_group";
	private static final String COL_GROUP_NAME = "group_name";
	private static final String COL_ID_CONTACT = "_ID_contact";
 
	private static final String CREATE_BDD_GROUP = "CREATE TABLE IF NOT EXISTS " + TABLE_GROUP + " ("
	+ COL_ID_GROUP + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_GROUP_NAME + " TEXT NOT NULL, "
	+ COL_ID_CONTACT + " TEXT NOT NULL);";
	
	private static final String TABLE_PREFERENCES = "ts_preferences";
	private static final String COL_ID_PREFERENCES  = "_ID_preferences";
	private static final String COL_HOST = "host";
	private static final String COL_USERNAME = "username";
	private static final String COL_PASSWORD = "password";
	
	private static final String CREATE_BDD_PREFERENCES = "CREATE TABLE IF NOT EXISTS " + TABLE_PREFERENCES + " ("
			+ COL_ID_PREFERENCES + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_HOST + " TEXT NOT NULL, "
			+ COL_USERNAME + " TEXT NOT NULL, " + COL_PASSWORD + " TEXT NOT NULL);";
 
	public MyBaseSQLite(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		//on crée la table à partir de la requète écrite dans la variable CREATE_BDD
		db.execSQL(CREATE_BDD_GROUP);
		db.execSQL(CREATE_BDD_PREFERENCES);
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//On peut faire ce que l'on veut ici moi j'ai décidé de supprimer la table et de la recréer
		//comme lorsque je change la version les id repartent de 0
		db.execSQL("DROP TABLE " + TABLE_GROUP + ";");
		db.execSQL("DROP TABLE " + TABLE_PREFERENCES + ";");
		onCreate(db);
	}
}
