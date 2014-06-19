package com.ufreedom.testview;

import com.ufreedom.widget.FlatChronometer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;




public class MainActivity extends Activity {

	
	private FlatChronometer cdView;
	private EditText editText;
	private RadioGroup radioGroup;
	private Button btnStart;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initWidget();
		initListener();

		
	}

	private void initListener() {
		btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
		      switch (radioGroup.getCheckedRadioButtonId()) {
		      
				case R.id.radion_btn_increase:
					cdView.setCircularModel(FlatChronometer.MODEL_INCREASE);
					break;

					
					
				case R.id.radion_btn_decrease:
					cdView.setCircularModel(FlatChronometer.MODEL_DECREASE);

					break;
				}
		      
		      
		      
		      Editable edit = editText.getText();
		      ;
		      cdView.setMinute(Integer.parseInt(edit.toString())).start();
				
				
				
			}
		});
		
	}

	private void initWidget() {
		cdView = (FlatChronometer) findViewById(R.id.count_down_view);
		
		cdView.setBenmostWidth(getResources().getDimension(R.dimen.countdown_view_benmost_width))
		      .setBenmostColor(Color.parseColor("#ffffff"))
		      
		      .setMiddleWidth(getResources().getDimension(R.dimen.countdown_view_middle_didth))
		      .setMiddleColor(Color.parseColor("#14b9d6"))
		      
		      
		      .setOutermosWidth(getResources().getDimension(R.dimen.countdown_view_outmost_didth))
		      .setOutermosColor(Color.parseColor("#14b9d6"))
		      
		      .setBackgroundColor(Color.parseColor("#323a45"));
		      
		
		
		cdView.setTextSize(80);   
		cdView.drawView();
		
		
		editText = (EditText) findViewById(R.id.edit_text);
		radioGroup = (RadioGroup) findViewById(R.id.radio_group);
	
		
		btnStart = (Button) findViewById(R.id.start);
		
		
		
	}

	
	

}
