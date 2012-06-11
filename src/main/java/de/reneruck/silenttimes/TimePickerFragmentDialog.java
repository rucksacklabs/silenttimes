package de.reneruck.silenttimes;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

public class TimePickerFragmentDialog extends DialogFragment {

	private TimePicker timePicker;
	private TimerType timerType;
	private AlarmHelper alarmHelper;

	public TimePickerFragmentDialog(TimerType timerType) {
		this.timerType = timerType;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.timepicker_dialog, container);
		this.timePicker = (TimePicker) view.findViewById(R.id.timePicker1);
		this.timePicker.setIs24HourView(true);
		
		this.alarmHelper = new AlarmHelper(this.timePicker.getContext());
		int[] storedTime = this.alarmHelper.getStoredTime(timerType);
		
		if(storedTime.length > 1) {
			this.timePicker.setCurrentHour(storedTime[0]);
			this.timePicker.setCurrentMinute(storedTime[1]);
		}
		
		Button cancelButton = (Button) view.findViewById(R.id.button_cancel);
		cancelButton.setOnClickListener(cancelListener);
		
		Button okButton = (Button) view.findViewById(R.id.button_ok);
		okButton.setOnClickListener(okListener);
		
		return view;
	}
	
	private OnClickListener okListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			store();
			dismiss();
		}
	};
	
	private OnClickListener cancelListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			dismiss();
		}
	};

	public void store() {
		Integer currentHour = this.timePicker.getCurrentHour();
		Integer currentMinute = this.timePicker.getCurrentMinute();
		
		this.alarmHelper.updateAlarmingTime(currentHour, currentMinute, this.timerType);
	}
}
