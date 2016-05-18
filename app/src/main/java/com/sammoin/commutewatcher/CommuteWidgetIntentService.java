package com.sammoin.commutewatcher;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by DoctorMondo on 5/16/2016.
 */
public class CommuteWidgetIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     //* @param name Used to name the worker thread, important only for debugging.
     */






    private String[] mRowProjection =
            {
                    UserScheduleContract.USER_END_ADDRESS,
                    UserScheduleContract.USER_ITEM_ACTIVE,
                    UserScheduleContract.USER_START_ADDRESS,
                    UserScheduleContract.USER_START_TIME,
                    UserScheduleContract.USER_WORKDAY,
                    UserScheduleContract._ID
            };

    public CommuteWidgetIntentService() {
        super("CommuteWidgetIntentService");
    }

    public CommuteWidgetIntentService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("IntentServiceHere", "IntentService onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                CommuteWidgetProvider.class));
        Context context = getApplicationContext();
        // Get data from the ContentProvider
        Log.e("IntentServiceHere", "IntentService OnHandleIntent");
        Configuration config = context.getResources().getConfiguration();
        //sql query to get score
        String dateFormat = "yyyy-MM-dd";
        String timeFormat = "HH:mm EEEE";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
        SimpleDateFormat simpleTime = new SimpleDateFormat(timeFormat, Locale.US);
        String formattedDate = simpleDateFormat.format(new Date());
        String formattedTime = simpleTime.format(new Date());

        int dayInt = DateTime.now().getDayOfWeek();

        long comparisonStartTime = DateTime.now().getMillis();
        LocalTime comparisonLocalTime = LocalTime.now();
        String mRowSelectionClause = UserScheduleContract.USER_START_TIME +">= "+ comparisonStartTime;

        Cursor cursor=getContentResolver().query(
                UserScheduleContract.CONTENT_URI,   // The content URI of the sched table
                mRowProjection,                        // The columns to return for each row
                null,                    // Selection criteria
                null,                     // Selection criteria
                UserScheduleContract.USER_START_TIME + " ASC");

        String endAddress = null;
        int isActive;
        String startAddress = null;
        LocalTime startTime = null;


        int workDayInt;
        int rowId;

        while (cursor.moveToNext()) {
            Log.e("IntentServiceHere", "movetofirst");
            startTime = LocalTime.fromMillisOfDay( cursor.getLong(cursor.getColumnIndex(UserScheduleContract.USER_START_TIME)));
            if (startTime.isAfter( comparisonLocalTime)
                    && 1==cursor.getInt(cursor.getColumnIndex(UserScheduleContract.USER_ITEM_ACTIVE))
                    && DateTime.now().getDayOfWeek()==cursor.getInt(cursor.getColumnIndex(UserScheduleContract.USER_WORKDAY)))
            {
                // Extract the data from the Cursor, we need to look for the first row that is later than the current time
                endAddress = cursor.getString(cursor.getColumnIndex(UserScheduleContract.USER_END_ADDRESS));
                isActive = cursor.getInt(cursor.getColumnIndex(UserScheduleContract.USER_ITEM_ACTIVE));
                startAddress = cursor.getString(cursor.getColumnIndex(UserScheduleContract.USER_START_ADDRESS));
                //startTime = cursor.getLong(cursor.getColumnIndex(UserScheduleContract.USER_START_TIME));
                workDayInt = cursor.getInt(cursor.getColumnIndex(UserScheduleContract.USER_WORKDAY));
                rowId = cursor.getInt(cursor.getColumnIndex(UserScheduleContract._ID));
                break;
            }
            else
            {
                startAddress="";
                endAddress="";
                startTime=null;
            }


        }






        cursor.close();


        // Perform this loop procedure for each Today widget
        for (int appWidgetId : appWidgetIds) {
            Log.i("WidgetIntentService", "widget ID " + appWidgetId + "and layout ID is " + R.layout.widget_layout);
            // Find the correct layout based on the widget's width
            int defaultWidth = getResources().getDimensionPixelSize(R.dimen.widget_today_large_width);

            int layoutId= R.layout.widget_layout;

            RemoteViews views = new RemoteViews(getPackageName(), layoutId);



            // Content Descriptions for RemoteViews were only added in ICS MR1



           if (startTime!=null)
           {
               views.setTextViewText(R.id.widget_start_time_textview, getString(R.string.start_time) + " " + startTime.toString("hh:mm aa", Locale.getDefault()));
               views.setTextViewText(R.id.widget_start_point_textview, getString(R.string.start_point) + ": " +startAddress);
               views.setTextViewText(R.id.widget_end_point_textview, getString(R.string.end_point) + ": " + endAddress);
           }
            else
           {
               views.setTextViewText(R.id.widget_start_time_textview, getString(R.string.no_remaining_commutes_today));
               views.setTextViewText(R.id.widget_start_point_textview, startAddress);
               views.setTextViewText(R.id.widget_end_point_textview, endAddress);

           }








            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, views.getLayoutId());
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
        views.setContentDescription(R.id.widget, description);
    }
}

