package com.tajchert.glassware.eyewallet;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AddService extends Service {

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	public int onStartCommand(Intent intent, int flags, int startId) {
		Intent i = new Intent(this, AddTransactionActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		return START_STICKY;
	}
	
	
	
}