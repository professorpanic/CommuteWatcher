package com.sammoin.commutewatcher;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

//import android.support.v7.internal.widget.AdapterViewCompat.AdapterContextMenuInfo;

public class WorkDayListFragment extends ListFragment {
    private UserDay mWorkday;
    UserDayAdapter adapter;
    public static final int REQUEST_POSITION = 3;
    public static final int REQUEST_NEW_COMMUTE = 4;
    public static final String LIST_BUNDLE = "com.sammoin.commutewatcher.bundle";
    public static final String USER_DAY_OBJECT = "com.sammoin.commutewatcher.user";
    public static final String USER_DAY_POSITION = "com.sammoin.commutewatcher.user";
    private UserWeek savedUserInfo;
    PassDayToWeekListener mCallback;

    //test
    public WorkDayListFragment() {
        // TODO Auto-generated constructor stub
    }

    public interface PassDayToWeekListener {
        public void passDayToWeek(UserDay userDay);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        //just to initialize this thing or else it'll throw null errors when it's loading a userday.
        mWorkday = new UserDay();
        if (args != null) {
            mWorkday.copy((UserDay) args.getSerializable(USER_DAY_OBJECT));
        }
        mCallback = (PassDayToWeekListener)getActivity();
        System.out.println(mWorkday.toString());

        Log.i("ON CREATE UPDATE", " " + mWorkday);
        //UserDayItem test = mWorkday.get(6);
        //test.setWorkDay(Day.MONDAY);
        adapter = new UserDayAdapter(mWorkday);

        setListAdapter(adapter);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.workday_list_layout, container, false);
        ListView lv = (ListView) view.findViewById(android.R.id.list);

        //lv.setEmptyView(view.findViewById(android.R.id.empty));
        Button addNewCommuteButton = (Button) view.findViewById(R.id.addNewItemButton);
        Button saveDayButton = (Button) view.findViewById(R.id.saveDayButton);
        registerForContextMenu(lv);

        addNewCommuteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addNewCommute();
            }
        });

        saveDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.passDayToWeek(mWorkday);

            }
        });

        return view;
    }


    @Override
    public void onResume() {

        super.onResume();
        //Collections.sort(mWorkday, new WorkWeekComparator());
        adapter = new UserDayAdapter(mWorkday);
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
    public void onListItemClick(ListView l, View v, int position, long id) {

        UserDayItem u = ((UserDayAdapter) (getListAdapter())).getItem(position);
        Log.i("WORKDAYLISTFRAGMENT", " " + u.toString() + position + "start " + u.getWorkAddress() + " end " + u.getHomeAddress());

        //TODO: revamp WorkWeekListFragment to use Intent with OnResult
        Intent i = new Intent(getActivity(), TimeAndTravelActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable(TimeAndTravelFragment.DAY_LIST_ITEM, u);
        extras.putInt(TimeAndTravelFragment.WORKDAY_POSITION, position);
        i.putExtra(LIST_BUNDLE, extras);
        startActivityForResult(i, REQUEST_POSITION);

    }

    //quick inner class for holding info in a row in a listadapter
    private class RowViewHolder {
        private TextView dayTextView;
        private TextView timeTextView;
        private CheckBox checkBox;

        public RowViewHolder() {

        }

        public RowViewHolder(TextView dayText, TextView timeText, CheckBox checkBox) {
            this.dayTextView = dayText;
            this.timeTextView = timeText;
            this.checkBox = checkBox;
        }

        public TextView getDayTextView() {
            return dayTextView;
        }

        public void setDayTextView(TextView dayTextView) {
            this.dayTextView = dayTextView;
        }

        public TextView getTimeTextView() {
            return timeTextView;
        }

        public void setTimeTextView(TextView timeTextView) {
            this.timeTextView = timeTextView;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }


    }

    private class UserDayAdapter extends ArrayAdapter<UserDayItem> {
        public UserDayAdapter(UserDay data) {
            super(getActivity().getApplicationContext(), 0, data.getDayItemArrayList());

        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CheckBox activeCheckBox;
            TextView startAddressView;
            TextView endAddressTextView;
            TextView startTimeTextView;
            UserDayItem u = (UserDayItem) this.getItem(position);

            //test UDI
            //u.addItemToDay(new UserDayItem());
            Log.e("WorkWeekListFragment", "u.dayitemarraylist size is " + u.toString());

            //UserDayItem uDi = u.getDayItemArrayList().get(position);


            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_workday, null);


                //using day item 0 as a test
//                startAddressView = (TextView) convertView.findViewById(R.id.workday_startpoint_TextView);
//                startAddressView.setText(u.getHomeAddress());

                endAddressTextView = (TextView) convertView.findViewById(R.id.workday_endpoint_timeTextView);
                if (u != null) {
                    endAddressTextView.setText(u.getWorkAddress());
                }


                activeCheckBox = (CheckBox) convertView.findViewById(R.id.workday_list_item_activeCheckBox);
                activeCheckBox.setTag(u);
                activeCheckBox.setClickable(true);
                activeCheckBox.setChecked(u.isActive());

                startTimeTextView = (TextView) convertView.findViewById(R.id.workday_starttime_TextView);
                startTimeTextView.setText(u.getStartCommuteTime().toString());


                convertView.setTag(new RowViewHolder(startTimeTextView, endAddressTextView, activeCheckBox));


                activeCheckBox.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        UserDayItem dayFromList = (UserDayItem) cb.getTag();
                        dayFromList.setActive(!dayFromList.isActive());
                        cb.setChecked(dayFromList.isActive());

                    }
                });
            } else {
                RowViewHolder viewHolder = (RowViewHolder) convertView.getTag();
                activeCheckBox = viewHolder.getCheckBox();
                startTimeTextView = viewHolder.getDayTextView();
                endAddressTextView = viewHolder.getTimeTextView();
            }
            return convertView;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == WorkDayListFragment.REQUEST_POSITION) {
                mWorkday.getDayItemArrayList().set(data.getIntExtra(TimeAndTravelFragment.WORKDAY_POSITION, 0), (UserDayItem) data.getSerializableExtra(TimeAndTravelFragment.DAY_LIST_ITEM));
            } else if (requestCode == WorkDayListFragment.REQUEST_NEW_COMMUTE) {
                mWorkday.addItemToDay((UserDayItem) data.getSerializableExtra(TimeAndTravelFragment.DAY_LIST_ITEM));
            }

        }
        adapter = new UserDayAdapter(mWorkday);

        setListAdapter(adapter);

    }


//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.main_actions, menu);
//
//    }
//
//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//
//        MenuItem toggleItem = menu.findItem(R.id.action_alarm_toggle);
//        getActivity().invalidateOptionsMenu();
//
//        if (CommuteCheckAlarmService.isServiceAlarmOn(getActivity())) {
//            toggleItem.setTitle(R.string.action_turn_checker_off);
//
//        } else {
//            toggleItem.setTitle(R.string.action_turn_checker_on);
//
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        switch (item.getItemId()) {
//
//            case R.id.action_settings:
//                return true;
//
//            case R.id.action_alarm_toggle:
//
//			boolean turnAlarmOn = !CommuteCheckAlarmService
//					.isServiceAlarmOn(getActivity());
//			CommuteCheckAlarmService.setServiceAlarm(getActivity(),
//					turnAlarmOn, savedUserInfo);
//			Log.i("WORKWEEKLISTFRAGMENT", "alarm on has been clicked "+ turnAlarmOn);
//                honeyCombOptionsInvalidate();
//                return true;
//
//            case R.id.action_add_new:
//                Log.i("WORKDAYLISTFRAGMENT", "add new options item");
//                addNewCommute();
//                adapter = new UserDayAdapter(mWorkday);
//                setListAdapter(adapter);
//                return true;
//
//		case R.id.action_delete_all:
//			Log.i("WORKWEEKLISTFRAGMENT", "add delete all options item");
//			for (UserDayItem ud : mWorkday)
//			{
//				ud.clear();
//			}
//            adapter.notifyDataSetChanged();
//			adapter = new UserDayAdapter(mWorkday);
//			setListAdapter(adapter);
//			return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.commute_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        UserDayAdapter adapter = (UserDayAdapter) getListAdapter();
        UserDayItem ud = adapter.getItem(position);

        switch (item.getItemId()) {
            case R.id.menu_item_delete_commute:

                mWorkday.getDayItemArrayList().remove(position);
                //Collections.sort(mWorkday, new WorkWeekComparator());
                adapter = new UserDayAdapter(mWorkday);
                setListAdapter(adapter);
                return true;

        }


        return super.onContextItemSelected(item);
    }

    public void addNewCommute() {
        Intent i = new Intent(getActivity(), TimeAndTravelActivity.class);
        Bundle bundle = new Bundle();
        UserDayItem u = new UserDayItem();
        u.setWorkDay(mWorkday.getDayOfTheWeek());
        bundle.putSerializable(TimeAndTravelFragment.DAY_LIST_ITEM, u);
        i.putExtra(LIST_BUNDLE, bundle);
        startActivityForResult(i, REQUEST_NEW_COMMUTE);
    }

    public void honeyCombOptionsInvalidate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            getActivity().invalidateOptionsMenu();
        }
    }


}
