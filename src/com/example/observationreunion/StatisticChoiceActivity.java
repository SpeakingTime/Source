package com.example.observationreunion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

	public class StatisticChoiceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistic_choice);

		Button buttonHistograms = (Button) findViewById(R.id.buttonHistograms);
		buttonHistograms.setOnClickListener( 
				new Button.OnClickListener(){
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(StatisticChoiceActivity.this, StatisticActivity.class);
						intent.putExtra("statistic_type", "histograms");
						startActivity(intent);
					}
				
		});
		
		Button buttonBoxPlot = (Button) findViewById(R.id.buttonBoxPlots);
		buttonBoxPlot.setOnClickListener( 
				new Button.OnClickListener(){
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(StatisticChoiceActivity.this, StatisticActivity.class);
						intent.putExtra("statistic_type", "boxplots");
						startActivity(intent);
					}
				
		});
		
	}

}
