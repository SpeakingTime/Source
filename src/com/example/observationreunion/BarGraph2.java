package com.example.observationreunion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import android.app.AlertDialog;
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
                
        webView = (WebView) findViewById(R.id.WebView);
        Post();
        
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
		
		String minValue = String.valueOf(getMinValue());
	    String quartile1 = String.valueOf(getQuartile1());
	    String median = String.valueOf(getMedian());
	    String quartile3 = String.valueOf(getQuartile3());
	    String maxValue = String.valueOf(getMaxValue());
	    
		url = "http://data-mysql.fr/boxplot/test3.php?a=" + minValue +
		                                              "&b=" + quartile1 +
		                                              "&c=" + median +
		                                              "&d=" + quartile3 +
		                                              "&e=" + maxValue;
	    
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
			//System.out.println(i_value);
			timesList.add(i_value);
		}
		return timesList;
	}
	
	public Integer getQuartile1(){
		//List<Integer> sortedValuesList = new ArrayList<Integer>(Arrays.asList(235,235,237,238,238,239,239,239,240,241,250,251,251,253,253,255,255,255,257,260,241,243,245,247,247,249,250,205,250,250));//getTimesList();
		List<Integer> sortedValuesList = getTimesList();
		Collections.sort(sortedValuesList);
		
		Number[] numbers = new Number[sortedValuesList.size()];
		for (int i=0; i < sortedValuesList.size(); i++){
			numbers[i] = sortedValuesList.get(i);
		}
		
		/*AlertDialog.Builder alertDialog = new AlertDialog.Builder(BarGraph2.this);
		alertDialog.setTitle("quartile1");
		alertDialog.setMessage(StatisticsUtils.quartile1(numbers).toString());
		alertDialog.show();*/
		
		return StatisticsUtils.quartile1(numbers).intValue();
	}
	
	public Integer getQuartile3(){
		//List<Integer> sortedValuesList = new ArrayList<Integer>(Arrays.asList(235,235,237,238,238,239,239,239,240,241,250,251,251,253,253,255,255,255,257,260,241,243,245,247,247,249,250,205,250,250));//getTimesList();
		List<Integer> sortedValuesList = getTimesList();
		Collections.sort(sortedValuesList);
		
		Number[] numbers = new Number[sortedValuesList.size()];
		for (int i=0; i < sortedValuesList.size(); i++){
			numbers[i] = sortedValuesList.get(i);
		}

		/*AlertDialog.Builder alertDialog = new AlertDialog.Builder(BarGraph2.this);
		alertDialog.setTitle("quartile3");
		alertDialog.setMessage(StatisticsUtils.quartile3(numbers).toString());
		alertDialog.show();*/
		
		return StatisticsUtils.quartile3(numbers).intValue();
	}
	
	public Integer getMedian(){
		//List<Integer> sortedValuesList = new ArrayList<Integer>(Arrays.asList(235,235,237,238,238,239,239,239,240,241,250,251,251,253,253,255,255,255,257,260,241,243,245,247,247,249,250,250,250,250));//getTimesList();
		List<Integer> sortedValuesList = getTimesList();
		Collections.sort(sortedValuesList);
		
		Number[] numbers = new Number[sortedValuesList.size()];
		for (int i=0; i < sortedValuesList.size(); i++){
			numbers[i] = sortedValuesList.get(i);
		}

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(BarGraph2.this);
		alertDialog.setTitle("median");
		alertDialog.setMessage(StatisticsUtils.median(numbers).toString());
		alertDialog.show();
		
		return StatisticsUtils.median(numbers).intValue();

	}
	
	public Integer getMinValue(){
		//List<Integer> sortedValuesList = new ArrayList<Integer>(Arrays.asList(235,235,237,238,238,239,239,239,240,241,250,251,251,253,253,255,255,255,257,260,241,243,245,247,247,249,250,250,250,250));//getTimesList();
		List<Integer> sortedValuesList = getTimesList();
		Collections.sort(sortedValuesList);
		
		Number[] numbers = new Number[sortedValuesList.size()];
		for (int i=0; i < sortedValuesList.size(); i++){
			numbers[i] = sortedValuesList.get(i);
		}
		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(BarGraph2.this);
		alertDialog.setTitle("minValue");
		alertDialog.setMessage(StatisticsUtils.minValue(numbers).toString());
		alertDialog.show();
				
		return StatisticsUtils.minValue(numbers).intValue();
	}
	
	public Integer getMaxValue(){
		//List<Integer> sortedValuesList = new ArrayList<Integer>(Arrays.asList(235,235,237,238,238,239,239,239,240,241,250,251,251,253,253,255,255,255,257,260,241,243,245,247,247,249,250,250,250,250));//getTimesList();
		List<Integer> sortedValuesList = getTimesList();
		Collections.sort(sortedValuesList);
		
		Number[] numbers = new Number[sortedValuesList.size()];
		for (int i=0; i < sortedValuesList.size(); i++){
			numbers[i] = sortedValuesList.get(i);
		}
		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(BarGraph2.this);
		alertDialog.setTitle("maxValue");
		alertDialog.setMessage(StatisticsUtils.maxValue(numbers).toString());
		alertDialog.show();
				
		return StatisticsUtils.maxValue(numbers).intValue();
	}
	
		
	
}
