package de.reneruck.silenttimes;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.Switch;

public class HelloAndroidActivity extends Activity {

    private static String TAG = "silenttimes";
	private AudioManager audioManager;
	private AlarmHandler alarmHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.main);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        
        this.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        this.alarmHandler = new AlarmHandler();
        
        Switch switcher = (Switch) findViewById(R.id.mode_switch);
        switcher.setOnCheckedChangeListener(modeSwitchListener);
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
	        	Intent i = new Intent(this, Configuration.class);
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
			alarmHandler.changeMode((Switch) buttonView, isChecked, audioManager);
			updateBars();
		}
	};
	

    private OnClickListener mStartRepeatingListener = new OnClickListener() {
        public void onClick(View v) {
            // When the alarm goes off, we want to broadcast an Intent to our
            // BroadcastReceiver.  Here we make an Intent with an explicit class
            // name to have our own receiver (which has been published in
            // AndroidManifest.xml) instantiated and called, and then create an
            // IntentSender to have the intent executed as a broadcast.
            // Note that unlike above, this IntentSender is configured to
            // allow itself to be sent multiple times.
            Intent intent = new Intent(HelloAndroidActivity.this, AlarmHandler.class);
            PendingIntent sender = PendingIntent.getBroadcast(HelloAndroidActivity.this,
                    0, intent, 0);

            // We want the alarm to go off 30 seconds from now.
            long firstTime = SystemClock.elapsedRealtime();
            firstTime += 15*1000;

            // Schedule the alarm!
            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            firstTime, 15*1000, sender);

            // Tell the user about what we did.
            }
    };
}

