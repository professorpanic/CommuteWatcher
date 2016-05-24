package com.sammoin.commutewatcher;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.joda.time.LocalTime;
import org.joda.time.Seconds;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.net.ssl.HttpsURLConnection;


public class CommuteCheckAlarmService extends IntentService {
    public static final String TAG = "MapQueryService";
    public static final String PREF_IS_ALARM_ON = "isServiceAlarmOn";
    public static final String START_ADDRESS = "work address";
    public static final String END_ADDRESS = "home address";
    public static final String COMMUTE_DAY = "user info object";
    String startAddress;
    String endAddress;
    double startLatitude = 0.0;
    double startLongitude = 0.0;
    double endLatitude = 0.0;
    double endLongitude = 0.0;

    Uri startAndEndUri;

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

    void onTaskCompleted(boolean isSuccess){

    }

    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    protected void onHandleIntent(Intent intent) {
        //better to keep this as an intentservice due to the one-off nature of the alarms.
        UserDayItem userDayItem = (UserDayItem)intent.getSerializableExtra(COMMUTE_DAY);
        startAddress = userDayItem.getHomeAddress();
        endAddress = userDayItem.getWorkAddress();

        String formattedStartAddress = startAddress.replace(" ", "+");
        String formattedEndAddress = endAddress.replace(" ", "+");
        String duration = "";
        String distance = "";
        String timeOfShortestTripWithoutTraffic =getApproxTimeByURL(formattedStartAddress, formattedEndAddress);
        String startResponse = getLatLongByURL("http://maps.google.com/maps/api/geocode/json?address=" + formattedStartAddress + "&sensor=false");
        String endResponse = getLatLongByURL("http://maps.google.com/maps/api/geocode/json?address="+formattedEndAddress + "&sensor=false");


        startLatitude = 0.0;
        startLongitude = 0.0;
        endLatitude = 0.0;
        endLongitude = 0.0;


        JSONObject jsonStartObject;
        JSONObject jsonEndObject;
        JSONObject jsonTravelTimeObject;
        try {
            jsonStartObject = new JSONObject(startResponse);
            jsonEndObject = new JSONObject(endResponse);
            jsonTravelTimeObject = new JSONObject(timeOfShortestTripWithoutTraffic);

            endLongitude = ((JSONArray)jsonEndObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            endLatitude = ((JSONArray)jsonEndObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            startLongitude = ((JSONArray)jsonStartObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            startLatitude = ((JSONArray)jsonStartObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");



            duration = getApproxTripDuration(jsonTravelTimeObject);
            distance = getApproxTripDistance(jsonTravelTimeObject);

        } catch (JSONException e) {
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
        setServiceAlarm(getApplicationContext(), true); //this will queue up the next alarm. The intent won't actually be sent unless the alarm service is already on, so no worries about loops.
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(getString(R.string.upcoming_info_avail))
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle(getString(R.string.upcoming_info_avail))
                .setContentText( getString(R.string.distance) + distance + ", " + getString(R.string.duration)+duration)
                .setContentIntent(notificationIntent).setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, notification);



    }

    public CommuteCheckAlarmService() {
        super(TAG);
    }




    @SuppressWarnings("static-access")
    public static void setServiceAlarm(Context context, boolean isOn) {


        PendingIntent alarmPendingIntent = null;
        Intent alarmIntent;


        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        long comparisonStartTime = LocalTime.now().getMillisOfDay();


        //we're essentially just grabbing the next commute that's active,
        // building a userdayitem with all of the relevant info, and loading into an intent and then
        //putting it into a pendingintent
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
                        && 1 == activeBoolAsNum) {
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



            if (startTime > 0) {
                Intent commuteIntent = new Intent(context,
                        CommuteCheckAlarmService.class);
                Bundle bundle = new Bundle();

                commuteIntent.putExtra(COMMUTE_DAY, userDayItem);


                alarmIntent = new Intent(context, CommuteCheckAlarmService.class);
                alarmIntent.putExtra(COMMUTE_DAY, userDayItem);
                alarmPendingIntent = PendingIntent.getService(context, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            }




            if (alarmPendingIntent != null) {

                LocalTime now = new LocalTime();
                Seconds secondsBetween = Seconds.secondsBetween(now, LocalTime.fromMillisOfDay(startTime));

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, secondsBetween.getSeconds());
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmPendingIntent);
            }


        }

        //if the alarm service is turned off, all PIs should be cancelled.
        else
        { if (alarmPendingIntent != null) {
            alarmManager.cancel(alarmPendingIntent);
            alarmPendingIntent.cancel();
        }
        }



    }


	public static boolean isServiceAlarmOn(Context context)
	{
		Intent i = new Intent(context,
				CommuteCheckAlarmService.class);
		PendingIntent pi = PendingIntent.getService(context, 0, i,
				PendingIntent.FLAG_NO_CREATE);
		return pi != null;
	}





    //found this method and above class on stackoverflow
    //http://stackoverflow.com/questions/15711499/get-latitude-and-longitude-with-geocoder-and-android-google-maps-api-v2
    public String getLatLongByURL(String requestURL) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getApproxTripDuration(JSONObject json)
    {
        JSONArray rowsArray = null;
        String duration = "";
        try {
            rowsArray = json.getJSONArray("rows");

        JSONObject rowsObject = rowsArray.getJSONObject(0);//should be only 1 in this array
        JSONArray elementsArray = rowsObject.getJSONArray("elements");
        JSONObject elementsObject = elementsArray.getJSONObject(0);//should be only 1 in this array
        JSONObject durationObject = elementsObject.getJSONObject("duration");
        duration= durationObject.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  duration;
    }

    public String getApproxTripDistance(JSONObject json)
    {
        JSONArray rowsArray = null;
        String distance = "";
        try {
            rowsArray = json.getJSONArray("rows");

            JSONObject rowsObject = rowsArray.getJSONObject(0);//should be only 1 in this array
            JSONArray elementsArray = rowsObject.getJSONArray("elements");
            JSONObject elementsObject = elementsArray.getJSONObject(0);//should be only 1 in this array
            JSONObject distanceObject = elementsObject.getJSONObject("distance");
            distance= distanceObject.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  distance;
    }

    public String getApproxTimeByURL(String startPoint, String endPoint) {
        URL url;
        String response = "";
        try {

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("maps.googleapis.com")
                    .appendPath("maps")
                    .appendPath("api")
                    .appendPath("distancematrix")
                    .appendPath("json")
                    .appendQueryParameter("units", "imperial")
                    .appendQueryParameter("origins", startPoint)
                    .appendQueryParameter("destinations", endPoint)
                    .appendQueryParameter("key", getString(R.string.google_maps_browser_key));
            url = new URL(builder.build().toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
