package de.reneruck.silenttimes;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends Activity {

    public static final String DEACTIVATE_TIME = "activate";
	public static final String ACTIVATE_TIME = "deactivate";
	public static final String STORED_TIMES = "stored-times";
	public static final String STORED_INTENT = "stored-intent";
	public static final String TIMER_TYPE = "timer-type";
	
	private static String TAG = "silenttimes";
	private AudioManager audioManager;
	private AlarmHandler alarmHandler;
	private AlarmHelper alarmHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.main);

        this.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        this.alarmHandler = new AlarmHandler();
        this.alarmHelper = new AlarmHelper(getApplicationContext());

        LinearLayout activationLayout = (LinearLayout) findViewById(R.id.activation_layout);
        LinearLayout deactivationLayout = (LinearLayout) findViewById(R.id.deactivation_layout);

        activationLayout.setOnClickListener(this.activationTimeChooserListener);
        deactivationLayout.setOnClickListener(this.deactivationTimeChooserListener);

        Switch switcher = (Switch) findViewById(R.id.mode_switch);
        switcher.setOnCheckedChangeListener(modeSwitchListener);

        TextView activationTimeIndicator = (TextView) findViewById(R.id.main_activationTime);
        TextView deactivationTimeIndicator = (TextView) findViewById(R.id.main_deactivationTime);

        int[] storedTime = this.alarmHelper.getStoredTime(TimerType.activation);
		activationTimeIndicator.setText(storedTime[0] + ":" + storedTime[1]);

        int[] storedTime2 = this.alarmHelper.getStoredTime(TimerType.deactivation);
		deactivationTimeIndicator.setText(storedTime2[0] + ":" + storedTime2[1]);

        checkState(switcher);
        updateBars();
    }
    
	/* Creates the menu items */
    public boolean onCreateOptionsMenu(Menu menu) {
    	Log.d(TAG, "------------- onCreateMenue --------------");
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main, menu);
        return true;
    }

	/* 
     * Menü Items
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        
    	switch(item.getItemId()){
	        case  R.id.menu_configure:
	        	Intent i = new Intent(this, AlarmHelper.class);
	        	startActivity(i);
	        	break;
	        default:
	            return super.onOptionsItemSelected(item);
    	}
		return true;
     }
    
    private void checkState(Switch switcher) {
    	if(AudioManager.RINGER_MODE_VIBRATE == this.audioManager.getRingerMode()
    			|| AudioManager.RINGER_MODE_SILENT == this.audioManager.getRingerMode()) {
    		switcher.setChecked(true);
    	} else {
    		switcher.setChecked(false);
    	}
	}

	private void updateBars() {
    	ProgressBar notificationVolumeBar = (ProgressBar) findViewById(R.id.notfication_indicator);
    	ProgressBar ringVolumeBar = (ProgressBar) findViewById(R.id.ring_volume);
    	
    	int notificationVolume = this.audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
    	int ringVolume = this.audioManager.getStreamVolume(AudioManager.STREAM_RING);
    	
    	
    	notificationVolumeBar.setProgress(notificationVolume * 10);
    	ringVolumeBar.setProgress(ringVolume * 10);
    }

	private CompoundButton.OnCheckedChangeListener modeSwitchListener = new OnCheckedChangeListener() {
		

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					alarmHandler.setNightMode(audioManager);
				} else {
					alarmHandler.setDayMode(audioManager);
				}
				updateBars();
		}
	};
	
	private OnClickListener activationTimeChooserListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			FragmentManager fr = getFragmentManager();
			TimePickerFragmentDialog timePickerFragmentDialog = new TimePickerFragmentDialog(TimerType.activation);
			timePickerFragmentDialog.show(fr, "timepicker");
		}
	};
	
	private OnClickListener deactivationTimeChooserListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			FragmentManager fr = getFragmentManager();
			TimePickerFragmentDialog timePickerFragmentDialog = new TimePickerFragmentDialog(TimerType.deactivation);
			timePickerFragmentDialog.show(fr, "timepicker");
		}
	};
	
}

