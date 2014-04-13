package com.tajchert.glassware.eyewallet;

import java.util.List;

import android.app.Activity;
import android.content.Context;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class AddTransactionActivity extends Activity implements
		SensorEventListener, LocationListener {

	private static final String LOG_TAG = "SensorTest";
	private TextView text;
	private SensorManager mSensorManager;
	private Sensor mOrientation;
	
	private int startValue = 1;
	private int startPitch = 80;
	
	private int maxPaymentVal = 100;
	private int minPaymentVal = 1;

	Float azimuth_angle;
	Float pitch_angle;
	Float roll_angle;

	private static void removeBackgrounds(final View aView) {
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
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.transaction_add);

		// removeBackgrounds(getWindow().getDecorView());

		text = (TextView) findViewById(R.id.text);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
		for (Sensor sensor : sensors) {
			Log.i(LOG_TAG, "Found sensor: " + sensor.getName());
		}

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
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float azimuth_angle = event.values[0];
		float pitch_angle = event.values[1];
		float roll_angle = event.values[2];
		// Do something with these orientation angles.
		//text.setText("azimuth, pitch, roll, lat, lon:\n" + azimuth_angle + "\n"+ pitch_angle + "\n" + roll_angle);
		//Log.d(LOG_TAG, "azimuth, pitch, roll, lat, lon:\n" + azimuth_angle + "\n"+ pitch_angle + "\n" + roll_angle);
		int val = (int) (Math.abs(pitch_angle)-startPitch);
		int newPaymentVal = (val*maxPaymentVal/90);
		//int newVal = (int) Math.round((startValue * (val * val)));
		text.setText(newPaymentVal +" PLN");
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