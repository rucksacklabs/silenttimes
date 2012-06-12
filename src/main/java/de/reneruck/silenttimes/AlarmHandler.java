package de.reneruck.silenttimes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

public class AlarmHandler extends BroadcastReceiver{

	private static final String TAG = null;

    public void setNightMode(AudioManager audioManager) {
    	
    	audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, AudioManager.FLAG_VIBRATE);
    	audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, 0, AudioManager.FLAG_VIBRATE);
    	audioManager.setStreamVolume(AudioManager.STREAM_RING, 0, AudioManager.FLAG_VIBRATE);
    	audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, AudioManager.FLAG_VIBRATE);
    	audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
    }
    
    public void setDayMode(AudioManager audioManager) {
    	
    	audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION) -1, AudioManager.FLAG_VIBRATE);
    	audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL) -1, AudioManager.FLAG_VIBRATE);
    	audioManager.setStreamVolume(AudioManager.STREAM_RING, audioManager.getStreamMaxVolume(AudioManager.STREAM_RING) -1, AudioManager.FLAG_VIBRATE);
    	audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) -1, AudioManager.FLAG_VIBRATE);
    	audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "Received intent");
		AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		if(AudioManager.RINGER_MODE_VIBRATE == am.getRingerMode()
				|| AudioManager.RINGER_MODE_SILENT == am.getRingerMode()) {
			Log.d(TAG, "Setting to day mode");
			setDayMode(am);
		} else {
			Log.d(TAG, "Setting to night mode");
			setNightMode(am);
		}
		
	}
}
