package com.sammoin.commutewatcher;

import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
			
			WorkWeekListFragment mainMenuFragment = new WorkWeekListFragment();
			
			
			// get a supportfragmentmanager reference and attach the fragment,
			// when the activity is created.
			getSupportFragmentManager().beginTransaction()
				.add(R.id.container, mainMenuFragment).commit();	
				//.add(R.id.container, userInfoEntryFragment).commit();
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

}
