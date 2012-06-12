package de.reneruck.silenttimes;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.format.DateFormat;
import android.util.Log;

public class AlarmHelper {

	private static final String TAG = "Alarm Helper";
	private Context context;
	private SharedPreferences storedTimes;
	
	public AlarmHelper(Context context) {
		this.context = context;
		this.storedTimes = this.context.getSharedPreferences(MainActivity.STORED_TIMES, Context.MODE_PRIVATE);
	}
	
	public int[] getStoredTime(TimerType timerType) {
		switch (timerType) {
			case activation:
				String activate = this.storedTimes.getString(MainActivity.ACTIVATE_TIME, "");
				if(!activate.isEmpty()){
					return new int[]{Integer.parseInt(activate.split(":")[0]) , Integer.parseInt(activate.split(":")[1]) }; 
				}
				
				break;
			case deactivation:
				String deactivate = storedTimes.getString(MainActivity.DEACTIVATE_TIME, "");
				if(!deactivate.isEmpty()){
					return new int[]{Integer.parseInt(deactivate.split(":")[0]) , Integer.parseInt(deactivate.split(":")[1]) }; 
				}

		default:
			break;
		}
		return new int[]{0,0};
	}
	
	public void updateAlarmingTime(int hour, int min, TimerType timerType) {

		int activate_hour = 0;
		int activate_min = 0;
		int deactivate_hour = 0;
		int deactivate_min = 0;
		
		switch (timerType) {
		case activation:
			
			activate_hour = hour;
			activate_min = min;
			
			int[] storedTime = getStoredTime(TimerType.deactivation);
			
			if (storedTime.length > 1) {
				deactivate_hour = storedTime[0]; 
				deactivate_min = storedTime[1];				
			}
			store(MainActivity.ACTIVATE_TIME, activate_hour + ":" + activate_min);
			break;

		case deactivation:
			
			deactivate_hour = hour;
			deactivate_min = min;
			
			int[] storedTime2 = getStoredTime(TimerType.activation);
			
			if(storedTime2.length > 1) {
				activate_hour = storedTime2[0];
				activate_min = storedTime2[1];
			}
			store(MainActivity.DEACTIVATE_TIME, deactivate_hour + ":" + deactivate_min);
			break;

		default:
			break;
		}
		
		long activateTimestamp = calcTimestamp(activate_hour, activate_min);
		long deactivateTimestamp = calcTimestamp(deactivate_hour, deactivate_min);
		setAlarm(activateTimestamp, deactivateTimestamp);
	}

	private long calcTimestamp(int hour, int min) {
		Calendar nowCal = Calendar.getInstance();
		long currentTimeMillis = System.currentTimeMillis();
		nowCal.setTimeInMillis(currentTimeMillis);
		
		int hourDiff = hour - nowCal.get(Calendar.HOUR_OF_DAY);
		
		if(hourDiff <= 0) {
			hourDiff = 24 + hourDiff; 
		}
		
		int minuteDiff = min - nowCal.get(Calendar.MINUTE);
		
		long hourDiffInMs = (hourDiff * 60) * 60000;
		long minuteDiffInMs = minuteDiff * 60000;
		
		return currentTimeMillis + hourDiffInMs + minuteDiffInMs;
	}

	private void store(String key, String value){
		SharedPreferences sharedPreferences = this.context.getSharedPreferences(MainActivity.STORED_TIMES, Context.MODE_PRIVATE);
		Editor edit = sharedPreferences.edit();
		edit.putString(key, value);
		edit.commit();
	}
	
	private void clearOldAlarms(){
		AlarmManager am = (AlarmManager)this.context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent pi = getPendingIntent();
		am.cancel(pi);
	}
	
	private void setAlarm(long activate, long deactivate) {
//		clearOldAlarms();
		
		PendingIntent sender = getPendingIntent(); 
		
        // Schedule the alarm!
		AlarmManager am = (AlarmManager)this.context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,
                        activate, Math.abs(activate - deactivate), sender);
        
        CharSequence format = DateFormat.format("dd.MM. hh:mm", activate);
        
        Log.d(TAG, "Setting new Alarm, next start at " + format + "h repeating every " + (Math.abs(activate - deactivate) / 10000) + "min");
	}
	
	private PendingIntent getPendingIntent() {
		 Intent intent = new Intent(this.context, AlarmHandler.class);
		 return PendingIntent.getBroadcast(this.context, 0, intent, 0);
	}
}
