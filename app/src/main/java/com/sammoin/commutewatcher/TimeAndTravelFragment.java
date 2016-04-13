package com.sammoin.commutewatcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimeAndTravelFragment extends Fragment
{
	private Button workCommuteTimeButton;
	private Button homeCommuteTimeButton;
	private TextView displayWorkCommute;
	private TextView displayHomeCommute;
	private Button saveCommuteInfoButton;
	private EditText editWorkAddressText;
	private EditText editHomeAddressText;
	static final String USER_INFO_FILE = "user_info.txt";
	private UserDayItem savedUserInfo;
	private UserDayItem userDayItem;
	private int selectedDay = -1;

	public static final String DIALOG_WORK_COMMUTE = "to work";
	public static final String DIALOG_HOME_COMMUTE = "to home";
	public static final String MAIN_MENU = "main menu";
	public static final int REQUEST_WORK_COMMUTE = 0;
	public static final int REQUEST_HOME_COMMUTE = 1;
	public static final String DAY_LIST_ITEM = "com.sammoin.commutewatcher.user";
	public static final String WORKDAY_POSITION = "com.sammoin.commutewatcher.position";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
		Bundle extras;
		extras = getActivity().getIntent().getBundleExtra(WorkWeekListFragment.LIST_BUNDLE);

		userDayItem = (UserDayItem) extras.get(DAY_LIST_ITEM);
		if (extras.containsKey(WORKDAY_POSITION))
		{
			
		selectedDay = extras.getInt(WORKDAY_POSITION);
		Log.i("TIMEANDTRAVEL", "selectedDay " + selectedDay + " start address " + userDayItem.getHomeAddress() + " end address " + userDayItem.getWorkAddress());
		}
	}	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_main, container, false);

		displayWorkCommute = (TextView) v
				.findViewById(R.id.userWorkDateTimeTextView);
		editWorkAddressText = (EditText) v
				.findViewById(R.id.editWorkAddressTextView);
		
		displayHomeCommute = (TextView) v
				.findViewById(R.id.userHomeTimeTextView);
		
		editHomeAddressText = (EditText) v
				.findViewById(R.id.editHomeAddressTextView);
		
		
		
			
				
				SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa ZZZZ",
						Locale.getDefault());

				displayWorkCommute.setText((sdf.format(userDayItem
						.getStartCommuteTime().getTime())));
				/*
				displayHomeCommute.setText((sdf.format(userDayItem
						.getDriveToHomeTime().getTime())));
				*/
				Log.i("TIMEANDTRAVEL", "try loop start address " + userDayItem.getHomeAddress() + " end address " + userDayItem.getWorkAddress());
				
				editWorkAddressText.setText(userDayItem.getWorkAddress());
				editHomeAddressText.setText(userDayItem.getHomeAddress());
				
		
		
		
		workCommuteTimeButton = (Button) v
				.findViewById(R.id.enterWorkDateTimeButton);
		workCommuteTimeButton.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				FragmentManager fm = getActivity().getSupportFragmentManager();
				WorkCommuteDayAndTimeFragment dialog = WorkCommuteDayAndTimeFragment
						.newInstance(userDayItem);
				dialog.setTargetFragment(TimeAndTravelFragment.this,
						REQUEST_WORK_COMMUTE);
				dialog.show(fm, DIALOG_WORK_COMMUTE);

			}
		});
		
		/*
		homeCommuteTimeButton = (Button) v
				.findViewById(R.id.enterHomeTimeButton);
		homeCommuteTimeButton.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				FragmentManager fm = getActivity().getSupportFragmentManager();
				HomeCommuteDayAndTimeFragment dialog = HomeCommuteDayAndTimeFragment
						.newInstance(userDayItem);
				dialog.setTargetFragment(TimeAndTravelFragment.this,
						REQUEST_HOME_COMMUTE);
				dialog.show(fm, DIALOG_HOME_COMMUTE);

			}
		});
		*/
		saveCommuteInfoButton = (Button) v
				.findViewById(R.id.saveCommuteInfoButton);
		saveCommuteInfoButton.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				userDayItem.setHomeAddress(editHomeAddressText.getText().toString());
				userDayItem.setWorkAddress(editWorkAddressText.getText().toString());
				
				
				
				Intent i = new Intent();
				if (selectedDay > -1)
				{
				i.putExtra(WORKDAY_POSITION, selectedDay);
				}
				i.putExtra(DAY_LIST_ITEM, userDayItem);
				getActivity().setResult(getActivity().RESULT_OK, i);
				getActivity().finish();

			}
		});

		return v;
	}

	
	
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		if (resultCode != Activity.RESULT_OK)
		{
			return;
		}

		else
		{
			UserDayItem tempUser;
			tempUser = ((UserDayItem) data.getSerializableExtra(WorkCommuteDayAndTimeFragment.EXTRA_USER_DATA));

			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa ZZZZ",
					Locale.getDefault());

			if (requestCode == REQUEST_WORK_COMMUTE)
			{
				if (tempUser.getStartCommuteTime().toString() != null)
				{
					if (tempUser.getWorkDay() != null)
					{
						userDayItem.setWorkDay(tempUser.getWorkDay());
					}
					userDayItem.setStartCommuteTime(tempUser.getStartCommuteTime());
					displayWorkCommute.setText(sdf.format(userDayItem
							.getStartCommuteTime().getTime()));

				}
			}
				
			/*
			if (requestCode == REQUEST_HOME_COMMUTE)
			{
				if (tempUser.getDriveToHomeTime().toString() != null)
				{
					userDayItem.setDriveToHomeTime(tempUser.getDriveToHomeTime());
					displayHomeCommute.setText(sdf.format(userDayItem
							.getDriveToHomeTime().getTime()));

				}
			}
			*/
		}

	}

	/*
	public boolean loadSavedInfo(String filename)
			throws StreamCorruptedException, IOException,
			ClassNotFoundException
	{

		savedUserInfo = new UserDayItem();
		ObjectInputStream file;

		try
		{
			file = new ObjectInputStream(new FileInputStream(new File(new File(
					getActivity().getApplicationContext().getFilesDir(), "")
					+ File.separator + USER_INFO_FILE)));
			savedUserInfo.copyUserData((UserDayItem) file.readObject());

			file.close();
			return true;
		} 
		catch (FileNotFoundException e)
		{

			e.printStackTrace();
		}

		return false;

	}

	public void saveInfo(UserDayItem user) throws IOException
	{

		ObjectOutput output = null;

		if (savedUserInfo == null)
		{
			savedUserInfo = new UserDayItem();
		}
		savedUserInfo.copyUserData(user);

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
	*/
	

}
