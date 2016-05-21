package com.sammoin.commutewatcher;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.joda.time.LocalTime;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


public class CommuteCheckAlarmService extends IntentService {
    public static final String TAG = "MapQueryService";
    public static final String PREF_IS_ALARM_ON = "isServiceAlarmOn";
    public static final String START_ADDRESS = "work address";
    public static final String END_ADDRESS = "home address";
    public static final String COMMUTE_DAY = "user info object";
    private static String[] mRowProjection =
            {
                    UserScheduleContract.USER_END_ADDRESS,
                    UserScheduleContract.USER_ITEM_ACTIVE,
                    UserScheduleContract.USER_START_ADDRESS,
                    UserScheduleContract.USER_START_TIME,
                    UserScheduleContract.USER_WORKDAY,
                    UserScheduleContract._ID
            };


    public CommuteCheckAlarmService(String name) {
        super(TAG);

    }

    @Override
    public void onCreate() {
        registerReceiver(mNewDayReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    public void onDestroy() {
        try {
            unregisterReceiver(mNewDayReceiver);
        } catch (IllegalArgumentException ex) {
            if (ex.getMessage().contains("Receiver not registered")) {
                //apparently this is a known bug, but our goal of unregistering the receiver still happens

            } else {
                throw ex;
            }
        }
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "MapQueryService is handing intent: " + intent);

        String startAddress = intent.getStringExtra(START_ADDRESS);
        String endAddress = intent.getStringExtra(END_ADDRESS);

        Geocoder startGeocoder = new Geocoder(this, Locale.getDefault());
        Geocoder endGeocoder = new Geocoder(this, Locale.getDefault());

        double startLatitude = 0.0;
        double startLongitude = 0.0;
        double endLatitude = 0.0;
        double endLongitude = 0.0;

        try {
            List<Address> startGeocoderAddress = startGeocoder
                    .getFromLocationName(startAddress, 1);
            startLatitude = startGeocoderAddress.get(0).getLatitude();
            startLongitude = startGeocoderAddress.get(0).getLongitude();

            List<Address> endGeocoderAddress = endGeocoder.getFromLocationName(
                    endAddress, 1);
            endLatitude = endGeocoderAddress.get(0).getLatitude();
            endLongitude = endGeocoderAddress.get(0).getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Uri startAndEndUri = Uri.parse("http://maps.google.com/maps?saddr="
                + startLatitude + "," + startLongitude + "&daddr="
                + endLatitude + "," + endLongitude);

        Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW,
                startAndEndUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        PendingIntent notificationIntent = PendingIntent.getActivity(this, 0,
                mapIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setTicker("Time to check your commute!")
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("CommuteWatcher")
                .setContentText(
                        "It's time to check your commute! Tap this notification to start.")
                .setContentIntent(notificationIntent).setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, notification);

    }

    public CommuteCheckAlarmService() {
        super(TAG);
    }

    //TODO: add boolean to each userday for active/inactive


    @SuppressWarnings("static-access")
    public static void setServiceAlarm(Context context, boolean isOn) {
        Toast.makeText(context, "Service checked!", Toast.LENGTH_SHORT).show();
        //these two arraylists are for holding pendingintents to load into the alarm manager, and an intent list to build the pending list from.
        PendingIntent alarmPendingIntent = null;
        Intent alarmIntent;


//		today = (GregorianCalendar) Calendar
//				.getInstance();
        //ArrayList<UserDayItem> userDayItems = userInfo.getActiveDayItemList();


        Log.i(TAG, "setService alarm is up is called, boolean is " + isOn);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        long comparisonStartTime = LocalTime.now().getMillisOfDay();

        String mRowSelectionClause = UserScheduleContract.USER_START_TIME + "> " + comparisonStartTime;

        if (isOn) {
            Cursor cursor = context.getContentResolver().query(
                    UserScheduleContract.CONTENT_URI,   // The content URI of the sched table
                    mRowProjection,                        // The columns to return for each row
                    null,                    // Selection criteria
                    null,                     // Selection criteria
                    UserScheduleContract.USER_START_TIME + " ASC");

            String endAddress = null;
            int isActive;
            String startAddress = null;
            long startTime = 0;
            UserDayItem userDayItem = new UserDayItem(context);

            int workDayInt;
            int rowId;

            while (cursor.moveToNext()) {
                Log.e("IntentServiceHere", "movetofirst");
                startTime = cursor.getLong(cursor.getColumnIndex(UserScheduleContract.USER_START_TIME));
                int activeBoolAsNum = cursor.getInt(cursor.getColumnIndex(UserScheduleContract.USER_ITEM_ACTIVE));
                int dayOfWeek = GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK);
                if (startTime > comparisonStartTime
                        && 1 == activeBoolAsNum
                        && dayOfWeek == cursor.getInt(cursor.getColumnIndex(UserScheduleContract.USER_WORKDAY))) {
                    // Extract the data from the Cursor, we need to look for the first row that is later than the current time
                    endAddress = cursor.getString(cursor.getColumnIndex(UserScheduleContract.USER_END_ADDRESS));
                    isActive = cursor.getInt(cursor.getColumnIndex(UserScheduleContract.USER_ITEM_ACTIVE));
                    startAddress = cursor.getString(cursor.getColumnIndex(UserScheduleContract.USER_START_ADDRESS));
                    startTime = cursor.getLong(cursor.getColumnIndex(UserScheduleContract.USER_START_TIME));
                    workDayInt = cursor.getInt(cursor.getColumnIndex(UserScheduleContract.USER_WORKDAY));
                    rowId = cursor.getInt(cursor.getColumnIndex(UserScheduleContract._ID));
                    userDayItem.setWorkDay(workDayInt);
                    userDayItem.setActive(true);
                    userDayItem.setStartCommuteTime(startTime);
                    userDayItem.setHomeAddress(startAddress);
                    userDayItem.setWorkAddress(endAddress);
                    break;
                } else {
                    startAddress = "";
                    endAddress = "";
                    startTime = 0;
                }


            }

            cursor.close();

            //this loop is for grabbing all of the active UserDayItems (each item has a point a, b, start time, and a boolean for if it's active or not)
            //and will then create an intent for them, add the item as an extra, then add the intent to an arraylist and make a pending intent as well, and throw
            //that into an arraylist for pendingintents. I want that pendingList so that I have an easily-managed reference for cancelling alarms later.


            if (startTime > 0) {
                Intent commuteIntent = new Intent(context,
                        CommuteCheckAlarmService.class);
                Bundle bundle = new Bundle();
                //bundle.putSerializable(Comm);
                commuteIntent.putExtra(COMMUTE_DAY, userDayItem);

                alarmIntent = new Intent(context, CommuteCheckAlarmService.class);
                alarmPendingIntent = PendingIntent.getService(context, 1, commuteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            }




            if (alarmPendingIntent != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, startTime, alarmPendingIntent);
            }


        }

        //if the alarm service is turned off, all PIs should be cancelled.
        else {

            alarmManager.cancel(alarmPendingIntent);
            alarmPendingIntent.cancel();
        }


        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(PREF_IS_ALARM_ON, isOn).commit();
    }


	public static boolean isServiceAlarmOn(Context context)
	{
		Intent i = new Intent(context.getApplicationContext(),
				CommuteCheckAlarmService.class);
		PendingIntent pi = PendingIntent.getService(context, 0, i,
				PendingIntent.FLAG_NO_CREATE);
		return pi != null;
	}

	private final NewDayReceiver mNewDayReceiver = new NewDayReceiver();

	public class NewDayReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(final Context context, Intent intent)
        {
            String intentAction = intent.getAction();
            setServiceAlarm(context, isServiceAlarmOn(context));
        }
    }
}
