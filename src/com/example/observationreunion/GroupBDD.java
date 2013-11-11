package com.example.observationreunion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GroupBDD {

	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "speakingtime.db";
 
	private static final String TABLE_GROUP = "ts_group";
	private static final String COL_ID_GROUP = "_ID_group";
	private static final int NUM_COL_ID_GROUP = 0;
	private static final String COL_GROUP_NAME = "group_name";
	private static final int NUM_COL_GROUP_NAME = 1;
	private static final String COL_ID_CONTACT = "_ID_contact";
	private static final int NUM_COL_ID_CONTACT = 2;
 
	private SQLiteDatabase bdd;
 
	private MyBaseGroupSQLite myBaseGroupSQLite;
 
	public GroupBDD(Context context){
		//On créer la BDD et sa table
		myBaseGroupSQLite = new MyBaseGroupSQLite(context, NOM_BDD, null, VERSION_BDD);
	}
 
	public void open(){
		//on ouvre la BDD en écriture
		bdd = myBaseGroupSQLite.getWritableDatabase();
	}
 
	public void close(){
		//on ferme l'accès à la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
 
	public long insertGroup(Group group){
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associée à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(COL_GROUP_NAME, group.getgroup_name());
		values.put(COL_ID_CONTACT, group.get_ID_contact());
		//on insère l'objet dans la BDD via le ContentValues
		return bdd.insert(TABLE_GROUP, null, values);
	}
 
	public int updateGroup(int _ID_group, Group group){
		//La mise à jour d'un group dans la BDD fonctionne plus ou moins comme une insertion
		//il faut simple préciser quel group on doit mettre à jour grâce à l'_ID_group
		ContentValues values = new ContentValues();
		values.put(COL_GROUP_NAME, group.getgroup_name());
		values.put(COL_ID_CONTACT, group.get_ID_contact());
		return bdd.update(TABLE_GROUP, values, COL_ID_GROUP + " = " + _ID_group, null);
	}
 
	public int removeGroupWith_ID_group(int _ID_group){
		//Suppression d'un group de la BDD grâce à l'ID
		return bdd.delete(TABLE_GROUP, COL_ID_GROUP + " = " + _ID_group, null);
	}
	
	public int removeGroupWithgroup_name(String group_name){
		//Suppression d'un group de la BDD grâce au nom du groupe
		return bdd.delete(TABLE_GROUP, COL_GROUP_NAME + " LIKE \"" + group_name + "\"", null);
	}
 
	public Group getGroupWithgroup_name(String group_name){
		//Récupère dans un Cursor les valeur correspondant à un group contenu dans la BDD (ici on sélectionne le group grâce à son group_name)
		Cursor c = bdd.query(TABLE_GROUP, new String[] {COL_ID_GROUP, COL_GROUP_NAME, COL_ID_CONTACT}, COL_GROUP_NAME + " LIKE \"" + group_name +"\"", null, null, null, null);
		return cursorToGroup(c);
	}
 
	//Cette méthode permet de convertir un cursor en un livre
	private Group cursorToGroup(Cursor c){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0)
			return null;
 
		//Sinon on se place sur le premier élément
		c.moveToFirst();
		//On crée un group
		Group group = new Group();
		//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
		group.set_ID_group(c.getInt(NUM_COL_ID_GROUP));
		group.setgroup_name(c.getString(NUM_COL_GROUP_NAME));
		group.set_ID_contact(c.getString(NUM_COL_ID_CONTACT));
		//On ferme le cursor
		c.close();
 
		//On retourne le group
		return group;
	}
		
	public Cursor getCursor(){		
		Cursor c = bdd.query(TABLE_GROUP, new String[] {COL_ID_GROUP, COL_GROUP_NAME, COL_ID_CONTACT}, null, null, null, null, null);		
		return c;
	}
}
