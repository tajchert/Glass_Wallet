package com.tajchert.glassware.eyewallet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.google.android.glass.app.Card;
import com.google.android.glass.timeline.TimelineManager;

public class AddTransactionActivity extends Activity implements
		SensorEventListener {

	private static final String LOG_TAG = "SensorTest";
	private static final String PREFS_SET = "payments_set";
	private static final String CURRENCY = " PLN";
	private TextView text;
	private TextView mBalance;
	private SensorManager mSensorManager;
	private Sensor mOrientation;
	private boolean firstRun = true;

	private Set<String> payments;
	private Integer balance = 0;

	private int startPitch = 80;
	private int maxPaymentVal = 100;
	private int newPaymentVal = 0;

	Float azimuth_angle;
	Float pitch_angle;
	Float roll_angle;

	private SharedPreferences prefs;

	@Override
	public boolean onKeyDown(int keycode, KeyEvent event) {
		if (keycode == KeyEvent.KEYCODE_DPAD_CENTER) {
			Log.i(LOG_TAG, "KEYCODE_DPAD_CENTER");
			new SaveAndExit().execute(newPaymentVal + "");
			return true;
		}
		return false;
	}

	private Set<String> getSharedSet() {
		Set<String> set = prefs.getStringSet(PREFS_SET, null);
		return set;
	}

	private void setSharedSet(Set<String> in) {
		prefs.edit().putStringSet(PREFS_SET, in).commit();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.transaction_add);

		prefs = this.getSharedPreferences("com.tajchert.glassware.eyewallet",
				Context.MODE_PRIVATE);
		text = (TextView) findViewById(R.id.text);
		mBalance = (TextView) findViewById(R.id.balance);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mOrientation,
				SensorManager.SENSOR_DELAY_UI);
		payments = getSharedSet();
		if (payments == null) {
			payments = new HashSet<String>();
		}
		for (String pay : payments) {
			Log.i(LOG_TAG, "Payment: " + pay);
			balance += Integer.parseInt(pay);
		}
		Log.i(LOG_TAG, "Balance: " + balance);
		setTextBalance();
	}

	private void setTextBalance() {
		if (firstRun == true && mBalance != null) {
			Log.i(LOG_TAG, "A");
			if (balance != null) {
				Log.i(LOG_TAG, "B");
				mBalance.setText(balance + "");
				if (balance >= 0) {
					mBalance.setTextColor(Color.GREEN);
				} else {
					mBalance.setTextColor(Color.RED);
				}
				firstRun = false;
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// float azimuth_angle = event.values[0];
		float pitch_angle = event.values[1];
		// float roll_angle = event.values[2];

		// Log.d(LOG_TAG, "azimuth, pitch, roll, lat, lon:\n" + azimuth_angle +
		// "\n"+ pitch_angle + "\n" + roll_angle);
		int val = (int) (Math.abs(pitch_angle) - startPitch);
		newPaymentVal = (val * maxPaymentVal / 90);

		if (newPaymentVal > 0) {
			text.setText("+" + newPaymentVal + " ");
			text.setTextColor(Color.GREEN);
		} else {
			text.setText(newPaymentVal + " ");
			text.setTextColor(Color.RED);
		}
	}

	private class SaveAndExit extends AsyncTask<String, Void, String> {
		private Card cardOperation;

		@Override
        protected String doInBackground(String... params) {
        	payments.add(params[0]);
        	setSharedSet(payments);
        	
        	
        	int payment = Integer.parseInt(params[0]);
        	
        	Calendar cal = Calendar.getInstance();
        	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
        	
        	cardOperation = new Card(AddTransactionActivity.this);
        	cardOperation.setImageLayout(Card.ImageLayout.FULL);
        	if(payment >= 0){
        		cardOperation.setText("Income: "+ Math.abs(payment)+ CURRENCY);
        		cardOperation.addImage(R.drawable.income_full);
        	}else if(payment < 0){
        		cardOperation.setText("Outcome: "+ Math.abs(payment)+ CURRENCY);
        		cardOperation.addImage(R.drawable.outcome_full);
        	}
        	cardOperation.setFootnote("" + sdf.format(cal.getTime()));
        	
        	//TODO
    		//card1.addImage(R.drawable.logo_pjwstk_red);
        	
            return "Executed";
        }

		@Override
		protected void onPostExecute(String result) {

			AddTransactionActivity.this.finish();
			TimelineManager tm = TimelineManager.from(AddTransactionActivity.this);
			tm.insert(cardOperation);
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}
}
