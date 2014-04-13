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

public class AddTransactionActivity extends Activity implements
		SensorEventListener, LocationListener {

	private static final String LOG_TAG = "SensorTest";
	private static final String PREFS_SET = "payments_set";
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
	@Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_DPAD_CENTER) {
        	Log.i(LOG_TAG, "KEYCODE_DPAD_CENTER");
        	payments.add(newPaymentVal+"");
        	setSharedSet(payments);
        	this.finish();
            return true;
        }
        return false;
    }
	
	private Set<String> getSharedSet(){
		Set<String> set = prefs.getStringSet(PREFS_SET, null);
		return set;
	}
	
	private void setSharedSet(Set<String> in){
		prefs.edit().putStringSet(PREFS_SET, in).commit();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.transaction_add);
		
		prefs = this.getSharedPreferences("com.tajchert.glassware.eyewallet", Context.MODE_PRIVATE);
		text = (TextView) findViewById(R.id.text);
		mBalance = (TextView) findViewById(R.id.balance);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		/*List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
		for (Sensor sensor : sensors) {
			Log.i(LOG_TAG, "Found sensor: " + sensor.getName());
		}*/

		mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

		LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		List<String> providers = mLocationManager.getAllProviders();
		for (String provider : providers) {
			Log.i(LOG_TAG, "Found provider: " + provider);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_UI);
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
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		//float azimuth_angle = event.values[0];
		float pitch_angle = event.values[1];
		//float roll_angle = event.values[2];
		
		// Do something with these orientation angles.
		//text.setText("azimuth, pitch, roll, lat, lon:\n" + azimuth_angle + "\n"+ pitch_angle + "\n" + roll_angle);
		//Log.d(LOG_TAG, "azimuth, pitch, roll, lat, lon:\n" + azimuth_angle + "\n"+ pitch_angle + "\n" + roll_angle);
		int val = (int) (Math.abs(pitch_angle)-startPitch);
		newPaymentVal = (val*maxPaymentVal/90);
		//int newVal = (int) Math.round((startValue * (val * val)));
		
		if(newPaymentVal>0){
			text.setText("+"+newPaymentVal +" ");
			text.setTextColor(Color.GREEN);
		}else{
			text.setText(newPaymentVal +" ");
			text.setTextColor(Color.RED);
		}
		
		
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
	
}

