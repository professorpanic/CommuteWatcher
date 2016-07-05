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
import android.view.View.OnClickListener;
import android.widget.TimePicker;

import org.joda.time.LocalTime;


public class WorkCommuteDayAndTimeFragment extends DialogFragment
{
	public static final String EXTRA_USER_DATA = "com.sammoin.commutewatcher.user";
	public static final String WORK_TIME_DIALOG = "work time dialog frgmt";

	
	private LocalTime commuteStartTime = new LocalTime();


	private UserDayItem userDayItem;
	private Day scheduledDay;



	public WorkCommuteDayAndTimeFragment()
	{
		// TODO Auto-generated constructor stub
	}

	public static WorkCommuteDayAndTimeFragment newInstance(UserDayItem user)
	{
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_USER_DATA, user);

		WorkCommuteDayAndTimeFragment fragment = new WorkCommuteDayAndTimeFragment();
		fragment.setArguments(args);
		
		return fragment;

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{

		userDayItem = (UserDayItem) getArguments().getSerializable(EXTRA_USER_DATA);

		if (userDayItem != null) {
			scheduledDay = userDayItem.getWorkDay();
		}

		View v = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_home_time_days, null);

		final TimePicker timePicker = (TimePicker) v
				.findViewById((R.id.dialog_home_time_days));
		timePicker.setAddStatesFromChildren(true);

		timePicker.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Log.i(WORK_TIME_DIALOG, "test");

			}

		});


		return new AlertDialog.Builder(getActivity())
				.setView(v)
				.setTitle(R.string.date_time_work_commute)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog,
									int which)
							{

								commuteStartTime = new LocalTime();
								commuteStartTime =commuteStartTime.hourOfDay().setCopy(timePicker.getCurrentHour());
                                commuteStartTime = commuteStartTime.minuteOfHour().setCopy(timePicker.getCurrentMinute());


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
		userDayItem.setWorkDay(scheduledDay);
		userDayItem.setStartCommuteTime(commuteStartTime, scheduledDay.get());


		//Log.i(WORK_TIME_DIALOG, "in sendResult "+ userDayItem.getStartCommuteTime().toString());
		i.putExtra(EXTRA_USER_DATA, userDayItem);
		getTargetFragment().onActivityResult(getTargetRequestCode(),
				resultCode, i);
	}
	


}
