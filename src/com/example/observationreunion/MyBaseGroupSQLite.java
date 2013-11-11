package com.example.observationreunion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MyBaseGroupSQLite extends SQLiteOpenHelper{
	
	private static final String TABLE_GROUP = "ts_group";
	private static final String COL_ID_GROUP = "_ID_group";
	private static final String COL_GROUP_NAME = "group_name";
	private static final String COL_ID_CONTACT = "_ID_contact";
 
	private static final String CREATE_BDD = "CREATE TABLE IF NOT EXISTS " + TABLE_GROUP + " ("
	+ COL_ID_GROUP + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_GROUP_NAME + " TEXT NOT NULL, "
	+ COL_ID_CONTACT + " TEXT NOT NULL);";
 
	public MyBaseGroupSQLite(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		//on crée la table à partir de la requète écrite dans la variable CREATE_BDD
		db.execSQL(CREATE_BDD);
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//On peut faire ce que l'on veut ici moi j'ai décidé de supprimer la table et de la recréer
		//comme lorsque je change la version les id repartent de 0
		db.execSQL("DROP TABLE " + TABLE_GROUP + ";");
		onCreate(db);
	}
}