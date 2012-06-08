package de.reneruck.silenttimes;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.Switch;

public class HelloAndroidActivity extends Activity {

    private static String TAG = "silenttimes";
	private AudioManager audioManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.main);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        
        
        this.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        int mode = this.audioManager.getRingerMode();
        
        Switch switcher = (Switch) findViewById(R.id.mode_switch);
        switch (mode) {
			case AudioManager.RINGER_MODE_NORMAL:
				switcher.setChecked(false);
				break;
			case AudioManager.RINGER_MODE_VIBRATE:
			default:
				switcher.setChecked(true);
				break;
		}
        switcher.setOnCheckedChangeListener(modeSwitchListener);
        checkState(switcher);
        updateBars();
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
			changeMode((Switch) buttonView, isChecked);
		}
	};
	
	private void changeMode(Switch switcher, boolean checked) {
		if(checked){
			setNightMode(this.audioManager);
		} else {
			setDayMode(this.audioManager);
		}
		updateBars();
	}
    
    private void setNightMode(AudioManager audioManager) {
    	
    	audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, AudioManager.FLAG_VIBRATE);
    	audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, 0, AudioManager.FLAG_VIBRATE);
    	audioManager.setStreamVolume(AudioManager.STREAM_RING, 0, AudioManager.FLAG_VIBRATE);
    	audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, AudioManager.FLAG_VIBRATE);
    	audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
    }
    
    private void setDayMode(AudioManager audioManager) {
    	
    	audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION) -1, AudioManager.FLAG_VIBRATE);
    	audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL) -1, AudioManager.FLAG_VIBRATE);
    	audioManager.setStreamVolume(AudioManager.STREAM_RING, audioManager.getStreamMaxVolume(AudioManager.STREAM_RING) -1, AudioManager.FLAG_VIBRATE);
    	audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) -1, AudioManager.FLAG_VIBRATE);
    	audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }

}

