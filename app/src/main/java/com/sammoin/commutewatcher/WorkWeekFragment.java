package com.sammoin.commutewatcher;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

//import android.support.v7.internal.widget.AdapterViewCompat.AdapterContextMenuInfo;

public class WorkWeekFragment extends Fragment
{
private UserWeek mWorkWeek;

private Uri mNewUri;

public static final int REQUEST_POSITION = 3;
public static final int REQUEST_NEW_COMMUTE = 4;
public static final String LIST_BUNDLE = "com.sammoin.commutewatcher.bundle";
static final String USER_INFO_FILE = "CommuteWatcher_user_info.txt";
private UserWeek savedUserInfo;
PassDayFromWeekListener mCallback;
private View view;
TextView sundayTextView;
TextView mondayTextView;
TextView tuesdayTextView;
TextView wednesdayTextView;
TextView thursdayTextView;
TextView fridayTextView;
TextView saturdayTextView;



    public interface PassDayFromWeekListener {
        public void passDayFromWeek(Bundle bundle);
    }

	public WorkWeekFragment()
	{
		// TODO Auto-generated constructor stub
	}

    public void addDayToWeek(UserDay userDay)
    {
//        mWorkWeek.set(userDay);
//        adapter.notifyDataSetInvalidated();
    }


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);

        if (mWorkWeek != null) {
            mWorkWeek.setContext(getActivity().getApplicationContext());
        }
        else
        {
            mWorkWeek = new UserWeek();
            mWorkWeek.setContext(getActivity().getApplicationContext());
        }
		System.out.println(mWorkWeek);
		mCallback = (PassDayFromWeekListener)getActivity();
		Log.i("ON CREATE UPDATE", " " + mWorkWeek);
		//UserDayItem test = mWorkWeek.get(6);
		//test.setWorkDay(Day.MONDAY);

        UserDayItem udi = new UserDayItem();




//        ContentValues values = new ContentValues();
//
//        values.put(UserScheduleContract.USER_START_ADDRESS, "start test");
//        values.put(UserScheduleContract.USER_END_ADDRESS, "end test");
//        values.put(UserScheduleContract.USER_WORKDAY, 2);
//        values.put(UserScheduleContract.USER_START_TIME, udi.getStartCommuteTime().getTimeInMillis());
//        values.put(UserScheduleContract.USER_ITEM_ACTIVE, 1);
//
//        mNewUri = getActivity().getContentResolver().insert(
//                UserScheduleContract.CONTENT_URI,
//                values);
//        values.put(UserScheduleContract.USER_START_ADDRESS, "start test");
//        values.put(UserScheduleContract.USER_END_ADDRESS, "end test");
//        values.put(UserScheduleContract.USER_WORKDAY, 3);
//        values.put(UserScheduleContract.USER_START_TIME, udi.getStartCommuteTime().getTimeInMillis());
//        values.put(UserScheduleContract.USER_ITEM_ACTIVE, 1);
//        mNewUri = getActivity().getContentResolver().insert(
//                UserScheduleContract.CONTENT_URI,
//                values);
//
//        values.put(UserScheduleContract.USER_START_ADDRESS, "start test");
//        values.put(UserScheduleContract.USER_END_ADDRESS, "end test");
//        values.put(UserScheduleContract.USER_WORKDAY, 4);
//        values.put(UserScheduleContract.USER_START_TIME, udi.getStartCommuteTime().getTimeInMillis());
//        values.put(UserScheduleContract.USER_ITEM_ACTIVE, 1);
//        mNewUri = getActivity().getContentResolver().insert(
//                UserScheduleContract.CONTENT_URI,
//                values);
//
//        values.put(UserScheduleContract.USER_START_ADDRESS, "start test");
//        values.put(UserScheduleContract.USER_END_ADDRESS, "end test");
//        values.put(UserScheduleContract.USER_WORKDAY, 5);
//        values.put(UserScheduleContract.USER_START_TIME, udi.getStartCommuteTime().getTimeInMillis());
//        values.put(UserScheduleContract.USER_ITEM_ACTIVE, 1);
//        mNewUri = getActivity().getContentResolver().insert(
//                UserScheduleContract.CONTENT_URI,
//                values);
//
//        values.put(UserScheduleContract.USER_START_ADDRESS, "start test");
//        values.put(UserScheduleContract.USER_END_ADDRESS, "end test");
//        values.put(UserScheduleContract.USER_WORKDAY, 1);
//        values.put(UserScheduleContract.USER_START_TIME, udi.getStartCommuteTime().getTimeInMillis());
//        values.put(UserScheduleContract.USER_ITEM_ACTIVE, 1);
//
//
//        mNewUri = getActivity().getContentResolver().insert(
//                UserScheduleContract.CONTENT_URI,
//                values);
        //getActivity().getContentResolver().insert(UserScheduleContract.CONTENT_URI, values);

        Cursor testCursor = getActivity().getContentResolver().query(UserScheduleContract.CONTENT_URI, null, null, null, null);
        testCursor.moveToFirst();
        Log.e("WORKWEEKCURSORTEST", "index 0 " + testCursor.getString(0));
        Log.e("WORKWEEKCURSORTEST", "index 1 " + testCursor.getString(1));
        Log.e("WORKWEEKCURSORTEST", "index 2 " + testCursor.getString(2));
        Log.e("WORKWEEKCURSORTEST", "index 3 " + testCursor.getString(3));
        Log.e("WORKWEEKCURSORTEST", "index 4 " + testCursor.getString(4));
        Log.e("WORKWEEKCURSORTEST", "index 5 " + testCursor.getString(5));
        Log.e("WORKWEEKCURSORTEST", "column names " + testCursor.getColumnNames().toString());
        Log.e("WORKWEEKCURSORTEST", "index 5 " + testCursor.toString());
        testCursor.close();
		//setListAdapter(adapter);
        try {
            loadSavedInfo(USER_INFO_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        updateDayView();
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.workweeklayout_nonlist, container, false);

        registerForContextMenu(view);
        sundayTextView = (TextView) view.findViewById(R.id.su_trips_TextView);
        mondayTextView = (TextView) view.findViewById(R.id.mo_trips_TextView);
        tuesdayTextView = (TextView) view.findViewById(R.id.tu_trips_TextView);
        wednesdayTextView = (TextView) view.findViewById(R.id.we_trips_TextView);
        thursdayTextView = (TextView) view.findViewById(R.id.th_trips_TextView);
        fridayTextView = (TextView) view.findViewById(R.id.fr_trips_TextView);
        saturdayTextView = (TextView) view.findViewById(R.id.sa_trips_TextView);
        updateDayView();

        Bundle extras = new Bundle();



        //mCallback.passDayFromWeek(extras);
        //startActivityForResult(i, REQUEST_POSITION);

        LinearLayout sundayBar = (LinearLayout) view.findViewById(R.id.su_layout);
        sundayBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            proceedToWorkDayList(1);
            }
        });

        LinearLayout mondayBar = (LinearLayout) view.findViewById(R.id.mo_layout);
        mondayBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedToWorkDayList(2);
            }
        });

        LinearLayout tuesdayBar = (LinearLayout) view.findViewById(R.id.tu_layout);
        tuesdayBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedToWorkDayList(3);
            }
        });

        LinearLayout wednesdayBar = (LinearLayout) view.findViewById(R.id.we_layout);
        wednesdayBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedToWorkDayList(4);
            }
        });

        LinearLayout thursdayBar = (LinearLayout) view.findViewById(R.id.th_layout);
        thursdayBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedToWorkDayList(5);
            }
        });

        LinearLayout fridayBar = (LinearLayout) view.findViewById(R.id.fr_layout);
        fridayBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedToWorkDayList(6);
            }
        });

        LinearLayout saturdayBar = (LinearLayout) view.findViewById(R.id.sa_layout);
        saturdayBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedToWorkDayList(7);
            }
        });

        return view;
    }
	
	

	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
//		if (resultCode==getActivity().RESULT_OK)
//		{
//			if (requestCode==WorkWeekFragment.REQUEST_POSITION)
//			{
//			//mWorkWeek.set(data.getIntExtra(TimeAndTravelFragment.WORKDAY_POSITION, 0), (UserDayItem) data.getSerializableExtra(TimeAndTravelFragment.DAY_LIST_ITEM));
//			}
//
//			else if (requestCode==WorkWeekFragment.REQUEST_NEW_COMMUTE)
//			{
//				//mWorkWeek.add((UserDayItem) data.getSerializableExtra(TimeAndTravelFragment.DAY_LIST_ITEM));
//			}
//
//		}

		Log.e("WORKWEEKLISTFRAGMENT", "in onActivityResult. Do I even need to override this now?");
		//setListAdapter(adapter);
		
	}

	public boolean loadSavedInfo(String filename)
			throws StreamCorruptedException, IOException,
			ClassNotFoundException
	{

		savedUserInfo = new UserWeek();
		ObjectInputStream file;

		try
		{
			file = new ObjectInputStream(new FileInputStream(new File(new File(
					getActivity().getApplicationContext().getFilesDir(), "")
					+ File.separator + filename)));


			savedUserInfo =(UserWeek) file.readObject();
			mWorkWeek.copy(savedUserInfo);
			file.close();
			return true;
		}
		catch (FileNotFoundException e)
		{

			e.printStackTrace();
		}

		return false;

	}

	public void saveInfo() throws IOException
	{

		ObjectOutput output = null;

		if (savedUserInfo == null)
		{
			savedUserInfo = new UserWeek();
		}
		savedUserInfo.copy(mWorkWeek);

		CommuteCheckAlarmService.setServiceAlarm(getActivity(),
                CommuteCheckAlarmService.isServiceAlarmOn(getActivity()),
				savedUserInfo);

		try
		{
			output = new ObjectOutputStream(new FileOutputStream(new File(
					getActivity().getApplicationContext().getFilesDir(), "")
					+ File.separator + USER_INFO_FILE));
			output.writeObject(savedUserInfo);
			output.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{

		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main_actions, menu);

	}

	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);

		MenuItem toggleItem = menu.findItem(R.id.action_alarm_toggle);
		getActivity().invalidateOptionsMenu();

		if (CommuteCheckAlarmService.isServiceAlarmOn(getActivity()))
		{
			toggleItem.setTitle(R.string.action_turn_checker_off);

		} 
		else
		{
			toggleItem.setTitle(R.string.action_turn_checker_on);

		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId())
		{

		case R.id.action_settings:
			return true;

		case R.id.action_alarm_toggle:
		
			boolean turnAlarmOn = !CommuteCheckAlarmService
					.isServiceAlarmOn(getActivity());
			try {
				CommuteCheckAlarmService.setServiceAlarm(getActivity(),
                        turnAlarmOn);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			Log.i("WORKWEEKLISTFRAGMENT", "alarm on has been clicked "+ turnAlarmOn + " and user object is " + savedUserInfo.toString());
			honeyCombOptionsInvalidate();
			return true;
		
		case R.id.action_save_all:
			Log.i("WORKWEEKLISTFRAGMENT", "save all");

			try {
				saveInfo();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		
		case R.id.action_delete_all:
			Log.i("WORKWEEKLISTFRAGMENT", "delete");
			clearAllRecords();
            updateDayView();

			return true;
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.commute_list_item_context, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		int position = info.position;

		switch (item.getItemId())
		{
		case R.id.menu_item_delete_commute:



			return true;
			
		}
		
		
		return super.onContextItemSelected(item);
	}

	
	public void honeyCombOptionsInvalidate()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{

			getActivity().invalidateOptionsMenu();
		}
	}

    public void clearAllRecords() {
        getActivity().getContentResolver().delete(UserScheduleContract.CONTENT_URI, "", null);

    }

    public void proceedToWorkDayList(int dayInt)
    {
        Bundle extras = new Bundle();
        extras.putInt(WorkDayListFragment.USER_DAY_POSITION, dayInt);
        mCallback.passDayFromWeek(extras);
        //startActivityForResult(i, REQUEST_POSITION);

    }

    public void updateDayView() {
        if (view != null) {
            Cursor testCursor = getActivity().getContentResolver().query(UserScheduleContract.CONTENT_URI, null, null, null, null);


            int sunNum = 0;
            int sunAct = 0;

            int monNum = 0;
            int monAct = 0;

            int tueNum = 0;
            int tueAct = 0;

            int wedNum = 0;
            int wedAct = 0;

            int thurNum = 0;
            int thurAct = 0;

            int friNum = 0;
            int friAct = 0;

            int satNum = 0;
            int satAct = 0;

            int indexWorkday = testCursor.getColumnIndex(UserScheduleContract.USER_WORKDAY);
            int indexActive = testCursor.getColumnIndex(UserScheduleContract.USER_ITEM_ACTIVE);

            while (testCursor.moveToNext()) {
                int day = testCursor.getInt(indexWorkday);
                int active = testCursor.getInt(indexActive);

                switch (day) {
                    case 1: {
                        sunNum++;
                        sunAct = sunAct + active;
                        break;
                    }
                    case 2: {
                        monNum++;
                        monAct = monAct + active;
                        break;
                    }
                    case 3: {
                        tueNum++;
                        tueAct = tueAct + active;
                        break;
                    }
                    case 4: {
                        wedNum++;
                        wedAct = wedAct + active;
                        break;
                    }
                    case 5: {
                        thurNum++;
                        thurAct = thurAct + active;
                        break;
                    }
                    case 6: {
                        friNum++;
                        friAct = friAct + active;
                        break;
                    }
                    case 7: {
                        satNum++;
                        satAct = satAct + active;
                        break;

                    }
                    default:
                        break;


                }
                Log.e("WORKWEEKCURSORTEST", "test cursor loop, rowNum is " + sunNum);




            }
            testCursor.close();
            sundayTextView.setText("Trips: " + sunNum + ", " + sunAct + " active");
            mondayTextView.setText("Trips: " + monNum + ", " + monAct + " active");
            tuesdayTextView.setText("Trips: " + tueNum + ", " + tueAct + " active");
            wednesdayTextView.setText("Trips: " + wedNum + ", " + wedAct + " active");
            thursdayTextView.setText("Trips: " + thurNum + ", " + thurAct + " active");
            fridayTextView.setText("Trips: " + friNum + ", " + friAct + " active");
            saturdayTextView.setText("Trips: " + satNum + ", " + satAct + " active");
        }
    }
}
