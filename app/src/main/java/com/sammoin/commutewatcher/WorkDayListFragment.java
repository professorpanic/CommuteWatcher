package com.sammoin.commutewatcher;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.joda.time.LocalTime;

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
    WorkWeek mWorkWeek;
    UpdateTitleListener mCallback;
    private String mRowSelectionClause;
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
    FloatingActionButton mFAB;
    int dayInt=0;
    View view;



    private String[] mRowProjection =
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
        return new CursorLoader(getContext(), CONTENT_URI, mRowProjection, null, null, UserScheduleContract.USER_START_TIME + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data=getActivity().getContentResolver().query(
                UserScheduleContract.CONTENT_URI,   // The content URI of the words table
                mRowProjection,                        // The columns to return for each row
                mRowSelectionClause,                    // Selection criteria
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

    public interface UpdateTitleListener
    {
        public void updateTitle(int in);
    }

    private int getDayStringIDFromInt(int in)
    {
        switch (in) {
            case 1:
                return R.string.Sunday;

            case 2:
                return R.string.Monday;
            case 3:
                return R.string.Tuesday;
            case 4:
                return R.string.Wednesday;
            case 5:
                return R.string.Thursday;
            case 6:
                return R.string.Friday;
            case 7:
                return R.string.Saturday;
            default: return R.string.app_name;

        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        mCallback = (UpdateTitleListener)getActivity();
        mRowSelectionClause = UserScheduleContract.USER_WORKDAY +"= "+ args.getInt(WorkDayListFragment.USER_DAY_POSITION);

        setListAdapter(mCursorAdapter);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.workday_list_layout, container, false);
        mFAB= (FloatingActionButton)view.findViewById(R.id.addNewItemFAB);

        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewCommute();
            }
        });
        mCallback.updateTitle(getDayStringIDFromInt(getArguments().getInt(WorkDayListFragment.USER_DAY_POSITION)));


        registerForContextMenu(view);

        return view;
    }


    @Override
    public void onResume() {

        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        CommuteCheckAlarmService.setServiceAlarm(getContext(), preferences.getBoolean(getString(R.string.pref_enable_disable_key), false));


    }
//
//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//
//
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
//
//
//
//    }

    //quick inner class for holding info in a row in the cursoradapter
    private class RowViewHolder {
        private TextView startPointTextView;
        private TextView endPointTextView;
        private CheckBox activeCheckBox;
        private TextView startTimeTextView;

        public RowViewHolder(View view) {

            startPointTextView = (TextView) view.findViewById(R.id.commute_start_point_textview);
            endPointTextView = (TextView) view.findViewById(R.id.commute_end_point_textview);
            activeCheckBox = (CheckBox) view.findViewById(R.id.commute_item_activeCheckBox);
            startTimeTextView = (TextView) view.findViewById(R.id.commute_start_time_textview);


        }

        public RowViewHolder(TextView startpoint,TextView starttime, TextView endpoint, CheckBox checkBox) {
            this.startPointTextView = startpoint;
            this.startTimeTextView = starttime;
            this.endPointTextView = endpoint;
            this.activeCheckBox = checkBox;
        }

        public TextView getStartPointTextView() {
            return startPointTextView;
        }

        public void setStartPointTextView(TextView spTextView) {
            this.startPointTextView = spTextView;
        }

        public TextView getTimeTextView() {
            return startTimeTextView;
        }

        public TextView getEndPointTextView() {
            return endPointTextView;
        }

        public void setEndPointTextView(TextView endTextView) {
            this.startPointTextView = endTextView;
        }

        public void setTimeTextView(TextView timeTextView) {
            this.startTimeTextView = timeTextView;
        }

        public CheckBox getCheckBox() {
            return activeCheckBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.activeCheckBox = checkBox;
        }


    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == WorkDayListFragment.REQUEST_POSITION) {
                UserDayItem udi = ((UserDayItem) data.getSerializableExtra(TimeAndTravelFragment.DAY_LIST_ITEM));
                int row = data.getIntExtra(TimeAndTravelFragment.WORKDAY_POSITION, -1);
                ContentValues values = new ContentValues();

                values.put(UserScheduleContract.USER_START_ADDRESS, udi.getHomeAddress());
                values.put(UserScheduleContract.USER_END_ADDRESS, udi.getWorkAddress());
                values.put(UserScheduleContract.USER_WORKDAY, udi.getWorkDay().get());
                values.put(UserScheduleContract.USER_START_TIME, udi.getStartCommuteTime().getMillisOfDay());
                values.put(UserScheduleContract.USER_ITEM_ACTIVE, 1);
                getActivity().getContentResolver().update(
                        UserScheduleContract.CONTENT_URI,   // The content URI of the words table
                        values,                        // The columns to return for each row
                        UserScheduleContract._ID + "=" + row,                    // Selection criteria
                        null);
                //mWorkday.getDayItemArrayList().set(data.getIntExtra(TimeAndTravelFragment.WORKDAY_POSITION, 0), (UserDayItem) data.getSerializableExtra(TimeAndTravelFragment.DAY_LIST_ITEM));
            } else if (requestCode == WorkDayListFragment.REQUEST_NEW_COMMUTE) {
                //mWorkday.addItemToDay((UserDayItem) data.getSerializableExtra(TimeAndTravelFragment.DAY_LIST_ITEM));
            UserDayItem udi = ((UserDayItem) data.getSerializableExtra(TimeAndTravelFragment.DAY_LIST_ITEM));
                        ContentValues values = new ContentValues();

        values.put(UserScheduleContract.USER_START_ADDRESS, udi.getHomeAddress());
        values.put(UserScheduleContract.USER_END_ADDRESS, udi.getWorkAddress());
        values.put(UserScheduleContract.USER_WORKDAY, getArguments().getInt(WorkDayListFragment.USER_DAY_POSITION));
        values.put(UserScheduleContract.USER_START_TIME, udi.getStartCommuteTime().getMillisOfDay());
        values.put(UserScheduleContract.USER_ITEM_ACTIVE, 1);

                getActivity().getContentResolver().insert(UserScheduleContract.CONTENT_URI, values);

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



    private void deleteCommute(final long rowId) {
        if (rowId>0) {
            new AlertDialog.Builder(getContext())
                    //.setTitle(R.string.clear_day)
                    .setPositiveButton(R.string.delete_this_commute,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    String mClickSelectionClause = UserScheduleContract._ID + "= " + rowId;
                                    getActivity().getContentResolver().delete(
                                            UserScheduleContract.CONTENT_URI,
                                            mClickSelectionClause,
                                            null);
                                }
                            })
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    // just let it ride
                                }
                            })
                    .show();
        }
    }

    public void addNewCommute() {
        Intent i = new Intent(getActivity(), TimeAndTravelActivity.class);
        Bundle bundle = new Bundle();

        bundle.putInt(TimeAndTravelFragment.NEW_ITEM_PASSED_DAY, getArguments().getInt(WorkDayListFragment.USER_DAY_POSITION));
        i.putExtra(LIST_BUNDLE, bundle);
        startActivityForResult(i, REQUEST_NEW_COMMUTE);
    }


    class DayListBuilder extends CursorAdapter {
        public DayListBuilder(Context context, Cursor c) {
            super(context, c);
            Log.d("TAG", "DayListBuilder Create");
            mInflator=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            String endAddress= null;
            String startAddress=null;
            final boolean isActive=cursor.getInt(cursor.getColumnIndex(UserScheduleContract.USER_ITEM_ACTIVE))==1;
            long startTime=0;

            RowViewHolder viewHolder = (RowViewHolder) view.getTag();


            Log.d("TAG","DayListBuilder BindView");

                Log.d("TAG", "DayListBuilder cursor not nuill");
            viewHolder.endPointTextView.setText(cursor.getString(cursor.getColumnIndex(UserScheduleContract.USER_END_ADDRESS)));
            viewHolder.startPointTextView.setText(cursor.getString(cursor.getColumnIndex(UserScheduleContract.USER_START_ADDRESS)));
            startTime=cursor.getLong(cursor.getColumnIndex(UserScheduleContract.USER_START_TIME));
            viewHolder.startTimeTextView.setText(LocalTime.fromMillisOfDay(startTime).toString("hh:mm aa"));

            viewHolder.activeCheckBox.setChecked(isActive);

            final int rowId=cursor.getInt(cursor.getColumnIndex(UserScheduleContract._ID));

            viewHolder.activeCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ContentValues contentValues = new ContentValues();
                    if (isActive) {
                        contentValues.put(UserScheduleContract.USER_ITEM_ACTIVE, 0);

                    } else {
                        contentValues.put(UserScheduleContract.USER_ITEM_ACTIVE, 1);

                    }
                    getActivity().getContentResolver().update(
                            UserScheduleContract.CONTENT_URI,   // The content URI of the words table
                            contentValues,                        // The columns to return for each row
                            UserScheduleContract._ID + "=" + rowId,                    // Selection criteria
                            null);



                }
            });

            Log.d("TAG", "" + startTime);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mClickSelectionClause = UserScheduleContract._ID + "= " + rowId;

                    UserDayItem u = new UserDayItem(getContext());

                    Cursor data = getActivity().getContentResolver().query(
                            UserScheduleContract.CONTENT_URI,   // The content URI of the words table
                            mRowProjection,                        // The columns to return for each row
                            mClickSelectionClause,                    // Selection criteria
                            null,                     // Selection criteria
                            UserScheduleContract.USER_START_TIME + " ASC");
                    data.moveToFirst();
                    u.setWorkDay(data.getInt(data.getColumnIndex(UserScheduleContract.USER_WORKDAY)));
                    u.setActive(isActive);
                    u.setHomeAddress(data.getString(data.getColumnIndex(UserScheduleContract.USER_START_ADDRESS)));
                    u.setWorkAddress(data.getString(data.getColumnIndex(UserScheduleContract.USER_END_ADDRESS)));
                    u.setStartCommuteTime(data.getLong(data.getColumnIndex(UserScheduleContract.USER_START_TIME)));
                    data.close();


                    Intent i = new Intent(getActivity(), TimeAndTravelActivity.class);
                    Bundle extras = new Bundle();
                    extras.putSerializable(TimeAndTravelFragment.DAY_LIST_ITEM, u);
                    extras.putInt(TimeAndTravelFragment.WORKDAY_POSITION, rowId);
                    i.putExtra(LIST_BUNDLE, extras);
                    startActivityForResult(i, REQUEST_POSITION);

                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    deleteCommute(rowId);
                    return true;
                }
            });
            //startTimeTextView.setText(startTime);

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            Log.d("TAG", "CursorAdapter newView");
            View customListView=LayoutInflater.from(context).inflate(R.layout.list_item_workday, viewGroup, false);



            RowViewHolder viewHolder = new RowViewHolder(customListView);
            customListView.setTag(viewHolder);


            return customListView;
        }

    }


}


