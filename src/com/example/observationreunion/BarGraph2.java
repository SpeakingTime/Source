package com.example.observationreunion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class BarGraph2  extends Activity{

	private String url;
    private WebView webView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
               
        setContentView(R.layout.activity_statistic_boxplot);
        
        Button buttonQuitBoxPlot = (Button) findViewById(R.id.buttonQuitBoxPlot);
        buttonQuitBoxPlot.setOnClickListener( 
				new Button.OnClickListener(){
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Post();
						//finish();
					}
		});
        
        webView = (WebView) findViewById(R.id.WebView);
        url = "http://data-mysql.fr/boxplot/";
        
        try {
        		webView.loadUrl(url);
		}
        catch (Exception e){
        	e.printStackTrace();
        };
	}
	
	public void Post(){
		
		// Create a new HttpClient and Post Header
	    /*HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://data-mysql.fr/boxplot/test.php");*/

	    /*try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("firstname", "Rosset"));
	        nameValuePairs.add(new BasicNameValuePair("lastname", "Bruno"));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        //HttpResponse response = httpclient.execute(httppost);

	        // Execute HTTP Post Request
	        ResponseHandler<String> responseHandler=new BasicResponseHandler();
	        String responseBody = httpclient.execute(httppost, responseHandler);*/

	    //Just display the response back
	    /*System.out.println(responseBody);*/
	    
		List<Integer> timesList = new ArrayList<Integer>();
		timesList = getTimesList();
		
		url = "http://data-mysql.fr/boxplot/test2.php?a=Test&b=0&c=12&d=52&e=75&f=365";
	    
	    /*String postData = "username=my_username&password=my_password";
	    webView.postUrl(url,EncodingUtils.getBytes(postData, "BASE64"));*/

	    webView.loadUrl(url);
	    
	    /*} catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }*/
	}
	
	public List<Integer> getTimesList(){
		List<Integer> timesList = new ArrayList<Integer>();
    	Bundle b = getIntent().getExtras();
    	String s = b.getString("participantsWithSpeakingTime");
    	Scanner scanner = new Scanner(s);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			int pos = line.indexOf(";", 0);
			String value = line.substring(pos+1, line.length()-1);
			//System.out.println("value : " + value);
			int i_value = Integer.valueOf(value);
			System.out.println(i_value);
			timesList.add(i_value);
		}
		return timesList;
	}
	
}
