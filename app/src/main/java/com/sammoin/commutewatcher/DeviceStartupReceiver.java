package com.sammoin.commutewatcher;

import java.io.IOException;
import java.io.StreamCorruptedException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

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
				CommuteCheckAlarmService.PREF_IS_ALARM_ON, false);

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
