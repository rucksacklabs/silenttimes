package de.reneruck.silenttimes;

import android.media.AudioManager;
import android.widget.Switch;

public class AlarmHandler {

	public void changeMode(Switch switcher, boolean checked, AudioManager audioManager) {
		if(checked){
			setNightMode(audioManager);
		} else {
			setDayMode(audioManager);
		}
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
