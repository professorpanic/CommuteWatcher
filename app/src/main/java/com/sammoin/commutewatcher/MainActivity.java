package com.sammoin.commutewatcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements WorkWeekFragment.PassDayFromWeekListener, WorkDayListFragment.PassDayToWeekListener
{
    WorkWeekFragment mainMenuFragment;
	private Toolbar mToolbar;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);
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
			
			
			//MainFragment userInfoEntryFragment = new MainFragment();
			//userInfoEntryFragment.setArguments(getIntent().getExtras());
			
			 mainMenuFragment = new WorkWeekFragment();

			
			// get a supportfragmentmanager reference and attach the fragment,
            // when the activity is created.
			getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in,android.R.anim.fade_out)
                    .add(R.id.container, mainMenuFragment).commit();
				//.add(R.id.container, userInfoEntryFragment).commit();
		}

	}

	@Override
	public void passDayFromWeek(Bundle bundle) {

			WorkDayListFragment workDayListFragment = new WorkDayListFragment ();
			Bundle args = new Bundle();
			args.putAll(bundle);
			workDayListFragment.setArguments(args);
			getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in,android.R.anim.fade_out)
                    .replace(R.id.container, workDayListFragment)
					.addToBackStack(null)
					.commit();

	}

    @Override
    public void passDayToWeek(UserDay userDay)
    {
        //WorkWeekFragment workWeekListFragment = new WorkWeekFragment ();

        mainMenuFragment.addDayToWeek(userDay);
        getSupportFragmentManager().popBackStack();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent newIntent = new Intent(this, SettingsActivity.class);
                    newIntent.putExtra(SettingsActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.GeneralPreferenceFragment.class.getName());
            newIntent.putExtra(SettingsActivity.EXTRA_NO_HEADERS, true);
            startActivity(newIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

}


