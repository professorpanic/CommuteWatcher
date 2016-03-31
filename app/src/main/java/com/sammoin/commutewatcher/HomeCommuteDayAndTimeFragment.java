package com.sammoin.commutewatcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class HomeCommuteDayAndTimeFragment extends DialogFragment
{
	public static final String EXTRA_USER_DATA = "com.sammoin.commutewatcher.user";
	public static final String HOME_TIME_DIALOG = "home time dialog fragment";

	private GregorianCalendar homeCommuteStartTime = new GregorianCalendar();
	private UserDayItem userDayItem = new UserDayItem();

	public HomeCommuteDayAndTimeFragment()
	{
		// TODO Auto-generated constructor stub
	}

	public static HomeCommuteDayAndTimeFragment newInstance(UserDayItem user)
	{
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_USER_DATA, user);

		HomeCommuteDayAndTimeFragment fragment = new HomeCommuteDayAndTimeFragment();
		fragment.setArguments(args);

		return fragment;

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{

		userDayItem = (UserDayItem) getArguments().getSerializable(EXTRA_USER_DATA);

		View v = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_home_time_days, null);

		final TimePicker timePicker = (TimePicker) v
				.findViewById(R.id.dialog_home_time_days);
		timePicker.setAddStatesFromChildren(true);
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener()
		{

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
			{
				// TODO Auto-generated method stub

			}

		});

		return new AlertDialog.Builder(getActivity())
				.setView(v)
				.setTitle(R.string.date_time_home_commute)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog,
									int which)
							{

								homeCommuteStartTime = new GregorianCalendar();
								homeCommuteStartTime
										.clear(Calendar.HOUR_OF_DAY);
								homeCommuteStartTime.clear(Calendar.MINUTE);
								homeCommuteStartTime.set(Calendar.HOUR_OF_DAY,
										timePicker.getCurrentHour());
								homeCommuteStartTime.set(Calendar.MINUTE,
										timePicker.getCurrentMinute());
								sendResult(Activity.RESULT_OK);
							}
						}).create();
	}

	private void sendResult(int resultCode)
	{
		if (getTargetFragment() == null)
		{
			return;
		}

		Intent i = new Intent();
		userDayItem.setDriveToHomeTime(homeCommuteStartTime);

		userDayItem.getStartCommuteTime().setTimeZone(TimeZone.getDefault());
		Log.i(HOME_TIME_DIALOG, "sendResult "
				+ userDayItem.getDriveToHomeTime().toString());
		i.putExtra(EXTRA_USER_DATA, userDayItem);
		getTargetFragment().onActivityResult(getTargetRequestCode(),
				resultCode, i);
	}

}
