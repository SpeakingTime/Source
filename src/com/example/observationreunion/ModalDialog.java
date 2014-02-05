package com.example.observationreunion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class ModalDialog {
	
	private boolean mChoice = false;
	private boolean mQuitModal = false;
	private String mEditText = "";
	private boolean chkSSH_Status = false;
	
	private Method mMsgQueueNextMethod = null;
	private Field mMsgTargetFiled = null;
	
	public ModalDialog() {

	}
	
	public void showAlertDialog(Context context, String info) {
		if (!prepareModal()) {
			return;
		}
		
		//reset choice
		mChoice = false;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(info);
		builder.setCancelable(false);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				ModalDialog.this.mQuitModal = true;
				ModalDialog.this.mChoice = true;
				dialog.dismiss();
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
		
		//run in modal mode
		doModal();
	}
	
	public void showWarningDialog(Context context, String info) {
		if (!prepareModal()) {
			return;
		}
		
		//reset choice
		mChoice = false;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("WARNING");
		builder.setMessage(info);
		builder.setCancelable(false);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				ModalDialog.this.mQuitModal = true;
				ModalDialog.this.mChoice = true;
				dialog.dismiss();
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
		
		//run in modal mode
		doModal();
	}
	
	public boolean showConfirmDialog(Context context, String info) {
		if (!prepareModal()) {
			return false;
		}
		
		// reset choice
		mChoice = false;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(info);
		builder.setCancelable(false);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				ModalDialog.this.mQuitModal = true;
				ModalDialog.this.mChoice = true;
				dialog.dismiss();
			}
		});
		
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				ModalDialog.this.mQuitModal = true;
				ModalDialog.this.mChoice = false;
				dialog.cancel();
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
		
		doModal();
		return mChoice;
	}
	
	public String showEditTextDialog(Context context, String info) {
		
		if (!prepareModal()) {
			return "Cancel";
		}
		
		mEditText = "Cancel";
		
		LayoutInflater factory = LayoutInflater.from(context);
		final View alertDialogView = factory.inflate(R.layout.alertdialogperso, null);

				
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(info);
		
		builder.setView(alertDialogView);
		
		builder.setCancelable(false);
		
		
		
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				ModalDialog.this.mQuitModal = true;
				
				final EditText editTextAlertDialogPerso = (EditText) alertDialogView.findViewById(R.id.EditTextAlertDialogPerso);
				
				ModalDialog.this.mEditText = editTextAlertDialogPerso.getText().toString();
				
				dialog.dismiss();
				
			}
		});
		
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				ModalDialog.this.mQuitModal = true;
				ModalDialog.this.mEditText = "Cancel";
				dialog.cancel();
			}
		});
		
		CheckBox chkSSH = (CheckBox) alertDialogView.findViewById(R.id.ChkAlertDialogPerso);
		
		
		AlertDialog alert = builder.create();
		
		alert.show();
		
		doModal();
		return mEditText;
	}
	
	public String showSSHDialog(Context context, String info, 
								final String host, final String username, 
								final String password, final String meeting_name, final String file) {
		
		if (!prepareModal()) {
			return "Cancel";
		}
		
		mEditText = "Cancel";
		
		LayoutInflater factory = LayoutInflater.from(context);
		final View alertDialogView = factory.inflate(R.layout.alertdialogssh, null);

				
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(info);
		
		builder.setView(alertDialogView);
		
		builder.setCancelable(false);
		
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				
				SendToSSH(host, username, password, meeting_name, file);
				
				ModalDialog.this.mQuitModal = true;
				
				final EditText editTextAlertDialogPerso = (EditText) alertDialogView.findViewById(R.id.EditTextAlertDialogHost);
				
				ModalDialog.this.mEditText = editTextAlertDialogPerso.getText().toString();
				
				dialog.dismiss();
				
			}
		});
		
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				ModalDialog.this.mQuitModal = true;
				ModalDialog.this.mEditText = "Cancel";
				//dialog.cancel();
			}
		});
		
		//CheckBox chkSSH = (CheckBox) alertDialogView.findViewById(R.id.ChkAlertDialogPerso);
		
		
		AlertDialog alert = builder.create();
		
		EditText editTextHost = (EditText) alertDialogView.findViewById(R.id.EditTextAlertDialogHost);
	    editTextHost.setText(host); 
	    EditText editTextUsername = (EditText) alertDialogView.findViewById(R.id.EditTextAlertDialogUsername);
	    editTextUsername.setText(username);
	    EditText editTextPassword = (EditText) alertDialogView.findViewById(R.id.EditTextAlertDialogPassword);
	    editTextPassword.setText(password);
		
		alert.show();
		
		doModal();
		return mEditText;
		
		
	}
			
	private boolean prepareModal() {
		Class<?> clsMsgQueue = null;
		Class<?> clsMessage = null;
		
		try {
			clsMsgQueue = Class.forName("android.os.MessageQueue");
		}	
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
		try {
			clsMessage = Class.forName("android.os.Message");
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
		try {
			mMsgQueueNextMethod = clsMsgQueue.getDeclaredMethod("next", new Class[]{});
		}
		catch (SecurityException e) {
			e.printStackTrace();
			return false;
		}
		catch (NoSuchMethodException e){
			e.printStackTrace();
			return false;
		}
		
		mMsgQueueNextMethod.setAccessible(true);
		
		try {
			mMsgTargetFiled = clsMessage.getDeclaredField("target");
		}
		catch (SecurityException e){
			e.printStackTrace();
			return false;
		}
		catch (NoSuchFieldException e) {
			e.printStackTrace();
			return false;
		}
		
		mMsgTargetFiled.setAccessible(true);
		return true;
	}
	
	private void doModal() {
		mQuitModal = false;
		
		//get message queue associated with main UI thread
		MessageQueue queue = Looper.myQueue();
		while (!mQuitModal) {
			// call queue.next(), might block
			Message msg = null;
			try {
				msg = (Message)mMsgQueueNextMethod.invoke(queue, new Object[]{});
			}
			catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		
		
			if (null != msg) {
				Handler target = null;
				try {
					target = (Handler)mMsgTargetFiled.get(msg);
				}
				catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
				catch (IllegalAccessException e) {
					e.printStackTrace();
				}
		
				if (target == null) {
					// No target is a magic identifier for the quit message.
					mQuitModal = true;
				}
		
				target.dispatchMessage(msg);
				msg.recycle();
	
			}
		}
	}
	
	private Boolean SendToSSH(String host, String username, String password, String meeting_name, String file){
		
		JSch jsch=new JSch();
                	 
        try {
			Session session=jsch.getSession(username, host, 22);
			
			java.util.Properties config = new java.util.Properties(); 
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);					
				
			session.setPassword(password);
				
			session.connect(30000);
				
			Channel channel=session.openChannel("shell");
			channel.setInputStream(System.in);
			channel.setOutputStream(System.out);
			channel.connect(3*1000);
				
			File myFile = new File(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime",meeting_name.toString() + ".txt"); //on déclare notre futur fichier
            File myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "SpeakingTime");
            Boolean success=true;
		    if (!myDir.exists()) {
		        success = myDir.mkdir(); //On crée le répertoire (s'il n'existe pas!!)
		    }
		    if (success){
		       	//String data= "Ce que je veux ecrire dans mon fichier \r\n";
		       	FileOutputStream output = new FileOutputStream(myFile,true); //le true est pour écrire en fin de fichier, et non l'écraser
		       	//output.write(data.getBytes());
		       	//output.write(file.getBytes());
		    }
		    else {
		    	System.out.println ("ERREUR DE CREATION DE DOSSIER");
		    }
				
			ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
		    sftpChannel.connect();
		    sftpChannel.put(new FileInputStream(myFile), myFile.getName());
			
			return true;
		}
		catch (Exception e){
			System.out.println(e.toString());
			return false;
		}
	}
	
	
}
