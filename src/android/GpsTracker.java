package org.apache.cordova.gps;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GpsTracker extends CordovaPlugin
{

	PluginResult mPluginResult;
	CallbackContext mCallbackContext;
	Intent mGpsIntent;
	boolean mIsTracking = false;

	private BroadcastReceiver mLocationReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			Bundle bundle = intent.getExtras();
			JSONObject location = new JSONObject();
			try
			{
				location.put("latitude", bundle.getDouble("latitude"));
				location.put("longitude", bundle.getDouble("longitude"));
			}
			catch(JSONException e)
			{
				e.printStackTrace();
			}

			mPluginResult = new PluginResult(PluginResult.Status.OK, location);
			mPluginResult.setKeepCallback(true);
			mCallbackContext.sendPluginResult(mPluginResult);
		}
	};

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException
	{
		if(action.equals("startTracking"))
		{
			mPluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
			mPluginResult.setKeepCallback(true);
			mCallbackContext = callbackContext;
			startGpsTracking(args);
			mIsTracking = true;
			return true;
		}
		else if(action.equals("stopTracking"))
		{
			stopGpsTracking();
			mIsTracking = false;
			callbackContext.success();
			return true;
		}
		else if(action.equals("isTracking"))
		{
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, mIsTracking));
			return true;
		}
		return false;
	}

	private void startGpsTracking(JSONArray args) throws JSONException
	{
		final int movementThreshold = args.getInt(0);
		final Activity activity = cordova.getActivity();
		mGpsIntent = new Intent(activity, GpsService.class);
		mGpsIntent.putExtra("movementThreshold", movementThreshold);
		activity.registerReceiver(mLocationReceiver, new IntentFilter(GpsService.ACTION_ON_LOCATION_CHANGED));
		activity.startService(mGpsIntent);
	}

	private void stopGpsTracking()
	{
		final Activity activity = cordova.getActivity();
		activity.stopService(mGpsIntent);
		activity.unregisterReceiver(mLocationReceiver);
	}

}
