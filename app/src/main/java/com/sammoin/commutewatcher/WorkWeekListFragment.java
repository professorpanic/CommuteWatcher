package com.sammoin.commutewatcher;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
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

public class WorkWeekListFragment extends ListFragment
{
private UserWeek mWorkWeek;
UserDataAdapter adapter;

public static final int REQUEST_POSITION = 3;
public static final int REQUEST_NEW_COMMUTE = 4;
public static final String LIST_BUNDLE = "com.sammoin.commutewatcher.bundle";
static final String USER_INFO_FILE = "CommuteWatcher_user_info.txt";
private UserWeek savedUserInfo;
PassDayFromWeekListener mCallback;


    public interface PassDayFromWeekListener {
        public void passDayFromWeek(Bundle bundle);
    }

	public WorkWeekListFragment()
	{
		// TODO Auto-generated constructor stub
	}

    public void addDayToWeek(UserDay userDay)
    {
        mWorkWeek.set(userDay);
        adapter.notifyDataSetInvalidated();
    }


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
		//getActivity().setTitle(R.string.hello_world);
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
		adapter = new UserDataAdapter(mWorkWeek);
		
		setListAdapter(adapter);
        try {
            loadSavedInfo(USER_INFO_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.workweek_list_layout, container, false);
		ListView lv = (ListView)view.findViewById(android.R.id.list);
		
		//lv.setEmptyView(view.findViewById(android.R.id.empty));
		//Button addNewCommuteButton = (Button) view.findViewById(android.R.id.empty);
		registerForContextMenu(lv);
		
//		addNewCommuteButton.setOnClickListener(new View.OnClickListener()
//		{
//
//			@Override
//			public void onClick(View v)
//			{
//				//addNewCommute();
//			}
//		});
//
		return view;
	}
	
	
	@Override
	public void onResume()
	{
		
		super.onResume();
		//Collections.sort(mWorkWeek, new WorkWeekComparator());
		adapter = new UserDataAdapter(mWorkWeek);
		setListAdapter(adapter);


//		try
//		{
//			saveInfo();
//		}
//		catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		Log.i("WorkWeekListFragment", "onResume");
		
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		
		UserDay u = ((UserDataAdapter)(getListAdapter())).getItem(position);
		//Log.i("WORKWEEKLISTFRAGMENT", " " + u.toString() + position + "start " + u.getDayItemArrayList().get(0).getWorkAddress() + " end " + u.getDayItemArrayList().get(0).getHomeAddress());
		
		//TODO: progress. incorporate correct passing and returning of times and addresses. will need null handling too.

        //Intent i = new Intent();
        if (u!=null) {
            Bundle extras = new Bundle();
            extras.putInt(WorkDayListFragment.USER_DAY_POSITION, position);
            extras.putSerializable(WorkDayListFragment.USER_DAY_OBJECT, u);

            mCallback.passDayFromWeek(extras);
            //startActivityForResult(i, REQUEST_POSITION);
        }
		
	}
	
	//quick inner class for holding info in a row in a listadapter
	private class RowViewHolder
	{
		private TextView dayTextView;
		private TextView timeTextView;
		private CheckBox checkBox;
		
		public RowViewHolder()
		{
			
		}
		
		public RowViewHolder(TextView dayText, TextView timeText, CheckBox checkBox)
		{
			this.dayTextView=dayText;
			this.timeTextView=timeText;
			this.checkBox=checkBox;
		}

		public TextView getDayTextView()
		{
			return dayTextView;
		}

		public void setDayTextView(TextView dayTextView)
		{
			this.dayTextView = dayTextView;
		}

		public TextView getTimeTextView()
		{
			return timeTextView;
		}

		public void setTimeTextView(TextView timeTextView)
		{
			this.timeTextView = timeTextView;
		}

		public CheckBox getCheckBox()
		{
			return checkBox;
		}

		public void setCheckBox(CheckBox checkBox)
		{
			this.checkBox = checkBox;
		}
		
		
		
		
	}
	
	private class UserDataAdapter extends ArrayAdapter<UserDay>
	{
		public UserDataAdapter(UserWeek data)
		{
			super(getActivity().getApplicationContext(), 0, data.getWorkWeek());
			
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			CheckBox activeCheckBox;
			TextView dayTextView;
			TextView commuteTimesTextView;
			UserDay u = (UserDay) this.getItem(position);

            //test UDI
            //u.addItemToDay(new UserDayItem());
            Log.e("WorkWeekListFragment", "u.dayitemarraylist size is "+ u.getDayItemArrayList().size());



			
			
			if (convertView==null)
			{
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_workweek, null);
				
			
			
			
			dayTextView = (TextView)convertView.findViewById(R.id.workweek_list_item_dayTextView);
			dayTextView.setText(u.getDayOfTheWeek().toString());
			
			commuteTimesTextView = (TextView)convertView.findViewById(R.id.workweek_list_item_timeTextView);
                if (u.getDayItemArrayList().size() == 1) {
                    commuteTimesTextView.setText(u.getDayItemArrayList().size() + " trip");
                }
                else
                {
                    commuteTimesTextView.setText(u.getDayItemArrayList().size() + " trips");
                }
			
			activeCheckBox = (CheckBox)convertView.findViewById(R.id.workweek_list_item_activeCheckBox);
			activeCheckBox.setTag(u);
			activeCheckBox.setClickable(true);
			activeCheckBox.setChecked(u.isActive());
			
			convertView.setTag(new RowViewHolder(dayTextView, commuteTimesTextView, activeCheckBox));
			
			
			
			activeCheckBox.setOnClickListener(new View.OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					CheckBox cb = (CheckBox) v;
					UserDay dayFromList = (UserDay) cb.getTag();
					dayFromList.setActive(!dayFromList.isActive());
					cb.setChecked(dayFromList.isActive());
					
				}
			});
			}
			
			else
			{
				RowViewHolder viewHolder = (RowViewHolder) convertView.getTag();
				activeCheckBox = viewHolder.getCheckBox();
				dayTextView = viewHolder.getDayTextView();
				commuteTimesTextView = viewHolder.getTimeTextView();
			}
			return convertView;
		}
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode==getActivity().RESULT_OK)
		{
			if (requestCode==WorkWeekListFragment.REQUEST_POSITION)
			{
			//mWorkWeek.set(data.getIntExtra(TimeAndTravelFragment.WORKDAY_POSITION, 0), (UserDayItem) data.getSerializableExtra(TimeAndTravelFragment.DAY_LIST_ITEM));
			}
			
			else if (requestCode==WorkWeekListFragment.REQUEST_NEW_COMMUTE)
			{
				//mWorkWeek.add((UserDayItem) data.getSerializableExtra(TimeAndTravelFragment.DAY_LIST_ITEM));
			}
			
		}
		adapter = new UserDataAdapter(mWorkWeek);
		Log.e("WORKWEEKLISTFRAGMENT", "in onActivityResult. Do I even need to override this now?");
		setListAdapter(adapter);
		
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

//		CommuteCheckAlarmService.setServiceAlarm(getActivity(),
//				CommuteCheckAlarmService.isServiceAlarmOn(getActivity()),
//				savedUserInfo);

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
		
//			boolean turnAlarmOn = !CommuteCheckAlarmService
//					.isServiceAlarmOn(getActivity());
//			CommuteCheckAlarmService.setServiceAlarm(getActivity(),
//					turnAlarmOn, savedUserInfo);
//			Log.i("WORKWEEKLISTFRAGMENT", "alarm on has been clicked "+ turnAlarmOn);
			honeyCombOptionsInvalidate();
			return true;
		
		case R.id.action_save_all:
			Log.i("WORKWEEKLISTFRAGMENT", "save all");
			//addNewCommute();
			try {
				saveInfo();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		
		case R.id.action_delete_all:
			Log.i("WORKWEEKLISTFRAGMENT", "add delete all options item");
			mWorkWeek.clearAll();
			adapter.notifyDataSetChanged();
			adapter = new UserDataAdapter(mWorkWeek);
			setListAdapter(adapter);
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
		UserDataAdapter adapter = (UserDataAdapter)getListAdapter();
		UserDay ud = adapter.getItem(position);
		
		switch (item.getItemId())
		{
		case R.id.menu_item_delete_commute:
			
			ud.clear();

			adapter = new UserDataAdapter(mWorkWeek);
			setListAdapter(adapter);
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



}
