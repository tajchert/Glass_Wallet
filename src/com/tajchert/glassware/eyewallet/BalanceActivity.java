package com.tajchert.glassware.eyewallet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class BalanceActivity extends Activity{

	private static final String LOG_TAG = "SensorTest";
	private static final String PREFS_SET = "payments_set";
	private TextView mBalance;
	private boolean firstRun = true;
	
	private Set<String> payments;
	private Integer balance = 0;
	
	
	private SharedPreferences prefs;

	/*private static void removeBackgrounds(final View aView) {
		aView.setBackgroundDrawable(null);
		aView.setBackgroundColor(Color.TRANSPARENT);
		aView.setBackgroundResource(0);
		if (aView instanceof ViewGroup) {
			final ViewGroup group = (ViewGroup) aView;
			final int childCount = group.getChildCount();
			for (int i = 0; i < childCount; i++) {
				final View child = group.getChildAt(i);
				removeBackgrounds(child);
			}
		}
	}*/
	
	private Set<String> getSharedSet(){
		Set<String> set = prefs.getStringSet(PREFS_SET, null);
		return set;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.balance_check);
		
		prefs = this.getSharedPreferences("com.tajchert.glassware.eyewallet", Context.MODE_PRIVATE);
		mBalance = (TextView) findViewById(R.id.balance);
	}


	@Override
	protected void onResume() {
		super.onResume();
		payments = getSharedSet();
		if(payments == null){
			payments = new HashSet<String>();
		}
		for (String pay : payments) {
			Log.i(LOG_TAG, "Payment: " + pay);
			balance += Integer.parseInt(pay);
		}
		Log.i(LOG_TAG, "Balance: " + balance);
		setTextBalance();
	}
	
	private void setTextBalance(){
		if(firstRun == true && mBalance != null){
			Log.i(LOG_TAG, "A");
			if(balance != null){
				Log.i(LOG_TAG, "B");
				mBalance.setText(balance + "");
				if(balance >= 0){
					mBalance.setTextColor(Color.GREEN);
				}else{
					mBalance.setTextColor(Color.RED);
				}
				firstRun = false;
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
}

