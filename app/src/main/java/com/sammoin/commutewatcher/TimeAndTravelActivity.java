package com.sammoin.commutewatcher;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class TimeAndTravelActivity extends AppCompatActivity implements TimeAndTravelFragment.UpdateTravelActivityTitleListener
{
    private Toolbar mToolbar;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbar.setSubtitleTextColor(getResources().getColor(android.R.color.white));
        if (getActionBar()!=null)
        {
            getActionBar().setDisplayShowTitleEnabled(false);
        }
        setSupportActionBar(mToolbar);
		// checking to be sure that the fragment container has a fragment in it.
		if (findViewById(R.id.container) != null)
		{
			// if it does, we should check if's being restored from a previous
			// instance state.
			if (savedInstanceState != null)
			{
				return;
			}
			// otherwise, we'll make a new user info fragment. this is where the
			// user will primarily be setting their address and hours they work
			// into, grab any extras meant for it.
			
			
			TimeAndTravelFragment userInfoEntryFragment = new TimeAndTravelFragment();
			Bundle args = getIntent().getBundleExtra(WorkDayListFragment.LIST_BUNDLE);
            int dayInt = args.getInt(TimeAndTravelFragment.NEW_ITEM_PASSED_DAY);
            setTitleFromInt(dayInt);


			userInfoEntryFragment.setArguments(getIntent().getExtras());
			
			//WorkWeekFragment mainMenuFragment = new WorkWeekFragment();
			
			
			// get a supportfragmentmanager reference and attach the fragment,
			// when the activity is created.
			getSupportFragmentManager().beginTransaction()
				//.add(R.id.container, mainMenuFragment).commit();
					.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in,android.R.anim.fade_out)
				.add(R.id.container, userInfoEntryFragment).commit();
		}

	}
	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.main_actions, menu); return true; }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { // Handle
	 * action bar item clicks here. The action bar will // automatically handle
	 * clicks on the Home/Up button, so long // as you specify a parent activity
	 * in AndroidManifest.xml. int id = item.getItemId(); if (id ==
	 * R.id.action_settings) { return true; } return
	 * super.onOptionsItemSelected(item); }
	 */
	/**
	 * A placeholder fragment containing a simple view.
	 */

    private void setTitleFromInt(int in)
    {
        if (getSupportActionBar()!=null){
        switch (in) {
            case 1:
                getSupportActionBar().setTitle(R.string.Sunday);
                break;
            case 2:
                getSupportActionBar().setTitle(R.string.Monday);
                break;
            case 3:
                getSupportActionBar().setTitle(R.string.Tuesday);
                break;
            case 4:
                getSupportActionBar().setTitle(R.string.Wednesday);
                break;
            case 5:
                getSupportActionBar().setTitle(R.string.Thursday);
                break;
            case 6:
                getSupportActionBar().setTitle(R.string.Friday);
                break;
            case 7:
                getSupportActionBar().setTitle(R.string.Saturday);
                break;
            default: getSupportActionBar().setTitle(R.string.app_name);
                break;

            }
        }
    }

    @Override
    public void updateTravelActivityTitle(int in) {
        setTitleFromInt(in);
    }
}
