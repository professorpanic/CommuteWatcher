package com.sammoin.commutewatcher;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

//import android.support.v7.internal.widget.AdapterViewCompat.AdapterContextMenuInfo;

public class WorkDayListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private UserDay mWorkday;
    ListAdapter adapter;
    private SimpleCursorAdapter mCursorAdapter;
    private Cursor mCursor;
    private static final String TAG = "WDLF";
    public static final int REQUEST_POSITION = 3;
    public static final int REQUEST_NEW_COMMUTE = 4;
    public static final String LIST_BUNDLE = "com.sammoin.commutewatcher.bundle";
    public static final String USER_DAY_OBJECT = "com.sammoin.commutewatcher.user";
    public static final String USER_DAY_POSITION = "com.sammoin.commutewatcher.user";
    private static final int DAY_LOADER = 0;
    private UserWeek savedUserInfo;
    PassDayToWeekListener mCallback;
    private String mSelectionClause;
    private TextView startPointTextView;
    private TextView endPointTextView;
    private CheckBox activeCheckBox;
    private TextView startTimeTextView;
    private LayoutInflater mInflator;
    private DayListBuilder mDayListBuilder;
    public static final int COL_END_POINT = 0;
    public static final int COL_ACTIVE = 1;
    public static final int COL_START_POINT = 2;
    public static final int COL_START_TIME = 3;
    public static final int COL_WORKDAY_NUM = 4;
    public static final int COL_ID= 5;


    private String[] mProjection =
            {
                    UserScheduleContract.USER_END_ADDRESS,
                    UserScheduleContract.USER_ITEM_ACTIVE,
                    UserScheduleContract.USER_START_ADDRESS,
                    UserScheduleContract.USER_START_TIME,
                    UserScheduleContract.USER_WORKDAY,
                    UserScheduleContract._ID
            };






    //test
    public WorkDayListFragment() {
        // TODO Auto-generated constructor stub
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


        getLoaderManager().initLoader(DAY_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri CONTENT_URI = UserScheduleContract.CONTENT_URI;
        return new CursorLoader(getContext(), CONTENT_URI, mProjection, null, null, UserScheduleContract.USER_START_TIME + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data=getActivity().getContentResolver().query(
                UserScheduleContract.CONTENT_URI,   // The content URI of the words table
                mProjection,                        // The columns to return for each row
                mSelectionClause,                    // Selection criteria
                null,                     // Selection criteria
                UserScheduleContract.USER_START_TIME + " ASC");
        mDayListBuilder=new DayListBuilder(getContext(), data);
        setListAdapter(mDayListBuilder);



    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mDayListBuilder.swapCursor(null);

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
        mSelectionClause = UserScheduleContract.USER_WORKDAY +"= "+ args.getInt(WorkDayListFragment.USER_DAY_POSITION);

        setListAdapter(mCursorAdapter);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.workday_list_layout, container, false);



        Bundle args = getArguments();



        return view;
    }


    @Override
    public void onResume() {

        super.onResume();


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

//        UserDayItem u = ((UserDayAdapter) (getListAdapter())).getItem(position);
//        Log.i(TAG, " " + u.toString() + position + "start " + u.getWorkAddress() + " end " + u.getHomeAddress());
//
//        //TODO: revamp WorkWeekFragment to use Intent with OnResult
//        Intent i = new Intent(getActivity(), TimeAndTravelActivity.class);
//        Bundle extras = new Bundle();
//        extras.putSerializable(TimeAndTravelFragment.DAY_LIST_ITEM, u);
//        extras.putInt(TimeAndTravelFragment.WORKDAY_POSITION, position);
//        i.putExtra(LIST_BUNDLE, extras);
//        startActivityForResult(i, REQUEST_POSITION);

    }

    //quick inner class for holding info in a row in a listadapter
//    private class RowViewHolder {
//        private TextView dayTextView;
//        private TextView timeTextView;
//        private CheckBox checkBox;
//
//        public RowViewHolder() {
//
//        }
//
//        public RowViewHolder(TextView dayText, TextView timeText, CheckBox checkBox) {
//            this.dayTextView = dayText;
//            this.timeTextView = timeText;
//            this.checkBox = checkBox;
//        }
//
//        public TextView getDayTextView() {
//            return dayTextView;
//        }
//
//        public void setDayTextView(TextView dayTextView) {
//            this.dayTextView = dayTextView;
//        }
//
//        public TextView getTimeTextView() {
//            return timeTextView;
//        }
//
//        public void setTimeTextView(TextView timeTextView) {
//            this.timeTextView = timeTextView;
//        }
//
//        public CheckBox getCheckBox() {
//            return checkBox;
//        }
//
//        public void setCheckBox(CheckBox checkBox) {
//            this.checkBox = checkBox;
//        }
//
//
//    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == WorkDayListFragment.REQUEST_POSITION) {
                mWorkday.getDayItemArrayList().set(data.getIntExtra(TimeAndTravelFragment.WORKDAY_POSITION, 0), (UserDayItem) data.getSerializableExtra(TimeAndTravelFragment.DAY_LIST_ITEM));
            } else if (requestCode == WorkDayListFragment.REQUEST_NEW_COMMUTE) {
                mWorkday.addItemToDay((UserDayItem) data.getSerializableExtra(TimeAndTravelFragment.DAY_LIST_ITEM));
            }

        }




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
//			Log.i(TAG, "alarm on has been clicked "+ turnAlarmOn);
//                honeyCombOptionsInvalidate();
//                return true;
//
//            case R.id.action_add_new:
//                Log.i(TAG, "add new options item");
//                addNewCommute();
//                adapter = new UserDayAdapter(mWorkday);
//                setListAdapter(adapter);
//                return true;
//
//		case R.id.action_delete_all:
//			Log.i(TAG, "add delete all options item");
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
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        int position = info.position;
//        UserDayAdapter adapter = (UserDayAdapter) getListAdapter();
//        UserDayItem ud = adapter.getItem(position);
//
//        switch (item.getItemId()) {
//            case R.id.menu_item_delete_commute:
//
//                mWorkday.getDayItemArrayList().remove(position);
//                //Collections.sort(mWorkday, new WorkWeekComparator());
//                adapter = new UserDayAdapter(mWorkday);
//                setListAdapter(adapter);
//                return true;
//
//        }


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
    class DayListBuilder extends CursorAdapter {
        public DayListBuilder(Context context, Cursor c) {
            super(context, c);
            Log.d("TAG", "DayListBuilder Create");
            mInflator=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public void bindView(View arg0, Context arg1, Cursor cursor) {
            String endAddress= null;
            String startAddress=null;
            boolean isActive=false;
            long startTime=0;
            Log.d("TAG","DayListBuilder BindView");
            if(null!=cursor){
                Log.d("TAG","DayListBuilder cursor not nuill");
                endAddress=cursor.getString(cursor.getColumnIndex(UserScheduleContract.USER_END_ADDRESS));
                startAddress=cursor.getString(cursor.getColumnIndex(UserScheduleContract.USER_START_ADDRESS));
                startTime=cursor.getLong(cursor.getColumnIndex(UserScheduleContract.USER_START_TIME));
                if (cursor.getInt(cursor.getColumnIndex(UserScheduleContract.USER_ITEM_ACTIVE))==1)
                {
                    isActive=true;
                }
            }

            startPointTextView = (TextView) arg0.findViewById(R.id.commute_start_point_textview);
            startPointTextView.setText(startAddress);
            endPointTextView= (TextView) arg0.findViewById(R.id.commute_end_point_textview);
            endPointTextView.setText(endAddress);
            activeCheckBox= (CheckBox) arg0.findViewById(R.id.commute_item_activeCheckBox);
            activeCheckBox.setChecked(isActive);


            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa ZZZZ",
                    Locale.getDefault());



            startTimeTextView = (TextView) arg0.findViewById(R.id.commute_start_time_textview);
            startTimeTextView.setText(sdf.format(startTime));
            Log.d("TAG", ""+startTime);
            //startTimeTextView.setText(startTime);

        }

        @Override
        public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
            Log.d("TAG","CursorAdapter newView");
            View customListView=mInflator.inflate(R.layout.list_item_workday,null);
            return customListView;
        }

    }
}


