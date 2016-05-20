package com.sammoin.commutewatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.io.StreamCorruptedException;

public class DeviceStartupReceiver extends BroadcastReceiver
{
	public static final String TAG = "DeviceStartupReceiver";

	public DeviceStartupReceiver()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean isOn = prefs.getBoolean(
				context.getString(R.string.pref_enable_disable_key), false);
		context.startService(new Intent(context, CommuteWidgetIntentService.class));
		try
		{
			CommuteCheckAlarmService.setServiceAlarm(context, isOn);
		} 
		catch (StreamCorruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
