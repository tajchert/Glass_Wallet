package com.tajchert.glassware.eyewallet;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;

public class MinusService extends Service {

	

	/*
	 * TimelineManager allows applications to interact with the timeline.
	 * 
	 * Additional information: https://developers.google.com/glass/develop/gdk/reference/com/google/android/glass/timeline/TimelineManager
	 */
	private TimelineManager mTimelineManager;

	/*
	 * LiveCard lets you create cards as well as publish them to the users timeline.
	 * 
	 * Additional information: https://developers.google.com/glass/develop/gdk/reference/com/google/android/glass/timeline/LiveCard
	 */
	@SuppressWarnings("unused")
	private LiveCard mLiveCard;

	@Override
	public void onCreate() {
		super.onCreate();
		mTimelineManager = TimelineManager.from(this);
	} // onCreate

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	} // IBinder

	/*
	 * onStartCommand is used to start a service from your voice trigger you set up in res/xml/voice_trigger_start.xml
	 */
	public int onStartCommand(Intent intent, int flags, int startId) {
		/*if(Tools.isNetworkAvailable(this)){
			new UserLoginTask().execute();
		}*/
		Intent i = new Intent(this, BalanceActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		return START_STICKY;
	} // onStartCommand
	
	
	
}