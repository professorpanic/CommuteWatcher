package com.sammoin.commutewatcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimeAndTravelFragment extends Fragment
{
	private Button workCommuteTimeButton;
	private Button homeCommuteTimeButton;
	private TextView displayWorkCommute;
	private TextView displayHomeCommute;
	private Button saveCommuteInfoButton;
    private Button cancelButton;
	private EditText editWorkAddressText;
	private EditText editHomeAddressText;
	static final String USER_INFO_FILE = "user_info.txt";
	private UserDayItem savedUserInfo;
	private UserDayItem userDayItem;
    private UpdateTravelActivityTitleListener mTitleCallback;
	private int selectedDay;
	private int sqlRowId;
    private Tracker mTracker;
	public static final String DIALOG_WORK_COMMUTE = "to work";
	public static final String DIALOG_HOME_COMMUTE = "to home";
	public static final String MAIN_MENU = "main menu";
	public static final int REQUEST_WORK_COMMUTE = 0;
	public static final int REQUEST_HOME_COMMUTE = 1;
	public static final String DAY_LIST_ITEM = "com.sammoin.commutewatcher.user";
    public static final String NEW_ITEM_PASSED_DAY="com.sammoin.commutewatcher.workday_item";
	public static final String WORKDAY_POSITION = "com.sammoin.commutewatcher.position";
	public static final String WORKDAY = "com.sammoin.commutewatcher.day";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
		Bundle extras;
		extras = getActivity().getIntent().getBundleExtra(WorkDayListFragment.LIST_BUNDLE);

		userDayItem = new UserDayItem(getContext());
		if (extras.containsKey(WORKDAY_POSITION))
		{
            sqlRowId=extras.getInt(WORKDAY_POSITION);

		}
        if (extras.containsKey(DAY_LIST_ITEM))
        {
            userDayItem.copyUserData((UserDayItem)extras.getSerializable(DAY_LIST_ITEM));
            selectedDay = userDayItem.getWorkDay().get();
            mTitleCallback= (UpdateTravelActivityTitleListener)getActivity();
            mTitleCallback.updateTravelActivityTitle(selectedDay);

        }
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
	}

    public interface UpdateTravelActivityTitleListener
    {
        public void updateTravelActivityTitle(int in);
    }
    //testing out google analytics in here.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_main, container, false);

		displayWorkCommute = (TextView) v.findViewById(R.id.userWorkDateTimeTextView);
		editWorkAddressText = (EditText) v.findViewById(R.id.editWorkAddressTextView);
		editHomeAddressText = (EditText) v.findViewById(R.id.editHomeAddressTextView);
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa ZZZZ",Locale.getDefault());

		displayWorkCommute.setText(userDayItem.getStartCommuteTime().toString("hh:mm aa"));
		editWorkAddressText.setText(userDayItem.getWorkAddress());
		editHomeAddressText.setText(userDayItem.getHomeAddress());
				
		
		
		
		workCommuteTimeButton = (Button) v.findViewById(R.id.enterWorkDateTimeButton);
		workCommuteTimeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Time and Travel Frag")
                        .setAction("Enter Time Button")
                        .build());
                FragmentManager fm = getActivity().getSupportFragmentManager();
                WorkCommuteDayAndTimeFragment dialog = WorkCommuteDayAndTimeFragment
                        .newInstance(userDayItem);
                dialog.setTargetFragment(TimeAndTravelFragment.this,
                        REQUEST_WORK_COMMUTE);
                dialog.show(fm, DIALOG_WORK_COMMUTE);

            }
        });
		
		cancelButton = (Button) v.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Time and Travel Frag")
                        .setAction("Cancel button")
                        .build());
                getActivity().onBackPressed();
            }
        });

		saveCommuteInfoButton = (Button) v
				.findViewById(R.id.saveCommuteInfoButton);
		saveCommuteInfoButton.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Time and Travel Frag")
                        .setAction("save button")
                        .build());
				userDayItem.setHomeAddress(editHomeAddressText.getText().toString());
				userDayItem.setWorkAddress(editWorkAddressText.getText().toString());

				
				
				//if there's no selected day, that means is this a new item..
				Intent i = new Intent();
				if (selectedDay > -1)
				{
				i.putExtra(WORKDAY_POSITION, sqlRowId);
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

			if (requestCode == REQUEST_WORK_COMMUTE)
			{
				if (tempUser.getStartCommuteTime().toString() != null)
				{
					if (tempUser.getWorkDay() != null)
					{
						userDayItem.setWorkDay(tempUser.getWorkDay());
					}
					userDayItem.setStartCommuteTime(tempUser.getStartCommuteTime(), selectedDay);
					displayWorkCommute.setText(userDayItem.getStartCommuteTime().toString("hh:mm aa"));

				}
			}
		}
	}
}
