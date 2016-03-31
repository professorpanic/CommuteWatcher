package com.sammoin.commutewatcher;


import android.annotation.SuppressLint;
import android.content.Context;

import java.util.ArrayList;

@SuppressLint("UseSparseArrays")

public class WorkWeek
{
	private static ArrayList<UserDayItem> sOutboundWorkWeek;
	private static Context mAppContext;

	
	
	private WorkWeek(Context appContext)
	{
		mAppContext= appContext;
		
		
	}
	
	//this class and method is for spitting out an ArrayList full of blank userdata objects, one for each day of the week.
	public static ArrayList<UserDayItem> get(Context c)
	{
		
		
			
			ArrayList<UserDayItem> sOutboundWorkWeek = new ArrayList<UserDayItem>();
			/*
			for (int count = 0; count < 7; count++)
			{
				Log.i("WORKWEEK", "" + count);
				sOutboundWorkWeek.add(new UserDayItem());
			}
			
			*/
		
		
		return sOutboundWorkWeek;
	}

}
