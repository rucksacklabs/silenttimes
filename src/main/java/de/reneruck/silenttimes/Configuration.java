package de.reneruck.silenttimes;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class Configuration extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configuration);
		TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker1);
		timePicker.setIs24HourView(true);
		
		timePicker.setOnTimeChangedListener(onTimeChangedListener);
	}
	
	private OnTimeChangedListener onTimeChangedListener = new OnTimeChangedListener() {
		
		@Override
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			
		}
	};
}
