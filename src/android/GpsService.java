package org.apache.cordova.gps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class GpsService extends Service implements LocationListener
{

	public static final String ACTION_ON_LOCATION_CHANGED = "org.apache.cordova.gps.ON_LOCATION_CHANGED";
	// minimum time interval between location updates, in milliseconds
	private static final int INTERVAL = 1000;

	private LocationManager mLocationMgr;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		final Bundle extras = intent.getExtras();
//		TODO handle NullPointerException
		mLocationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mLocationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, INTERVAL,
				extras.getInt("movementThreshold", 1), this);
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onDestroy()
	{
		mLocationMgr.removeUpdates(this);
	}

	public void onLocationChanged(Location location)
	{
		Intent intent = new Intent(ACTION_ON_LOCATION_CHANGED);
		intent.putExtra("latitude", location.getLatitude());
		intent.putExtra("longitude", location.getLongitude());
		sendBroadcast(intent);
	}

	public void onStatusChanged(String provider, int status, Bundle extras)
	{

	}

	public void onProviderEnabled(String provider)
	{

	}

	public void onProviderDisabled(String provider)
	{

	}

}
